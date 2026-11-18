# Firma, recursos y procedencia

## Firma del APK (auténtica, preservada)

`apksigner verify --print-certs` sobre el APK base:

```
Signer #1 certificate DN: CN=Eduardo Martinez, OU=cweb, O=cweb, L=México, ST=Distrito Federal
Signer #1 SHA-256: 58ac094fee7db4bb88c1813c56cdf5f34fea931c6df800718cb9e9d0cd21a85f
Source Stamp Signer DN: CN=Android, OU=Android, O=Google Inc., …
Source Stamp SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

- El certificado de firma es de **`O=cweb` (CN=Eduardo Martinez), L=México** — el
  **desarrollador/proveedor real** de la app, no el genérico `CN=Android, O=Google
  Inc.`. La firma **verifica**. Es decir: el binario está firmado con la llave
  original del emisor y no fue re-firmado por el redistribuidor.
- Hay un **Source Stamp** genérico de Google (`3257d599…`, el mismo sello que
  aparece en los APK redistribuidos por APKPure). Esto indica que APKPure
  **redistribuyó el APK original sin alterar su firma de aplicación** — distinto de
  CFE, donde APKPure re-firmó la app entera con el certificado genérico.
- **Consecuencia:** a diferencia de CFE, aquí la firma de aplicación **sí**
  identifica al emisor (`cweb`, proveedor de la SAF CDMX) y el binario es
  verificable como el firmado por ese proveedor. La procedencia de la **descarga**
  siguió siendo APKPure (de ahí el Source Stamp); para una cadena de custodia
  perfecta se compararía contra el APK del canal de Google Play.

## Estructura

- Distribución **XAPK** (base `mx.wdd.sefin.apk` + splits `config.*` de idioma,
  densidad y `armeabi_v7a`). Se analizó el **APK base** (contiene `assets/public/`,
  manifiesto, DEX y recursos).
- `assets/public/` — bundle web de Capacitor (Angular/Ionic), ~1.6k entradas.
- `assets/capacitor.config.json`, `assets/capacitor.plugins.json` — config de la
  capa híbrida.

## Hashes

Ver [`hashes.sha256`](hashes.sha256) (XAPK y APK base).
