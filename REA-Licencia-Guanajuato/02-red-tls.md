# 02 · Red, endpoints y TLS

## Endpoints observados (strings de base + `libapp.so`)

| Host | Rol |
|------|-----|
| `licenciadigitalgtoapp.appspot.com` | **backend de la app** — Google App Engine / Firebase |
| `guanajuato.gob.mx/aviso-legal`, `seguridad.guanajuato.gob.mx/aviso-de-privacidad/` | páginas legales |
| `www.recaptcha.net/recaptcha/api3` | reCAPTCHA |
| `vnd.triscape.mx` | tipo MIME de mapas (no es backend) |

El backend de licencias corre en **infra de Google (appspot.com)**, no en un dominio `gob.mx` ni en
el `licenciamanejo.com` de Cosmocolor. Los endpoints concretos de API (rutas, pago, verificación)
están compilados en el **Dart AOT** (`libapp.so`) y no se recuperan en claro por estático.

## TLS / cleartext

- **Sin `android:usesCleartextTraffic`** en el manifiesto.
- **Sin `networkSecurityConfig`.**
- `targetSdk 36` → en Android moderno el cleartext queda **bloqueado por defecto**. → **AUSENTE-SANO.**

## Trust-all (CWE-295) — comparación con Cosmocolor

- **No aplica el defecto de Cosmocolor:** esa familia es nativa Kotlin+Retrofit con
  `ServiceGenerator.createService` que instala un `X509TrustManager` vacío. Aquí la pila es Flutter;
  no hay Retrofit ni `ServiceGenerator`/`getUnsafeOkHttpClient` (grep = 0).
- **A-CONFIRMAR (muro Dart):** en `libapp.so` aparece el símbolo
  `SecureSocket_RegisterBadCertificateCallback`, que es parte **estándar del runtime de Dart/Flutter**
  (presente en toda app Flutter) — **no** es evidencia de que el código acepte certificados malos.
  Confirmar si el cliente Dart desactiva la validación requiere análisis **dinámico** (reflutter/Frida).
  No se afirma trust-all.

## Conclusión del cruce de vendors

**ZTK (Guanajuato) es un producto distinto de Cosmocolor**, con stack (Flutter) e infra (Firebase/GAE)
diferentes, y **sin** el trust-all de Cosmocolor ni cleartext. El defecto Cosmocolor **no** se propaga
vía este vendor.
