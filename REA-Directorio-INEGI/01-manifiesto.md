# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 3.1, compileSdk 33, etiqueta "DENUE".

## Permisos

| Permiso | Para qué |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | "establecimientos cerca de mí", mapa |
| `CAMERA` | **realidad aumentada** (`ar/ArActivity`) + Street View |
| `INTERNET`, `ACCESS_NETWORK_STATE` | consulta del DENUE |
| `com.google.android.gms.permission.AD_ID` | **identificador de publicidad** (ads/analytics) |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | guardar/compartir (galería, favoritos) |
| `mx.org.inegi.denuemv.permission.MAPS_RECEIVE` (signature) | legacy Google Maps |
| `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE` | widget/servicios |

**Observaciones:**
- La **ubicación** y la **cámara** están justificadas (mapa/AR/Street View). No pide contactos,
  SMS, teléfono ni ubicación en segundo plano.
- **`AD_ID`** señala recolección de identificador publicitario — coherente con las librerías de
  ads/analytics (doc 03); es el permiso sensible desde el punto de vista de **privacidad** en una
  app del INEGI.
- **Mismatch de permiso `MAPS_RECEIVE`:** se **declara** `mx.org.inegi.denuemv.permission.MAPS_RECEIVE`
  (signature) pero se **usa** `mx.org.inegi.denue.permission.MAPS_RECEIVE` (nota: `denue` ≠
  `denuemv`). Es el patrón legacy de Google Maps v1 (ya deprecado) y el nombre no coincide → el
  permiso protegido queda efectivamente inerte. Deuda técnica menor, sin impacto de seguridad.

## Flags
- **Sin** `usesCleartextTraffic` → HTTP en claro bloqueado por defecto (consistente con los
  endpoints HTTPS, doc 03).
- Sin `networkSecurityConfig` por recurso → política por defecto (CAs del sistema).
- La **Google Maps API key** se declara como meta-data del manifiesto (`com.google.android.geo.API_KEY`)
  y aparece también en `strings.xml` (doc 04).
