# 09 — Firma, recursos y estructura del bundle

## Firma del APK

**No verificada por el motor** (`signature_verification: "not_performed"`). El binario
proviene del SSD (`/Volumes/PortableSSD/apps-gobmx-mx/`), no de una descarga directa y
verificada de Google Play. Por tanto:

- No se afirma la identidad del firmante ni la cadena v1/v2/v3.
- PairIP (doc 07) verifica integridad **en runtime**, pero eso es ortogonal a validar la
  firma del artefacto en reposo.
- Para una verificación formal habría que correr `apksigner verify --print-certs` sobre el
  APK base y contrastar la huella con la de la ficha de Google Play.

## Estructura del bundle XAPK

Split APKs estilo Google Play (App Bundle). El `manifest.json` del bundle
(`xapk-manifest.json`) declara base + 19 splits:

| Grupo | Splits |
|---|---|
| Base | `mx.gob.cdmx.adip.apps.apk` (código + recursos + assets) |
| Densidad | `config.hdpi` |
| Nativo | `config.armeabi_v7a` (6 `.so`, doc 07) |
| Idiomas (18) | `en, es, ar, de, fr, hi, in, it, ja, ko, my, pt, ru, th, tr, vi, zh` (+ base) |

- `xapk_version: 2`, `total_size: 57,528,123` bytes.
- `localeConfig="@xml/locales_config"` en el manifiesto respalda los 18 idiomas por split.

## Recursos y assets

- **3,744 recursos** (reportado por el motor).
- Assets destacados en el APK base: `assets/ca.cert.der` (cert pinning, doc 07).
- `res/xml/` relevantes: `file_paths` (FileProvider), `apduservice` (HCE Calypso),
  `locales_config`, `splits0` (PairIP), y 7 `*_widget_info` (widgets, doc 01).

## DEX

| DEX | Tamaño |
|---|---|
| `classes.dex` | 8,959,828 |
| `classes2.dex` | 3,172,948 |

12,742 clases top-level (JADX). Solo 2 DEX pese al tamaño: código denso, sin multidex extremo.

## Hashes (SHA-256)

Ver `hashes.sha256` (20 APK del bundle). Principales:

```
3070e6eb79ab01abee6d54980a4bf9309028e1e0bfcaf8878ed4c57248d0efcf  mx.gob.cdmx.adip.apps.apk (base)
27d20e2e1fef1ca658db6c2bc20bc4c9e0e14fd52b18bc67d2c781d79fd3d541  config.armeabi_v7a.apk (nativo)
7a099afbe0c4dcbcceb6f6df72a417b776fd0443012425ba5d27ceff80184389  config.es.apk
43fa0dcf84189445e184ca1e3ba44956c1f56bab847290a3de7c88f102bc8a9c  config.hdpi.apk
```

## Procedencia y reproducibilidad

- Fuente: SSD `apps-gobmx-mx` (inventario gob.mx del proyecto).
- Herramienta: REA (JADX headless + apktool), análisis 100% estático.
- Artefactos pesados (APK, DEX, `.so`, `resources.arsc`, `ca.cert.der`) **no versionados**
  (`.gitignore`); viven en el SSD. Este directorio versiona solo docs + metadatos
  (`AndroidManifest.xml`, `xapk-manifest.json`, `hashes.sha256`).
