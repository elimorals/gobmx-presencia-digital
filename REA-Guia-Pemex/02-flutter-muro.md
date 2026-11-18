# Arquitectura Flutter y `.env` con secretos

## Confirmación de pila
- `assets/flutter_assets/` presente (`.env`, `AssetManifest.json/.bin`, `FontManifest`, `NOTICES.Z`,
  `NativeAssetsManifest.json`); `libapp.so` (Dart AOT) + `libflutter.so` en el split de
  arquitectura (el xapk descargado trae `config.armeabi_v7a`). El DEX es el embedding de Flutter.
- Flutter 3.x. No hay `.dll`/assemblies (.NET), ni `il2cpp` (Unity), ni bundle web.

## Por qué es un muro
La lógica (consulta de estaciones, auth, uso de ubicación) se compila en el **snapshot Dart AOT**
dentro de `libapp.so`. JADX solo ve el host Flutter. Recuperarla exige **reflutter/blutter** o
**Frida** (dinámico, fuera de alcance estático). Se documenta el muro y lo recuperable del
bundle (el `.env`), sin inventar la lógica.

## `assets/flutter_assets/.env` (en claro) — el hallazgo central
`flutter_dotenv` empaqueta el `.env` como asset de texto plano, **legible** en el APK. Contenido
(valores reales SOLO en `evidence/flutter.env`, no versionado):

```
APP_SERVICES_ENDPOINT=https://api-cloud.pemex.com/guiapemex
APP_SERVICES_API_KEY=<clave del backend de Pemex>
GOOGLE_MAPS_API_KEY=<clave>
TOM_TOM_KEY=<clave>
APPSINSIGHTS_INSTRUMENTATION_KEY=<...;IngestionEndpoint=...applicationinsights.azure.com...>
COLOR_MAP_WEB_KEY=<clave>
COLOR_MAP_MOBILE_KEY=<clave>
```

- **`APP_SERVICES_API_KEY`** es el más serio: es la **credencial de acceso al backend propio de
  Pemex** (`api-cloud.pemex.com/guiapemex`), no una clave pública por diseño. Cualquiera con el
  APK la recupera y puede usarla contra el backend (doc 05).
- **`GOOGLE_MAPS_API_KEY` / `TOM_TOM_KEY`**: llaves de mapas. Su riesgo depende de si están
  restringidas (package + SHA-1 de firma, o referer/IP). Si no, el consumo corre a cargo de Pemex.
- **`APPSINSIGHTS…`**: clave de ingestión de telemetría (baja sensibilidad; permite contaminar
  telemetría, no leer datos). **ColorMap**: llaves del proveedor de mapas de color.

> `flutter_dotenv` NO cifra el `.env`. Poner una API key real ahí equivale a publicarla con el
> APK. El patrón correcto es no embeber secretos en el cliente (que el backend los exija por un
> flujo autenticado, o usar claves restringidas y rotables).
