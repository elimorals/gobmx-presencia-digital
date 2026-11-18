# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de Agua de Hermosillo)
```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```
Certificado genérico de APKPure (mismo patrón que CFE, Pemex, Qrobús, etc.). **No verificable
como el artefacto auténtico de Agua de Hermosillo**: vale para arquitectura, no para integridad.
Binario auténtico: Google Play con cuenta + comparar certificado. Ver `evidence/firma.txt`.

## Hashes
Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle) + APK base analizado.

## Recursos
- Runtime Flutter (`libapp.so`/`libflutter.so` en el split arm64) + assets Flutter.
- SDKs: Firebase, Google Maps, AdServices (doc 04).
