# 03 — Red: Retrofit 1.x, catálogo de API y flujo de subida

## Pila de red

- **Square Retrofit 1.x** — se observan `retrofit.RestAdapter`, `retrofit.RestAdapter$Builder`,
  `retrofit.mime.TypedFile`, `retrofit.mime.TypedString`, `retrofit.converter.GsonConverter`,
  `retrofit.RequestInterceptorTape`, `retrofit.Endpoints$FixedEndpoint`. Es la **generación 1** de
  Retrofit (paquete `retrofit.*`, no `retrofit2.*`), **en fin de vida desde ~2016**.
- **RxJava 1** (`rx.Observable`) como modelo asíncrono (todos los métodos devuelven `rx.Observable`).
- Converter JSON: **Gson**.

> Retrofit 1.x no recibe parches de seguridad desde hace años. No es una vulnerabilidad directa por
> sí sola, pero es deuda técnica relevante en una app que transmite PII electoral (hallazgo P3).

## Dos capas: `EndpointCatalog` y `SessionService`

- `mx.ine.apoyociudadano.endpoints.SessionService` — **interfaz Retrofit** (los métodos HTTP reales;
  parámetros `TypedString`/`TypedFile` = multipart). Decompilada en
  [`decompiled/SessionService.metodos.txt`](decompiled/SessionService.metodos.txt).
- `mx.ine.apoyociudadano.endpoints.EndpointCatalog` — **fachada** sobre la interfaz (18 métodos,
  17 inner classes `a`..`q` que son los `Func`/callbacks RxJava). Decompilada en
  [`decompiled/EndpointCatalog.metodos.txt`](decompiled/EndpointCatalog.metodos.txt).

## Catálogo de operaciones (decompilado)

| Operación | Entrada relevante | Devuelve |
|---|---|---|
| `logIn` | 11 strings (credenciales del auxiliar) | `LoginResponse` |
| `logInComplemento` | **File** + 20 campos | `LoginResponse` |
| `loginAutoservicio` | 5 strings | `LoginAutoservicioResponse` |
| `generaCaptcha` | `DatosGeneraCaptcha` | `RespuestaGeneraCaptcha` |
| `validaOTP` / `generarCodigoOTP*` | datos OTP | `RespuestaValidaOTP` / `RespuestaStatus` |
| `getLlave` | — | `GetLlaveResponse` (**llave de cifrado desde el servidor**, ver doc 04) |
| `generaCodigo` | **File** (foto) + 14 campos | `GenerarCodigoResponse` |
| `uploadZip` | **TypedFile (ZIP)** + 15 campos | `UploadResponse` |
| `uploadFileHttp` | **File** + long + varios | `UploadResponse` |
| `consultaEnviados` | folio/credenciales | `ConsultaAvanceResponse` |
| `synchronize` | `List<String>` | `List<LoginResponse>` |
| `checkAuxiliar` | `List<CheckProcessInfo>` | `List<CheckProcessInfo>` |
| `bajaDispositivo` | credenciales + motivo | `BajaDispositivoResponse` |
| `validarCodigo` / `reload` | — | `ValidaCodigoResponse` / `LoginResponse` |

## Flujo de subida del paquete ciudadano

El dato central: **`SessionService.uploadZip(TypedFile, TypedString×15)`** sube un **ZIP** (el
"paquete ciudadano": fotos de credencial + OCR + foto en vivo + firma + metadatos). Alternativas con
archivo: `generaCodigo(TypedFile, …)` y `loginComplemento(TypedFile, …)`. Es multipart sobre HTTPS
(la política de red por defecto no permite cleartext, doc 01).

> **Implicación de privacidad (P1):** el payload es PII electoral de **terceros** (el ciudadano que
> respalda), no del usuario de la app. La credencial para votar fotografiada + OCR + biometría facial
> + firma salen del dispositivo hacia el backend del INE.

## Endpoints

Las **URLs del backend no están en claro** en el DEX: viven **cifradas** en `assets/config.properties`
(`BASE_PRE`, `BASE_PRE_SEGURIDAD`, `BASE_PRE_C3`, `PRODUCTION`, `DEVELOPMENT`, variantes
`*_DESA_*`/`*_DEV_*`). Ver doc 04 para el mecanismo. En el DEX en claro solo aparecen dominios de
terceros (Facebook Graph, Firebase/Firestore, Google, recaptcha) — ver doc 06.

## TLS / validación de certificado

Se observan referencias a `android.net.http.X509TrustManagerExtensions` (el helper estándar de
Android para **certificate pinning**), `org.apache.http.conn.ssl.StrictHostnameVerifier` (verificador
de hostname estricto), `TrustManagerFactory`/`getTrustManagers`. **No** se observan strings de
trust-all (`trustAllCerts`, `allowAllHostnames`) ni `checkServerTrusted` vacío.

> **Lectura honesta:** los indicadores apuntan a validación TLS **estricta y posiblemente con
> pinning** (`X509TrustManagerExtensions` existe precisamente para eso). Pero **no se aisló la clase
> que construye el cliente ni los pines**, así que no se afirma el pinning como hecho — solo que no
> hay señales de la anti-práctica trust-all y sí señales de validación reforzada.
