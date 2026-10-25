# Banco del Bienestar Móvil — Ingeniería inversa (REA)

> Análisis **estático** del APK de `gob.bancodelbienestar.bcobienapp`. La app nunca se ejecuta.
> Herramientas: REA (JADX headless 0.7.1 + apktool 3.0.3). Fecha: 2026-10-24.

## Procedencia del artefacto (importante)

El archivo del SSD llamado `gob.bancodelbienestar.bcobienapp.apk` **era Aptoide mal nombrado**
(ver nota histórica). El APK analizado aquí es el **real**, obtenido **instalando la app desde
Google Play en un Motorola XT2435-3 (Android 16) y extrayéndola con `adb pull`** — fuente oficial,
sin manipulación. Package y versión verificados con apktool **y** JADX antes de documentar.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| App | **Banco del Bienestar Móvil** (Banco del Bienestar, S.N.C. — oficial) |
| Paquete | `gob.bancodelbienestar.bcobienapp` |
| Versión | **7.17.0.52467** (versionCode 5246790) |
| minSdk / targetSdk | 29 (Android 10) / 36 |
| Bundle | base + splits: `arm64_v8a` (nativo, 17 MB), `en`, `xxhdpi` |
| APK base | 33 MB · SHA-256 `4b928b2a31da00d57fc8684e11980fc87e24f449a9c26b5015e576a880b1985f` |
| DEX | 3 (`classes.dex`×3) · 16,355 clases (JADX) · 1,017 recursos |
| Firma APK | **no verificada** (apksigner ausente) — pero origen = Google Play |
| Firebase | proyecto `app-banco-del-bienestar` (sender 56592496113) |

## Hallazgo arquitectónico central: **plataforma Veritran (low-code)**

La app **no es código Kotlin propio**: el namespace `gob.bancodelbienestar.bcobienapp` tiene **solo
2 clases** (`R`, `VTCommonActivity`). El 99 % del binario es el runtime **Veritran** (`net.veritran.*`,
`com.veritran.*`), una plataforma low-code de banca. La lógica de negocio (pantallas, flujos) se
entrega como **definición empacada** (`.szip` en assets) y se interpreta en runtime vía un motor
**Duktape** (JS). Implicación RE: la lógica funcional **no es decompilable como Kotlin**; vive en los
`.szip`/Duktape y/o se descarga del gateway Veritran (doc 02, 03).

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| Plataforma app | **Veritran** low-code | `net/veritran/{vtuserapplication,contract,monitor,commons,smart,component}`, `VTCommonActivity`, `com.veritran.xpressplug.VTFlowActivity` |
| Motor de lógica | **Duktape** (JS) + payloads `.szip` | `libduktape.so`, `assets/*.szip`, 112 refs `Duktape` |
| Red | Retrofit2 + OkHttp3 + **Conscrypt** | `retrofit2/`, `okhttp3/`, `org/conscrypt`, `libconscrypt_jni.so` |
| Cripto local | **SQLCipher** (Zetetic) + SpongyCastle + `com.veritran.vtcrypto` | `net/zetetic`, `libsqlcipher.so`, `org/spongycastle` |
| Protección (RASP) | **Veritran Security Library** + packer nativo | `net/veritran/android_vtsecuritylibrary`, `libtoolChecker.so`, `.so` de nombre aleatorio |
| Escaneo | **ML Kit barcode** | `com/google/mlkit`, `libbarhopper_v3.so` |
| Push / backend móvil | **Firebase** (FCM) + SMS Retriever (OTP) | `FirebaseInstanceIdReceiver`, `SmsRetriever` |
| Auth local | **BiometricPrompt** | refs en smali |
| Identificadores | GMS **Advertising-ID** (solo `gms/ads/identifier`) + AdServices attribution | `gms/ads/identifier` · **no hay AdMob/publicidad** |

## Hallazgos principales

1. **Protección robusta (RASP).** Detección de **root (RootBeer), Xposed (182 refs), emulador,
   debugger y Frida**, más `libtoolChecker.so`, packer nativo con `.so` de nombre aleatorio
   (`libnjhpdqdxvc.so`, `libxmsqibvoii.so`) y `.szip` empacados, y permiso `DETECT_SCREEN_CAPTURE`.
   Contrasta de lleno con Licencia Federal Digital (cero protección). (doc 04)
2. **Lógica fuera del DEX.** La funcionalidad bancaria se interpreta vía Veritran/Duktape desde
   `.szip`; no es analizable estáticamente como Kotlin. Para verla haría falta desempacar/dinámica. (doc 02)
3. **Sin endpoint en texto plano.** No hay URL de backend en strings/recursos; el gateway Veritran
   se resuelve en runtime (config/`.szip`). Sólo dinámica (MITM) daría las rutas reales. (doc 03)
4. **network_security_config vacío** (`<network-security-config />`) — sin pinning declarativo; el
   pinning (si existe) vive en Conscrypt/Veritran a nivel código/nativo. (doc 03)
5. **Superficie de datos amplia para un banco social:** `READ_CONTACTS`, `READ_PHONE_STATE`,
   ubicación fina, biometría, cámara, Advertising-ID. (doc 07)

## Mapa de hallazgos por documento

- `01-manifiesto.md` — package, 34 permisos, componentes, exportados, deeplinks (`bancodelbienestar://`, App Links, NFC).
- `02-arquitectura-veritran.md` — plataforma Veritran, Duktape, `.szip`, por qué la lógica no se decompila.
- `03-red-endpoints.md` — sin endpoint en claro, NSC vacío, Conscrypt, Retrofit/OkHttp.
- `04-proteccion-rasp.md` — RootBeer, Xposed, emulador, debugger, Frida, packer nativo, DETECT_SCREEN_CAPTURE.
- `05-cripto-almacenamiento.md` — SQLCipher/Zetetic, vtcrypto, SpongyCastle, Conscrypt.
- `06-sdks-dependencias.md` — Firebase, ML Kit, SMS Retriever, WorkManager, Advertising-ID.
- `07-permisos-datos.md` — superficie de datos y privacidad.
- `08-firma-recursos.md` — firma, splits, hashes, limitaciones.

## Contraste en el pipeline REA

| Dimensión | Licencia Federal Digital | **Banco del Bienestar** |
|---|---|---|
| Tipo | Híbrida Capacitor/Ionic | **Nativa + plataforma Veritran (low-code)** |
| Lógica legible | Sí (JS Angular) | **No** (Veritran/Duktape sobre `.szip`) |
| Protección | **Ninguna** | **RASP fuerte** (root/Xposed/Frida/emu/debug) + packer |
| Endpoints en claro | Sí (`wslfd.sct.gob.mx`) | **No** (runtime Veritran) |
| Cripto local | SQLCipher (llave = PIN) | SQLCipher + vtcrypto (llave no determinada en estático) |
