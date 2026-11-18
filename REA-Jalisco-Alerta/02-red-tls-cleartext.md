# 02 · Red, TLS y cleartext

## Endpoints (extraídos del bundle Hermes por `strings`)

| Host | Rol | Esquema |
|---|---|---|
| `api.edl.jaliscoalerta.com` | **API principal** de la app | HTTPS |
| `edl.jaliscoalerta.com` | portal EDL | HTTPS |
| `blog.jaliscoalerta.com` | blog/avisos | HTTPS |
| `aire.jalisco.gob.mx` | calidad del aire (dato gob oficial) | HTTPS |
| `airejalisco.digital` | calidad del aire (dominio alterno) | HTTPS |
| `erlia.mx` | aviso de privacidad / FAQ del **proveedor Erlia** | HTTPS |
| `exp.host`, `classic-assets.eascdn.net` | Expo push / EAS CDN | HTTPS |

> El dominio de producto `jaliscoalerta.com` y el `erlia.mx` son del **proveedor Erlia**, no de un
> dominio `gob.mx`. Solo `aire.jalisco.gob.mx` es dominio oficial del estado. Esto es relevante para
> proveniencia de datos (dónde vive la info de alertas de ciudadanos).

## Cleartext (CWE-319) — **AUSENTE / SANO**

- El manifest **no** declara `usesCleartextTraffic` ni `networkSecurityConfig`.
- targetSdk **36** → Android bloquea cleartext por defecto.
- No se halló ningún endpoint `http://` de backend en el bundle (el único `http://…` en strings es
  ruido de concatenación Hermes pegado a nombres de iconos, no un host real).
- **Veredicto:** a diferencia de **911-CDMX** (cleartext=true, 30+ http://), Jalisco Alerta **no**
  repite ese patrón. CONFIRMADO por estático.

## TLS trust-all (CWE-295) — **SIN EVIDENCIA**

- No se halló en el bundle ningún marcador de desactivación de validación (`trustAll`,
  `rejectUnauthorized:false`, `allowAllHostnames`, pinning inseguro de `react-native-ssl-pinning`).
- React Native usa OkHttp con validación por defecto. No se identificó código nativo propio que la
  desactive.
- **Límite honesto:** la validación TLS efectiva de OkHttp no se puede confirmar al 100% por
  estático sobre Hermes; pero **no hay evidencia de trust-all**. Marcado AUSENTE (sin evidencia de
  defecto), no "confirmado seguro".
