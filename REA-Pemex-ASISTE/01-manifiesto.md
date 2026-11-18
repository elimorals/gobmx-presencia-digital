# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 3.1.19, compileSdk 35, etiqueta "Asiste".

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | localizador de estaciones/servicios cercanos |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red (API + video) |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | notificaciones push (FCM) |
| `READ_EXTERNAL_STORAGE` (`maxSdkVersion=32`) | lectura de medios en versiones viejas |
| `VIBRATE`, `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED` | UX / background |
| badges de launchers (Samsung/Sony/HTC/Huawei/…) | contador de icono |

**Lectura:** conjunto **acotado**. La ubicación es el único permiso sensible, coherente con un
localizador. **No** pide cámara, micrófono, contactos, SMS, teléfono ni ubicación en segundo
plano. Buena higiene de permisos.

## Componentes

- Launcher y runtime de **Flutter** (el `FlutterActivity`/engine). El grueso de la UI y la
  lógica corre en el motor Flutter + snapshot Dart (doc 02), no en clases Java propias.
- Firebase (FCM) + GMS estándar para notificaciones.
- `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (patrón AndroidX).

## Flags

- Sin `usesCleartextTraffic` → por defecto HTTP en claro bloqueado (consistente con el backend
  HTTPS, doc 03).
- Sin `networkSecurityConfig` por recurso → política por defecto (CAs del sistema). El pinning,
  de existir, lo haría la capa Dart (Dio/http), no observable estáticamente.
