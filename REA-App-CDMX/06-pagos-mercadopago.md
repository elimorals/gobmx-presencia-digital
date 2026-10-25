# 06 — Pagos: Mercado Pago, recarga y SafPayments

App CDMX cobra de verdad: recarga de la tarjeta de transporte "Mi", trámites y servicios.
La pasarela es **Mercado Pago**, con endpoints propios de la CDMX como orquestadores.

## Evidencia de endpoints

```
https://api.mercadopago.com/semovi/appcdmx/v1/transactions/confirm
https://www.app.cdmx.gob.mx/MP/Payment
https://www.app.cdmx.gob.mx/MP/Challenge
https://apiapp.cdmx.gob.mx/index.php/v4/MP/
https://apiapp.cdmx.gob.mx/index.php/v4/SafPayments/
https://apiapp.cdmx.gob.mx/index.php/v4/MIVC/
```

Módulos implicados: `.pagos` (13 componentes), `.recargami`, `.tarjetavirtualmi`.

## Flujo inferido

1. El usuario inicia un pago (recarga tarjeta Mi, trámite) desde `.pagos` / `.recargami`.
2. La app llama a `/v4/MP/` o `/v4/SafPayments/` (orquestador CDMX) que prepara la
   transacción con Mercado Pago.
3. **Mercado Pago** procesa; `/MP/Challenge` sugiere soporte de **3-D Secure / challenge**
   (autenticación reforzada del emisor de la tarjeta).
4. La confirmación llega a `api.mercadopago.com/semovi/appcdmx/v1/transactions/confirm`
   (el path `semovi/appcdmx` indica una integración dedicada SEMOVI ↔ App CDMX).
5. En recarga, el saldo se refleja en la tarjeta virtual "Mi" (`MIVC/` = Mi tarjeta Virtual
   CDMX) y, por NFC, en la tarjeta Calypso (doc 05).

## Confirmación: ningún DTO maneja datos de tarjeta (decompilado)

Se decompiló el módulo `.pagos` completo (33 DTOs + Activities; ver
`decompiled/pagos-dtos.md`). **Resuelto: App CDMX no almacena ni procesa datos de tarjeta
de forma nativa.**

- `FormPasarela` (lo que la app envía a la pasarela) tiene 11 campos, **todos de referencia
  de cobro**: `account`, `captureLine` (línea de captura), `company`, `invoice`,
  `controlInvoice`, `usage`, `overdueDebts`, `lat/lng`. **Ninguno de tarjeta.**
- El flujo es **por URL**: `getPaymentUrl` → `CheckoutBrickFragment` (Mercado Pago *Bricks*,
  checkout alojado) abierto en WebView. La captura y tokenización de la tarjeta ocurren
  **dentro del entorno web de Mercado Pago**.
- Grep sobre los DEX: **cero** campos `cardnumber/numeroTarjeta/cvv/cardholder/PAN/cardToken`;
  **sin** SDK nativo de Mercado Pago (`Lcom/mercadopago…` = 0). Los `expir*` son de
  token/documento/sesión, no de vencimiento de tarjeta.

Patrón correcto (PCI-friendly por delegación). El único eslabón no auditable por estática es
el orquestador servidor `apiapp.cdmx.gob.mx/.../MP/` y la custodia del token de MP, fuera del
binario.
- **3-D Secure presente** (`/MP/Challenge`): señal positiva de autenticación reforzada.
- El orquestador `apiapp.cdmx.gob.mx/.../MP/` es el eslabón CDMX entre app y Mercado Pago;
  su seguridad (y la custodia del access token de MP del lado servidor) queda fuera del
  binario y no es auditable por estática.
