# Red y SDKs

Recuperado de strings del DEX y de `libapp.so` (el detalle completo vive en el snapshot Dart).

## Endpoints
| URL | Rol |
|---|---|
| `https://ssl1.agua.gob.mx/pagatuagua/documentos/` | backend de pago/recibos (HTTPS) |
| `http://www.aguadehermosillo.gob.mx/` | sitio institucional (link, no API) |
| `https://wa.me/5216623474870`, `wa.me/5216624290000` | contacto WhatsApp |
| Twitter/Facebook `aguahermosillo` | redes de contacto |

## SDKs (DEX)
- **Firebase** (`app-measurement.com` → Analytics; FCM para notificaciones).
- **Google Maps** (`com.google.android.gms.maps`) — ubicación de oficinas/puntos de pago.
- **AdServices** (perms `ACCESS_ADSERVICES_*` + Install Referrer) → **atribución de publicidad**
  (identificadores con fines de anuncios). Observación de privacidad en una app de pago de agua
  (doc 06).

## Transporte / pinning
- Backend de pago HTTPS; sin cleartext propio (salvo el link del sitio).
- **Pinning:** no verificable estáticamente (capa de red en Dart). Límite.
