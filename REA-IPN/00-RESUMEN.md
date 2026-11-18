# Resumen — IPN Oficial 2.2.2 (`com.ipnmx.AppIPN`)

## Identidad
- **App:** IPN Oficial — Instituto Politécnico Nacional (app institucional de alumnos).
- **Package:** `com.ipnmx.AppIPN` · **versión** 2.2.2 (versionCode 81). compileSdk 35.
- **#44** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
**Flutter** (engine **3.5.3 stable**; `libapp.so` Dart AOT + `libflutter.so` en el split de
arquitectura). Lógica en el snapshot Dart → **muro**. **Pero** el `libapp.so` deja los **endpoints
en claro** (recuperables con `strings`) → se reconstruyó todo el mapa de backend sin ejecutar.
Sin `.env` empaquetado (solo `AssetManifest`).

## Backend (recuperado del `libapp.so`, `decompiled/endpoints.txt`)
- **Identidad:** **Keycloak** `auth.dsi.ipn.mx/auth/realms/app_ipn` (OpenID Connect) + federación a
  **Azure AD** (`broker/aad_ipn/token`).
- **API institucional (Apache Karaf):** `auth.dsi.ipn.mx/karaf/app_institucional/...`
  - `dae/credencial` (credencial digital del alumno)
  - `daes/nss`, `daes/nss/obtener` (**NSS — número de seguridad social**)
  - `baz/businesspayweb/...` + `baz/captcha/...` (**pagos/donaciones vía Banco Azteca BAZ**)
  - `ipn/contenido`, `ipn/info`, `alumno`, `notification/editTokenNotification`
- **Tercero:** `apps.mycampusdigital.com/promotions` → la app es **white-label de MyCampus Digital**.
- SDKs: Firebase (FCM), Google Maps.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte (práctica) | ✅ Los endpoints son HTTPS |
| NSC | ⚠️ **cleartext permitido globalmente** (`base-config`) |
| Pinning | ⚠️ Solo `www.ipn.mx`/radio — **NO** el backend `auth.dsi.ipn.mx` (auth/NSS/pagos) |
| Identidad | ✅ Keycloak + Azure AD (OIDC) |
| Proveedor | ⚠️ White-label MyCampus Digital (tercero) |
| Firma | ⚠️ Re-firmada por APKPure |

## Muro
La lógica Dart (cómo arma las peticiones, maneja tokens, PII, flujo de pago BAZ) vive en el
snapshot — no observable estáticamente. Vía: reflutter/blutter/Frida (dinámico, fuera de alcance).
Lo recuperable (endpoints, NSC, SDKs, permisos) quedó documentado; el resto se marca como límite.
