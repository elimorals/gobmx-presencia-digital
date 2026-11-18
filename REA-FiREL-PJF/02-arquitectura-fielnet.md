# Arquitectura — white-label FielNet / IQSEC

## La señal

- La búsqueda de clases propias bajo `gob.mx.android.firel` devuelve **solo `R`**: el código no
  vive bajo el package del store.
- El **launcher** es `com.fielnet.notificaciones_colegio_notarios_mvvm.ui.login.LoginActivity`.

→ El package real es **`com.fielnet.notificaciones_colegio_notarios_mvvm`**: la app de **firma
electrónica del Poder Judicial** está construida sobre el **codebase de una app de "Colegio de
Notarios"** del proveedor **FielNet** (marca de **IQSEC**, integrador mexicano de identidad/PKI).
Es un **white-label**: el mismo producto de enrolamiento biométrico re-etiquetado para el CJF.

**Implicación (cadena de suministro):** la firma electrónica del Poder Judicial corre sobre un
producto genérico de un tercero; el nombre del módulo ("notificaciones_colegio_notarios") queda
embebido en el binario de producción.

## Pila

- **Nativa Android, Kotlin, MVVM + DataBinding** (clases `…databinding.Activity*Binding`).
- **Red:** Volley (`com.android.volley`, 153 clases) + **SOAP** (`org.simpleframework.xml`, 419
  clases; envelopes `RequestEnvelope`/`ResponseEnvelope`) + Jackson/Gson/`net.pwall.json` (JSON
  schema). Es un cliente SOAP+REST mixto.
- **Biometría:** SDK **Identy.io** — `com.identy.*` (huella: `CaptureFingersActivity`,
  `EnrollThumbActivity`, `FingerAS`, `DeduplicationIdentyResponse`) + `com.identy.face` (410
  clases) + ONNX runtime (`libonnxruntime.so`) con modelos en `assets/identy_data/*.ort` + libs
  nativas `libfinger-native-lib.so`, `libface-native-lib.so`, `libsupport-native-lib.so` (más
  `libb61a.so`/`libc656.so`, nombres ofuscados del SDK).
- Otros SDKs: ZXing (QR), Karumi Dexter (permisos), OpenCSV.
- **JUnit en el release** (`org.junit.*`) — framework de test empaquetado en producción (bloat;
  no es ejecutable por el usuario, pero no debería ir en el bundle).

## Límite

El código propio `com.fielnet…` es extenso (131+ clases en la capa `data`) y está parcialmente
minificado (R8). Se mapeó el **flujo completo** por nombres de clase/modelo (doc 03) y se
confirmaron SDKs y proveedores; no se decompiló método a método toda la UI (no aporta sobre lo
ya hallado). El punto crítico pendiente —qué cliente HTTP usan las llamadas de biometría— se
trata en doc 04.
