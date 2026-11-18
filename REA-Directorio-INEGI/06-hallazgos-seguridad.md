# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: app de **datos públicos** (DENUE open data) → sin credenciales ni PII sensible; el
riesgo se centra en **privacidad** (telemetría) y **buenas prácticas** (Maps key, build viejo).

## H1 (Bajo-Medio) — Google Maps API key embebida sin restricción verificable
`AIzaSy…` en el meta-data del manifiesto y en `strings.xml` (doc 04). Es normal que viaje en el
cliente, pero debe estar **restringida por package + SHA-1** en Google Cloud. Si no lo está,
cualquiera la extrae del APK y consume **cuota facturable** a nombre del INEGI. La restricción
no es verificable estáticamente. **Fix:** confirmar/aplicar restricción de la key; rotarla si
estuvo sin restringir.

## H2 (Bajo) — Publicidad + analítica + AD_ID en app de estadística federal
`pagead2.googlesyndication.com` (ads), Google Analytics (GMS) y permiso `AD_ID` (doc 03). Recolecta
identificadores con fines de medición/anuncios. Observación de **privacidad**, no vulnerabilidad.
**Fix:** evaluar si una app del INEGI debe incluir ads/AD_ID; migrar a analítica sin publicidad.

## H3 (Bajo) — Build obsoleto / deuda técnica
targetSdk 33, `com.google.android.gms.analytics` **deprecado**, enlaces `goo.gl` (apagados), y el
**mismatch** del permiso `MAPS_RECEIVE` (`denuemv` declarado vs `denue` usado, doc 01). No es un
agujero de seguridad, pero indica falta de mantenimiento. **Fix:** actualizar SDK/dependencias.

## Positivos

- **P1 — Transporte HTTPS sin bypass TLS.** Endpoints oficiales `https://www.inegi.org.mx/…`;
  sin cleartext, sin trust-all, sin `getUnsafeOkHttpClient` → validación por defecto (doc 03). A
  diferencia de ProcurApp/Infonavit/Licencia EDOMEX, **no** desactiva TLS.
- **P2 — PII mínima.** Datos públicos; lo único sensible es la ubicación/cámara del usuario, con
  permisos justificados y sin ubicación en background (doc 01, 04).
- **P3 — Firma auténtica del INEGI** (`O=INEGI`) → binario íntegro del emisor (doc 05).

## Límites (sin inventar)

- **Código ofuscado (R8):** el parseo de la ficha y el cliente HTTP exacto están en helpers
  minificados (`o4.b`/`o4.c`); no se trazaron función por función. La superficie de seguridad
  (permisos, endpoints, TLS, Maps key, telemetría) sí se cubrió.
- **Restricción de la Maps key:** vive en Google Cloud (server-side), no verificable desde el APK.
- **Uso de la ubicación:** se infiere "cerca de mí"; no se confirmó a qué tercero viajan las
  coordenadas más allá del INEGI/Google.
