# 01 — Manifiesto, permisos y superficie

Fuente: `AndroidManifest.xml` decodificado (apktool) + `inspect_android_package` (JADX 0.7.1).
Copia completa en [`AndroidManifest.xml`](AndroidManifest.xml).

- **Package:** `com.icv.app` · **versionName** 2.4 · **versionCode** 45 · **minSdk** 21 · **targetSdk** 36.
- **Aplicación:** `crc64c9673c5d1e894c67.MainApplication` (nombre `crc64…` = puente JNI de **.NET MAUI/Xamarin**, ver doc 02).

## Permisos declarados (13)

| Permiso | Uso probable | Nota |
|---|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | Red | — |
| `CAMERA` + `uses-feature camera` | Captura para **FaceLiveness** y **QR** (docs 06) | peligroso |
| `USE_BIOMETRIC` | Biometría del sistema | — |
| `HIGH_SAMPLING_RATE_SENSORS` | Sensores de alta frecuencia (liveness) | — |
| `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE` | Guardar/leer PDFs y fotos de licencia | peligroso, amplio |
| `POST_NOTIFICATIONS` | Notificaciones | — |
| `VIBRATE`, `FOREGROUND_SERVICE` | UX / `KeepAliveService` | — |
| **`INSTALL_PACKAGES`** | Instalar APK (¿auto-actualización?) | **`signature\|privileged`: el sistema NO lo concede a apps de tienda** → declarado pero inefectivo; señal de intención de instalar paquetes. Ver doc 10. |
| `com.icv.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | Receptor dinámico (firma) | correcto |

## Banderas de `<application>`

| Atributo | Valor | Lectura |
|---|---|---|
| `android:usesCleartextTraffic` | **`true`** | Permite HTTP en claro a nivel app. No hay `networkSecurityConfig` que lo acote. La API nativa observada es HTTPS (doc 03); el riesgo real depende de lo que cargue el **WebView** (docs 06-07). Ver hallazgo H2. |
| `android:allowBackup` | **`true`** | Permite respaldo de datos de la app (prefs, token). Contrasta con `allowBackup=false` de AforeMóvil. Ver hallazgo H3. |
| `android:extractNativeLibs` | `false` | `.so` se cargan comprimidos del APK. |
| `android:hardwareAccelerated` | `true` | — |

## Componentes

- **`MainActivity`** (`crc64…`, **exported=true**): launcher + **deep link** `autoVerify` a `https://icvnl.gob.mx/app…` y `www.icvnl.gob.mx` (App Links). Único componente exportado de la app (correcto).
- `WebAuthenticatorIntermediateActivity`, `IntermediateActivity` — OAuth/WebAuthenticator de MAUI Essentials (exported=false).
- `microsoft.maui.essentials.fileProvider` (authorities `com.icv.app.fileProvider`, exported=false, grantUriPermissions) — compartir archivos (PDF de licencia). FileProvider acotado ✅.
- `mono.MonoRuntimeProvider` — **runtime Mono/.NET** (confirma MAUI).
- `KeepAliveService` (sin exportar), receptores de conectividad/batería de MAUI Essentials, Play Core AssetPacks (deshabilitados).
- `ProfileInstallReceiver` (androidx, `permission=DUMP`, exported=true) — estándar de androidx, no propio.

## Procedencia (Source Stamp)

`com.android.stamp.source=https://play.google.com/store` + `STAMP_TYPE_DISTRIBUTION_APK` → artefacto **distribuido por Google Play**. La firma es auténtica del emisor (doc 10).

## Superficie de ataque (resumen)

Superficie local mínima: un solo activity exportado (launcher con App Links verificados). El grueso de la superficie es **remota** (API HTTPS :1046) y el **WebView** con puente JS (doc 07). `usesCleartextTraffic=true` + WebView + puente `invokeCSharpAction` es la combinación a vigilar.
