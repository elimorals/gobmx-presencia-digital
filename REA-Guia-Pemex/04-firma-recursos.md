# Firma y procedencia

## Firma AUTÉNTICA de Pemex
```
Signer #1 certificate DN:  CN=Petroleos Mexicanos, OU=PEMEX, O=PEMEX, L=Mexico, ST=Distrito Federal, C=MX
Signer #1 SHA-256:         7745b8b9ad7674b435a8e477e9c86a82fe26e0741025e3f55a0fea44a8f4a3c4
Source Stamp Signer DN:    CN=Android, OU=Android, O=Google Inc., …  (3257d599… — restamp de APKPure)
```
El APK está **firmado por Pemex** (`O=PEMEX`) y la firma del emisor verifica → **binario íntegro
de Pemex**. El `Source Stamp` genérico de Google es el que APKPure añade al redistribuir; **no**
reemplaza la firma de la app (a diferencia de las apps re-firmadas donde el Signer #1 es el
genérico `O=Google Inc.`). Por tanto, es el artefacto auténtico de Pemex. Ver `evidence/firma.txt`.

## Hashes
Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos
- Flutter: `assets/flutter_assets/` (incl. `.env`); runtime en el split de arquitectura.
- 19 splits de idioma + densidad + `config.armeabi_v7a` (el bundle descargado es v7a).
- El `.env` (doc 02) es el artefacto más informativo; valores reales solo en `evidence/`.
