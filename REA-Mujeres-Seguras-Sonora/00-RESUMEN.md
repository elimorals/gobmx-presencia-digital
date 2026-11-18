# 00 · Resumen ejecutivo — Mujeres Seguras (Sonora)

## Identidad

| Campo | Valor |
|---|---|
| App | Mujeres Seguras |
| Package | `mx.gob.segobsonora.mujersegura` (verificado con `inspect_android_package`) |
| versionName / versionCode | 2.0.3 / 20003 |
| minSdk / targetSdk | 21 / 30 |
| Firma | **re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica**; cruzar contra Play oficial de SEGOB Sonora |
| sha256 APK | `e660ddf55c9b9a20dd793b20af8f5751927715d48dcc92b6b09d7962e879b559` |
| Clases / recursos | 4 423 / 2 415 |
| Dueño | Secretaría de Seguridad Pública de Sonora (SSP Sonora) |

## Pila tecnológica

- **Cordova** (`assets/public/cordova.js`, `cordova_plugins.js`) con bundle JS/Ionic-Angular
  **legible, no ofuscado**. Shell nativo delgado; el DEX es mayormente librerías (logback, slf4j,
  SmiSDK de video).
- Backend REST/SOAP de la SSP Sonora.

## Veredicto de seguridad

**La hipótesis "pánico→cleartext" NO se confirma aquí** (contraejemplo). El `SendLocation` por
**HTTP en claro** (`http://apps.sspsonora.gob.mx/wsMS/Services.svc/SendLocation`) está **comentado**
(`//url`/`//syncUrl`) en el bundle; el tráfico **activo es HTTPS** a `servicios.sspsonora.gob.mx`.
A diferencia de Veracruz Mujer Alerta, aquí la ubicación **sí** va cifrada.

Hallazgo real: el **endpoint activo es `mstest2`** (de prueba) en el build de producción (CWE-489).

## Hallazgos

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | Backend activo = endpoint de **prueba** (`/mstest2`) en producción | CWE-489 | MEDIA | CONFIRMADO |
| H2 | NSC permite cleartext (`cleartextTrafficPermitted=true`, `includeSubdomains`) — vestigial, para el `SendLocation` http **comentado**; el tráfico activo es HTTPS | CWE-319 | BAJA | CONFIRMADO (config) |
| H3 | Sin trust-all activo de la app; sin secretos en el JS (solo client-ID OAuth público de Google) | — | AUSENTE-SANO | CONFIRMADO |

Detalle en [02-red-tls.md](02-red-tls.md) y [03-hallazgos.md](03-hallazgos.md).
