# 03 — Código C# recuperado (Assembly-CSharp)

Reconstrucción a partir del **dump estructural propio** de la metadata IL2CPP v31 (4,667
clases con campos y métodos; ver [02](02-unity-il2cpp.md)). Inventario legible de las
clases de app en [`decompiled/app-classes.md`](decompiled/app-classes.md). Los **nombres**
de clases/campos/métodos/parámetros son **ciertos**; los **tipos** de cada miembro y los
**cuerpos** no se recuperan sin el binario (límite en [02](02-unity-il2cpp.md)).

## Managers (orquestadores de la app)

| Clase | Rol inferido por su API |
|---|---|
| `SharedManager` | Singleton global (`get_SharedManager`). Estado compartido entre escenas. |
| `BilleteManager` (`billManager`/`billeteManager`) | Catálogo de billetes. Métodos: `GetActiveBills`, `GetBill`, `CreateDictionaryBill`, `DoSortBills`, `CompareBillDenomination`, `CompareBillFamily`. Carga `bills.json`. |
| `MenuManager` | Navegación del menú principal. |
| `ContextualMenuManager` | Menú contextual (al tocar un billete/elemento). |
| `SceneManager` | Gestión de escenas Unity. |
| `AudioManager_AR` / `Manager_AR` | Audio contextual AR; `Manager_AR.GetTargetBill(key)` mapea target→billete. |
| `NetworkController` | Descarga del catálogo. Campos: `hostUrl` (+7). Métodos: `GetActiveBills(url)` (21 métodos). |
| `DataManager` | `DownloadData()` — orquesta la actualización de datos. |
| `Loading` | `downloadedDataCount` — progreso de descarga. |
| `OpenUrl` | `Open(url)` — abre enlaces externos (Banxico, redes, tiendas). |

> La URL base del catálogo (`NetworkController.hostUrl`) **no** está embebida como literal
> ni en el bundle `data.unity3d` (barrido de 13,722 objetos); viaja vacía o se compone en
> runtime. Ver [05](05-red-endpoints.md).

## Modelo de datos

| Tipo | Rol |
|---|---|
| `Bill` | Entidad billete (denominación, familia, caras). Mapea `bills.json` (ver [06](06-datos-bills-json.md)). |
| `BilleteInfo` | Información/ficha del billete. |
| `AccessibleBill` | Variante para accesibilidad (texto leído por voz; ver [11](11-accesibilidad-tts.md)). |
| `Denomination` / `Family` | Enumeraciones de denominación y familia (F / F1 / G). |

## Flujo de realidad aumentada

`EasyARBehaviour` arranca la sesión EasyAR; `ImageTargetController` /
`SurfaceTargetController` gestionan los objetivos. El ciclo de reconocimiento emite
`ARTargetFound` / `LateARTargetFound` / `ARTargetLost`; los overlays se dibujan con
`ARGifAnimation`, `ARMaterial`, `ARShader`. Botones en escena: `ButtonARBehavior`,
`ButtonARBMBehavior`. Detalle en [04](04-easyar-ar.md).

## Animaciones de ecosistema (familia G)

Cada billete de la familia G anima su motivo natural. Clases/campos observados:

| Elemento | Billete asociado (motivo oficial) | Símbolos |
|---|---|---|
| Ajolote | $50 G — Xochimilco | `audioAjolote`, `actualPosParticulasAjolotes` |
| Manglar / Garza | $20 G — Sian Ka'an (humedales) | `ManglarBehaviour`, `GarzaBehaviour` |
| Jaguar + Mono | $1000 G — selva de Calakmul | `JaguarSceneScript`, `MonkeyScript` (`monkeyAudio`, `OnMonkeyHidden`, `OnMonkeyScared`), `JaguarRest` |
| Mariposa | fauna | `ButterflyManager`, `TIME_BUTTERFLIES_STARTS_MOVING_UP` |

Cada escena AR tiene su script dedicado con audio y callbacks de animación (p. ej.
`MonkeyScript.MonkeyHiddenCallback()`, `MonkeyScaredCallback()`, `MonkeySoundCallback()`).

> La correspondencia elemento↔billete se apoya en el motivo oficial de cada billete de la
> familia G y en los nombres de clase/escena; la asignación exacta por denominación dentro
> del código no se verificó sin decompilar los cuerpos.

## Accesibilidad

`AccesibilityBehaviour`, `UAP_AccessibilityManager` (plugin UAP) y `AccessibleBill`
componen el modo accesible, que usa Google Cloud TTS para leer la información. Ver
[11](11-accesibilidad-tts.md).

## Animación general

**iTween** (confirmado por el literal `http://itween.pixelplacement.com`) más una familia
de ayudantes `Apply*Targets` (`ApplyMoveToTargets`, `ApplyColorToTargets`,
`ApplyShakePositionTargets`, …) para animar propiedades de objetos en escena.

## Qué queda fuera de este nivel

Sin el cruce `libil2cpp.so` ↔ metadata (Il2CppDumper), **no** se recuperan: cuerpos de
método, orden de llamadas real, valores de campos serializados (viven en `data.unity3d`),
ni la clave EasyAR / clave Google TTS. Es el paso siguiente natural del análisis.
