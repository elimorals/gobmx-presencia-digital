# Arquitectura — .NET MAUI AOT + PairIP, el doble muro

## Confirmación de pila

- **.NET MAUI** (Multi-platform App UI). En `lib/arm64-v8a/` hay **173 ensamblados .NET**
  compilados a **AOT**: `libaot-<Assembly>.dll.so` (código máquina nativo ARM64 por ensamblado).
  Ensamblado **propio**: `libaot-Cepo.CiudadanoMAUI.dll.so` (5.4 MB) — el proveedor es **Cepo**.
- **3 DEX grandes** (`classes.dex` 9.5 MB, etc.) = el runtime + el **wrapper PairIP**
  (`com.pairip.*`, 48 referencias en el DEX) + CHECK_LICENSE.
- Sin Flutter (`libflutter`), sin Unity (`il2cpp`), sin híbrida (`assets/www`).

Lista completa en `evidence/dotnet-assemblies.txt`. SDKs notables:
- `Microsoft.AspNetCore.SignalR.Client` (+ Http.Connections.Client) → **tiempo real** (doc 03).
- `OneSignalSDK.DotNet` (+ Android.Location.Binding) → **push + ubicación a tercero** (doc 04).
- `CommunityToolkit.Maui` / `CommunityToolkit.Mvvm` → UI/MVVM.
- `MPowerKit.Lottie`, `InputKit.Maui` → animaciones / inputs.

## Por qué es un muro (doble capa)

1. **AOT:** los ensamblados están compilados a **código nativo** (`.dll.so` = ELF). Se verificó
   que **no contienen el PE/IL** recuperable: `0` headers `PE\0\0`, `0` firmas de metadata CLI
   `BSJB`, sin stub DOS. Las tools managed de REA lo rechazan (*"Managed static triage requires
   a PE target; observed elf"*). → **no hay IL que decompilar**: ni strings de URL, ni nombres
   de método, ni la estructura de las clases del modelo.
2. **PairIP:** el DEX está envuelto por el runtime de PairIP (anti-tamper + integridad +
   licencia), lo que bloquea también el análisis del lado Java.

Consecuencia: la **lógica de negocio, el backend, el hub y el payload del pánico** no son
observables estáticamente. Es el mismo tipo de muro que Banco del Bienestar (Veritran) o las
Flutter, aquí por **AOT + PairIP**.

## Qué sí es observable

- El **grafo de dependencias** (los 173 ensamblados) → la arquitectura: MAUI + SignalR + OneSignal.
- El **manifiesto** (permisos, cleartext, PairIP) → doc 01.
- Los **endpoints de SDKs** en claro (OneSignal; doc 04) — pero **no** los propios de Cepo/Puebla.

## Vía de profundización (fuera de alcance)

Análisis **dinámico**: instrumentar el runtime .NET en ejecución (Frida/hooks sobre MonoVM o el
AOT) tras **evadir PairIP** — requiere ejecución, probablemente root. No se hizo (estático, sin
root). Documentado como camino, sin inventar el contenido.
