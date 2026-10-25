# 06 · SDKs y dependencias

Fuente: paquetes smali, `.so` de los splits, manifiesto, `strings.xml`. Estático.

## SDKs confirmados (directorios smali presentes)

| SDK | Paquete | Uso |
|---|---|---|
| **Veritran** (plataforma) | `net/veritran/*`, `com/veritran/*` | Runtime low-code (doc 02) |
| **Firebase** | `com/google/firebase` | FCM push + Crashlytics (`firebase-settings.crashlytics.com`) + Analytics |
| **GMS Measurement** | `com/google/android/gms/measurement` | Analítica (`app-measurement.com`) |
| **GMS Advertising-ID** | `com/google/android/gms/ads/identifier` | Sólo el **identificador** (AD_ID) para atribución — **no AdMob, no publicidad** |
| **ML Kit (barcode)** | `com/google/mlkit`, `libbarhopper_v3.so` | Escaneo de códigos |
| **SQLCipher / Zetetic** | `net/zetetic` | DB cifrada (doc 05) |
| **Conscrypt** | `org/conscrypt` | TLS (doc 03) |
| **SpongyCastle** | `org/spongycastle` | Cripto |
| **Retrofit2 + OkHttp3 + Okio** | `retrofit2`, `okhttp3`, `okio` | HTTP |
| **Kotlin / Coroutines / Serialization / Parcelize** | `kotlinx/*` | Lenguaje |
| **AndroidX WorkManager, ProfileInstaller, Startup** | manifiesto | Jobs / arranque |
| **GMS Auth (Google Sign-In)** | `RevocationBoundService` exportado | Posible login con Google (no confirmado el flujo) |

## .so nativas (split arm64)

| Librería | Origen / rol |
|---|---|
| `libsqlcipher.so` | SQLCipher |
| `libconscrypt_jni.so` | Conscrypt (TLS) |
| `libbarhopper_v3.so` | ML Kit barcode |
| `libduktape.so` | Motor JS Duktape (runtime Veritran, doc 02) |
| `libtoolChecker.so` | RASP / detección de herramientas (doc 04) |
| `libnjhpdqdxvc.so`, `libxmsqibvoii.so` | Packer / carga de `.szip` (nombres aleatorios, doc 04) |
| `libyoga_vt.so` | Yoga (layout) — variante Veritran (`_vt`) |
| `libdatastore_shared_counter.so` | Jetpack DataStore |

## OTP y notificaciones

- **SMS Retriever API** — OTP sin permiso de SMS (`SMSBroadcastReceiver`).
- **FCM** — proyecto `app-banco-del-bienestar`.

## Nota

No se listan versiones exactas de cada SDK (no hay un manifiesto de dependencias legible; muchas van
reempaquetadas dentro de Veritran). No se afirman versiones puntuales.
