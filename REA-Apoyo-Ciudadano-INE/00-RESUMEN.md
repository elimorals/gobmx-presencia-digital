# Resumen ejecutivo — Apoyo Ciudadano - INE 5.93

## Identidad

| Campo | Valor |
|---|---|
| Nombre | Apoyo Ciudadano - INE |
| Package | `mx.ine.apoyociudadano` |
| Versión | 5.93 (versionCode 236) |
| minSdk / targetSdk | 28 / 36 |
| Dependencia | Instituto Nacional Electoral (INE) |
| Instalaciones | ≈500 000+ (Google Play, #26 del inventario) |
| Firma | **Auténtica** — `O=Instituto Nacional Electoral` (verifica) |
| SHA-256 (APK arm64-v8a) | `d06daf0d78df4bab89e47b2c71a221cd49f8c539f2f3d446c5618253345497fe` |

## Para qué sirve

Es la app con la que **auxiliares/aspirantes captan apoyo ciudadano**: recolectan el respaldo
(firma) de ciudadanos para registrar candidaturas independientes, consultas o iniciativas. Por
cada apoyo, el auxiliar **fotografía la credencial para votar** del ciudadano (anverso/reverso),
la app **extrae los datos por OCR**, captura una **foto en vivo** y la **firma** del ciudadano, y
sube todo al INE.

## Pila técnica

- **Nativa Android (Kotlin/Java)**, 3 DEX, 14 034 clases. El package propio `mx.ine.apoyociudadano.*`
  está **sin ofuscar** (nombres de clases/métodos/activities legibles) → decompilación directa.
- **Red: Square Retrofit 1.x** (`retrofit.RestAdapter`, `retrofit.mime.TypedFile/TypedString`) +
  **RxJava 1** (`rx.Observable`). Pila EOL.
- **Identidad: SDK `com.personalcode`** (`PersonalCodeOffline`, `PersonalCodeInterface`,
  `ScannerActivity`) — mismo proveedor que la app Valida INE-QR — + **tesseract** (OCR,
  `eng.traineddata`/`ocr.traineddata`), **cardIO** (lectura de tarjeta), **OpenCV**
  (`libopencv_java3.so`). Captura/lectura de credencial, en gran parte **nativa**.
- **Almacenamiento local: Realm** (`librealm-jni.so`).
- **Terceros:** Firebase (Analytics, Crashlytics, Remote Config, Firestore), Facebook SDK,
  Twitter, Google Play Integrity, AdServices.

## Flujo de datos (lo esencial)

1. Login del auxiliar (`logIn` / `loginComplemento`, con captcha y OTP SMS).
2. Captura de la credencial del ciudadano: SDK PersonalCode + OCR tesseract + cardIO/OpenCV
   (`CapturaQRsCifradosKt`, `CredentialCaptureActivity`, `OcrProcessingActivity`,
   `LivePhotoActivity`, `SignActivity`).
3. Empaquetado y **envío al INE**: `SessionService.uploadZip(TypedFile, …)` /
   `generaCodigo(File, …)` / `loginComplemento(File, …)` — multipart con la foto/paquete.
4. Consulta de avance (`consultaEnviados`), sincronización (`synchronize`), baja de dispositivo.

## Hallazgos (resumen)

- **P1** — Sube PII electoral de terceros (credencial del ciudadano) empaquetada; superficie de
  datos sensible (doc 03, 08).
- **P2** — Endpoints + llaves de Twitter **cifrados en `config.properties`** con llave en el APK
  (ofuscación, no protección real) (doc 04, 08).
- **P2** — **`AES/ECB/NoPadding`** presente (modo débil) (doc 04, 08).
- **P3** — **Retrofit 1.x** (EOL, sin parches) (doc 03, 08).
- **P3** — Recolección de **datos sociales (Facebook/Twitter) + geolocalización + estado del
  teléfono** y bitácora a Firestore (doc 05, 08).
- **Positivo** — Firma auténtica del INE; indicadores de validación TLS estricta / pinning (doc 07, 03).

## Qué NO se hizo

Estático puro. No se descifraron los valores de `config.properties` (solo se documentó el mecanismo
y que la llave está en el APK). No se ejecutó la app ni se llamó a endpoints. La lógica interna del
SDK nativo PersonalCode y de las `.so` (OpenCV/tesseract/cardIO) es un **muro nativo**: se documenta
su presencia y rol, no su implementación.
