# Ingeniería inversa de BanjeCel 7.10.3 — índice

Análisis **estático** del APK `mx.com.banjercito.model.common.VTUserApplicationBJCTMB` con REA
(JADX headless + inspección de archivo). La app **nunca se ejecutó**. Es la app oficial de
banca móvil de **Banjército** (Banco Nacional del Ejército, Fuerza Aérea y Armada), **#11 por
instalaciones** del inventario gob.mx (≈1 M en Google Play).

> **Low-code Veritran — no decompilable, como Banco del Bienestar.** La lógica bancaria no vive
> en el DEX: se **interpreta en tiempo de ejecución** por un motor **Duktape** (`libduktape.so`)
> sobre la plataforma **Veritran** (`libsmma.so`). El DEX es el runtime; la app en sí es un
> bundle interpretado. Protegida con **RASP** (`libtool-checker.so`), **SQLCipher**
> (`libsqlcipher.so`) y **sin endpoints en claro** en el DEX. A diferencia de Banco del
> Bienestar, este binario sí trae su **firma auténtica de Banjército**. El hallazgo
> documentable pese al muro: un **SDK biométrico on-device** (reconocimiento facial con
> anti-spoofing + verificación de voz).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Veritran, biometría, muro |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos amplios (cámara, micrófono, huella, NFC, ubicación) |
| 02 | [02-arquitectura-veritran.md](02-arquitectura-veritran.md) | Duktape + SMMA, por qué no es decompilable |
| 03 | [03-biometria-smma.md](03-biometria-smma.md) | Modelos faciales (liveness/anti-spoof) y de voz on-device |
| 04 | [04-proteccion-rasp.md](04-proteccion-rasp.md) | `tool-checker`, SQLCipher, Conscrypt |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma **auténtica de Banjército**, procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos y límites del muro |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK (versionado) |
| `evidence/native-libs.txt` | 9 `.so` (Veritran/Duktape/RASP/SQLCipher) (no versionado) |
| `evidence/smma-biometric-assets.txt` | 20 modelos biométricos faciales/voz (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

> El APK y los artefactos pesados **no se versionan** (`.gitignore`).

## 5 hallazgos principales

1. **Arquitectura Veritran interpretada = muro (no es un hallazgo de debilidad, es el límite).**
   La lógica bancaria se interpreta con **Duktape** (`libduktape.so`) sobre Veritran
   (`libsmma.so`); el DEX no contiene la lógica de negocio ni las rutas de API. **Sin endpoints
   en claro** en el DEX. No es decompilable estáticamente sin instrumentar el intérprete
   (doc 02).
2. **Biometría on-device: facial con anti-spoofing + voz.** `assets/smma/model/face-pnn/` trae
   un pipeline facial completo — `liveness_ensemble`, `impostor`, `spoof_detect_nn`,
   `motion_liveness_nn`, `glasses_detector`, `pixel_embedding`, `face_aligner` — y
   `assets/smma/model/voice-tssv-*` un verificador de locutor (voz). Autenticación/onboarding
   biométrico que corre en el dispositivo (doc 03).
3. **RASP fuerte.** `libtool-checker.so` (detección de herramientas: root/Frida/emulador/debug)
   más el endurecimiento típico de Veritran. El análisis dinámico requeriría evadir esto
   (doc 04).
4. **Cifrado local con SQLCipher.** `libsqlcipher.so` → base de datos local cifrada; TLS vía
   `libconscrypt_jni.so` (Conscrypt). La gestión de llaves vive en la capa Veritran, no
   observable (doc 04).
5. **Firma auténtica de Banjército (positivo).** `CN=Banjercito, O=Banco Nacional del Ejercito
   Fuerza Aerea y Armada` — la llave real del emisor verifica. A diferencia de Banco del
   Bienestar (cuyo APK del acervo era Aptoide mal nombrado) y de las re-firmadas por APKPure,
   este binario **es el artefacto auténtico** (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app. El desempacado
del bundle Veritran, el volcado de la lógica interpretada por Duktape y el análisis dinámico
(que exigiría evadir el RASP y, en su caso, root) **quedan fuera de alcance**. Lo documentable
sin ejecutar es la arquitectura, el SDK biométrico, la pila de protección y la firma —
registrados aquí con evidencia.
