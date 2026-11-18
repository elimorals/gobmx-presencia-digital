# Arquitectura — Xamarin.Forms (.NET/Mono)

## Confirmación de pila
- `.so` nativas: `libmonosgen-2.0.so`, `libmonodroid.so`, `libmono-btls-shared.so`,
  `libmono-native.so`, `libxamarin-app.so`, `libxa-internal-api.so` → **Xamarin.Android clásico**
  (Mono), **no** .NET MAUI (MAUI usa `libassembly-store.so`/.NET 6+; aquí es Mono + `assemblies.blob`).
- `assemblies/assemblies.blob` (7.5 MB) + `assemblies.manifest` → **AssemblyStore XABA v1**.

## Cómo se extrajo
El blob `assemblies.blob` es formato **XABA** (magic `5841 4241`, store v1, 52 ensamblados). Cada
ensamblado está comprimido con **XALZ** (magic `XALZ` + índice + tamaño descomprimido + bloque
LZ4). Se parseó la tabla de descriptores del XABA, se localizaron por el `assemblies.manifest`
los dos ensamblados propios y se descomprimió su LZ4 a PE .NET válidos:
- `DemoCFE.dll` (idx 0001) → 1 274 368 bytes, PE `MZ`, .NET Standard 2.0.
- `DemoCFE.Android.dll` (idx 0000) → 508 416 bytes.

(Los DLL viven en `evidence/` — no se versionan.)

## `DemoCFE.dll` (ensamblado propio)
`inspect_managed_artifact` / `inspect_managed_members`:
- .NET Standard 2.0, MVID `f13ccbc5-…`, **519 TypeDef, 3 429 MethodDef**, 10 AssemblyRef.
- Dependencias: Xamarin.Forms.Core/Platform, Newtonsoft.Json, Rg.Plugins.Popup, System.Net.Http.
- Tipos propios observados: `DemoCFE.Data.RestService` (capa de red), `DemoCFE.Views.{Login,
  MainPageLogin, RecoverPassword, MyServices, Consortiums}`, entidades `CFE.MSC.Entities.*`.

## Nombre interno "DemoCFE"
El proyecto se llama **DemoCFE** / namespace `DemoCFE.*` — un codebase "Demo" compilado y
publicado como la app oficial de proveedores. No es una vulnerabilidad, pero es una señal de
higiene de release (doc 07).

> **Límite:** "función por función" en .NET aquí = metadata/IL/literales (tipos, métodos, call
> edges, cadenas `ldstr`). No hay ILSpy en el entorno, así que **no** se reconstruyó el C#
> textual; se marca como límite.
