# Manifiesto y superficie de ataque

Manifiesto decodificado completo: `AndroidManifest.xml` (en este directorio) y
`res-decoded/AndroidManifest.xml` (SSD). 31 activities, 11 services, 5 receivers, 5 providers.

## Permisos (16)

| Permiso | Nota |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE`, `CHANGE_WIFI_STATE` | Red |
| `WAKE_LOCK` | Mantener CPU despierta |
| `POST_NOTIFICATIONS` | Notificaciones Android 13+ |
| **`CAMERA`** | Lectura de QR/códigos y captura de documentos (contratación) |
| **`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`** | Ubicación de centros CAC / reportes de falla |
| **`CALL_PHONE`** | Llamada directa a CFE desde la app |
| **`USE_BIOMETRIC`, `USE_FINGERPRINT`** | Desbloqueo biométrico de sesión |
| `com.google.android.c2dm.permission.RECEIVE` | Push Firebase (FCM) |
| `com.google.android.finsky...BIND_GET_INSTALL_REFERRER_SERVICE` | Install referrer (Play) |
| `com.huawei.appmarket...GET_COMMON_DATA` | Huawei AppGallery |
| `mx.com.cfe.cfecontigo.permission.PROCESS_PUSH_MSG` | Firma, interno (HMS) |
| `mx.com.cfe.cfecontigo.permission.PUSH_PROVIDER` | Firma, interno (HMS) |

**Permisos peligrosos presentes**: cámara, ubicación fina/gruesa, teléfono. Coherentes con
las funciones (QR, CAC cercanos, llamar a CFE, foto de documentos). A diferencia de
jw-library, esta app sí pide permisos sensibles — razonable para su dominio, pero amplía la
superficie de privacidad.

## Flags de aplicación

- `android:allowBackup="false"` — **no respalda** datos de la app (correcto para app financiera).
- `android:usesCleartextTraffic="false"` — prohíbe HTTP en claro.
- **Sin `networkSecurityConfig` propio** — usa el default de la plataforma (targetSdk 36 →
  cleartext bloqueado por defecto). Ver doc 04.
- `data-extraction-rules` → `<cloud-backup/>` vacío; `full-backup-content` vacío.

## Componentes EXPORTADOS (superficie de ataque)

| Componente | Tipo | Por qué está expuesto |
|---|---|---|
| `.ui.login.SplashActivity` | activity | **LAUNCHER** (único punto de entrada de UI) |
| `net.openid.appauth.RedirectUriReceiverActivity` | activity | Redirect OAuth2 — scheme `mx.com.cfe.cfecontigo://` |
| `com.google.firebase.auth.internal.GenericIdpActivity` | activity | Firebase Auth IdP — scheme `genericidp://firebase.auth` |
| `com.google.firebase.auth.internal.RecaptchaActivity` | activity | Firebase reCAPTCHA — scheme `recaptcha://firebase.auth` |
| `com.google.firebase.iid.FirebaseInstanceIdReceiver` | receiver | FCM (protegido por permiso `c2dm`) |
| `com.huawei.hms.support.api.push.*` | service/receiver/provider | Push de Huawei HMS |

### Observaciones de superficie

1. **No hay deeplinks ni App Links de negocio.** El único scheme propio,
   `mx.com.cfe.cfecontigo://`, es el **redirect URI de AppAuth (OAuth2/OIDC)** — no una ruta
   de contenido manipulable. Esto reduce drásticamente la superficie frente a apps con
   `VIEW` de datos externos (contraste con `SiloContainer` de jw-library).
2. **Pantallas de negocio no exportadas.** `LoginActivity`, `DashboardActivity`,
   `ContratacionActivity`, `CardsActivity`, etc. son todas `exported="false"`. Solo se llega
   a ellas tras el Splash/login.
3. **Los componentes exportados son de SDKs** (AppAuth, Firebase Auth, HMS Push), no código
   propio de CFE. Su seguridad depende de esas librerías, no de la app.

## FileProvider (mala práctica)

`androidx.core.content.FileProvider` (`exported="false"`, con `grantUriPermissions`) usa
`res/xml` con rutas:

```xml
<paths>
    <external-path name="external_files" path="." />
    <files-path name="files" path="/" />
    <cache-path name="cache" path="." />
</paths>
```

`files-path path="/"` expone **toda** la carpeta interna `files/` de la app vía
`content://`. Aunque el provider no está exportado y requiere grant explícito, es la misma
mala práctica observada en jw-library: conviene acotar las rutas a subcarpetas concretas
(p.ej. `shared/`) en vez de la raíz.

## `<queries>` (apps que la app puede ver, Android 11+)

WebView (Google/AOSP), Google Maps, y el stack Huawei (`hwid`, `hms`, `pushagent`, `works`,
`hff`). Confirma la doble distribución Google/Huawei y el uso de Maps para ubicar CAC.
