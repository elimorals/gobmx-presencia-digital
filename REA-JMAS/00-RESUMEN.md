# Resumen — JMAS Móvil 3.0.3 (`mx.gob.jmasmovil`)

## Identidad
- **App:** JMAS Móvil — Junta Municipal de Agua y Saneamiento (Chihuahua). Contratos, recibos,
  historial de pagos y **pago de agua**.
- **Package:** `mx.gob.jmasmovil` · **versión** 3.0.3 (versionCode 3000301). compileSdk 36.
- **#39** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Arquitectura (en una línea)
Nativa **Kotlin + Jetpack Compose** con **Hilt** (DI), **Retrofit/OkHttp** (red) y **Room** (DB
local). 8 168 clases; paquete propio limpio (`data.network`, `di`, `ui.*`, `data.models`). Sin
Flutter/Unity/.NET/WebView-wrapper. Minificada (R8) pero legible.

## Backend
- `BuildConfig.BASE_URL = https://jmaschih.gob.mx/jmas` (HTTPS, dominio oficial).
- API REST (`JmasApi`/`AuthApi`): login, signUp, resetPassword, updatePassword, deleteAccount,
  getUserContracts, registerNewContract, getUserBillingHistory, **getPaymentURL**, healthCheck.

## Postura de seguridad (balance)
| Aspecto | Veredicto |
|---|---|
| TLS | ✅ Validación estándar, **sin trust-all** y sin cleartext (doc 03) |
| Pinning | ⚠️ No hay (validación por defecto, correcta pero sin pinning) |
| Pagos | ✅ URL hosteada de **BBVA**; la app no maneja tarjeta (doc 04) |
| Token | ⚠️ JWT en `SharedPreferences` planas (no EncryptedSharedPreferences) (doc 05) |
| Permisos | ✅ Mínimos: INTERNET + CAMERA (QR) + NETWORK_STATE |
| Firma | ⚠️ Re-firmada por APKPure |

**Lectura:** de las apps **mejor construidas** del acervo. Arquitectura moderna, transporte
sano, pago delegado correctamente. Los dos puntos mejorables (token sin cifrar, sin pinning) son
de severidad **baja**. Contrasta de lleno con ProcurApp/Infonavit/Licencia EDOMEX.
