# 06 — SDKs y dependencias de terceros

Evidencia: nombres de paquete en el DEX + librerías nativas + dominios en claro (ver
[`evidence/dex-strings-notables.txt`](evidence/dex-strings-notables.txt)).

## Identidad / visión por computadora (nativo)

| SDK | Evidencia | Rol |
|---|---|---|
| **PersonalCode** | `com.personalcode.*`, `libPersonalCode.so` | Captura/validación de la credencial (propietario; mismo proveedor que Valida INE-QR) |
| **tesseract / leptonica** | `libtess.so`, `liblept.so`, `*.traineddata` | OCR de la credencial |
| **cardIO** | `libcardioRecognizer*.so`, `libcardioDecider.so` | Reconocimiento de tarjeta/credencial |
| **OpenCV 3** | `libopencv_*.so` | Procesamiento de imagen (bordes, encuadre) |
| **Fresco** (inferido) | `libimagepipeline.so`, `libjpgt.so`, `libpngt.so` | Pipeline/decodificación de imágenes |

## Backend / red

| SDK | Evidencia |
|---|---|
| **Square Retrofit 1.x** | `retrofit.RestAdapter`, `retrofit.mime.*`, `retrofit.converter.GsonConverter` |
| **RxJava 1** | `rx.Observable` |
| **Gson** | converter JSON |

## Google / Firebase

| SDK | Evidencia (dominios/strings) |
|---|---|
| Firebase Analytics | `firebase.google.com/docs/analytics` |
| Firebase Crashlytics | `firebase-settings.crashlytics.com/...` |
| Firebase Remote Config | `firebaseremoteconfig.googleapis.com`, `firebaseremoteconfigrealtime.googleapis.com` |
| **Cloud Firestore** | `mx.ine.apoyociudadano.utils.firestore.*` (bitácora, doc 05) |
| Google Play Integrity | `play/integrity/...` (anti-fraude/attestation) |
| Google Sign-In / OAuth | `accounts.google.com/o/oauth2/revoke`, `googleapis.com/auth/...` |
| reCAPTCHA | `recaptcha.net/recaptcha/api3` (la API tiene `generaCaptcha`, doc 03) |
| AdServices (Privacy Sandbox) | permisos `ACCESS_ADSERVICES_*` + `googleadservices.com` |

## Social

| SDK | Evidencia |
|---|---|
| Facebook SDK | `http://graph.facebook.com/`, `model.SocialLayer.Fb*` (doc 05) |
| Twitter | `CONSUMER_K`/`CONSUMER_SK` en `config.properties` (doc 04/05) |

## Lectura

La pila de **identidad es robusta y especializada** (PersonalCode + OCR + cardIO + OpenCV), acorde a
una app que lee credenciales. La presencia de **Play Integrity + reCAPTCHA** indica controles
anti-fraude (esperable en captación electoral). Lo discutible no son los SDKs en sí, sino la
**combinación de telemetría (Firestore, AdServices, Crashlytics) + datos sociales** en una app que ya
maneja PII electoral sensible (doc 05, 08).
