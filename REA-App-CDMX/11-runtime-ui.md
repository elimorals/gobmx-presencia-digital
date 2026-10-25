# 11 — Runtime: ejecución y recorrido de UI

Primera capa **dinámica** del análisis. La app se instaló y ejecutó en un dispositivo real
y se documentó su comportamiento observable. Complementa (no sustituye) el análisis estático.

| Dato | Valor |
|---|---|
| Dispositivo | Motorola **XT2435-3** (`taipei`), **Android 16** (SDK 36), arm64-v8a + armeabi-v7a |
| Root | **No** (SELinux enforcing) |
| Instalación | **Google Play** (`installerPackageName=com.android.vending`), v4.5.2 (620) |
| Fecha | 2026-10-24 |
| Capturas | `evidence/11-home.png`, `12-mitaxi.png`, `13-utopias.png`, `14-movilidad.png` (no versionadas) |

## Hallazgo 1 — PairIP exige instalación legítima (license check)

Al instalar el bundle **por sideload** (`adb install-multiple`) la app **arranca el splash y
se autotermina**. En logcat:

```
ActivityRecord … mx.gob.cdmx.adip.apps/com.pairip.licensecheck.LicenseActivity
AndroidRuntime: VM exiting with result code 0, cleanup skipped.
ActivityManager: Process mx.gob.cdmx.adip.apps … has died … isKilledByAm=false reason=null
RestartModeController: markWaitRestartAppIsSuicided … mx.gob.cdmx.adip.apps
```

No es un crash: es **`com.pairip.licensecheck.LicenseActivity`** (la verificación de licencia
de PairIP, respaldada por el permiso `com.android.vending.CHECK_LICENSE`) que, al no
encontrar un veredicto válido de Google Play para una copia sideloaded, **sale limpiamente
(`exit 0`, "suicided")**. Reinstalada **desde Google Play**, el license check pasa y la app
corre con normalidad. Confirma en runtime la protección documentada en el doc 07.

## Hallazgo 2 — Home sin sesión (MainActivity)

`mx.gob.cdmx.adip.apps/.ui.activities.MainActivity` (ver `evidence/11-home.png`). El home es
**navegable sin iniciar sesión con Llave** y ya trae datos del servidor:

- Encabezado "Gobierno de la Ciudad de México — Capital de la Transformación"; saludo genérico
  "Hola" (sin nombre → sin sesión).
- Widget **AIRE Y SALUD** con datos vivos: **22°**, calidad del aire **Buena**, **UV Bajo (0)**
  y **Hoy No Circula "H1:Pares H2:Todos"**.
- Rejilla **Servicios**: Vehículos, Movilidad, Cultura y entretenimiento, Salud y bienestar,
  Seguridad, Denuncia y anticorrupción.
- **Mis atajos**: Recargar tarjeta, Información UTOPIAS, Quejas y reportes (SUAC), Denuncia de
  delito, Incidentes.
- Nav inferior: **Inicio · WiFi · Pagos · Documentos · Mi perfil**.

Confirma en vivo el mapa de módulos del doc 02.

## Hallazgo 3 — Deeplinks funcionan (confirmado)

| Deeplink | Activity resuelta | Resultado |
|---|---|---|
| `appcdmx://mitaxi` | `…mitaxi.ui.activities.UserHomeActivity` | Abre **Mi Taxi** ("Pedir Taxi", "Abordar Taxi", "¿A dónde vas?"). **Solicita ubicación precisa** al entrar (`GrantPermissionsActivity`: "Change … from approximate to precise?"). |
| `appcdmx://utopias` | `…culturaentretenimiento.ui.activities.CulturaEntretenimientoActivity` | Abre **UTOPIAS** con catálogo real del servidor (Escuela Agroecológica, Lab. de química ambiental, Taller de Biodiversidad, Exploradores de la naturaleza, Ecotecnologías). Sin login. |

Confirma los `intent-filter` `appcdmx://` y las Activities exportadas del doc 01.

## Hallazgo 4 — Movilidad Integrada usa Google Maps + capa de transporte

`…movilidadintegrada.ui.activities.MovilidadIntegradaActivity` (`evidence/14-movilidad.png`):
base de **Google Maps** (atribución "Google" visible) con **estilo oscuro** —coincide con
`res/raw/style_map_*.json`— y onboarding "Checa el mapa real de la Ciudad o del Transporte"
para **alternar entre mapa de ciudad y la Red de Transporte** (la vista SVG de `assets/web/`,
doc 10). Confirma en vivo el SDK de Google Maps (doc 08) y los datasets offline (doc 10).

## Comportamiento de permisos observado

- Mi Taxi fuerza **ubicación precisa** en la entrada (coherente con `ACCESS_FINE_LOCATION` +
  `ACCESS_BACKGROUND_LOCATION`, doc 01). No se concedió durante el análisis.
- No se observaron datos personales en pantalla (sin sesión Llave iniciada); no se capturó
  ninguna pantalla con PII.

## Límite alcanzado (lo que runtime sin root NO resuelve)

Con la app **corriendo** se validó la capa de UI/flujos. Los 4 puntos profundos
(contenido de los 22 blobs PairIP, claves Calypso en `libcalypsoHceNatives.so`, confirmación
de cert pinning por MITM, fugas de Analytics) **siguen requiriendo Frida**, imposible en este
dispositivo (Android 16, sin root; reempaquetar con gadget lo detecta PairIP — hallazgo 1).
Para esos cuatro se necesita un **emulador rooteado con Play** o un teléfono con root.

## Procedencia

Instalación desde Google Play sobre dispositivo propio; análisis de UI observable, sin evadir
protecciones. Se tocó **únicamente** `mx.gob.cdmx.adip.apps`. El ajuste temporal
`stay_on_while_plugged_in` se restauró a su valor por defecto. Capturas en `evidence/`
(no versionadas por `.gitignore`).
