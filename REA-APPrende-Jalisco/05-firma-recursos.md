# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de la SEJ)
```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```
Certificado genérico de APKPure (mismo patrón que CFE, Pemex, CONALITEG…). **No verificable como
el artefacto auténtico de la SEJ**: vale para arquitectura, no para integridad. Binario auténtico:
Google Play con cuenta + comparar certificado. Ver `evidence/firma.txt`.

## Hashes
Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos
- Bundle Ionic/Angular en `assets/www/` (muchos módulos lazy: calificaciones, asistencia,
  preinscripción, trámites, salud, RH) + **source maps** `.map` (doc 04).
- Config Firebase + llaves + endpoints extraídos a `evidence/firebase-keys-endpoints.txt`
  (no versionado; valores reales ahí, redactados en los `.md`).
