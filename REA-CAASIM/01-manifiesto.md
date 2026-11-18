# 01 · Manifiesto

Package `mx.gob.hidalgo.caasim.app` · versionName 1.0.39 (40) · minSdk 23 · targetSdk 36.
Manifest decodificado: [AndroidManifest.xml](AndroidManifest.xml) (árbol aapt2).

## Permisos (28)

Red/ubicación: `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `ACCESS_FINE_LOCATION`,
`ACCESS_COARSE_LOCATION`, `GPS_PROVIDER`, `NetworkLocationService`.
Teléfono: **`CALL_PHONE`**, **`READ_PHONE_STATE`**.
Almacenamiento/docs: `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE`, **`MANAGE_DOCUMENTS`**.
Cámara: `CAMERA`. Diccionario: **`READ_USER_DICTIONARY`**, **`WRITE_USER_DICTIONARY`** (inusual).
Sistema: `WAKE_LOCK`, `VIBRATE`, `RECEIVE_BOOT_COMPLETED`, `POST_NOTIFICATIONS`,
`INSTALL_SHORTCUT`.
Google/ads/billing: `AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID`,
`c2dm.RECEIVE`, `BIND_GET_INSTALL_REFERRER_SERVICE`, `CHECK_LICENSE`.

**Nota (H5):** `CALL_PHONE`, `READ_PHONE_STATE`, `MANAGE_DOCUMENTS` y los permisos de diccionario
de usuario exceden lo que un autoservicio de pago/consulta de agua necesita. Severidad baja, pero
amplía la superficie.

## Componentes

~12 componentes exportados (conteo del árbol). Revisión de superficie exportada pendiente de
segundo pase; el foco de este análisis fue red/TLS/pagos.

> `usesCleartextTraffic="true"` + `networkSecurityConfig=@xml/network_security_config` — ver
> [02-red-endpoints-tls.md](02-red-endpoints-tls.md).
