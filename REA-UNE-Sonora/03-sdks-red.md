# SDKs y red

El backend propio vive en el snapshot Dart (muro, doc 02); lo observable desde el DEX son los
**SDKs de terceros** y sus endpoints.

## SDKs (strings del DEX)

| SDK | Endpoint / señal | Rol y nota |
|---|---|---|
| **Sentry** | `sentry.io`, `docs.sentry.io/...` | reporte de errores/crashes. Envía diagnósticos (stack traces, contexto del dispositivo) a un tercero. El DSN de Sentry (que incluye una clave pública de ingestión) se configura en el snapshot Dart; no se recuperó en claro desde el DEX. |
| **reCAPTCHA** | `recaptcha.net/recaptcha/api3` | verificación anti-bot (Google). |
| **Firebase** | `app-measurement.com` | Analytics + FCM (notificaciones). |
| **AdServices** | permisos `ACCESS_ADSERVICES_ATTRIBUTION`/`AD_ID` + Install Referrer | **atribución de publicidad** (Android Privacy Sandbox): mide conversiones de anuncios. |

## Observación — publicidad en una app de transporte

La combinación **AdServices + Install Referrer + AD_ID** indica que la app participa en
**medición/atribución de campañas publicitarias** (recoge identificadores con fines de anuncios).
En una app de **transporte público** es una elección notable de privacidad: no es necesario para
la función de transporte y expone al usuario a tracking publicitario. No es una vulnerabilidad,
pero sí una observación de privacidad (doc 05).

## Transporte

- No se observó `usesCleartextTraffic` ni endpoints `http://` propios en el DEX → el backend
  (en el snapshot) usa HTTPS. Los `http://` presentes son namespaces XML de librerías, ajenos.
- **Pinning:** no verificable (capa de red en Dart). Límite.
