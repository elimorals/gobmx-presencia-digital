# App CDMX — Ingeniería inversa (REA)

> Análisis **estático** del APK base de `mx.gob.cdmx.adip.apps`. La app nunca se ejecuta.
> Herramienta: REA (JADX headless + apktool). Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| App | **App CDMX** (Gobierno de la Ciudad de México · ADIP, oficial) |
| Paquete | `mx.gob.cdmx.adip.apps` |
| Versión | **4.5.2** (versionCode 620) |
| minSdk / targetSdk | 29 (Android 10) / 36 |
| Bundle | XAPK: base + **19 split APKs** (18 idiomas + `hdpi`) + nativo |
| APK base | 52,274,442 bytes · SHA-256 `3070e6eb79ab01abee6d54980a4bf9309028e1e0bfcaf8878ed4c57248d0efcf` |
| Tamaño total bundle | 57,528,123 bytes |
| DEX | classes.dex (8.96 MB) + classes2.dex (3.17 MB) |
| Clases (top-level, JADX) | **12,742** · Recursos: **3,744** |
| `Application` | **`com.pairip.application.Application`** (hardening PairIP de Google) |
| Firma APK | **no verificada** por el motor (origen SSD) |
| Librería nativa clave | `lib/armeabi-v7a/libpairipcore.so` (599,404 bytes) |

> **Nota de bundle:** el XAPK se descargó como `@arm64-v8a`, pero el split nativo extraído
> es `config.armeabi_v7a.apk`. Las librerías documentadas son las de `armeabi-v7a`.

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| UI | Android Views (una Activity por servicio, MVVM) + widgets de pantalla | 105 Activities `mx.gob.cdmx.adip.*.ui.activities.*` |
| Red | **Retrofit2 + OkHttp3 + Gson** | `Lretrofit2/`, `Lokhttp3/`, `Lcom/google/gson` |
| API backend | Monolito **CodeIgniter** (`index.php/v4/`) | `https://apiapp.cdmx.gob.mx/index.php/v4/*` |
| Identidad / SSO | **Llave CDMX** + biometría | `mx.gob.cdmx.adip.llave`, `llave.cdmx.gob.mx`, `BiometricPrompt` |
| Pagos | **Mercado Pago** | `api.mercadopago.com/semovi/appcdmx/v1/...`, `/MP/Payment` |
| NFC / transporte | **Spirtech Calypso HCE** | `com.spirtech.android.hce.calypso.*`, `libcalypsoHceNatives.so`, `apduservice.xml` |
| Mapas | **Google Maps SDK** | `Lcom/google/android/gms/maps` |
| QR / barcode | **ML Kit** (barhopper) | `libbarhopper_v3.so`, `Lcom/google/mlkit` |
| Cámara | **CameraX** | `libimage_processing_util_jni.so`, `libsurface_util_jni.so` |
| Push / backend móvil | **Firebase** (FCM, Auth, Analytics, Crashlytics, Installations) | 1,556 refs `Lcom/google/firebase`; proyecto `app-cdmx---prod` |
| Cripto local | **Google Tink** (shaded) + AndroidKeyStore | `com/google/crypto/tink/...`, AES-GCM |
| Imágenes / anim | **Glide**, **Lottie** | `Lcom/bumptech/glide`, `Lcom/airbnb/lottie` |
| Protección | **Google PairIP** (Automatic Integrity Protection) | `com.pairip.application.Application`, `libpairipcore.so` |

## Módulos propios (`mx.gob.cdmx.adip.*`) — super-app

24 sub-paquetes de servicio, ordenados por nº de componentes en el manifiesto:

| Paquete | Componentes | Servicio |
|---|---|---|
| `.pagos` | 13 | Pasarela de pagos (trámites, Mercado Pago) |
| `.mitaxi` / `.mitaxioperador` | 11 / 7 | Mi Taxi (pasajero y chofer) |
| `.apps` | 10 | Núcleo / Splash / contenedor |
| `.seguridad` | 9 | Seguridad ciudadana (C5, policía cuadrante) |
| `.movilidadintegrada` | 8 | Transporte: rutas, Metrobús, incidencias, tarjeta Mi |
| `.clinicacondesa` | 8 | Clínica Condesa (salud) |
| `.documentosdigitales` | 6 | Documentos oficiales digitales |
| `.tarjetavirtualmi` / `.recargami` | 5 / 5 | Tarjeta virtual "Mi" y su recarga |
| `.puntoswifi` | 5 | Puntos de WiFi gratuito (C5/Infinitum) |
| `.culturaentretenimiento` | 5 | Cultura y entretenimiento |
| `.botonpanico` | 5 | Botón de pánico |
| `.locatelreport` / `.locatel` | 4 / 2 | LOCATEL (reportes, chat) |
| `.denunciadigital` | 3 | Denuncia digital |
| `.calidadaire` | 3 | Calidad del aire |
| `.anticorrupcion` | 3 | Canal anticorrupción |
| `.reporteciudadano` | 2 | Reporte ciudadano 311 |
| `.garagedigital` | 2 | Garage digital (vehículos) |
| `.saludutopias` / `.saludbienestar` | 1 / 1 | Salud (UTOPÍAS, bienestar) |
| `.llave` | 1 | Identidad única Llave CDMX |
| `.utils` | 2 | Utilidades compartidas |

## Mapa de hallazgos por documento

- `01-manifiesto.md` — 26 permisos, 105/21/20/5 componentes, 13 exportados, deeplink `appcdmx://`, FileProvider, 7 app-widgets.
- `02-modulos-servicios.md` — los 24 servicios y su superficie.
- `03-red-endpoints.md` — API v4 CodeIgniter, Mercado Pago, Llave, Mundial 2026.
- `04-autenticacion-llave.md` — Llave CDMX SSO, biometría, prefs de sesión.
- `05-nfc-calypso-movilidad.md` — Calypso HCE, APDU, tarjeta de transporte.
- `06-pagos-mercadopago.md` — pagos y recarga.
- `07-proteccion-pairip.md` — PairIP, nativos, cert pinning `ca.cert.der`.
- `08-sdks-terceros.md` — Firebase, Retrofit, Maps, ML Kit, Tink.
- `09-firma-recursos.md` — firma no verificada, bundle, hashes.

## Contraste con el pipeline REA previo

| Dimensión | JW Library | CFE Contigo | **App CDMX** |
|---|---|---|---|
| Dominio | Lectura de contenido | Energía (financiero) | **Super-app: 24 servicios** |
| Endpoints en claro | Sí | No (DexGuard) | **Sí** (API v4 visible) |
| Protección | R8 parcial | DexGuard + packer | **Google PairIP** |
| String encryption | No | Sí | **No** (PairIP no cifra strings) |
| Librería nativa | libsodium/Javet | `libb1bd.so` packed | `libpairipcore.so` + `libcalypsoHceNatives.so` |
| Pagos | — | Tarjetas, puntos | Mercado Pago |
| NFC | — | — | **Calypso HCE (transporte)** |
| Identidad | — | OAuth2 propio | **Llave CDMX (SSO)** |
