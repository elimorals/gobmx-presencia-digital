# 14 — Análisis dinámico (dispositivo por cable)

Ejecución real de la app en un dispositivo físico conectado por USB. Artefactos en
[`evidence/dynamic/`](evidence/dynamic/).

## Entorno

| | |
|---|---|
| Dispositivo | **Motorola XT2435-3** (`taipei`), serial `ZY22KQ8WZD` |
| SO | **Android 16 (API 36)**, `ro.build.tags=release-keys` |
| Root | **No** (`su` ausente) |
| Depuración | App **no debuggable** ("process cannot load the jdwp agent") |
| Instalación | `adb install-multiple base.apk config.arm64_v8a.apk` → **Success**; `primaryCpuAbi=arm64-v8a` |

## Arranque (logcat, tag `Unity`)

La app inicia y Unity reporta su identidad (coincide con el análisis estático):

```
Built from '6000.2/respin/6000.2.7f1-2eb0c79396db' branch,
  Version '6000.2.7f2 (2b518236b676)', Build type 'Release',
  Scripting Backend 'il2cpp', CPU 'arm64-v8a', Stripping 'Enabled'
Company Name: banxico     Product Name: BilletesMx
Device Model 'motorola XT2435-3', OS 'Android OS 16 (API 36)'
SystemInfo CPU = ARM64 ... Cores = 8, Memory = 7589mb
```

Confirma **Unity 6000.2.7f2**, IL2CPP, **stripping activado**, firma de empresa `banxico`.
Log completo en [`evidence/dynamic/unity-boot-log.txt`](evidence/dynamic/unity-boot-log.txt).

Aparece un log de la app `Disabled!! **********` seguido de un stacktrace nativo
(libunity/libil2cpp) durante la inicialización de servicios. **No es un crash** (no hay
señal/tombstone) y la app continúa; **qué queda "Disabled" no se puede afirmar** sin el
cuerpo del método que lo emite.

## Telemetría en runtime — confirmada

La app crea `files/Unity/local.<hash>/Analytics/` y escribe su configuración de telemetría
(pulled a [`evidence/dynamic/analytics-config.json`](evidence/dynamic/analytics-config.json)):

```json
{"analytics":{"enabled":true},
 "connect":{"limit_user_tracking":false,"player_opted_out":false,"enabled":true},
 "performance":{"enabled":true},
 "dynamic":{"coreBusinessMetrics":{"enabled":true,"timeToWaitForUserInfoS":60},
            "analytics":{"shouldCollectAutomation":true,"timeToWaitForUserInfoS":60}}}
```

Es decir, **en ejecución**: Unity Analytics **habilitado**, Performance Reporting
**habilitado**, `limit_user_tracking=false`, `player_opted_out=false`, métricas de negocio
y recolección automática **activas**. Esto confirma de forma dinámica el hallazgo H1 de
telemetría activa (ver [08](08-telemetria-ads.md), [12](12-privacidad-hallazgos.md)).
`values` registra `engine_ver 6000.2.7f2`, `app_ver 5.5`.

## Almacenamiento creado

`/sdcard/Android/data/org.banxico.dge.movil/`:
- `files/il2cpp/` — copia en runtime de `global-metadata.dat` y recursos.
- `files/Unity/.../Analytics/` — telemetría (arriba).
- `cache/vulkan_pso_cache.bin` — caché de shaders Vulkan.
- **No** se observó `billetesMX.json` descargado en la corrida corta (el catálogo remoto
  —ver [05](05-red-endpoints.md)— puede requerir navegar el menú o no haber datos nuevos).

## Límites del dinámico en este dispositivo

Sin root y con una app `targetSdk 36`, **no fue posible**:

- **Leer el sandbox** `/data/data/org.banxico.dge.movil` (sin `run-as`, app no debuggable).
- **Interceptar TLS** (MITM): en Android 9+ la app sólo confía en CAs del **sistema**; una
  CA de usuario no basta sin root o reempaquetado. Por eso **no se capturó el host real del
  catálogo** ni si se dispara Google TTS — se conocen las plantillas por el dump
  ([05](05-red-endpoints.md)), no el tráfico en vivo.
- **Frida**: requeriría `frida-server` (root) o un gadget inyectado reempaquetando el APK.
- La captura de pantalla sale **negra** (la superficie Unity es un `SurfaceView` que
  `screencap` no compone sin root); no implica que la app falle.

## Instrumentación con Frida Gadget (sin root) — realizada

