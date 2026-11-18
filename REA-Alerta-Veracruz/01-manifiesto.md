# 01 · Manifiesto

`AndroidManifest.xml` decodificado en esta carpeta.

- **Package:** `mx.gob.veracruz.c4.emrgcyveracruz` · versión 1.2.11 (21) · minSdk 23 / targetSdk 36.
- **Permisos (9):** `INTERNET`, `ACCESS_NETWORK_STATE`, `WRITE_EXTERNAL_STORAGE`,
  `READ_EXTERNAL_STORAGE`, `WAKE_LOCK`, `com.google.android.c2dm.permission.RECEIVE` (FCM),
  `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION`, y el permiso dinámico autogenerado
  `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`.
- **Ausentes (notable para una app de alerta):** sin `CAMERA`, sin `RECORD_AUDIO`, sin `READ_SMS`,
  sin `ACCESS_BACKGROUND_LOCATION`. Superficie de permisos **contenida** — coherente con
  reporte+ubicación en primer plano, no vigilancia.
- **`usesCleartextTraffic`:** no declarado (con targetSdk 36 → cleartext bloqueado por defecto).
- **`networkSecurityConfig`:** no referenciado (no reabre cleartext).
