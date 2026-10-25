# Pagos y tarjetas

CFE Contigo es una app de **pago real**, no solo de consulta de recibos. La evidencia está en
los DTOs del paquete `data.*`.

## Tokenización de tarjetas — `data.CardDto`

Campos observados:

| Campo | Tipo | Nota |
|---|---|---|
| `id` | String | Identificador de la tarjeta guardada |
| `alias` | String | Nombre que le pone el usuario |
| `last4` | String | Últimos 4 dígitos (para mostrar) |
| `bin` | long | BIN (primeros dígitos, identifica emisor/marca) |
| `token` | String | **Token de la tarjeta** (sustituye al PAN) |
| `tipo` | String | Tipo (débito/crédito) |
| `cardType` | `CardType` | Marca (Visa/MC/…) |
| `permiteCargosRecurrentes` | boolean | Habilita cargos recurrentes |
| `permitePagoPuntos` | boolean | Habilita pago con puntos |
| `fechaCreacion`, `fechaActualizacion` | long | Auditoría |

**Buena práctica PCI**: la app guarda **`token` + `bin` + `last4`**, nunca el PAN completo ni
el CVV. Esto implica **tokenización del lado de una pasarela** (la app manda los datos de
tarjeta a la pasarela, que devuelve un token reusable). `CardDto` es `Parcelable` (se pasa
entre pantallas) pero el campo sensible ya es un token, no el número real.

DTOs relacionados: `CardBinDto` (consulta de BIN → marca/emisor), `CardsDto` (lista),
`PaymentSuccess` (resultado de pago), `Voucher` (comprobante).

## Cargos recurrentes y pago con puntos

- `permiteCargosRecurrentes`: domiciliación del pago de luz a la tarjeta.
- `permitePagoPuntos`: pago (total o parcial) con puntos de recompensa de la tarjeta.

## Flujo de pago inferido

```
Usuario elige recibo (Factura / FacturaServicio)
   └─> selecciona CardDto (token guardado) o captura nueva tarjeta
         └─> pasarela tokeniza (CardBinDto para identificar marca)
               └─> backend procesa cargo (Bearer del usuario)
                     └─> PaymentSuccess + Voucher (comprobante)
```

## Anti-fraude

El pago está cubierto por **LexisNexis ThreatMetrix** (doc 05): perfila el dispositivo y
puntúa el riesgo de la transacción antes de autorizarla. Coherente con el nivel de protección
DexGuard + packer nativo.

## Lo que NO se pudo confirmar

- **Qué pasarela** se usa (no hay SDK de Stripe/Conekta/OpenPay por nombre en claro; podría
  estar dentro del código ofuscado `o.*` o ser integración propia vía API). Los nombres de
  pasarela, de existir como string, están cifrados por DexGuard.
- El endpoint de pago (ver doc 04).
