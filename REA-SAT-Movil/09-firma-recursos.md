# Firma, recursos y procedencia

## Estructura del bundle XAPK

`mx.gob.sat@arm64-v8a.xapk` (36,195,963 bytes) = 5 split APKs estilo Google Play:

| Split | Bytes | SHA-256 | Contenido |
|---|---|---|---|
| `mx.gob.sat.apk` (base) | 8,304,047 | `ab3fcd6d…e39939` | DEX, manifiesto, recursos, **flutter_assets** (incl. `.env`) |
| `config.arm64_v8a.apk` | 27,771,789 | `f2fb50c6…10d654` | **Todos los `.so`** (libapp, libflutter, tflite, …) |
| `config.en.apk` | 45,465 | `2b396d86…842184` | Recursos locale `en` |
| `config.fr.apk` | 24,985 | `4592d3b9…6cda2e` | Recursos locale `fr` |
| `config.mdpi.apk` | 49,677 | `c854cc3e…cf194d` | Recursos densidad mdpi |

(Hashes completos en `hashes.sha256`.) Nota: la app solo trae locales `en`/`fr` además del default
— el español es el idioma base embebido en el código Dart, no un split.

## Build

- `assets/commit.txt` = **`521065f7`** — commit del repositorio fuente que produjo este build.
- `Dart VM 3.10.0 stable`, build **2025-11-06** (cadena en `libflutter.so`) → artefacto **reciente**
  (finales de 2025). `compileSdkVersion=36` (Android 16).
- `derived.apk.id=3`, `com.android.vending.splits.required=true` → distribución por **Android App
  Bundle** vía Google Play.

## Firma

- El split **base** (tal como viene de APKPure) **no trae ficheros v1** (`.RSA`/`.SF`/`.DSA`) en
  `META-INF/`; solo un `stamp-cert-sha256` (el *source stamp* de Google Play) y los `.version` de
  AndroidX. La firma real es **v2/v3**, que vive en el *APK Signing Block* (no como ficheros) y por
  tanto no la lista `keytool`.
- `apksigner` **no está instalado** en el host, así que la firma v2/v3 **no se verificó**. `keytool
  -printcert -jarfile` no devolvió nada (es v1-only).
- `meta-data com.android.stamp.source = https://play.google.com/store` y
  `stamp.type = STAMP_TYPE_DISTRIBUTION_APK` → el origen declarado es Google Play.

> **Advertencia de procedencia:** el bundle se obtuvo de **APKPure**, no directamente de Google
> Play. El *source stamp* sugiere origen Play, pero sin verificar la firma v2/v3 **no se puede
> afirmar que el certificado sea el oficial del SAT ni descartar re-empaquetado**. Para un veredicto
> de integridad: `apksigner verify --print-certs` sobre cada split y comparar el certificado con el
> de la ficha oficial de Play.

## Recursos

- `res/values/strings.xml`: 72 entradas, **todas de framework** (ver `strings.json`) — confirma
  Flutter (doc 02).
- `flutter_assets/`: fuentes (`geomanist`, `SAT.ttf`, `indicators.ttf`, familia `fluttericon`),
  SVG/PNG de marca (`logo.svg`, `tapiz.svg`, `sat-movil.png`, `resources0..22.svg`), y los 3 `.env`.
- 439 ficheros decodificados por apktool en total (base).

## Qué se versiona y qué no

En este directorio (git): los `.md`, `AndroidManifest.xml`, `strings.json`, `xapk-manifest.json`,
`hashes.sha256`, y `decompiled/`. **No** se versiona nada propietario del SAT (APK, `.so`, fuentes,
SVG, los `.env`) — ver `.gitignore`. Esos insumos viven en el SSD en
`/Volumes/PortableSSD/apps-gobmx-mx/` (`extraidos/mx.gob.sat/` y `work/sat/`).
