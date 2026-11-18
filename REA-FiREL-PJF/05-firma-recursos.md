# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico del CJF)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, CONALITEG…). **No
verificable como el artefacto auténtico del Consejo de la Judicatura Federal**: vale para
arquitectura, no para integridad. Binario auténtico: Google Play con cuenta + comparar el
certificado. Ver `evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): `gob.mx.android.firel@arm64-v8a.apk`.

## Recursos / estructura

- 3 `classes.dex`, 9 872 clases, 1 540 recursos.
- **Biometría on-device:** `assets/identy_data/*.ort` (modelos ONNX de Identy) + libs nativas
  `libfinger-native-lib.so`, `libface-native-lib.so`, `libonnxruntime.so`, `libsupport-native-lib.so`,
  `libb61a.so`, `libc656.so`.
- **JUnit en el release** (`org.junit.*`, `org.junit.runners/internal/runner`) — framework de
  test empaquetado en producción. No es ejecutable por el usuario, pero es bloat y revela
  estructura de pruebas; no debería ir en el bundle.
- `assets/PublicSuffixDatabase.list` (OkHttp) — confirma OkHttp presente (la base del
  CertificatePinner / getUnsafeOkHttpClient, doc 04).
