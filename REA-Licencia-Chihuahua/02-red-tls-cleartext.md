# 02 · Red, TLS y cleartext

## Endpoints (del bundle JS, no ofuscado)

- **API principal:** `https://licdigital.sspechih.gob.mx:8082/api/LicenciaDigital/` (HTTPS, 8082).
- **OTA (CapacitorUpdater):** `https://licdigital.sspechih.gob.mx:8082/api/Actualizaciones/`
  (`check-update`, `download-update`, `version-update`) — `autoUpdate:false`.
- Framework: `api.capgo.app` (referencia de Capgo/CapacitorUpdater en el dex).

Todos los endpoints **propios** observados son **HTTPS**. No se halló ningún `http://` de backend en
el bundle (los `http://` presentes son docs de frameworks: fb.me, ionicframework).

## TLS / trust-all — AUSENTE-SANO (CONFIRMADO)

- Barrido del dex: **sin** `getUnsafeOkHttpClient`, `TrustAllCerts`, `checkServerTrusted` vacío,
  `ALLOW_ALL_HOSTNAME`, `X509TrustManager` propio. **No es el defecto Cosmocolor/Infonavit.**
- Capacitor usa el WebView del sistema → validación TLS estándar de Android; no se halló override de
  `onReceivedSslError`.

## Cleartext — MEDIA-BAJA (config permisiva, no explotada)

- **CWE-319 (potencial):** `usesCleartextTraffic="true"` global + `allowMixedContent:true`. La app
  **permite** tráfico HTTP/mixed-content, aunque el backend observado es HTTPS.
- **A-CONFIRMAR:** si alguna ruta en runtime (o un contenido mixto dentro del WebView) cae a HTTP.
  No se demostró una llamada en claro; se reporta como higiene de configuración, no como MITM
  confirmado. No se inventa causa.

## Secretos

- Sin API keys/tokens/secretos hardcodeados en el bundle JS (grep de `AIza`/JWT/`apikey`/`secret`).
