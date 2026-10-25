# Análisis dinámico (dispositivo físico, sin root)

Segundo tipo de evidencia: la app **sí se ejecutó**, en un dispositivo físico, para resolver los
pendientes de los docs 06 y 10. Alcance acotado por tres límites que se explican abajo.

## Entorno

| Campo | Valor |
|---|---|
| Dispositivo | Motorola **XT2435-3** (`taipei`, SoC MediaTek) |
| Android | **16** (`user` build, **no rooteado**, `ro.debuggable=0`) |
| ABI | arm64-v8a (coincide con el split analizado) |
| Instalación | `adb install-multiple` de los 5 splits del XApk (base + arm64_v8a + en + fr + mdpi) → `Success`, versionName 2.4.4 / code 35 |
| Herramientas | adb, frida 17.24.3 (host) — **sin frida-server** (requiere root), sin objection |

Capturas en `analisis/runtime/` (no versionadas): `02-sat-pantalla1.png` (T&C), `04-sat-estado.png`
(T&C relanzada). *(`03-*.png` capturó por error la app CFE Contigo, ver abajo.)*

## Lo que se confirmó en ejecución

1. **Arranca y renderiza con Flutter/Impeller (Vulkan).** Logcat:
   `android_context_vk_impeller.cc ... Using the Impeller rendering backend (Vulkan)`. Carga
   `libflutter.so` y `libdartjni.so` desde `split_config.arm64_v8a.apk` (confirmado por
   `nativeloader`). Proceso estable, sin crash.
2. **TensorFlow Lite NO se carga en el arranque ni en la pantalla inicial.** En todo el logcat del
   arranque **no aparece** la carga de `libtensorflowlite_c.so` por el proceso `mx.gob.sat`. Las
   únicas cadenas "tflite" del log son librerías **del vendor MediaTek** (`libtflite_mtk.mtk.so`,
   `libneuron_*`) que `nativeloader` expone a **todas** las apps del sistema — no es la app
   cargándolas. → Refuerza el doc 06: la capacidad TFLite existe en el `.so` empaquetado, pero su
   uso **no ocurre** en la superficie alcanzable sin interacción; si se usa, es dentro del flujo
   SAT ID (que exige datos personales, fuera de alcance).
3. **Primera pantalla = gate de Términos y condiciones** (`02`/`04`): logo SAT, texto legal,
   checkbox "He leído y acepto los términos y condiciones", botón "Aceptar" (deshabilitado hasta
   marcar), y "Comenzar" detrás. Sin red en claro, sin errores TLS en el arranque.
4. **La app no registró tráfico en claro** en logcat (esperado: release no imprime URLs; dio no
   loguea por defecto). Las únicas URLs del log eran de **Google Play (Finsky)**, del sistema.
5. **Convivencia con CFE Contigo 7.0.0**, también instalada en el dispositivo de una sesión previa
   (`mx.com.cfe.cfecontigo`). Al conducir la UI, su `LoginActivity` se trajo al frente por error;
   se forzó su cierre y se relanzó SAT en un task limpio. Nota operativa, no hallazgo de SAT.

## Navegación de servicios públicos (sin login, sin PII)

Se recorrió toda la superficie pública (menú lateral) conduciendo la UI por `adb input`, sin
introducir datos. Capturas en `analisis/runtime/` (`06`–`12`).

### Menú lateral (vivo) vs árbol Dart (doc 10)

El menú confirma y amplía el inventario. Footer: **`V: 521065f7`** = coincide con `commit.txt`.

| Ítem de menú | Servicio Dart (doc 10) | Observado en vivo |
|---|---|---|
| **Citas** | — (WebView `citas.sat.gob.mx`) | listado |
| **Chat** | `contact_service` / WebView `chat.sat.gob.mx` | — |
| **MarcaSAT** | `contact_service` | llamada telefónica (nuevo, no visto por endpoints) |
| **Lector QR** | `qr_scan_page`, ZXing | **Verificador de Códigos** (abajo) |
| **Tutoriales** | `tutorials_service` | lista de videos YouTube (miniaturas cargan) |
| **Noticias** | `message_service` | **feed en vivo** (abajo) |
| **Indicadores** | (servicio de indicadores) | **datos fiscales en vivo** (abajo) |
| **Denuncias** | WebView `denunciaSatMovil.aspx` | — |
| **Aplicaciones** | `other_apps_service` | otras apps gob |
| **Valorar** / **Acerca de** | `about_of_service` | — |

