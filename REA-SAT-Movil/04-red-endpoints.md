# Red y endpoints

Este es el documento con más valor del análisis. A diferencia de CFE Contigo —que cifró sus
endpoints con DexGuard y no se pudieron recuperar en estático— **SAT Móvil los envía en texto
plano** en tres ficheros `.env` dentro del APK, y el resto aparece como literales en `libapp.so`.

## 1. Los ficheros `.env` (en claro, dentro de `flutter_assets/`)

La app usa **flutter_dotenv** y en runtime carga **`.env.production`** (confirmado: la cadena
`.env.production` es la única referenciada en `libapp.so`). Los tres se empaquetan en el binario.

### `.env.production` (el que se usa)

```
#Configuración Oauth 2.0.
OAUTH_TOKEN="https://sm-sat-movilzuul-sm-servicios-moviles.cnh.cloudb.sat.gob.mx/satmovil/v1/smnovel?"
GRANT_TYPE="password"
SCOPE="satmovil"
RESOURCE_SERVER="satmovil"
GRANT_TYPE_REFRESH="refresh_token"
BASE_API_YOUTUBE="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
SAT_MOVIL_BASE="https://sm-sat-movilzuul-sm-servicios-moviles.cnh.cloudb.sat.gob.mx/satmovil/v1"
SAT_ID_BASE="https://chiikul.sat.gob.mx/api/v1"
SAT_ID_CAPTCHA="https://captcha.sat.gob.mx/get_captcha"
```

### `.env.uat` y `.env.dev` (latentes pero embarcados)

```
# .env.uat
SAT_MOVIL_BASE="https://sm-sat-movilzuul-sm-servicios-moviles.cnh.uat.cloudb.sat.gob.mx/satmovil/v1"
SAT_ID_BASE="https://13.85.190.35:3002/api/v1"
SAT_ID_CAPTCHA="https://13.85.190.35:3001/get_captcha"

# .env.dev
OAUTH_TOKEN="https://www.dev.cloudb.sat.gob.mx/satmovil/v1/smnovel?"
SAT_MOVIL_BASE="https://www.dev.cloudb.sat.gob.mx/satmovil/v1"
SAT_ID_BASE="https://13.85.190.35:3002/api/v1"
SAT_ID_CAPTCHA="https://13.85.190.35:3001/get_captcha"
```

## 2. Hallazgo: fuga de infraestructura interna

| Entorno | SAT ID base | Nota |
|---|---|---|
| producción | `https://chiikul.sat.gob.mx/api/v1` | Host interno con nombre de fantasía |
| UAT | `https://13.85.190.35:3002/api/v1` | **IP pública de Azure** (rango Microsoft), puerto no estándar |
| dev | `https://13.85.190.35:3002/api/v1` | Misma IP; captcha en `:3001` |

`13.85.190.35` pertenece a Microsoft Azure. Exponer un servidor de UAT/dev **por IP cruda con
HTTPS en puertos altos (3001/3002)** implica casi con certeza un **certificado que no casa con la
IP** (self-signed o para otro CN) y, por tanto, validación TLS relajada en esos entornos (ver
§4). Aunque el `.env` activo es el de producción, estos valores **viajan en la app pública** y
revelan topología interna que no debería salir de la red del SAT. **Recomendación:** no empaquetar
`.env.uat`/`.env.dev` en el artefacto de tienda; inyectar la config por entorno en build-time.

## 3. Gateway y arquitectura de servicios

- **Zuul (Netflix OSS):** el host `sm-sat-movilzuul-sm-servicios-moviles.cnh.cloudb.sat.gob.mx`
  nombra explícitamente *zuul* → hay un **API gateway Zuul** enrutando a microservicios
  (`sm-servicios-moviles`). `cnh.cloudb.sat.gob.mx` es la nube "B" del SAT; `clouda` es otra.
