# 07 — WebView y puente JavaScript↔C#

La app es híbrida en la práctica: el núcleo nativo .NET MAUI delega **trámites, pagos y
liveness** a un WebView que carga el portal del ICVNL.

## `ICustomWebView` y handlers

- `ICV_APP.Render.ICustomWebView` (con `get_Source`/`set_Source`) + `ICV_APP.Droid.Handlers` →
  WebView personalizada para Android.
- Modelos de interceptación web en `Entitys`: `IWebResourceRequest`, `WebResourceResponse`,
  `ShouldInterceptRequest`, `OnPermissionRequest`, `OpenFileRequest`,
  `EvaluateJavaScriptAsyncRequest` → la app **intercepta peticiones**, concede permisos (cámara)
  al contenido web y **evalúa JavaScript** en la página.

## Puente JS↔C#

Literal inyectado en la página:

```js
javascript: function invokeCSharpAction(data){jsBridge.invokeAction(data);}
```

- `jsBridge`/`jsInterface` expone `invokeAction(data)` → el JavaScript de la página llama a
  código **C#** (p. ej. `resFaceliveness(...)` para devolver el resultado de liveness, doc 06).
- Funciones de trasiego de archivos desde el WebView: `getBase64FromBlobData`,
  `getBase64StringFromBlobUrl`, `convertBase64StringToPdfAndStoreIt` → la página web puede
  entregar blobs (p. ej. PDF de licencia/comprobante) que la app decodifica de Base64 y
  **guarda en almacenamiento** (doc 08).

## URLs cargadas

El contenido se carga desde `ISeguridad.UrlPortal` / `UrlTramite` (doc 03): **el servidor
decide la URL**. No es observable estáticamente qué dominios/paths devuelve ni si fuerza HTTPS.

## Por qué importa (superficie)

Un puente `invokeAction` que ejecuta acciones C# desde JavaScript es seguro **solo si** la
WebView carga exclusivamente contenido propio sobre HTTPS. Con `usesCleartextTraffic=true`
(doc 01) y las URLs provenientes del servidor, si alguna carga ocurriera sobre HTTP o admitiera
contenido mixto, un MITM podría inyectar JS que invoque el puente (acceso a cámara, a los
helpers de blob→archivo, al resultado de liveness). **No hay evidencia de que eso ocurra** —las
URLs no son observables— pero es la superficie a vigilar y la razón por la que
`usesCleartextTraffic=true` es un hallazgo aquí y no una nota trivial (H2).
