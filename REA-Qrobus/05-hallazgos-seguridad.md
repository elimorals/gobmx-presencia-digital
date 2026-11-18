# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.

## Observaciones

### O1 (Bajo-Medio) — Backend del transporte público en dominio de proveedor privado
La API corre en `admin.qrobici.com` (`qrobici.com`), no bajo `*.gob.mx`/`iqt` (doc 03). No es
una vulnerabilidad en sí (es HTTPS), pero implica que la infraestructura del servicio público
vive en un dominio de un tercero, con las implicaciones de gobernanza/pinning que eso conlleva.

### O2 (Bajo) — Ubicación en segundo plano
`ACCESS_BACKGROUND_LOCATION` + `FOREGROUND_SERVICE_LOCATION` (doc 01): la app puede rastrear
ubicación en background. Coherente con seguimiento de viaje, pero de alto impacto para
privacidad; el uso exacto está en el snapshot Dart (no verificable).

### P1 (positivo) — Pagos vía Stripe, sin secreto en el cliente
Los cobros se delegan a **Stripe**; no se halló una clave secreta (`sk_`) embebida (doc 03). La
publishable key es pública por diseño. Patrón correcto.

### P2 (positivo) — Transporte HTTPS
Backend y Stripe por HTTPS; sin cleartext ni endpoints `http://` propios (doc 03).

## Límites (el muro, sin inventar)
- **Lógica Dart (Flutter):** en `libapp.so`, no decompilable estáticamente (doc 02). Flujo de
  pago con Stripe (qué datos envía), auth, manejo de NFC/tarjeta y uso de la ubicación de fondo =
  **desconocidos** sin análisis dinámico (reflutter/blutter/Frida), fuera de alcance.
- **Pinning TLS:** no observable (capa de red en Dart).
- **`READ_CONTACTS`:** declarado, uso no verificable desde el manifiesto.
- **Integridad:** binario re-firmado por APKPure (doc 04) → conclusiones de arquitectura, no de
  integridad del artefacto original.
