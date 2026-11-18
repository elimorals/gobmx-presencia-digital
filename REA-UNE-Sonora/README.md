# Ingeniería inversa de UNE (Transporte Sonora) 5.7.2 — índice

Análisis **estático** del APK `com.bigbangdev.bussonora` (etiqueta "UNE") con REA (JADX headless
+ inspección de archivo). La app **nunca se ejecutó**. Es la app de transporte público del
estado de **Sonora** (UNE / Bus Sonora, ligada al IMTES). **#17 por instalaciones** del
inventario gob.mx (≈1 M).

> **Flutter — lógica en el snapshot Dart (muro).** `flutter_assets/` + `libapp.so`; la lógica
> vive en el snapshot Dart AOT, no en el DEX → no decompilable estáticamente. Lo observable son
> los **SDKs** (Sentry para errores, **reCAPTCHA**, Firebase, AdServices para atribución de
> publicidad) y los permisos (ubicación + **biometría**). El backend propio va en el snapshot
> (no en claro en el DEX). **Firma de desarrollador individual** (`CN=Omar Aguilar Fraga, O=Bus
> Sonora`), original preservada — no re-firmada por APKPure, pero tampoco un certificado de una
> dependencia de gobierno.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, SDKs, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación, biometría), AdServices |
| 02 | [02-flutter.md](02-flutter.md) | Flutter/Dart AOT, muro |
| 03 | [03-sdks-red.md](03-sdks-red.md) | Sentry, reCAPTCHA, Firebase, AdServices |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Firma de dev individual (Bus Sonora) |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos y límites |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/firma.txt`
(no versionado).

## 5 hallazgos principales

1. **Flutter = lógica en el snapshot Dart (muro).** `libapp.so` (Dart AOT); el DEX es embedding.
   Backend propio, auth y pago no observables estáticamente (doc 02).
2. **Firma de desarrollador individual.** `CN=Omar Aguilar Fraga, O=Bus Sonora` (Hermosillo) —
   firma **original preservada** (no re-firmada por APKPure), pero es un **certificado de una
   persona/empresa (Bus Sonora)**, no de una dependencia estatal. Implicación de cadena de
   suministro para una app de servicio público (doc 04).
3. **Telemetría de errores con Sentry.** El DEX referencia `sentry.io` → reporte de crashes/
   errores a Sentry. Útil, pero envía datos de diagnóstico a un tercero (doc 03).
4. **reCAPTCHA + Firebase + AdServices.** reCAPTCHA (anti-bot), Firebase (analytics/FCM) y
   **AdServices** (`ACCESS_ADSERVICES_ATTRIBUTION`/`AD_ID`) → **atribución de publicidad** en una
   app de transporte público: recolección de identificadores con fines de medición de anuncios
   (doc 01, 03).
5. **Biometría de desbloqueo.** `USE_BIOMETRIC`/`USE_FINGERPRINT` → login/desbloqueo biométrico
   local (doc 01).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. La lógica Dart no se desensambló (muro); se documentan SDKs, permisos y firma,
con evidencia. La firma es de **Bus Sonora** (dev), no de una dependencia de gobierno — verifica
integridad del artefacto tal como lo firmó el desarrollador, no identifica a una institución
estatal.
