# Red y endpoints

**83 endpoints** extraídos de los assemblies .NET descomprimidos (lista completa en
`decompiled/endpoints.txt`). La app habla con una constelación de **microservicios** de IMSS
(`ms<nombre>-…`) desplegados sobre **OpenShift** (`apps.ocp`, `cloudapps`).

> **Nota sobre verificación dinámica**: se instaló IMSS Digital en un dispositivo real
> (Motorola, Android 16, sin root) y se intentó capturar su tráfico con mitmproxy. No se logró,
> por dos razones: (1) la app es **Xamarin/.NET** y su `HttpClient` no respeta el proxy global
> de Android; (2) el `LatestVersionPlugin` detectó que la build de APKPure difiere de la de Play
> y **redirigió a la tienda** antes de llamar a sus APIs. Los 83 endpoints de abajo provienen del
> análisis estático (descompresión de assemblies), que para esta app resultó más completo que la
> captura dinámica. Confirmarlos en vivo requeriría un dispositivo rooteado o repackaging con una
> CA de usuario.

> Advertencia de calidad del dato: los endpoints se extraen de cadenas contiguas en el blob;
> algunas aparecen concatenadas con la siguiente (p.ej. `…/v1/listablancaghttps://cds…`). Las
> rutas base y verbos son fiables; los sufijos pegados se marcaron al depurar.

## ⚠ Endpoints en HTTP (texto claro)

Con `usesCleartextTraffic="true"` (doc 01), estos viajan **sin TLS**:

| Endpoint | Qué expone |
|---|---|
| `http://siap.imss.gob.mx/ServiciosWebIMSSDigital/api/auth/login` | **Login** (credenciales) |
| `http://siap.imss.gob.mx/.../api/rjp/personal/Consultar` | Datos de pensionado (RJP) |
| `http://siap.imss.gob.mx/.../api/rjp/supervivencia` | Prueba de supervivencia |
| `http://siap.imss.gob.mx/.../api/rjp/correo/actualizar` | Cambio de correo |
| `http://chktback.imss.gob.mx/api-login/mschkt-login/v1/login` | Login del módulo ChécaT |
| `http://tspi.imss.gob.mx/maternidad-web-movil/incapacidad/registro` | Incapacidad por maternidad |
| `http://chkt.imss.gob.mx/publico/…` | Recursos ChécaT |

Varios de estos hosts **también** existen en `https://` (p.ej. `tspi`), lo que sugiere
inconsistencia: el mismo servicio referenciado con y sin TLS. El login de pensionados (RJP) por
HTTP es el más grave. Ver doc 08.

## Entornos de prueba en el binario de producción

| Endpoint | Entorno |
|---|---|
| `https://cds-qa.apps.ocp.imss.gob.mx/msnssa-app-movil/…` | **QA** (maternidad, asegurado, COVID) |
| `https://mscmov-version-cmov-qa.apps.qaocp.imss.gob.mx/…` | **QA** (versión/maternidad) |
| `https://facetec-preprod.firmaautografa.com` | **preprod** (biometría facial, doc 04) |

Dejar `-qa`/`-preprod` embebidos en el release es la misma mala práctica vista en jw-library.

## IP pública hardcodeada

`https://201.149.45.42/Chat_Yo_Decido/DIRECTORIO` — un chat ("Yo Decido", salud sexual y
reproductiva) referenciado por **IP directa** en vez de dominio. Fragiliza el pinning/rotación
y suele ser residuo de un despliegue temporal.

## Microservicios de producción (HTTPS) por dominio

| Dominio | Microservicios / función |
|---|---|
| `cds.imss.gob.mx` | **Centro de Datos de Salud**: `mscds-notas` (notas médicas), `mscds-resultados` (laboratorio), `mscds-somatometria`, `mscds-derechohabiente`, `msnssa-app-movil` (asegurado, incapacidades, COVID) |
| `canalmovil.imss.gob.mx` | `mscmov-derechohabiencia-rest` (vigencia, constancias), `mscmov-cmsrevista`, `mscmov-listablanca-opt` |
| `bmt.imss.gob.mx`, `bmtval.imss.gob.mx` | Biometría (`msbmt-enroll`) + OTP SMS (`msotp-registro`) + OAuth (doc 06) |
| `siap.imss.gob.mx` | Pensionados (RJP), login, `ServiciosWebSiap` |
| `tspi.imss.gob.mx` | Maternidad, incapacidad, licencia 140-bis |
| `vfam.imss.gob.mx`, `msvfam-*.imss.gob.mx` | **Detección de violencia familiar**: cuestionarios, grado de violencia, directorio de centros de atención a víctimas, alta de usuario |
| `msvph-consulta-…cloudapps.imss.gob.mx` | Resultados de **VPH** (papiloma) |
| `rpci.imss.gob.mx` | `msreci-reportes`, `msreci-suscripciones` (reportes/notificaciones PDF) |
| `mscmsamid-cms-apmid.cloudapps.imss.gob.mx` | CMS (carrusel, contenidos) |
| `covid-calculadora / testcoronavirus / infofamiliarcovid .imss.gob.mx` | COVID-19 |
| `apopmovil.cloudapps.imss.gob.mx`, `movil.imss.gob.mx`, `rpci` | Apoyo/portal móvil |
| `www.gob.mx/curp`, `…/AltaCambioUMF` | CURP y alta/cambio de clínica (UMF) |

## Observaciones

1. Arquitectura de **microservicios sobre OpenShift**, segmentada por trámite. El patrón de
   nombres `ms<área>-<función>/v<N>/…` es consistente y versionado (v1–v4).
2. El módulo de **violencia familiar** y el de **VPH** manejan datos especialmente sensibles;
   su tráfico (HTTPS) es correcto, pero el login base por HTTP debilita toda la cadena si el
   token se obtiene sin TLS.
3. No se hallaron secretos/API keys en claro junto a los endpoints (solo rutas); la
   autorización se resuelve con tokens OAuth/OTP en runtime (doc 06).
