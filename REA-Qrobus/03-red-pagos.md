# Red y pagos

Recuperado de strings del DEX (el detalle completo vive en el snapshot Dart, doc 02).

## Backend — dominio de proveedor privado

```
https://admin.qrobici.com/api/v1/travels/trip
```

- El backend de Qrobús corre en **`admin.qrobici.com`** — un **dominio comercial del proveedor**
  (`qrobici.com`), **no** bajo `*.gob.mx` ni `iqt`. HTTPS (bien), pero:
  - la infraestructura del transporte público de Querétaro queda en un dominio de un tercero,
  - expone el nombre del proveedor/plataforma (`qrobici`),
  - dificulta políticas de dominio/pinning atadas a `gob.mx`.
- La ruta `/api/v1/travels/trip` sugiere un API REST de viajes; las demás rutas no son
  observables (snapshot Dart).

## Pagos — Stripe

El DEX incluye el SDK de **Stripe** (`api.stripe.com`, `api.stripe.com/v1/`,
`api.stripe.com/edge-internal/`). El cobro de viajes/recargas se procesa con Stripe. La
*publishable key* de Stripe (que es **pública por diseño**, no un secreto) se configura en el
snapshot Dart; **no se recuperó una clave secreta** (lo correcto: una `sk_` en el cliente sería
el hallazgo, y no aparece).

## Transporte (postura)

- **HTTPS** en el backend y en Stripe; sin `usesCleartextTraffic`; sin endpoints `http://`
  propios en el DEX. Transporte sano.
- **Pinning:** no verificable estáticamente (capa de red en Dart). Marcado como límite.
