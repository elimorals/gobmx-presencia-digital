# Urbani (MiRuta Monterrey) — Ingeniería inversa (REA)

> Análisis **estático** del App Bundle `com.miruta.mty`. La app nunca se ejecuta.
> Herramientas: REA (JADX headless 0.7.1 + apktool 3.0.3) + análisis binario propio.
> Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| Archivo (base) | `com.miruta.mty.apk` (132,440,286 bytes, APK base del bundle) |
| SHA-256 (base) | `639655b1fed590ade9356765e72ed6dfd21ec5faafbd2ba820fd36ea3a29b852` |
| Bundle | App Bundle (3 APK): base + `config.armeabi_v7a` + `config.xhdpi`, 166,643,619 bytes |
| Paquete | `com.miruta.mty` |
| App | **Urbani** (MiRuta Monterrey — transporte **municipal**, Monterrey / Nuevo León) |
| Versión | 3.1.5 (versionCode 430) |
| minSdk / targetSdk | 26 / 35 (compileSdk 34 / Android 14) |
| Clases (top-level, JADX) | 33,861 · Recursos | 2,491 · Archivos decodificados | 2,709 |
| ABI | **solo `armeabi-v7a`** (ARM 32-bit); no hay split `arm64-v8a` en la fuente |
| Firma | v2 + v3 verificadas, pero `CN=Android, O=Google Inc.` **genérico** → **re-firmado** (ver doc 09) |
| Application | `o.access301` (ofuscada; envoltorio del packer Baidu — ver doc 03) |

> **Es una app municipal (Monterrey / Nuevo León), no federal.** No es gob.mx de la
> Federación; es transporte público del área metropolitana de Monterrey. Se documenta como tal.

## Qué es la app

Urbani / MiRuta es la app de **transporte público del área de Monterrey**. Por la superficie
observada estáticamente ofrece: mapa de rutas y paradas (**Google Maps** con polilíneas,
marcadores y heatmaps), **tarjeta de transporte virtual por NFC** (emulación de tarjeta HCE
ante validadores **RD300**, doc 05), **pago** (Google Pay / Google Wallet, doc 07), y **alta de
identidad con verificación facial** (doble KYC: **Baidu Face** + **Veriff**, doc 06). El flujo de
negocio real (endpoints, saldos, recargas) vive en el código **Dart**, que está **cifrado por el
packer** (doc 03) y no se recuperó estáticamente.

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| Framework | **Flutter** (Dart AOT) | `assets/flutter_assets/`, `libflutter.so`, `libapp.so`; doc 02 |
| Protección | **Baidu Protect / LianTian** (packer) | `libliantian*.so`, `libbaiduprotect_sdk*.so`, `assets/liantiancontent.properties`, ~187 clases `com.baidu.liantian.*`; `libapp.so` con entropía 7.995; doc 03 |
| UI nativa | Flutter engine + plugins (código propio en `o.*` ofuscado por R8) | 33,861 clases DEX |
| Base de datos | **ObjectBox** (embebida) | `libobjectbox-jni.so` |
| Red | HTTP/HTTPS (pila Dart; cleartext **bloqueado** en release) | `network_security_config.xml`; doc 04 |
| Identidad/biometría | **Baidu Face SDK** (liveness) + **Veriff** (KYC) + biometría local | `libbdface_sdk.so`, `com.baidu.idl.face.*`, `com.veriff.*`, `localauth`; doc 06 |
| Movilidad NFC | **flutter nfc_host_card_emulation** (HCE) | `AndroidHceService`, `@xml/apduservice`; doc 05 |
| Pagos | **Google Pay** (plugin `pay_android`) + Google Wallet | `io.flutter.plugins.pay_android.*`, `gms.wallet.api.enabled`; doc 07 |
| Visión | **ML Kit** (barcode, face, vision) + ZXing/journeyapps | `libbarhopper_v3.so`, `com.journeyapps.barcodescanner`; doc 08 |
| Push / backend-as-a-service | **Firebase** completo (ver doc 07) | 7 plugins `firebase.*`, `firebaseinitprovider` |
| Ubicación | **Geolocator** (foreground service) | `com.baseflow.geolocator.GeolocatorLocationService` |
| Mapas | **Google Maps** | `io.flutter.plugins.googlemaps.*`, `geo.API_KEY` |
| Background | **transistorsoft background_fetch** | `com.transistorsoft.tsbackgroundfetch.*` |

## Mapa de componentes

El código **propio** de la app (negocio) está compilado a **Dart** dentro de `libapp.so`
(cifrado, doc 02/03) y **no es legible**. En el DEX Java solo quedan: el engine Flutter, los
plugins (`io.flutter.plugins.*`), los SDKs de terceros (Firebase, Baidu, Veriff, ML Kit,
ObjectBox…) y el shell del packer (`com.baidu.liantian.*`, `o.access301`). No hay un mapa de
"módulos de negocio" en Java como en apps nativas: en Flutter ese mapa vive en Dart.

## Hallazgos principales

Ver los 8 hallazgos en [README.md](README.md) y el detalle en [10-hallazgos-seguridad.md](10-hallazgos-seguridad.md). En una línea cada uno:
1. **Lógica Dart cifrada por packer chino Baidu** (`libapp.so` entropía 7.995, 0 secciones snapshot) → endpoints/cripto de la app no recuperables estáticamente.
2. **APK re-firmado** con certificado genérico `CN=Android` → autenticidad del publicador original no verificable.
3. **Doble KYC biométrico** (Baidu Face + Veriff): datos faciales; Baidu es proveedor chino (soberanía de datos en app de gobierno).
4. **Dos API keys de Google en claro** (Maps + Firebase) en manifest/recursos.
5. **Tarjeta de transporte NFC HCE** con `requireDeviceUnlock="false"` (emulación sin desbloqueo).
6. **FileProvider** expone `cache-path path="/"` (todo el cache).
7. **Cleartext efectivamente bloqueado** en release (positivo), pero sin cert pinning a nivel plataforma.
8. **Solo `armeabi-v7a`** (ARM 32-bit) en la fuente; `libed66.so` de función no determinada.

## Limitaciones

**Estáticamente**, el código Dart de `libapp.so` está **cifrado por el packer Baidu**: endpoints,
cripto de la tarjeta y flujos de pago no se recuperan del binario. **El pase dinámico**
([11-analisis-dinamico.md](11-analisis-dinamico.md), dispositivo real sin root) sí recuperó el
**backend real** (`urbani.io`, HTTPS) y la **arquitectura** (server-driven UI, tabs, feature flags),
porque la app tiene **logging verboso en release**. Lo sólido del estático: manifest, permisos,
componentes, SDKs (clases DEX y `.so`), config Firebase/Google, red, NFC y firma. **No se evadió
ninguna protección.** Lo que sigue bloqueado (requiere root): cripto de la tarjeta, cuerpos de los
endpoints autenticados y el volcado de `libapp.so` descifrada (chocaría con el `EmulatorAcquisition`
de LianTian).
