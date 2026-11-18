# 01 · Manifiesto

Package `mx.gob.hidalgo.nomasextorsion` v2.3.13 (code 43) · minSdk 22 · targetSdk 36. Manifest
decodificado en [AndroidManifest.xml](AndroidManifest.xml).

## Permisos (9)

| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| **`READ_CALL_LOG`** | leer registro de llamadas (identificar llamadas de extorsión) |
| **`READ_PHONE_STATE`** | estado de la llamada entrante |
| **`SYSTEM_ALERT_WINDOW`** | overlay para avisar sobre una llamada sospechosa |
| `POST_NOTIFICATIONS` | notificaciones |
| `WAKE_LOCK`, `com.google.android.c2dm.permission.RECEIVE` | FCM push |
| `BIND_GET_INSTALL_REFERRER_SERVICE` | install referrer (Play) |

Los permisos de telefonía son **coherentes con la función** (filtrado de llamadas de extorsión vía
`CallScreeningService`), no excesivos. No pide ubicación, cámara, SMS ni contactos.

## Componentes relevantes

- `ExtortionCallScreeningService` — servicio de pantalla de llamadas (API nativa de Android).
- `MyFirebaseMessagingService` — recepción de push.
- `PhoneFunctions.CustomBroadcastReceiver` — eventos de telefonía.
- Actividades: Splash → Main; `ReportNumber` (reportar número), `SettingsActivity`, `actualizacion`.

## Config de red

- **Sin** `networkSecurityConfig` en el manifest; **`usesCleartextTraffic="false"`** (bloqueo de
  cleartext por defecto reforzado). targetSdk 36 → cleartext bloqueado salvo opt-in, que no existe.
