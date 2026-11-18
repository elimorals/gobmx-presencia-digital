# 06 — Firma del APK, procedencia y recursos

## Procedencia

APK descargado de **APKPure** con `apkeep` (fuente sin cuenta de Google), como el resto del
lote. A diferencia de varias apps re-firmadas por APKPure, **aquí la firma original del INE se
conservó** y verifica.

## Firma (apksigner verify --print-certs)

```
Signer #1 certificate DN: CN=INSTITUTO NACIONAL ELECTORAL, OU=UNIDAD TECNICA DE SERVICIOS
                          DE INFORMATICA, O=INSTITUTO NACIONAL ELECTORAL,
                          L=CIUDAD DE MEXICO, ST=CIUDAD DE MEXICO, C=MX
Signer #1 certificate SHA-256: 56b342c85eee981ec89ff6f589cadbc99fbd81937d4f9286b863ac50943e152d

Source Stamp Signer certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View,
                                    ST=California, C=US
Source Stamp Signer certificate SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

### Lectura

- El certificado de firma de app es el **propio del INE** (`O=INSTITUTO NACIONAL ELECTORAL`)
  y la firma **verifica**. Es decir, este binario está firmado por la llave real del emisor:
  APKPure redistribuyó el APK original **sin alterarlo**. Esto es **más fuerte que CFE**
  (cuyo APK quedó re-firmado con el certificado genérico `O=Google Inc.`).
- El **Source Stamp** sí es el genérico de Google (`O=Google Inc.`), con el mismo SHA-256
  `3257d5…` que aparece en otras apps del acervo: es la marca de origen de Play que acompaña a
  la redistribución. No invalida la firma de app del INE.
- Una de las dos huellas SHA-1 embebidas en la licencia del SDK (doc 03) debe corresponder a
  este certificado de firma del INE, ya que la licencia está atada al certificado del package
  (no se verificó byte a byte la correspondencia; se deja como coherencia esperada).

> **Conclusión de integridad:** este APK es verificable como **el artefacto auténtico firmado
> por el INE**. Las conclusiones de arquitectura y de seguridad de este análisis se apoyan en
> un binario con firma de emisor válida.

## Estructura del paquete

- **Formato**: bundle XAPK (split APKs). Base `mx.ine.rfe.lectorqr.apk` (~7 MB) + splits
  `config.arm64_v8a`, densidades (`config.xxhdpi`, `config.mdpi`) e idiomas (`config.es`,
  `config.en`, y ~17 idiomas más).
- **DEX**: un solo `classes.dex` (1527 clases).
- **Nativo** (en `config.arm64_v8a.apk`): `libO0p.so`, `libl1I.so` (motor de escaneo,
  ofuscados), `libimage_processing_util_jni.so` (CameraX), `libpl_droidsonroids_gif.so` (GIF).
- **Assets**: `license.txt` (licencia SDK, doc 03), `catalogo_municipios.txt` (doc 04),
  `legal.html`, fuentes Poppins/Arial Narrow, `dexopt/baseline.prof`.
- **Audio** (`res/raw/`): `beep.mp3`, `datos.mp3`, `invalidqrs.mp3` — retroalimentación sonora
  del escaneo (pitido, "datos", "QR inválido"), coherente con uso offline/en campo.

## Hashes

Ver [`hashes.sha256`](hashes.sha256):
- XAPK: `daa80f318997…`
- APK base: `c6e487a819d3…`
