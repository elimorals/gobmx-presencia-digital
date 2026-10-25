# 02 · Arquitectura (Capacitor / Ionic / Angular)

## Forma general

App **híbrida WebView**: un cascarón nativo Capacitor (una sola `MainActivity`) que carga una
SPA de **Ionic + Angular** empaquetada en `assets/public/`. Toda la lógica de negocio es
JavaScript; el DEX (5,094 clases smali) es sólo Capacitor + plugins Cordova + Firebase.

Evidencia:
- `assets/capacitor.config.json` → `{ "appId": "mx.sct.licencia", "appName": "Licencia Federal Digital", "webDir": "www" }`
- `assets/public/cordova.js`, `cordova_plugins.js`, `native-bridge.js` → puente Cordova sobre Capacitor.
- `main-es2015.js` / `vendor-es2015.js` (~5 MB) → Angular. Build dual **es5 + es2015** (differential loading).

## Estructura del bundle (`assets/public/`)

```
assets/public/
├── index.html, cordova.js, native-bridge.js, cordova_plugins.js
├── main-es{5,2015}.js        ← arranque + servicios propios (DatabaseService, UserService, ...)
├── vendor-es{5,2015}.js      ← Angular + Ionic (5–6 MB)
├── common-es{5,2015}.js      ← código compartido entre páginas
├── runtime / polyfills / *   ← webpack runtime y polyfills
├── pages-*-module-es*.js     ← un módulo lazy por pantalla (login, pin-*, get-license, licenses/...)
├── plugins/                  ← JS de cada plugin Cordova (ver doc 06)
├── svg/  assets/{img,icon,fonts}
└── assets/seed.sql           ← esquema inicial de la DB SQLCipher (ver doc 05)
```

Los módulos por página (`pages-*-module`) son pequeños (20–80 KB) y están **minificados pero
estructurados** (no ofuscados): nombres de clase/servicio como `DatabaseService`, `UserService`,
`PinModel`, `LicenseService` son legibles, lo que hace el análisis estático directo.

## Servicios propios identificados (`main-es2015.js`, `common-es2015.js`)

| Servicio | Rol | Evidencia |
|---|---|---|
| `DatabaseService` | Abre/gestiona `sct.db` (SQLCipher), siembra con `seed.sql` | `openDB(key)`, `this.sqlite.create({...})`, `seedDatabase()` lee `assets/seed.sql` |
| `UserService` | Lee/guarda el usuario en la DB | `getUser()` → `openDB()` + `getDatabaseInstance()` |
| `LicenseService` | Lógica de licencias (post-login) | referenciado en `pin-login` |
| `PinModel` | Modelo del formulario de PIN | `new PinModel(this.pinForm)` |

Rutas Angular (pantallas): `welcome`, `login`, `pin-assigment`, `pin-login`, `pin-verification`,
`code-verification`, `get-license`, `licenses` con submódulos **Transportista** y **Ferroviario**
(`license-detail`, `license-detail-artf`, `license-scan-details`, `license-scan-details-artf`),
`modal-terms`.

## Puente nativo

- **Capacitor** core (`com.getcapacitor.*`) + plugins Cordova cargados vía `cordova_plugins.js`.
- Acceso a hardware/SO exclusivamente por plugins (cámara, archivos, red, SQLCipher, inappbrowser,
  statusbar, screen-orientation, app-version). Ver doc 06.

## Implicación de seguridad del modelo híbrido

La lógica sensible (manejo de la llave de la DB, construcción de requests, validación) corre en
JS dentro del WebView, **sin ofuscación fuerte**. Cualquiera con el APK puede leer el flujo
completo (lo que hace este mismo análisis). No hay capa de hardening tipo PairIP/DexGuard.
