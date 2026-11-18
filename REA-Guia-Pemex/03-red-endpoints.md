# Red y endpoints

Recuperado del `.env` (doc 02); el detalle de rutas vive en el snapshot Dart (muro).

## Backend
- **`https://api-cloud.pemex.com/guiapemex`** (`APP_SERVICES_ENDPOINT`). HTTPS, dominio oficial
  de Pemex. **Mismo host raíz que Pemex ASISTE** (`api-cloud.pemex.com/asiste`) → `api-cloud` es
  la API-cloud compartida de Pemex, con una ruta por app.
- Acceso autenticado con `APP_SERVICES_API_KEY` (en claro en el `.env`, doc 02/05). Las rutas
  concretas bajo `/guiapemex` no son observables (snapshot Dart).

## Mapas
- **Google Maps** (`GOOGLE_MAPS_API_KEY`) y **TomTom** (`TOM_TOM_KEY`) para el mapa de
  estaciones. **ColorMap** (`COLOR_MAP_*_KEY`) como proveedor de capas/estilo.

## Telemetría
- **Azure Application Insights** (`APPSINSIGHTS_INSTRUMENTATION_KEY`), ingestión en
  `eastus-8.in.applicationinsights.azure.com`.

## Transporte (postura)
- **HTTPS** en backend y mapas; **sin** `usesCleartextTraffic`; sin endpoints `http://` propios
  en el DEX/assets (los `http://` presentes son namespaces de librerías, ajenos). Transporte sano.
- **Pinning:** no verificable (capa de red en Dart). Límite.
