# 04 — Realidad aumentada (EasyAR Sense)

La RA la provee **EasyAR Sense** (librería china de Visionstar / `cn.easyar`), no
ARCore/ARKit ni Vuforia. Evidencia: `libEasyAR.so` (8 MB), la cadena `EasyARSense` y
`cn.easyar.Engine` en el binario, **289 identificadores** del namespace `easyar` en la
metadata, y los logs `[EasyAR] …`.

## Componentes EasyAR presentes (recuperados de metadata)

| Clase | Rol |
|---|---|
| `ImageTracker` / `ImageTrackerBehaviour` / `ImageTrackerMode` / `ImageTrackerResult` | **Seguimiento local de imágenes** — el motor que reconoce el billete. |
| `ImageTarget` / `ImageTargetController` / `ImageTargetParameters` / `CenterImageTarget` | Definición de cada objetivo (el billete) y su controlador en escena. |
| `LoadImageTarget` (`<LoadImageTarget>d__25`) | Carga de los targets de imagen (corrutina). |
| `CameraDevice` / `CameraDeviceSelector` / `FrameSource` / `InputFrameSource` / `FeedbackFrameSource` | Captura de cámara y pipeline de frames. |
| `FrameFilter` / `FrameFilterResult` | Filtros del pipeline de visión. |
| `CloudImageRecognizer` / `CloudRecognizer` / `CloudRecognizeBehaviour` | **Reconocimiento en nube (presente, no configurado — ver abajo).** |
| `ObjectTracker` / `ObjectTrackerResult` | Seguimiento de objetos 3D (presente; sin uso observado). |
| `ARSession` / `ARSessionUpdateEventArgs` | Sesión AR. |

Capa de aplicación encima de EasyAR: `EasyARBehaviour`, `SurfaceTargetController`,
`ARTargetFound` / `ARTargetLost` / `LateARTargetFound` (ciclo de reconocimiento),
`ARGifAnimation` / `ARMaterial` / `ARShader` (overlays), `AudioManager_AR`,
`ButtonARBehavior` / `ButtonARBMBehavior`.

## Reconocimiento **local**, no en nube

El código incluye las dos rutas de EasyAR (local `ImageTracker` y `CloudImageRecognizer`),
pero la de nube **no está configurada**: las credenciales de Cloud Recognition son los
**placeholders de muestra** del SDK, presentes como literales:

```
--YOUR CLOUD KEY HERE--
--YOUR CLOUD SECRET HERE--
```

Por tanto el reconocimiento se hace **en el dispositivo** contra los targets embebidos
(las imágenes de referencia `assets/<denom>G_Anverso/Reverso.jpg`), coherente con
`bills.json` (`isLocal=true`, `hasRA=true` en los 6 billetes de la familia G; ver
[06](06-datos-bills-json.md)). La RA **funciona sin conexión**.

## Licencia EasyAR (hallada)

EasyAR Sense requiere una **clave de licencia** por paquete (validada contra
`org.banxico.dge.movil`). Está en `easyar.EasyARSettings.EasyARKey` y la ruta de
inicialización es `license_init` / `license_error` / `EasyAR initialization failed` /
`only support easyar camera`.

Al extraer `data.unity3d` con UnityPy **se recuperó el token de licencia**: una cadena
Base64 de **1,016 caracteres**, `sha256 958c37ca3825b9db1ab44e4ec1c19ac72993a299565b6a8aee5d7e0ad7c1a6b2`.
Se guarda **completa pero fuera de control de versiones** en `evidence/easyar-key.txt` (en
`.gitignore`); **no se reproduce aquí** (ni siquiera el prefijo).

Matiz de riesgo: la clave de licencia de EasyAR **está pensada para viajar en el cliente**
y queda **ligada al nombre de paquete** (`org.banxico.dge.movil`); no es un secreto de
servidor. Su exposición no permite por sí sola abusar de la cuenta de Banxico salvo
reempaquetando con el mismo package name. Es, por tanto, un hallazgo **informativo**, no
una vulnerabilidad de alto impacto.

Las credenciales de **Cloud** (`easyar.CloudRecognizeBehaviour.CloudServer/CloudKey/
CloudSecret` y `easyar.CloudRecognizer.create(server, appKey, appSecret, …)`) **sí** son
los placeholders de muestra (ver arriba) → Cloud no se usa.

## Cámara

Logs del pipeline de cámara confirmados en metadata:
`[EasyAR] use ezar camera device`, `open camera back/front result`, `open camera failed`,
`camParams is null`, `ezar camera device can not work`. EasyAR abre la cámara por su
propio dispositivo (`ezar camera`) en lugar de la API de Unity; de ahí que el permiso
`CAMERA` sea imprescindible y que la actividad fije `screenOrientation="landscape"`.

## Versión del SDK

`libEasyAR.so` reporta la cadena `EasyARSense`. Las cadenas de versión legibles en el
binario son ambiguas (varias: `0.8.1`, `1.2.7`, `4.1.0`, `8.0.2`, probablemente de
dependencias internas), por lo que **no se fija la versión exacta** aquí; el conjunto de
clases (`ImageTracker`, `CloudImageRecognizer`, `FeedbackFrameSource`, `FrameFilter`)
corresponde a la **familia EasyAR Sense 4.x**. Confirmación exacta requeriría leer el
header de versión del `.so` (ver [09](09-nativo-so.md)).
