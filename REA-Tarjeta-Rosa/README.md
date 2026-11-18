# Ingeniería inversa de Tarjeta Rosa 1.0.5 — índice

Análisis **estático** del APK `gob.guanajuato.TarjetaRosa` con REA (JADX headless 0.7.1).
La app **nunca se ejecutó**. Es la app oficial del programa social **Tarjeta Rosa** del
Gobierno del Estado de Guanajuato (apoyo a mujeres), **#15 por instalaciones** del
inventario gob.mx (≈1 M en Google Play).

> **Arquitectura: wrapper nativo de WebView.** La app es una cáscara Android (Kotlin/Java,
> 4 614 clases) que carga el portal web `https://app-tarjeta-rosa.guanajuato.gob.mx`. Toda
> la lógica del programa y la PII de las beneficiarias viven **del lado del servidor**, en
> el web app; el APK solo aporta el WebView, un puente JS, registro de dispositivo (FCM),
> captura de cámara/fotos y notificaciones. Postura **mejor que la mediana del acervo** en
> TLS: **no** desactiva la validación de certificado (contrasta con Infonavit y Licencia
> EDOMEX). Su superficie real es el **puente JavaScript**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura WebView, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, ausencia de cleartext |
| 02 | [02-arquitectura-webview.md](02-arquitectura-webview.md) | Cáscara nativa, `configureWebView`, settings |
| 03 | [03-puente-js-navegacion.md](03-puente-js-navegacion.md) | `WebAppInterface` (`openUrl`/`deviceRegister`), navegación sin restricción |
| 04 | [04-red-tls.md](04-red-tls.md) | URL base HTTPS, manejo SSL del WebViewClient, FCM |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma re-firmada por APKPure, procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y del APK base (versionado) |
| `decompiled/webview-js-bridge.java` | Cuerpos de `configureWebView` y `openUrl` (no versionado) |
| `evidence/firma.txt` | Salida de `apksigner --print-certs` (no versionado) |

> Lo pesado de Tarjeta Rosa (APK, decompilado, evidencia) **no se versiona** (`.gitignore`).

## 6 hallazgos principales

1. **(Medio) Puente JS abre intents arbitrarios.** `WebAppInterface.openUrl(String)` es
   `@JavascriptInterface` y hace `startActivity(Intent.ACTION_VIEW, Uri.parse(str))` **sin
   validar el scheme ni el host**. El puente se adjunta como `Android` y `AndroidInterface`
   a **todas** las páginas del WebView, y **no hay `shouldOverrideUrlLoading`** que restrinja
   la navegación al dominio oficial → si el WebView llega a renderizar contenido influido por
   un atacante (XSS/redirección abierta en el web app, o navegación fuera de dominio), ese JS
   puede disparar intents arbitrarios y tocar el registro de dispositivo (doc 03).
2. **(Bajo-Medio) Acceso a archivos del WebView habilitado.** `setAllowFileAccess(true)` +
   `setAllowContentAccess(true)` junto con el puente JS amplían la superficie (lectura de
   `file://`/`content://` desde contenido del WebView); el valor seguro por defecto en apps
   modernas es `false` (doc 02).
3. **(Bajo) Dos interfaces JS redundantes.** El mismo `WebAppInterface` se registra con dos
   nombres (`Android`, `AndroidInterface`); ambos exponen los mismos métodos potentes — doble
   superficie sin necesidad aparente (doc 03).
4. **Postura TLS correcta (positivo).** El `WebViewClient` **no** sobreescribe
   `onReceivedSslError` → usa el manejo por defecto, que **cancela** ante error de
   certificado. No hay omisión de validación TLS como en Infonavit/Licencia EDOMEX (doc 04).
5. **Sin cleartext y dominio oficial (positivo).** `BASE_URL = https://app-tarjeta-rosa.guanajuato.gob.mx`
   (HTTPS, dominio `.guanajuato.gob.mx`); el manifiesto **no** declara `usesCleartextTraffic`
   y no hay endpoints `http://` propios (doc 01, 04).
6. **PII mínima en el binario.** La PII de beneficiarias (CURP, domicilio, datos del programa)
   se maneja en el web app del servidor, no en el código nativo; el APK solo usa `CAMERA` +
   `READ_MEDIA_IMAGES` para captura de documentos/fotos que el web app sube. Lo demás no es
   observable estáticamente (doc 06, marcado como límite).

## Alcance y límites

Análisis **original** de una app pública del gobierno de Guanajuato que se posee, con fines de
investigación de seguridad. **No redistribuye** el APK ni el decompilado (`.gitignore`).
Observaciones **estáticas**: no se ejecutó la app ni se cargó el portal. **Firma**: el APK del
acervo está **re-firmado por APKPure** (`CN=Android, O=Google Inc.` + Source Stamp genérico),
así que **no es verificable como el artefacto auténtico publicado por el GEG**; las
conclusiones de arquitectura se sostienen, las de integridad no. El comportamiento real de la
PII vive en el portal web del lado del servidor, fuera del alcance de este análisis estático.
