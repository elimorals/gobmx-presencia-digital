# Pagos

La app cobra **recargas de monedero** (saldo de la tarjeta de transporte). Integra varias
pasarelas; la confirmación corre en la WebView React (doc 02). Fuente: `decompiled/web-src/`
(código original de los source maps) y strings de `.dex`.

## Flujo nativo de recarga (decompilado)

El cobro lo orquesta el módulo nativo `module.balance_purchase` (VIPER:
`BalancePurchaseInteractor`/`Presenter`/`DataManager`, doc 09). El interactor expone callbacks
`onListPurchases`, `onMap(String)`, `onCode`, `onError` y usa el gateway `network.gateway.balances`
(`DetailPurchaseResponseDTO`, `SearchMovementsRequestDTO`). Secuencia reconstruida del código:

1. El usuario elige monto/paquete en `BalancePurchaseActivity` sobre una tarjeta del monedero
   (`ListCard`, `CardPurseData`).
2. El módulo pide al backend crear la orden/preferencia de pago (gateway `balances`), con el
   token de usuario en `Authorization` (doc 09).
3. El servidor devuelve los datos de pago (preferencia/URL de checkout); la app abre la
   **WebView React** (`assets/www`) con ese `idOrder`/URL.
4. La WebView ejecuta la pasarela (MercadoPago/PayPal) y reporta el resultado al código nativo
   por `window.AndroidNative.responsePay(true|false)` (doc 02).

Es decir: la **lógica de pago vive en el servidor + la WebView**; el módulo nativo arma la
orden y abre la WebView. El binario no construye directamente la petición HTTP a
`multicobros.banorte.com` ni firma cobros en el cliente — Banorte es el procesador del checkout
server-side (`checkout-recaudo…`), por eso aparece en la lista de cleartext (doc 03) pero no en
código de petición propia. No afirmo el detalle del lado servidor (fuera de alcance estático).

## Pasarelas integradas

| Pasarela | Evidencia | Capa |
|---|---|---|
| **MercadoPago** | `@mercadopago/sdk-react` (`CardPayment`, `Wallet`, `Payment`), `sdk.mercadopago.com/js/v2` | WebView |
| **PayPal** | `@paypal/react-paypal-js` (`PayPalScriptProvider`, `PayPalButtons`), `paypal.com/sdk/js` | WebView |
| **Braintree** | `js.braintreegateway.com/web/` | WebView |
| **Banorte (multicobros)** | `multicobros.banorte.com` en `network_security_config` | nativo/checkout |
| Checkout propio (proveedor) | `checkout-recaudo.transporteyucatan.org.mx/transporte_checkout/checkout` | web/servidor |
| Tokenización | `tokenizacion-{dev,qa}.transporte.mrptechnology.mx` | servidor |

## Hallazgo: clave TEST de MercadoPago en producción

En `decompiled/web-src/MercadoPagoButton.js` (recuperado de los source maps):

```js
initMercadoPago('TEST-e1127083-5f41-43d3-b68b-d7a86eed3ae5');
```

Es una **public key de entorno de PRUEBA** (`TEST-…`) embebida en el bundle del APK de
producción. Consecuencias posibles, sin afirmar cuál aplica en runtime (la app no se ejecutó):
- Si en runtime se sobreescribe con la key de producción (p. ej. vía `idOrder`/preferencia
  creada server-side), la constante quedó como residuo.
- Si no se sobreescribe, los pagos MercadoPago irían contra el sandbox de pruebas.

No determino cuál ocurre sin ejecución; se documenta el hecho (constante TEST en prod) con su
evidencia.

## Flujo (MercadoPago Wallet)

1. El shell nativo pasa `idOrder` (= `preferenceId`) a la WebView.
2. Se monta `<Wallet initialization={{preferenceId}}>`; la preferencia se crea en el servidor
   (no en el cliente) → el cliente no maneja montos ni credenciales secretas en el Wallet.
3. Al volver del pago, MercadoPago redirige con parámetros (`collection_status`, `payment_id`,
   etc.); el componente lee `window.location.search`, arma un `data` y llama
   `AndroidNative.responsePay(true|false)` según `collection_status == "approved"`.

**Nota de robustez (no es vuln confirmada):** el veredicto de éxito del pago se decide en el
**cliente** a partir del `collection_status` de la URL de retorno. Lo correcto es que el
backend confirme el pago contra MercadoPago (webhook/consulta server-side) antes de acreditar
saldo. No hay evidencia en el binario de qué hace el servidor; el cliente **no debería** ser la
autoridad del cobro. Queda como punto a verificar del lado servidor, no como hallazgo probado.

## PayPal

`PayPalButton.js` usa `@paypal/react-paypal-js` con un `// TODO: verificar si funciona de esta
manera` en el código original — señal de integración en progreso al momento del build.
