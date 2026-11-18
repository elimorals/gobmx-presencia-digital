# Manifiesto — permisos, componentes, flags

Fuente: `AndroidManifest.xml` decodificado (JADX/apktool). Ver copia completa en
[`AndroidManifest.xml`](AndroidManifest.xml).

## Permisos

| Permiso | Para qué | Nota |
|---|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | WebView + chequeo de conexión | esperado |
| `CAMERA` (+ features camera/autofocus/raw) | captura de documentos/fotos | `foregroundServiceType="camera"` |
| `READ_MEDIA_IMAGES`, `READ_EXTERNAL_STORAGE` | elegir fotos para subir | selección de archivos del WebView |
| `POST_NOTIFICATIONS` | notificaciones push | `required=false` |
| `com.google.android.c2dm.permission.RECEIVE` | Firebase Cloud Messaging | push |
| `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE`, `WAKE_LOCK` | servicio en primer plano / rearranque | — |

No pide ubicación, contactos, SMS ni permisos `signature|privileged` anómalos. Conjunto
**acotado** para una app de captura de documentos.

## Componentes

- **1 Activity exportada:** `MainActivity` (LAUNCHER + acción `OPEN_NOTIFICATION_SCREEN` +
  `BOOT_COMPLETED`). Única pantalla: aloja el WebView.
- **Servicios:** `FirebaseMessagingService` (push; `android:permission="TODO"` — valor
  placeholder dejado por el build, no concede acceso real), `ForegroundService` (no exportado).
- **Receptor:** `bootReceiver`; más los de Firebase/GMS/datatransport estándar.
- `ProfileInstallReceiver` (AndroidX) con `permission="android.permission.DUMP"` — **lo mete
  AndroidX ProfileInstaller, no la app**; no es un permiso solicitado por Tarjeta Rosa (mismo
  patrón que se corrigió como falso positivo en 911 CDMX).

## Flags de seguridad

- **`android:usesCleartextTraffic`** — **ausente** → en targetSdk 36 el valor por defecto es
  `false`: tráfico HTTP en claro bloqueado salvo excepción explícita (no hay). ✅
- Sin `android:networkSecurityConfig` → se aplica la política por defecto (sin cleartext,
  confianza en CAs del sistema). No hay pinning declarado por recurso.
- `android:extractNativeLibs="false"` (no hay `.so` propias; es app Java/Kotlin).
- `android:appComponentFactory="androidx.core.app.CoreComponentFactory"` (estándar).

## Lectura

El manifiesto es limpio: permisos justificados por la captura de documentos, sin cleartext, sin
componentes exportados peligrosos más allá de la Activity principal. El riesgo no está aquí
sino en la configuración del WebView (doc 02–03).
