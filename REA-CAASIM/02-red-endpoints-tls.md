# 02 · Red, endpoints y TLS

## Pila de red

OkHttp/Retrofit nativo (huella: `assets/PublicSuffixDatabase.list`, maquinaria
`getX509TrustManagerOrNull$okhttp` estándar de OkHttp). Sin capa de red Flutter/RN/.NET.

## Backends (de strings del DEX)

| Host | Esquema | Rol |
|------|---------|-----|
| `servicioscaasim.hidalgo.gob.mx` | **http** y **https** | API de la app (ambos esquemas referenciados) |
| `caasimapps.com` | https | dominio del proveedor de la app |
| `www.adquiramexico.com.mx` | https | **pasarela de pago (Adquira México)** |

## Network Security Config

`res/xml/network_security_config.xml` declara **un `domain-config` con
`cleartextTrafficPermitted="true"`** y **7 entradas `<domain includeSubdomains="true">`**
(3 dominios distintos + variantes http/https):

- `servicioscaasim.hidalgo.gob.mx` ← **dominio de la API**
- `caasimapps.com`
- `www.adquiramexico.com.mx`

El bloque incluye `trust-anchors`/`certificates` (confía en el store del sistema; **sin** pinning y
**sin** trust de CAs de usuario embebidas).

### H1 — Cleartext permitido para la API (CWE-319) · ALTA

La app es de **pagos/consulta de agua** y su NSC **habilita HTTP en claro** para el dominio de la
API (`servicioscaasim.hidalgo.gob.mx`, que aparece referenciado en http y https) y para
`caasimapps.com`. Con `targetSdk 36` el cleartext estaría bloqueado por defecto; **el NSC lo
reabre** para esos dominios. Un atacante de red podría leer/alterar el tráfico que use el esquema
http (datos de cuenta/consumo; según la ruta, inicio de pago).

- **CONFIRMADO** a nivel configuración (NSC + referencias http en el DEX).
- **A-CONFIRMAR** qué llamada concreta usa http vs https (requiere trazar la capa Retrofit; el DEX
  referencia ambos esquemas para el mismo host).

## TLS — trust-all

### H3 — Sin desactivación de validación TLS · AUSENTE-SANO (CWE-295)

Un barrido de strings **no** halló `getUnsafeOkHttpClient`, `checkServerTrusted` vacío,
`trustAllCerts`, `ALLOW_ALL_HOSTNAME` ni `NullHostnameVerifier`. Las únicas apariciones de
`X509TrustManager` son la **maquinaria interna estándar de OkHttp**
(`getX509TrustManagerOrNull$okhttp`, `X509TrustManagerExtensions`). **No** es el antipatrón de
Infonavit/Cosmocolor: CAASIM **no** desactiva la validación TLS.

## Pagos — Adquira México (cruce de proveedor)

La pasarela es **Adquira México** (`www.adquiramexico.com.mx`, HTTPS). Es el **mismo procesador de
pagos que Tesorería CDMX** (que usa `adquiracloud.mx` / `prepro.adquiracloud.mx`). Cruce de
proveedor de pagos entre **CDMX** e **Hidalgo** → clúster C del mapa. El canal de pago en sí es
HTTPS (correcto); el cleartext de H1 es del dominio de la API, no de la pasarela.