Como el equipo no tiene root, se reempaquetó la app con **Frida Gadget** para hookear la
red **dentro del proceso** (sin MITM ni CA de usuario). Procedimiento (artefactos en
[`evidence/dynamic/frida/`](evidence/dynamic/frida/)):

1. `apktool d base.apk` → inyección de `System.loadLibrary("gadget")` en un `<clinit>`
   nuevo de `com.unity3d.player.UnityPlayerActivity` (`UnityPlayerActivity-patched.smali`).
2. `lib/arm64-v8a/libgadget.so` (Frida 17.24.3 arm64) + `libgadget.config.so` (modo
   `listen`, `on_load: resume`) añadidos al base.
3. `apktool b` → **re-firma de base + split** con una misma llave propia (`CN=REA`,
   apksigner) para que `install-multiple` acepte ambos. El **package name se mantiene**
   (`org.banxico.dge.movil`), por lo que la **licencia EasyAR sigue válida**.
4. `adb install-multiple` de los dos APK re-firmados → **Success**.
5. Arranque: el gadget carga y queda en **LISTEN 127.0.0.1:27042** (verificado en
   `/proc/net/tcp`). Se adjunta `frida -U -n Gadget -l net-hook.js`.

El hook ([`net-hook.js`](evidence/dynamic/frida/net-hook.js)) engancha correctamente
`getaddrinfo`, `android_getaddrinfofornet`, `connect` y **8 símbolos `SSL_write/SSL_read`**
(BoringSSL dentro de los `.so`). La instrumentación **funciona**.

### Resultado: la app no genera red por sí sola

En **tres capturas (50 s, 95 s y 180 s con navegación de usuario)** —app viva y en primer
plano (PID, `topResumedActivity`/`mCurrentFocus` = la app)— el hook registró **cero eventos
de red**: ni una resolución DNS, ni un `connect`, ni TLS. El proceso **no abrió un solo
socket** en ~5.5 min acumulados. Conclusiones:

- La app es **offline-first**: arranca y opera sobre el `bills.json` embebido sin tocar la
  red. La descarga del catálogo (`billetesMX.json`, [05](05-red-endpoints.md)) **no se
  dispara en el arranque** — está tras interacción de usuario o `hostUrl` va vacío.
- La **telemetría de Unity**, aunque **configurada como activa** (ver [08](08-telemetria-ads.md)),
  **no transmitió** en ninguna captura. El motor registró al arranque un log
  `Disabled!! **********` durante la init de servicios: **hipótesis** (no confirmada sin el
  cuerpo del método) de que Unity Gaming Services queda **deshabilitado en runtime** pese a
  la config `enabled:true`, quizá por falta de project ID enlazado. Resultado neto
  observado: **no sale analítica a la red**.
- No se observó llamada a **Google TTS** (coherente con que la clave va vacía,
  [11](11-accesibilidad-tts.md); el TTS accesible usaría el motor nativo de Android).

### Captura interactiva no lograda — la app no renderiza en este equipo

Se intentó navegar la app con el gadget adjunto para forzar el tráfico del catálogo/TTS,
pero **la app no llega a una UI usable en este dispositivo** (Motorola XT2435-3, Android
16): pantalla negra / crash. Esto **ya ocurría con el APK original sin parchear** (primer
arranque: log `Timeout (2000 ms) while trying detaching primary window` + `SurfaceView`
destruida), por lo que **no es efecto de la instrumentación** sino una incompatibilidad de
render de la app Unity/EasyAR con este equipo.

Verificación de que la instrumentación sí funciona: Frida adjunta al gadget (`frida-ps -U`
muestra `Gadget`, puerto 27042 en LISTEN), coloca los hooks y lee memoria sin error. El
único bloqueo es que la app **crashea antes de instanciar `NetworkController`** (que vive en
la escena del catálogo), así que `NetworkController..ctor`/`Start()` **nunca se llaman** y
no hay tráfico que capturar. En ~8 min acumulados de captura: **0 eventos de red**.

**Para completarlo** (si algún día se retoma) bastaría un **emulador o un equipo donde la
app renderice**: los hooks (`net-hook.js`, `frida-nc-fields.js` con los RVA/offsets ya
resueltos) capturarían `hostUrl` y el HTTP en claro de inmediato. El APK instrumentado
(re-firmado `CN=REA` + `libgadget.so`) queda instalado en el dispositivo; se desinstala con
`adb uninstall org.banxico.dge.movil`.
