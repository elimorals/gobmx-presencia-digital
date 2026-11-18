# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 1.2.11, compileSdk 36.

## Permisos
| Permiso | Para qué (inferido) |
|---|---|
| `CAMERA` | lectura de código de recibo / captura (QR del recibo) |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | login/desbloqueo biométrico local |
| `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID`, `AD_ID` | **atribución de publicidad** (Privacy Sandbox) |
| `BIND_GET_INSTALL_REFERRER_SERVICE` (Finsky) | atribución de instalación |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | notificaciones push (FCM) |
| `CHECK_LICENSE` (vending) | licencia de Play |
| `VIBRATE`, `WAKE_LOCK` | UX |

**Lectura:** no pide ubicación, contactos ni SMS. Lo que llama la atención es el bloque
**AdServices + AD_ID + Install Referrer** → medición/atribución de publicidad en una app de
**pago de agua**, innecesaria para la función (doc 04) — observación de privacidad. La biometría
es para el login.

## Flags / componentes
- Sin `usesCleartextTraffic` en el manifiesto → HTTP en claro bloqueado por defecto (el backend
  de pago es HTTPS; doc 03).
- Runtime Flutter; Firebase (FCM/Analytics) + Google Maps; `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`
  (AndroidX).
