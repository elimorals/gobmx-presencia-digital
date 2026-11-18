# Manifiesto — permisos, componentes, flags

Fuente: `AndroidManifest.xml` decodificado (apktool 3.0.3). Package `mx.com.cosmocolor.movapp`,
`platformBuildVersionName=16`, `platformBuildVersionCode=36`.

## Permisos

| Permiso | Para qué (inferido del uso) |
|---|---|
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | `LocationUtils`; geolocalización en trámites/citas |
| `CAMERA` | Enrolamiento: captura de documento (Regula) y rostro (Face API) |
| `NFC` | `NfcLicenseReader` / `EnrollmentNfcActivity`: lectura de la licencia física por NFC |
| `RECORD_AUDIO` | Declarado; sin flujo de audio evidente (posible dependencia de SDK de captura) |
| `BLUETOOTH` / `BLUETOOTH_ADMIN` | Declarados (probable SDK de lectura/periféricos) |
| `FLASHLIGHT` | Linterna para captura de documento |
| `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` | Almacenamiento de imágenes de captura |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | Red |
| `POST_NOTIFICATIONS` | Firebase Cloud Messaging (`NotificationsMessagingService`) |
| `ACCESS_ADSERVICES_AD_ID` / `ACCESS_ADSERVICES_ATTRIBUTION` | Privacy Sandbox / atribución (dependencia de SDK) |
| `com.google.android.finsky...INSTALL_REFERRER_SERVICE` | Install Referrer de Play |
| `dm.permission.RECEIVE` | Receptor de un SDK de push/mensajería |

`RECORD_AUDIO` es el permiso más llamativo para una app de licencias: no se halló un flujo de
grabación en el código propio; queda como permiso declarado sin uso claro (no se afirma causa).

## Flags de la aplicación

| Flag | Valor | Nota |
|---|---|---|
| `android:allowBackup` | **`false`** | ✅ Bloquea respaldo ADB de la DB/prefs (mitiga exfil de la llave SQLCipher) |
| `android:usesCleartextTraffic` | **`true`** | ⚠️ Permite HTTP en claro globalmente; **no hay** `networkSecurityConfig` que lo acote (doc 03) |
| `android:networkSecurityConfig` | — | Ausente |
| `android:debuggable` | — | No presente (release) |

## Componentes exportados

| Componente | Nota |
|---|---|
| `ui.launch.SplashActivity` | Launcher (normal) |
| `com.google.firebase.iid.FirebaseInstanceIdReceiver` | Firebase (normal) |
| `androidx.compose.ui.tooling.PreviewActivity` | **Artefacto de desarrollo** de Jetpack Compose quedó exportado (doc 09) |
| `androidx.profileinstaller.ProfileInstallReceiver` | Baseline Profiles (normal) |

No se observaron `content-provider` propios exportados ni `exported="true"` en actividades de
negocio.
