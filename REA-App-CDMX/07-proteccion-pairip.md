# 07 — Protección: Google PairIP, nativos, cert pinning

App CDMX está endurecida con **PairIP**, el sistema anti-tamper de Google (Play Automatic
Integrity Protection), no con DexGuard como CFE Contigo. La diferencia práctica es grande.

## PairIP — evidencia

```
AndroidManifest: android:name="com.pairip.application.Application"
Librería nativa: lib/armeabi-v7a/libpairipcore.so   (599,404 bytes)
Metadatos:       @xml/splits0   (control de integridad de splits)
```

### Qué hace PairIP (y qué no)

- **Sí:** envuelve la `Application`, verifica la **integridad del APK y de los splits** en
  runtime (detecta repackaging / splits manipulados), y protege rutas de código con una
  VM/ofuscación de flujo en `libpairipcore.so`. Rompe el re-firmado casual y el parcheo
  smali ingenuo.
- **No:** **no cifra los literales de string** del DEX. Por eso, a diferencia de CFE, en
  App CDMX los **endpoints, nombres de módulo, claves de preferencias y config Firebase son
  legibles** por `strings` sobre `classes*.dex` (docs 03, 04, 08). PairIP es anti-tamper,
  no anti-análisis-de-strings.

> Implicación para este trabajo: el análisis estático recupera casi toda la superficie
> funcional (API, módulos, identidad, pagos). Lo que queda tras PairIP es el **flujo de
> ejecución** de las rutas protegidas y la verificación de integridad, no los datos.

## Librerías nativas (split `armeabi-v7a`)

| `.so` | Tamaño | Origen / función |
|---|---|---|
| `libpairipcore.so` | 599,404 | **PairIP** anti-tamper (Google) |
| `libbarhopper_v3.so` | 3,244,440 | **ML Kit** barcode/QR (barhopper v3) |
| `libcalypsoHceNatives.so` | 84,012 | **Spirtech Calypso** HCE (doc 05) |
| `libimage_processing_util_jni.so` | 24,448 | **CameraX** (procesamiento de imagen) |
| `libsurface_util_jni.so` | 3,460 | **CameraX** (surface) |
| `libdatastore_shared_counter.so` | 4,416 | Jetpack **DataStore** |

`extractNativeLibs="false"`: las `.so` se cargan desde el APK comprimido (menor huella,
estándar moderno). El XAPK se descargó etiquetado `@arm64-v8a`, pero el split nativo
extraído es `armeabi-v7a`; un bundle real de Play entregaría `arm64-v8a` a la mayoría de
dispositivos.

## Certificate pinning

```
assets/ca.cert.der   (certificado DER embebido)
```

Junto con `usesCleartextTraffic="false"` y la pila OkHttp, indica **pinning contra una CA
propia** de la CDMX (o anclaje de certificado del backend `apiapp.cdmx.gob.mx`). Confirmar
el modo exacto (pin de CA vs. de hoja, y si cubre a Mercado Pago/Llave) requiere decodificar
el `CertificatePinner`/`NetworkSecurityConfig` en runtime.

## Resumen de postura

| Control | Estado |
|---|---|
| Anti-tamper / integridad | **Fuerte** (PairIP + verificación de splits) |
| Ofuscación de strings | **Ausente** (endpoints/config visibles) |
| `allowBackup` | `false` (correcto) |
| Cleartext | bloqueado (`false`) |
| Cert pinning | presente (`ca.cert.der`), alcance por confirmar |
| Legacy storage | **`true`** (riesgo de acceso amplio a almacenamiento) |
