# 02 — Captura de identidad: PersonalCode + OCR + cardIO + OpenCV

La función central de la app es **capturar la credencial para votar** del ciudadano que otorga su
apoyo y extraer sus datos. Esto se hace con una pila mayoritariamente **nativa**.

## Componentes de captura (código propio decompilado)

- `mx.ine.apoyociudadano.CapturaQRsCifradosKt` — pantalla de captura. Campos decompilados:
  - `com.personalcode.PersonalCodeInterface f19978o`
  - `com.personalcode.PersonalCodeOffline f19981c`
  - `bi.a f19980b` (adaptador de API Retrofit)
  - métodos `S(PersonalCodeInterface)`, `U(Activity)`, `X(Activity): boolean` → integra el SDK.
- `mx.ine.apoyociudadano.activity.CredentialCaptureActivity`, `CredentialCiudadanoActivity`,
  `CredentialInfoFormActivity`, `CredentialTypeActivity`, `CredentialbActivity`.
- `mx.ine.apoyociudadano.activity.OcrProcessingActivity` — procesamiento OCR.
- `mx.ine.apoyociudadano.activity.LivePhotoActivity` — foto en vivo (cámara frontal).
- `mx.ine.apoyociudadano.activity.SignActivity` / `FirmaAuxActivity` — firma.
- `mx.ine.apoyociudadano.utils.barcode.BarcodeCaptureActivity` — lectura de código/QR.
- Modelos: `CredentialInformation`, `CredentialOcrInformation`, `CredentialBorders`,
  `DatosCiudadano`, `DatosAuxiliar` → estructura de lo que se extrae de la credencial.

## Librerías nativas de identidad (`lib/arm64-v8a/`)

| Librería | Rol |
|---|---|
| `libPersonalCode.so` (5.7 MB) | SDK propietario `com.personalcode.*` (captura/validación de credencial) |
| `libtess.so` + `liblept.so` + `res/*.traineddata` | **tesseract OCR** (leptonica) — lectura de texto de la credencial |
| `libcardioRecognizer.so`, `libcardioRecognizer_tegra2.so`, `libcardioDecider.so` | **cardIO** — reconocimiento de tarjeta/credencial |
| `libopencv_core.so`, `libopencv_imgproc.so`, `libopencv_java3.so` (14 MB) | **OpenCV 3** — procesamiento de imagen (detección de bordes, encuadre) |
| `libimagepipeline.so`, `libjpgt.so`, `libpngt.so` | Pipeline de imagen (Fresco/decodificadores) |
| `librealm-jni.so` | Realm (BD local, ver doc 04) |

Datos OCR embebidos: `res/*.traineddata` (modelos tesseract, uno de ~21 MB `AV.traineddata`),
y referencias `tesseract/tessdata/eng.traineddata`, `tesseract/tessdata/ocr.traineddata`,
`eng.user-words`.

## Qué se extrae y a dónde va

El SDK PersonalCode + tesseract + OpenCV producen `CredentialOcrInformation` (datos OCR de la
credencial: previsiblemente CIC/OCR, nombre, sección, etc. — la estructura de campos exacta vive en
el modelo y en el SDK nativo). La foto en vivo (`LivePhotoActivity`) y la firma (`SignActivity`) se
suman. Todo se empaqueta y se sube al INE (ver doc 03, flujo `uploadZip`/`generaCodigo`).

## Muro nativo

La **lógica de reconocimiento/validación de la credencial** (PersonalCode, cardIO, el pipeline OCR)
corre en las `.so` nativas y **no es auditable desde el DEX**: el DEX invoca al SDK por JNI
(`PersonalCodeInterface`/`PersonalCodeOffline`) y recibe estructuras ya pobladas. Documentar el
mecanismo interno del `.so` requeriría análisis nativo con desensamblado, fuera del alcance estático
del DEX. Se documenta su presencia y rol; no se infiere su implementación.
