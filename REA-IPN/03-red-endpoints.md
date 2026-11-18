# Red, backend y endpoints

Recuperados del `libapp.so` y el DEX (`decompiled/endpoints.txt`), todos **HTTPS**.

## Identidad — Keycloak + Azure AD
- `https://auth.dsi.ipn.mx/auth/realms/app_ipn` — realm de **Keycloak** (OpenID Connect).
- `https://auth.dsi.ipn.mx/auth/realms/app_ipn/broker/aad_ipn/token` — **federación a Azure AD**
  (broker `aad_ipn`): el login institucional se delega a Microsoft.
- `.../protocol/openid-connect/logout` — logout OIDC.

Identidad bien planteada: delegada a Keycloak con federación corporativa; sin credenciales propias
embebidas (positivo).

## API institucional — Apache Karaf (`auth.dsi.ipn.mx/karaf/app_institucional/`)
| Ruta | Qué |
|---|---|
| `dae/credencial` | credencial digital del alumno |
| `daes/nss`, `daes/nss/obtener` | **NSS — número de seguridad social del alumno** |
| `baz/businesspayweb/cadena`, `baz/captcha/...` | **pagos/donaciones vía Banco Azteca (BAZ)** |
| `ipn/contenido`, `ipn/info` | contenido institucional |
| `alumno` | datos del alumno |
| `notification/editTokenNotification/` | registro de token push |

El backend concentra **PII sensible** (credencial, NSS) y **pagos** (BAZ) en `auth.dsi.ipn.mx` — el
dominio que el NSC **no** pinnea y para el que el `base-config` permite cleartext (doc 01, 05).

## Tercero — MyCampus Digital
- `https://apps.mycampusdigital.com/promotions` → la app es un **white-label de MyCampus Digital**,
  una plataforma comercial de apps universitarias. Parte del contenido/promociones viene del vendor.

## SDKs
- **Firebase** (FCM, notificaciones), **Google Maps** (ubicación de planteles).

## Transporte / pinning
- En la práctica todos los endpoints son **HTTPS**.
- **Pinning:** solo `www.ipn.mx` y el dominio de radio (doc 01) — **no** el backend de API. El
  `base-config` permite cleartext globalmente. Es la debilidad principal (doc 05).
