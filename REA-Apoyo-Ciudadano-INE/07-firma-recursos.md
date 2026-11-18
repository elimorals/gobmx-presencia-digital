# 07 — Firma del APK, procedencia y recursos

## Procedencia

El APK se descargó de **APKPure** con `apkeep` (fuente sin cuenta). A diferencia de la mayoría de las
apps bajadas por esa vía, **este binario conserva la firma original del emisor** y **no** lleva Source
Stamp de redistribuidor.

## Certificado de firma (auténtico)

```
Signer #1 certificate DN:   CN=INE, OU=UNICOM, O=Instituto Nacional Electoral, L=Mexico, ST=DF, C=MX
Signer #1 SHA-256 digest:   93de2f54824fa27911a69df040721a6f4dbd68041a564fec57ed1cda48cc2be3
Signer #1 SHA-1 digest:     6f700f0e7ecc7fdfcb4f3424463c41991e9df705
```

- El firmante es el **Instituto Nacional Electoral** (`O=Instituto Nacional Electoral`, `OU=UNICOM` —
  la Unidad Técnica de Servicios de Informática del INE), no un certificado genérico de redistribuidor.
- Esquemas de firma: **v2 = verificado**; v1/v3/v3.1/v4 = no; **SourceStamp = no**.

> **Consecuencia:** la firma verifica y es la llave del INE → este APK es **el artefacto auténtico
> firmado por el INE**, no fue alterado después de firmarse. Es una procedencia **más fuerte que CFE
> Contigo** (que llegó re-firmado por APKPure con `CN=Android, O=Google Inc.`). Coincide en fortaleza
> con Valida INE-QR, también firmado por el INE.

## Estructura

| Elemento | Detalle |
|---|---|
| DEX | `classes.dex`, `classes2.dex`, `classes3.dex` (≈16 MB de código, 14 034 clases) |
| Nativo (`lib/arm64-v8a/`) | PersonalCode, OpenCV 3, tesseract/leptonica, cardIO, Realm, Fresco |
| Recursos OCR | `res/*.traineddata` (modelos tesseract; uno de ~21 MB) |
| Config | `assets/config.properties` (endpoints/llaves cifrados), `assets/custom.properties` (`app.local=false`, `live.photo=true`) |
| `custom.properties` | `live.photo=true` → la captura de foto en vivo está habilitada |

## Verificación independiente

El SHA-256 del APK está en [`hashes.sha256`](hashes.sha256):
`d06daf0d78df4bab89e47b2c71a221cd49f8c539f2f3d446c5618253345497fe`. El certificado puede recomputarse
con `apksigner verify --print-certs` sobre ese APK.
