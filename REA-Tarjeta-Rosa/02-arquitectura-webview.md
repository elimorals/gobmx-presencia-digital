# Arquitectura — cáscara nativa de WebView

## Confirmación de pila

- `inspect_android_package`: 4 614 clases, 0 `.so` en el APK base, sin `assemblies/*.dll`
  (.NET), sin `libflutter.so`/`libapp.so` (Flutter), sin `il2cpp` (Unity), sin
  `index.android.bundle` (React Native), sin `assets/public|www` (Capacitor).
- El package propio tiene **12 clases** (`search_android_classes`); el resto es
  AndroidX + Firebase + Google Play Services. → **nativa Android (Kotlin/Java) actuando como
  wrapper de un WebView**, no una app híbrida empaquetada.
- `BuildConfig.BASE_URL = https://app-tarjeta-rosa.guanajuato.gob.mx` (extraído de `classes*.dex`).

## `WebViewConfigurator.configureWebView()` (cuerpo decompilado)

```java
private void configureWebView() {
    WebSettings settings = this.webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setMediaPlaybackRequiresUserGesture(false);
    settings.setAllowFileAccess(true);          // ⚠️ acceso a file://
    settings.setAllowContentAccess(true);       // ⚠️ acceso a content://
    settings.setCacheMode(-1);
    settings.setDomStorageEnabled(true);
    settings.setSupportZoom(false);
    ...
    settings.setLoadsImagesAutomatically(true);
    settings.setBlockNetworkImage(false);
    settings.setUserAgentString(settings.getUserAgentString() + " promoWebKit/1.0");
    this.webView.addJavascriptInterface(new WebAppInterface(...), "Android");
    this.webView.addJavascriptInterface(new WebAppInterface(...), "AndroidInterface");
    this.webView.clearCache(true);
    this.webView.setWebViewClient(createWebViewClient());
    this.webView.setWebChromeClient(createWebChromeClient());
    this.webView.setDownloadListener(createDownloadListener());
}
```

### Observaciones

- **JavaScript habilitado** y **DOM storage** — necesario para un web app; implica que la
  sesión del portal se guarda en el almacenamiento DOM del WebView (del lado del web app, no
  en código nativo).
- **`setAllowFileAccess(true)` + `setAllowContentAccess(true)`** — habilita que el contenido
  del WebView acceda a `file://` y `content://`. En apps modernas el valor seguro por defecto
  es `false`; aquí se activa sin uso visible que lo requiera → **superficie innecesaria**
  (hallazgo doc 06). En combinación con el puente JS, aumenta el impacto si el WebView
  renderiza contenido no confiable.
- **Doble `addJavascriptInterface`** con nombres `Android` y `AndroidInterface`, ambos
  apuntando al mismo `WebAppInterface` (doc 03).
- `WebChromeClient` + `getFilePathCallback`/`openFileChooser` → el WebView abre el selector de
  archivos para que la beneficiaria suba documentos/fotos (encaja con `READ_MEDIA_IMAGES`).
- `createDownloadListener` → descargas iniciadas por el web app (p. ej. comprobantes).

## Qué NO hace

- No carga HTML local empaquetado como producto (no hay `assets/www` con la app): el producto
  es remoto (HTTPS). Solo hay un `offlineError_Webview` para la pantalla de "sin conexión".
- No se observó `setMixedContentMode(...)`: con targetSdk 36 el valor por defecto es
  `MIXED_CONTENT_NEVER_ALLOW`, que bloquea sub-recursos HTTP dentro de una página HTTPS.
