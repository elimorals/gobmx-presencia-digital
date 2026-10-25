# 08 — SDKs de terceros (inventario)

Inventario por **librería nativa** (`.so` del split `armeabi-v7a`) y por **clases DEX** (JADX).
El código propio de la app es Dart (cifrado, doc 03); esta es la superficie de capacidades que sí se
recupera.

## Librerías nativas (`config.armeabi_v7a.apk`)

| `.so` | Tamaño | Origen / función |
|---|---|---|
| `libapp.so` | 29,016,941 | **Dart AOT** de la app (cifrado, doc 02/03) |
| `libflutter.so` | 7,545,485 | **Flutter engine** |
| `libbarhopper_v3.so` | 3,244,440 | **ML Kit** barcode/QR (barhopper v3) |
| `libbdface_sdk.so` | 2,925,072 | **Baidu Face SDK** (doc 06) |
| `libobjectbox-jni.so` | 1,645,568 | **ObjectBox** (base de datos local embebida) |
| `libed66.so` | 1,438,000 | **no identificada** (doc 03) |
| `libliantiansec.so` | 380,708 | **Baidu LianTian** (packer, doc 03) |
| `libbd_facecollect_unifylicense.so` | 247,664 | **Baidu Face** (licencia) |
| `libflutter-support.so` | 197,837 | soporte Flutter |
| `libbaiduprotect_sdk-f57be6…2796441.so` | 125,764 | **Baidu Protect** (packer) |
| `libcrashlytics.so` / `-common` / `-handler` / `-trampoline` | 94,813 / 435,732 / 87,908 / 6,912 | **Firebase Crashlytics NDK** |
| `libliantian.so` | 50,956 | **Baidu LianTian** (packer) |
| `libbaiduprotect_sec_jni_v3_lite_face.so` | 50,748 | **Baidu Protect** (jni face) |
| `libimage_processing_util_jni.so` | 20,380 | **CameraX** |
| `libsurface_util_jni.so` | 3,440 | **CameraX** |
| `libdatastore_shared_counter.so` | 4,416 | Jetpack **DataStore** |

## Plugins Flutter (DEX `io.flutter.plugins.*`)

| Plugin | Capacidad |
|---|---|
| `firebase.{core,crashlytics,messaging,firebaseremoteconfig,storage,appcheck,installations}` | Firebase (doc 07) |
| `googlemaps` | **Google Maps** (markers, polylines, heatmaps, clusters) |
| `pay_android` | **Google Pay** (doc 07) |
| `nfc_host_card_emulation` | **tarjeta NFC HCE** (doc 05) |
| `localauth` | biometría local (huella/rostro) |
| `webviewflutter` | WebView (pigeon proxy completo) |
| `videoplayer` | ExoPlayer (video) |
| `urllauncher` | abrir URLs / `WebViewActivity` |
| `pathprovider` | rutas de archivos |
| `sharedpreferences` | preferencias |
| `flutter_plugin_android_lifecycle` | ciclo de vida |

## Otros SDKs (clases DEX / manifest)

| SDK | Evidencia | Función |
|---|---|---|
| **Veriff** | `com.veriff.sdk.*` | KYC de identidad (doc 06) |
| **Baidu Face** | `com.baidu.idl.face.platform.ui.*`, `com.example.baidu_face_sdk.*` | liveness facial (doc 06) |
| **Baidu LianTian** | `com.baidu.liantian.*` (~187 clases) | packer / anti-tamper (doc 03) |
| **ML Kit** | `com.google.mlkit.vision.{barcode,face,common}` | visión (QR, rostro) |
| **ZXing / journeyapps** | `com.journeyapps.barcodescanner.CaptureActivity` | escaneo de códigos |
| **Geolocator** | `com.baseflow.geolocator.GeolocatorLocationService` (FG `location`) | ubicación |
| **transistorsoft background_fetch** | `com.transistorsoft.tsbackgroundfetch.*` | tareas en 2º plano |
| **share_plus** | `dev.fluttercommunity.plus.share.*` | compartir |
| **flutter_local_notifications** | `.flutterlocalnotifications.ForegroundService` | notificaciones locales |
| **flutter_secure_storage** | `backup_rules.xml` excluye `FlutterSecureStorage` | tokens/secretos cifrados (Keystore) |
| **Play Core** | `com.google.android.play.core.*` | entrega de splits |
| **CameraX** | `androidx.camera.*` + `libimage_processing_util_jni.so` | cámara |

## Lectura

El stack mezcla infraestructura Google estándar de Flutter (Firebase, Maps, Pay, ML Kit, CameraX) con
un **bloque chino de Baidu** (packer LianTian + Face SDK + Protect) que es lo distintivo y lo más
sensible de esta app (docs 03, 06, 10).
