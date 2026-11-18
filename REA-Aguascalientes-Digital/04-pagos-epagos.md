# Pagos — ePagos (CitiBanamex) y wallet tokenizado

## Gateway: ePagos del GEA

Los pagos (refrendo, control vehicular, multas) van por **`epagos.aguascalientes.gob.mx`**, la
plataforma de pagos del Gobierno de Aguascalientes. Endpoints hallados en el DEX:

```
https://epagos.aguascalientes.gob.mx/WServicioPagos/SeleccionaFormaPago.aspx
https://epagos.aguascalientes.gob.mx/WServicioPagosWeb/SeleccionaFormaPago.aspx
https://epagos.aguascalientes.gob.mx/WServicioPagos/RespuestaCitiBanamex.aspx
https://epagos.aguascalientes.gob.mx/controlvehicular/
https://epagos.aguascalientes.gob.mx/sefirest/api/
```

- `SeleccionaFormaPago.aspx` → selección de forma de pago (flujo web; probable WebView/redirección
  al navegador).
- **`RespuestaCitiBanamex.aspx`** → el **procesador de tarjeta es CitiBanamex** (banco), vía la
  pasarela ePagos. La app no implementa el procesamiento de tarjeta: lo delega al gateway.

## Wallet tokenizado (positivo)

El módulo de red incluye un cliente **Wallet** y modelos **`WalletRequest`**,
`WalletRequestTarjetaCirculacion`, `WalletRequestTarjetaSoluciones`, **`WalletTokenResponse`** y
`TokenResponse`. Patrón de **tokenización**: la tarjeta/credencial se cambia por un **token**
server-side y la app maneja el token, no el PAN/CVV en claro. Es la práctica correcta para no
almacenar datos de tarjeta en el cliente.

## El matiz

La buena práctica (tokenización + gateway bancario) **queda socavada por el NSC** (doc 03): el
dominio `epagos.aguascalientes.gob.mx` está en la lista con **cleartext permitido**, así que el
tráfico hacia el gateway de pagos **podría viajar sin TLS**. Un MITM sobre ese canal vería
referencias de pago, importes y tokens en tránsito (no el PAN si la captura de tarjeta ocurre en
el lado web de CitiBanamex, pero sí el resto del contexto de pago).

> Límite: el flujo exacto de captura de tarjeta (si ocurre en WebView de CitiBanamex o en la app)
> y las reglas del servidor de ePagos no son verificables estáticamente. Se documenta lo
> observable (tokenización + gateway + endpoints), sin inventar el flujo completo.
