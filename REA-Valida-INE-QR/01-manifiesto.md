# 01 — Manifiesto, permisos y componentes

Fuente: `AndroidManifest.xml` decodificado (apktool) + `inspect_android_package` (REA/JADX).
Ver copia completa en [`AndroidManifest.xml`](AndroidManifest.xml).

## Permisos declarados

| Permiso | Uso | Nota |
|---|---|---|
| `android.permission.CAMERA` | Escanear el QR y OCR de la credencial | Esperado |
| `android.permission.WAKE_LOCK` | Mantener pantalla activa durante el escaneo | — |
| `android.permission.VIBRATE` | Retroalimentación háptica al detectar QR | — |
| `android.permission.HIDE_OVERLAY_WINDOWS` | **Anti-overlay / anti-tapjacking**: oculta ventanas superpuestas de otras apps durante el escaneo | Postura de seguridad proactiva |
| `mx.ine.rfe.lectorqr.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | Permiso propio a nivel `signature` para receivers dinámicos | Generado por androidx |

### Hallazgo 1 — No hay permiso `INTERNET`

El manifiesto **no declara `android.permission.INTERNET`** ni `ACCESS_NETWORK_STATE`. Sin
`INTERNET`, Android impide a la app abrir cualquier socket: **no puede contactar un backend,
subir los datos escaneados ni telemetría**. Esto fuerza que toda la validación del QR sea
**local/offline** (ver doc 02) y es el rasgo de privacidad más importante de la app: lo que
se escanea no sale del dispositivo por red. Consistente con el doc 02 (sin dominios/endpoints
en el DEX).

> Evidencia: lista de permisos de `get_app_info` (REA) = `[HIDE_OVERLAY_WINDOWS, CAMERA,
> WAKE_LOCK, VIBRATE, DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION]`. No aparece `INTERNET`.

## Features de hardware

- `android.hardware.camera` **required=true**
- `android.hardware.camera.front` **required=true**
- `android.hardware.camera.autofocus` **required=true**

## Flags de la aplicación (postura de seguridad)

| Atributo | Valor | Lectura |
|---|---|---|
| `android:allowBackup` | `false` | ✅ los datos de la app no se respaldan a la nube/ADB |
| `android:useEmbeddedDex` | `true` | Ejecuta el DEX embebido sin extraerlo |
| `android:extractNativeLibs` | `false` | `.so` se cargan comprimidas del APK |
| `android:allowAudioPlaybackCapture` | `false` | No permite capturar su audio |
| `android:largeHeap` | `true` | Heap grande (procesamiento de imagen) |

## Componentes

**Actividades** (todas `exported=false` salvo el launcher):

| Actividad | Exportada | Rol |
|---|---|---|
| `mx.ine.rfe.SplashActivity` | **true** (launcher) | Entrada, `MAIN/LAUNCHER` |
| `mx.ine.rfe.WelcomeActivity` | false | Bienvenida |
| `mx.ine.rfe.TutorialActivity` | false | Tutorial de uso |
| `mx.ine.rfe.LegalsActivity` | false | Avisos legales (`assets/legal.html`) |
| `mx.ine.rfe.HomeActivity` | false | Pantalla principal |
| `mx.ine.rfe.WaitingRoomActivity` | false | Sala de espera / procesamiento |
| `mx.ine.rfe.ResultsActivity` | false | Resultados del QR (CIC/CURP/OCR/…) |
| `mx.ine.rfe.ErrorActivity` | false | Error / QR inválido |
| `com.personalcode.ScannerActivity` | false | **Escáner del SDK** PersonalCode |

**Otros**: `androidx.startup.InitializationProvider` (no exportado),
`androidx.camera.core.impl.MetadataHolderService` (CameraX, `enabled=false`).

**Superficie de ataque**: mínima. La única entrada externa es `SplashActivity` (launcher sin
datos de entrada). El escáner y los resultados no son exportados, así que otra app no puede
invocarlos directamente.

## Metadatos relevantes

- `com.google.mlkit.vision.DEPENDENCIES = ocr` → descarga/usa el modelo **OCR de MLKit**.
- `com.android.stamp.source = https://play.google.com/store` + `STAMP_TYPE_DISTRIBUTION_APK`
  → Source Stamp de Play presente (ver doc 06, firma).
