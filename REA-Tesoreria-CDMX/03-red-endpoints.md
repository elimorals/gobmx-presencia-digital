# Red y endpoints

Fuente: `grep` sobre `assets/public/*.js`. Lista completa en
[`decompiled/endpoints.txt`](decompiled/endpoints.txt) (27 URLs). Rutas presentes
en el binario; **no se llamó a ninguna**.

## Backend principal — Laravel (`finanzas.cdmx.gob.mx`)

Base configurada en el bundle (`apiUrl`, `apiUrlSiscor`, `apiUrlCurp`, `apiUrlBanorte`):

| Endpoint | Función |
|---|---|
| `https://tesoreriacdmx.finanzas.cdmx.gob.mx/tesoreriacdmx/public/api` | API de la app (login, cuenta) |
| `…/tesoreriacdmx/public/api/SignatureBanorte` | Firma de pago Banorte |
| `…/tesoreriacdmx/public/api/PagoBanorteConfirma` | Confirmación de pago Banorte |
| `…/siscorApis/public/api/appconfig` | Configuración remota de la app |
| `…/siscorApis/public/api/regcsfpreval` | CSF (constancia de situación fiscal) |
| `…/siscorServices/public/api/v1/adeudostenenciaplaca` | Adeudos de tenencia por placa |
| `…/srp/public/api/obtenerCamposPorLineaCaptura` | Campos por línea de captura |
| `https://sac.finanzas.cdmx.gob.mx/srp/public/api/cajas422/buscarLineaDeCaptura/` | Búsqueda de línea de captura |

El patrón `*/public/api/` y el OAuth2 Passport (doc 04) confirman **Laravel**.
Hay dos frentes: `tesoreriacdmx.finanzas.cdmx.gob.mx` y `sac.finanzas.cdmx.gob.mx`.

## Pasarelas de pago

| Host | Uso |
|---|---|
| `https://multicobros.banorte.com/orquestador/lightbox/checkoutV2.js` | **Banorte** (lightbox de cobro) |
| `https://sac.finanzas.cdmx.gob.mx/paybbva/paymentbbva.html` | **BBVA** (página de pago) |
| `https://data.finanzas.cdmx.gob.mx/pagos/pagoToServer.php` | Envío de pago (PHP) |
| `https://www.egbs5.com.mx/egobierno/operacion/respuesta.jsp` | Respuesta de pago (e-gobierno) |

## Endpoint de pre-producción embebido (hallazgo)

```
https://prepro.adquiracloud.mx/clb/endpoint/flapMexico
```

El subdominio **`prepro`** (pre-producción) de Adquira quedó en el bundle de
producción. Mismo patrón que IMSS Digital (entornos de prueba en el binario
publicado). No se determinó si está activo (análisis estático) (doc 08).

## Otros

- `https://tics.finanzas.cdmx.gob.mx/citas/public/citas` — sistema de citas.
- `http://procesos.finanzas.cdmx.gob.mx/OIP/index.php/Datos_Personales` — aviso de
  privacidad, servido por **`http://`** (enlace informativo, no API).
- `server.arcgisonline.com` — mapas base (ArcGIS), coherente con permisos de ubicación.
- Pusher (`pusher.com`, `js.pusher.com`) para realtime — ver doc 06.

> La política de red (doc 01) **permite cleartext** a `tesoreriacdmx…` y
> `sac.finanzas…`; aunque las URLs listadas usan `https://`, el permiso deja abierta
> la posibilidad de downgrade a esos hosts de pagos.
