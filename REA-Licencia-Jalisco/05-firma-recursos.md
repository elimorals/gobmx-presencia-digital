# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG, Licencia
EDOMEX). **No verificable como el artefacto auténtico** de la Secretaría de Transporte de Jalisco
/ Cosmocolor: vale para arquitectura, no para integridad. Para el binario auténtico, descargar de
Google Play con cuenta y comparar el certificado. Ver `evidence/firma.txt`.

> Nota: el defecto crítico (doc 02) está en el **código** y es independiente de la re-firma —
> estaría igual en el binario auténtico de Play, porque es parte del producto Cosmocolor.

## Hashes
Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos / estructura
- 6 dex, 22 183 clases (app grande; mismo tamaño que EDOMEX).
- SQLCipher (`.so`), BouncyCastle, Regula Forensics, Firebase, Compose.
- `decompiled/ServiceGenerator.createService.java` conserva el cuerpo del defecto (no versionado).
