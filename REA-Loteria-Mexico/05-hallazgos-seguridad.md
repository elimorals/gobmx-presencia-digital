# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: app de **consulta de resultados** (sin pagos ni PII fuerte) → impacto centrado en
**integridad del dato mostrado** y **tracking**, no en credenciales.

## H1 (Medio) — Cleartext + resultados por HTTP desde un tercero
`usesCleartextTraffic="true"` y los resultados/sorteos se consultan por **HTTP** contra
**`nacionalloteria.com`** (dominio de **tercero**) y `lotenal.gob.mx` (doc 03). MITM puede
**alterar los resultados** mostrados (número ganador falso) y depende de un tercero para el dato
oficial. **Fix:** HTTPS en todos los endpoints; servir resultados desde dominio oficial
(`*.gob.mx`); quitar el cleartext.

## H2 (Bajo-Medio) — Chequeo de versión vía Dropbox público
`CheckNewVersionAvalaibleTask` lee `dropbox.com/s/iwydmo8…/version` (+ OneDrive) (doc 03):
mecanismo frágil y controlado por un archivo en almacenamiento personal de un tercero.
**Fix:** chequear versión contra un endpoint oficial; distribuir solo por Play.

## H3 (Bajo, latente) — Trust-all TLS presente pero inerte
`SSLHelper.enableSSLSocket()` es un trust-all global, pero **sin llamador** en el smali → código
muerto (doc 03). Riesgo latente (podría activarse), **no MITM confirmado**. **Fix:** eliminar la
clase; nunca usar trust-all.

## H4 (Bajo) — Tracking publicitario
`AD_ID` + `ACCESS_ADSERVICES_*` + `RefreshAdIntervalTask` (doc 01): recolección de identificadores
con fines de anuncios. Observación de privacidad, no vulnerabilidad.

## Positivos
- **Sin pagos / sin PII fuerte:** no hay compra, pasarela, login observado, ubicación, cámara ni
  contactos → superficie de datos mínima.
- **Firma original preservada** (GemesGroup), no re-firmada por APKPure (doc 04).
- **Trust-all no cableado:** la validación TLS efectiva de los endpoints HTTPS es la del sistema.

## Límites (sin inventar)
- El `grep` de llamadas cubre invocaciones estáticas en smali; **no** descarta una llamada
  reflectiva a `enableSSLSocket()` (improbable, pero no verificable estáticamente) → por eso H3 es
  "latente", no "descartado".
- No se confirmó si `CheckNewVersionAvalaibleTask` descarga un APK (solo que lee el "version" de
  Dropbox) → H2 se marca como superficie a revisar.
- No se recorrió la UI clase por clase (no aporta más allá de lo hallado).
- Las rutas internas de `nacionalloteria.com`/`lotenal` no se consultaron (estático, sin tocar
  endpoints).
