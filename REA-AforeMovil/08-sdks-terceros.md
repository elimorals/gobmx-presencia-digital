# 08 — SDKs de terceros

Inventario de dependencias observadas (manifiesto, `BuildConfig`, paquetes JADX y `.so`).

## Identidad y biometría

| SDK | Evidencia | Uso |
|---|---|---|
| **Daon IdentityX / FIDO (UAF)** | `com.daon.fido.client.sdk.*` (manifest), `com.daon.sdk.fido.service.rest.HTTP`, ruta `IdentityXServices/rest/v1/secured/users` | Autenticación FIDO, GestoPago (docs 02) |
| **Daon xFace** | `com.daon.face.{detector,quality,maskdetector,passivelivenessv1,authentication}`, `libDaonFace*.so`, `libVDFaceVerificationAPI-jni.so` | Reconocimiento facial + liveness pasivo (doc 03) |
| **Daon IDCapture / Document** | `com.daon.idcapture`, `com.daon.sdk.document`, `libDaonIDCapture.so` | Captura de identificación |
| **Daon crypto/device** | `com.daon.sdk.crypto.*`, `com.daon.sdk.device`, `SecureKeyStore` | Keystore por hardware, tool checker |
| **Sensory** | `com.sensory.datautils`, `com.sensory.vvutils`, strings `VoiceRecognizer*` | Reconocimiento de voz |
| **Google ML Kit (Vision)** | `com.google.mlkit.vision.DEPENDENCIES=ocr, barcode` | OCR + PDF417 del INE (doc 03) |

## Google / Firebase

| SDK | Evidencia | Uso |
|---|---|---|
| **Firebase Crashlytics** | `BaseApp.onCreate` (collection ON), registrars en manifest | Reporte de crashes |
| **Firebase Cloud Messaging** | `PushNotificationService`, `FirebaseMessagingService`, `FirebaseInstanceIdReceiver` | Push |
| **Firebase Analytics / Measurement** | `AnalyticsConnectorRegistrar`, `AppMeasurement*` (manifest) | Analítica |
| **Firebase Sessions / Installations / Datatransport** | registrars en `ComponentDiscoveryService` | Plataforma Firebase |
| **Google Maps** | `com.google.android.geo.API_KEY` (manifest, key redactada), query a `com.google.android.apps.maps` | Sucursales / citas |
| **Google Play Core (Feature Delivery)** | `SplitCompat`, `PlayCoreDialogWrapperActivity`, `splits.required` | Módulos dinámicos (doc 07) |
| **Play Integrity** (referencia) | string `play/integrity/.../IntegrityErrorCode` | Posible verificación de integridad (confirmar en runtime) |
| **Install Referrer** | permiso `BIND_GET_INSTALL_REFERRER_SERVICE` | Atribución |

## Telemetría / APM

| SDK | Evidencia | Uso |
|---|---|---|
| **Dynatrace OneAgent** | `com.dynatrace.agent.*`, `com.dynatrace.android.api.*`, `Callback.onCreate`, beacon `bf31153cbh.bf.dynatrace.com/mbeacon`, `BuildConfig.DYNATRACE_*` | APM (DEV + PROD) |
| **TrustX (Daon cloud)** | `mobilemetrics.*.trustx*.com/api/mobile-metrics-global` | Métricas de sesión biométrica |

## UI / plataforma / utilidades

| SDK | Evidencia | Uso |
|---|---|---|
| **Jetpack Compose + Material3** | temas `Theme.Material3.*`, botones m3/m3expressive en `res/xml` | UI |
| **AndroidX WorkManager** | servicios/receivers `androidx.work.*` (manifest) | Trabajo en segundo plano |
| **AndroidX Room** | `MultiInstanceInvalidationService` | DB (además de Realm) |
| **Realm** | `librealm-jni.so`, `io.realm`/`RealmConfigs` | DB de objetos (doc 04) |
| **Jetpack DataStore** | `DataStoreManager`, `libdatastore_shared_counter.so` | Preferencias |
| **CameraX / Camera2** | `androidx.camera.*`, `uses-library androidx.camera.extensions.impl` | Cámara (captura) |
| **Karumi Dexter** | `com.karumi.dexter.*`, `DexterActivity` | Permisos en runtime |
| **OkHttp** | `okhttp3.internal.platform.PlatformInitializer` | Cliente HTTP (doc 05) |
| **PDFBox-Android (tom_roush)** | `assets/com/tom_roush/pdfbox/*` | Generación/lectura de PDF (constancias) |
| **org.apache.http.legacy** | `uses-library` (opcional) | Compatibilidad HTTP legada |

## Notas de privacidad

- Una app del SAR con **`AD_ID` + AdServices attribution** y **Firebase Analytics** recopila
  identificadores de publicidad; valorar necesidad frente a datos financieros/biométricos.
- Dos pilas de telemetría de terceros (Dynatrace + TrustX) además de Firebase: el tráfico de
  diagnóstico sale a infraestructuras distintas a la de Procesar.
