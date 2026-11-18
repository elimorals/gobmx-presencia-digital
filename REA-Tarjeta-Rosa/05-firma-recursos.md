# Firma y procedencia

## ⚠️ Procedencia: APK re-firmado por APKPure (no es el binario original del GEG)

El APK se obtuvo de **APKPure** (vía la web, tras no estar disponible por `apkeep`). La
verificación de firma (`apksigner verify --print-certs`) lo confirma:

```
Signer #1 certificate DN:  CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
Signer #1 SHA-256:         42bc069c6e47188c3729f9e1036bb52737179d4e866cabc5ab84517cb5040ed8
Source Stamp Signer DN:    CN=Android, OU=Android, O=Google Inc., …
Source Stamp SHA-256:      3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

- El certificado de firma es el **genérico `CN=Android, O=Google Inc.`**, no uno del Gobierno
  de Guanajuato ni de su proveedor. Es el patrón de un APK **re-empaquetado y re-firmado** por
  el pipeline de redistribución de APKPure (el mismo Source Stamp `3257d599…` que aparece en
  CFE y otras del acervo).
- **Consecuencia:** este binario **no es verificable como el artefacto auténtico publicado por
  el GEG**. Sirve para analizar la arquitectura (lo que hace este repositorio), pero **ninguna
  conclusión de integridad** debe asumir que el binario no fue alterado por el redistribuidor.

### Cómo obtener el binario auténtico

Descargar desde **Google Play con una cuenta** (o del canal oficial del GEG) y comparar el
certificado de firma. Solo entonces la firma identifica al emisor real.

## Hashes

Ver [`hashes.sha256`](hashes.sha256):

```
e185a49c7482…  gob.guanajuato.TarjetaRosa@arm64-v8a.xapk   (bundle descargado)
6effd81379ac…  _base/gob.guanajuato.TarjetaRosa.apk        (APK base analizado)
```

## Recursos

- 891 recursos; app pequeña (APK base 6.7 MB, 2 dex). La UI real es remota (web app), por eso
  el APK es ligero.
- `promoWebKit/1.0` añadido al User-Agent (marca el tráfico del WebView).
- `splits0.xml` + metadatos `com.android.vending.splits` → distribución por App Bundle (el
  `config.arm64_v8a`/densidad van en splits aparte).
