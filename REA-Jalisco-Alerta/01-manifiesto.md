# 01 · Manifiesto

Manifest decodificado completo en [AndroidManifest.xml](AndroidManifest.xml) (dump `aapt2 xmltree`).

## Permisos (sensibles primero)

- **Ubicación:** `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, **`ACCESS_BACKGROUND_LOCATION`**,
  `FOREGROUND_SERVICE_LOCATION`.
- **Cámara/sensor:** `CAMERA`, `FOREGROUND_SERVICE_CAMERA`, `FLASHLIGHT`, `ACCESS_MEDIA_LOCATION`,
  `READ_MEDIA_VISUAL_USER_SELECTED`.
- **UI sobre otras apps:** `SYSTEM_ALERT_WINDOW`.
- **Push/arranque:** FCM/C2DM (`C2D_MESSAGE`, `RECEIVE`), `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK`,
  `POST_NOTIFICATIONS`, `FOREGROUND_SERVICE_REMOTE_MESSAGING`.
- **Almacenamiento:** `READ/WRITE_EXTERNAL_STORAGE`.
- **Publicidad/attribution:** `AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID`.
- Resto: ~20 permisos de *badge* de launchers (Samsung/HTC/Huawei/Oppo/Sony) — ruido de librería de
  badges, no sensibles.

**Nota:** `ACCESS_BACKGROUND_LOCATION` + `CAMERA` + `SYSTEM_ALERT_WINDOW` son coherentes con una app
de alerta/pánico, pero la ubicación en segundo plano es el permiso de mayor impacto de privacidad.
Requiere aviso y justificación claros (Google exige formulario para background location).

## Componentes

- Deeplinks: esquema propio **`erliaapp://`** + App Links `https` (verificar `autoVerify`).
- Varios componentes `exported=true` (Activities/Receivers de RN, Expo, FCM, badges). La mayoría son
  del framework (MainActivity, receptores de push/badge). No se identificó un componente propio con
  lógica sensible expuesto sin permiso — **a confirmar** con revisión de intents (muro Hermes para
  el handler).
- targetSdk 36: cleartext **bloqueado por defecto**; sin `networkSecurityConfig` ni
  `usesCleartextTraffic` declarados (ver doc 03).
