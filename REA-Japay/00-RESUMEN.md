# Resumen — Japay 1.0.48 (`mx.gob.yucatan.japay.japaymovil`)

## Identidad
- **App:** Japay — JAPAY (Junta de Agua Potable y Alcantarillado de Yucatán). Consulta y **pago
  de recibos de agua**.
- **Package:** `mx.gob.yucatan.japay.japaymovil` · **versión** 1.0.48 (versionCode 10048). compileSdk 33.
- **#41** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
**Híbrida Cordova** (`assets/www/`, `cordova.js`, jQuery). Lógica en `js/japayMovil.js` (108 KB) +
`js/functions.js`. Plugins: camera, file, file-transfer, device. Backend
`https://japayaplicacion.yucatan.gob.mx` (HTTPS).

## Manejo de tarjetas (lo central)
- **Guardar tarjeta** (`guardatarjeta()` → `webservice/guarda_tarjeta`): envía **número, mes, año**
  en texto plano en el POST (sobre HTTPS), **sin cifrado de cliente**. Guarda el PAN en el
  servidor para reuso → preocupación **PCI**. El **CVV no se guarda**.
- **Pagar (3DS)**: abre en **InAppBrowser** `…/frmprocesapago.html` (host actual
  `japayaplicacion.yucatan.gob.mx`; host viejo comentado = `dwmultimedia.com`), que postea
  tarjeta+CVV al gateway **`egbs5.com.mx`** (egobierno). `idUser`/`nocontrato`/`importe` en la URL.
- **`js-rijndael`** está en el bundle pero **no se usa** (dead dep).

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS; sin cleartext |
| Tarjeta guardada | ⚠️ PAN al servidor en claro (sin cifrado de cliente) → PCI |
| Pago 3DS | ➖ en gateway tercero (egbs5) vía web form; CVV no persistido |
| Higiene de build | ⚠️ respaldos `*RESPALDO*.js` + subdominio `dev-dev` + `.php` + `dwmultimedia` muerto |
| Cripto de cliente | ⚠️ `js-rijndael` presente pero sin uso |
| Firma | ⚠️ Re-firmada por APKPure |

## Lo que NO se puede
Cómo almacena el servidor el PAN (`guarda_tarjeta`), la autorización del gateway y el detalle del
flujo 3DS del lado servidor → no observable desde el cliente. Se documenta lo que la app envía.
