# Resumen — UNE / Bus Sonora 5.7.2 (`com.bigbangdev.bussonora`)

## Identidad
- **App:** UNE — transporte público del estado de **Sonora** (Bus Sonora; sistema UNE, ligado
  al IMTES). Planeación/pago de transporte.
- **Package:** `com.bigbangdev.bussonora` · **versión** 5.7.2 (versionCode 129).
- **#17** del inventario por instalaciones (≈1 M).
- **Firma:** `CN=Omar Aguilar Fraga, O=Bus Sonora` (dev individual, original preservada).

## Pila
**Flutter** (`flutter_assets/`, `libapp.so`/`libflutter.so` en el split de arquitectura). Lógica
en el **snapshot Dart AOT** → muro (como SAT Móvil, Urbani, Mi Policía, Pemex, Qrobús). El DEX es
embedding + SDKs.

## SDKs observables (DEX)
| SDK | Rol |
|---|---|
| **Sentry** (`sentry.io`) | reporte de errores/crashes a un tercero |
| **reCAPTCHA** (`recaptcha.net`) | anti-bot |
| **Firebase** (`app-measurement.com`) | analytics / FCM |
| **AdServices** (perm. `ACCESS_ADSERVICES_*`) | **atribución de publicidad** (Privacy Sandbox) |

El backend propio (API de Bus Sonora) va en el snapshot Dart, **no en claro** en el DEX.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ Sin cleartext (backend HTTPS en el snapshot; no se vio `http://` propio) |
| Firma | ⚠️ Dev individual (`Bus Sonora`), no una dependencia estatal |
| Privacidad | ⚠️ AdServices (atribución de anuncios) + Sentry en app de transporte |
| Identidad | ✅ Biometría local (USE_BIOMETRIC) + reCAPTCHA |
| Lógica | 🧱 Muro Dart AOT |

## Lo que NO se puede (muro)
Backend propio, rutas de API, auth y pago → en el snapshot Dart, no observable estáticamente.
Vía: reflutter/blutter/Frida (dinámico, fuera de alcance). Documentado como límite, sin inventar.
