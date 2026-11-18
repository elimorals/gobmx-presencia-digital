# Arquitectura — MVP nativo

## Confirmación de pila

`inspect_android_package`: 7 175 clases, 2 dex, **sin** `flutter_assets`/`libapp.so` (Flutter),
`assemblies/*.dll` (.NET), `il2cpp` (Unity), `assets/www` (Capacitor) ni bundle RN. Es una app
**Android nativa Kotlin/Java** convencional, **decompilable**.

## Patrón MVP

El package `com.gobbc.identidaddigital` está organizado en capas (nombres legibles, poca
ofuscación):

- **`contracts.*`** — interfaces MVP (`LoginContract`, `RegistroUsuarioContract`,
  `ObtenerCurpContract`, `VerificarIdentidadCurpContract`, `RegistroTarjetaContract`,
  `CompartirTarjetaContract`, `SolicitarCodigoSmsContract`, `ConfirmarCodigoSmsContract`, …).
- **`presenters.*`** — lógica (`LoginPresenter`, `RegistroUsuarioPresent`,
  `VerificarIdentidadCurpPresenter`, `CompartirTarjetaPresenter`, `RegistroLicPresenter`, …).
- **`model.*`** — entidades (`Ciudadano`, `Identidad`, `Credenciales`, `Licencia`,
  `TarjetaCirculacion`, `Documento`, `TokenSession`, `AuthorizationCredentials`, `Dispositivo`,
  `reCaptcha.*`).
- **`remote.api.*`** + **`remote.client.ServiceGenerator`/`TokenGenerator`** — capa de red
  (Retrofit/OkHttp).
- **`ui.activities.*`** — pantallas.
- **`manager.DataManager`** + `utils.*` (`ImageDownloader`, `NetworkUtils`, `ConnectivityChecker`,
  `TcPdfProvider`).

## SDKs

- **Retrofit + OkHttp** (red; `ServiceGenerator`, doc 04).
- **reCAPTCHA Enterprise** (`model.reCaptcha.*` con `RiskAnalysis`, `TokenProperties`,
  `ReCaptchaResponse`) — anti-bot/antifraude en registro/login.
- **Firebase** — Phone Auth (OTP SMS, `SmsFirebaseActivity`) + FCM (notificaciones).
- Glide/descarga de imágenes (`ImageDownloader`, `ImagesApi`) para las credenciales/tarjetas.

## Observación

Arquitectura limpia y mantenible; el código propio es legible (poca ofuscación). Eso facilita la
auditoría y es, en sí, positivo. Las debilidades no son de diseño de la app sino de
**configuración** (logging, pinning, entornos — doc 04).
