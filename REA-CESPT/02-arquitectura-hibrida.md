# Arquitectura — Capacitor/Ionic (Angular)

## Confirmación de pila

- `assets/capacitor.config.json` presente + `assets/public/*.js` (chunks con hash tipo Angular:
  `1033.0d4c404c…js`). → **Capacitor** con app **Ionic/Angular**.
- `capacitor.config.json` (en `evidence/`):
  ```json
  { "appId": "io.ionic.starter", "appName": "Cespt", "webDir": "www",
    "plugins": { "PushNotifications": { ... } },
    "cordova": { "preferences": { "BackupWebStorage": "none", "SplashScreen": "screen", ... } } }
  ```
  - **`appId` = `io.ionic.starter`** → el **default del template de Ionic**; no lo personalizaron
    (descuido de build; el package del APK sí es `com.cespt.cesptApp`). No es vulnerabilidad, pero
    delata plantilla sin ajustar (doc 06, H4).
  - `BackupWebStorage = none` → el almacenamiento web no se respalda (mitiga parte de
    `allowBackup=true`, doc 01).
- No Flutter/Unity/.NET/React Native. La lógica es el bundle JS (sin `.map` → **sin source maps**,
  pero el JS minificado sigue siendo legible/decompilable).

## Identidad — Llave Baja California (OAuth)

El bundle referencia `https://www.llave.bajacalifornia.gob.mx/oauth.xhtml` (+ `RegistroCiudadano`,
`PreguntasFrecuentes`). La autenticación del ciudadano está **delegada a "Llave BC"**, el
proveedor de identidad del estado (el mismo ecosistema que usa Identidad Digital BC). Patrón
correcto: la app no maneja credenciales propias de identidad, usa OAuth contra el IdP estatal.
También hay un login propio (`identidaddigital/loginidentidad.aspx`).

## Backend

API propia `https://www.cespt.gob.mx/API/g/v...` + páginas ASP.NET (`.aspx`) para citas, pagos y
noticias. Todo bajo `www.cespt.gob.mx` por HTTPS. El detalle de rutas está en
`decompiled/endpoints.txt`.
