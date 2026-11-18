# 01 — Manifiesto: permisos, componentes, flags

Fuente: `AndroidManifest.xml` decodificado (apktool) del APK base. `compileSdkVersion="35"`.

## Permisos declarados

| Permiso | Uso probable | Nota |
|---|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación para cuadrante/geolocalización | esperado |
| **`ACCESS_BACKGROUND_LOCATION`** | Ubicación en segundo plano | amplio; plugin `background_location` (doc 05) |
| `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_LOCATION` | Servicio de ubicación en primer plano | — |
| `CALL_PHONE` | Llamada directa de emergencia (`flutter_phone_direct_caller`) | — |
| `CAMERA` | Escaneo de códigos (`flutter_barcode_scanner`) | — |
| `FLASHLIGHT` | Linterna | — |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | Red | — |
| **`DUMP`** | — | **`android.permission.DUMP` es `signature\|privileged`**: el sistema NO lo concede a apps de tienda. Declararlo es inusual/residual en una app de este tipo. No se observó el código que lo usaría (vive en Dart); se reporta como anomalía del manifiesto, sin inventar su propósito. |

No declara SMS, CONTACTS, ni permisos de almacenamiento externo amplio — superficie relativamente
acotada salvo por `DUMP` y la ubicación en segundo plano.

## Componentes

- **Entrada:** `com.example.mipolicia_ssc.MainActivity` (exported) — actividad Flutter. Nótese el
  namespace **`com.example.`**: el `applicationId` real es `com.moobky.MiPolicia`, pero la clase de
  entrada quedó bajo el paquete de plantilla por defecto de Flutter (`com.example.mipolicia_ssc`).
  Higiene de build, no un riesgo por sí mismo.
- `ProfileInstallReceiver` (androidx.profileinstaller) — baseline profiles, estándar.
- Providers: `androidx.startup.InitializationProvider`, `dev.fluttercommunity.plus.share.ShareFileProvider`
  (FileProvider del plugin `share_plus`).
- `uses-feature`: `android.hardware.camera`, `android.hardware.telephony`.

## Flags de seguridad

- **No** hay `android:networkSecurityConfig` (grep: 0 coincidencias) ni se encontró
  `network_security_config.xml` en los recursos.
- **No** se declara `usesCleartextTraffic="true"`. Con `compileSdk 35` y sin NSC, el tráfico en claro
  queda **deshabilitado por defecto**: el único `http://` referenciado en el código
  (`http://data.ssp.cdmx.gob.mx/.../AVISOS.xlsx`) quedaría bloqueado en runtime salvo que el targetSdk
  efectivo sea &lt;28 (no confirmado). Se documenta como enlace **latente**, no como cleartext activo.
- No se observó `android:debuggable="true"`.
