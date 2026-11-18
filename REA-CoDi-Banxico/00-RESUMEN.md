# Resumen — CoDi Banxico 2.5.2 (`org.banxico.dgspim.mobile.codi`)

## Identidad
- **App:** CoDi (Cobro Digital) — **Banco de México**. Cobros por QR ligados a cuentas bancarias
  vía SPEI. Es la app del **cobrador** (genera QR de cobro; clases `DetalleCobrar`, `menu.cobro.Cuentas`,
  `CobrosAnteriores` — no hay flujo de "pagar"/escanear → sin CAMERA).
- **Package:** `org.banxico.dgspim.mobile.codi` · **versión** 2.5.2 (versionCode 18).
- **#35** del inventario por instalaciones (≈100 K). **Firma:** auténtica (`O=Banco de México`).

## Pila
**Nativa Kotlin/Java** (2 dex, 3 538 clases, sin Flutter/.NET/Unity), **ofuscada con R8**
(clases `a/b/c`). Módulos propios: `activities.*` (registro, cobro, historial, config),
`ddispim.codi.httpclient.*` (cliente HTTP + `security.*`), `ddspim.mobile.kt.securityservices.engine.*`
(motor cripto), `inactivitytimeout.*` (timeout de sesión), `receivers.FirebaseService`.

## Postura de seguridad (la mejor del acervo)

| Aspecto | Veredicto | Evidencia |
|---|---|---|
| **TLS** | ✅✅ **Certificate pinning correcto** | `checkServerTrusted`: valida cadena vs CAs del sistema **y** fija SHA-256 del hoja (doc 04) |
| Almacenamiento de llaves | ✅ AndroidKeyStore + `setUserAuthenticationRequired` | atadas a biometría (doc 03) |
| Firma de operaciones | ✅ EC (curvas elípticas) + SHA-256/512 + HmacSHA256 | doc 03 |
| Cifrado | ✅ AES-GCM (autenticado) + AES-CBC | doc 03 |
| Sesión | ✅ Timeout de inactividad (`InactivityWorker`) + biometría | doc 01 |
| Transporte | ✅ HTTPS a `*.codi.org.mx` (Banxico) | doc 04 |
| Overlay | ⚠️ Declara `SYSTEM_ALERT_WINDOW` (inusual en banca) | doc 01 |
| Ofuscación | ✅ R8 (dificulta el análisis) | doc 02 |

## Lectura

CoDi Banxico es, con diferencia, la app **mejor construida** del acervo en seguridad: hace
**todo lo que Infonavit y Licencia EDOMEX hacen mal** (esas desactivan la validación TLS; CoDi
la endurece con pinning), guarda las llaves en hardware atadas a biometría y firma las
operaciones con EC. La única nota es el permiso de overlay (`SYSTEM_ALERT_WINDOW`), cuyo uso no
se confirmó. Es el contrapunto positivo del lote.
