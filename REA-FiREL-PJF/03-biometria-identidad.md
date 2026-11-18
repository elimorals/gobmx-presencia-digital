# Biometría y flujo de identidad

Reconstruido a partir de los **modelos de red** propios (`com.fielnet…data.network.model` /
`…services.model`, 131 clases) y las Activities DataBinding. Mapa completo en
`decompiled/flujo-identidad-clases.txt`.

## Qué captura (PII extrema)

| Dato | Clases / evidencia |
|---|---|
| **INE (credencial)** | `RequestRegisterINE`, `RequestRegisterInfoINE`, `ResponseOCRData`, `VerifyDataOcr`, `ActivityDocumentReviewOcr` → lectura por **OCR** + foto del documento |
| **INE + rostro + huellas (combinado)** | `RequestRegisterINERostroHuellas`, `ResponseINEFingerFace` |
| **Rostro** | `RequestFaceMatch`, `RequestRegisterPhoto`, `ResponseFace`, `ResponseFaceMatch`, `DataAttemptsFace`, `ActivityFaceIdenty`, `ActivityFaceSimilarity` → **match + liveness** (intentos, anti-spoof) |
| **Huellas (sin contacto)** | `RequestRegisterFingers`, `RequestRegisterFingerIndex`, `NfiqFingers` (calidad NFIQ), `ObjectFIngerIdenty`, Identy `CaptureFingersActivity`/`EnrollThumbActivity`, `TouchlessActivityIntroFourFingers`, `TouchlessSpoofDialog` |
| **Firma autógrafa** | `RequestRegisterSignature`, `RequestReportFaceSignature`, `ActivityHandwrittenSignature` |
| **Video de verificación** | `RequestSendVideoMeet` (`RECORD_VIDEO`) — captura/envía un video (verificación remota tipo videollamada) |
| **Ubicación** | `RequestCoordinates`, `ResponseCoordinates`, `ActivityGetLocation` (`ACCESS_FINE_LOCATION`) |
| **Certificado (e.firma/FIEL)** | `RequestGenerateCertificate`, `ResponseGenerateCertificate` — **genera el certificado** tras el enrolamiento |

Ciclo de vida de la identidad: `RequestCreateID` · `RequestEnrrolarID` · `RequestAddBiometric` ·
`RequestRemoveID` · `RequestConsultData`. Verificación tipo **ABIS** (`ResponseCreateIDAbis`,
`RequestEnvelopeFaceAbis`) → se compara contra una base biométrica (deduplicación:
`DeduplicationIdentyResponse`).

## SDK biométrico (Identy.io)

- Huella **sin contacto** (touchless, captura de 4 dedos con la cámara) y rostro, con modelos
  **ONNX** on-device (`libonnxruntime.so` + `assets/identy_data/*.ort`) y libs nativas
  (`libfinger-native-lib.so`, `libface-native-lib.so`).
- Licenciamiento online contra `licensemgr.identy.io` / `bancolicensemgr.identy.io` (doc 04).
- Anti-spoofing presente (`TouchlessSpoofDialog`, `DataAttemptsFace`).

## Valoración

Es, junto con BanjeCel, la app con **biometría más completa del acervo** (INE + rostro + huella +
firma + video), pero a diferencia de BanjeCel aquí el dato **sale del dispositivo** hacia varios
backends/terceros (doc 04). El riesgo no es la captura (está bien hecha, con liveness), sino
**cómo se transmite y a quién** — y la postura TLS contradictoria (doc 04) es lo que convierte
eso en una preocupación real.

> Alcance: los `.ort` son modelos (pesos), no biometría de personas. No se extraen ni
> redistribuyen. El flujo se infiere de nombres de clase/modelo; no se ejecutó la app.
