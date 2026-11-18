# Arquitectura Flutter y `.env` embebido

## Confirmación de pila (Flutter)

- `assets/flutter_assets/` presente: `AssetManifest.json`, `FontManifest.json`,
  `NativeAssetsManifest.json`, imágenes (`t_bienvenida` en densidades 1.5x–4.0x) y `.env`.
- `libapp.so` (snapshot Dart AOT) + `libflutter.so` (engine) en el split
  `config.armeabi_v7a.apk` (por eso el triage sobre el APK base la marcó "nativa": las `.so` no
  están en el base).
- El DEX es el **embedding** de Flutter (`FlutterActivity`, plugins) + **ExoPlayer** (doc 03).

## Por qué es un muro

La lógica de negocio de una app Flutter se compila a código máquina en el **snapshot Dart AOT**
dentro de `libapp.so`. No hay bytecode Java/Kotlin legible con la lógica; JADX solo ve el
embedding. Recuperarla requiere:
- herramientas específicas de Flutter (**reflutter**, **blutter**) que reconstruyen símbolos del
  snapshot, o
- **análisis dinámico** (Frida sobre el runtime Dart).

Ambas son **dinámicas / fuera del alcance** estático de este repositorio. Mismo veredicto que
SAT Móvil, Urbani y Mi Policía. Se documenta el muro, no se inventa su contenido.

## `assets/flutter_assets/.env` (en claro)

La app usa `flutter_dotenv`: un archivo `.env` empaquetado como asset y leído en runtime. Queda
**legible** dentro del APK. Contenido (valor de la clave **solo** en `evidence/flutter.env`, no
versionado):

```
APP_SERVICES_ENDPOINT=https://api-cloud.pemex.com/asiste
APPSINSIGHTS_INSTRUMENTATION_KEY=<clave Azure Application Insights>
```

- **`APP_SERVICES_ENDPOINT`** — el backend único de la app (doc 03). No es secreto; es el host
  de la API.
- **`APPSINSIGHTS_INSTRUMENTATION_KEY`** — clave de **ingestión de telemetría** de Azure
  Application Insights (doc 05). No permite leer datos, sí enviar/contaminar telemetría a la
  cuenta de Pemex. Baja sensibilidad, pero no debería viajar en el cliente.

> `flutter_dotenv` **no** cifra el `.env`: es texto plano en los assets. Cualquier cosa que se
> ponga ahí (una API key real, un secreto) quedaría expuesta. Aquí solo hay el endpoint y la
> clave de telemetría; no se halló un secreto de alto impacto.
