# 06 — Ofuscación y protecciones

## Ofuscación de código (R8)

- Código propio y de terceros **renombrado por R8**: clases de una letra (`V`, `W8.l`, `B.i`),
  campos como `f33567c`/`f39885l`. El `Application` real (`BaseApp`) extiende `W8.l` ofuscada.
- **Sin cifrado de strings global**: `BaseApp.onCreate` decompila con strings legibles
  (`FirebaseCrashlytics`, `RealmConfigs`, etc.). Contrasta con **CFE Contigo**, que sí usa cifrado
  de strings estilo DexGuard. Aquí la protección es R8 estándar + ocultamiento de secretos en
  nativo (abajo), no DexGuard.
- 28 DEX, 17,984 clases top-level. Los nombres de dominio propios (p. ej. `commons.data.*`) se
  **conservan** parcialmente, lo que facilitó el mapa de módulos.

## Secretos fuera del bytecode (ocultamiento nativo)

Las claves/licencias no están en el DEX sino en librerías nativas:

| `.so` | Contenido inferido |
|---|---|
| `libkeys.so`, `libkeys_core.so` | Material de claves de la app |
| `libidx_facial_keys.so` | Claves del módulo facial IDx / licencia Daon |

Esto explica por qué `strings` sobre los DEX no revela la licencia Daon ni secretos de cliente.
La base de API, en cambio, sí está en `strings.xml` (no es un secreto, es configuración).

## Anti-tamper / integridad del entorno

- `libtoolChecker.so` y `libFaceToolChecker.so` + string `com.daon.sdk...ToolChecker` →
  **verificación de herramientas** (detección de depuradores/hooking/entornos manipulados),
  típica de los SDK de Daon para proteger la captura biométrica.
- `com.daon.sdk.device.*` evalúa propiedades del dispositivo (posible detección de root/emulador).
- No se confirmó una rutina propia de detección de root de Procesar; la protección de integridad
  parece provenir de los SDK de identidad (Daon) más que de la capa de la app.

> No se verificó en runtime la efectividad de estas comprobaciones (análisis estático). Un bypass
> con Frida requeriría instrumentación, fuera del alcance de este pase.

## Empaquetado / carga de nativo

- `android:extractNativeLibs="false"` → las `.so` se cargan comprimidas desde el APK (no se
  extraen al disco), reduciendo superficie en el sistema de archivos.
- No se observó un *packer* del DEX (los DEX cargan y decompilan normalmente en JADX), a diferencia
  de la `libb1bd.so` empaquetada de CFE.

## Integridad de distribución

- Firma **APK Signature Scheme v2 + v3** verificada (doc 09).
- **Source Stamp de Google Play** verificado → el artefacto proviene de Play Store, no de un
  re-empaquetado de terceros. Buena señal de cadena de suministro (mejor que el CFE de APKPure).

## Resumen

| Protección | Estado |
|---|---|
| Ofuscación de nombres (R8) | Sí |
| Cifrado de strings (DexGuard) | **No** (R8 estándar) |
| Secretos en nativo | Sí (`libkeys*`, `libidx_facial_keys`) |
| Anti-tamper / tool checker | Sí (vía SDK Daon) |
| Packer de DEX | No observado |
| Pinning de red | **No** (ver doc 05) |
| Anti-captura **FLAG_SECURE** | **Sí** — confirmado en runtime (doc 10): bloquea screenshots/grabación/Recents |
| Firma v2/v3 + Source Stamp | Sí |
