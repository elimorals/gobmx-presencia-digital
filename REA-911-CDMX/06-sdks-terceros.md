# 06 — SDKs de terceros, claves y analytics

Fuente: manifiesto (componentes/meta-data de cada SDK) y `res/values/strings.xml`.

## SDKs integrados

| SDK | Para qué | Evidencia |
|---|---|---|
| **Firebase Cloud Messaging** | Notificaciones push (red de emergencia, anuncios) | `NotificationService`, `FirebaseMessagingService`, `FirebaseInstanceIdReceiver` |
| **Firebase Crashlytics** | Reporte de crashes | `CrashlyticsRegistrar` |
| **Firebase Analytics / RemoteConfig** | Analítica y configuración remota | `AnalyticsConnectorRegistrar`, `RemoteConfigRegistrar`, `utils.RemoteConfig` |
| **Google Maps** | Mapa de ubicación/incidente | `com.google.android.geo.API_KEY`, `queries` a `com.google.android.apps.maps` |
| **Google Play Services Measurement + AdId** | Medición/atribución (ads) | `AppMeasurementService/Receiver/JobService`, permiso `gms.permission.AD_ID` |
| **Twitter SDK (tweetui + core)** | Feed de Twitter (Sismológico MX / SASMEX) | `com.twitter.sdk.android.*` (PlayerActivity, GalleryActivity, OAuthActivity) |
| **AndroidX WorkManager / Room / ProfileInstaller** | Trabajo en segundo plano, DB, baseline profile | providers/receivers AndroidX estándar |

## Claves y secretos en recursos (hallazgo)

`res/values/strings.xml` contiene credenciales en claro (valores completos en
`evidence/claves-en-claro.txt`, **gitignored**; aquí redactados):

| Recurso | Valor (redactado) | Riesgo |
|---|---|---|
| `com.twitter.sdk.android.CONSUMER_KEY` | `N7xLahuv…` | API key de la app de Twitter |
| **`com.twitter.sdk.android.CONSUMER_SECRET`** | `cYoC876P…` (49 chars) | **Secreto OAuth del consumidor en claro** |
| `google_maps_key` | `AIzaSyAPVP…LD6I` | Clave de Google Maps (restringible por package) |
| (2ª key GMS/Firebase) | `AIzaSyDo_5…57UHg` | Clave de servicios Google |

El **`CONSUMER_SECRET` de Twitter** es el hallazgo relevante: un secreto OAuth embebido en
los recursos de un APK distribuido es recuperable por cualquiera con `apktool`. "Ocultarlo"
en `strings.xml` no lo protege; debería residir en el servidor. Las claves de Google Maps
en cliente son práctica común y se mitigan restringiéndolas por huella+package en la consola
de Google (no verificable desde el binario).

## Analytics/ads en una app de emergencia

La combinación Twitter SDK + Google AdId + AppMeasurement implica recolección de
identificadores de publicidad/medición en una app cuya función es el auxilio ciudadano. No
es una vulnerabilidad en sí, pero es una decisión de privacidad discutible para este tipo de
servicio (se documenta como observación, no como causa de daño).

> **No inventado:** no se afirma que estos SDKs exfiltren datos del incidente; solo se
> documenta su presencia y el secreto en claro, con su evidencia.
