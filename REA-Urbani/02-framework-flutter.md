# 02 — Framework: Flutter (Dart AOT) y estructura del bundle

## Huella de framework

Urbani es una app **Flutter**. Evidencia (pura observación de archivos, sin ejecutar):

```
assets/flutter_assets/AssetManifest.bin
assets/flutter_assets/FontManifest.json
assets/flutter_assets/NOTICES.Z
assets/flutter_assets/assets/...           (fuentes, audios, google_fonts)
lib/armeabi-v7a/libflutter.so   (7,545,485 bytes)  — engine Flutter
lib/armeabi-v7a/libapp.so       (29,016,941 bytes) — snapshot Dart AOT (la app)
io.flutter.plugins.*  (en el DEX: GeneratedPluginRegistrant + ~15 plugins)
```

En una app Flutter release, `libapp.so` contiene el **snapshot AOT de Dart**: toda la lógica de
negocio compilada a código máquina + el pool de strings (donde normalmente viven **endpoints,
rutas de API y constantes**). `libflutter.so` es el motor genérico (no lleva lógica de la app).

## Estructura del App Bundle

La fuente es un App Bundle con 3 APK por rol (no un APK monolítico):

| APK | Tamaño | Contenido |
|---|---|---|
| `com.miruta.mty.apk` (base) | 132,440,286 | DEX (33,861 clases), recursos, `assets/flutter_assets/`, manifest |
| `config.armeabi_v7a.apk` | 34,105,272 | **librerías nativas** (`libapp.so`, `libflutter.so`, SDKs `.so`) |
| `config.xhdpi.apk` | 98,061 | recursos de densidad xhdpi |

> **Consecuencia:** el `base.apk` **no contiene ninguna `.so`**. El código Dart de negocio
> (`libapp.so`) vive en el split `config.armeabi_v7a`. Para analizar la lógica hay que abrir el
> split, no el base. Ambos se copiaron a `_apk/` y se hashearon (`hashes.sha256`).

Solo existe el split **`armeabi-v7a`** (ARM 32-bit). Un bundle real de Play entregaría
`arm64-v8a` a la mayoría de dispositivos modernos; la fuente solo trae la variante de 32 bits (es
lo que el extractor descargó), así que el análisis nativo se hace sobre `armeabi-v7a`.

## Identificación del build

`identify_flutter_build` sobre `config.armeabi_v7a.apk`:

```
flutter_detected: true
libapp.so:   present, elf_header_valid: true, snapshot_hash: null,
             snapshot_hash_sources: 0   ← una AOT completa reporta 2 secciones de snapshot
libflutter.so: build_id: null, dart_version: null, toolchain_lines: []  ← engine stripped
```

Dos observaciones que **no son normales** en una Flutter release sin proteger:

1. `libapp.so` reporta **0 secciones de snapshot** ("the library is truncated or nonstandard";
   una AOT completa tiene VM + isolate). No se halló el **magic de snapshot Dart** (`f5 f5 dc dc`):
   **0 ocurrencias** en los 29 MB.
2. No hay **ninguna URL ni string de dependencia** legible en `libapp.so`.

La causa es el packer (doc 03): el AOT de Dart está **cifrado**. La versión exacta de Flutter/Dart
**no se pudo determinar** — el `dart_version` del engine no está etiquetado (release stripped) y el
`snapshot_hash` (que mapearía a un SDK de Dart) no existe porque el snapshot está cifrado. **No se
infiere una versión**; se reporta como no determinada.

## Qué sí y qué no se recupera

| Artefacto | ¿Legible estáticamente? |
|---|---|
| Manifest, permisos, componentes | **Sí** (doc 01) |
| Plugins Flutter (`io.flutter.plugins.*`) | **Sí** — nombres de clase intactos en DEX |
| SDKs de terceros (Firebase, Baidu, Veriff, ML Kit…) | **Sí** — clases DEX + `.so` (docs 06–08) |
| Config Firebase/Google, API keys | **Sí** — recursos en claro (doc 07) |
| **Lógica de negocio Dart (endpoints, saldos, cripto de tarjeta)** | **No** — `libapp.so` cifrado (doc 03) |
| Código propio Java/Kotlin | Mínimo; lo poco que hay está en `o.*` ofuscado por R8 |
