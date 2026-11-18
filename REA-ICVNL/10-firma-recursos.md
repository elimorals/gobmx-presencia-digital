# 10 — Firma, procedencia y estructura

## Firma del APK — AUTÉNTICA del emisor

`apksigner verify --print-certs --verbose` sobre el APK base:

```
Verified using v1 scheme (JAR signing): true
Verified using v2 scheme (APK Signature Scheme v2): true
Verified using v3 scheme (APK Signature Scheme v3): true
Number of signers: 1
Signer #1 certificate DN: CN=Instituto de Control Vehicular de Nuevo Leon, OU=ICVNL, O=ICVNL,
                          L=Monterrey, ST=Nuevo Leòn, C=MX
Signer #1 certificate SHA-256: 0f5cb8e90bdc73f584cef14374d4245aac7174ef94da3c96a37eb8dda2dcc1dc
```

- El certificado es del **emisor real** (`O=ICVNL, Instituto de Control Vehicular de Nuevo Leon`),
  no el genérico `CN=Android, O=Google Inc.` de los APK re-firmados por APKPure. La firma
  **verifica** bajo v1+v2+v3.
- **Más fuerte que CFE/IMSS:** este binario está firmado con la llave del ICVNL y la firma es
  válida → es el artefacto auténtico, no alterado tras firmarse. APKPure lo redistribuyó sin
  re-empaquetar.
- El manifiesto trae `com.android.stamp.source=https://play.google.com/store` +
  `STAMP_TYPE_DISTRIBUTION_APK` → origen Google Play.

## Estructura del bundle (XAPK)

- `com.icv.app@arm64-v8a.xapk` → base `com.icv.app.apk` + splits `config.arm64_v8a`,
  `config.*` (densidad/idioma).
- Código .NET: **AssemblyStore** en `lib/arm64-v8a/libassembly-store.so` (38 MB, 425 ensamblados
  LZ4). Runtime: `libmonodroid.so`, `libmonosgen-2.0.so`, `libxamarin-app.so`,
  `libSystem.*.so`. Ver doc 02 para la extracción.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK, APK base, split arm64, `libassembly-store.so` y
`ICV_APP.dll`.

## Artefactos (no versionados, `.gitignore`)

- `evidence/so/` — `.so` del split arm64 (incl. `libassembly-store.so`).
- `evidence/assemblies/` — los 424 DLL extraídos (`ICV_APP.dll` y framework/terceros).
- `evidence/apktool/` — recursos decodificados.
- `evidence/endpoints.txt` — base + acciones de la API.
