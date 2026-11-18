# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: app de **contenido educativo gratuito** → sin credenciales de pago ni PII crítica
aparente; el impacto se centra en **integridad de contenido y privacidad de navegación**.

## H1 (Medio) — Cleartext HTTP a backends de producción
El `network_security_config` habilita `cleartextTrafficPermitted="true"` para la API y el
middleware de producción (`api`/`middleware`/`libros`/`secundaria`/`milesdehistorias.conaliteg.gob.mx`)
y para varias IPs (doc 03). El tráfico puede viajar sin TLS → **MITM** puede leer/alterar
contenido y telemetría. **Fix:** quitar el cleartext; forzar HTTPS en todos los backends propios.

## H2 (Medio) — Confianza en CAs instaladas por el usuario
`<certificates src="user" />` en los `trust-anchors` → MITM **trivial** instalando una CA
(doc 03). **Fix:** eliminar `src="user"`; confiar solo en `system` (y, si se quiere robustez,
añadir pinning).

## H3 (Bajo) — Residuos de desarrollo en producción
El NSC de producción incluye IP privada (`192.168.101.6`), emulador (`10.0.2.2`), loopback y el
subdominio `pruebasistemas.conaliteg.gob.mx` (doc 03): fuga menor de infraestructura interna y
señal de build no saneado. **Fix:** limpiar el NSC para el build de producción.

## Positivos

- **Permisos acotados** (lector: red + notificaciones + almacenamiento; sin cámara/ubicación/
  contactos) (doc 01).
- **Sin PII crítica aparente:** biblioteca gratuita; no se observaron flujos de pago ni de
  credenciales sensibles.
- **Código decompilable** (nativo, sin ofuscar) — transparente para auditoría.

## Límites (sin inventar)

- No se decompiló la UI de lectura clase por clase (no aporta hallazgos de seguridad sobre lo ya
  observado en el NSC).
- Las referencias a `HostnameVerifier`/`checkServerTrusted` en el DEX son símbolos de librería;
  **no se confirmó** un trust-all en código (distinto de la debilidad del NSC).
- Las rutas exactas de la API bajo `api.conaliteg.gob.mx` no se enumeraron (no se tocó el
  endpoint, por alcance).
- Firma re-firmada por APKPure → conclusiones de arquitectura, no de integridad.
