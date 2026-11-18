# 00 · Resumen ejecutivo — Licencia Digital Chihuahua

## Identidad

| Campo | Valor |
|---|---|
| App | Licencia Digital Chihuahua |
| Package | `com.gob.licdig` |
| versionName / versionCode | 615.12.11 / 14 |
| minSdk / targetSdk | 22 / 35 |
| Firma | `CN=Android, O=Google Inc.` → **re-firmada por APKPure** (no auténtica) |
| SHA-256 cert | `fdb1969e0b45e838157d379ab9c7c99f222563add3df26a59586372df71da86c` (genérico APKPure) |
| sha256 base apk | `f7308170b2b6d6463d4752e14c293e7e884e699ec18e52e3d3d22ba5746e49fe` |
| Dueño | SSP Chihuahua (Secretaría de Seguridad Pública del Estado) |

Package **verificado** (`aapt2`/`manifest.json`) antes de documentar.

## Pila tecnológica

- **Capacitor (Ionic)** — shell nativo delgado + WebView; la lógica vive en el bundle JS
  (`assets/public/assets/index-*.js`, ~1.3 MB, **no ofuscado**).
- Libs nativas: ML Kit barcode (`libbarhopper_v3`) + CameraX (`libimage_processing_util_jni`,
  `libsurface_util_jni`) — escaneo/cámara para la credencial.
- **CapacitorUpdater** (OTA) configurado a `https://licdigital.sspechih.gob.mx:8082/api/Actualizaciones/`
  con `autoUpdate:false`.
- **NO es Cosmocolor** ni ningún white-label de las licencias EDOMEX/Jalisco/Chiapas — es un
  desarrollo Capacitor propio de SSP Chihuahua.

## Backend

- API: **`https://licdigital.sspechih.gob.mx:8082/api/LicenciaDigital/`** (HTTPS, puerto 8082,
  dominio oficial de SSP Chihuahua).
- Sin pasarela de pago embebida detectada; biometría (facial/huella) para la credencial.

## Veredicto de seguridad (resumen)

- **NO comparte el defecto Cosmocolor** (sin trust-all / `getUnsafeOkHttpClient` / `ServiceGenerator`).
- Backend real **HTTPS**. Sin secretos/API keys hardcodeados en el bundle JS.
- Único punto: **config de red permisiva** — `usesCleartextTraffic=true` (global, sin NSC) +
  `allowMixedContent:true` en `capacitor.config.json`. Permite cleartext/mixed-content, pero el
  backend observado es HTTPS → **MEDIA-BAJA** (higiene, no explotado). Ver doc 02/03.
