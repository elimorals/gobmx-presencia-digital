# 10 — Análisis dinámico (dispositivo real)

Pase dinámico sobre un dispositivo físico para cerrar los pendientes del análisis estático.

## Entorno

| Campo | Valor |
|---|---|
| Dispositivo | Motorola **XT2435-3** (`taipei`), serial `ZY22KQ8WZD` |
| Android | **16** (API 36), build type **`user`** (producción) |
| Root | **No** (`su` ausente; sin Magisk) |
| ABI | `arm64-v8a` (abilist incluye `armeabi-v7a`) |
| App | AforeMóvil **6.0.15** instalada desde el XAPK analizado (`install-multiple`, 20 splits) |
| Herramientas | `adb` 37.0.0 (REA ADB bridge) |

> Build `user` sin root: **no** se pueden leer datos privados de la app (DataStore/Realm en el
> sandbox) ni correr `frida-server`. Por eso los puntos que requieren ese acceso quedan abiertos
> (ver §Pendientes). Lo que **sí** se probó, sin tocar datos personales (la app nunca se logueó),
> es la **superficie exportada** lanzando sus activities desde fuera (uid `shell`).

## Comportamiento de los componentes exportados (lanzados en frío, sin sesión)

`adb shell am start -n mx.com.procesar.aforemovil/<activity>`; capturas en `evidence/dyn_*.png`.

| Activity | Resultado | Lectura |
|---|---|---|
| `...ui.main.MainActivity` (launcher) | **Splash** de AforeMóvil, la app arranca | La app es compatible con Android 16 |
| `...ps.facial.ui.main.FacialActivity` *(exportada)* | **Crash** `java.lang.IllegalArgumentException: Configuration parameters expected` en `Zj.a.onCreate` | Falla cerrada: la activity base exige parámetros de configuración que el flujo interno provee |
| `...ps.facial.base.activity.IdxFacialCaptureActivity` *(exportada)* | Pide **"Unlock with PIN or fingerprint"** (6 dígitos) | Gate del **keystore Daon**: no captura sin autenticación local |
| `...home.main.HomeActivity` *(exportada)* | Pantalla **negra** (FLAG_SECURE, ver abajo) con un control suelto | No hay fuga de datos de sesión |
| `...preexistence.main.PreexistenceActivity` *(exportada)* | Diálogo del sistema **"This app isn't compatible with the latest version of Android"** | Falla cerrada |

### Traza del crash de FacialActivity
```
E AndroidRuntime: FATAL EXCEPTION: main
E AndroidRuntime: Process: mx.com.procesar.aforemovil
E AndroidRuntime: java.lang.IllegalArgumentException: Configuration parameters expected
E AndroidRuntime:     at Zj.a.o(SourceFile:1327)
E AndroidRuntime:     at Zj.a$a.invokeSuspend(SourceFile:44)
E AndroidRuntime:     at Zj.a.onCreate(SourceFile:50)
E AndroidRuntime:     at mx.com.procesar.ps.facial.ui.main.FacialActivity.onCreate(SourceFile:1)
```

### Conclusión sobre el hallazgo #6 (superficie exportada)
Confirmado en runtime: **ningún `Intent` externo pelón logra** disparar una captura biométrica ni
exponer contenido autenticado. Las cuatro activities exportadas **fallan cerradas** (crash, gate de
PIN/huella, pantalla vacía o diálogo de incompatibilidad). El `exported="true"` sigue siendo una
superficie más ancha de lo necesario, pero su **explotabilidad práctica por intents ingenuos es
baja**.

> Caveat: no se probó un `Intent` **con** los "configuration parameters" que espera `FacialActivity`.
> Reconstruir esos extras (y si `CreateEvaluation` acepta un contexto controlable por el invocador)
> requeriría más reversing del flujo de `register`/`idxfacial`; queda como vector abierto.

## Controles de seguridad observados en runtime

| Control | Evidencia | Lectura |
|---|---|---|
| **FLAG_SECURE** | `dumpsys window` sobre `MainActivity`: `fl=LAYOUT_IN_SCREEN SECURE ...` | La app **bloquea capturas de pantalla, grabación y miniatura en Recents** de su contenido. Confirmado: la pantalla post-splash sale **negra** (`evidence/dyn_MainActivity_postsplash.png`). Control **positivo**, correcto para una app financiera; también implica que no se puede documentar por captura el contenido real sin root/Frida. |
| **Permisos peligrosos diferidos** | `dumpsys package` runtime perms | `CAMERA`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `POST_NOTIFICATIONS` → `granted=false` (se solicitan en runtime al usarse). Solo auto-concedidos: `USE_BIOMETRIC`/`USE_FINGERPRINT` (normales) y `AD_ID`/`ACCESS_ADSERVICES_AD_ID`. |
| **Firebase activo al arranque** | logcat `pid` del app | `FirebaseApp` DEFAULT init OK, **Crashlytics 20.0.1**, **Firebase Sessions 3.0.1**, Analytics (`FA`); crea sesiones de Crashlytics en cold start. Confirma la telemetría del doc 08 en vivo. |

> El proceso se registra en logcat como `esar.aforemovil` (refuerza la marca e-SAR del backend).
> No se observaron URLs en logcat (R8 elimina los `Log` del app y los SDK no imprimen endpoints).

## Pendientes que requieren root (no disponible en este build `user`)

| Pendiente | Por qué está bloqueado |
|---|---|
| Procedencia de la llave de Realm (`realmEncryptionKey()`) | Hook con Frida requiere `frida-server` (root) o repackaging con **frida-gadget** + resign; además el **`toolChecker`/`FaceToolChecker`** de Daon (doc 06) puede detectar la instrumentación |
| Almacenamiento del refresh token (Keystore vs. DataStore en claro) | Leer `/data/data/mx.com.procesar.aforemovil/` exige root o `run-as` (la app no es `debuggable`) |
| Captura de tráfico a `api.esar.io` (confirmar ausencia de pinning) | Requiere proxy + CA de sistema **y** loguear con credenciales reales del SAR (se evita por privacidad) |

Para cerrarlos haría falta un dispositivo **rooteado** (o un build `userdebug`/emulador) y, aun así,
sortear el anti-tamper de Daon. Fuera del alcance de este pase sin root.

## Limpieza

La app quedó instalada y detenida (`am force-stop`) tras las pruebas; no se creó ninguna sesión ni
se introdujeron datos personales. Se puede desinstalar con
`adb uninstall mx.com.procesar.aforemovil`.
