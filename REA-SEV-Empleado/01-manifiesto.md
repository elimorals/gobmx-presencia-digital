# 01 · Manifiesto

- **Package:** `mx.gob.sev.empleados` · versión 1.2 (102008).
- **minSdk 14 / targetSdk 22.** targetSdk 22 (Android 5.1) es la raíz de H2: en targetSdk < 28 el
  tráfico **cleartext HTTP está permitido por defecto** (sin necesidad de `usesCleartextTraffic` ni
  de un `networkSecurityConfig`). No hay NSC en el APK.
- **`hardwareAccelerated=true`.**

## Permisos (5, mínimos)

| Permiso | Uso |
|---|---|
| `INTERNET` | red |
| `ACCESS_NETWORK_STATE` / `ACCESS_WIFI_STATE` | estado de conexión |
| `WRITE_EXTERNAL_STORAGE` / `READ_EXTERNAL_STORAGE` | descargar/guardar comprobantes y constancias (PDF) |

Superficie de permisos **pequeña y razonable** — sin ubicación, SMS, contactos ni teléfono. El
riesgo no está en los permisos sino en la red (doc 03).

## Componentes

Cordova estándar: una `MainActivity` (WebView), `SplashScreen`, provider de `FileProvider` para
compartir PDFs. Sin servicios/receivers exportados sospechosos (shell Cordova típico).

## Config Cordova (`res/xml/config.xml`)

- `<content src="index.html">` — carga la app local.
- `<access origin="*">` — **whitelist abierta a cualquier origen** (ver H3, doc 04).
- `<allow-navigation href="tel:*">`, `launch-external yes`.
- Autor: `aplicacion.sev@gmail.com` / `http://www.sev.gob.mx` (correo Gmail, no institucional —
  anecdótico).
