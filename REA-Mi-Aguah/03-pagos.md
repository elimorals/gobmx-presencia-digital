# Pagos

## Backend de pago
- **`https://ssl1.agua.gob.mx/pagatuagua/documentos/`** (recuperado de strings del snapshot
  Dart). HTTPS, dominio `agua.gob.mx` — plataforma **"Paga tu agua"**. Es el endpoint de
  documentos/recibos; las rutas de pago concretas viven en el snapshot (muro, doc 02).

## Manejo de tarjeta
- El snapshot Dart contiene decenas de referencias a `card`/`tarjeta`/`Card`/`CVV` → la app
  presenta un **formulario de tarjeta in-app** (captura de número + CVV en la propia app, no solo
  un redirect a una pasarela externa).
- **Pregunta abierta (no se afirma):** no se identificó estáticamente la pasarela (no aparecen
  SDK ni claves `pk_`/`sk_` de Stripe/Conekta/Openpay/MercadoPago en el DEX ni en strings de
  `libapp.so`), así que **si la tarjeta se tokeniza en el cliente o viaja al backend, y por qué
  pasarela, no es verificable estáticamente** — vive en la lógica Dart. Requeriría análisis
  dinámico. Se reporta como superficie a revisar, no como hallazgo confirmado.

## Transporte
El backend de pago es HTTPS; sin `usesCleartextTraffic`. El único `http://` hallado es el link
público `http://www.aguadehermosillo.gob.mx/` (sitio institucional, no un endpoint de pago).
