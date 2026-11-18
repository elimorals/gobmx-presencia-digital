# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de la JAPAY)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG, Qrobús…).
**No verificable como el artefacto auténtico de la JAPAY**: vale para arquitectura, no para
integridad. Binario auténtico: Google Play con cuenta + comparar certificado. Ver
`evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): APK analizado (`@arm64-v8a`).

## Recursos

- Bundle Cordova `assets/www/` (jQuery + lógica propia + plugins). Incluye múltiples **respaldos**
  de los JS y archivos `.php` (doc 02).
- Librería `js-rijndael` empaquetada **sin uso** (doc 03).
