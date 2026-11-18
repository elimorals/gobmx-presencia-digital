# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico del IQT)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG…). **No
verificable como el artefacto auténtico del IQT/Qrobús**: vale para arquitectura, no para
integridad. Binario auténtico: Google Play con cuenta + comparar certificado. Ver
`evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos

- APK ~41 MB (runtime Flutter + assets).
- Splits: `config.*` (arquitectura con `libapp.so`, densidad, idiomas).
- SDKs embebidos: Stripe (pagos), Firebase (FCM).
