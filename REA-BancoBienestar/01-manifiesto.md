# 01 · Manifiesto

Fuente: `AndroidManifest.xml` (apktool) + resumen JADX. Estático.

## Identidad

| Campo | Valor |
|---|---|
| package | `gob.bancodelbienestar.bcobienapp` |
| versionName / versionCode | `7.17.0.52467` / `5246790` |
| minSdk / targetSdk | 29 / 36 |

## Permisos (34)

**Sensibles / notables:**

| Permiso | Nota |
|---|---|
| `READ_CONTACTS` | Lee la agenda — probable para transferencias/DiMo por teléfono. Superficie de privacidad alta. |
| `READ_PHONE_STATE`, `READ_BASIC_PHONE_STATE` | Identificadores de dispositivo/SIM |
| `CAMERA` | Escaneo (ML Kit barcode) / captura de documentos |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación (uso no determinado estáticamente) |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | Autenticación biométrica |
| `DETECT_SCREEN_CAPTURE` | Conciencia de captura/grabación de pantalla (anti-fraude, ver doc 04) |
| `AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID` | Advertising-ID / atribución (analítica; **no** publicidad, doc 06) |
| `RECEIVE` (c2dm), `POST_NOTIFICATIONS`, `RECEIVE_BOOT_COMPLETED` | Push FCM |
| `READ/WRITE_EXTERNAL_STORAGE`, `WRITE_SETTINGS`, `WAKE_LOCK`, `FOREGROUND_SERVICE` | Operación |
| badges (Sony/Huawei/HTC) | Contador en ícono de launcher |

> No declara permisos de SMS de lectura; usa **SMS Retriever API** (OTP sin permiso, doc 06).

## Componentes

| Tipo | Total | Exportados |
|---|---|---|
| activity | 12 | 2 |
| service | 17 | 2 |
| receiver | 14 | 5 |
| provider | 4 | 0 |

**Exportados relevantes:**

- `net.veritran.smart.VTSpecialStartActivity`, `gob.bancodelbienestar.bcobienapp.VTCommonActivity`
  — entradas del runtime Veritran (una de ellas es el launcher + deeplinks).
- `net.veritran.android.implementation.smsretrieved.SMSBroadcastReceiver` — recepción OTP.
- `net.veritran.android.implementation.push_wrapper.PushPackageReplacedBroadcastReceiver` — push.
- Resto: GMS/Firebase/WorkManager/ProfileInstaller (framework).

## Deeplinks / intents (en `VTCommonActivity`)

| Esquema / host | Uso |
|---|---|
| `bancodelbienestar://` | Deeplink propio de la app |
| `https` (App Links) | Enlaces universales |
| `vnd.android.nfc` | **Intent NFC** — la app responde a etiquetas/lectura NFC |
| `package`, host `ext` | Flujos internos / externos |

## Providers

FileProvider (`...fileprovider`), FirebaseInitProvider, androidx-startup, androidx WorkManager. Ninguno exportado.

## Limitaciones

- Firma **no verificada** (apksigner ausente); origen = Google Play (doc 08).
- Los componentes son mayormente del framework Veritran; la superficie funcional real está en la
  definición Veritran empacada, no en el manifiesto (doc 02).
