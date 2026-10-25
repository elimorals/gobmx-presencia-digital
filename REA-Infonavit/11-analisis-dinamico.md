# 11 · Análisis dinámico (dispositivo real)

Ejecución en dispositivo físico, **sin root**, el 2026-10-24.

| Dato | Valor |
|---|---|
| Dispositivo | Motorola XT2435-3 (`ZY22KQ8WZD`) |
| Android | 16 (SDK 36) |
| Root | No (`which su` vacío, uid shell) |
| App | `com.infonavit.micuentainfonavit` 5.3.48 (99), instalada desde `_apk/base.apk` |

## 1. Autenticidad confirmada en vivo

`dumpsys package` tras instalar:

```
Signatures: [84:B7:C1:48:B8:3F:92:28:32:DD:45:8F:58:05:34:A2:04:72:7B:56:DA:EC:64:06:FB:30:4A:15:49:11:13:1D]
signatures=PackageSignatures{… version:3 …}   apkSigningVersion=3
```

La firma instalada **coincide exactamente** con la del análisis estático (doc 09) y es
**APK Signature Scheme v3** (lo que `keytool` no podía ver, solo veía v1). Refuerza que el
artefacto es el auténtico de INFONAVIT.

GMS reporta en logcat `GoogleCertificatesRslt: not allowed: pkg=…sha256=[84b7c148…]` por ser
sideload, y **la app igual arranca** → confirma H5: no hay gate de attestation/anti-tamper que
bloquee la ejecución fuera de Play.

## 2. Permisos y superficie (en vivo)

- Concedidos en instalación: INTERNET, NETWORK/WIFI_STATE, USE_BIOMETRIC/FINGERPRINT, WAKE_LOCK,
  AD_ID, ADSERVICES, INSTALL_REFERRER. Localización (`FINE`/`COARSE`) queda `granted=false` hasta
  que el usuario la otorgue en runtime (correcto).
- Actividades exportadas confirmadas: `SplashActivity` (LAUNCHER), `RegistroActivity`,
  `RestablecerContrasenaActivity` — coincide con el manifiesto (doc 01).
- Logcat de arranque: **no filtra URLs ni tokens** (logging off en release), como CFE/IMSS.
  Sí se observa `ProviderInstaller: Installed default security provider AndroidOpenSSL`.

## 3. H1 demostrado en vivo — MITM sin root ni CA de sistema

Montaje: proxy por USB (`adb reverse tcp:8080 tcp:8080` + `settings put global http_proxy
127.0.0.1:8080`) y `mitmdump` en la Mac. **No se instaló la CA de mitmproxy** en el teléfono.

Resultado: la app **aceptó el certificado de mitmproxy** y completó el TLS, exponiendo el tráfico
en claro. Esto **sólo es posible porque el TrustManager acepta cualquier certificado** (H1,
doc 03). En una app con validación normal, el MITM sin CA de sistema habría fallado en el handshake.

Flujo real capturado (arranque, **sin login**):

```
POST https://serviciosweb.infonavit.org.mx/omni/mobile-version/api/v1/check  HTTP/2.0
  x-api-key: 2/s5T4F8al2fJbr07Xoea/seGNx9LQ286o6kDFsxzvPDtkl8IzF90Thxw…   (redactado)
  user-agent: MCI/5.3.48 (XT2435-3; Android 16; Scale/3.00)
  content-type: application/json
  body: {"arg1":"00000000000","arg2":"5.3.48","arg3":"IAPP","arg4":"ANDROID"}

<< HTTP/2.0 200 OK
  server: nginx · x-powered-by: Servlet/3.0 · x-global-transaction-id: 2710f2d4…
  body: {"update":"NONE","code":"0000","message":"No requiere actualización."}
```

Evidencia completa en `evidence/mitm/captura-legible.txt` (y `flows.mitm`, gitignored).

### Qué añade sobre el estático

- **Confirma H1 empíricamente**: intercepción exitosa sin root. Antes era "confirmado en estático".
- **Backend real revelado**: `serviciosweb.infonavit.org.mx` resuelve a **Akamai** (23.223.240.80/.90)
  y detrás hay **nginx + IBM WebSphere** (`Servlet/3.0`, `x-backside-transport`,
  `x-global-transaction-id` = patrón de IBM API Connect/DataPower).
- **Patrón de API real**: `/omni/{servicio}/api/v{n}/…` — ruta concreta que el estático (solo
  base-URLs) no daba. El check de versión es `/omni/mobile-version/api/v1/check`.
- **`x-api-key` en vivo**: viaja en cada request y, por H1, es **interceptable y reutilizable** por
  un atacante en la red. Redactado aquí; valor en la captura de evidencia.
- **User-Agent** real: `MCI/5.3.48 (modelo; Android ver; Scale/x)`.

Solo se capturó el endpoint de arranque porque el resto del tráfico (catálogos, registro,
precalificación) requiere navegar la UI, y el alcance acordado fue **sin login**. Un único request
basta para demostrar H1; para mapear más endpoints haría falta recorrer la UI.

## 4. Estado del dispositivo tras la prueba

- Proxy global **removido** (`settings put global http_proxy :0`, `adb reverse --remove`).
- La app **queda instalada** (como CFE/IMSS de sesiones previas), pendiente de que el usuario
  decida desinstalarla: `adb uninstall com.infonavit.micuentainfonavit`.

## Límites

Sin root no se interceptaron rutas que usen pinning (no aplica aquí: no hay pinning) ni se accedió
al sandbox de datos de la app. No se hizo login ni se capturó tráfico autenticado (fuera de
alcance). La demostración de H1 es sobre un endpoint no autenticado; el resto del tráfico se
comportaría igual por usar el mismo cliente trust-all (doc 03).
