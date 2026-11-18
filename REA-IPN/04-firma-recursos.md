# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico del IPN)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG…). **No
verificable como el artefacto auténtico del IPN**: vale para arquitectura, no para integridad. Para
el binario auténtico, descargar de Google Play con cuenta y comparar el certificado. Ver
`evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256).

## Recursos

- Flutter engine 3.5.3 stable; `libapp.so` (Dart AOT) + `libflutter.so` en el split de arquitectura.
- `evidence/network_security_config.xml` — el recurso clave (doc 01).
- `decompiled/endpoints.txt` — 39 endpoints recuperados del `libapp.so`/DEX.
- SDKs: Firebase (FCM), Google Maps.
