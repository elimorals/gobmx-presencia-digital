# Autenticación — Azure AD B2C (Microsoft Identity)

Toda la configuración de identidad vive en `assets/appsettings.json` (ver
`evidence/appsettings.json`), en claro.

## Dos flujos de identidad

### 1. Azure AD (corporativo / `Settings`)
```
Authority: https://login.microsoftonline.com/<TenantId>
ClientId:  885d5352-9a8b-41ad-bde8-b0002d39cd8c
TenantId:  17a8b2c6-b69e-4f1f-8377-a679f3ae4b47
Scopes:    api://dec44ea0-…/lecturaEscritura
```
Autenticación contra un tenant de Azure AD de la SEG, con scope de API propia
(`lecturaEscritura`).

### 2. Azure AD B2C social (`…Social`)
```
InstanceUrlSocial: https://seggtob2c.b2clogin.com
TenantSocial:      https://seggtob2c.onmicrosoft.com
AuthoritySocial:   https://seggtob2c.b2clogin.com/tfp/seggtob2c.onmicrosoft.com/B2C_1_InicioSesionRedesSociales
ClientIdSocial:    aee1f87d-1554-4a1e-9671-d78d8754abe8
ScopesSocial:      https://seggtob2c.onmicrosoft.com/…/autorizacion.acceso
```
Un tenant **Azure AD B2C** (`seggtob2c`) con una **política de inicio de sesión con redes
sociales** (`B2C_1_InicioSesionRedesSociales`) para ciudadanos/padres.

## Valoración

- **Patrón correcto (positivo).** La identidad está **delegada a Microsoft Identity** (Azure AD
  + B2C). La app es un **cliente público** (mobile): usa `ClientId`/`TenantId` — que son
  **identificadores públicos de OAuth/OIDC, no secretos** — y **no** incluye `client_secret`
  (lo correcto; un secreto en un binario distribuible sería el hallazgo). El token se obtiene
  vía MSAL contra `login.microsoftonline.com`/`b2clogin.com` por HTTPS.
- **Exposición real:** que estos IDs estén en claro **no es una vulnerabilidad** — están
  pensados para ser públicos. Lo que sí revelan es el **tenant** (`seggtob2c`), las **políticas**
  y los **scopes** de la SEG, útil para un atacante como reconocimiento de la superficie de
  identidad (doc 06).

> No se ejecutó el flujo; no se obtuvieron tokens. La valoración es sobre la **configuración**
> presente, no sobre el comportamiento en runtime del login.
