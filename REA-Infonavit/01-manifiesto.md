# 01 · Manifiesto, componentes y superficie

Fuente: `AndroidManifest.xml` (decodificado, en la raíz del REA) extraído por
`inspect_android_package`. `package="com.infonavit.micuentainfonavit"`, versionCode 99,
versionName 5.3.48, minSdk 24, targetSdk 36.

## Permisos

| Permiso | Nota |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | red |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` + `uses-feature` GPS | localización de oficinas/citas |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | login biométrico |
| `WAKE_LOCK` | |
| `com.google.android.finsky…BIND_GET_INSTALL_REFERRER_SERVICE` | install referrer (atribución) |
| `com.google.android.gms.permission.AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID` | Ad ID / Privacy Sandbox (analytics) |
| `…DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (signature) | permiso interno autogenerado |

No pide almacenamiento legacy de forma peligrosa salvo `requestLegacyExternalStorage="true"` en
`<application>` (relevante junto al FileProvider, abajo). No hay permisos de SMS/contactos/cámara
declarados en el manifiesto (la captura de documentos, si existe, no reservó cámara aquí).

## `<application>`

- `android:name=com.infonavit.micuentainfonavit.MCIApplication`
- **`allowBackup="false"`**, **`fullBackupContent="false"`** → positivo (no backup de datos).
- **`networkSecurityConfig="@xml/network_security_config"`** → ver doc 03; su contenido es
  `<base-config cleartextTrafficPermitted="false"/>` (bloquea HTTP en claro globalmente).
- `extractNativeLibs="false"`, `supportsRtl="true"`, `requestLegacyExternalStorage="true"`.

## Componentes exportados (superficie de ataque directa)

| Componente | exported | Notas |
|---|---|---|
| `ui.login.SplashActivity` | **true** | LAUNCHER (entrada normal) |
| `ui.registro.v2.RegistroActivity` | **true** | 3 deeplinks `autoVerify` (abajo) |
| `ui.recuperar_contrasena.v2.RestablecerContrasenaActivity` | **true** | 2 deeplinks `autoVerify` |
| `androidx.profileinstaller.ProfileInstallReceiver` | true | permiso `DUMP`, estándar AndroidX |

El resto de actividades (`MainActivity`, `HomeActivity`, `LoginV3Activity`,
`OficinasContainerActivity`, términos/aviso, etc.) son `exported="false"`. Las dos activities
exportadas con deeplink reciben datos de navegación web (registro / recuperación de contraseña):
superficie a revisar para *intent spoofing*, pero sin permisos sensibles asociados.

## Deeplinks / App Links

Registro y recuperación de contraseña declaran `intent-filter` con `autoVerify="true"` para:

- `https://micuenta.infonavit.org.mx/wps/portal/mci2/registro` y `…/recupera` (producción)
- `https://portalmciqa.infonavit.org.mx/registro` (**host QA en el build de producción**, H4)
- **`http://10.82.1.41:10039` con `pathPrefix=/wps/portal/mci2/...`** — **IP privada interna
  hardcodeada** en un `data` de deeplink (H4). El esquema `http` está declarado pero el tráfico
  en claro está bloqueado por el network security config; queda como fuga de topología interna.

## FileProvider

```xml
<provider android:name="androidx.core.content.FileProvider"
    android:authorities="com.infonavit.micuentainfonavit.provider"
    android:exported="false" android:grantUriPermissions="true"
    android:readPermission="content.permission.READ_CONTENT"
    android:writePermission="content.permission.WRITE_CONTENT"/>
```

`provider_paths.xml` (en `res-decoded/`): `<external-path name="external_files" path="."/>` →
expone **toda** la raíz de archivos externos de la app vía `content://`. Mitigado por
`exported="false"` + permisos custom de lectura/escritura, pero el alcance `"."` es excesivo (H6).
Es más restrictivo que el caso CFE Contigo (que usaba `files-path "/"` con provider heredado).

## Integraciones declaradas en meta-data

- Google Maps (`com.google.android.geo.API_KEY` → `@string/google_maps_key`), Google Places
  (varias `AutocompleteActivity`), GMS measurement/analytics, Firebase (InitProvider, discovery).
- Huawei HMS: maps `6.11.0.300`, safetydetect `6.7.0.304`, base, dynamic-api; appid
  `104066105`; AGConnect provider. La app soporta dispositivos Huawei sin Google.

Detalle de hosts en [02-red-endpoints.md](02-red-endpoints.md).
