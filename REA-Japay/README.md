# Ingeniería inversa de Japay 1.0.48 — índice

Análisis **estático** del APK `mx.gob.yucatan.japay.japaymovil` con REA (inspección de archivo
+ bundle web). La app **nunca se ejecutó**. Es la app oficial de la **JAPAY** (Junta de Agua
Potable y Alcantarillado de Yucatán): consulta y **pago de recibos de agua**. **#41 por
instalaciones** del inventario gob.mx (≈100 K).

> **Híbrida Cordova (JS legible).** Toda la lógica vive en `assets/www/` (Cordova + jQuery). El
> backend es `https://japayaplicacion.yucatan.gob.mx` (HTTPS). El hallazgo central es el manejo
> de **tarjetas**: la app **guarda el número de tarjeta en el servidor** (`webservice/guarda_tarjeta`)
> enviándolo **en texto plano** en el cuerpo del POST (sin cifrado de cliente, pese a traer la
> librería `js-rijndael` **sin usar**); el pago 3D Secure se hace en un **form de proveedor**
> (`egbs5.com.mx`) abierto en InAppBrowser. Además el bundle arrastra **artefactos de desarrollo**
> (subdominio `dev-dev`, varios `*RESPALDO*.js`, `.php`, referencia muerta a `dwmultimedia.com`).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Cordova, pagos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, cleartext |
| 02 | [02-arquitectura-hibrida.md](02-arquitectura-hibrida.md) | Cordova, bundle `www`, respaldos en prod |
| 03 | [03-pagos.md](03-pagos.md) | Guardado de tarjeta (PAN en claro), 3DS, gateway tercero |
| 04 | [04-red-endpoints.md](04-red-endpoints.md) | `japayaplicacion.yucatan.gob.mx`, dev, `egbs5`/`dwmultimedia` |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/` (firma, perms),
`decompiled/pago-flujo.js` (snippet del flujo de pago) — estos dos no versionados.

## 5 hallazgos principales

1. **(Medio) Guarda el número de tarjeta en el servidor, enviado en texto plano.** `guardatarjeta()`
   hace POST a `webservice/guarda_tarjeta` con `{numero, mes, ano, …}` **sin cifrar** (sobre
   HTTPS). Guardar PAN para reuso es una preocupación **PCI-DSS**; la lib `js-rijndael` está en el
   bundle pero **no se usa** en este flujo (doc 03). El CVV **no** se guarda (correcto).
2. **(Bajo-Medio) Pago 3DS en gateway de un tercero vía web form.** El pago abre en InAppBrowser
   `…/frmprocesapago.html` que postea tarjeta+CVV a **`https://www.egbs5.com.mx/egobierno/…/indexgen.jsp`**
   (procesador egobierno). Estándar para 3DS redirect, pero el `idUser`/`nocontrato`/`importe`
   viajan en la **URL** (query) (doc 03).
3. **(Bajo) Artefactos de desarrollo en producción.** Bundle con subdominio `dev-dev-japayaplicacion.yucatan.gob.mx`,
   varios respaldos (`japayMovilRESPALDP.js`, `functionsRESPALDO.js`, `functionsrespaldo2.js`,
   `japayMovil2/3.js`, `function2.js`), `autoload.php` y referencia muerta al proveedor
   `dwmultimedia.com` → fuga de infraestructura y superficie extra (doc 02).
4. **(Bajo) `js-rijndael` empaquetada pero sin uso** — dependencia muerta; sugiere una intención
   de cifrado de cliente que no se implementó (doc 03).
5. **Transporte HTTPS (positivo).** Backend `japayaplicacion.yucatan.gob.mx` por HTTPS; **sin**
   `usesCleartextTraffic`; permisos acotados (doc 01, 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app, no se hizo ningún
pago ni se llamó a ningún endpoint. El manejo real del PAN del lado del servidor
(`guarda_tarjeta`) no es observable desde el cliente — se documenta qué **envía** la app, no cómo
lo almacena el servidor. **Firma** re-firmada por APKPure → no verificable como auténtica de la
JAPAY.
