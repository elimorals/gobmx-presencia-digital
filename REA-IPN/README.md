# Ingeniería inversa de IPN Oficial 2.2.2 — índice

Análisis **estático** del APK `com.ipnmx.AppIPN` con REA (JADX + inspección de archivo). La app
**nunca se ejecutó**. Es la app institucional oficial del **Instituto Politécnico Nacional (IPN)**
para alumnos (credencial, servicios escolares, pagos). **#44 por instalaciones** del inventario
gob.mx (≈100 K).

> **Flutter — lógica en el snapshot Dart (muro), pero endpoints recuperables.** Hecha con Flutter
> (engine 3.5.3 stable, `libapp.so` Dart AOT). La lógica vive en el snapshot → no decompilable; sin
> embargo, el `libapp.so` **sí deja los endpoints en claro**, revelando todo el backend: identidad
> con **Keycloak + Azure AD** (`auth.dsi.ipn.mx`), servicios Apache Karaf (credencial, **NSS**,
> pagos **BAZ/Banco Azteca**). Es un **white-label de MyCampus Digital** (`apps.mycampusdigital.com`).
> El punto débil está en el `network_security_config`: **cleartext permitido globalmente** y
> **pinning solo para los dominios de contenido/radio**, no para el backend sensible.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, backend, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, NSC (cleartext + pinning parcial) |
| 02 | [02-flutter-muro.md](02-flutter-muro.md) | Flutter/Dart AOT, qué se recuperó |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | Keycloak/Azure AD, Karaf, NSS, pagos BAZ, MyCampus |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Re-firmada por APKPure |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK base (versionado) |
| `decompiled/endpoints.txt` | 39 endpoints recuperados del `libapp.so`/DEX (no versionado) |
| `evidence/network_security_config.xml` | NSC decodificado (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 5 hallazgos principales

1. **(Medio) NSC con prioridades invertidas.** `base-config cleartextTrafficPermitted="true"`
   habilita **HTTP en claro globalmente**, y el **certificate pinning** solo cubre `www.ipn.mx` y
   el dominio de radio — **no** el backend sensible `auth.dsi.ipn.mx` (auth, NSS, pagos), que queda
   sin pinning y con cleartext permitido (doc 01, 05).
2. **(Bajo-Medio) White-label de MyCampus Digital.** La app institucional corre sobre la plataforma
   de un **tercero** (`apps.mycampusdigital.com/promotions`): un proveedor comercial de apps
   universitarias (doc 03).
3. **PII sensible por el backend.** Los endpoints manejan **credencial** del alumno, **NSS** (número
   de seguridad social: `daes/nss/obtener`) y **pagos BAZ/Banco Azteca** (`baz/businesspayweb`) —
   todo vía `auth.dsi.ipn.mx` por HTTPS (doc 03).
4. **Identidad bien planteada (positivo).** **Keycloak** (`/auth/realms/app_ipn`, OpenID Connect) con
   federación a **Azure AD** (broker `aad_ipn`) → identidad delegada, estándar (doc 03).
5. **Firma re-firmada por APKPure** (`CN=Android, O=Google Inc.`) → arquitectura sí, integridad no
   (doc 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a ningún
endpoint. La lógica Dart no se desensambló (muro); se documentan los endpoints recuperados del
`libapp.so`, el NSC, los permisos y la firma, con evidencia. Firma re-firmada por APKPure → no
verificable como auténtica del IPN.
