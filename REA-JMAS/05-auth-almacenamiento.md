# Autenticación y almacenamiento

## Auth
- Login por `logIn` → `LoginResponse`/`TokenResponse` (JWT). El token se inyecta en cada petición
  con `di.AuthInterceptor` (cabecera `Authorization`), y `data.network.utils.TokenAuthenticator`
  hace **refresh** del token ante un 401 (patrón OkHttp `Authenticator`).
- Registro con `signUp`/`UserRegisterRequest`; recuperación/cambio de contraseña
  (`resetPassword`, `updatePassword`); baja de cuenta (`deleteAccount`).

## Almacenamiento
- **Room** (`JMASMDatabase`): contratos, notificaciones, datos de usuario locales.
- **`SharedPreferencesManager`** guarda el token/sesión usando `getSharedPreferences` **plano**.
  No se observó `EncryptedSharedPreferences` ni `MasterKey`/Keystore (barrido del DEX) →

### Hallazgo (Bajo) — token JWT en SharedPreferences sin cifrar
El JWT de sesión queda **legible en claro** en el almacenamiento de la app. En un dispositivo no
comprometido Android lo aísla por sandbox, pero:
- en un dispositivo con root o vía un respaldo (`adb backup`/nube) el token es recuperable,
- con `allowBackup` por defecto, podría incluirse en respaldos.
**Fix:** usar `EncryptedSharedPreferences` con una `MasterKey` del AndroidKeyStore para el token.

> Matiz honesto: no se confirmó el valor de `android:allowBackup` ni la caducidad del JWT
> (server-side); el impacto exacto depende de ambos. Se reporta el almacenamiento sin cifrar, que
> sí está confirmado.
