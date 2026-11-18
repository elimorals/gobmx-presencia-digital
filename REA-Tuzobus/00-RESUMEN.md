# 00 · Resumen ejecutivo — Tuzobús (Hidalgo)

## Identidad

| Campo | Valor |
|---|---|
| App | Tuzobús |
| Package | `com.tuzobus` |
| versionName / versionCode | 2.4.1 / 32 |
| minSdk / targetSdk | 24 / 36 |
| Firma | **Auténtica** — `CN=Alejandro Tenorio, OU=Gobierno del Estado de Hidalgo, O=Gobierno Digital, L=Pachuca, ST=Hidalgo` · Source Stamp genérico de Google (Play App Signing, normal) |
| sha256 XAPK | `15cdb219d9ba4c52fc192a25b3b3770e19878e56afb37a778cfa4e75de9f6ec0` |
| sha256 base APK | `24bd9f67b26c7f31201fc61f30b3820ec18d8571584c4bb47f6e25a75afb26be` |
| Clases / recursos | 9 837 / 896 |
| Dueño | Gobierno del Estado de Hidalgo (BRT Tuzobús) |

Package **verificado con `inspect_android_package`** sobre el APK base (extraído del XAPK) antes de
documentar. Coincide: `com.tuzobus`.

## Pila tecnológica

- **React Native + Expo + Hermes.** Confirmado por `libhermesvm.so`, `libreactnative.so`,
  `libfbjni.so`, `libjsi.so`, `assets/index.android.bundle`, `com/facebook/react` (×388 en el DEX) y
  marcadores `expo`. RN moderno: gesture-handler, rn-screens, rn-svg, safe-area-context.
- **La lógica JS vive en `index.android.bundle` como bytecode Hermes** → **muro** para el análisis
  estático (igual que Jalisco Alerta). Las URLs y strings son parcialmente recuperables; el flujo
  real no.
- Red/push: Firebase Cloud Messaging (FCM / c2dm).

## Permisos (manifest)

`INTERNET`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `USE_BIOMETRIC`, `USE_FINGERPRINT`,
`POST_NOTIFICATIONS`, `FOREGROUND_SERVICE`, `RECEIVE_BOOT_COMPLETED`, `SCHEDULE_EXACT_ALARM`,
`WAKE_LOCK`, `VIBRATE`, `ACCESS_NETWORK_STATE`, FCM. **Sin SMS/registro de llamadas.** Ubicación +
biometría son razonables para una app de transporte con saldo/recarga.

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | Endpoint de **pruebas** en producción: `https://apis-tests.hidalgo.gob.mx` | CWE-489 | MEDIA | CONFIRMADO |
| H2 | TLS/cleartext correcto: `usesCleartextTraffic=false`, `allowBackup=false`, sin trust-all, firma auténtica | — | — | **AUSENTE-SANO** |
| H3 | Pasarela de pago, endpoint prod real, secretos y validación TLS efectiva | — | — | A-CONFIRMAR (muro Hermes) |

Detalle en [01-manifiesto.md](01-manifiesto.md), [02-red-tls-cleartext.md](02-red-tls-cleartext.md),
[03-hallazgos.md](03-hallazgos.md).

## Cruce con el mapa de proveedores

- **NO repite el cleartext de 911-CDMX** (contraejemplo limpio, como Jalisco Alerta): postura de red
  correcta de base.
- **3ª app React Native/Hermes del corpus** (con Internet para el Bienestar y Jalisco Alerta) →
  consolida Hermes como clase de muro, al lado de Flutter/Veritran/.NET.
