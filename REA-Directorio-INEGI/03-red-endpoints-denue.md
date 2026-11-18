# Red, endpoints y telemetría

## Endpoints propios del INEGI (HTTPS)

Recuperados del DEX (código propio ofuscado):

```
https://www.inegi.org.mx/app/mapa/denue/default.aspx   # mapa/consulta del DENUE
https://www.inegi.org.mx/temas/directorio/             # sección del directorio
```

- Ambos **HTTPS** y en el dominio oficial **`www.inegi.org.mx`**. El contenido (establecimientos)
  es **dato abierto**, no PII.
- No se hallaron endpoints `http://` propios en el DEX (los `http://goo.gl/...` son enlaces
  cortos de Google, ya deprecados, no API).

## Terceros (telemetría / publicidad)

| Dominio / lib | Rol |
|---|---|
| `com.google.android.gms.analytics` + `www.google-analytics.com` / `ssl.google-analytics.com` | **Google Analytics** (GMS, **deprecado**) |
| `pagead2.googlesyndication.com/pagead/gen_204` | **publicidad** (AdMob/AdSense) |
| permiso `AD_ID` | identificador de publicidad |

→ La app incluye **ads + analítica** de Google. En una app de **estadística federal** esto es
una observación de **privacidad** (recolección de identificadores con fines de medición/anuncios),
no una vulnerabilidad. El tráfico de telemetría/ads va a los dominios de Google por HTTPS.

## Postura TLS (positivo)

Confirmado por barrido del DEX — **no** hay:
- `getUnsafeOkHttpClient` / `TrustManager` con `checkServerTrusted` vacío / `trustAllCerts`,
- `HostnameVerifier` permisivo (`ALLOW_ALL_HOSTNAME` / null verifier),
- `usesCleartextTraffic="true"`, ni `networkSecurityConfig` laxo.

→ La validación TLS es la **por defecto** del sistema (CAs del sistema). No hay el antipatrón de
Infonavit/Licencia EDOMEX/ProcurApp. **Sin** certificate pinning (aceptable para datos públicos;
un plus sería pinnear `inegi.org.mx`).

## Librería de red
El cliente HTTP exacto (OkHttp/Volley/HttpURLConnection) queda en helpers ofuscados; no se aisló
el nombre. No cambia la conclusión de transporte (HTTPS sin bypass). Marcado como límite.
