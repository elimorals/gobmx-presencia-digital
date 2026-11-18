# 01 · Manifiesto y permisos

`AndroidManifest.xml` decodificado en esta carpeta. targetSdk 30, minSdk 21.

## Permisos declarados

Coherentes con una app de pánico/seguridad:
- **Ubicación:** `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `ACCESS_LOCATION_EXTRA_COMMANDS`,
  `FOREGROUND_SERVICE`, `com.google.android.gms.permission.ACTIVITY_RECOGNITION`.
- **Contacto/llamada:** `CALL_PHONE`, `READ_CONTACTS`, `WRITE_CONTACTS`, `READ_PHONE_STATE`,
  `GET_ACCOUNTS`, `AUTHENTICATE_ACCOUNTS`.
- Red/sistema: `INTERNET`, `ACCESS_NETWORK_STATE`, `VIBRATE`, `WAKE_LOCK`, `BLUETOOTH`,
  `RECEIVE_BOOT_COMPLETED`, `READ/WRITE_EXTERNAL_STORAGE`, FCM (`c2dm.permission.RECEIVE`).

**Sin** `RECORD_AUDIO` ni permisos de SMS — superficie acotada frente a otras apps de pánico.

## Red

- `android:networkSecurityConfig` → `res/xml/network_security_config.xml` con
  `cleartextTrafficPermitted=true` (domain-config, `includeSubdomains=true`). Ver doc 02.
