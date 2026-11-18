# 04 · Hallazgos

Cruce con `analisis/MAPA-PROVEEDORES-SDK.md`. Cada hallazgo marcado CONFIRMADO / A-CONFIRMAR /
AUSENTE-SANO. No se reporta causa sin evidencia.

## H1 — PII de menores en infra Azure de proveedor (no gob.mx) · MEDIA-BAJA · CONFIRMADO

- **Clase:** gobernanza/privacidad/residencia de datos (no un defecto de código).
- **Hecho:** los web services que manejan **CURP del menor** (`w_service_curp.php`), **datos del
  hermano** (`w_service_datos_del_hermano.php`) y **ubicación** viven en infraestructura **Azure
  genérica del proveedor LUCA** — `servicios.mexicocentral.cloudapp.azure.com` (VM) y
  `seduinscripciones.azurewebsites.net` (App Service) — **no** en dominios `gob.mx` de la SEDU.
- **Mitigación presente:** todo el tránsito es **HTTPS**.
- **Riesgo:** concentración de PII de menores bajo el control operativo de un proveedor privado,
  fuera de la infraestructura gubernamental. Punto a revisar por SEDU Coahuila (contrato, aviso de
  privacidad, residencia/custodia de datos de menores).

## H2 — Endpoints de demo/plantilla en producción · BAJA · CONFIRMADO

- **CWE-489 (Active Debug/Leftover).** El release empaca `jsonplaceholder.typicode.com`,
  `pokeapi.co`, `icanhazip.com`, `one.one.one.one` — residuo de la plantilla Flutter del proveedor.
- **Riesgo:** bajo (APIs públicas de terceros), pero higiene: no deben ir en un build de producción.

## H3 — TLS / cleartext / secretos · AUSENTE-SANO

- **Sin cleartext** (manifest sin `usesCleartextTraffic`, sin NSC, targetSdk 36 → bloqueado).
- **Sin secretos** hardcodeados (keys, tokens, `.env`).
- **Sin trust-all evidenciable** (los símbolos `BadCertificateCallback` son del runtime Dart
  estándar; la desactivación, si existiera, vive en el Dart AOT = muro). Ver
  [03-tls-cleartext.md](03-tls-cleartext.md).
- **Contraejemplo limpio** entre las apps de datos sensibles del corpus: una app de datos de menores
  que **no** hereda el cleartext ni el trust-all.

## Qué quedó tras el muro Dart (requiere dinámico)

- Si el cliente Dart valida TLS o sobreescribe `badCertificateCallback`.
- Si hay autenticación/token en las llamadas PHP y cómo se protege la consulta por CURP.
- La lógica real del flujo de preinscripción.
