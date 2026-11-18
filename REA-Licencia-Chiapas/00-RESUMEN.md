# 00 · Resumen ejecutivo — Licencia Digital Chiapas

## Identidad

| Campo | Valor |
|---|---|
| App | Licencia Digital Chiapas |
| Package | `mx.com.cosmocolor.movapp.chp` (**verificado** con `inspect_android_package` antes de documentar) |
| versionName / versionCode | 1.1.1 / 6 |
| minSdk / targetSdk | 29 / 36 |
| Firma | **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica**; cruzar con el canal oficial del Gobierno de Chiapas |
| SHA-256 cert (re-firma) | `7fda6e8a3777d88c8ad69c0eab9749ae51e7e29ae809d98ee55c2896fcd4648e` |
| sha256 XAPK | `316ccab53fc4061d6062e6aef721a9769176db5134ae1ac7c0a47f7596678216` |
| Clases | 8 892 |
| Dueño | Gobierno del Estado de Chiapas (producto del proveedor **Cosmocolor**) |

## Pila tecnológica

- **Nativa Android (Kotlin/Java, R8 ofuscado)** — Retrofit2 + OkHttp3 + Gson. Biometría facial
  (cámara), NFC, Bluetooth.
- Es el **mismo producto Cosmocolor** que Licencia Digital EDOMEX (`mx.com.cosmocolor.movapp`) y
  Jalisco (`.jal`): misma clase `mx.com.cosmocolor.cameralibrary.webservice.ServiceGenerator`, mismo
  `Service` + `interceptors.ConnectivityInterceptor`/`HeaderInterceptor`, mismo backend
  `licenciamanejo.com`.

## Hallazgo principal — confirma el clúster B (Cosmocolor multi-estado)

**Chiapas es el 3er estado Cosmocolor confirmado** (antes solo residuo en strings de EDOMEX/Jalisco).
Evidencia: backend `licenciamanejo.com` idéntico + **residuo cross-estado de EDOMEX dentro del build
de Chiapas** (`https://api2.licenciamanejo.com/edomex/web/noticias/edomex`,
`https://prod.cosmokeys.edomex.licenciamanejo.com`), espejo de cómo EDOMEX traía residuos de Chiapas.

## Hallazgos (tabla)

| # | Hallazgo | Clase | Evidencia |
|---|----------|-------|-----------|
| H1 | **3er estado del producto Cosmocolor** (confirma multi-estado) | — | **CONFIRMADO** — misma clase/backend + residuo EDOMEX |
| H2 | **Trust-all TLS del producto** (como EDOMEX/Jalisco) | CWE-295 | **A-CONFIRMAR** — ver abajo |
| H3 | Cleartext | CWE-319 | **AUSENTE-SANO** — mejor higiene que EDOMEX |
| H4 | Proveniencia | — | re-firmada APKPure (la firma no prueba autenticidad) |

### H2 — Trust-all: A-CONFIRMAR (no inflar)

Las primitivas del trust-all están en el DEX (`X509TrustManager`, `HostnameVerifier`, `SSLContext`,
`checkServerTrusted`, `getAcceptedIssuers`), **pero** también son parte de OkHttp estándar, así que su
presencia **no basta** para afirmar trust-all. El `OkHttpClient` se arma en el **Companion de
`ServiceGenerator` (R8-ofuscado)**, y **no se pudo leer un `checkServerTrusted` vacío en este
binario**. Dado que es, estructura por estructura, el mismo producto que EDOMEX/Jalisco —donde el
trust-all está **CONFIRMADO**— es **muy probable** que lo herede, pero aquí queda **A-CONFIRMAR**:
requeriría desofuscar el builder o un pase dinámico. No se afirma confirmado.

### H3 — Cleartext: AUSENTE-SANO

`android:usesCleartextTraffic="false"` + `network_security_config.xml` con
`cleartextTrafficPermitted="false"` para `licenciamanejo.com` (includeSubdomains). **Mejor que
EDOMEX**, que sí tenía `http://impresion.licenciamanejo.com`. Aquí los endpoints son HTTPS.

## Endpoints (todos HTTPS)

- `https://api1.licenciamanejo.com/multi/appmov/` (API principal)
- `https://api2.licenciamanejo.com/edomex/web/noticias/edomex` (**residuo EDOMEX**)
- `https://faceapi.licenciamanejo.com/` (verificación facial)
- `https://impresion.licenciamanejo.com/objetosImpresionApi/` (impresión)
- `https://prod.cosmokeys.edomex.licenciamanejo.com` (**residuo EDOMEX**)
- `chiapas-verifica-infraccion-ty3qpufnqq-uc.a.run.app` + `sspc.chiapas.gob.mx` (propios de Chiapas)

## Permisos sensibles

`CAMERA`, `ACCESS_FINE_LOCATION`, `RECORD_AUDIO`, `NFC`, `BLUETOOTH` — coherentes con licencia +
verificación facial.

## Cruce con el mapa

- **Clúster B (Cosmocolor multi-estado):** sube de 2 a **3 apps confirmadas** (EDOMEX, Jalisco,
  Chiapas). El informe `divulgacion/02` (dirigido al proveedor) ya cubre el defecto de producto;
  Chiapas lo refuerza.
- **Clúster A (trust-all):** Chiapas queda como **A-CONFIRMAR** (no se suma a los confirmados hasta
  leer el builder o dinámico).
