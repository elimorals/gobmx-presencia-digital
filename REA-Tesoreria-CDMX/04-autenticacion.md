# Autenticación

Fuente: `assets/public/main.*.js` (lógica de login) y pantallas en los chunks
`1399.*.js`, `9443.*.js`.

## OAuth2 Resource Owner Password Credentials (Laravel Passport)

El login arma esta petición de token (evidencia en `main.*.js`):

```js
// login(T): normaliza correo/teléfono y pide token
const M = {
  grant_type: "password",
  client_id: N.passport.client_id,        // = 5
  client_secret: N.passport.client_secret, // fijo en el bundle
  login: S,                                // correo o teléfono, trim+lowercase
  password: T.password.trim(),
  rememberme: T.rememberme
};
```

Y la configuración embebida:

```js
passport: { grant_type:"password", client_id:5, client_secret:"3Yi1jmX…" /* redactado */ }
```

- Flujo **ROPC** (`grant_type=password`): la app manda usuario y contraseña
  directo al endpoint de token. Es el patrón clásico de apps Laravel+Passport.
- **`client_secret` hardcodeado en el cliente.** `client_id:5` y un `client_secret`
  de 40 chars fijos están en el bundle JS distribuido. Un secreto de cliente en un
  binario público **no es secreto**: cualquiera lo extrae y puede hacerse pasar por
  el cliente oficial de Passport contra el endpoint de token. El valor completo
  queda en `evidence/` (no se reproduce aquí) (doc 08).

## Identificador de acceso

`login` admite **correo o teléfono** (se normaliza a minúsculas). El password se
envía con `trim()`.

## Biometría

Plugin `capacitor-native-biometric` presente y referenciado en pantallas de login
(`9443.*.js`). Habilita re-login con huella/rostro.

> **Límite de evidencia:** el único match de `setCredentials` hallado es el **shim
> del plugin** ("Method not implemented") en `6707.*.js`, no la llamada real que
> guardaría usuario/contraseña. Por tanto **no afirmamos** dónde ni cómo persiste la
> credencial para el desbloqueo biométrico; solo que el plugin está integrado. El
> plugin, por diseño, guarda en Keystore/EncryptedSharedPreferences.
