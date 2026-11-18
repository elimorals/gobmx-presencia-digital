# Biometría on-device (SMMA)

El hallazgo más concreto pese al muro Veritran: bajo `assets/smma/model/` hay un **SDK
biométrico completo** que corre en el dispositivo. 20 archivos de modelo
(`evidence/smma-biometric-assets.txt`).

## Biometría facial con anti-spoofing (`assets/smma/model/face-pnn/`)

| Modelo | Función |
|---|---|
| `pixel_embedding-4.0.0.jcq` (5.4 MB) | embedding del rostro (vector de identidad) |
| `face_aligner-3.0.0.jcq` (3.5 MB) | alineación/normalización del rostro |
| `primary_cascade-2.0.0.xml`, `secondary_cascade-2.0.0.xml` | detección de rostro (cascadas) |
| `liveness_ensemble-1.2.3.jcq` | **liveness** (vivacidad: rostro real vs foto/video) |
| `motion_liveness_nn-0.0.2.jcq` | liveness por movimiento |
| `spoof_detect_nn-1.2.0.jcq` | **detección de spoofing** (anti-presentación) |
| `impostor-1.3.0.jcq` | detección de impostor |
| `glasses_detector-1.0.1.jcq` | detección de lentes |
| `logistic_regression`, `lr_pnn_ensemble…` | clasificadores del ensemble |
| `conversions.yaml`, `distributions.yaml`, `model.yaml` | configuración del pipeline |

Es un pipeline de **reconocimiento facial con control de vivacidad y anti-spoofing** — robusto,
pensado para onboarding/autenticación bancaria (abrir cuenta, autorizar operaciones).

## Verificación de voz (`assets/smma/model/voice-tssv-udp_enUS/`)

| Modelo | Función |
|---|---|
| `text-dependent-1.18.0.ubm` (160 KB) | UBM (Universal Background Model) de verificación de locutor |
| `conversions.yaml`, `distributions.yaml`, `model.yaml` | configuración |

`tssv` = *text-dependent speaker verification*: la app puede verificar la identidad por la voz
(una frase fija). Explica el permiso `RECORD_AUDIO`. (El modelo es `enUS`; el despliegue real
en español se resolvería en runtime.)

## Lectura

Banjército implementa **autenticación biométrica multimodal on-device** (cara + voz) con
anti-spoofing — de lo más sofisticado del acervo gob.mx. Los modelos están presentes y
nombrados; **cómo se orquestan, qué umbrales usan y si el match ocurre local o contra el
servidor** se deciden en la capa Veritran interpretada (doc 02) → no observable estáticamente,
marcado como límite.

## Nota de alcance

Los `.jcq`/`.ubm` son **modelos** (pesos), no datos de usuarios; no contienen biometría de
personas. No se extraen ni se redistribuyen.
