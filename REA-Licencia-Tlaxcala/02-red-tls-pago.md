# 02 · Red, TLS y pago

## Endpoints (del bundle JS legible)

| Host | Rol | Esquema |
|------|-----|---------|
| `applic.smyt.tlaxcala.gob.mx` | **API principal** (baseURL) | **HTTPS** ✓ |
| `applic.smyt.tlaxcala.gob.mx/broadcasting/auth` | auth de canales Pusher | HTTPS |
| `js.pusher.com` / `pusher.com` | SDK Pusher (realtime) | referencia http en libs (uso real wss) |
| `prisma-apps.servehttp.com` | **solo en allowNavigation**, no llamado en el JS | — (DNS dinámico) |
| `banxico.org.mx/.../codi-...` | doc CoDi (informativo) | HTTPS |

`baseURL:"https://applic.smyt.tlaxcala.gob.mx"` — confirmado en el bundle. **No** se hallaron
endpoints `http://` de backend propios.

## TLS

- **H3 · AUSENTE-SANO (CWE-295):** el DEX contiene `onReceivedSslError`/`SslErrorHandler`/`proceed`,
  pero son **símbolos del framework Capacitor/WebView**, no un override de la app que llame
  `proceed()` para aceptar cualquier cert. No hay `getUnsafeOkHttpClient`/trust-all (no es nativo).
  **No** es el defecto de Cosmocolor/Infonavit. (Confirmación total del runtime requeriría dinámico.)
- **H1 · MEDIA (CWE-319 enabler):** `cleartext:true` + `allowMixedContent:true` en Capacitor permiten
  HTTP/mixto en el WebView. Como la API es HTTPS, no se confirma fuga en claro; es una config
  insegura por defecto para una app de licencia/pago.

## Pago

- Patrón **redirect a pasarela hospedada**: `openPaymentUrl(row.gateway_url | row.urlPasarela)` abre
  la URL de la pasarela vía `@capacitor/browser`. **OpenPay** como procesador. El `card_number` que
  aparece en la UI es la tarjeta **guardada/enmascarada**, no captura de PAN crudo en la app.
- **Riesgo de pago bajo** relativo: el PAN no se maneja en el cliente nativo; la tokenización ocurre
  en la pasarela externa. (El flujo exacto de la pasarela queda fuera del APK.)
