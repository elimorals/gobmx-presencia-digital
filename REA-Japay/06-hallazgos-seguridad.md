# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se hizo ningún pago ni se llamó
a ningún endpoint.

## H1 (Medio) — Guarda el PAN en el servidor, enviado sin cifrado de cliente
`guardatarjeta()` POSTea `{numero, mes, ano, …}` a `webservice/guarda_tarjeta` en texto plano
(sobre HTTPS), para **guardar la tarjeta** y reusarla (doc 03). Almacenar el PAN es una
preocupación **PCI-DSS**; además la lib `js-rijndael` del bundle **no se usa** → el cifrado de
cliente que parecía previsto no existe. **Mitigante:** el CVV **no** se guarda; el transporte es
HTTPS. **Fix:** tokenizar (no almacenar PAN), o cifrar/segregar según PCI.

## H2 (Bajo-Medio) — Pago 3DS en gateway de un tercero; parámetros en la URL
El pago abre un form en InAppBrowser que postea tarjeta+CVV al procesador **`egbs5.com.mx`**
(egobierno) con flujo 3D Secure (doc 03). Patrón estándar de redirección (el CVV no toca el
servidor de la JAPAY), pero `idUser`/`nocontrato`/`importe` viajan en la **URL** (query) → pueden
quedar en logs/historial. **Fix:** pasar los parámetros por POST.

## H3 (Bajo) — Artefactos de desarrollo en producción
Bundle con `dev-dev-japayaplicacion.yucatan.gob.mx`, varios `*RESPALDO*.js` / `japayMovil2-3.js` /
`function2.js`, `autoload.php`/`videoa.php`, y la referencia muerta al proveedor `dwmultimedia.com`
(doc 02, 04). Fuga de infraestructura/lógica interna y superficie extra. **Fix:** empaquetar solo
los assets activos y minificados.

## H4 (Bajo) — `js-rijndael` empaquetada sin uso
Dependencia muerta (lib + docker + tests) en el bundle (doc 02). Ruido; sugiere una intención de
cifrado de cliente no implementada. **Fix:** eliminarla.

## Positivos
- **HTTPS** en todos los endpoints propios y en el gateway; **sin** cleartext (doc 01, 04).
- **CVV no persistido** (solo en el pago en vivo, hacia el gateway).
- **Permisos acotados** (red, ubicación, cámara, almacenamiento; sin contactos/SMS/teléfono).

## Límites (sin inventar)
- **Almacenamiento del PAN del lado servidor** (`guarda_tarjeta`): no observable desde el cliente;
  se documenta qué **envía** la app, no cómo lo guarda el servidor.
- **Flujo 3DS del lado servidor / autorización del gateway `egbs5`:** fuera del alcance estático.
- **Integridad:** binario re-firmado por APKPure → conclusiones de arquitectura, no de integridad.
