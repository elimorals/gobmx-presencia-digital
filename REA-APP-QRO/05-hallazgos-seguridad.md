# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.

## H1 (Medio) — Superficie de permisos muy amplia
Declara ubicación + **`FOREGROUND_SERVICE_LOCATION`** (segundo plano), `CAMERA`, `RECORD_AUDIO`,
`READ_CONTACTS`, biometría y alarmas exactas (doc 01). Una super-app justifica parte, pero el
conjunto es de alto impacto para privacidad y el **uso real de cada permiso vive en el snapshot
Dart** → no verificable. Se reporta como superficie, no como abuso comprobado. **Fix (defensivo):**
minimizar permisos por módulo / solicitarlos solo al usarse.

## Observaciones
- **O1 (Bajo)** Firebase **Remote Config**: comportamiento configurable server-side (doc 03) →
  parte de la lógica no está en el binario.
- **O2 (Bajo)** `READ_CONTACTS` sin uso evidente desde el manifiesto (la razón vive en el Dart).

## Positivos
- **Sin cleartext declarado** (targetSdk 36, sin `usesCleartextTraffic`) (doc 01, 03).
- Notificaciones/identidad delegadas a Firebase (estándar).

## Límites (el muro, sin inventar)
- **Lógica Dart (Flutter):** en `libapp.so`, no decompilable estáticamente (doc 02). Backend
  `*.queretaro.gob.mx`, rutas de API, auth, trámites y uso real de los permisos = **desconocidos**
  sin análisis dinámico (reflutter/blutter/Frida), fuera de alcance.
- **Pinning TLS:** no observable (capa de red en Dart).
- **Integridad:** binario re-firmado por APKPure (doc 04) → conclusiones de arquitectura, no de
  integridad del artefacto original.
