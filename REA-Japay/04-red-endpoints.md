# Red y endpoints

Recuperados del bundle web (`assets/www/js/*.js`, HTML). El backend se arma como
`urlserv = https://japayaplicacion.yucatan.gob.mx/`.

## Backend JAPAY (`japayaplicacion.yucatan.gob.mx`, HTTPS)

| Endpoint | Rol |
|---|---|
| `/webservice/login` | autenticación |
| `/webservice/guarda_tarjeta` | guardar tarjeta (PAN) — doc 03 |
| `/webservice/procesa_pago_3dsecure` | procesar pago 3DS (referenciado) |
| `/webservice/recuperaUrlProdApp` | obtener URL de producción (config remota) |
| `/frmprocesapago/frmprocesapago.html` | form de pago (InAppBrowser) |
| `/politica-privacidad.php`, `/videoa.php` | contenido |

Todos **HTTPS**. Imágenes/logos desde el mismo host.

## Terceros

- **`https://www.egbs5.com.mx/egobierno/gyuc/principal/indexgen.jsp`** — **gateway de pago**
  (plataforma "egobierno") al que postea el form 3DS (doc 03). Es el procesador real de la tarjeta.
- **`https://dwmultimedia.com/japay/frmprocesapago/frmprocesapago.html`** — **host de pago
  anterior (proveedor), ahora comentado** (referencia muerta). Fuga menor del proveedor histórico.
- `maps.google.com` / `google.com/maps/search` — ubicación de centros.

## Artefactos de entorno (fuga, Bajo)

- **`https://dev-dev-japayaplicacion.yucatan.gob.mx/…`** — **subdominio de desarrollo** embebido en
  el bundle de producción (en `img/backlogin.png` y afines). Revela infraestructura interna.

## Transporte

- **HTTPS** en todos los endpoints propios y en el gateway; **sin** `usesCleartextTraffic` (doc 01);
  no se hallaron endpoints `http://` propios.
- **Pinning:** no hay (Cordova sobre el WebView del sistema; sin `networkSecurityConfig`). La
  confianza recae en las CAs del sistema.
