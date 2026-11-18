# Red, TLS y notificaciones

## URL base y dominio

- `BuildConfig.BASE_URL = https://app-tarjeta-rosa.guanajuato.gob.mx` (extraído de
  `classes*.dex`). **HTTPS** y dominio oficial **`.guanajuato.gob.mx`**.
- No se hallaron endpoints `http://` propios en el DEX (el único `http://` presente es
  `http://crbug.com/...`, una referencia interna de Chromium, no un endpoint de la app).
- Clases de red propias: `NetworkHelper`, `DeviceRegister` (registro del token FCM contra el
  backend). El grueso del tráfico es el del portal dentro del WebView.

## Manejo de TLS en el WebView (lo importante)

El `WebViewClient` (`WebViewConfigurator.AnonymousClass1`) expone **solo**:

```
onPageFinished(WebView, String)
onReceivedError(WebView, WebResourceRequest, WebResourceError)
onReceivedHttpError(WebView, WebResourceRequest, WebResourceResponse)
```

**No implementa `onReceivedSslError`.** Consecuencia (con evidencia, no por ausencia de
strings): ante un error de certificado TLS, el WebView aplica el **comportamiento por
defecto**, que es **cancelar** la carga (`handler.cancel()`). Es decir, **la validación de
certificado NO está desactivada**.

Esto distingue a Tarjeta Rosa de dos apps críticas del acervo:
- **Infonavit** (`getUnsafeOkHttpClient`, sin validación) y
- **Licencia EDOMEX** (`checkServerTrusted` vacío + `hostnameVerifier=true`).

Aquí **no** hay ese patrón: un atacante en la red **no** puede interponerse presentando un
certificado inválido, porque el WebView rechaza la conexión.

> Matiz honesto: "no desactivar la validación" no es lo mismo que "certificate pinning". No se
> observó pinning (ni por `networkSecurityConfig` ni en código), así que la confianza recae en
> las CAs del sistema — el estándar correcto, aunque sin la capa extra del pinning.

## Cleartext

El manifiesto **no** declara `usesCleartextTraffic` (doc 01) → por defecto, HTTP en claro
bloqueado en targetSdk 36. Combinado con la URL base HTTPS, el transporte está sano.

## Notificaciones (FCM)

Firebase Cloud Messaging: `FirebaseMessagingService` propio + los componentes estándar de
Firebase en el manifiesto. `deviceRegister` (puente JS, doc 03) asocia el token al backend.
`NotificationHelper` arma las notificaciones; una de ellas abre `OPEN_NOTIFICATION_SCREEN` en
`MainActivity`.
