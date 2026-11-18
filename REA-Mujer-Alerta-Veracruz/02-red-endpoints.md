# 02 · Red y endpoints

## Backend único

| Host | Protocolo | Base | Dueño |
|------|-----------|------|-------|
| `alertagenero.sspver.gob.mx` | **HTTP (cleartext)** | `/alerta/` | SSP Veracruz |

- Endpoint base confirmado en el DEX: **`http://alertagenero.sspver.gob.mx/alerta/`**.
- Es un **backend REST GeneXus** (`/alerta/` = objeto/servicio de la KB). El runtime GeneXus
  (`com.artech`/`com.genexus`) arma las llamadas contra esa base.
- **No se halló variante HTTPS** del mismo host en strings. El tráfico de la alerta de género va en
  claro.

## Otros hosts

Solo dominios de librerías/terceros estándar (login.live.com, login.yahoo.com, twitter.com,
bouncycastle.org, youtube.com) y namespaces XML/SAX — no son backend de la app. Ninguno recibe la
PII de la alerta.

## Capa de red GeneXus (vs SEG-Móvil)

GeneXus genera la capa HTTP bajo `com.artech`/`com.genexus` (OkHttp). En **SEG-Móvil** la misma capa
apunta a HTTPS (Azure AD B2C, `*.seg.guanajuato.gob.mx`). Aquí apunta a **HTTP**. El runtime es el
mismo; lo que cambia es la **URL del servidor configurada en la KB/despliegue** — HTTP en este caso.
Confirma que el cleartext es decisión del despliegue, no del framework.
