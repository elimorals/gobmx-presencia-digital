# 05 — Red y endpoints

## Dónde viven los endpoints

A diferencia de muchas apps, la base de API **no** está como const-string en el DEX: vive en
`res/values/strings.xml` (referencias de recurso resueltas en runtime). Un `grep https://` sobre
los 28 DEX solo devuelve URLs de librerías (Firebase, Google, Dynatrace), nunca el host de Procesar.

## Backend del SAR — `api.esar.io` (Procesar / e-SAR)

| String (`strings.xml`) | Valor | Rol |
|---|---|---|
| `server_url` | `https://api.esar.io:443/aforemovil/` | **Base de servicios** de la app |
| `url_api_login` | `https://api.esar.io/sartoken/` | Login / token SAR |
| `url_api` | `https://api.esar.io/aweb/sartoken/` | API autenticada (aweb) |

`esar.io` corresponde a **e-SAR**, infraestructura de Procesar (la empresa operadora de la Base de
Datos Nacional SAR). Puerto 443 explícito y rutas `/aforemovil/`, `/sartoken/`, `/aweb/`.

## Telemetría y analítica (tres destinos)

| Destino | URL / evidencia | Qué envía |
|---|---|---|
| **Dynatrace** (APM) | `https://bf31153cbh.bf.dynatrace.com/mbeacon`; `BuildConfig.DYNATRACE_URL_DEV/PROD`, `DYNATRACE_ID_DEV/PROD`; instrumentación `Callback.onCreate` | Rendimiento, trazas, errores de la app |
| **TrustX** (Daon, biometría) | `analytics_base_url=https://mobilemetrics.%1$s.trustx%2$s.com/api/mobile-metrics-global`; evento `/v2/mobileCaptureSessionEvent` | Métricas de la **sesión de captura** biométrica |
| **Firebase** | `firebase_database_url=https://procesar-d4477.firebaseio.com`; Crashlytics/Analytics/Sessions | Crashes, analytics, FCM push |

Proyecto Firebase: **`procesar-d4477`**. La app trae **IDs/URL de Dynatrace separados para DEV y
PROD** embebidos en `commons.BuildConfig` (el flavor elige cuál usar).

## Enlaces de salida a gob.mx / IMSS (informativos, abren navegador)

| String | URL |
|---|---|
| `url_message_consult_nss` | `https://serviciosdigitales.imss.gob.mx/gestionAsegurados-web-externo/asignacionNSS` |
| `url_message_consult_curp` | `https://www.gob.mx/curp/` |
| `url_consar` | `https://www.gob.mx/consar/` |
| `url_calculator_info` | `https://www.gob.mx/consar/acciones-y-programas/calculadoras-de-ahorro-y-retiro` |
| `url_message_consult_irn` | `https://www.gob.mx/consar/articulos/indicador-de-rendimiento-neto` |
| `url_help_goals` | `https://laaventurademivida.gob.mx/` |
| `url_calculator_work` | `https://www.aforemovil.com/calculadora` |
| `url_delete_user` | `https://www.aforeweb.com.mx/cuenta-suspension` |

## TLS, cleartext y pinning

- `android:usesCleartextTraffic="false"` → sin HTTP en claro.
- `res/xml/network_security_config.xml` **solo** contiene `<debug-overrides>` que confían CA de
  usuario en builds depurables:
  ```xml
  <network-security-config>
    <debug-overrides>
      <trust-anchors><certificates src="user" /></trust-anchors>
    </debug-overrides>
  </network-security-config>
  ```
- **No hay `<pin-set>`** y **no existe `okhttp3.CertificatePinner`** entre las 17,984 clases →
  **sin certificate pinning**. En release se usa el almacén de CA del sistema sin fijación.

**Lectura de seguridad.** Un atacante con una CA de confianza del sistema (dispositivo propio o
rooteado con CA instalada) podría interceptar el tráfico a `api.esar.io` por MITM. Para una app
financiera, la ausencia de pinning es la debilidad de red más relevante. En release los
`debug-overrides` no aplican (la app no es `debuggable`), así que una CA de usuario sola no basta en
un dispositivo no modificado.

## Cliente HTTP

`okhttp3.internal.platform.PlatformInitializer` (androidx.startup) confirma **OkHttp**. No se
recuperó la configuración del cliente (interceptores, timeouts) por análisis estático; requiere
instrumentación. Daon FIDO usa su propio cliente REST (`com.daon.sdk.fido.service.rest.HTTP`) hacia
`IdentityXServices/rest/v1/secured/users` (host configurado por servidor/BuildConfig).

## Pendiente de verificación dinámica

- Esquema y caducidad del token SAR; si hay pinning aplicado por código fuera de NSC/OkHttp.
- Host real de IdentityX/TrustX (plantilla `%s`) y del Dynatrace PROD.
- Cabeceras de autenticación reales (Bearer/propias) contra `api.esar.io`.
