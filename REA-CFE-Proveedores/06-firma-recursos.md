# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de la CFE)
```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```
Certificado genérico de APKPure (mismo patrón que CFE Contigo, ICVNL, etc.). **No verificable
como el artefacto auténtico de la CFE**: sirve para arquitectura, no para integridad. Para el
binario auténtico, descargar de Google Play con cuenta y comparar el certificado. Ver
`evidence/firma.txt`.

## Hashes
Ver [`hashes.sha256`](hashes.sha256) (SHA-256 del APK arm64-v8a).

## Recursos / estructura
- Xamarin.Android clásico: `assemblies/assemblies.blob` (XABA v1, 52 ensamblados, XALZ/LZ4) +
  `assemblies.manifest`.
- Ensamblados propios: `DemoCFE.dll` (1.27 MB), `DemoCFE.Android.dll` (508 KB) — en `evidence/`,
  no versionados.
- Build 2024-06-18 (versionCode `20240618`), targetSdk 33 (relativamente actual para Xamarin,
  que está EOL frente a .NET MAUI).
