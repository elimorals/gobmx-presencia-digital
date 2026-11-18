# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se usó ninguna clave ni se
llamó a ningún endpoint.

## H1 (Medio-Alto) — API key del backend de Pemex en claro
`assets/flutter_assets/.env` → `APP_SERVICES_API_KEY=<clave>` junto al endpoint
`api-cloud.pemex.com/guiapemex` (doc 02). Es la **credencial de acceso al backend propio de
Pemex**, recuperable de cualquier copia del APK — **no** una clave pública por diseño. Un tercero
puede usarla contra la API de Pemex. **Fix:** no embeber la clave en el cliente (exigirla por un
flujo autenticado del lado servidor); mientras tanto, **rotarla** y restringir por origen.

## H2 (Bajo-Medio) — Llaves de Google Maps y TomTom embebidas
`GOOGLE_MAPS_API_KEY` y `TOM_TOM_KEY` en el `.env` (doc 02/03). Riesgo = abuso de cuota a cargo
de Pemex **si no están restringidas** (package + SHA-1 de firma para Android; referer/IP). **Fix:**
restringir las llaves a esta app y rotarlas.

## H3 (Bajo) — Telemetría y ColorMap en claro
`APPSINSIGHTS_INSTRUMENTATION_KEY` (ingestión de telemetría; permite contaminar, no leer) y
`COLOR_MAP_*_KEY` en el `.env` (doc 02).

## Positivos
- **P1** Transporte **HTTPS** (backend + mapas), sin cleartext (doc 03).
- **P2** Permisos **mínimos** (ubicación + internet; sin cámara/contactos/SMS) (doc 01).
- **P3** Firma **auténtica de Pemex** → binario íntegro (doc 04).

## Límites (el muro, sin inventar)
- **Lógica Dart (Flutter):** en `libapp.so`, no decompilable estáticamente (doc 02). Rutas de API
  bajo `/guiapemex`, auth y uso de la ubicación = desconocidos sin análisis dinámico
  (reflutter/blutter/Frida), fuera de alcance.
- **Restricción de las llaves de mapas:** no verificable desde el APK (se configura del lado del
  proveedor) → el impacto real de H2 depende de esa configuración; se reporta la exposición, no
  el abuso confirmado.
- **Pinning TLS:** no observable (capa de red en Dart).

## Recomendación
Dado que se exponen **credenciales activas** (API key del backend + llaves de mapas),
**divulgación responsable a Pemex** y **rotación** de las claves. Los valores reales se mantienen
solo en `evidence/` (no versionado), no se redistribuyen.
