# Resumen ejecutivo — Mi Policía (CDMX) 3.2.5

## Identidad

| Campo | Valor |
|---|---|
| App | Mi Policía (CDMX) |
| Package | `com.moobky.MiPolicia` |
| Versión | 3.2.5 (`versionName`, aapt2) |
| Dependencia | Secretaría de Seguridad Ciudadana de la CDMX (SSC) |
| Instalaciones | ≈1 M+ (Google Play), #12 del inventario |
| Firma | `CN=Saul Espinosa Ruiz` (desarrollador individual; firma original preservada) |
| SHA-256 base | `5092a09aa5844ecdfbec331534e87ceed5dcc0f8e2d073b31e4e624b1cb9b41e` |

## Pila (confirmada, corrige el triage)

**Flutter** (Dart AOT). Evidencia:
- `assets/flutter_assets/` en el APK base.
- `lib/arm64-v8a/libapp.so` (8 258 480 bytes, snapshot Dart) + `libflutter.so` (10.5 MB) en el split
  `config.arm64_v8a.apk`.
- DEX = **solo embedding**: `search_android_classes(query="com.moobky")` sobre 3 948 clases devuelve
  **únicamente** `com.moobky.MiPolicia.R`. No hay `MainActivity` propia ni lógica de red/cripto en el
  DEX; el punto de entrada es `io.flutter.embedding` (vía manifiesto).

Consecuencia metodológica: el análisis a fondo se hace sobre `libapp.so` (cadenas, endpoints,
símbolos de cripto), no sobre el DEX. La lógica de negocio en Dart es el **muro** (doc 08).

## Función de la app

Cuadrantes y contacto con la policía de la CDMX, reglamento de tránsito (consulta de placas,
artículos, cuadrante por ubicación), ubicación en tiempo real, llamada directa de emergencia,
escaneo de códigos y contenido informativo (WebView). Los plugins Flutter lo confirman:
`background_location`, `geolocator`, `google_maps_flutter`, `flutter_phone_direct_caller`,
`flutter_barcode_scanner`, `flutter_inappwebview`, `http` (doc 05).

## Backend SSC

Todas las APIs sobre **HTTPS**, en el dominio `*.ssc.cdmx.gob.mx` (doc 03):
`api-p.ssc.cdmx.gob.mx` (API principal + reglamento de tránsito), `cuadrantes.ssc.cdmx.gob.mx`,
`cms.ssc.cdmx.gob.mx`, `ucs.ssc.cdmx.gob.mx:8443`, `data.ssp.cdmx.gob.mx` (un `.xlsx` por `http://`,
latente). Mapas vía HERE y Google Maps.

## Hallazgos (6, priorizados en doc 07)

1. Token `Bearer` estático + token de API en la ruta de cuadrantes, hardcodeados en `libapp.so`.
2. App de seguridad firmada por una persona, no por la SSC.
3. Permiso `DUMP` (signature|privileged) declarado + ubicación en segundo plano.
4. Superficie de API de la SSC expuesta (rutas de placas/cuadrantes/tránsito).
5. Indicio de pinning a la raíz DigiCert G2 (cert embebido + primitivas Dart; cableado en el snapshot).
6. Postura por lo demás razonable (HTTPS en todo, sin cleartext habilitado, permisos acotados).

## Qué queda fuera

El manejo exacto de la PII del ciudadano (qué envía cada pantalla, cómo se almacena localmente) vive
en el snapshot Dart y no se recuperó a código fuente. Se documenta la forma de los endpoints y los
símbolos presentes, no el flujo interno (doc 08).
