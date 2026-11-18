# 01 · Manifiesto — Tuzobús

- **Package:** `com.tuzobus` · v2.4.1 (vc 32) · minSdk 24 / targetSdk 36.
- **Flags de seguridad:** `android:usesCleartextTraffic="false"`, `android:allowBackup="false"`,
  sin `android:debuggable`. Sin `networkSecurityConfig` declarado (con cleartext=false el default de
  targetSdk 36 ya bloquea HTTP en claro).
- **Permisos:** ver 00-RESUMEN. Sensibles: `ACCESS_FINE_LOCATION`/`ACCESS_COARSE_LOCATION`
  (seguimiento de unidad/paradas), `USE_BIOMETRIC`/`USE_FINGERPRINT` (login/pago),
  `SCHEDULE_EXACT_ALARM`. **No** pide SMS, call log, ni phone-state privilegiado.
- **Componentes exportados:** varios `exported=true` (patrón normal de RN/Expo: launcher activity,
  receivers de FCM/boot). No se halló un deeplink/esquema propio llamativo en el barrido rápido;
  revisión exhaustiva de exportados pendiente de segundo pase si se requiere.
- **Push:** Firebase Cloud Messaging (`com.google.android.c2dm.permission.RECEIVE` +
  `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`).

El `AndroidManifest.xml` decodificado (aapt2 xmltree) está en este directorio.
