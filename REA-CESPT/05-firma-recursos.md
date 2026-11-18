# Firma, recursos y Google Maps key

## Firma — re-firmada por APKPure

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (no de la CESPT ni de BC). **No verificable como el artefacto
auténtico de la CESPT**: vale para arquitectura, no para integridad. Binario auténtico: Google
Play con cuenta + comparar certificado. Ver `evidence/firma.txt`.

## Google Maps API key embebida (H2, Bajo)

El bundle JS contiene una API key de Google (`AIzaSy…`, valor completo en
`evidence/google-maps-key.txt`, no versionado), usada para el geocoding
(`maps.googleapis.com/maps/api/geocode/json`). Las Maps keys **viajan en el cliente por diseño**,
pero deben **restringirse** (por package + SHA-1 de firma para Android, o por API/referer) para
que un tercero no las use a costa de la cuenta de la CESPT. No se puede verificar la restricción
desde el APK; se reporta la key embebida y la recomendación de restringirla.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): SHA-256 del APK analizado.

## Recursos

- APK ~135 MB: el peso lo aportan los assets del bundle web (imágenes, íconos, chunks JS) y los
  recursos de Capacitor; no hay `.so` de lógica propia (es híbrida).
- Sin source maps (`.map`) en `assets/public` → el JS minificado es legible pero no trae el mapeo
  al código fuente original.
- `evidence/`: `network_security_config.xml`, `capacitor.config.json`, `google-maps-key.txt`,
  `firma.txt`. `decompiled/endpoints.txt`. (No versionados.)