- **Stack de red (confirmado en runtime, doc 11): Cronet (Chromium 139 mainline).** Aunque en Dart
  se usan `dio`/`http`, el transporte real en el dispositivo es **Cronet** (`libmainlinecronet.
  139.0.7205.3.so` + su BoringSSL propio). El gateway de producción se resolvió **9 veces** en vivo
  durante navegación pública. Los cuerpos cifrados no se capturaron porque Cronet usa su BoringSSL
  interno (ver doc 11).
- **Base de API móvil:** `.../satmovil/v1` con rutas observadas en `libapp.so`:
  `/satmovil/v1/events/` (telemetría/eventos), más `/dwh/efirma/`, `/dwh/digitalcertificate/`,
  `/dwh/miinformacion/` (DWH = data warehouse del contribuyente), `/opinionc/` (opinión de
  cumplimiento), `/certificates.json`.

## 4. Endpoints observados (tabla completa, de `libapp.so` + `.env`)

| Host | Para qué | Fuente |
|---|---|---|
| `sm-sat-movilzuul-sm-servicios-moviles.cnh.cloudb.sat.gob.mx/satmovil/v1` | API móvil principal (vía Zuul): token, eventos, DWH, opinión | `.env` + libapp |
| `chiikul.sat.gob.mx/api/v1` | **SAT ID** (verificación de identidad) | `.env` |
| `captcha.sat.gob.mx/get_captcha` | Captcha propio | `.env` |
| `satid.sat.gob.mx` | Portal web SAT ID | libapp |
| `anualpf.clouda.sat.gob.mx` | **Declaración Anual Personas Físicas** | libapp |
| `citas.sat.gob.mx` | **Citas** en oficinas | libapp |
| `chat.sat.gob.mx` | **Chat**/orientación | libapp |
| `aplicacionesc.mat.sat.gob.mx/.../denunciaSatMovil.aspx` | **Denuncias** (ASP.NET) | libapp |
| `verificacfdi.facturaelectronica.sat.gob.mx/verificaccp/` | **Verificación CFDI / carta porte** | libapp |
| `siat.sat.gob.mx/app/qr/.../validadorqr.jsf` | **Validador QR** (JSF) | libapp |
| `pstcdypisr.clouda.sat.gob.mx`, `ptscdecprov.clouda.sat.gob.mx` | Portales de servicios/declaraciones (clouda) | libapp |
| `wwwmat.sat.gob.mx/iniciar-expediente/medios_contacto/` | Expediente / medios de contacto | libapp |
| `wu1agsprosta001.blob.core.windows.net` | **Azure Blob Storage** — hosting de contenido/assets | libapp |
| `www.googleapis.com/youtube/v3/playlistItems` | Videos educativos (YouTube Data API) | `.env` + libapp |
| `i3.ytimg.com`, `www.youtube-nocookie.com` | Miniaturas / reproductor YouTube | libapp |

## 5. Seguridad de transporte

- **Producción es HTTPS** en todos los hosts `*.sat.gob.mx`. Sin `networkSecurityConfig` propio
  (doc 01) → confía en el truststore del sistema.
- **Sin certificate pinning.** No se hallaron pins (`sha256/…`), ni `setTrustedCertificates`, ni
  callbacks `onBadCertificate` del app. Los únicos símbolos TLS en `libapp.so`
  (`SecurityContext`, `SecureSocket_RegisterBadCertificateCallback`, `TrustBuiltinRoots`) son del
  **runtime Dart (`dart:io`)**, no lógica de pinning del SAT. → El tráfico es interceptable con un
  CA de usuario/MITM en un dispositivo propio (no se hizo).
- El paquete `fallback_root_certificates` aparece en el SBOM: probablemente provee un bundle de
  CA raíz adicional (hipótesis: para aceptar el cert de los entornos por IP). **Pendiente** de
  confirmar.

## 6. Contraste con CFE Contigo

| | CFE Contigo | SAT Móvil |
|---|---|---|
| Endpoints en estático | **No recuperables** (DexGuard) | **Todos en claro** (`.env` + Dart literals) |
| Gateway | OkHttp directo | **Zuul** |
| Pinning | (no determinado; `.so` packed) | **Ninguno** |
| Fuga de infra | — | **IP Azure UAT/dev hardcodeada** |
