# SAT ID: identidad, biometría y ML on-device

SAT ID es el subsistema de verificación de identidad. Es lo que justifica los permisos más
sensibles de la app y la librería nativa más pesada después del motor.

## Señales que lo sostienen

| Señal | Evidencia |
|---|---|
| Permisos sensoriales | `CAMERA` (required), `RECORD_AUDIO`, `AUDIO_CAPTURE`, **`VIDEO_CAPTURE`**, `ACCESS_COARSE_LOCATION` (doc 01) |
| Cámara | `camera_android_camerax`, `camera_platform_interface`, `libimage_processing_util_jni.so`, `libsurface_util_jni.so` |
| **ML on-device** | **`libtensorflowlite_c.so` (4.7 MB)** — TensorFlow Lite nativo |
| Puente nativo | `libdartjni.so` + `package:jni` (Dart llama al intérprete TFLite / APIs Android) |
| Backend | `SAT_ID_BASE=https://chiikul.sat.gob.mx/api/v1`, captcha `captcha.sat.gob.mx` (doc 04) |
| Foto / recorte | `image_picker`, `photo_view`, `qr_code_scanner` |

## Qué está evidenciado y qué no (corregido en el 2º pase, doc 10)

> **Corrección importante.** En el primer pase inferí "liveness facial on-device". El segundo pase
> **no sostiene esa afirmación** y se retira. Lo que la evidencia realmente dice:

| Afirmación | Evidencia | Veredicto |
|---|---|---|
| La app captura cámara/micrófono/video | permisos + `camera`/`camerax` | **Confirmado** |
| SAT ID verifica identidad contra backend | `chiikul.sat.gob.mx/api/v1`, captcha, `identification_service.dart` | **Confirmado** |
| `libtensorflowlite_c.so` es TFLite real | símbolos `kTfLite*`, `tensorflow/lite/kernels/*` | **Confirmado** |
| Existe un modelo TFLite en el bundle | buscado en `flutter_assets/`, `libapp.so`, `libs/` | **No** — no hay ningún `.tflite` |
| Hay binding Dart de TFLite/ML Kit | `tflite_flutter`/`google_mlkit`/`mlkit` en NOTICES y `libapp.so` | **No aparece** |
| Se hace *liveness* facial | cadenas `liveness`/`face`/`blazeface`/`mediapipe` | **No halladas** |

**Conclusión honesta:** la *capacidad* de inferencia TFLite está presente a nivel nativo, pero su
*uso* no está evidenciado en el snapshot Dart. Dos hipótesis, ninguna confirmada en estático: (a) el
modelo se **descarga en runtime** (del backend o del Azure Blob) y se invoca vía `com.sat_movil/utils`
+ `jni`; (b) `libtensorflowlite_c.so` entra como **dependencia transitiva** de la cadena de cámara y
no se usa para biometría. Resolverlo requiere dinámica (Frida sobre `libapp.so`, observar si descarga
un `.tflite` y si llama al intérprete).

## Privacidad

- La captura de **video + audio** es más intrusiva que la de CFE (que solo pide cámara para QR).
  Es proporcional al propósito (probar identidad para trámites fiscales), pero conviene que la app
  sea explícita sobre qué se procesa en local y qué se sube a `chiikul.sat.gob.mx`.
- Ubicación **gruesa** (no fina): suficiente para contexto/antifraude de citas, menos invasiva.
- Biometría de **desbloqueo** (local_auth, doc 03) es distinta de la biometría de **identidad**
  (SAT ID): la primera es huella/Face ID del sistema para re-abrir; la segunda es el video-liveness.

## Límite

Capacidades presentes, flujo no trazado. Confirmar el pipeline de liveness (qué modelo, qué se
envía, si el video se sube) requiere dinámica sobre `libapp.so` + captura de red, fuera de alcance.
