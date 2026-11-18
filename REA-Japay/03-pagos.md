# Pagos — guardado de tarjeta y 3D Secure

Dos flujos distintos tocan datos de tarjeta. Código citado en `decompiled/pago-flujo.js`.

## 1. Guardar tarjeta (`guardatarjeta()` → `webservice/guarda_tarjeta`)

La pantalla de configuración permite **guardar una tarjeta** para reuso. `japayMovil.js`:

```js
$.ajax({
  url: urlserv + 'webservice/guarda_tarjeta', type: 'post',
  data: { numero: tnumero, nombre: tnombre, mes: tmes, ano: tano,
          apodo: tapodo, tipo_tarjeta: ttipo }
});
```

- **Envía el número de tarjeta (`numero`) + mes + año en texto plano** en el cuerpo del POST, **sin
  cifrado de cliente**. Va sobre **HTTPS** (transporte protegido), pero:
  - la app **guarda el PAN en el servidor** para reuso → preocupación **PCI-DSS** (almacenar
    números de tarjeta exige controles estrictos; idealmente se tokeniza, no se guarda el PAN).
  - **no** usa la librería `js-rijndael` que trae el bundle (doc 02) → la intención de cifrar del
    lado cliente **no se implementó**; es una **dependencia muerta**.
- **El CVV no se envía** en `guarda_tarjeta` (correcto: el CVV no debe almacenarse).

## 2. Pago 3D Secure (InAppBrowser → gateway tercero)

El pago abre un form remoto en **InAppBrowser** (`functions.js`):

```js
cordova.InAppBrowser.open(
  'https://japayaplicacion.yucatan.gob.mx/frmprocesapago/frmprocesapago.html'
  + '?nocontrato=' + nocontrato + '&importe=' + importe + '&idUser=' + idUser);
// línea comentada (host viejo de proveedor): 'https://dwmultimedia.com/japay/frmprocesapago/...'
```

Y `frmprocesapago.html` **postea al gateway de un tercero**:

```html
<form action="https://www.egbs5.com.mx/egobierno/gyuc/principal/indexgen.jsp"> … </form>
```

- La tarjeta + **CVV** (`name="cvv"`) se capturan en el form y van al **procesador `egbs5.com.mx`**
  (plataforma "egobierno"), con el flujo **3D Secure** (`procesa_pago_3dsecure`, `3dsecure.html`).
  Es un patrón **estándar** de redirección a pasarela (el CVV no toca el servidor de la JAPAY).
- **Observación:** `nocontrato`, `importe` e **`idUser` viajan en la URL** (query string) → pueden
  quedar en logs/historial. Impacto bajo (no es dato de tarjeta), pero es mejor pasarlos por POST.
- El host de pago migró del proveedor `dwmultimedia.com` (comentado) al dominio oficial
  `japayaplicacion.yucatan.gob.mx` — buena señal; la referencia vieja quedó como código muerto.

## Resumen

| Flujo | Dato | Destino | Cifrado |
|---|---|---|---|
| Guardar tarjeta | PAN + exp (sin CVV) | `webservice/guarda_tarjeta` (JAPAY) | solo TLS; sin cifrado de cliente |
| Pagar 3DS | PAN + CVV | gateway `egbs5.com.mx` vía InAppBrowser | TLS + 3DS (redirección) |
