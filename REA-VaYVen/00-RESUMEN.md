# Resumen ejecutivo — Va-y-Ven 4.3.0

## Identidad

| Campo | Valor |
|---|---|
| Nombre | Va-y-Ven |
| Package | `gob.yucatan.vayven` |
| Versión | 4.3.0 (versionCode 199) |
| minSdk / targetSdk | 28 / 36 |
| Dependencia | Agencia de Transporte de Yucatán (ATY) · Gobierno de Yucatán |
| Instalaciones | 1 M+ (Google Play, #18 del inventario) |
| Paquete interno | `com.embsoft.vinden` (clase app `com.embsoft.vinden.application.VindenApp`) |
| Desarrollador | embsoft / **MRP Technology** (`mrptechnology.mx`) |

## Pila

App **nativa Android (Kotlin/Java)**, 3 `.dex`. No es Flutter/Unity. El componente web es
una **WebView React** (Create React App, con `.map`/source maps) montada solo para la
**pantalla de confirmación de pago** (`assets/www`), no para la app completa. La detección
previa de "híbrida Capacitor" fue imprecisa: el grueso de la lógica (login, rutas, GPS,
monedero, tarifas) vive en el código nativo Vinden; la WebView es un puente de cobro.

Librerías nativas propias: `libapi-keys.so` (guarda claves de API),
`libimage_processing_util_jni.so` / `libsurface_util_jni.so` (CameraX, para escaneo/QR).

## Es un white-label "Vinden"

El namespace interno es `com.embsoft.vinden.*`. Va-y-Ven reusa la plataforma **Vinden** de
transporte, re-marcada para Yucatán. El mismo motor aparece referenciado contra entornos
`transporte.mrptechnology.mx` (del proveedor) y `transporteyucatan.org.mx` (producción ATY).

## Hallazgos (detalle en doc 08)

0. **Sin TLS pinning** (verificado en el código del cliente OkHttp) — doc 09.
1. **Cleartext permitido en dominios de producción** (API, checkout, dominio gob.mx oficial,
   pasarela Banorte) — doc 01, 03.
2. **Clave Google en claro** en `libapi-keys.so` — doc 05.
3. **Clave TEST de MercadoPago** en el bundle de producción — doc 04.
4. **Entornos dev/QA/prep + IPs internas + mock de Postman** embebidos — doc 03.
5. **Fuga de ruta/identidad del desarrollador** en la `.so` — doc 05.
6. **Cobro vía WebView con bridge `AndroidNative`** — doc 02, 04.

## Método

Estático: `apktool` (manifiesto + recursos), `aapt2` (badging/permisos), `strings` sobre
`.dex` y `.so`, **recuperación del código React original desde los source maps**
(`assets/www/static/js/*.map`), y **decompilación nativa función por función** de
`com.embsoft.vinden.*` con JADX (vía REA) — cliente HTTP, auth, login, pago (doc 09,
`decompiled/native/`). La app no se ejecutó.
