# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 2.5.2, compileSdk 36.

## Permisos

| Permiso | Para qué (inferido) | Nota |
|---|---|---|
| `NFC` | cobro por NFC (tap) además de QR | esperado en CoDi |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | desbloqueo + autorización de llaves (doc 03) | ✅ |
| `READ_CONTACTS` | elegir a quién cobrar desde contactos | |
| `SYSTEM_ALERT_WINDOW` | **overlay** (dibujar sobre otras apps) | ⚠️ inusual en banca; uso no confirmado en código (doc 06) |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red (API CoDi) | |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | notificaciones push (FirebaseService) | |
| `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE`, `WAKE_LOCK` | background / servicio | |

**No pide `CAMERA`** — confirma que es la app del **cobrador** (genera el QR de cobro, no lo
escanea). **No pide ubicación, SMS ni almacenamiento amplio.** Conjunto acotado salvo el overlay.

## Componentes relevantes

- **`activities.login.Registro`** — enrolamiento de cuenta (doc 03).
- **`activities.menu.cobro.Cuentas` / `DetalleCobrar`** — alta de cuentas y generación del cobro.
- **`activities.menu.historial.*`** — historial, filtrar, ordenar, **`Exportar`** (CSV).
- **`inactivitytimeout.InactivityWorker` + `SessionTimeoutReceiver`** — **cierre de sesión por
  inactividad** (buena práctica, doc 06).
- **`activities.menuLateral.configActivities.{CambioPass, AdminSpam}`** — cambio de contraseña,
  anti-spam.
- **`receivers.FirebaseService`** — push (FCM).

## Flags

- Sin `usesCleartextTraffic` → HTTP en claro bloqueado por defecto (consistente con los
  endpoints HTTPS, doc 04). La confianza TLS está, además, reforzada por **pinning** propio
  (doc 04), no por `networkSecurityConfig` (no hay recurso NSC).
