# 02 · Red y backend

## Backend Firebase/GCP

- Proyecto **Firebase**: `boton-de-auxilio-escudo-urbano.appspot.com` (string hallado en
  `libapp.so`). Recursos de Firebase presentes: `firebase-analytics.properties`,
  `firebase-measurement-connector.properties`, `res/raw/firebase_common_keep.xml`.
- **Firebase API key** (`AIzaSy…`) embebida en recursos (estándar en apps con GMS/Firebase).
- Contacto `gmail.com` embebido en `libapp.so`.

## Endpoints de aplicación (cifrados en el `.env`)

El `.env` (ver doc 03) define los endpoints de la API propia como **valores cifrados / base64**, no
como URLs en claro:

- `API_EVENTOS` / `API_USUARIOS` — endpoints de **producción** (mismo blob en ambos).
- `API_EVENTOS_PRUEBAS` / `API_PRUEBAS` — endpoints de **prueba** (mismo blob en ambos).
- `ID_APP` — identificador de app (cifrado/base64).

Como los valores están cifrados y la lógica de descifrado vive en el Dart AOT (`libapp.so`, muro),
**no se pudo resolver el host real de la API propia por análisis estático**. Lo honesto: sé que hay
endpoints prod y prueba y un `ID_APP`, pero **no** su URL en claro.

## Muro

La capa de red real (qué manda el botón de pánico, con qué headers, a qué host tras descifrar) está
en el Dart AOT. Confirmarlo requiere análisis dinámico (Frida/reflutter con dispositivo).
