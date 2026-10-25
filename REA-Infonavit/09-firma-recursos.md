# 09 · Firma, autenticidad y estructura

## Firma del APK

`keytool -printcert -jarfile base.apk` (apksigner no disponible en el host):

```
Propietario: CN=serviciosweb.infonavit.org.mx, OU=SGTI, O=INFONAVIT, L=CDMX, ST=CDMX, C=MX
Emisor:      CN=serviciosweb.infonavit.org.mx, OU=SGTI, O=INFONAVIT, L=CDMX, ST=CDMX, C=MX
Algoritmo de firma: SHA256withRSA
SHA-256 cert: 84:B7:C1:48:B8:3F:92:28:32:DD:45:8F:58:05:34:A2:04:72:7B:56:DA:EC:64:06:FB:30:4A:15:49:11:13:1D
```

Certificado **autofirmado por INFONAVIT** (`O=INFONAVIT, OU=SGTI` — Subdirección General de TI).
Todo cert de firma de app Android es autofirmado; lo relevante es que el titular es la propia
organización, no un tercero.

### Autenticidad

A diferencia de CFE Contigo (APKPure, re-firmado `CN=Android` → no verificable), este APK conserva
una firma con identidad de INFONAVIT → **señal fuerte de que es el artefacto auténtico** y no fue
re-empaquetado por el espejo de descarga. No se pudo cruzar contra el certificado exacto de Google
Play (no se tiene la referencia oficial), así que no se afirma "verificado por Play"; se afirma
"firmado por certificado de INFONAVIT, no re-firmado". `keytool` valida la cadena del JAR pero no
los bloques de firma APK v2/v3 (requiere `apksigner`, ausente).

## Librerías nativas

`lib/<abi>/` para `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` (APK multi-ABI; el archivo del SSD
es el split `arm64-v8a`):

| .so | Origen | Función |
|---|---|---|
| `libmodpdfium.so` (~4.8 MB) | PDFium | motor de render de PDF |
| `libjniPdfium.so` | PDFium JNI | puente Java↔PDFium |
| `libmodft2.so` | FreeType | fuentes para PDF |
| `libmodpng.so` | libpng | imágenes PDF |
| `libdatastore_shared_counter.so` | Jetpack DataStore | contador compartido |
| `libc++_shared.so` | NDK | runtime C++ |

Ninguna librería de packer/protección (ver doc 06). `extractNativeLibs="false"` (se cargan desde
el APK comprimido).

## Estructura del APK

- `apktool.yml` (en `evidence/`): apktool 3.0.3, packageId 127, minSdk 24, targetSdk 36,
  versionCode 99, versionName 5.3.48.
- DEX múltiple: `classes.dex` … (20 093 clases; smali en `smali`, `smali_classes2..N`).
- `resources.arsc` + `res/` (1 930 recursos). `AndroidManifest.xml` decodificado en la raíz del REA.
- `res-decoded/`: `network_security_config.xml`, `provider_paths.xml`.

## Hashes

- APK: `1f824c78a48f1e06a3a203c9e912d6d291954be3b1e389ef7ff6b93d4ba0b6bd` (`hash-apk.txt`).
- Documentos de este REA: `hashes.sha256`.

## Confirmación en vivo (v3)

Instalada en el Motorola (doc 11), `dumpsys package` reporta
`signatures=PackageSignatures{… version:3 …}`, `apkSigningVersion=3` y el mismo SHA-256
`84:B7:C1…1D`. Es decir, la firma **sí es APK Signature Scheme v3** (lo que `keytool` no veía) y
coincide con el estático → autenticidad confirmada en dispositivo.

## Límite

No se comparó contra el certificado de Play oficial (no se tiene la referencia). La verificación
v3 proviene del instalador de Android (`dumpsys`), no de `apksigner`.
