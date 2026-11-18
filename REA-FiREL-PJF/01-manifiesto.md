# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 1.0.14, compileSdk 36.

## Permisos

| Permiso | Para qué (confirmado por el flujo, doc 03) |
|---|---|
| `CAMERA` + `camera.autofocus` + `FLASHLIGHT` | captura de INE, rostro y **huellas sin contacto** (Identy usa cámara + flash) |
| `RECORD_VIDEO` | **video de verificación** (`RequestSendVideoMeet`) |
| `HIGH_SAMPLING_RATE_SENSORS` | sensores de alta frecuencia (liveness / anti-spoof) |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | geolocaliza la operación (`RequestCoordinates`) |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | red (SOAP) |
| `BATTERY_STATS`, `VIBRATE`, `c2dm…RECEIVE` | estado / notificaciones push |

**Lectura:** el conjunto es **amplio y coherente con enrolamiento biométrico**: cámara + video +
flash + sensores de alta frecuencia + ubicación. No pide contactos ni SMS. La ubicación fina en
una app de firma es para dejar constancia geográfica del acto (sello de ubicación).

## Flags / componentes

- Sin `usesCleartextTraffic` en el manifiesto → HTTP en claro bloqueado por defecto (pero ver la
  postura TLS contradictoria en doc 04).
- Launcher: `com.fielnet.notificaciones_colegio_notarios_mvvm.ui.login.LoginActivity` (revela el
  package real, doc 02).
- Firebase (FCM) para notificaciones; `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (AndroidX).
- Actividades del flujo (DataBinding): `ActivityCaptureFinger`, `ActivityFaceIdenty`,
  `ActivityFaceSimilarity`, `ActivityHandwrittenSignature`, `ActivityDocumentRequestFirel`,
  `ActivityDocumentReviewOcr`, `ActivityVerificationIdentityPerson`, `ActivityScannerQr`,
  `ActivityGetLocation`, `ActivityNoticePrivacy`, `ActivityPrivacyAndAgreement`,
  `TouchlessActivityIntroFourFingers`, `TouchlessSpoofDialog`.
