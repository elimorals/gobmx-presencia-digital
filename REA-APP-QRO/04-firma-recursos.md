# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico del Gobierno de Querétaro)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG…). **No
verificable como el artefacto auténtico del Poder Ejecutivo de Querétaro**: sirve para
arquitectura, no para integridad. Binario auténtico: Google Play con cuenta + comparar
certificado. Ver `evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos

- Runtime Flutter (`libapp.so`/`libflutter.so` en el split de arquitectura) + 506 entradas en
  `flutter_assets/`.
- SDKs embebidos: Firebase (Remote Config/FCM/Installations), ExoPlayer, ML translate.
