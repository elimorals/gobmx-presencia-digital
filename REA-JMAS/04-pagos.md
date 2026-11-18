# Pagos — URL hosteada (sin datos de tarjeta en la app)

## Flujo
- `RemoteRepository.getPaymentURL(PaymentURLRequest)` pide al backend una **URL de pago**
  (`PaymentResponse`), que la app abre en `ui.main.ExternalLinkScreen` (navegador/WebView externo).
- Las pistas en el DEX apuntan a **BBVA** como procesador (pasarela bancaria hosteada).

## Implicación (positiva)
La app **no captura ni transmite datos de tarjeta** (PAN/CVV): delega todo el checkout a la
página hosteada del banco. Es el patrón correcto para PCI — el riesgo de tarjeta queda fuera de
la app, en la pasarela de BBVA.

## Límite
- El contenido de `PaymentURLRequest` (qué identifica el pago: contrato, monto) y la seguridad de
  la URL/sesión de pago viven en el backend y en BBVA → **no verificable estáticamente**. Si la
  URL de pago fuera predecible o reutilizable sería superficie a revisar, pero no se puede afirmar
  desde el binario. Se deja como nota de alcance, sin inventar.
