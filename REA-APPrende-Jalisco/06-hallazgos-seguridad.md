# Hallazgos de seguridad y preguntas abiertas

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se llamó a Firebase ni a
endpoints, no se usaron las llaves. Contexto: **PII educativa fuerte** (calificaciones, asistencia,
**salud**, RH) → el transporte es sano, el riesgo vive en Firebase y los secretos del bundle.

## H1 (Medio) — Proyecto Firebase de prueba en producción
`apprende-prueba` (+ RTDB de prueba) embebido junto a `apprende-prod` en el build público (doc 03).
Fuga de infraestructura interna y superficie extra. **Fix:** solo producción en el build publicado.

## H2 (Medio, pregunta abierta) — Reglas de Firebase RTDB no auditables
Uso de Realtime Database para datos educativos; la seguridad depende de las **reglas del lado
servidor**, no presentes en el cliente (doc 03). Si fueran laxas → exposición de PII. **No
verificable estáticamente** (requiere probar la RTDB, fuera de alcance/autorización). La SEJ debería
auditar que las reglas exijan autenticación y limiten por usuario. **No se afirma que estén mal.**

## H3 (Bajo-Medio) — `ApiKey` hex hardcodeado
Secreto de app embebido en el bundle JS (`ce93108d…`, doc 04), recuperable del APK. Propósito no
confirmado. **Fix:** sacarlo del cliente / usar token por sesión.

## H4 (Bajo) — Llaves web de Google/Firebase sin restricción verificable
6 llaves `AIzaSy…` en claro (doc 04). Públicas por diseño, pero deben restringirse por package+SHA-1
y API en GCP; el estado de restricción no es verificable estáticamente.

## H5 (Bajo) — Source maps en producción
Se publican los `.map` → el TypeScript Angular es reconstruible (doc 02). Exposición de código/
estructura. **Fix:** no publicar source maps en producción.

## Positivos
- **Transporte HTTPS** a dominios oficiales `jalisco.gob.mx`; sin cleartext (doc 01, 03).
- **Identidad delegada a Firebase Auth** — sin credenciales propias en el cliente (doc 03).
- **Permisos moderados** (sin ubicación/SMS/contactos directos) (doc 01).

## Límites (sin inventar)
- **Reglas de Firebase** (RTDB/Auth) y propósito del `ApiKey` custom: no verificables sin tocar los
  servicios (fuera de alcance).
- **Lógica de negocio:** reconstruible de los source maps, pero no se recorrió módulo a módulo; se
  documentó la arquitectura, los backends y los secretos.
- **Integridad:** binario re-firmado por APKPure (doc 05) → conclusiones de arquitectura, no de
  integridad del artefacto original de la SEJ.
