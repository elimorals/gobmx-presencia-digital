# Firma y procedencia

## ✅ Firma AUTÉNTICA de Banco de México (binario íntegro del emisor)

```
Signer #1 certificate DN: CN=Banco de México, OU=Banco de México, O=Banco de México,
                          L=Ciudad de México, ST=Distrito Federal, C=MX
```

- El certificado identifica al **emisor real** (Banco de México). La firma **verifica** → APKPure
  redistribuyó el **APK original sin alterarlo**. Es el **artefacto auténtico** publicado por
  Banxico — la base más fuerte posible para un análisis (como BanjeCel/Banjército, y a diferencia
  de las re-firmadas por APKPure).

Ver `evidence/firma.txt` para el detalle de esquemas (v1/v2/v3) y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos / estructura

- APK ligero (2 dex); la única `.so` es `libandroidx.graphics.path.so` (AndroidX).
- Sin `networkSecurityConfig` por recurso — la seguridad de transporte se implementa **en código**
  con pinning propio (doc 04).
- Ofuscado con R8 (clases `a/b/c`); strings de algoritmos y `AndroidKeyStore` visibles.
