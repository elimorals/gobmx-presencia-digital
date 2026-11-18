# Ingeniería inversa de Apoyo Ciudadano - INE 5.93 — índice

Análisis **estático** del APK `mx.ine.apoyociudadano` (base del bundle, arm64-v8a) con REA
(JADX headless 0.7.1) + apktool + extracción de assets. La app **nunca se ejecutó**. Es la app
oficial del Instituto Nacional Electoral para la **captación de apoyo ciudadano** (recolección de
firmas de respaldo para aspirantes a candidaturas y consultas), **#26 por instalaciones** del
inventario gob.mx (≈500 K+ en Google Play).

> A diferencia de Valida INE-QR (lectura 100% local, sin red), **Apoyo Ciudadano sí sube datos**:
> captura la credencial del ciudadano que respalda (foto anverso/reverso, OCR, foto en vivo y
> firma), **la empaqueta en un ZIP y la transmite** al backend del INE. El código propio (14 034
> clases en total, package `mx.ine.apoyociudadano.*` **sin ofuscar**) se decompiló función por
> función: catálogo completo de la API, pila de identidad y manejo de datos sociales.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila, flujo de datos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (cámara + ubicación + estado del teléfono), componentes |
| 02 | [02-captura-identidad-ocr.md](02-captura-identidad-ocr.md) | SDK PersonalCode + tesseract OCR + cardIO + OpenCV; flujo de captura |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | **Retrofit 1.x (EOL)**, catálogo de API, flujo `uploadZip` del paquete ciudadano |
| 04 | [04-cripto-almacenamiento.md](04-cripto-almacenamiento.md) | **Endpoints cifrados en `config.properties` (AES)**, `getLlave`, Realm DB |
| 05 | [05-datos-sociales-privacidad.md](05-datos-sociales-privacidad.md) | **Capa social Facebook/Twitter**, geolocalización, bitácora a Firestore |
| 06 | [06-sdks-terceros.md](06-sdks-terceros.md) | Firebase/Crashlytics, PersonalCode, cardIO, OpenCV, tesseract, Play Integrity |
| 07 | [07-firma-recursos.md](07-firma-recursos.md) | **Firma auténtica del INE**, estructura |
| 08 | [08-hallazgos-seguridad.md](08-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `decompiled/EndpointCatalog.metodos.txt` | 18 operaciones de la API (decompiladas de `EndpointCatalog`) |
| `decompiled/SessionService.metodos.txt` | Interfaz Retrofit 1.x (`uploadZip`, `generaCodigo`, …) |
| `evidence/config.properties` | Endpoints y llaves de Twitter **cifrados** (AES) embebidos |
| `evidence/dex-strings-notables.txt` | Strings de cripto, TLS, identidad y red extraídas del DEX |
| `AndroidManifest.xml` | Manifiesto decodificado |
| `hashes.sha256` | SHA-256 del APK |

> Lo propietario del INE (APK, `.so`, `decompiled/`, `evidence/`) **no se versiona** (`.gitignore`);
> solo los `.md`, el manifiesto decodificado y los hashes.

## 6 hallazgos principales

1. **Sube la credencial del ciudadano respaldado en un ZIP.** El flujo captura credencial (foto +
   OCR + foto en vivo + firma), arma un paquete y lo transmite con `SessionService.uploadZip(...)`
   / `generaCodigo(File, …)` (doc 03). Es PII electoral de terceros, no solo del usuario.
2. **Endpoints y llaves de Twitter cifrados en el APK, no en claro (seguridad por oscuridad).**
   `assets/config.properties` guarda `BASE_PRE*`, `PRODUCTION`, `DEVELOPMENT`, `CONSUMER_K/SK`
   como texto cifrado **AES** (los comentarios del archivo dicen *"Cifrado ECB"* / *"Cifrado CBC"*);
   la llave de descifrado vive en el propio binario → recuperable (doc 04).
3. **Criptografía débil presente: `AES/ECB/NoPadding`.** Aparece en el DEX junto a
   `SecretKeySpec`/`IvParameterSpec`/PBKDF; ECB no oculta patrones y NoPadding es frágil (doc 04).
4. **Pila de red obsoleta: Square Retrofit 1.x** (`retrofit.RestAdapter`), fin de vida desde ~2016,
   sin mantenimiento de seguridad (doc 03).
5. **Recolecta datos sociales y de ubicación del usuario.** Clases `model.SocialLayer.Fb*`
   (perfil, empleador, ubicación, trabajo de Facebook) + llaves de Twitter + `SocialSessionActivity`;
   permisos `ACCESS_FINE_LOCATION` y `READ_PHONE_STATE`; bitácora de eventos a **Firestore**
   (`FireStoreBitacoraBeta`) (doc 05).
6. **Firma auténtica del INE.** `O=Instituto Nacional Electoral` (verifica) → binario íntegro del
   emisor, más fuerte que un APK re-firmado por redistribuidor (doc 07).

## Alcance y límites

Análisis **original** de una app pública del gobierno mexicano, con fines de investigación de
seguridad. **No redistribuye** el APK ni los binarios del INE (`.gitignore`). Los endpoints y
llaves citados están **cifrados** en el binario; **no se descifraron ni se publican** valores
sensibles. Observaciones **estáticas**: no se ejecutó la app, no se escanearon credenciales reales
ni se llamó a ningún endpoint. **Validación TLS**: se observan indicadores de validación estricta /
pinning (`X509TrustManagerExtensions`, `StrictHostnameVerifier`), pero no se aisló la clase que lo
implementa, así que no se afirma el pinning como hecho (doc 03).

---
