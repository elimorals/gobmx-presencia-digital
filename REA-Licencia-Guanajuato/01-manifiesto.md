# 01 · Manifiesto

Package `com.ztk.licencia.digital.gto` v2.1.1 (code 36), minSdk 24 / targetSdk 36. Decodificado:
[AndroidManifest.xml](AndroidManifest.xml).

## Permisos

- `USE_BIOMETRIC`, `USE_FINGERPRINT` — autenticación biométrica (acceso a la licencia).
- `CAMERA` — probable captura de documento / verificación.
- `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` — almacenamiento.
- `INTERNET`, `ACCESS_NETWORK_STATE`.
- `com.google.android.gms.permission.AD_ID`, `READ_GSERVICES`, `BIND_GET_INSTALL_REFERRER_SERVICE`
  — Google/Play (ads/analytics/referrer).

**Notable:** **sin** `SEND_SMS`/`READ_SMS` ni permisos de **ubicación** — superficie más acotada que
la app Cosmocolor de EDOMEX (que sí pedía más). Coherente con un flujo de licencia por biometría +
cámara sin OTP SMS ni geolocalización.

## Pila

Flutter: `libflutter.so` (engine) + `libapp.so` (Dart AOT, 15.6 MB) en el split `config.arm64_v8a`.
La lógica de negocio vive en el Dart AOT → muro para el análisis estático.
