# 02 · Red y endpoints

Hosts extraídos por grep sobre smali + recursos del árbol apktool, y de
`com.infonavit.data.enviroment.EnviromentRetrofit` (constructor de los servicios Retrofit) y
`com.infonavit.data.RetrofitService`. Clasificados por rol. Todas las bases productivas son
`https://`; el tráfico en claro está bloqueado por el network security config (doc 03), pero la
validación TLS está deshabilitada (doc 03, H1).

## Backends productivos de INFONAVIT

| Base URL | Uso (método en `EnviromentRetrofit`) |
|---|---|
| `https://serviciosweb.infonavit.org.mx` | API principal de datos/perfil (`getApiServiceDP`, `getApiServiceJax`, `getApiServiceCesi`) |
| `https://serviciosweb.infonavit.org.mx:8892` | mismo gateway, puerto alterno (`getApiServiceDP_Port`, `getApiServiceMAP` rama interna) |
| `https://micuenta.infonavit.org.mx` | Portal MCI / WebSphere Portal (`getApiServiceMCI`), login web `…/wps/portal/mci2/login/` |
| `https://map.infonavit.org.mx` | servicio MAP (precalificación / Mi Ahorro Puntos) |
| `https://gtwservices.infonavit.org.mx:8065` | gateway de servicios (puerto 8065) |

`wps/portal` y `wps/wcm` revelan **IBM WebSphere Portal / WCM** como backend del portal MCI.

## Hosts QA / internos filtrados en el build de producción (H4)

| Host | Dónde aparece |
|---|---|
| `https://portalmciqa.infonavit.org.mx` | rama `!isProductiveEnviroment()` de `getApiServiceMCI` + deeplink de registro |
| `https://servicioswebinternoqa.infonavit.org.mx` | rama no productiva de `getApiServiceCesi` |
| `https://10.81.1.56:8065` | string embebido (IP privada + puerto gateway) |
| `https://091402AQ249.infonavit.net:8065` | hostname interno de máquina (`.infonavit.net`) |
| `http://10.82.1.41:10039` | deeplink del manifiesto (doc 01) |

`isProductiveEnviroment()` retorna `true` en este build, así que las ramas QA están **muertas en
ejecución**, pero los hosts/IPs quedan embebidos como strings → fuga de topología interna. No se
documenta ningún endpoint QA como "activo"; se reporta su presencia, no su uso.

## SOAP / JAX-WS (servicios legados)

Namespaces y referers `http://…` que son **URIs de namespace SOAP**, no necesariamente endpoints
HTTP en claro:

- `http://mci2-registro.jaxws.infonavit.org.mx` (JAX-WS de registro)
- `http://servicio.consultaCesi.infonavit.org.mx`, `http://ws.consultaDirectorioCesi.infonavit.org.mx/`
- `http://infonavit.org.mx/GENERICO/PORTAL/sndConsultaDocumentos` y `…/sndBusquedaOrdenGenerico`
  → usados como **`Referer`** en `RetrofitService` (`REFERER_CONSULTA_DOCUMENTO`, `REFERER_CASO`).

La app usa SimpleXML (`org.simpleframework`) como convertidor Retrofit para estos servicios XML.

## Integraciones de gobierno / terceros

| Host | Para qué |
|---|---|
| `services.wserv.ecurp.dgti.segob.gob.mx` | CURP / RENAPO (SEGOB) |
| `serviciosdigitales.imss.gob.mx/gestionAsegurados-web-externo/asignacionNSS` | NSS del IMSS |
| `www.sat.gob.mx/…/verifica-si-estas-registrado-en-el-rfc`, `www.gob.mx/curp/` | enlaces SAT/CURP (web) |
| `micreditoinfonavit-15f0d.firebaseio.com` | Firebase Realtime Database (reglas no verificadas) |
| `app-measurement.com`, `firebase.google.com`, `www.googleapis.com` | Firebase/Google analytics |
| `metrics*.hicloud.com` / `dbankcloud.cn` | telemetría Huawei HMS |
| `adquiramexico.com.mx/clb/endpoint/infonavit`, `barri.com`/`barrimoneytransfer.com`, `intercambioexpress.com`, `maxillc.com` | enlaces/validadores de terceros (remesas/comercios); aparecen pocas veces, probablemente de un SDK o catálogo de enlaces, no del core |
| `infonavitfacil.mx/pensiones`, `cdn.infonavitservicios.com.mx` | CDN / micrositios |
| `portalmx.infonavit.org.mx/wps/…` | términos, aviso de privacidad, PDFs públicos |

## Credenciales de transporte

`RetrofitService` define constantes de usuario/clave para Basic Auth, cola MQ y servicio de pago,
además de `X_API_KEY_REGISTRO`/`X_API_FLAG_REGISTRO` y `USER_AGENT`. Son credenciales de
**servicio** embebidas en el cliente → ver [04-autenticacion-credenciales.md](04-autenticacion-credenciales.md).

> Nota: el reparto exacto base-URL↔feature se infiere de los nombres de método de
> `EnviromentRetrofit` y los `const-string` adyacentes; el enrutado real en runtime no se ejecutó.
