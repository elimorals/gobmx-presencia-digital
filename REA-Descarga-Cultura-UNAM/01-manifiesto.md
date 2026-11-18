# 01 · Manifiesto

`AndroidManifest.xml` decodificado (xmltree) en esta carpeta.

## Permisos (10, mínimos y coherentes con una app de audio)

| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red (descarga/stream de audio) |
| `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | reproducción de audio en segundo plano |
| `WAKE_LOCK` | mantener reproducción activa |
| `POST_NOTIFICATIONS` | controles de reproducción / avisos |
| `BIND_GET_INSTALL_REFERRER_SERVICE` (Finsky) | atribución de instalación (Play) |
| `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID` | Privacy Sandbox / medición |
| `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | permiso propio autogenerado (AndroidX) |

**Sin** permisos peligrosos: no ubicación, no SMS, no contactos, no cámara/micrófono, no
almacenamiento externo, no permisos de operador. Superficie mínima, apropiada al propósito.

- `minSdk 23 / targetSdk 36` — moderno; en API ≥28 el cleartext está bloqueado por defecto salvo
  lo que permita el `networkSecurityConfig` (ver [03-red-tls.md](03-red-tls.md)).
