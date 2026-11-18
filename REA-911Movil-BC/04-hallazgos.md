# 04 · Hallazgos (consolidado)

| # | Hallazgo | CWE | Severidad | Estado | Evidencia |
|---|----------|-----|-----------|--------|-----------|
| H1 | Cleartext HTTP permitido a 4 dominios de backend (app de emergencias) | CWE-319 | **ALTA** | **CONFIRMADO** | NSC `res/8G.xml` `cleartextTrafficPermitted=true` |
| H2 | Subdominio de desarrollo (`tijuana.c4bcdev.gob.mx`) en build de producción | CWE-489 | MEDIA | **CONFIRMADO** | `app.config` + NSC |
| H3 | Desactivación de validación TLS (trust-all) | CWE-295 | — | **AUSENTE-SANO** | no se halló; NSC sin trust user-CA |
| H4 | Secretos/API keys en claro en el cliente | CWE-312/798 | — | **AUSENTE (en lo visible)** | strings del bundle sin keys; muro Hermes |

## Detalle

### H1 — Cleartext en app de emergencias (ALTA, CONFIRMADO)
Ver [03-tls-cleartext.md](03-tls-cleartext.md). El NSC autoriza HTTP en claro hacia
`066movilbc.seguridadbc.gob.mx`, `911bc.gob.mx`, `www.seguridadbc.gob.mx` y `tijuana.c4bcdev.gob.mx`.
Es la **misma clase** que el hallazgo de 911-CDMX (cleartext en app de auxilio), aunque más acotada
(4 dominios vía NSC, no `usesCleartextTraffic=true` global). Impacto: un MITM podría leer/alterar
ubicación y alertas de auxilio si el tráfico cae en http.

### H2 — Dev en producción (MEDIA, CONFIRMADO)
`tijuana.c4bcdev.gob.mx` (entorno `c4bcdev`) va empacado y whitelisteado en la app pública.

### H3/H4 — Lo que está SANO o no se confirmó
- **Trust-all TLS: ausente.** No es el caso Infonavit/Cosmocolor. Sin trust de CAs de usuario.
- **Secretos en claro: no visibles.** Los strings del bundle Hermes no exponen keys/tokens. No se
  afirma que no existan: el bytecode Hermes es un muro para el estático.

## Límites
React Native con **Hermes** (bytecode compilado) = muro: la lógica de red, el manejo de la sesión y
posibles secretos no se leen en estático. Un análisis dinámico (Frida + proxy sobre un dispositivo)
confirmaría si el tráfico vivo usa http y revelaría secretos en runtime. Fuera de alcance aquí.

## Cruce con el mapa de proveedores
Refuerza el clúster "**apps de pánico/emergencia con cleartext**" junto a 911-CDMX. No comparte
proveedor/SDK con las otras (es desarrollo propio RN/Expo del C4 BC, firma auténtica).
