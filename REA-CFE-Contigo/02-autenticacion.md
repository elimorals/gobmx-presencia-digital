# Autenticación y sesión

La app usa un esquema de **OAuth2 con tokens de acceso/refresco**, reforzado con **OTP por
correo**, **captcha propio**, **biometría** y, para ciertos flujos, **AppAuth (OIDC)** y
**Firebase Auth**. Todo lo de abajo proviene de metadatos de clases (`inspect_android_class`)
y constantes del DEX; los endpoints concretos no son recuperables (ver doc 04/05).

## Tokens OAuth2 — `data.AuthResponse`

Campos observados (Kotlin `data class`):

| Campo | Tipo | Significado |
|---|---|---|
| `access_token` | String | Token de acceso (se envía como `Authorization: Bearer`) |
| `refresh_token` | String | Token de refresco para renovar sesión sin re-login |
| `expires_in` | long | Vigencia del access token |
| `created_at` | long | Marca de emisión |
| `mustChangePassword` | Boolean | Fuerza cambio de contraseña al entrar (p.ej. primer acceso) |

Esto implica un servidor de autorización propio (grant tipo password o code) que emite
JWT/opaque tokens con refresh. La presencia de `Basic ` y `Authorization` como literales en
el DEX sugiere que el intercambio de token usa **HTTP Basic** (client credentials del cliente
móvil) y las llamadas de negocio usan **Bearer**.

## OTP por correo — `data.OtpRequestDto` / `OtpResponseDto` / `OtpValidarRequestDto`

`OtpRequestDto` tiene un único campo: **`email`**. El flujo es:
1. `OtpRequestDto{email}` → el backend envía un código al correo.
2. `OtpValidarRequestDto` → valida el código.
3. `OtpResponseDto` → resultado.

Segundo factor / verificación de identidad por correo, usado al menos en recuperación de
contraseña y probablemente en alta de dispositivo.

## Captcha propio — `data.Captcha*` + `ui.captcha.CaptchaKey`

`CaptchaImage`, `CaptchaResponse`, `CaptchaTokenResponse` y `ui.captcha.CaptchaKey`: la app
pide una imagen de captcha al backend, el usuario la resuelve y se devuelve un token. Captcha
**propio de CFE**, independiente del reCAPTCHA de Firebase (que se usa solo en el flujo de
Firebase Auth).

## Biometría

Permisos `USE_BIOMETRIC` + `USE_FINGERPRINT`. Patrón típico: tras el primer login con
credenciales, la sesión (o el refresh token) se guarda cifrada bajo una clave de
**AndroidKeyStore** con `setUserAuthenticationRequired`, y el desbloqueo biométrico libera
esa clave. Ver doc 03 para el almacenamiento.

## AppAuth / OIDC

`net.openid.appauth.*` con `RedirectUriReceiverActivity` y redirect
`mx.com.cfe.cfecontigo://`. Indica un flujo **Authorization Code + PKCE** contra un IdP
(posiblemente para contratación o vinculación con otra identidad gubernamental). El redirect
custom-scheme es estándar de AppAuth.

## Firebase Auth

`GenericIdpActivity` + `RecaptchaActivity` (schemes `genericidp://firebase.auth`,
`recaptcha://firebase.auth`) y `default_web_client_id` en `strings.xml`. Firebase Auth está
presente, probablemente para federación (Google Sign-In) o verificación por SMS/reCAPTCHA. No
es el mecanismo principal (que es el OAuth2 propio), sino complementario.

## Resumen del modelo de identidad

```
Login (usuario/contraseña + captcha propio)
   └─> OAuth2 token endpoint (HTTP Basic del cliente)
         └─> AuthResponse { access_token, refresh_token, expires_in, mustChangePassword }
               ├─ access_token  -> Authorization: Bearer   (llamadas de negocio)
               └─ refresh_token -> renovación de sesión
Factores/variantes:
   - OTP por correo (recuperación / verificación)
   - Biometría (desbloqueo local de sesión, AndroidKeyStore)
   - AppAuth/OIDC (code+PKCE, redirect mx.com.cfe.cfecontigo://)
   - Firebase Auth (federación / reCAPTCHA)
```

> Límite: no se recuperó la URL del token endpoint ni los scopes; el cifrado de strings de
> DexGuard oculta esas constantes (doc 05).
