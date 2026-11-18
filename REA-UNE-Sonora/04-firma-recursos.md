# Firma y procedencia

## Firma de desarrollador individual (original preservada)

```
Signer #1 certificate DN: CN=Omar Aguilar Fraga, O=Bus Sonora, L=Hermosillo, ST=Sonora, C=MX
```

- El certificado es de un **desarrollador/empresa** (`Omar Aguilar Fraga` / `Bus Sonora`,
  Hermosillo), **no** el genérico `CN=Android, O=Google Inc.` de APKPure → la firma **original se
  conservó** (APKPure redistribuyó el APK sin re-firmarlo). En ese sentido, el binario es
  **íntegro respecto a quien lo firmó**.
- Matiz: el firmante es una **persona/empresa privada (Bus Sonora)**, no una dependencia del
  Gobierno de Sonora. Para una app de **servicio público** es una observación de cadena de
  suministro: la identidad que respalda el binario es la del operador/proveedor, no la del
  Estado (mismo tipo de nota que Mi Policía CDMX, firmada por un dev individual).

Ver `evidence/firma.txt` para esquemas (v1/v2/v3) y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos

- APK ~33 MB (runtime Flutter + assets).
- Splits: `config.*` (arquitectura con `libapp.so`, densidad, idiomas).
- SDKs embebidos: Sentry, reCAPTCHA, Firebase, AdServices (doc 03).
