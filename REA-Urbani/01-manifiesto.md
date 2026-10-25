# 01 — Manifiesto: permisos, componentes, superficie de ataque

Manifiesto decodificado completo en [AndroidManifest.xml](AndroidManifest.xml) (222 líneas).

## Atributos de `<application>`

```
android:name="o.access301"                              ← Application ofuscada (shell del packer)
android:appComponentFactory="androidx.core.app.CoreComponentFactory"
android:allowBackup="false"                             ← correcto (sin respaldo)
android:usesCleartextTraffic="true"                     ← ver matiz en doc 04 (NSC lo anula)
android:networkSecurityConfig="@xml/network_security_config"
android:requestLegacyExternalStorage="true"            ← almacenamiento legacy (riesgo, doc 10)
android:zygotePreloadName="o.AppCompatTextHelperApi24Impl"
```

## Permisos declarados

| Grupo | Permisos |
|---|---|
| Red | INTERNET, ACCESS_NETWORK_STATE |
| Ubicación | ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION |
| NFC | NFC (+ `uses-feature` nfc, nfc.hce, no requeridas) |
| Bluetooth | BLUETOOTH/ADMIN (≤30), BLUETOOTH_SCAN (neverForLocation), ADVERTISE, CONNECT; `bluetooth_le` **requerido** |
| Cámara / audio | CAMERA, RECORD_AUDIO (KYC facial) |
| Biometría | USE_BIOMETRIC, USE_FINGERPRINT |
| Almacenamiento | READ/WRITE_EXTERNAL_STORAGE, WRITE_SETTINGS |
| Servicios en 2º plano | FOREGROUND_SERVICE, FOREGROUND_SERVICE_DATA_SYNC, WAKE_LOCK, RECEIVE_BOOT_COMPLETED, SCHEDULE_EXACT_ALARM (≤33) |
| Push | POST_NOTIFICATIONS, `com.google.android.c2dm.permission.RECEIVE` (FCM) |
| Publicidad | `com.google.android.gms.permission.AD_ID` |
| Propios | `com.miruta.mty.permission.liantian.RECEIVE` (**packer Baidu**, `signatureOrSystem`), `VERIFF_STATUS_BROADCAST_PERMISSION` (`signature`), `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (`signature`) |

`WRITE_SETTINGS` + `requestLegacyExternalStorage` + BLE requerido son los permisos más amplios de
la lista. CAMERA/RECORD_AUDIO sostienen el KYC facial (doc 06); NFC+BLE sostienen la tarjeta de
transporte y la lectura de validadores (doc 05).

## Deeplinks

Un solo esquema propio, en `MainActivity` (`exported="true"`):

```
urbani://navigate            (scheme="urbani", host="navigate")
```

No se observaron App Links (autoVerify) ni más hosts en el manifest. Cualquier otra ruta de
deeplink que maneje la app vive en el Dart cifrado y no se recuperó.

## Componentes exportados (superficie de ataque)

| Componente | exported | Nota |
|---|---|---|
| `.MainActivity` | **true** | launcher + deeplink `urbani://navigate` |
| `io.flutter.plugins.nfc_host_card_emulation.AndroidHceService` | **true** | servicio APDU HCE (protegido por `BIND_NFC_SERVICE`, system) |
| `io.flutter.plugins.firebase.messaging.FlutterFirebaseMessagingReceiver` | **true** | FCM (protegido por `c2dm.permission.SEND`) |
| `com.google.firebase.iid.FirebaseInstanceIdReceiver` | **true** | FCM (protegido por `c2dm.permission.SEND`) |
| `com.transistorsoft.tsbackgroundfetch.FetchJobService` | **true** | protegido por `BIND_JOB_SERVICE` |
| `androidx.profileinstaller.ProfileInstallReceiver` | **true** | protegido por `android.permission.DUMP` |

Los exportados "peligrosos" (HCE, FCM, JobService) están todos **protegidos por permisos de
sistema/firma**; no son invocables por una app arbitraria. La única superficie realmente abierta es
`MainActivity` (launcher + deeplink), que es Flutter: el enrutamiento del deeplink lo resuelve el
Dart cifrado, así que no se puede evaluar estáticamente si valida parámetros.

## Proveedores de contenido

| Provider | authority | exported | Nota |
|---|---|---|---|
| `androidx.core.content.FileProvider` | `com.miruta.mty` | false | `@xml/file_paths` → `cache-path path="/"` (doc 04/10) |
| `dev.fluttercommunity.plus.share.ShareFileProvider` | `com.miruta.mty.flutter.share_provider` | false | share_plus |
| `com.baidu.liantian.LiantianProvider` | `com.miruta.mty.liantian.ac.provider` | false | **packer Baidu** |
| varios `*initprovider` (firebase, mlkit, startup) | — | false | inicialización |

## Actividades de KYC embebidas (no exportadas)

Baidu Face (`com.baidu.idl.face.platform.ui.*`: liveness color/silence/action/distance,
collection success/failure) y `com.example.baidu_face_sdk.HybridLivenessActivity`, más
`com.veriff.sdk.views.base.verification.VeriffActivity`. Todas `exported="false"`. Detalle en
doc 06.
