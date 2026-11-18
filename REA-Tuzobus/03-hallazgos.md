# 03 · Hallazgos — Tuzobús

## H1 — Endpoint de pruebas en producción (CWE-489) · MEDIA · CONFIRMADO

El build de producción embebe `https://apis-tests.hidalgo.gob.mx` (entorno `apis-tests`). Es HTTPS
(no cleartext), pero la infraestructura de **pruebas** no debería viajar en una app publicada: suele
tener menos endurecimiento, datos de prueba o CORS/credenciales laxas, y confunde el entorno. Mismo
patrón de "entorno de prueba en prod" visto en SAT-Móvil, VaYVen, IMSS, Internet para el Bienestar
(`.preprod`) y 911Móvil BC (`c4bcdev`).

**Remediación:** retirar los endpoints de pruebas del build de producción; separar configuraciones
por flavor/variant.

## H2 — Postura de red correcta · AUSENTE-SANO

`usesCleartextTraffic=false` + `allowBackup=false` + sin trust-all + firma auténtica de Hidalgo. La
app **no** hereda el defecto de 911-CDMX ni el de Infonavit/Cosmocolor. Va como contraejemplo limpio
(junto con Jalisco Alerta y Descarga Cultura UNAM).

## H3 — Flujo de pago/recarga y secretos · A-CONFIRMAR (muro Hermes)

La lógica de recarga/pago, la pasarela, el endpoint prod real, los secretos y la validación TLS
efectiva están en `index.android.bundle` (bytecode Hermes) → **no confirmables** estáticamente. No
se afirma que sean seguros ni inseguros: se marca pendiente de **dinámico**. (No inventar causa sin
evidencia.)

## Alcance y método (ética)

Análisis **estático** del APK público (XAPK de tienda, APK base extraído). No se ejecutó la app, no
se tocó `apis-tests.hidalgo.gob.mx` ni ningún backend, no se realizó ningún pago.
