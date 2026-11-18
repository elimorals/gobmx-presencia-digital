# Pagos — InAppBrowser hacia Bancomer

## Flujo

El bundle JS referencia:
- `https://www.cespt.gob.mx/pago/pcm.aspx` (página ASP.NET de pago),
- el plugin **InAppBrowser** (Cordova) — `Browser.open`/`inAppBrowser`,
- la cadena **"bancomer"** (BBVA Bancomer, la pasarela bancaria).

Lectura: el cobro de recibos **abre `pcm.aspx` en un InAppBrowser**, y esa página redirige/embebe
la **pasarela de BBVA Bancomer**. La captura de la **tarjeta ocurre en la página del banco**,
dentro del navegador in-app, **no** en el JS de la app.

## Valoración (positivo)

- **No se halló** en el bundle: número de tarjeta/CVV manejados por el JS, ni claves de pasarela
  (Stripe `sk_`/`pk_`, MercadoPago `APP_USR`, Openpay, Conekta). La app **no toca los datos de la
  tarjeta** — los delega al banco. Es el patrón correcto para cumplimiento tipo PCI (el comercio
  no procesa la tarjeta en su propio código).
- El riesgo residual: como el InAppBrowser hereda la configuración de red de la app, y el NSC
  permite **cleartext global + CAs de usuario** (doc 04), un MITM con CA instalada podría
  interponerse en la navegación hacia `pcm.aspx`/Bancomer. El transporte del pago depende de que
  esas páginas sean HTTPS (lo son, `https://`), pero la postura del NSC debilita esa garantía.

## Límite (sin inventar)
El comportamiento exacto del InAppBrowser (si valida el origen, si bloquea navegación fuera de
`cespt.gob.mx`/Bancomer) vive en el JS troceado y no se trazó pantalla por pantalla; se confirmó
**qué** hace (abrir pcm.aspx/Bancomer en InAppBrowser) y que **no** maneja la tarjeta en claro,
no el detalle de cada callback.
