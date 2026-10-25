# Análisis dinámico

Complemento al análisis estático, ejecutado en un **dispositivo físico** para recuperar lo que
DexGuard ocultó (los endpoints). No se usó root ni se evadió ninguna protección: se capturaron
los **hostnames de destino** del tráfico, no el contenido cifrado.

## Entorno

| Elemento | Valor |
|---|---|
| Dispositivo | Motorola XT2435-3 (moto), Android 16, arm64-v8a, **sin root** |
| App | CFE Contigo 7.0.0 instalada vía `adb install-multiple` (splits del XAPK) |
| Captura | `mitmproxy 11.0.2` (en venv aislado) como proxy HTTP en `192.168.150.64:8080` |
| Proxy en teléfono | `adb shell settings put global http_proxy …` (revertido al terminar) |
| Método | Registro del `CONNECT`/`server_conn` (hostname destino). Sin CA instalada → el TLS no se descifra; solo se observa a qué dominios habla la app. |

## Hallazgo principal: el backend propio

Al arrancar (splash → login, **sin introducir credenciales**), CFE Contigo contactó un único
dominio propio:

```
CONNECT appcfecontigo.cfe.mx:443
SERVER_CONN  ('appcfecontigo.cfe.mx', 443)
```

**`https://appcfecontigo.cfe.mx`** es el backend que el análisis estático no pudo recuperar
(cifrado por DexGuard, doc 05). Todo el tráfico va por **HTTPS/443**; no se observó ninguna
conexión `http://` en claro.

## Resto del tráfico de arranque (terceros)

Hosts de terceros observados (confirman los SDKs del doc 07), todos HTTPS:

- Google/Firebase: `firebaseinstallations.googleapis.com`, `firebase-settings.crashlytics.com`,
  `firebaseinappmessaging.googleapis.com`, `app-measurement.com`, `digitalassetlinks.googleapis.com`,
  `notifications-pa.googleapis.com`, `www.googleapis.com`.
- Play / sistema: `play.googleapis.com`, `play-fe.googleapis.com`, `checkin.gstatic.com`,
  `securitydomain-pa.googleapis.com`, `googleads.g.doubleclick.net`.
- Motorola/Lenovo (del sistema, **no de la app**): `adapi.lenovomm.com`.

> El proxy global captura el tráfico de **todo el teléfono**, no solo de la app. Los hosts de
> Play y `lenovomm` son del sistema/fabricante; se listan por transparencia, no se atribuyen a
> CFE. El único dominio claramente de la app es `appcfecontigo.cfe.mx`.

## Lo que no se pudo obtener (y por qué)

- **Rutas `/api/...` y cuerpos**: requieren descifrar TLS, lo que a su vez exige instalar una CA
  en el **almacén de sistema** (solo con root) o **repackaging** con una CA de usuario. El
  dispositivo no está rooteado y CFE tiene anti-tamper (DexGuard + packer + ThreatMetrix, doc 05)
  que probablemente detectaría el repackaging. Queda como trabajo futuro con un dispositivo
  rooteado o un build de ingeniería.
- **Hook a `$$c()` (descifrador de strings) con Frida**: sin root se necesita `frida-gadget`
  reempaquetado; mismo riesgo de anti-tamper. No intentado.

## Otras extracciones vía ADB (sin root)

- **logcat durante el arranque**: CFE Contigo **no registra** URLs, tokens ni datos sensibles en
  logcat (solo trazas del sistema de ventanas y del cargador de clases). Buena higiene de logging
  en release — no hay fuga por logs. Flujo confirmado: `SplashActivity → LoginActivity`.
- **Permisos realmente concedidos** (`dumpsys package`): los permisos peligrosos están
  `granted=false` en instalación (se solicitan en runtime al usarse): `CAMERA`,
  `ACCESS_FINE_LOCATION`, `CALL_PHONE`. Coherente con el modelo de permisos de Android.
- **Versión instalada**: `versionName=7.0.0` — coincide con el análisis estático.

## Por qué no hay más que extraer sin root

Vía cable, sin root, se agotó lo accesible: APK (ya se tenía), tráfico a nivel de host
(dominio obtenido), logcat (limpio) y estado de permisos (confirmado). Lo que falta —rutas y
cuerpos HTTPS, y la lógica descifrada— vive **cifrado en tránsito (TLS) y en reposo (DexGuard)**;
abrirlo requiere romper TLS con una CA en el almacén de sistema (**root**) o reempaquetar la app
con `frida-gadget`/CA de usuario, lo que además chocaría con su anti-tamper (doc 05). No es una
omisión del método, es el techo del acceso sin root.

## Higiene

- El proxy del teléfono se **revirtió** al terminar (`settings delete global http_proxy`).
- mitmproxy corrió en un **venv aislado** (el del sistema estaba roto por un conflicto
  `passlib`/`bcrypt`); no se modificó el Python del usuario.
- **No se introdujeron credenciales** reales; solo se capturó el tráfico de arranque previo al
  login. La app CFE quedó instalada en el teléfono (se puede desinstalar con
  `adb uninstall mx.com.cfe.cfecontigo`).
