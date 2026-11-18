# Manifiesto — permisos, componentes, flags

Fuente: `AndroidManifest.xml` decodificado (apktool) + `aapt2 dump badging`. Copia completa
en [`AndroidManifest.xml`](AndroidManifest.xml).

## Aplicación

```
package=gob.yucatan.vayven  versionName=4.3.0  versionCode=199
minSdk=28  targetSdk=36
android:name=com.embsoft.vinden.application.VindenApp
```

Flags relevantes de `<application>`:

| Flag | Valor | Lectura |
|---|---|---|
| `android:usesCleartextTraffic` | **`true`** | Permite HTTP sin TLS (ver doc 03) |
| `android:networkSecurityConfig` | `@xml/network_security_config` | Lista dominios con cleartext (doc 03) |
| `android:allowBackup` | `false` | ✅ no respalda datos de app |
| `android:extractNativeLibs` | `false` | `.so` se cargan comprimidas del APK |
| `android:largeHeap` | `true` | Heap grande (mapas/imágenes) |
| `android:debuggable` | (ausente) | ✅ no debuggable |

## Permisos

| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | Red |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación (rastreo de camiones / rutas) |
| `CAMERA` | Escaneo (QR / lectura) |
| `GET_ACCOUNTS`, `USE_CREDENTIALS` | Cuentas Google (login) |
| `CALL_PHONE` | Llamada directa (atención ciudadana) |
| `POST_NOTIFICATIONS`, `com.google.android.c2dm.permission.RECEIVE` | Push FCM |
| `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE` | Almacenamiento (legacy, amplio) |
| `FOREGROUND_SERVICE`, `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED` | Servicios en segundo plano |
| `AD_ID`, `ACCESS_ADSERVICES_{ATTRIBUTION,AD_ID,TOPICS,CUSTOM_AUDIENCE}` | Publicidad / atribución |
| `BIND_GET_INSTALL_REFERRER_SERVICE` | Atribución de instalación (Play) |
| `REORDER_TASKS` | Reordenar tareas |

**Sin permisos de sistema peligrosos** (nada `signature|privileged`), a diferencia de IMSS.
El conjunto es coherente con una app de transporte con pagos y push. `WRITE_EXTERNAL_STORAGE`
es un permiso amplio heredado (legacy) poco recomendable en targetSdk 36.

## Componentes exportados

No se detectaron `android:exported="true"` explícitos en actividades/servicios/receivers
propios más allá del launcher (con intent-filter). La superficie de componentes exportados es
**mínima** según el manifiesto decodificado. (No se verificó cada `<provider>` de librerías de
Google/AndroidX, que suelen venir con permisos de firma propios.)

## CALL_PHONE + ads

Dos puntos a notar sin sobreinterpretar:
- `CALL_PHONE` permite marcar sin pasar por el marcador; coherente con "atención ciudadana"
  (`transporteyucatan.org.mx/atencion-ciudadana`, `wa.me/` en doc 03), pero es un permiso que
  una app de transporte podría resolver con un `ACTION_DIAL` sin permiso.
- El bloque `ACCESS_ADSERVICES_*` + `AD_ID` indica medición/publicidad (Google Ads / Privacy
  Sandbox). Evidencia de SDKs en doc 06.
