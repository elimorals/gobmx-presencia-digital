# Arquitectura: Xamarin / .NET

IMSS Digital es la **única app del lote hecha con Xamarin/.NET**. El DEX (19,312 clases) es
solo el *shim* de bindings; la lógica real es **C# compilado a assemblies .NET** dentro del
APK.

## Evidencia de runtime

- **363 referencias a `Lmono/`** en el DEX → runtime Mono/.NET embebido.
- Nombres de clase `crc64<hex>.<Nombre>` en el manifiesto → generador de bindings de
  **Xamarin.Android**.
- `assemblies/assemblies.blob` (**17 MB**) + `assemblies/assemblies.manifest` (12 KB) en el
  APK base → formato de **assembly store** de Xamarin.
- Plugins Xamarin.Forms en el blob: `SlideOverKit.dll`, `Svg.Platform.dll`,
  `Plugin.FirebaseAnalytics.dll`, `Plugins.Fingerprint.dll`, `Maps.dll` → **Xamarin.Forms
  clásico** (no .NET MAUI; no aparece `Microsoft.Maui`).
- Librerías base .NET fragmentadas: `mscorlib`, `System.*`, `Mono.Android`, `Xamarin.*`.

## El assembly store (`assemblies.blob`)

El blob agrupa **162 assemblies .NET** comprimidos. Cada uno empieza con el magic **`XALZ`**
seguido de un header de 12 bytes (`XALZ` + índice de descriptor + longitud sin comprimir) y el
cuerpo comprimido con **LZ4 block**.

```
[XALZ][u32 descriptor_index][u32 uncompressed_len][ LZ4 block ... ]  × 162
```

### Cómo se extrajo (reproducible)

```python
import struct, re, lz4.block as lb
data = open('assemblies.blob','rb').read()
idxs = [m.start() for m in re.finditer(b'XALZ', data)]
for i, off in enumerate(idxs):
    _, didx, ulen = struct.unpack_from('<4sII', data, off)
    start, end = off+12, (idxs[i+1] if i+1 < len(idxs) else len(data))
    dec = lb.decompress(data[start:end], uncompressed_size=ulen)   # IL + strings C#
```

Tras descomprimir, las cadenas C# (UTF-16) contienen **todos los endpoints, nombres de SDK y
rutas** — por eso este análisis sí recuperó la capa de red, a diferencia de CFE (DexGuard) o
las apps IL2CPP/Flutter donde el código queda en binario nativo.

> Nota: para `lz4.block` se instaló el paquete `pip install lz4`. El blob no se versiona
> (propietario de IMSS); vive en el SSD.

## Componentes .NET identificados (del blob)

| Assembly / plugin | Rol |
|---|---|
| `IMSSDigital.dll` / `IMSSDigital.resources.dll` | App principal (C#) |
| `SlideOverKit.dll` | Menús laterales Xamarin.Forms |
| `Svg.Platform.dll` | Render de SVG |
| `Plugin.FirebaseAnalytics.dll` | Analytics |
| `Plugins.Fingerprint.dll` | Biometría (huella/rostro local) |
| `Maps.dll` | Mapas |
| `SQLitePCL.raw` | Base de datos local SQLite |
| `metadata-extractor-dotnet` | EXIF de imágenes (documentos) |
| `AndHUD`, `userdialogs` (Acr) | HUD / diálogos |
| `LatestVersionPlugin` | Chequeo de versión en tienda |

## Implicación para el análisis

El runtime Xamarin **no ofusca strings** por defecto (a diferencia de DexGuard en CFE). Eso
hizo posible mapear la arquitectura completa de backend y KYC. La contraparte: el código IL
podría decompilarse a C# casi original con herramientas .NET (ILSpy/dnSpy) si se extraen los
assemblies individuales — fuera del alcance de este pase, pero trivial de continuar.
