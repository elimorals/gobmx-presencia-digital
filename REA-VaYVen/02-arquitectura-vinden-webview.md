# Arquitectura — shell nativo Vinden + WebView React de pago

## Dos capas

1. **Shell nativo (Kotlin/Java, 3 `.dex`).** Todo el producto: login, perfil, tarifas, rutas,
   rastreo de camiones en vivo (SignalR), monedero, notificaciones. Namespace
   `com.embsoft.vinden.*` (módulos observados en strings: `com.embsoft.vinden.module.home.*`,
   `com.embsoft.vinden.net`, `com.embsoft.vinden.common.net`).
2. **WebView React (CRA).** Un bundle estático en `assets/www` que **solo** renderiza la
   pantalla "Confirmación de pago" (`app/App.js`). Recuperado legible de los source maps en
   `decompiled/web-src/`.

## La WebView es un puente de cobro

`app/App.js` expone una función global que el shell nativo invoca para fijar el cobro:

```js
window.setAmountAndOptionPay = (optionPay, concept, amount, idOrder) => { ... }
// optionPay: 1=BankPaymentButton  2=PayPalButton  3=MercadoPagoButton
```

Y cada componente de pago **devuelve el resultado al código nativo** por un bridge
JavaScript↔nativo (y su equivalente iOS):

```js
// MercadoPagoButton.js
if (window.AndroidNative && typeof window.AndroidNative.responsePay === 'function') {
  window.AndroidNative.responsePay(JSON.stringify(response)); // response: true/false
}
if (window.webkit?.messageHandlers?.messageHandler) { ... } // iOS
```

Es decir, el shell nativo inyecta un objeto `AndroidNative` en la WebView
(`addJavascriptInterface`, referenciado en los `.dex`) con al menos `responsePay()` e
`initialized()`. **Implicación de seguridad:** un `@JavascriptInterface` es seguro solo si la
WebView carga contenido de confianza; el riesgo depende de si el checkout se sirve siempre por
HTTPS y de origen controlado (ver doc 03 — el checkout de producción está en la lista de
cleartext permitido, lo que degrada esa garantía).

## Flujo de pago (reconstruido del código web)

1. El shell nativo abre la WebView y llama `setAmountAndOptionPay(opción, concepto, monto, idOrder)`
   — el concepto por defecto en el código es `"Recarga monedero"` (recarga de saldo de la
   tarjeta de transporte).
2. Según `optionPay` se monta PayPal, MercadoPago (Wallet con `preferenceId = idOrder`) o el
   botón de banco.
3. Al aprobarse/rechazarse, el componente llama `AndroidNative.responsePay(true|false)`.

> `BankPaymentButton.js` está **sin implementar** en el bundle web (solo un `console.log`
> placeholder): el pago con tarjeta/banco se resuelve por otra vía (checkout del proveedor o
> flujo nativo), no en esta WebView. No infiero cuál sin evidencia.

## Qué NO está en la WebView

Login, GPS, rutas, QR, monedero: **no** aparecen en `assets/www`. Están en el shell nativo
`com.embsoft.vinden.*`. El **análisis profundo de esas funciones (decompilación función por
función) está en el doc 09** (red/auth/pago) con los cuerpos en `decompiled/native/`: mapa de
módulos (VIPER + Hilt + Retrofit/OkHttp), cliente HTTP **sin TLS pinning**, header de auth,
API key cifrada, login (PATCH), y flujo nativo de recarga.
