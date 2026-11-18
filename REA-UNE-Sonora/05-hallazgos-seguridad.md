# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.

## Observaciones

### O1 (Bajo-Medio) — Firma de desarrollador individual en app de servicio público
`CN=Omar Aguilar Fraga, O=Bus Sonora` (doc 04): el binario está respaldado por la identidad de un
**operador/proveedor privado**, no del Gobierno de Sonora. Para una app de transporte público es
una observación de **cadena de suministro** (quién controla la llave de firma y, por tanto, las
actualizaciones). La firma original está preservada (no re-firmada por APKPure).

### O2 (Bajo) — Tracking publicitario en app de transporte
**AdServices** (`ACCESS_ADSERVICES_ATTRIBUTION`/`AD_ID`) + Install Referrer + Firebase (doc 01,
03): la app hace **atribución de publicidad** (identificadores con fines de anuncios), innecesario
para la función de transporte. Observación de **privacidad**, no vulnerabilidad.

### O3 (Bajo) — Telemetría de errores a un tercero (Sentry)
Reporte de crashes/diagnóstico a `sentry.io` (doc 03). Práctica común; envía contexto del
dispositivo a un tercero.

## Positivos

- **Permisos relativamente acotados:** ubicación + biometría; **sin** cámara, micrófono,
  contactos ni ubicación en segundo plano (más contenida que Qrobús) (doc 01).
- **Biometría local + reCAPTCHA:** desbloqueo biométrico y anti-bot (doc 03).
- **Sin cleartext:** no se vio `usesCleartextTraffic` ni endpoints `http://` propios (doc 03).

## Límites (el muro, sin inventar)

- **Lógica Dart (Flutter):** en `libapp.so`, no decompilable estáticamente (doc 02). Backend
  propio, rutas de API, auth, pago y uso de la ubicación = **desconocidos** sin análisis dinámico
  (reflutter/blutter/Frida), fuera de alcance.
- **Pinning TLS:** no observable (capa de red en Dart).
- **DSN de Sentry / config de AdServices:** en el snapshot Dart; no recuperados en claro.
- **Integridad:** firma preservada pero de un dev privado (doc 04) → identifica al desarrollador,
  no a una dependencia estatal.
