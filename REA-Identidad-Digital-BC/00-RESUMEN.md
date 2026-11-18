# Resumen — Identidad Digital BC 6.0.8 (`com.gobbc.identidaddigital`)

## Identidad
- **App:** Identidad Digital BC — wallet de identidad del **Gobierno de Baja California** (GEBC):
  CURP, licencia de conducir, **tarjeta de circulación digital**, documentos digitales.
- **Package:** `com.gobbc.identidaddigital` · **versión** 6.0.8 (versionCode 51). compileSdk 37.
- **#33** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Arquitectura (en una línea)
Nativa **Kotlin/Java** con patrón **MVP** (contracts + presenters + model + remote.api + ui),
7 175 clases. Retrofit/OkHttp para red, reCAPTCHA Enterprise, Firebase (OTP SMS + push). Sin
Flutter/Unity/.NET/WebView-wrapper → decompilable.

## Módulos propios (del árbol de clases)
- **model:** `Ciudadano`, `Identidad`, `Credenciales`, `Licencia`, `TarjetaCirculacion`,
  `Documento(s)Digitales`, `Usuario`, `TokenSession`, `AuthorizationCredentials`, `reCaptcha.*`.
- **remote.api:** `LoginAPI`, `RegistroAPI`, `DocumentosAPI`, `ImagesApi`, `PhoneVerificationApi`,
  `ReCaptchaInterface` + `ServiceGenerator` (cliente) + `TokenGenerator`.
- **ui.activities:** registro v2, login, licencia digital, tarjeta de circulación, **compartir
  tarjeta**, `ScannQrActivity`/`ScannerActivity` (QR), `SmsFirebaseActivity` (OTP).

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS a dominios oficiales de BC; sin cleartext |
| TLS | ⚠️ Validación por defecto, **sin pinning** — pero **no** trust-all (mejor que EDOMEX) |
| **Logging** | ⚠️ **`HttpLoggingInterceptor` BODY en producción** → PII + tokens a logcat |
| Auth | ✅ CURP + OTP SMS + reCAPTCHA Enterprise |
| Compartir tarjeta | ⚠️ Tokens/QR a terceros — robustez no verificable |
| Higiene build | ⚠️ 7 URLs de entorno + `sandbox` embebidos |
| Firma | ⚠️ Re-firmada por APKPure |

## Lectura
Es una app **bien construida** (MVP limpio, reCAPTCHA, OTP, HTTPS, sin trust-all) cuyo punto
débil concreto es el **logging de cuerpos HTTP en producción**: en una app que transporta CURP,
licencias y tarjetas de circulación, dejar `HttpLoggingInterceptor.BODY` activo filtra PII y
tokens a logcat.
