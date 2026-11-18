# Firma y procedencia

## Firma AUTÉNTICA del INEGI

```
Signer #1 certificate DN: CN=Alberto Ramos Lazcano, OU=ORG, O=INEGI, L=AGS, ST=AGS, C=52
Signer #1 SHA-256:        5cadf3e7db76928ff9be4e5c621c5629e68cdd83f03df87d75c34b2eab6c91d1
```

- El certificado identifica al **INEGI** (`O=INEGI`, Aguascalientes), firmado por un desarrollador
  de la institución. **No** es el genérico `CN=Android, O=Google Inc.` de APKPure → la firma
  **original se conservó** y verifica → **binario auténtico del emisor**. Base de análisis
  confiable (al contrario que las re-firmadas del acervo).
- Nota: `C=52` es un valor de país no estándar (México es `MX`/`484`); dato anecdótico del
  certificado, sin impacto.

Ver `evidence/firma.txt` para esquemas (v1/v2/v3) y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): APK `mx.org.inegi.denuemv@arm64-v8a.apk`.

## Recursos / estructura

- 1327 recursos; 2007 clases (R8). Build **viejo**: targetSdk 33 (2022), `com.google.android.gms.analytics`
  **deprecado**, enlaces `goo.gl` (servicio apagado por Google en 2025).
- `libblur.so` (única `.so`): efecto de desenfoque de UI.
- La **Maps API key** (doc 04) se conserva solo en `evidence/maps-api-key.txt` (no versionado).
