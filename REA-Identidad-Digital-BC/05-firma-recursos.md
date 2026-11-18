# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico del GEBC)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG, Qrobús…).
**No verificable como el artefacto auténtico del Gobierno de Baja California**: sirve para
arquitectura, no para integridad. Binario auténtico: Google Play con cuenta + comparar
certificado. Ver `evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Almacenamiento (nota)

El token de sesión (`TokenSession`/`access_token`) se maneja con `getSharedPreferences`. Se vio
una referencia a `AndroidKeyStore` en el DEX, pero **no se confirmó** que cifre el token (podría
venir de una librería). → Se reporta el uso de `SharedPreferences` para la sesión **sin confirmar
cifrado**; combinado con el logging de cuerpos (doc 04), el token tiene más de una vía de
exposición. Marcado como no verificado, sin inventar.

## Recursos

- 2 dex, 7 175 clases; app de tamaño medio. UI de wallet (credenciales, tarjetas, documentos).
- reCAPTCHA Enterprise + Firebase (Phone Auth + FCM) como dependencias principales.