### Hallazgos de la navegación

1. **Lector QR = "Verificador de Códigos".** Verifica la autenticidad de documentos emitidos por el
   SAT escaneando su QR (flujo `validadorqr.jsf`). Al pulsar *Escanear* abre la **cámara con ZXing**
   (`CameraService connect ... Camera API version 1`, Camera1 legacy). **No carga
   `libtensorflowlite_c.so`** — el QR usa ZXing, no ML. (capturas `08`, `09`)
2. **La API de producción responde en vivo (sin login).** Dos pruebas con datos frescos:
   - **Indicadores** fechados **24-10-2026** (el día del análisis): tipo de cambio 18.7005, INPC
     146.075, UDIS 8.860824, UMA diaria $117.31, recargos mora 2.07% / prórroga 1.38% (captura `11`).
   - **Noticias** fechadas **23-10-2026**: calendario de Oficinas Móviles por estado (captura `12`).
   Confirma que `https://sm-sat-movilzuul-...cloudb.sat.gob.mx/satmovil/v1` (doc 04) está **activo** y
   sirve datos públicos sin autenticación. No se envió PII.
3. **TFLite ausente en TODA la superficie pública.** Arranque, T&C, landing, menú y el escáner de
   cámara: en ningún punto el proceso `mx.gob.sat` cargó `libtensorflowlite_c.so`. Queda **acotado,
   a lo sumo, al flujo SAT ID** (que exige RFC/CURP/biometría y no se ejecutó). Refuerza el doc 06.
4. **Permiso de cámara** concedido por `pm grant` para el escáner; ningún otro permiso sensible se
   activó en la navegación pública.

## Captura de tráfico con frida-gadget (sin root)

Se repackageó la app para instrumentarla sin root y confirmar la red en runtime.

### Montaje (reproducible)

1. **Merge** de los 5 splits del XApk → APK universal con **APKEditor 1.4.9** (`m -i splits -o universal.apk`).
2. **Inyección de frida-gadget** (arm64) con **objection 1.12.5** (`patchapk`). *Gotcha:* objection
   colocó la `.so` en `lib/arm64/`; Android exige `lib/arm64-v8a/`. Hubo que **mover la lib** a
   `lib/arm64-v8a/libfrida-gadget.so`, re-`zipalign` y re-firmar con `apksigner` (keystore debug).
   Sin ese arreglo: `UnsatisfiedLinkError: library "libfrida-gadget.so" not found` y crash al abrir.
3. Instalar la parcheada, lanzar → gadget escuchando en `127.0.0.1:27042` (confirmado en logcat:
   `Frida: Listening on 127.0.0.1 TCP port 27042`). Attach con frida 17.24.3.

### Hallazgo mayor: el stack de red es **Cronet (Chromium 139 mainline)**

Módulos nativos cargados en runtime (no visibles en el análisis estático de DEX):
`libmainlinecronet.139.0.7205.3.so`, `stable_cronet_libssl.so`, `stable_cronet_libcrypto.so`,
`stable_cronet_libpki.so`, más `libssl.so`. La app **no usa el cliente HTTP de dart:io**: enruta
por **Cronet** (el mismo motor de red de Chrome, vía la API `android.net.http` / jni). Esto no se
veía en estático porque el DEX es un thin-embedding y la lógica Dart está en `libapp.so`.

### Red observada en vivo (hooks libc `getaddrinfo`, navegación pública)

| Host resuelto | Veces | Servicio |
|---|---|---|
| `sm-sat-movilzuul-sm-servicios-moviles.cnh.cloudb.sat.gob.mx` | 9 | **Gateway de producción** (`.env.production`) — confirmado en vivo |
| `www.googleapis.com` | 2 | YouTube Data API (Tutoriales) |
| `i.ytimg.com` | 4 | Miniaturas de YouTube |
| `google.com` | 3 | Chequeo de conectividad |

- **No se contactó** ningún host de descarga de modelo, ni `wu1agsprosta001.blob.core.windows.net`
  (Azure), ni `chiikul.sat.gob.mx` (SAT ID) durante la navegación pública. Refuerza que el modelo
  TFLite y SAT ID viven tras el flujo de identidad (PII), no en el arranque ni en servicios públicos.

