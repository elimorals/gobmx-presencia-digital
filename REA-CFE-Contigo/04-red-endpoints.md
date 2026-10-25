# Red y endpoints

## Cliente HTTP

- **OkHttp** confirmado: `Lokhttp3/internal` aparece en el DEX. Las clases públicas
  (`okhttp3.OkHttpClient`, `retrofit2.*`) **no** aparecen por nombre en el inventario
  top-level de JADX → están renombradas dentro del paquete ofuscado `o.*` (R8/DexGuard).
- Headers y tipos presentes como literales: `Authorization`, `Basic `, `Content-Type`,
  `application/json`. Consistente con OAuth2: `Basic` en el token endpoint, `Bearer` (el
  literal "Bearer " queda dentro de strings cifrados) en las llamadas de negocio.

## TLS y cleartext

- `android:usesCleartextTraffic="false"` en el manifiesto.
- **Sin `networkSecurityConfig` propio** → aplica el default de la plataforma. Con
  `targetSdkVersion 36`, el default **bloquea todo HTTP en claro**.
- No se detectaron dominios dev/QA ni `localhost` reales de CFE en claro (el único `localhost`
  hallado es de librerías). Contrasta con jw-library, que sí tenía `dev-library.jw.org` con
  cleartext y dominios `-qa` en el release.

## Backend real (confirmado dinámicamente): `appcfecontigo.cfe.mx`

> **Actualización por análisis dinámico** (ver `10-analisis-dinamico.md`). Lo que DexGuard
> ocultó estáticamente se obtuvo capturando el tráfico de la app en un dispositivo real:
> el backend propio de CFE es **`https://appcfecontigo.cfe.mx`** (puerto 443). Es el **único**
> dominio propio al que llama al arrancar; el resto del tráfico es a Firebase/Google y
> telemetría. No se observó ningún endpoint `http://` en claro (coherente con
> `usesCleartextTraffic="false"`). Las **rutas** (`/api/...`) siguen sin verse: requieren
> romper TLS (CA de sistema = root), fuera del alcance.

## Endpoints: NO recuperables estáticamente (sí dinámicamente, ver arriba)

La búsqueda exhaustiva de URLs/hosts sobre `classes*.dex` **no devolvió ningún dominio de
backend de CFE** por el cifrado de strings. Lo único que aparece son dominios de terceros:

| Host | Origen |
|---|---|
| `cfe-contigo.firebaseio.com` | Firebase Realtime Database (en `strings.xml`) |
| `app-measurement.com` | Firebase Analytics |
| `h-sdk.online-metrix.net` | LexisNexis ThreatMetrix (anti-fraude) |
| `firebaseio.com`, `googleapis.com`, `*.hicloud/dbankcloud` | Firebase / Huawei |

**Causa raíz** (ver doc 05): los literales de URL del backend propio están **cifrados por
DexGuard** (patrón `$$a: byte[]` + `$$c()` que los descifra en runtime). No viven como
strings planos en el DEX, por lo que `strings`/grep no los ve. Tampoco están en la librería
nativa `libb1bd.so` (empaquetada, sin strings legibles).

### Qué haría falta para obtenerlos

- **Instrumentación dinámica** (hook a `okhttp3.Request.url()` o a `$$c()`), sobre un
  dispositivo/emulador propio, para capturar las URLs ya descifradas en memoria.
- **Intercepción TLS** (mitmproxy + CA de sistema) para observar el tráfico real.

Ambas quedan **fuera del alcance** de este análisis estático.

## Forma de la API (inferida de los DTOs)

Aunque no hay URLs, los ~90 DTOs `data.*` revelan la superficie del backend:

| Dominio | DTOs representativos |
|---|---|
| Auth | `AuthResponse`, `TokenRequestDto/ResponseDto`, `Otp*Dto`, `Captcha*` |
| Servicios/recibos | `ServiceInfoResponse`, `Servicio`, `Factura`, `FacturaServicio`, `Voucher` |
| Pagos/tarjetas | `CardDto`, `CardBinDto`, `PaymentSuccess`, `CardsDto` (ver doc 06) |
| Contratación | `ContractRequest`, `DatosContratanteDto`, `DatosFiscalesDto`, `DomicilioInstalacionDto`, `IdentificacionDto`, `FirmaDto` |
| Citas CAC | `AgendarCitaRequestDto`, `DisponibilidadResponseDto`, `HorarioDisponibleDto`, `CitaAgendadaDto`, `CentroCACDto` |
| Reportes/quejas | `DenunciaRequest`, `QuejaTrabajador*Dto`, `SeguimientoNoLuz`, `SituacionNoLuzDto`, `OrdenConexion` |
| Fiscal | `RegimenFiscalDto`, `UsoCfdiDto`, `DatosFiscalesDto` |
| Catálogos | `ColoniaDto`, `CountryCodeDto`, `Estado/Municipio` (vía trámites) |

Esto describe un backend REST con JSON para: autenticación, consulta de recibos y facturas,
pagos con tarjeta tokenizada, contratación de servicio nuevo con firma y datos fiscales,
agendado de citas, y reportes de falla / denuncias.
