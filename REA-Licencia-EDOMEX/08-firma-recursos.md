# Firma del APK, recursos y procedencia

## ⚠️ Procedencia: APK re-firmado por APKPure (no es el binario original de SEMOV)

El APK se descargó de **APKPure** con `apkeep` (apk-pure). La verificación de firma lo confirma:

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
Signer #1 SHA-256: 087925a0567eb79053c77553dc6ce3eb934d541099f53beb61b05158b59f2eed
Source Stamp Signer DN: CN=Android, OU=Android, O=Google Inc., …
Source Stamp SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
Esquemas: v2=true, v3=true (v1/v4=false)
```

- El certificado es el **genérico `CN=Android, O=Google Inc.`**, no uno de SEMOV ni de Cosmocolor.
- El **Source Stamp** es el de "Google Inc." genérico de APKPure (SHA-256 `3257d599…`, el mismo
  que aparece en CFE Contigo, re-firmada por la misma fuente).

**Consecuencia:** este binario **no es verificable como el artefacto auténtico publicado por
SEMOV/Cosmocolor**. Para análisis de arquitectura (lo que hace este repositorio) sigue siendo
útil, pero **ninguna conclusión de integridad debe asumir** que el binario no fue alterado por el
redistribuidor. Para el binario auténtico: descargar de Google Play con una cuenta de Google y
comparar el certificado real del emisor.

## Estructura

- `mx.com.cosmocolor.movapp`, versionName 16, `platformBuildVersionCode=36`.
- 6 `classes.dex` (principal 13 MB), 22 181 clases, **0 librerías nativas** (`lib/*.so`).
- `assets/`: `PublicSuffixDatabase.list` (OkHttp), `PublicSuffixDatabase`. Recursos Compose +
  databinding generados.
- XAPK con splits de idioma/densidad; el APK base (`mx.com.cosmocolor.movapp.apk`) contiene todo
  el código y los assets analizados.

## Hashes

Ver `hashes.sha256` (SHA-256 del XAPK y del APK base).
