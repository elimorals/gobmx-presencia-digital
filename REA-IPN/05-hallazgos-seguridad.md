# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.

## H1 (Medio) — NSC: cleartext global + pinning con prioridad invertida
El `network_security_config` (doc 01):
- `base-config cleartextTrafficPermitted="true"` → **HTTP en claro permitido** para todos los
  dominios no listados, incluido el backend sensible `auth.dsi.ipn.mx` (auth, NSS, pagos BAZ).
- **Certificate pinning solo para `www.ipn.mx` y el dominio de radio** (contenido/streaming), **no**
  para `auth.dsi.ipn.mx`.

Resultado: se protege lo menos sensible (marketing/radio) y se deja lo más sensible (identidad,
NSS, pagos) sin pinning y con cleartext permitido. **Fix:** quitar el cleartext global; pinnear
`auth.dsi.ipn.mx` o moverlo a un `domain-config` propio sin cleartext.

## H2 (Bajo-Medio) — White-label de un proveedor (MyCampus Digital)
La app institucional corre sobre `apps.mycampusdigital.com` (doc 03): plataforma comercial de un
tercero. Implica dependencia de proveedor para una app oficial con PII estudiantil (observación de
cadena de suministro, no vulnerabilidad per se).

## H3 (Bajo) — PII sensible concentrada en el canal menos protegido
Credencial del alumno y **NSS** (`daes/nss/obtener`) + pagos BAZ viajan por `auth.dsi.ipn.mx`,
justo el dominio sin pinning y con cleartext permitido por el NSC (H1). El dato es sensible; el
transporte, aunque HTTPS en práctica, no está endurecido para él.

## Positivos

- **Identidad delegada (Keycloak + Azure AD, OIDC)** — sin credenciales propias embebidas (doc 03).
- **Endpoints HTTPS** en la práctica; permisos acotados (doc 01).
- **Código decompilable solo en parte** (es Flutter), pero los endpoints recuperables dan
  transparencia del backend para auditoría.

## Límites (el muro, sin inventar)
- **Lógica Dart (Flutter):** en `libapp.so`, no decompilable (doc 02). Cómo arma las peticiones, qué
  campos de PII envía, cómo guarda los tokens y el flujo de pago BAZ = **desconocidos** sin análisis
  dinámico (reflutter/blutter/Frida), fuera de alcance.
- **Integridad:** binario re-firmado por APKPure (doc 04) → conclusiones de arquitectura, no de
  integridad del artefacto original del IPN.
