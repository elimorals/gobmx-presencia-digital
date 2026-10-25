# 03 — Biometría e identidad (prueba de vida + documento)

El diferenciador de AforeMóvil es el **onboarding biométrico remoto**: el alta y la recuperación
exigen **prueba de vida facial** y **lectura de la identificación** (INE/IFE), además de voz. Se
apoya en tres proveedores: **Daon xFace/IdentityX**, **Sensory** y **Google ML Kit**.

## Daon xFace / IdentityX (reconocimiento y liveness facial)

Librerías nativas en `config.armeabi_v7a.apk` (y equivalentes en arm64):

| `.so` | Función |
|---|---|
| `libDaonFaceDetector.so` | Detección de rostro |
| `libDaonFaceV3.so`, `libVDFaceVerificationAPI-jni.so` | Verificación/match facial |
| `libDaonFacePassiveLivenessV1.so` | **Liveness pasivo** (anti-spoofing sin gestos) |
| `libDaonFaceQuality.so` | Calidad de captura |
| `libDaonFaceMaskDetector.so` | Detección de mascarilla |
| `libDaonIDCapture.so` | Captura de documento de identidad |
| `libFaceIad.so`, `libFaceToolChecker.so`, `libtoolChecker.so` | Presentación/IAD y verificación de integridad del entorno |
| `librenderscript-daon.so`, `librenderscript-xpd.so`, `libszell.so`, `libsmma.so` | Procesamiento de imagen |

Paquetes Java (BuildConfig confirmados): `com.daon.face.detector`, `com.daon.face.quality`,
`com.daon.face.maskdetector`, `com.daon.face.passivelivenessv1`, `com.daon.face.authentication`,
`com.daon.idcapture`, `com.daon.sdk.device`, `com.daon.sdk.document`.

Captura por cámara mediante `com.daon.sdk.document.cameraxframeprovider.CameraXFrameProviderActivity`
y `com.daon.sdk.provider.camera2.Camera2FrameProviderActivity` (ambas no exportadas) +
`com.daon.sdk.authenticator.capture.CaptureActivity`. Licencia Daon embebida:
`com.daon.sdk.face.license.License` (clave en `libidx_facial_keys.so` / `libkeys.so`).

## Módulo de identidad propio (Procesar PS)

| Clase | Rol |
|---|---|
| `mx.com.procesar.ps.facial.ui.main.FacialActivity` | **Pantalla de captura facial** (exportada — doc 01) |
| `mx.com.procesar.ps.facial.base.activity.IdxFacialCaptureActivity` | Captura IDx de identidad (exportada) |
| `mx.com.procesar.ps.facial.ui.customViews.overlay.FaceOverlayView` | Guía visual del encuadre |
| `mx.com.procesar.ps.facial.data.model.documents.DocumentToScan` | Documento a escanear (anverso/reverso) |
| `...documents.IdentificationOcrData` | **Datos OCR** extraídos de la identificación |
| `...documents.ScanResult`, `ScannedDocumentInfo` | Resultado de escaneo |
| `...data.model.user.UserData` | Datos del usuario capturados |
| `mx.com.procesar.ps.idxfacial.services.*`, `...idxfacial.ui.*` | Servicios/UI de IdentityX facial |
| `mx.com.procesar.ps_take_documents.activity.TakeDocumentsActivity` | **Captura de documentos** (INE/IFE) |
| `com.procesar.aforemovil.commons.data.model.dto.FacialImageResultDTO` | DTO del resultado facial hacia backend |
| `com.procesar.aforemovil.commons.data.realm.model.FilenetImagesStatus` | Estado de subida de imágenes a **IBM FileNet** |

## OCR y código de barras (lectura del INE)

`meta-data com.google.mlkit.vision.DEPENDENCIES = "ocr, barcode"` → **ML Kit** descarga/usa los
modelos de OCR (texto del INE) y de **barcode/PDF417** (el reverso del INE lleva un PDF417 con
los datos del ciudadano). `IdentificationOcrData` recibe lo leído; `DocumentSupport`
(en `status.captureidentification`) enumera los tipos de documento admitidos.

## Sensory (voz)

BuildConfig `com.sensory.datautils`, `com.sensory.vvutils`; strings del DEX
`VoiceRecognizerState_getEndpoints`/`getNumEndpoints` (aquí “endpoints” = *speech endpoints*, no de
red). Indica un **reconocedor de voz Sensory** (verificación o comandos por voz). No se declara el
permiso `RECORD_AUDIO` en este manifiesto, por lo que su activación en runtime debe confirmarse.

## Pipeline de alta (reconstruido, estático)

1. `preexistence` resuelve si ya existe cuenta.
2. `register` orquesta el alta → lanza captura.
3. **Documento**: `TakeDocumentsActivity` + Daon IDCapture + ML Kit OCR/PDF417 → `IdentificationOcrData`.
4. **Rostro**: `FacialActivity`/`IdxFacialCaptureActivity` + Daon detector/quality/**passive liveness**/mask → `FacialImageResultDTO`.
5. Enrolamiento **FIDO/UAF** (doc 02) con clave en keystore por hardware (doc 04).
6. Imágenes a **FileNet** (`FilenetImagesStatus`); telemetría de la sesión de captura a **TrustX** (doc 05).

Confirmado en `FacialActivity.onCreate` (decompilado en
[`decompiled/FacialActivity.onCreate.java`](decompiled/FacialActivity.onCreate.java)): se cablean
por Dagger los casos de uso `CreateRegistrationChallenge`/`UpdateRegistrationChallenge`,
`CreateAuthenticationRequest`/`UpdateAuthenticationRequest`, `GetLifeTests` (prueba de vida),
`CreateIdCheck`, `SearchFace`, `UploadPhoto`, `CreateEvaluation`, `GetDocuments`/`UploadDocument` y
`DocumentOcrData`, más `Fido.getInstance(...)`. La respuesta `CreateEvaluation.ItemCreateEvaluation`
alimenta la decisión de identidad. **La pantalla no valida sesión local** (ver doc 02): el control
recae en las credenciales de usuario que la capa de red de IdentityX inyecta.

## Privacidad

Se capturan y transmiten **biométricos faciales y una identificación oficial** — datos personales
sensibles. El liveness es pasivo (menos fricción, buen anti-spoofing). La imagen facial sale como
`FacialImageResultDTO` y las imágenes del documento van a FileNet; confirmar cifrado en tránsito y
retención es trabajo dinámico/legal fuera de este pase.
