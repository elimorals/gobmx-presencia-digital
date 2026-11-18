# Pagos — Banorte, BBVA y credenciales embebidas

Fuente: `assets/public/main.*.js` y endpoints (doc 03).

## Hallazgo central: credenciales de comercio Banorte en el bundle

En `main.*.js`, la función que arma la "cadena original" del pago Banorte incluye
las credenciales del comercio **en texto plano dentro del JS distribuido**:

```js
getOriginalChain(T){
  return `{"merchantId":<REDACTADO>,"name":"<REDACTADO>","password":"<REDACTADO>", ... }`
}
```

- `merchantId`, `name` y `password` del comercio están **hardcodeados** en el
  cliente. Los valores completos quedan en `evidence/` (no se reproducen aquí por
  divulgación responsable).
- Esta cadena alimenta el endpoint `…/api/SignatureBanorte`, que presumiblemente
  firma la operación del lado servidor; pero las credenciales del comercio **no
  deberían viajar ni residir en el cliente**: quien abra el APK las obtiene.
- Es el hallazgo más serio del análisis (doc 08).

## Pasarelas

| Pasarela | Mecanismo |
|---|---|
| **Banorte** | `multicobros.banorte.com/orquestador/lightbox/checkoutV2.js` (lightbox) + `…/api/SignatureBanorte` y `…/api/PagoBanorteConfirma` en el backend |
| **BBVA** | `sac.finanzas.cdmx.gob.mx/paybbva/paymentbbva.html` (página de pago, abierta en in-app browser) |
| e-gobierno | `data.finanzas.cdmx.gob.mx/pagos/pagoToServer.php`, `egbs5.com.mx/.../respuesta.jsp` |
| **Adquira (pre-pro)** | `prepro.adquiracloud.mx/clb/endpoint/flapMexico` — **entorno de pre-producción** embebido (doc 03) |

El flujo de pago abre la pasarela en el **in-app browser** (plugin
`@capacitor/inappbrowser`), patrón habitual para checkout de tarjeta.

## Qué se paga

Por los endpoints (adeudos de tenencia por placa, líneas de captura, CSF) y la
dependencia (SAF/Tesorería CDMX): **predial, agua, tenencia/control vehicular** y
pagos por línea de captura.
