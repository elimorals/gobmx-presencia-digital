# Firma del APK, recursos y procedencia

## ⚠️ Procedencia: APK re-firmado por APKPure (no es el binario original de CFE)

El APK se descargó de **APKPure** con `apkeep` (única fuente sin cuenta de Google). APKPure
**re-empaqueta y re-firma** los APK. La verificación de firma lo confirma:

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
Signer #1 SHA-256: bb8aa0c1bd23ab3e9cca5a8b27812a7eaf5c9788a7dca86c6ad7ec62f5944c68
Source Stamp Signer DN: CN=Android, OU=Android, O=Google Inc., …
Source Stamp SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

- El certificado de firma es el **genérico `CN=Android, O=Google Inc.`**, no uno de CFE ni de
  la CFE. Es el patrón de un APK **re-firmado tras pasar por el pipeline de redistribución**
  (APKPure entrega el bundle extraído del "split APK" de Play y lo re-firma).
- Hay además un **Source Stamp** también de "Google Inc." genérico.

**Consecuencia:** este binario **no es verificable como el artefacto auténtico publicado por
CFE**. Para análisis de arquitectura (lo que hace este repositorio) sigue siendo útil, pero
**ninguna conclusión de seguridad debe asumir que el binario no fue alterado** por el
redistribuidor. La documentación de `apkeep` advierte, además, que APKPure ha llegado a
distribuir apps modificadas con malware.

### Cómo obtener el binario auténtico

Descargar desde **Google Play con una cuenta de Google** (o del canal oficial de CFE) y
comparar el certificado de firma. Solo entonces la firma identifica al emisor real.

## Estructura del bundle XAPK

El paquete de Play es un **split bundle**. El `.xapk` contiene 4 APK:

| APK | Rol | SHA-256 (16 pref.) |
|---|---|---|
| `mx.com.cfe.cfecontigo.apk` | base (código + recursos comunes) — **el analizado** | `d859fb91e74279e2` |
| `config.arm64_v8a.apk` | librerías nativas arm64 (`libb1bd.so`) | `a39c454847625fd2` |
| `config.en.apk` | recursos de idioma inglés | `d194f8c0148d7806` |
| `config.hdpi.apk` | recursos de densidad hdpi | `e85c83d2455cd4f5` |

Hashes completos en `hashes.sha256`. Se instalan juntos con `adb install-multiple *.apk`.

## Recursos y assets

- **1,011 recursos**, 1,051 archivos decodificados por apktool.
- `res/xml`: 3 XML renombrados por apktool →
  `full-backup-content` (vacío), `data-extraction-rules` (`<cloud-backup/>` vacío),
  `filepaths` (FileProvider con `files-path "/"`, ver doc 01). **No hay
  `network_security_config.xml`** propio.
- `assets/` relevante:
  - `avisoprivacidad.md` (2,859 B) — aviso de privacidad embebido.
  - `crmkey_pub.pem` (816 B) — llave pública (doc 03).
  - `markdown.css` — estilos para render de aviso/términos.
  - `grs_sdk_*.json`, `grs_sp.bks`, `hmsincas.bks`, `hmsrootcas.bks` — SDK Huawei (doc 03/07).
  - `services/com.lexisnexisrisk.threatmetrix.rl.*` — registro de ThreatMetrix (doc 05).
  - `dexopt/baseline.prof(m)` — perfil de arranque (ART baseline profiles).

## Inventario de código

- **10,339 clases** top-level (JADX), 2 DEX (classes.dex + classes2.dex).
- ~130 clases propias `mx.com.cfe.cfecontigo.*` (capas `ui.*` y `data.*`), el resto son SDKs
  de terceros y el paquete ofuscado `o.*`.
- `App.onCreate` (clase `Application`) es tan grande/ofuscado que JADX **cayó a smali**
  (`decompiled/App.onCreate.java`, ~19k líneas de smali, no legible como Java).

## Nota sobre el versionado

Todo lo propietario de CFE (APK, `res-decoded/`, `.so`, `.bks`, `.pem`, DEX) está en
`.gitignore` y **no se sube al repositorio**. Vive en el SSD bajo
`/Volumes/PortableSSD/apps-gobmx-mx/`. Este directorio solo versiona los documentos de
análisis originales y volcados mínimos de metadatos.
