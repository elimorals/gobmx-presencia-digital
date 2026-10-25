# 09 — Firma, estructura y recursos

## Firma del APK base

`apksigner verify -v --print-certs` (salida completa en
[`evidence/apksigner-verify.txt`](evidence/apksigner-verify.txt)):

```
Verifies
v1 (JAR): false   v2: true   v3: true   v3.1: false   v4: false
Source Stamp: true
Number of signers: 1

Signer #1 DN:  CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
Signer #1 cert SHA-256: e1b3eb9e03fdee22933dea99d37c1d378de0ced5917e0d8c6a560e0de1e2073b
Signer #1 cert SHA-1:   b5f0e812e42a9aca21c772ea0d19d5aec8a2a560
Signer #1 key:          RSA 4096

Source Stamp DN:  CN=Android, OU=Android, O=Google Inc., L=Mountain View, ...
Source Stamp SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

El bloque de firma v2+v3 existe (`APK Sig Block 42`, IDs `0x7109871a` v2 y `0xf05368c0` v3
verificados por byte-magic).

### Interpretación — artefacto re-firmado

El certificado del firmante es el **genérico de depuración `CN=Android, O=Google Inc.`**, no un
certificado de desarrollador. Una app auténtica de Google Play lleva el cert del publicador y un
**Source Stamp de Google Play** (`O=Google Inc.` con el OID de Play, no el DN genérico). Aquí el
Source Stamp es el **mismo DN genérico `CN=Android`** → el artefacto fue **re-empaquetado y
re-firmado por un mirror** (la fuente es APKPure, por el flujo de descarga). 

**Consecuencia:** **no se puede verificar la autenticidad del publicador original** (la alcaldía /
operador de transporte) ni garantizar que el bytecode no fue alterado respecto al de Play. Es el
mismo patrón que CFE Contigo en este repositorio. El análisis asume que el contenido es
representativo de la app pública, pero la cadena de firma **no lo prueba**.

## Estructura del bundle

| APK | SHA-256 | Rol |
|---|---|---|
| `com.miruta.mty.apk` | `639655b1…a29b852` | base (DEX, recursos, flutter_assets) |
| `config.armeabi_v7a.apk` | `23f58a15…d8ec6de9` | nativas (`.so`) |
| `config.xhdpi.apk` | `75d5ac51…c71b481` | densidad xhdpi |

Todos los hashes en [`hashes.sha256`](hashes.sha256). Metadatos del bundle en
[`bundle-manifest.json`](bundle-manifest.json).

## Recursos

- `decoded_file_count = 2709`, `resource_count = 2491`.
- **92 locales** declarados (tabla estándar de Flutter/Play; la app en sí es `Urbani` en todos).
- `assets/flutter_assets/` incluye `assets/fonts/` (familia propia T1–T12 + branding-sf),
  `google_fonts/` (Inter), `assets/audios/`, `AssetManifest.bin`, `NOTICES.Z`.
- XML de seguridad decodificados en `res-decoded/res/xml/`:
  `network_security_config.xml` (doc 04), `apduservice.xml` (doc 05), `file_paths.xml`,
  `flutter_share_file_paths.xml`, `splits0.xml`.

## FileProvider

```
res/xml/file_paths.xml:  <cache-path name="images" path="/" />
```

Expone **todo** el directorio de caché (`path="/"` dentro de `cache-path`) vía `content://` con
`grantUriPermissions="true"`. Es cache (no `files/` ni `root-path`), así que el alcance es menor que
los `root-path "/"` vistos en CFE/jw-library, pero sigue siendo más amplio de lo necesario
(bastaría `cache/images/`). Hallazgo menor, doc 10 (H6).
