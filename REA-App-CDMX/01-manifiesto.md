# 01 — Manifiesto, superficie de ataque

Decodificado de `AndroidManifest.xml` (1,137 líneas). Todo lo de abajo es estático.

## Elemento `<application>`

```
android:name="com.pairip.application.Application"   ← wrapper de PairIP (ver doc 07)
android:allowBackup="false"
android:usesCleartextTraffic="false"
android:extractNativeLibs="false"
android:largeHeap="true"
android:hardwareAccelerated="true"
android:requestLegacyExternalStorage="true"        ← almacenamiento legado (pre-scoped)
android:appComponentFactory="androidx.core.app.CoreComponentFactory"
android:localeConfig="@xml/locales_config"
android:theme="@style/Theme.AppCDMX"
```

Higiene base **correcta**: sin backup, sin tráfico en claro, librerías no extraíbles. El
punto débil clásico es `requestLegacyExternalStorage="true"` + `WRITE_EXTERNAL_STORAGE`,
que reabre el acceso amplio a almacenamiento en dispositivos donde Android lo respeta.

## Permisos (26)

**Peligrosos / sensibles:**
- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, **`ACCESS_BACKGROUND_LOCATION`** — ubicación continua, incl. segundo plano (Mi Taxi, botón de pánico).
- `FOREGROUND_SERVICE`, **`FOREGROUND_SERVICE_LOCATION`** — servicio en primer plano con ubicación.
- `CAMERA` — QR/ML Kit, fotos de reportes.
- `NFC` — tarjeta de transporte Calypso (doc 05).
- `USE_BIOMETRIC` — desbloqueo biométrico de sesión.
- `WRITE_CALENDAR` — agendado de citas/eventos.
- `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` — con legacy storage.

**Normales / sistema:** `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`,
`CHANGE_WIFI_STATE`, `VIBRATE`, `WAKE_LOCK`, `POST_NOTIFICATIONS`,
`RECEIVE_BOOT_COMPLETED`, `SET_ALARM`.

**Publicidad / tienda:** `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID`
(Privacy Sandbox / atribución), `com.google.android.c2dm.permission.RECEIVE` (FCM),
`com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE`,
`com.android.vending.CHECK_LICENSE`.

**Propios:** `mx.gob.cdmx.adip.apps.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (patrón
AndroidX para receivers dinámicos no exportados).

## Componentes

| Tipo | Total | Exportados |
|---|---|---|
| `<activity>` | 105 | 3 propios |
| `<service>` | 21 | 3 (WorkManager + Calypso) |
| `<receiver>` | 20 | 7 (Firebase, WorkManager, Calypso, ProfileInstaller) |
| `<provider>` | 5 | 1 (Calypso MultiApp) |

### Componentes exportados (`android:exported="true"`)

Propios:
- `…ui.activities.SplashActivity` — entrada (LAUNCHER).
- `…culturaentretenimiento.ui.activities.CulturaEntretenimientoActivity` — deeplink/handoff.
- `…mitaxi.ui.activities.UserHomeActivity` — deeplink `appcdmx://mitaxi`.

De librerías (normales en su contexto, pero parte de la superficie):
- `com.google.firebase.iid.FirebaseInstanceIdReceiver`
- `androidx.work.impl.background.systemjob.SystemJobService`, `…diagnostics.DiagnosticsReceiver`
- `androidx.profileinstaller.ProfileInstallReceiver`
- **Calypso/Spirtech (4):** `…hce.calypso.scheduling.BootReceiver`,
  `…hce.calypso.contactless.ContactlessService`, `…hce.calypso.scheduling.SyncJobService`,
  `…hce.calypso.multiapp.MultiAppProvider` (provider), `…hce.calypso.multiapp.MultiAppReceiver`,
  `ccm.spirtech.calypsocardmanager.front.nfcdiscovery.android.AndroidNfcReceiver`.
  Estos deben estar exportados para que el sistema NFC entregue eventos de tarjeta (doc 05).

## Providers (authorities)

| Authority | Origen |
|---|---|
| `…apps.fileprovider` | FileProvider de la app (`@xml/file_paths`) |
| `…apps.com.spirtech.surpass.ma.provider` | Spirtech SURPASS (gestión de tarjeta Calypso) |
| `…apps.firebaseinitprovider` | Inicialización Firebase |
| `…apps.mlkitinitprovider` | Inicialización ML Kit |
| `…apps.androidx-startup` | AndroidX App Startup |

> **Resuelto (doc 10):** se decodificó `res/xml/file_paths.xml` + `image_share_filepaths.xml`.
> El alcance está **correctamente acotado** (solo `Pictures/`, `Download/`, `images/`,
> `image_provider/`). **No** repite el `files-path "/"` abierto de jw-library y CFE.

## Deeplinks y enlaces

- **Scheme propio:** `appcdmx://` — hosts `mitaxi` y `utopias` (BROWSABLE).
- **App Links https** — manejados por varias Activities (p. ej. Cultura/Entretenimiento).
- **`<queries>`:** `com.whatsapp`, `com.google.android.apps.maps` — la app verifica/abre
  WhatsApp (compartir/soporte) y Google Maps (navegación Mi Taxi).
- **Feature:** `android.hardware.nfc.hce` — Host Card Emulation declarada.

## App-widgets (pantalla de inicio)

Siete widgets (`@xml/*_widget_info`): `boton_panico`, `chat_locatel`,
`documentos_digitales`, `mi_taxi`, `policia_cuadrante`, `tarjeta_mi`, `wifi_hotspot`.
Más `@xml/apduservice` (servicio HCE Calypso) y `@xml/splits0` (metadatos de split install).
