# 05 — SDKs y librerías de terceros

Paquetes de terceros observados en el smali decodificado (`apktool`) y en el inventario de
clases (REA/JADX):

| SDK / librería | Paquete | Rol | Nota |
|---|---|---|---|
| **PersonalCode** | `com.personalcode(.qrdetection/.scanner/.verification)` | Wrapper de validación de credencial; fachada `Azf` con cuota y licencia | Propietario; núcleo en nativo (doc 02, 03) |
| **Scandit** | `com.scandit.recognition` | **Motor comercial de reconocimiento** (QR/barcode) | **Confirmado**: `com.scandit.recognition.Native` es el binding JNI (SWIG, 121 métodos `sc_*`); origen de los `.so` `libO0p`/`libl1I` |
| **ReLinker** | `com.getkeepsafe.relinker` | Carga robusta de librerías nativas `.so` | Coherente con `extractNativeLibs=false` |
| **ML Kit (OCR)** | (vía Google Play Services) | Reconocimiento de texto impreso de la credencial | `meta-data DEPENDENCIES=ocr` (doc 01) |
| **CameraX** | `androidx.camera.*` + `libimage_processing_util_jni.so` | Captura de cámara para el escaneo | Google/AndroidX |
| **android-gif-drawable** | `libpl_droidsonroids_gif.so` | Reproducción de GIF en la UI (tutorial/overlay) | pl.droidsonroids |
| **AndroidX / Kotlin** | `androidx.*`, `kotlin*`, `kotlinx.coroutines` | Base del app | — |

## Lectura

- El stack de escaneo real es **comercial**: PersonalCode (wrapper con licencia y cuota)
  montado sobre **Scandit** (motor de reconocimiento). Esto explica tanto la ofuscación de
  los `.so` como el esquema de licencia firmada (doc 03): es la forma típica de distribuir un
  SDK de pago. ReLinker se usa para cargar esos `.so` de forma fiable.
- **No se observaron SDKs de analítica/publicidad/crash reporting de terceros** (Firebase
  Analytics, Crashlytics, AppsFlyer, Facebook, etc.) en los paquetes del smali. Combinado con
  la ausencia de permiso `INTERNET` (doc 01), es consistente con una app que **no hace
  telemetría**. (Afirmación acotada a lo observado en el inventario estático; no excluye
  código ofuscado que no se nombre como tal.)
- **ML Kit OCR** se apoya en Google Play Services del dispositivo; su posible descarga de
  modelo la gestiona el framework, no un socket abierto por la app.

> Evidencia: directorios de paquetes en el smali (`com/scandit/recognition`,
> `com/getkeepsafe/relinker`, `com/personalcode/*`); `.so` en el split `config.arm64_v8a.apk`;
> `meta-data` de MLKit en el manifiesto.
