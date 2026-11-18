# SDKs y servicios de terceros

Fuente: `assets/public/*.js`, `evidence/apktool/res/values/strings.xml`,
`assets/capacitor.plugins.json` y los `*.properties` de Play Services/Firebase.

## Firebase / Google (push y analítica)

De `res/values/strings.xml`:

| Clave | Valor |
|---|---|
| `project_id` | `app-pagos-tesoreria` |
| `gcm_defaultSenderId` | `1015908174795` |
| `google_app_id` | `1:1015908174795:android:8201caa2d7ca8c471f844e` |
| `google_api_key` | `AIzaSy…` (Android API key de Firebase) |

- **Firebase Cloud Messaging** para push (plugin `@capacitor/push-notifications`).
- La `google_api_key` de Android no es un secreto crítico: va en todos los APK y
  Google la restringe por package + firma. Se documenta como dato, no como fuga.

## Pusher (realtime)

Referencias a `pusher.com`, `js.pusher.com`, rutas `pusher/auth` y `pusher/user-auth`
en el bundle → canal realtime autenticado contra el backend. **No se localizó la
app key de Pusher** en este pase (no se afirma su valor).

## MLKit Barcode Scanning

`@capacitor-mlkit/barcode-scanning` (+ `play-services-code-scanner`,
`play-services-mlkit-barcode-scanning`) → escaneo de **QR** de líneas de captura
con la cámara. Justifica `CAMERA` y `FLASHLIGHT`.

## Plugins Capacitor (resumen, detalle en doc 02)

Biometría nativa, in-app browser (pago), filesystem + file-opener (comprobantes),
app, haptics, splash, local/push notifications.

## Mapas

`server.arcgisonline.com` (ArcGIS) como mapa base, coherente con permisos de
ubicación (¿sucursales/cajas/citas?). Sin evidencia del flujo exacto.
