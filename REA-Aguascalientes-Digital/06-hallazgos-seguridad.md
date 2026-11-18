# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Firma **auténtica del GEA** → los defectos están en el código oficial.

## H1 (Medio-Alto) — Cleartext HTTP a producción, incluido el gateway de pagos
El `network_security_config` habilita `cleartextTrafficPermitted="true"` para
`epagos.aguascalientes.gob.mx` (**pagos**), las APIs del GEA (`egobierno2`/`eservicios2`), el
expediente digital y el C5 (`interfaces.c5ags`) (doc 03). Tráfico de pago/trámite/emergencia
puede ir sin TLS → MITM. **Fix:** quitar el cleartext; forzar HTTPS en todos los backends propios.

## H2 (Medio) — Logging HTTP nivel BODY en los 15 clientes
`provideHttpLoggingInterceptor` usa `Level.BODY` (redacta solo `Authorization`) → vuelca a logcat
los cuerpos con CURP, tarjeta de circulación y referencias de pago (doc 03, código citado).
**Fix:** `Level.NONE` en release (o `BASIC` sin cuerpos).

## H3 (Bajo-Medio) — Entornos dev + IP cruda en el NSC de producción
`apidesarrollo.c5ags.gob.mx`, `desarrolloweb.aguascalientes.gob.mx`, IP `189.254.244.184` (doc 03):
fuga de infraestructura interna. **Fix:** limpiar el NSC para el build de producción.

## Positivos

- **P1 — No desactiva la validación TLS.** Sin `getUnsafeOkHttpClient`/trust-all; validación por
  defecto contra CAs del sistema. `CertificatePinner` referenciado (config por-cliente no
  confirmada). Contrasta con ProcurApp/EDOMEX/Infonavit (doc 03).
- **P2 — Pagos con wallet tokenizado + gateway bancario** (ePagos/CitiBanamex): la tarjeta se
  tokeniza, no se almacena en claro en la app (doc 04).
- **P3 — Arquitectura moderna y limpia:** Kotlin + Hilt + Room + Moshi + MVVM, clientes de red
  separados por backend.

## Observación (privacidad)
- **Screening de llamadas anti-extorsión** (`MyCallScreeningService`/`CallReceiver`): la app
  inspecciona metadatos de llamadas entrantes. Es funcionalidad declarada, pero amplía la
  superficie de privacidad (doc 01).
- **Ubicación en tiempo real** (`LocationService`, módulo emergencia).

## Límites (sin inventar)
- Config de pinning por cliente: `CertificatePinner` está referenciado, pero no confirmé el pin
  por dominio (requeriría decompilar cada `provideXxxOkHttpClient`).
- Almacenamiento local (Room/SharedPreferences) + cifrado por Keystore: no se decompiló a fondo.
- Flujo exacto de captura de tarjeta en ePagos (WebView de CitiBanamex vs app) y reglas del
  servidor: no verificables estáticamente.
- `libd64d72.so`: nombre ofuscado, rol no confirmado.
