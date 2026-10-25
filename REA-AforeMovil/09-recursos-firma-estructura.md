# 09 — Firma, recursos y estructura del bundle

## Firma del APK (verificada)

`apksigner verify --print-certs` (build-tools 36.1.0) sobre el APK base:

| Esquema | Resultado |
|---|---|
| v1 (JAR) | no |
| **v2 (APK Signature Scheme v2)** | **verificado** |
| **v3 (APK Signature Scheme v3)** | **verificado** |
| v3.1 / v4 | no |
| **Source Stamp** | **verificado** |

- **Firmante**: `O=procesar` (certificado oficial de Procesar).
  - SHA-256 cert: `ceb39723634964cde7f14e1285d9ab235ea67136edbf5dea747fe563da7401d0`
  - SHA-1 cert: `db0dcde7d76119a79232f5bdef816985459764e9`
  - Clave pública SHA-256: `a33dc842c796fa8598e63d0cbf03b38a8cebdf36403529d39511d57b47a10a9d`
- **Source Stamp**: `CN=Android, O=Google Inc.` (SHA-256
  `3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d`) → sello de distribución de
  **Google Play**: el bundle proviene de Play, no de un re-empaquetado.

> Procedencia fuerte, a diferencia de **CFE Contigo** (de APKPure, firma no verificada por el motor).

## Estructura del bundle XAPK

`xapk_version 2`, `total_size 141,727,045 bytes`, 20 APK:

| Tipo | Archivo(s) | Notas |
|---|---|---|
| Base | `mx.com.procesar.aforemovil.apk` (111,809,524 B) | DEX + recursos + assets |
| ABI | `config.armeabi_v7a.apk` (28,559,480 B) | **Todas las `.so`** (nativo) |
| Idioma | `config.{ar,de,en,es,fr,hi,in,it,ja,ko,my,pt,ru,th,tr,vi,zh}.apk` | 18 splits de idioma |
| Densidad | `config.tvdpi.apk` | split de densidad |

`android:extractNativeLibs="false"`, `splits.required=true`. El SHA-256 de los 20 APK está en
[`hashes.sha256`](hashes.sha256) y el manifiesto original del bundle en
[`xapk-manifest.json`](xapk-manifest.json).

> Nota: el bundle descargado es `@arm64-v8a`, pero el split de ABI presente en la extracción es
> `armeabi_v7a`. Las `.so` listadas abajo provienen de ese split; en arm64 existen equivalentes.

## Librerías nativas (`config.armeabi_v7a.apk`)

| `.so` | Rol |
|---|---|
| `libDaonFaceDetector`, `libDaonFaceV3`, `libVDFaceVerificationAPI-jni` | Detección / verificación facial |
| `libDaonFacePassiveLivenessV1` | Liveness pasivo |
| `libDaonFaceQuality`, `libDaonFaceMaskDetector` | Calidad / mascarilla |
| `libDaonIDCapture`, `libFaceIad` | Captura de documento / presentación |
| `libFaceToolChecker`, `libtoolChecker` | Anti-tamper / integridad (doc 06) |
| `librenderscript-daon`, `librenderscript-xpd`, `libszell`, `libsmma` | Procesamiento de imagen |
| `libkeys`, `libkeys_core`, `libidx_facial_keys` | **Secretos/licencias ocultos** (doc 06) |
| `librealm-jni` | Realm (doc 04) |
| `libdatastore_shared_counter` | DataStore |
| `libimage_processing_util_jni`, `libsurface_util_jni`, `libandroidx.graphics.path` | Utilidades AndroidX/cámara |

El APK base no contiene `.so` (van en el split de ABI).

## Recursos

| Métrica | Valor |
|---|---|
| Recursos (JADX) | 1,795 |
| Archivos decodificados (apktool) | 1,849 |
| `res/xml` propios | `network_security_config`, `file_paths`, `backup_rules`, `data_extraction_rules`, `splits0` (+ shapes Material3) |
| Assets | PDFBox/fontbox (unicode, glyphlist), sin ficheros de config de red |
| `strings.xml` | contiene los endpoints (doc 05) y textos de UI |

## Identidad resumida

| Campo | Valor |
|---|---|
| Paquete | `mx.com.procesar.aforemovil` |
| Versión | 6.0.15 (793) |
| min / target SDK | 24 / 36 |
| SHA-256 base | `01b3f3fbb6ea7b47ca909cab96eaedda57e74dbc7023d2ed41ecb0208204efc6` |
| Firmante | `O=procesar` (v2+v3 + Source Stamp Play) |
