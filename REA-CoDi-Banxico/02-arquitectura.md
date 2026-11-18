# Arquitectura — nativa ofuscada, módulos

## Pila

- **Nativa Android (Kotlin/Java)**: 2 dex, 3 538 clases. La única `.so` en el split arm64 es
  `libandroidx.graphics.path.so` (AndroidX) → **no** Flutter/Unity/.NET/Veritran. Es una app
  Android convencional.
- **Ofuscada con R8**: las clases de lógica interna llevan nombres de una letra (`a`, `b`, `c`),
  los paquetes propios conservan nombres descriptivos. La ofuscación dificulta el análisis pero
  no lo impide para las clases clave.

## Módulos propios (`search_android_classes`)

| Paquete / clase | Rol |
|---|---|
| `dgspim.mobile.codi.activities.login.{Registro, SplashScreen}` | **enrolamiento** y arranque |
| `…activities.menu.cobro.Cuentas`, `…detalle.DetalleCobrar` | alta de cuentas y **generación del cobro** |
| `…activities.menu.historial.{CobrosAnteriores, Exportar, Filtrar, Ordenar}` | historial de cobros, exportar CSV |
| `…activities.detalle.{Contactos, DetalleCuenta, DetalleOperacion}` | detalle de operaciones y cuentas |
| `…activities.menuLateral.configActivities.{CambioPass, AdminSpam}` | cambio de contraseña, anti-spam |
| **`ddispim.codi.httpclient.{a,b,c,d}`** + `dto.*` | **cliente HTTP** CoDi |
| **`ddispim.codi.httpclient.security.{a,b,c}`** | **seguridad de transporte** (pinning, doc 04) |
| **`ddspim.mobile.kt.securityservices.engine.{a,b,c}`** | **motor criptográfico** (doc 03) |
| `inactivitytimeout.{InactivityWorker, SessionTimeoutReceiver}` | **timeout de sesión** |
| `receivers.FirebaseService` | push (FCM) |

## Flujo (inferido de la estructura + strings)

1. **Registro** (`Registro`) → enrolamiento de la cuenta contra `codi-registro` (doc 04); genera/
   almacena llaves en AndroidKeyStore (doc 03).
2. **Cobro** (`DetalleCobrar`/`Cuentas`) → arma el mensaje de cobro CoDi, lo **firma con EC**
   (doc 03) y lo publica (QR/NFC); la contraparte paga vía su app bancaria (SPEI).
3. **Historial** (`CobrosAnteriores`) → consulta (`codi-consulta`) y exporta (`Exportar` → CSV,
   `CoDi.csv`).

> La lógica fina está ofuscada; el análisis se centra en las clases de **seguridad** (pinning y
> motor cripto), que son las de mayor valor y sí se decompilaron (doc 03–04). El detalle exacto
> del armado del mensaje CoDi queda parcialmente tras la ofuscación, marcado como límite.
