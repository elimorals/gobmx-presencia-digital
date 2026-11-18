# Resumen — APP QRO 9.0.8 (`appqro.queretaro.gob.mx`)

## Identidad
- **App:** APP QRO — super-app del **Poder Ejecutivo del Estado de Querétaro** (servicios/trámites).
- **Package:** `appqro.queretaro.gob.mx` · **versión** 9.0.8 (versionCode 90008). compileSdk 36.
- **#34** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
**Flutter** (`flutter_assets/`, `libapp.so`/`libflutter.so` en el split de arquitectura; 506
entradas de assets). Lógica en el **snapshot Dart AOT** → muro (como Pemex ASISTE, SAT, Urbani,
Qrobús). El DEX es embedding + SDKs. No `.env` en `flutter_assets` (solo `AssetManifest.bin` y
un `config.svg` que es una imagen, no configuración) → el backend va dentro del snapshot.

## SDKs observables (DEX)
Firebase (**Remote Config** + FCM + Installations), **ExoPlayer/DASH** (video), ML translate
offline de Google. No se hallaron endpoints propios `*.queretaro.gob.mx` en el DEX (están en el
snapshot Dart).

## Permisos (amplios, super-app)
`INTERNET`, `ACCESS_FINE/COARSE_LOCATION`, **`FOREGROUND_SERVICE_LOCATION`**, `CAMERA`,
`RECORD_AUDIO`, `READ_MEDIA_AUDIO`, `READ_CONTACTS`, `USE_BIOMETRIC`/`USE_FINGERPRINT`,
`SCHEDULE_EXACT_ALARM`/`USE_EXACT_ALARM`, `USE_FULL_SCREEN_INTENT`, `RECEIVE_BOOT_COMPLETED`.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ Sin `usesCleartextTraffic` (HTTP bloqueado por defecto) |
| Permisos | ⚠️ Muy amplios (ubicación de fondo + contactos + micrófono + cámara + biometría) |
| Config | ⚠️ Firebase **Remote Config** (comportamiento configurable remotamente) |
| Lógica | 🧱 Muro Dart AOT (`libapp.so`) |
| Firma | ⚠️ Re-firmada por APKPure |

## Lo que NO se puede (muro)
Backend propio, rutas de API, auth, trámites y el uso real de ubicación/contactos/micrófono →
en el snapshot Dart, no observable estáticamente. Vía: reflutter/blutter/Frida (dinámico, fuera
de alcance). Documentado como límite, sin inventar.
