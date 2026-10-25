# 09 — Librerías nativas (.so)

Todo el peso ejecutable está en 4 librerías ARM64 del split `config.arm64_v8a`. Las 4 son
**ELF 64-bit aarch64, stripped**. Copias en [`so-extract/`](so-extract/).

| Librería | Tamaño | BuildID | Rol |
|---|---|---|---|
| `libil2cpp.so` | 30 MB | `56c4ce0cdec7af33a597afe65fdf0f7134c579ba` | **Todo el código C# de la app + SDKs**, compilado a nativo (IL2CPP). |
| `libunity.so` | 20 MB | `f460a974b9988865` (xxHash) | Motor Unity. |
| `libEasyAR.so` | 8 MB | `1edab17a17fdab86cea18a92d9cc97c5a336d6a7` | SDK EasyAR Sense (visión/RA). |
| `libmain.so` | 6.7 KB | `53721771b2ddf6bf4327728b6ec03619e53748ae` | Shim de arranque de Unity. |

## Cadena de carga

`libmain.so` es el punto de entrada JNI. Su única exportación es **`JNI_OnLoad`** (REA
`open_binary` confirma ELF arm64; símbolo verificado con `nm -D`). Importa `dlopen`/
`dlsym`/`dlclose` → carga dinámicamente `libunity.so` y `libil2cpp.so` al iniciar
`UnityPlayerActivity`. EasyAR se enlaza desde el código IL2CPP (`cn.easyar.Engine` por
JNI) contra `libEasyAR.so`.

## `libil2cpp.so`

Exporta **1,087 símbolos** del **API C estándar de IL2CPP** (`il2cpp_init`,
`il2cpp_runtime_invoke`, `il2cpp_runtime_class_init`, `il2cpp_runtime_object_init`, …).
El código **propio** de Banxico (los métodos de `Assembly-CSharp`) **no** se exporta: son
funciones internas, alcanzables sólo cruzando las direcciones con `global-metadata.dat`.
Por eso la reconstrucción C# de este análisis se apoya en la metadata (ver
[02](02-unity-il2cpp.md) y [03](03-codigo-csharp.md)).

## Alcance del análisis nativo

REA **abrió** los ELF (identidad, arquitectura, símbolos), pero sus proveedores de
**decompilación nativa** (Ghidra / Hopper / IDA / Kuna) **no están configurados en este
entorno** (`GHIDRA_INSTALL_DIR` sin definir, Hopper/IDA ausentes). Por tanto **no** se
produjo pseudocódigo de las funciones nativas. Lo documentado aquí se basa en hechos ELF
verificables (`file`, `nm -D`, BuildID, cadenas). La decompilación profunda de
`libil2cpp.so`/`libEasyAR.so` queda como paso siguiente al configurar un proveedor Ghidra
12.1.x.

## Cómo completar el análisis nativo (comandos)

**Il2CppDumper ya se ejecutó** (dump tipado en `decompiled/il2cppdumper-out/`; ver
[02](02-unity-il2cpp.md)). Localizó `CodeRegistration=0x1b75908`,
`MetadataRegistration=0x1c1c8a8` (Il2Cpp v31) y generó `dump.cs`, `il2cpp.h`, `script.json`,
`stringliteral.json` y `DummyDll/`. Queda sólo la **decompilación de cuerpos nativos** con
Ghidra:

```bash
# Ghidra 12.1.x para cuerpos nativos (libil2cpp.so / libEasyAR.so)
# 1) descargar release Ghidra 12.1.x y: export GHIDRA_INSTALL_DIR=/ruta/ghidra
# 2) importar libil2cpp.so y aplicar el script de Il2CppDumper:
#    decompiled/il2cppdumper-out/script.json  (nombra funciones por RVA)
# 3) o, con REA, open_binary sobre el .so una vez configurado el proveedor Ghidra.
```

Con eso se obtienen los **cuerpos** línea a línea — lo único pendiente; tipos y firmas ya
están en `dump.cs`.

## Observaciones

- **Sólo arm64-v8a**: este XAPK no trae armeabi-v7a ni x86. Correcto para 2022+ pero
  excluye emuladores x86 y dispositivos de 32 bits.
- `extractNativeLibs="true"`: las `.so` se extraen al instalar (mayor huella en disco; no
  es un problema de seguridad).
- Librerías **stripped**: sin símbolos de depuración, lo normal en release.
