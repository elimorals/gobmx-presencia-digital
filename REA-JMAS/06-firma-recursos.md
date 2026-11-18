# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de la JMAS)
```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```
Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG…). **No
verificable como el artefacto auténtico de la JMAS Chihuahua**: vale para arquitectura, no para
integridad. Binario auténtico: Google Play con cuenta + comparar certificado. Ver
`evidence/firma.txt`.

## Hashes
Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos
- APK ~22 MB. App Compose moderna; splits de idioma (`en`/`es`) y densidad, sin split de
  arquitectura (no hay `.so` propias → Java/Kotlin puro).
