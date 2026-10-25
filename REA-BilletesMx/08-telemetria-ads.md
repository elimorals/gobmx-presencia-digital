# 08 — Telemetría, analytics y Unity Services

A diferencia de lo que sugería sólo el DEX, la extracción del assetbundle `data.unity3d`
(UnityPy; ver [07](07-assets-unity.md)) revela que **Unity Gaming Services / telemetría de
Unity está configurada y activa**: sus endpoints están embebidos en los assets.

## Endpoints de telemetría Unity (hallados en `data.unity3d`)

| Endpoint | Servicio |
|---|---|
| `https://api.uca.cloud.unity3d.com/v1/events` | **Unity Cloud Analytics** (UCA) — eventos de uso. |
| `https://cdp.cloud.unity3d.com/v1/events` | **Cloud Diagnostics Platform** — eventos de diagnóstico. |
| `https://config.uca.cloud.unity3d.com` | Config remota de analytics. |
| `https://perf-events.cloud.unity3d.com` | **Performance Reporting** — métricas de rendimiento. |
| `https://engine-data.services.api.unity.com/v1/stack-traces` | **Crash/Exception reporting** — trazas de pila. |
| `https://engine-data.unity3d.com` | Datos del motor. |
| `https://dashboard.unity3d.com` | Panel de UGS. |

Es decir, la app **envía a Unity**: eventos de analítica de uso, diagnósticos, métricas de
rendimiento y **trazas de crash**. Coincide con los módulos `UnityAnalyticsModule` /
`UnityAnalyticsCommonModule` y `AnalyticsSessionInfo` vistos en la metadata.

### Confirmado en ejecución (dinámico)

Al correr la app en un dispositivo real (ver [14](14-analisis-dinamico.md)) escribe su
config de telemetría en `files/Unity/local.<hash>/Analytics/config`
([`evidence/dynamic/analytics-config.json`](evidence/dynamic/analytics-config.json)):

```json
{"analytics":{"enabled":true},
 "connect":{"limit_user_tracking":false,"player_opted_out":false,"enabled":true},
 "performance":{"enabled":true},
 "dynamic":{"coreBusinessMetrics":{"enabled":true},"analytics":{"shouldCollectAutomation":true}}}
```

Telemetría **configurada como habilitada**, **sin opt-out** (`player_opted_out=false`) y
**sin limitar el tracking** (`limit_user_tracking=false`), con métricas de negocio y
recolección automática activas. La app lo deja escrito en disco al arrancar.

**Pero** la captura de red con Frida (ver [14](14-analisis-dinamico.md)) **no observó
ninguna transmisión** a los endpoints de Unity en ~5.5 min: el proceso no abrió sockets. Al
arranque el motor registra un log `Disabled!!` en la init de servicios. **Lectura
honesta:** la telemetría está **configurada activa y sin opt-out** (capacidad presente y
lista), pero **no se observó que enviara datos** en esta sesión —posiblemente UGS queda
deshabilitado en runtime por falta de project ID, o envía por lotes muy espaciados. La
*capacidad* de recolección es real; el *envío* no se confirmó.

## Publicidad (Unity Ads)

| Señal | Evidencia | Estado |
|---|---|---|
| Activities `com.unity3d.services.ads.adunit.AdUnit*` | Manifiesto | Declaradas (SDK Unity Ads). |
| Permiso `com.google.android.gms.permission.AD_ID` | Manifiesto | Añadido por Unity Services. |
| Clases de juego de Unity Ads (`Advertisement`, `UnityAds*`) | metadata | **No** aparecen con uso activo. |

No se observan **anuncios** activos ni endpoints de servidores de ads (sólo los de
analytics/diagnóstico). El permiso `AD_ID` y las activities `AdUnit*` son el footprint del
SDK de Unity Services; aun así, `AD_ID` + Unity Analytics permiten **asociar un
identificador de publicidad** a los eventos.

## Qué NO hay

En los 36,453 identificadores de metadata y en las URLs del bundle **no** aparecen SDKs de
terceros: Firebase/Crashlytics, Facebook, AppsFlyer, Adjust, Sentry, App Center. Toda la
telemetría es **de Unity**, no de terceros ajenos.

## Conclusión

La telemetría **no es inexistente ni "sólo módulos por defecto"**: Unity Gaming Services
(analytics + diagnóstico + rendimiento + crash) está **configurado con endpoints activos**.
Para una app de gobierno conviene valorar: (1) **desactivar Unity Analytics/UGS** si no se
necesita, (2) **quitar `AD_ID`** y las activities de Ads, y (3) declarar esta recolección
en el aviso de privacidad. No se envían datos a terceros ajenos a Unity, y no hay
publicidad activa. El volumen/contenido exacto de los eventos requeriría **captura
dinámica** (ver [12](12-privacidad-hallazgos.md)).
