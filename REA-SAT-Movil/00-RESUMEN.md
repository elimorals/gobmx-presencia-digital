# SAT Móvil — Ingeniería inversa (REA)

> Análisis **estático** del bundle XAPK de `mx.gob.sat`. La app nunca se ejecuta.
> Herramientas: REA (apktool 3.0.3 + JADX headless 0.7.1) + `strings`/`unzip` sobre los `.so`.
> Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| App | **SAT Móvil** (Servicio de Administración Tributaria, oficial) |
| Paquete | `mx.gob.sat` |
| Versión | **2.4.4** (versionCode 35) |
| minSdk / targetSdk / compileSdk | 26 (Android 8) / 35 / 36 |
| Build (commit del repo, `assets/commit.txt`) | **`521065f7`** |
| Bundle | XAPK (split APKs estilo Google Play): base + `config.arm64_v8a` + `config.en` + `config.fr` + `config.mdpi` |
| APK base | 8,304,047 bytes · SHA-256 `ab3fcd6df84d1d45ce62fd9c5ceb79349e8d8b0958604f3d7e6fb92511e39939` |
| Split ABI | `config.arm64_v8a.apk` 27,771,789 bytes (contiene **todos** los `.so`) |
| Tamaño total bundle | 36,195,963 bytes |
| DEX | classes.dex (JADX reporta **5,534** clases top-level en base) |
| Framework | **Flutter** (`flutterEmbedding=2`), motor `libflutter.so` |
| Runtime Dart | **Dart VM 3.10.0 stable**, build 2025-11-06 (cadena en `libflutter.so`) |
| Paquete propio Dart | `package:sat_movil_2` |
| Firma APK | **no verificada** por las herramientas (origen APKPure; split base solo con `stamp-cert-sha256`) |
| Librerías nativas | `libapp.so` 11.1 MB (Dart AOT), `libflutter.so` 10.6 MB, `libtensorflowlite_c.so` 4.5 MB, `libdartjni.so`, `libimage_processing_util_jni.so`, `libsurface_util_jni.so`, `libdatastore_shared_counter.so` |

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| UI / app | **Flutter** (widgets Dart), gestión de estado **GetX** (`package:get`) + `rxdart` | `flutterEmbedding=2`, `package:flutter/` (445 refs), `package:get/`, `package:sat_movil_2/` (135) |
| Lenguaje | **Dart** compilado a **AOT ARM64** | `libapp.so` (snapshot), Dart VM 3.10.0 |
| Red | **dio** + **http** (API Dart) sobre **Cronet** (Chromium 139 mainline, confirmado en runtime); gateway **Zuul** del lado servidor | `package:dio/`, `package:http/`, `libmainlinecronet…so`, host `...movilzuul...` |
| Config | **flutter_dotenv** (`DotEnv`) carga `.env.production` | strings `DotEnv`, `.env.production` en `libapp.so` |
| Cripto local | **PointyCastle** (AES-GCM, RSA), **asn1lib** (.cer/.key), `crypto`, `encrypt`, `base32` | `package:pointycastle/` (157), `AES_GCM_NoPadding`, `AesCipher` |
| Almacenamiento seguro | **flutter_secure_storage** (Keystore/AES) | `package:flutter_secure_storage/` |
| Almacenamiento local | **sqflite** (SQLite), Jetpack **DataStore** (`libdatastore_shared_counter.so`) | `package:sqflite_common/`, `.so` |
| Biometría | **local_auth** (huella / Face ID) | `package:local_auth_platform_interface/`, permisos `USE_BIOMETRIC`/`USE_FINGERPRINT` |
| Identidad / ML | **CameraX** + **TensorFlow Lite** nativo (liveness SAT ID) | `libtensorflowlite_c.so`, `camera_android_camerax`, permisos cámara/audio/video |
| QR | **qr_code_scanner** + ZXing (`CaptureActivity`) | `package:qr_code_scanner/`, `com.journeyapps.barcodescanner` |
| PDF | **Syncfusion PDF** + **pdfx** + **printing** (acuses, CFDI) | `package:syncfusion_flutter_pdf/`, `package:pdfx/`, `package:printing/` |
| WebView | **flutter_inappwebview** (portal SAT embebido) + url_launcher | `com.pichillilorenzo.flutter_inappwebview_android.*` |
| Video | **youtube_player_iframe** (contenido educativo) | `package:youtube_player_iframe/`, host `i3.ytimg.com` |
| Background | **flutter_background_service** (foreground `dataSync`, reinicia al boot) | service + `BootReceiver` en manifest |
| Interop nativo | **jni / jnigen** (`libdartjni.so`) | `package:jni/`, `.so` |

