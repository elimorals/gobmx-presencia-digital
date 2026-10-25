# Manifiesto y superficie de ataque

Manifiesto decodificado completo: `AndroidManifest.xml` (en este directorio). El APK base
declara **1 activity exportada** (launcher), **1 service exportado** (background), varios
receivers/providers de plugins y androidx, y **21 permisos** (`uses-permission`).

## Permisos (21)

| Permiso | Nota |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | Red |
| **`CAMERA`** (`uses-feature required="true"`) | QR, foto de documentos, **video de identidad SAT ID** |
| **`RECORD_AUDIO`**, `MODIFY_AUDIO_SETTINGS`, **`AUDIO_CAPTURE`**, **`VIDEO_CAPTURE`** | **Captura de audio+video** → prueba de vida / enrolamiento SAT ID |
| **`BLUETOOTH`**, **`BLUETOOTH_ADMIN`** | Bluetooth (legacy; probable NOM/impresoras o resto de plugin) |
| **`ACCESS_COARSE_LOCATION`** | Ubicación **gruesa** (no fina) — módulos de citas/oficinas |
| **`USE_BIOMETRIC`**, **`USE_FINGERPRINT`** | Desbloqueo biométrico de sesión (local_auth) |
| `SCHEDULE_EXACT_ALARM` | Alarmas exactas (recordatorios/avisos) |
| `WRITE_EXTERNAL_STORAGE` (`maxSdkVersion=28`), `READ_EXTERNAL_STORAGE` | Guardar/leer archivos (acuses, PDF) |
| `FOREGROUND_SERVICE` | Servicio en primer plano (`flutter_background_service`) |
| `RECEIVE_BOOT_COMPLETED` | **Reinicia el servicio al encender el teléfono** |
| `WAKE_LOCK`, `VIBRATE`, `POST_NOTIFICATIONS` | Mantener CPU, vibrar, notificaciones Android 13+ |
| `mx.gob.sat.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (definido `signature` + usado) | Protege receivers dinámicos; interno |

**Permisos peligrosos presentes:** cámara, micrófono, captura de audio/video, ubicación gruesa,
biometría. Coherentes con las funciones (SAT ID con video, QR, biometría de sesión). La app
pide **más superficie sensorial que CFE Contigo** (añade micrófono y captura de video), lo cual
es el precio del enrolamiento de identidad.

## Flags de aplicación

- `android:allowBackup="false"` — **no respalda** datos de la app (correcto; maneja e.firma).
- `android:extractNativeLibs="false"` — `.so` se cargan comprimidos del APK (estándar moderno).
- **Sin `networkSecurityConfig` propio** y **sin `android:usesCleartextTraffic`** declarado →
  usa el default de la plataforma (targetSdk 35 → cleartext bloqueado salvo excepción). Ver
  doc 04: los endpoints de prod son HTTPS; los de UAT/dev son HTTPS-sobre-IP.
- `appComponentFactory="androidx.core.app.CoreComponentFactory"` — estándar AndroidX.

## Componentes exportados (superficie de ataque)

| Componente | Tipo | Por qué está expuesto |
|---|---|---|
| `mx.gob.sat.MainActivity` | activity | **LAUNCHER** — único punto de entrada de UI (FlutterActivity) |
| `id.flutter.flutter_background_service.BackgroundService` | service | Servicio foreground `dataSync` (`exported="true"`, `stopWithTask="false"`) |
| `id.flutter.flutter_background_service.WatchdogReceiver` | receiver | Watchdog del servicio (`exported="true"`) |
| `id.flutter.flutter_background_service.BootReceiver` | receiver | **Arranque en `BOOT_COMPLETED` / `MY_PACKAGE_REPLACED`** (`exported="true"`) |
| `androidx.work.impl.background.systemjob.SystemJobService` | service | WorkManager (protegido por `BIND_JOB_SERVICE`) |
| `androidx.work.impl.diagnostics.DiagnosticsReceiver` | receiver | WorkManager diag (protegido por `DUMP`) |
| `androidx.profileinstaller.ProfileInstallReceiver` | receiver | Baseline Profile (protegido por `DUMP`) |

### Observaciones de superficie

1. **No hay deeplinks ni App Links de negocio.** El único `intent-filter` con datos es el
   `MAIN/LAUNCHER`. No existe scheme propio `mx.gob.sat://` ni rutas `VIEW` manipulables.
   La superficie de intents entrantes es mínima — toda la navegación es interna de Flutter.
2. **El servicio en background es la pieza exportada más interesante.**
   `flutter_background_service.BackgroundService` es `exported="true"` con un
   `WatchdogReceiver` y un `BootReceiver` que lo relevantan tras reinicio. Para una app fiscal
   esto implica un proceso persistente; su propósito (polling de notificaciones, sync) no se
   puede fijar en estático porque la lógica vive en Dart. Es superficie a confirmar en dinámico.
3. **Los receivers de boot/watchdog exportados** pertenecen al plugin (no a código del SAT) y
   no declaran permiso propio de entrada, solo filtran por acción del sistema. El arranque
   automático es activable por `BOOT_COMPLETED`, que solo lo emite el sistema.
4. **Resto de pantallas no exportadas.** WebView, Custom Tabs/TWA, `CaptureActivity` de ZXing,
   `WebViewActivity` de url_launcher: todas `exported="false"`.

## `<queries>` (Android 11+)

La app declara poder ver solo tres intents: `CustomTabsService` (navegador para Custom Tabs),
`DIAL` con scheme `tel` (llamar), y `GET_CONTENT` `*/*` (adjuntar archivos). No enumera otras
apps del ecosistema gob.mx — contraste con CFE, que consultaba todo el stack Huawei.

## FileProviders (3) — rutas a revisar

| Authority | Plugin | Nota |
|---|---|---|
| `mx.gob.sat.flutter.image_provider` | image_picker | `exported="false"`, `grantUriPermissions="true"` |
| `mx.gob.sat.flutter.share_provider` | share_plus | idem — comparte acuses/PDF |
| `mx.gob.sat.flutter.printing` | printing | idem — imprime acuses/CFDI |

Los tres son providers de plugins Flutter con `grantUriPermissions="true"`. **Confirmado** leyendo
`res-decoded/base/res/xml/` (segundo pase, doc 10): sus `<paths>` están **bien acotados a cache**,
no exponen la raíz de `files/`:

```xml
<!-- flutter_image_picker_file_paths.xml -->   <cache-path name="cached_files" path="." />
<!-- flutter_printing_file_paths.xml -->       <cache-path name="share" path="/share/" />
<!-- flutter_share_file_paths.xml -->          <cache-path name="cache" path="share_plus/" />
<!-- provider_paths.xml -->                     <external-path name="external_files" path="." />
```

A diferencia de jw-library y CFE (que exponían `files-path "/"`), **SAT Móvil no incurre en esa
mala práctica**: es un punto a favor.
