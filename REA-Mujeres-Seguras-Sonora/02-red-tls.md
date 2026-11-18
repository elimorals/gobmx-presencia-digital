# 02 · Red, endpoints y TLS

## Endpoints (del bundle Cordova `assets/public/`)

**Activos (HTTPS):**
- `const APIURL = 'https://servicios.sspsonora.gob.mx/ms2/api/mujeres/'` — API principal (foto de
  usuaria, registro, etc.: `RegistrarPhotoUsuario`, `DeletePhotoUsuario`, `ActUbicacion.aspx`).
- `url: 'https://servicios.sspsonora.gob.mx/ms/Service/REST.svc/SendLocation'` — envío de ubicación.
- `url: 'https://servicios.sspsonora.gob.mx/mstest2'` — **endpoint de prueba activo** (ver H1).

**Comentados (legacy, NO activos):**
- `//url: 'http://apps.sspsonora.gob.mx/wsMS/Services.svc/SendLocation'` — `SendLocation` por **HTTP
  en claro**, pero **comentado** en el bundle.
- `//url: 'https://servicios.sspsonora.gob.mx/ms'`, `//url: '.../mstest'` — versiones previas.

## TLS / cleartext (honesto)

- El `network_security_config.xml` declara `cleartextTrafficPermitted=true` para un domain-config
  (`includeSubdomains`). **Pero el tráfico activo es HTTPS** — la permisión de cleartext parece
  **vestigial**, dejada para el `SendLocation` http que quedó comentado.
- **No es el caso de Veracruz Mujer Alerta:** allí la alerta/ubicación viajaba activamente por
  HTTP; aquí el http está comentado y lo activo va cifrado. Por eso **H2 es BAJA**, no crítica.
- **Sin trust-all de la app:** las strings `X509TrustManager`/`checkServerTrusted` del DEX son de
  OkHttp/librerías; **no** hay `getUnsafeOkHttpClient` ni desactivación de validación. AUSENTE-SANO.

## Terceros

- **SmiSDK** (video/telemedicina, `s3.amazonaws.com/smisdk-release`), Firebase/FCM, Google OAuth
  (client-ID `915880006269-….apps.googleusercontent.com`, **público por diseño**).
