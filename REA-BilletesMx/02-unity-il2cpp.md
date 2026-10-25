# 02 — Motor Unity e IL2CPP

BilletesMx es una app **Unity** compilada con **IL2CPP**: el código C# se transpila a
C++ y se compila a ARM64 nativo dentro de `libil2cpp.so`. No quedan ensamblados .NET
ejecutables; toda la información de tipos vive en `global-metadata.dat`.

## Artefactos Unity (en `assets/bin/Data/`)

| Archivo | Tamaño | Qué es |
|---|---|---|
| `Managed/Metadata/global-metadata.dat` | 3.8 MB | Metadata IL2CPP: nombres de tipos, métodos, campos y literales. |
| `data.unity3d` | 61 MB | Assetbundle principal (escenas, prefabs, materiales, configs serializadas). |
| `resources.resource` | 5.2 MB | Recursos (texturas/audio). |
| `sharedassets0.resource` | 45 KB | Assets compartidos. |
| `boot.config` | — | `gfx-threading-mode=4`, `build-guid=592aa4a7885a4d3f9bdc512877995aa1`. |
| `ScriptingAssemblies.json` | — | Lista de DLLs lógicas (ver abajo). |
| `Managed/Resources/mscorlib.dll-resources.dat` | — | Recursos de mscorlib. |

Librerías nativas (en el split `config.arm64_v8a`, ver [09](09-nativo-so.md)):
`libil2cpp.so` (30 MB), `libunity.so` (20 MB), `libEasyAR.so` (8 MB), `libmain.so` (6.7 KB).

## Ensamblados lógicos

De `ScriptingAssemblies.json`, los **no** `UnityEngine.*`:

- **`Assembly-CSharp.dll`** y **`Assembly-CSharp-firstpass.dll`** → código propio de Banxico + SDKs en `firstpass` (incluye EasyAR).
- `Unity.TextMeshPro.dll`, `Unity.Timeline.dll`, `Unity.AI.Navigation.dll`, `Unity.Multiplayer.Center.Common.dll`.

## Metadata IL2CPP — versión y cabecera (verificada)

El header de `global-metadata.dat` se validó leyendo sus primeros campos:

```
sanity   = 0xFAB11BAF   (correcto)
version  = 31
stringLiteral      off=8    (tabla índice: length+dataIndex, 8 bytes/entrada)
stringLiteralData  off=46720  size=146424   (bytes de los literales C#)
string (identifiers) off=193144  size=647288  (blob de nombres NUL-terminados)
total    = 3,932,556 bytes
```

**Metadata v31** corresponde a **Unity 6**. La versión exacta se confirmó en el header
UnityFS de `data.unity3d`: **`6000.2.7f2`** (Unity 6000.2.7f2). Con
`unity.strip-engine-code=true` en el manifiesto hay **code stripping** activo: los tipos no
usados se eliminaron, así que el inventario refleja lo realmente enlazado.

## Reconstrucción realizada

La reconstrucción C# se hizo por **dos vías independientes que coinciden**:

1. **Il2CppDumper** (Perfare) cruzando `libil2cpp.so` + `global-metadata.dat` →
   **`dump.cs` tipado completo** (clases, campos y métodos con **tipos resueltos** y RVA).
   Localizó `CodeRegistration` en `0x1b75908` y `MetadataRegistration` en `0x1c1c8a8`
   (Il2Cpp v31). Salida en `decompiled/il2cppdumper-out/`.
2. **Parser propio** de `global-metadata.dat` v31 (código original), **autovalidado**
   contra la tabla de strings (validez de nombres tipo/método = **1.000**).

| Artefacto | Contenido |
|---|---|
| `decompiled/il2cppdumper-out/dump.cs` (10.6 MB) | **Dump C# tipado** de las 4,667 clases (firmas completas con tipos + RVA). |
| `decompiled/il2cppdumper-out/il2cpp.h` (34 MB) | Cabeceras C de todas las estructuras. |
| `decompiled/il2cppdumper-out/script.json` (31 MB) | Script para nombrar funciones nativas por RVA en Ghidra/IDA. |
| `decompiled/il2cppdumper-out/stringliteral.json` | Todos los literales de cadena. |
| `decompiled/il2cppdumper-out/DummyDll/` | Ensamblados .NET reconstruidos. |
| [`decompiled/types-structured.md`](decompiled/types-structured.md) / `.json` | Dump estructural del parser propio (4,667 clases). |
| [`decompiled/app-classes.md`](decompiled/app-classes.md) | Clases de app (global + 94 `easyar` + 8 `UAP_*`). |

Validación cruzada: ambos coinciden en 4,667 clases; la clase `Bill` coincide **exacta**
con el esquema de `bills.json` (ver [06](06-datos-bills-json.md)).

> **Único límite restante:** los **cuerpos** decompilados de cada método (lógica línea a
> línea) requieren cargar `libil2cpp.so` en Ghidra/IDA aplicando `script.json`. Con eso se
> cierra el 100%. Tipos y firmas **ya** están recuperados. Il2CppDumper reportó el aviso
> "This file may be protected" pero completó el volcado igualmente. Comandos en
> [09](09-nativo-so.md).

> `dump.cs` se conserva como artefacto; por tamaño puede excluirse del control de
> versiones si se desea (ver `.gitignore`).
