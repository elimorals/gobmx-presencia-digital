# Licencia Federal Digital — Ingeniería inversa (REA)

> Análisis **estático** del APK de `mx.sct.licencia`. La app nunca se ejecuta.
> Herramientas: REA (JADX headless + apktool 3.0.3) + lectura directa del bundle web.
> Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| App | **Licencia Federal Digital (LFD)** · SCT/SICT (oficial) |
| Paquete | `mx.sct.licencia` |
| Versión | **1.0.49** (versionCode 49) |
| minSdk / targetSdk | 27 (Android 8.1) / 33 |
| APK | `@arm64-v8a`, 30,548,056 bytes · SHA-256 `8216279428d74bf623056772168463a285f487a955c033f01b9f1dc1c45f83c4` |
| DEX | 1 (`classes.dex`) · 5,094 clases smali (cascarón nativo delgado) |
| Copyright incrustado | `Licencia Federal Digital (LFD), Copyright (C) SCT 2020` (`main-es2015.js`) |
| Firma APK | **no verificada** (apksigner ausente; origen SSD) |
| Librería nativa clave | `libsqlcipher.so` (SQLite cifrado) |

> **Nota:** el archivo del SSD llamado `gob.bancodelbienestar.bcobienapp.apk` resultó ser
> Aptoide mal nombrado. Por eso aquí el package real se verificó con apktool **y** JADX
> ANTES de escribir nada: `mx.sct.licencia`, `app_name = "Licencia Federal Digital"`.

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| Tipo de app | **Híbrida WebView** (Capacitor + Cordova) | `assets/capacitor.config.json`, `assets/public/cordova.js` |
| UI / lógica | **Ionic + Angular** (build producción, webpack lazy modules, dual es5/es2015) | `main-es2015.js`, `vendor-es2015.js` (5–6 MB), `pages-*-module` |
| Cascarón nativo | Capacitor Android (`com.getcapacitor.*`) | `MainActivity`, `CapacitorFirebaseMessagingService` |
| Red | **cordova-plugin-advanced-http** (sobre OkHttp nativo) | `plugins/cordova-plugin-advanced-http` |
| API backend | **WSLFD** (Web Service LFD) de la SCT | `https://wslfd.sct.gob.mx/api` (+ QA `wslfdqa.apps.lab.sct.gob.mx`) |
| Almacenamiento local | **SQLCipher** (`cordova-sqlcipher-adapter`) + `sqliteporter` | `libsqlcipher.so`, `sct.db`, `assets/seed.sql` |
| Escaneo | **phonegap-plugin-barcodescanner** (ZXing) | plugin + permiso `CAMERA` |
| Push | **Firebase Cloud Messaging** | `CapacitorFirebaseMessagingService`, `FirebaseInstanceIdReceiver` |
| Navegador embebido | `cordova-plugin-inappbrowser` | plugin |

## Flujos de la app (rutas Angular)

Tipos de licencia soportados: **Transportista** y **Ferroviario** (`artf`).

`welcome → login → pin-assigment / pin-login → pin-verification → code-verification → get-license → licenses (Transportista/Ferroviario) → license-detail / license-scan-details`.

## Hallazgos principales

1. **La DB local se cifra con el PIN del usuario.** `DatabaseService.openDB(key)` abre
   `sct.db` con `this.sqlite.create({name:'sct.db', location:'default', key})`, y el `key`
   es `model.pin` (ver `pin-login` y `pin-verification`, doc 05). El PIN es **numérico**
   (validador `/[0-9]/`). Esa DB guarda CURP, RFC, teléfono, email, `password` y la licencia
   federal completa (esquema en `seed.sql`). → llave de baja entropía protegiendo PII fuerte.
2. **Ruta que abre la DB sin llave.** `UserService.getUser()` llama `openDB()` **sin argumento**
   → `key: undefined`. Ver doc 05 para alcance y la duda que queda.
3. **Host de QA en build de producción.** `wslfdqa.apps.lab.sct.gob.mx/api` viaja en el mismo
   bundle que el de producción (doc 03).
4. **Sin pinning TLS propio.** El código de la app no llama `setServerTrustMode`; advanced-http
   queda en su modo `default` (valida contra el store del SO, sin pinning ni desactivado) (doc 03).
5. **Permiso `RECORD_AUDIO`** declarado en una app de licencias (doc 01) — probablemente arrastre
   del barcodescanner; se marca para revisión, no se afirma uso.

## Mapa de hallazgos por documento

- `01-manifiesto.md` — package, versión, 13 permisos, componentes exportados, deeplink `mx.sct.licencia://`, FCM.
- `02-arquitectura-capacitor.md` — pila híbrida, módulos webpack, puente Capacitor/Cordova, servicios Angular.
- `03-red-endpoints.md` — WSLFD prod + QA, advanced-http, modo de confianza TLS.
- `04-autenticacion-pin.md` — login, PIN (assign/login/verification), verificación por código.
- `05-cripto-sqlcipher-datos-locales.md` — `sct.db`, llave = PIN, ruta sin llave, esquema y PII.
- `06-sdks-plugins.md` — plugins Cordova, Capacitor, Firebase, barcodescanner.
- `07-recursos-web-esquema.md` — estructura `assets/public`, `seed.sql`, tipos de licencia.
- `08-firma-recursos.md` — firma no verificada, hashes, limitaciones.

## Contraste con el pipeline REA previo

| Dimensión | App CDMX | SAT Móvil | **Licencia Federal Digital** |
|---|---|---|---|
| Tipo | Nativa (Views) | Flutter | **Híbrida WebView (Capacitor/Ionic)** |
| Lógica legible | DEX (JADX) | Dart AOT (opaca) | **JS Angular minificado (legible)** |
| Endpoints en claro | Sí (API v4) | Parcial | **Sí** (`wslfd.sct.gob.mx` + QA) |
| Cripto local | Tink + Keystore | Realm cifrado | **SQLCipher con llave = PIN** |
| Protección | Google PairIP | — | **Ninguna** (JS sin ofuscar más allá del minify) |
