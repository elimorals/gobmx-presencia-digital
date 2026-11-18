# Puente JavaScript y navegación

## `WebAppInterface` — lo que el web app puede pedirle al nativo

`inspect_android_class` lista 3 métodos `@JavascriptInterface` (más lambdas sintéticas):

| Método | Firma | Qué hace |
|---|---|---|
| `openUrl` | `openUrl(String)` | abre un intent `ACTION_VIEW` con la URI recibida |
| `deviceRegister` | `deviceRegister(String)` | registra el token (FCM) vía `DeviceRegister` |
| `checkConnectionAndReload` | `checkConnectionAndReload()` | recarga el WebView si hay red |

Campos: `context`, `webView`, `originalUrl`. El puente se adjunta con **dos** nombres
(`Android` y `AndroidInterface`) en `configureWebView` (doc 02).

## `openUrl` — cuerpo decompilado

```java
@JavascriptInterface
public void openUrl(String str) {
    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
    intent.addFlags(268435456); // FLAG_ACTIVITY_NEW_TASK
    this.context.startActivity(intent);
    Log.d("WebViewLink", "Link opened in browser ");
}
```

**Riesgo (hallazgo, Medio):** `str` llega directo a `Uri.parse` + `startActivity` **sin
validar scheme ni host**. Cualquier JavaScript que se ejecute en el WebView puede invocar
`Android.openUrl("<uri>")` y lograr que la app dispare un intent arbitrario:

- abrir cualquier `https://`/`http://` en el navegador (phishing fuera de la app),
- lanzar schemes sensibles (`tel:`, `sms:`, deep links a otras apps, `intent:`),
- en combinación con `setAllowFileAccess(true)` (doc 02), ampliar el impacto si el contenido
  es malicioso.

El JS que puede llamarlo es, en condiciones normales, solo el del portal oficial confiable. El
riesgo se materializa si ese contenido deja de ser confiable: un **XSS o redirección abierta**
en el web app, o una **navegación fuera del dominio** (ver abajo).

## Navegación no restringida

El `WebViewClient` (`WebViewConfigurator.AnonymousClass1`) implementa:
`onPageFinished`, `onReceivedError`, `onReceivedHttpError` — y **no** implementa
`shouldOverrideUrlLoading`. Consecuencia:

- la navegación **no está acotada al dominio** `app-tarjeta-rosa.guanajuato.gob.mx`: si el
  portal enlaza (o es redirigido) a un dominio externo, el WebView lo carga **en la misma
  instancia**, que **sigue teniendo el puente JS adjunto** (los interfaces se registran una
  vez sobre el WebView, aplican a toda página que cargue).
- por tanto una página externa cargada en ese WebView también podría llamar
  `openUrl`/`deviceRegister`.

**Mitigante parcial:** no se omite la validación TLS (doc 04) y la URL base es HTTPS, así que
un MITM pasivo no basta para inyectar JS; haría falta un fallo del lado del web app
(XSS/redirección) o navegación deliberada fuera de dominio.

## `deviceRegister`

Registra el token de notificaciones (FCM) a través de `DeviceRegister`/`NetworkHelper` contra
el backend. Expuesto al JS, permite que el web app asocie el dispositivo a la cuenta. No
observé que exponga PII adicional desde el lado nativo; el detalle del request vive en
`DeviceRegister`/`NetworkHelper` y en el servidor.

## Recomendaciones (defensivas)

- Validar en `openUrl` el scheme (`https` solamente) y, si aplica, el host.
- Registrar el puente con **un** solo nombre y, idealmente, solo tras confirmar que la página
  es del dominio oficial.
- Implementar `shouldOverrideUrlLoading` para mantener la navegación dentro de
  `*.guanajuato.gob.mx` (abrir lo externo con un `CustomTabs`/navegador, sin puente).
- Poner `setAllowFileAccess(false)` salvo necesidad real.
