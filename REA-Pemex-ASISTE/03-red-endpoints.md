# Red y endpoints

## Backend

- **`https://api-cloud.pemex.com/asiste`** (`APP_SERVICES_ENDPOINT` del `.env`, doc 02).
  HTTPS, dominio oficial de Pemex (`api-cloud.pemex.com`). Es el **único backend** de la app.
- Las rutas concretas bajo `/asiste` (auth, estaciones, perfil, etc.) viven en el snapshot Dart
  (`libapp.so`) → no observables estáticamente. Se documenta el host, no el detalle del API.

## Telemetría

- Azure **Application Insights** (la *instrumentation key* del `.env`, doc 02/05). El tráfico de
  telemetría va a los endpoints de ingestión de Azure Monitor.

## Multimedia (ExoPlayer)

El DEX trae los marcadores de **ExoPlayer/DASH** (`dashif.org`, `aomedia.org/emsg`, HLS de
Apple) → la app reproduce **video** (contenido de Pemex). Son strings de la librería, no
endpoints de Pemex.

## Transporte (postura)

- **HTTPS** en el único backend; **sin** `usesCleartextTraffic`; **sin** endpoints `http://`
  propios en el DEX ni en los assets (los `http://` presentes son de DASH-IF/Adobe/mozilla,
  ajenos). Transporte **sano**.
- **Pinning:** no verificable estáticamente (la capa de red es Dart, en `libapp.so`). Se marca
  como límite; la confianza, por defecto, recae en las CAs del sistema.
