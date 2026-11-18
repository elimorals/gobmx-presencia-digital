# 07 — Hallazgos de seguridad (priorizados)

Contexto: app de validación de credencial del INE, sin cuenta ni backend. A diferencia de
otras apps del acervo, **no se hallaron vulnerabilidades**; los hallazgos son en su mayoría
**rasgos positivos de postura de seguridad**. Todo es observación **estática**.

## Positivos (postura de seguridad fuerte)

### P1 — Sin red: imposible exfiltrar lo escaneado (alto valor, confirmado por código)
No declara `INTERNET` y, **confirmado en el código decompilado** (no solo por el permiso): 0
`Socket`/`URL`/`HttpURLConnection`/OkHttp/SSL, 0 URLs http(s) propias, 0 `Intent ACTION_VIEW`;
el único `WebView` carga `file:///android_asset/legal.html` (asset local). Los datos de la
credencial (CIC, CURP, OCR, sección, vigencia) se procesan y muestran **100 % localmente** y no
pueden enviarse a ningún servidor. Nota: el SDK Scandit expone estados de validación de
licencia *online* (`NO_NETWORK_CONNECTION`, `LICENSE_VALIDATION_FAILED_400/403`), pero sin
`INTERNET` la app no puede ejercerlos → licenciamiento offline. Mejor rasgo de privacidad
posible para una app que lee datos personales. (doc 01, 02, 04)

### P2 — Firma auténtica del emisor
Firmado por `O=INSTITUTO NACIONAL ELECTORAL` y la firma verifica → binario íntegro del INE.
(doc 06)

### P3 — Manifiesto endurecido
`allowBackup=false`, `HIDE_OVERLAY_WINDOWS` (anti-tapjacking), actividades no exportadas salvo
el launcher, permiso de receiver a nivel `signature`, `useEmbeddedDex=true`. (doc 01)

### P4 — Sin telemetría/publicidad de terceros observada
No se hallaron SDKs de analítica/ads/crash en el inventario de paquetes; coherente con la
ausencia de `INTERNET`. (doc 05)

## Observaciones / límites (no son vulnerabilidades)

### O1 — Criptografía de validación no verificable (muro nativo)
Cómo valida el QR (si verifica firma del INE, si descifra) está en `libO0p.so`/`libl1I.so`
(motor Scandit/PersonalCode ofuscado). No se puede auditar desde el DEX. **No se afirma** que
sea correcta ni incorrecta: no es observable estáticamente. (doc 02)

### O2 — Licencia del SDK con fecha `20260604` ya pasada a la fecha del análisis
`assets/license.txt` lleva fecha `2026-06-04`, anterior al 2026-11-17 de este análisis. Si eso
implica caducidad y si el SDK bloquea el uso tras esa fecha **no se verificó** (lógica en el
.so nativo, sin red para renovar). Posible riesgo de **disponibilidad** (la app podría dejar
de validar) si el build de Play no se ha actualizado — **a confirmar**, no concluido. (doc 03)

### O3 — Dependencia de un proveedor comercial (Scandit/PersonalCode)
La función central de la app depende de un SDK de pago con licencia y cuota. Es una
observación de **sostenibilidad/operación**, no de seguridad: si la licencia o el contrato
vencen, la capacidad de validar depende del proveedor. (doc 03, 05)

## No aplica / no encontrado

- No hay almacenamiento de credenciales, tokens ni PII persistente (no hay login ni backend).
- No hay tráfico en claro (no hay tráfico).
- No hay pinning TLS porque no hay TLS ni red.
- No se halló `getUnsafeOkHttpClient` ni clientes HTTP (contrasta con Infonavit/CFE).

## Resumen

Es, de las apps analizadas del acervo, **una de las de mejor postura de seguridad y
privacidad**: sin red, firma auténtica, permisos mínimos, manifiesto endurecido y sin
telemetría. Los únicos puntos abiertos son el muro nativo del motor de escaneo (no auditable)
y la operación/caducidad de la licencia del SDK comercial.
