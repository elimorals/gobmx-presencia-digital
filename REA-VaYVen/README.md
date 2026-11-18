# Ingeniería inversa de Va-y-Ven 4.3.0 — índice

Análisis **estático** del APK `gob.yucatan.vayven` (versionName 4.3.0, versionCode 199)
con REA + `apktool`/`aapt2` + lectura de los **source maps** del bundle React. La app
**nunca se ejecutó**. Es la app oficial del sistema de transporte público del estado de
Yucatán (**Va-y-Ven**, Agencia de Transporte de Yucatán / Gobierno de Yucatán), **#18 por
instalaciones** del inventario gob.mx (1 M+ en Google Play).

> Va-y-Ven es un **white-label de la plataforma "Vinden"** (paquete interno
> `com.embsoft.vinden`, clase de aplicación `com.embsoft.vinden.application.VindenApp`),
> desarrollada por **embsoft / MRP Technology** (`mrptechnology.mx`). La app es **nativa
> (Kotlin/Java)** con una **WebView React/Capacitor** acotada a la pantalla de confirmación
> de pago. El hallazgo central: el `network_security_config` **permite tráfico en claro
> (HTTP) sobre dominios de producción**, incluida la API, el checkout de pagos, el dominio
> oficial `appvayven.yucatan.gob.mx` y la pasarela bancaria `multicobros.banorte.com`.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila Vinden/MRP, módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, `usesCleartextTraffic=true` |
| 02 | [02-arquitectura-vinden-webview.md](02-arquitectura-vinden-webview.md) | Shell nativo Vinden + WebView React (pago), bridge `AndroidNative` |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | **Entornos prod/QA/dev en el binario**, SignalR bus-hub, cleartext |
| 04 | [04-pagos.md](04-pagos.md) | MercadoPago (**clave TEST en prod**), PayPal, Braintree, Banorte |
| 05 | [05-claves-nativas.md](05-claves-nativas.md) | `libapi-keys.so`: **clave Google en claro**, fuga de identidad del dev |
| 06 | [06-sdks-plugins.md](06-sdks-plugins.md) | Firebase (auth/FCM/RC), SignalR, greenDAO, Facebook, Google Ads |
| 07 | [07-firma-recursos.md](07-firma-recursos.md) | Firma **re-firmada por APKPure**, procedencia |
| 08 | [08-hallazgos-seguridad.md](08-hallazgos-seguridad.md) | **Hallazgos priorizados** |
| 09 | [09-codigo-nativo-auth-red.md](09-codigo-nativo-auth-red.md) | **Decompilación nativa**: cliente OkHttp (**sin pinning**), auth, API key cifrada, login |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `decompiled/endpoints.txt` | 47 URLs extraídas de los `.dex` |
| `decompiled/web-src/` | Código React original recuperado de los source maps (pantalla de pago) |
| `decompiled/native/` | Clases nativas `com.embsoft.vinden.*` decompiladas (JADX): cliente OkHttp, auth, login |
| `evidence/network_security_config.xml` | Config de red (lista de dominios con cleartext permitido) |
| `evidence/libapi-keys-strings.txt` | Strings de `libapi-keys.so` (clave Google + ruta del dev) |
| `AndroidManifest.xml` | Manifiesto decodificado |
| `hashes.sha256` | SHA-256 del APK |

> Lo propietario (APK, `.dex`, `.so`, `decompiled/`, `evidence/`) **no se versiona**
> (`.gitignore`).

## 7 hallazgos principales

1. **Sin TLS pinning (verificado en código).** `NetworkManagerImp.createOkHttpClient()` arma el
   `OkHttpClient` sin `certificatePinner`, sin `TrustManager`/`sslSocketFactory` custom ni
   `hostnameVerifier` (doc 09). Combinado con el cleartext permitido, amplía el margen de MITM.
2. **Tráfico en claro permitido en producción.** `android:usesCleartextTraffic="true"` y el
   `network_security_config` lista con `cleartextTrafficPermitted="true"` dominios de
   **producción**: `api-recaudo.transporteyucatan.org.mx` (API), `checkout-recaudo…` (checkout
   de pagos), el dominio oficial **`appvayven.yucatan.gob.mx`** y la pasarela bancaria
   **`multicobros.banorte.com`**. Habilita MITM sobre pagos y datos de usuario (doc 01, 03, 08).
3. **Clave de Google en claro dentro de una lib "oculta".** `libapi-keys.so` guarda una Google
   API key (`AIzaSy…` truncada; completa en `evidence/`, vía `NativeKeysHelper.getPlacesKey`),
   recuperable con un simple `strings`. Esconder la clave en código nativo no la protege
   (doc 05, 08).
4. **Credencial TEST de MercadoPago en el binario de producción.**
   `initMercadoPago('TEST-e1127083-…')` quedó embebida en el bundle React de pago (doc 04, 08).
5. **Entornos de desarrollo/QA en producción.** El binario lleva `api-dev…`, `api-qa…`,
   `api-prep…`, `tokenizacion-dev…`, IPs internas (`192.168.68.57`, `10.0.2.2`, varias
   públicas) y un **mock de Postman** (`*.mock.pstmn.io`) (doc 03, 08).
6. **Fuga de identidad del desarrollador.** `libapi-keys.so` contiene la ruta de build
   `/Users/eduardoezequielargaezmay/Documents/workspace/GitHub/TRANSPORTE_APP_ANDROID/…`
   (doc 05).
7. **Pago vía WebView con puente nativo.** La confirmación de pago corre en una WebView React
   que llama a `window.AndroidNative.responsePay(...)` para informar al shell nativo el
   resultado del cobro (doc 02, 04).

## Alcance y límites

Análisis **original** de una app pública del gobierno de Yucatán que se posee, con fines de
investigación de seguridad. **No redistribuye** el APK ni los binarios (`.gitignore`). Los
endpoints documentados son rutas presentes en el binario, no secretos ni datos de usuarios.
Observaciones **estáticas**: no se ejecutó la app ni se llamó a ningún endpoint. **Firma:**
el APK obtenido (APKPure) está **re-firmado con el certificado genérico `CN=Android,
O=Google Inc.`** (doc 07) → sirve para análisis de arquitectura, **no** como prueba de
integridad del binario publicado por la ATY.

---
