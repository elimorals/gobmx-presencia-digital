# 03 — Red y endpoints (recuperados del snapshot Dart)

Las URLs viajan como literales en `libapp.so`. Extraídas con `strings` + filtrado
(`evidence/endpoints-libapp.txt`). Todas las APIs de la SSC usan **HTTPS**.

## Backend de la SSC

| Dominio | Uso observado (por la ruta) |
|---|---|
| `api-p.ssc.cdmx.gob.mx` | API principal. Rutas de **reglamento de tránsito**: `/reglamento-transito/public/api/temp/busca_placa/`, `/busca_empresa`, `/elemento_cuadrante/`, `/acreditado_post`, `/api/v1/articulos`; además `/api/v1/discover?proyecto=4&lat=…&lon=…` y `/api/v1/geolocalizacion` |
| `cuadrantes.ssc.cdmx.gob.mx` | `/api/v1/cuadrante/<TOKEN>/` — **token embebido en la ruta** (ver abajo) |
| `cms.ssc.cdmx.gob.mx` | `/api/v1` — CMS de contenidos |
| `ucs.ssc.cdmx.gob.mx:8443` | `/api` — Unidad de Contacto del Secretario (UCS), puerto 8443 |
| `www.ssc.cdmx.gob.mx` | `storage/app/media/...` — PDFs/mp4 públicos (reglamento, avisos de privacidad) |
| `data.ssp.cdmx.gob.mx` | `http://.../AvisosPrivacidad/AVISOS.xlsx` — **único `http://`**, latente (doc 01) |
| `estrados.cdmx.gob.mx`, `facultados.ssc.cdmx.gob.mx` | referenciados |

Terceros: `*.places.api.here.com` (HERE Maps), Google Maps (plugin), redes sociales
(twitter/facebook/instagram/tiktok de la SSC) y `accounts.google.com` (sign-in de Google embebido en
cadenas, probablemente de una librería).

## Secretos hardcodeados en el cliente

Recuperados de `libapp.so` (valores completos en `evidence/secretos-hardcoded.txt`, **no versionado**;
aquí redactados):

1. **Token `Bearer` estático** — `Bearer l1duV4g…` (48+ chars) embebido en el snapshot. Un token de
   portador fijo, igual para todas las instalaciones, es extraíble con `strings` sobre el `.so`.
2. **Token en la ruta de cuadrantes** — `…/cuadrante/s2lbgWk…/` (32 chars) embebido como parte de la
   URL del endpoint de cuadrantes.

**Riesgo:** cualquiera que extraiga `libapp.so` (trivial, está en el APK) obtiene estas credenciales y
puede invocar los endpoints de la SSC autenticándose como la app. "Ocultarlas" en el snapshot Dart es
ofuscación, no protección. No se probaron los endpoints (estático); no se afirma qué autorizan esos
tokens más allá de su presencia (ver doc 08). Priorizado en doc 07.

## Cliente HTTP

El tráfico lo hace el paquete Dart `package:http/` (doc 05), no OkHttp del DEX. La validación/pinning
de TLS, por tanto, se configura en Dart vía `dart:io SecurityContext` (doc 04).
