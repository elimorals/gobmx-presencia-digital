# 01 · Manifiesto

Fuente: `AndroidManifest.xml` decodificado con apktool 3.0.3. Análisis estático.

## Identidad

| Campo | Valor |
|---|---|
| package | `mx.sct.licencia` |
| versionName / versionCode | `1.0.49` / `49` |
| minSdkVersion | 27 (Android 8.1) |
| targetSdkVersion | 33 (Android 13) |
| `custom_url_scheme` | `mx.sct.licencia` |

## Permisos (13)

| Permiso | Uso probable |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | Red / WSLFD |
| `CAMERA`, `FLASHLIGHT` | Escaneo de código (barcodescanner) |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación (uso no determinado estáticamente) |
| `RECORD_AUDIO`, `MODIFY_AUDIO_SETTINGS` | **Atípico** en app de licencias; probable arrastre del barcodescanner/ZXing. Marcado para revisión, sin afirmar uso. |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | Archivos (cordova-plugin-file) |
| `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK` | Firebase / FCM |
| `com.google.android.c2dm...RECEIVE` | FCM |

> No se declara `usesCleartextTraffic` ni `networkSecurityConfig`. Con `targetSdk 33`, el
> tráfico en claro queda deshabilitado por defecto de la plataforma; **no hay** configuración
> de network-security ni pinning a nivel manifiesto.

## Componentes

4 activities · 6 services · 5 receivers · 2 providers.

**Exportados:**

| Componente | Tipo | Nota |
|---|---|---|
| `mx.sct.licencia.MainActivity` | activity | Única Activity (patrón Capacitor). Launcher + deeplink. |
| `com.getcapacitor.CapacitorFirebaseMessagingService` | service | FCM (framework) |
| `com.google.firebase.iid.FirebaseInstanceIdReceiver` | receiver | FCM (framework) |

**Deeplink** (en `MainActivity`):

```
VIEW + DEFAULT + BROWSABLE
data android:scheme="mx.sct.licencia"
```

Esquema propio `mx.sct.licencia://`. Toda la navegación interna la resuelve el router de
Angular dentro del WebView; la Activity sólo es el contenedor Capacitor.

**Providers:**

- `androidx.core.content.FileProvider` — compartición de archivos acotada (cordova-plugin-file).
- `com.google.firebase.provider.FirebaseInitProvider` — inicialización Firebase (framework).

## Limitaciones

- Firma del APK **no verificada** (apksigner no disponible en el entorno). Ver doc 08.
- La superficie real de la app no está en los componentes Android (sólo hay una Activity):
  vive en el bundle JS. Ver docs 02–05.
