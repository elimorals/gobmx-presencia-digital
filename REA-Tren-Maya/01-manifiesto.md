# 01 · Manifiesto

Package `com.indra.trenmaya.pro` v2.7.4 (vc 200700040) · minSdk 29 · targetSdk 36.
Manifiesto decodificado completo en [AndroidManifest.xml](AndroidManifest.xml) (aapt2 xmltree).

## Permisos

| Permiso | Nota |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (mapas/estaciones) |
| `WRITE_EXTERNAL_STORAGE` | legado (maxSdk implícito); boletos/PDF |
| `POST_NOTIFICATIONS`, `WAKE_LOCK`, `FOREGROUND_SERVICE` | notificaciones/servicio |
| `RECEIVE_BOOT_COMPLETED`, `REORDER_TASKS` | arranque/tareas |
| `com.google.android.gms.permission.AD_ID` | **ID de publicidad** |
| `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID` | **atribución de anuncios** |
| `BIND_GET_INSTALL_REFERRER_SERVICE` | referrer de instalación (atribución) |
| `com.android.vending.CHECK_LICENSE` | licencia Play |

**Observación:** el conjunto de permisos es razonable para una app de boletos (ubicación +
notificaciones). Lo que destaca es la **publicidad/atribución** (`AD_ID` + AdServices) en una app
de gobierno — ver H2.

## Postura de red (del manifiesto)

- **Sin `android:usesCleartextTraffic="true"`** en el manifiesto.
- **targetSdk 36** → cleartext **bloqueado por defecto** salvo NSC explícito; no se halló NSC
  permisivo.
- Resultado: la app no permite HTTP en claro a nivel plataforma. Ver [03-tls-pagos.md](03-tls-pagos.md).
