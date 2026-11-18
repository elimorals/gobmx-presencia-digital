# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 9.0.8, compileSdk 36.

## Permisos (amplios — super-app)

| Permiso | Para qué (inferido) |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (trámites/servicios cercanos) |
| **`FOREGROUND_SERVICE_LOCATION`** + `FOREGROUND_SERVICE` | ubicación en segundo plano |
| `CAMERA` | captura/escaneo (QR, documentos) |
| `RECORD_AUDIO`, `READ_MEDIA_AUDIO` | audio (video/notas) |
| `READ_CONTACTS` | ¿compartir/invitar? (inferido) |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | login/desbloqueo biométrico |
| `SCHEDULE_EXACT_ALARM`, `USE_EXACT_ALARM`, `USE_FULL_SCREEN_INTENT` | recordatorios/notificaciones |
| `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK` | background |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |

**Lectura:** conjunto **muy amplio**. Como super-app agrega muchos servicios, pero reúne a la
vez ubicación en segundo plano, contactos, micrófono, cámara y biometría — alto impacto para
privacidad. El **uso exacto de cada permiso vive en el snapshot Dart** (doc 02) y **no es
verificable estáticamente**; se declara la superficie, no el abuso.

## Flags

- **Sin `usesCleartextTraffic`** en el manifiesto → en targetSdk 36 el valor por defecto es
  `false`: HTTP en claro bloqueado salvo excepción (no hay).
- Runtime Flutter (FlutterActivity/engine) + Firebase (FCM/Remote Config) estándar.
