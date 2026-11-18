# Arquitectura — Capacitor + Angular/Ionic

Fuente: `assets/capacitor.config.json`, `assets/capacitor.plugins.json` y los
chunks de `assets/public/*.js` (extraídos a `evidence/apk/assets/public/`).

## Pila

- **Capacitor** (`bundledWebRuntime: false`, `webDir: www`), appId `mx.wdd.sefin`,
  appName "Tesorería CDMX".
- Frontend **Angular** compilado (chunks con hashes, `main.*.js`, `polyfills`,
  lazy chunks numerados tipo `1716.4ad2…js`) con componentes **Ionic** (referencias
  a `ionicframework.com/docs`, `drifty.com`). El JS **no está ofuscado**: nombres
  de servicios, rutas y variables son legibles.
- Capa nativa Android mínima: WebView + plugins Capacitor.

## Plugins Capacitor declarados

| Plugin | Función |
|---|---|
| `@capacitor-mlkit/barcode-scanning` | **Escaneo de QR** (líneas de captura) |
| `capacitor-native-biometric` | **Login biométrico** (huella/rostro) |
| `@capacitor/push-notifications` | Push (FCM) |
| `@capacitor/inappbrowser` | **Navegador embebido** (pasarela de pago, doc 05) |
| `@capacitor/filesystem` + `@capacitor-community/file-opener` | Guardar/abrir comprobantes (PDF) |
| `@capacitor/app`, `@capacitor/haptics`, `@capacitor/splash-screen` | UX |
| `LocalNotifications` (cordova pref) | Notificaciones locales |

## Preferencias Cordova (de `capacitor.config.json`)

`ScrollEnabled:false`, `BackupWebStorage:"none"`, splash 3 s. `BackupWebStorage:none`
evita respaldar el web storage del WebView — bien para no filtrar sesión en backups.

## Dónde vive la lógica

Toda la lógica de negocio (login, consulta de adeudos, armado de pagos, llamadas a
API) está en los chunks JS de `assets/public/`, **no** en el DEX. Por eso el
análisis se centra en ese bundle: es legible y contiene URLs, config y — como se ve
en docs 04 y 05 — **secretos**.