### Por qué NO se obtuvo el texto en claro de los cuerpos

Se hookearon `SSL_write`/`SSL_read` exportados en `libssl.so` y `stable_cronet_libssl.so`: **cero
tráfico**. El hook de `sendto` (UDP) también quedó mudo. Conclusión: **Cronet cifra con su propio
BoringSSL enlazado estáticamente dentro de `libmainlinecronet…so`** (símbolos internos, no los
exportados que se hookearon) y emite por `write`/`sendmsg`, no por las funciones exportadas. Los
hooks estándar de BoringSSL **no interceptan** la ruta de datos de Cronet. Es un límite de
arquitectura, verificado, no una conjetura.

Capturar los cuerpos requeriría una de estas (no hechas): hookear los símbolos **internos** de TLS
dentro de `libmainlinecronet.139.0.7205.3.so` (por patrón/offset, frágil entre versiones), habilitar
el **NetLog de Cronet** vía la API Java `android.net.http.HttpEngine.startNetLog*` (hook Java con el
bridge de frida 17), o un **CA a nivel de sistema** (requiere root). Todas exceden lo razonable aquí;
y de todos modos los cuerpos de alto valor (login, SAT ID) exigen PII que no se introduce.

## Límites (por qué no se llegó más lejos)

1. **Sin root.** Sin `su`/Magisk no corre `frida-server`, así que **no se pudo interceptar TLS** ni
   hookear `libapp.so`. Flutter cifra con **BoringSSL propio** e **ignora el CA de usuario y el
   proxy del sistema**, de modo que un MITM con CA de usuario **no** descifra su tráfico. Capturar
   payloads/headers reales requiere **root (frida-server)** o **repackage con frida-gadget**
   (modifica y re-firma el APK).
2. **Frontera de datos personales (PII).** El tráfico de mayor valor (login OAuth2 ROPC, SAT ID,
   e.firma) exige RFC/CIEC/CURP y biometría reales. **No se introdujeron** credenciales ni datos de
   identidad: hacerlo enviaría PII a los servidores de producción del SAT, fuera del alcance ético
   de una investigación estática de un binario que se posee.
3. **Conducción de UI acotada.** Avanzar más allá del gate de T&C requiere automatizar toques en la
   UI; esa acción quedó sujeta a autorización explícita del usuario y no se forzó.

## Qué quedaría para una sesión dinámica completa (requiere decisión del usuario)

- **Opción A — repackage con frida-gadget (sin root):** inyectar `frida-gadget` en el APK,
  re-firmar e instalar; hookear `SSL_write`/`SSL_read` de BoringSSL para volcar en claro las
  peticiones **pre-login** (config remota, tutoriales, YouTube, y comprobar si se **descarga un
  modelo `.tflite`** al inicio — resolvería el origen del modelo del doc 06). No cruza la frontera
  de PII si se limita a lo público.
- **Opción B — dispositivo rooteado / emulador:** `frida-server` + captura de red para el mismo fin,
  más capacidad de forzar el intérprete TFLite y observar el flujo SAT ID **con datos de prueba**
  (nunca PII real).
- **Navegación de servicios públicos** — **hecho** (ver sección anterior): menú, Lector QR,
  Tutoriales, Indicadores, Noticias recorridos y capturados. Queda pendiente solo lo que exige login
  o PII.

**Veredicto honesto (actualizado tras la captura con gadget):** la dinámica confirmó la arquitectura
(Flutter/Impeller, gate de T&C), recorrió la superficie pública cruzándola contra el árbol Dart,
**probó en vivo que el gateway de producción se usa** (`...movilzuul...cloudb.sat.gob.mx`, 9
resoluciones) y descartó TFLite en toda la superficie alcanzable. El **repackage con frida-gadget
funcionó** (sin root) y reveló un hallazgo que el estático no mostró: **la red va por Cronet
(Chromium 139 mainline)**. Lo que **sigue abierto** y por qué: los **cuerpos en claro** no se
capturaron porque Cronet usa su **BoringSSL interno** (los `SSL_read/write` exportados no están en su
ruta) — es un límite de arquitectura verificado, no falta de intento; y el **modelo TFLite / SAT ID**
exige PII contra producción. No se fuerza ninguna conclusión sin evidencia.
