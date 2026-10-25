# Autenticación

Tres mecanismos conviven: **OAuth2 (ROPC)** para la sesión de SAT Móvil, **SAT ID** para
verificación de identidad, y **biometría local** para re-desbloqueo. Todo lo de abajo sale de
`.env.production` y de cadenas en `libapp.so`; no se probó ningún endpoint.

## OAuth2 — Resource Owner Password Credentials (ROPC)

De `.env.production`:

```
OAUTH_TOKEN="https://sm-sat-movilzuul-sm-servicios-moviles.cnh.cloudb.sat.gob.mx/satmovil/v1/smnovel?"
GRANT_TYPE="password"
SCOPE="satmovil"
RESOURCE_SERVER="satmovil"
GRANT_TYPE_REFRESH="refresh_token"
```

| Campo | Valor | Lectura |
|---|---|---|
| Flujo | `grant_type=password` | **ROPC**: la app recibe usuario (RFC) + contraseña (CIEC) y las **postea directamente** al endpoint de token. |
| Endpoint token | `.../satmovil/v1/smnovel?` | Pasa por el gateway **Zuul** (`...movilzuul...`), ver doc 04. |
| Scope / resource | `satmovil` | Un único scope para toda la app. |
| Refresh | `grant_type=refresh_token` | Hay renovación de token sin re-pedir contraseña. |

**Por qué importa.** ROPC está **desaconsejado por OAuth 2.1**: obliga a la app cliente a ver la
contraseña del usuario en claro (aunque sea en memoria) y no soporta MFA ni consentimiento del
proveedor de identidad. Es común en apps de gobierno que envuelven un login legacy (CIEC), pero
amplía el riesgo frente a flujos basados en navegador (Authorization Code + PKCE, que es lo que
usa CFE con AppAuth/OIDC). Aquí **no hay AppAuth ni OIDC**: el login es propio.

El `refresh_token` y el `access_token` se guardan con `flutter_secure_storage` (doc 05).

## SAT ID — verificación de identidad

De `.env.production`:

```
SAT_ID_BASE="https://chiikul.sat.gob.mx/api/v1"
SAT_ID_CAPTCHA="https://captcha.sat.gob.mx/get_captcha"
```

- **SAT ID** es el servicio de verificación de identidad del contribuyente (alta/recuperación de
  contraseña y trámites que exigen probar quién eres). Su backend en prod es
  `chiikul.sat.gob.mx/api/v1` (host interno con nombre de fantasía).
- El flujo combina **captcha propio** (`captcha.sat.gob.mx/get_captcha`) con captura de
  **cámara + audio + video** y verificación biométrica/liveness on-device (doc 06).
- En UAT/dev, `SAT_ID_BASE` y el captcha apuntan a una **IP de Azure hardcodeada**
  (`13.85.190.35:3002` / `:3001`) — ver doc 04.

## Captcha

Captcha servido por el propio SAT (`get_captcha`), no reCAPTCHA de Google (contraste con CFE, que
usa `RecaptchaActivity` de Firebase). Es un captcha de imagen propio consumido desde Dart.

## Biometría local (re-desbloqueo de sesión)

- Paquete **local_auth** (`local_auth_platform_interface`), permisos `USE_BIOMETRIC` +
  `USE_FINGERPRINT`. Cadenas en `libapp.so`: `AndroidBiometricType`, `BiometricType`,
  *"Biometric authentication is not set up on your device..."*.
- Sirve para **re-abrir la app sin re-teclear credenciales** (huella / Face ID), no para
  autenticar contra el servidor. La sesión real la sostiene el `access_token`.

## Resumen de confianza

| Elemento | Estado |
|---|---|
| Login | OAuth2 **ROPC** propio (RFC + CIEC) — sin MFA ni OIDC |
| Renovación | `refresh_token` |
| Identidad fuerte | SAT ID (captcha + biometría/liveness) |
| Re-desbloqueo | Biometría local (local_auth) |
| Almacenamiento de tokens | `flutter_secure_storage` (doc 05) |

**Límite:** estos son los parámetros declarados y los hosts; el intercambio real (headers,
cuerpo del token, manejo de errores) está en Dart AOT y no se trazó en estático.
