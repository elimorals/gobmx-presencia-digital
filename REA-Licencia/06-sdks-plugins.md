# 06 · SDKs y plugins

Fuente: `cordova_plugins.js`, carpeta `assets/public/plugins/`, `.so` del APK, manifiesto.

## Plugins Cordova

| Plugin | Función | Nota de seguridad |
|---|---|---|
| `cordova-plugin-advanced-http` | Cliente HTTP nativo (OkHttp), cookies | Capa de red real; modo de confianza TLS en doc 03 |
| `cordova-sqlcipher-adapter` | SQLite cifrado (SQLCipher) | → `libsqlcipher.so`; llave = PIN (doc 05) |
| `uk.co.workingedge.cordova.plugin.sqliteporter` | Importa/exporta SQL (siembra `seed.sql`) | Usado por `seedDatabase()` |
| `cordova-plugin-file` | Acceso a sistema de archivos | Permisos `READ/WRITE_EXTERNAL_STORAGE` |
| `phonegap-plugin-barcodescanner` | Escaneo de códigos (ZXing) | Permiso `CAMERA`/`FLASHLIGHT`; probable origen de `RECORD_AUDIO` |
| `cordova-plugin-inappbrowser` | Navegador embebido | Revisar si abre URLs externas/OAuth (superficie de WebView) |
| `cordova-plugin-network-information` | Estado de red | — |
| `cordova-plugin-app-version` | Versión de la app | — |
| `cordova-plugin-statusbar` | Barra de estado | — |
| `cordova-plugin-screen-orientation` | Orientación | — |
| `es6-promise-plugin` | Polyfill de promesas | — |

> Versiones exactas: `cordova_plugins.js` lista los `id` de submódulos pero no un número de versión
> por plugin; no se afirman versiones puntuales.

## Framework y librerías

| Componente | Evidencia |
|---|---|
| **Capacitor** (Android) | `com.getcapacitor.*`, `capacitor.config.json` |
| **Ionic + Angular** | `vendor-es2015.js` (Angular/Ionic), build dual es5/es2015 |
| **Firebase Cloud Messaging** | `CapacitorFirebaseMessagingService`, `FirebaseInstanceIdReceiver`, `FirebaseInitProvider` |
| **Swiper** | `swiper-bundle-*.js` (carruseles UI) |
| **SQLCipher (nativo)** | `lib/arm64-v8a/libsqlcipher.so` |

## `.so` nativas del APK

| Librería | Origen |
|---|---|
| `libsqlcipher.so` | cordova-sqlcipher-adapter (cifrado de `sct.db`) |

(El resto de la funcionalidad nativa la aporta Capacitor/Cordova en el DEX, no en `.so` propias.)
