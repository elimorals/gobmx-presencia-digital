# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se llamó a ningún endpoint y
la clave de telemetría **no se usó**.

## H1 (Bajo) — Clave de Application Insights embebida en el `.env`

`assets/flutter_assets/.env` → `APPSINSIGHTS_INSTRUMENTATION_KEY=<clave>` en claro (doc 02).

- **Qué permite:** la *instrumentation key* de Azure Application Insights es de **ingestión de
  telemetría** — con ella se puede **enviar/contaminar** telemetría en la cuenta de Pemex
  (inyectar eventos falsos, inflar métricas), **no leer** datos ni acceder al backend.
- **Por qué es Bajo:** Microsoft la clasifica como de baja sensibilidad y la **deprecó** a favor
  de *connection strings* precisamente porque no es un secreto fuerte. Aun así, no debería
  viajar en el binario distribuible.
- **Fix:** mover a *connection string* con ingestión autenticada, o inyectarla vía configuración
  remota; rotarla.

## Observaciones (postura correcta)

### P1 (positivo) — Transporte HTTPS al dominio oficial
Único backend `https://api-cloud.pemex.com/asiste`; sin cleartext ni endpoints `http://`
propios (doc 03).

### P2 (positivo) — Permisos acotados
Solo ubicación (localizador) + notificaciones + red. Sin cámara, micrófono, contactos, SMS,
teléfono ni ubicación en segundo plano (doc 01).

## Límites (el muro, sin inventar)

- **Lógica Dart (Flutter):** en `libapp.so` (snapshot AOT), no decompilable estáticamente
  (doc 02). Rutas de API bajo `/asiste`, flujo de auth, manejo de ubicación y de datos =
  **desconocidos** sin análisis dinámico (reflutter/blutter/Frida), fuera de alcance.
- **Pinning TLS:** no observable (capa de red en Dart).
- **Qué más hay en el backend:** `/asiste` es el host; su superficie de API no se enumeró (no se
  tocó el endpoint, por alcance y autorización).
- **Integridad:** binario re-firmado por APKPure (doc 04) → las conclusiones son de arquitectura,
  no de integridad del artefacto original de Pemex.
