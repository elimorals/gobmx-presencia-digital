# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 1.0.32, compileSdk 36.

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `CAMERA` | captura / escaneo (p. ej. QR de pase de lista) o video |
| `RECORD_AUDIO`, `MODIFY_AUDIO_SETTINGS` | audio (video de cursos vía ExoPlayer; posible videollamada) |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | notificaciones push (servicio `notificaciones`) |
| `VIBRATE`, `WAKE_LOCK` | UX |
| `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | patrón AndroidX |

Conjunto **acotado**: no pide ubicación, contactos, SMS, teléfono ni almacenamiento amplio. La
cámara y el micrófono encajan con reproducción de contenido educativo y captura (QR/foto).

## Componentes

- **Launcher:** `crc6434ee2e0abc662176.MainActivity`. El prefijo `crc64<hash>` es la convención
  de **Java Callable Wrappers** que generan Xamarin/.NET — señal de que la UI/lógica corre en el
  runtime .NET, no en clases Java propias (doc 02).
- Componentes de Firebase (FCM) y GMS estándar para las notificaciones push.

## Flags

- No se observó `usesCleartextTraffic` → por defecto, sin HTTP en claro (consistente con los
  backends HTTPS, doc 04).
- Sin `networkSecurityConfig` por recurso → política por defecto (CAs del sistema). El pinning,
  si existe, se haría en la capa .NET/MSAL, no observable aquí.