## Mapa de módulos y superficie Android (DEX)

El DEX es casi todo embedding Flutter + plugins; **no hay paquetes de negocio del SAT en Java/Kotlin**.

| Origen | Rol en el DEX |
|---|---|
| `io.flutter.embedding.*` | Motor Flutter (FlutterActivity = `MainActivity`) |
| `io.flutter.plugins.imagepicker` / `urllauncher` | Selector de imágenes, abrir URLs |
| `com.pichillilorenzo.flutter_inappwebview_android` | WebView embebido (In-App Browser, Custom Tabs, TWA) |
| `id.flutter.flutter_background_service` | Servicio en primer plano + watchdog + boot |
| `dev.fluttercommunity.plus.share` | Compartir (acuses/PDF) con FileProvider |
| `net.nfet.flutter.printing` | Impresión con FileProvider |
| `com.journeyapps.barcodescanner` | Escáner QR/código de barras |
| `androidx.camera.*`, `androidx.work.*`, `androidx.room.*` | CameraX, WorkManager, Room |
| `mx.gob.sat.MainActivity` | **Único código propio**: registra plugins + 1 MethodChannel (ver `decompiled/`) |

## Mapa de hallazgos por documento

- `01-manifiesto.md` — 22 permisos, 1 activity exportada (launcher), 3 FileProviders, servicio foreground + boot.
- `02-arquitectura-flutter.md` — Flutter/Dart AOT, qué hay en cada `.so`, por qué los textos no están en `res/`.
- `03-autenticacion.md` — OAuth2 ROPC (`grant_type=password`), SAT ID, captcha propio, biometría local.
- `04-red-endpoints.md` — `.env` en claro, prod/UAT/dev, Zuul, IP de Azure filtrada, tabla completa de endpoints.
- `05-cripto-efirma.md` — e.firma portable, PointyCastle/asn1lib, AES-GCM, secure_storage.
- `06-biometria-satid.md` — cámara+audio+video, TensorFlow Lite, liveness/enrolamiento SAT ID.
- `07-sdks-dependencias.md` — SBOM de NOTICES (34,697 líneas), paquetes Dart, ausencia de Firebase/ThreatMetrix.
- `08-pantallas-flujos.md` — trámites (declaración anual, citas, chat, denuncias, CFDI, QR, CSF, buzón, régimen, obligaciones, sellos).
- `09-firma-recursos.md` — firma no verificada, estructura del bundle, assets, procedencia.
- `10-extraccion-profunda.md` — **árbol de código Dart (129 archivos)**, decode con smali, y correcciones de precisión al resto.
- `11-dinamica.md` — **ejecución en dispositivo físico** (Android 16, sin root): Flutter/Impeller vivo, **TFLite no carga al arranque**, gate de T&C, y límites (sin root → sin captura TLS; frontera de PII).

## Contraste con el resto del inventario (mismo pipeline REA)

| Dimensión | JW Library | CFE Contigo | **SAT Móvil** |
|---|---|---|---|
| Framework | Nativo Android | Nativo Android (Kotlin) | **Flutter / Dart AOT** |
| Endpoints en claro | Sí (prod/dev/QA) | **No** (DexGuard string encryption) | **Sí — en `.env` empaquetados** |
| Ofuscación de código | R8 parcial | **DexGuard** + packer nativo | **Ninguna** sobre Dart (strings legibles); R8 solo en el thin DEX |
| Anti-fraude / hardening | — | **ThreatMetrix** + `.so` packed | **Nada** (sin pinning, sin anti-root) |
| Dominio funcional | Lectura de contenido | Financiero (pagos/tarjetas) | **Fiscal/identidad** (e.firma, SAT ID, CFDI) |
| Cripto de cliente | libsodium | AndroidKeyStore + AES | **PointyCastle + asn1lib** (firmado e.firma en el dispositivo) |
| ML on-device | — | — | **TensorFlow Lite** (liveness) |
| Fuga de infraestructura | Dominios dev/QA | — | **IP de Azure UAT/dev hardcodeada** |

La lectura corta: SAT Móvil es, de las apps analizadas, la **más transparente para el analista
estático** (todo legible) y a la vez la que más datos sensibles maneja (FIEL, biometría). La
combinación —superficie alta + datos de alto valor + endurecimiento débil— es su rasgo distintivo.
