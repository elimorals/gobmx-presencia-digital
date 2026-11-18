# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 5.7.2, compileSdk 36.

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (paradas/unidades cercanas) |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | desbloqueo/login biométrico local |
| `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID` | **atribución de publicidad** (Android Privacy Sandbox / AdServices) |
| `BIND_GET_INSTALL_REFERRER_SERVICE` (Finsky) | atribución de instalación (Play Install Referrer) |
| `READ_GSERVICES` | Google Services Framework |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `POST_NOTIFICATIONS`, `VIBRATE`, `WAKE_LOCK` | estándar |

**Lectura:** no pide cámara, micrófono, contactos ni ubicación en segundo plano (más acotada que
Qrobús en ese sentido). Lo que llama la atención es el bloque de **AdServices + Install Referrer**
→ la app hace **medición/atribución de publicidad** (identificadores con fines de anuncios), algo
inesperado en una app de transporte público (doc 03). La biometría es para el login local.

## Flags / componentes

- Sin `usesCleartextTraffic` en el manifiesto → HTTP en claro bloqueado por defecto.
- Runtime Flutter; Firebase (FCM/Analytics); `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (AndroidX).
