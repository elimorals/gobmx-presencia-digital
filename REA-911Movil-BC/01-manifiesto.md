# 01 · Manifiesto

Package `com.c4bc.alerta066m`, v7.0.4 (154), minSdk 24 / targetSdk 36.
Manifest decodificado completo en [AndroidManifest.xml](AndroidManifest.xml) (aapt2 xmltree).

## Permisos (sensibles primero)

- **Ubicación:** `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, **`ACCESS_BACKGROUND_LOCATION`**,
  `FOREGROUND_SERVICE_LOCATION` — ubicación continua, también en segundo plano (coherente con app
  de pánico que reporta posición durante una emergencia).
- **Captura:** `RECORD_AUDIO`, `CAMERA` — evidencia de la emergencia.
- **Interfaz/persistencia:** `SYSTEM_ALERT_WINDOW` (overlay), `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK`,
  `FOREGROUND_SERVICE`, `POST_NOTIFICATIONS`.
- **Biometría:** `USE_BIOMETRIC`, `USE_FINGERPRINT`.
- **Push:** `com.google.android.c2dm.permission.RECEIVE` (FCM).
- Resto: almacenamiento, red/wifi state, y una batería larga de permisos de *badge* de launchers
  (Samsung/HTC/Sony/Huawei/Oppo… — típico de libs de notificación con contador).

## Config de red

- `android:networkSecurityConfig` presente (→ `res/.../network_security_config`, ver
  [03-tls-cleartext.md](03-tls-cleartext.md)). **No** hay atributo `usesCleartextTraffic` global; la
  política de cleartext la fija el NSC.

## Componentes / deeplinks

- Deeplink Expo `exp+911movilbc://` y esquema `https` (Expo Router / universal links).
- Componentes exportados: los estándar de React Native/Expo (MainActivity launcher, servicios de
  notificación/ubicación FCM, `BIND_GET_INSTALL_REFERRER`). Sin exportados anómalos evidentes.
- Permiso propio `com.c4bc.alerta066m.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (RN runtime).
