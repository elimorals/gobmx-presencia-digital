# 01 — Manifiesto: permisos, componentes, flags

Fuente: `AndroidManifest.xml` decodificado (JADX `get_android_manifest` + apktool). Copia
completa en [`AndroidManifest.xml`](AndroidManifest.xml).

## Permisos solicitados (uses-permission)

| Permiso | Uso probable | Nota |
|---|---|---|
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Ubicación del incidente y Sígueme | Esperado |
| `CAMERA` | Adjuntar foto al incidente (`attachImage`) | Esperado |
| `CALL_PHONE` | Marcar 911 / 089 | Esperado |
| `READ_CONTACTS` | Armar la **red de emergencia** (contactos guardianes) | Sensible |
| `RECEIVE_BOOT_COMPLETED` | Reprogramar servicios tras reinicio | — |
| `FOREGROUND_SERVICE` | `LocationService`/`SiguemeService` en primer plano | — |
| `WRITE/READ_EXTERNAL_STORAGE` | Fotos/adjuntos | Legacy |
| `POST_NOTIFICATIONS`, `WAKE_LOCK`, `ACCESS_NETWORK_STATE`, `ACCESS_NOTIFICATION_POLICY` | UX / notificaciones | — |
| `INTERNET` | Red (Volley) | — |
| `c2dm.RECEIVE`, `gms.permission.AD_ID`, `BIND_GET_INSTALL_REFERRER_SERVICE` | Firebase / GMS / Play | Ads/atribución |

**Corrección de un posible falso positivo:** el apktool muestra `android:permission="android.permission.DUMP"`
pero **solo como atributo que protege dos receivers de AndroidX** (`androidx.work.impl.diagnostics.DiagnosticsReceiver`
y `androidx.profileinstaller.ProfileInstallReceiver`). La app **NO declara `DUMP` como
uses-permission** (la lista autoritativa de `get_app_info` no lo incluye). Es comportamiento
estándar de las librerías AndroidX, no un permiso privilegiado pedido por la app.

## Flags de la aplicación

| Flag | Valor | Implicación |
|---|---|---|
| `android:usesCleartextTraffic` | **`true`** | Permite HTTP en claro a cualquier host → base del hallazgo P1 (doc 03) |
| `networkSecurityConfig` | **ausente** | No hay restricción de dominios ni pinning declarativo |
| `android:allowBackup` | **`true`** | Respaldo del app-data (incluye SharedPreferences con PII, doc 05) |
| `android:fullBackupContent` | `false` | Sin reglas de exclusión → respaldo completo por defecto |
| `android:debuggable` | ausente (false) | OK |
| `extractNativeLibs` | `false` | `.so` se cargan comprimidas del APK |

## Componentes exportados (superficie de ataque)

| Componente | Tipo | `exported` | Protección | Observación |
|---|---|---|---|---|
| `activities.SplashScreenActivity` | activity | true | — | LAUNCHER (normal) |
| `services.NotificationService` | service | true | intent-filter FCM | FCM (normal) |
| `receivers.PhonecallReceiver` | receiver | true | — | Escucha `PHONE_STATE` (detección de llamada de extorsión) |
| `activities.RegistroActivity` | activity | true | — | acción propia `CODIGO_VERIFICACION` |
| `activities.NavigationActivity` | activity | true | — | acciones propias (SOLICITUD_*, SUPERVISION, ANUNCIO) |
| **`services.SiguemeService`** | service | **true** | **ninguna** | **Otra app puede arrancar el seguimiento de ubicación** |
| **`services.LocationService`** | service | **true** | **ninguna** | **Otra app puede arrancar el servicio de ubicación** |

Los dos servicios de ubicación exportados sin permiso son el punto más débil del
manifiesto: cualquier app instalada puede invocarlos (`startService`) y potencialmente
disparar el comportamiento de ubicación/seguimiento. Ver doc 08, hallazgo 4.

## Receivers con custom actions (red de emergencia)

`NavigationActivity` declara intent-filters para `SOLICITUD_PENDIENTE`, `SOLICITUD_ACEPTADA`,
`CONTACTO_BORRADO`, `ANUNCIO`, `SUPERVISION` → son el bus de la **red de emergencia**
(solicitudes de contacto guardián, supervisión) que llega por FCM y se enruta por intents.
