# Resumen — Lotería de México 1.3.26 (`loterianacional.mx`)

## Identidad
- **App:** Lotería de México — **Lotería Nacional (Lotenal)**. Resultados de sorteos +
  verificación de billetes/cupones.
- **Package:** `loterianacional.mx` · **versión** 1.3.26 (versionCode 13141326). compileSdk 36.
- **#48** del inventario por instalaciones (≈100 K).
- **Firma:** `CN=Jorge Alonso Nicolás, O=GemesGroup, L=Madrid, C=ES` — proveedor **español**,
  firma original preservada.

## Arquitectura (en una línea)
Nativa **Kotlin/Java** (13 954 clases, 22 propias, 3 dex, 9.4 MB). **White-label de GemesGroup**:
el mismo codebase atiende loterías de España (ONCE) y Portugal (Santa Casa), con los endpoints de
esos países embebidos (doc 03).

## Clases propias (`search_android_classes`)
`CuponActivity`, `CheckCupon`/`CheckCuponTask` (verificar billete), `data.{CuponBean,CuponDatabase,
CuponParser,SorteoBean,SorteosParser}` (sorteos/resultados), `util.{CargarHistoricoTask,
CheckNewVersionAvalaibleTask, RefreshAdIntervalTask, CuponUtil, SSLHelper}`, `RemoteConfig`,
`Prefs`, `alarmChecker`, `StartAtBootServiceReceiver`.

## Qué hace (y qué no)
- **Consulta resultados de sorteos** (parse de `SorteosParser`) y **historial**.
- **Verifica cupones/billetes** (`CheckCuponTask`) — comprueba si un billete ganó.
- **Anuncios** (`RefreshAdIntervalTask` + AdServices).
- **Chequeo de versión** (`CheckNewVersionAvalaibleTask`) vía Dropbox/OneDrive.
- **NO maneja pagos/compra** de billetes: sin pasarela, sin permisos ni clases de pago.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ⚠️ `usesCleartextTraffic=true`; resultados por **HTTP** desde **tercero** `nacionalloteria.com` |
| TLS trust-all | ⚠️ presente (`SSLHelper`) pero **sin llamador** → inerte (riesgo latente, no MITM confirmado) |
| Actualización | ⚠️ chequeo de versión vía **Dropbox público** |
| Pagos | ✅ No aplica (solo consulta) |
| PII | ✅ Mínima (sin login observado, sin ubicación/cámara/contactos) |
| Publicidad | ⚠️ AD_ID + AdServices |
| Firma | Proveedor español (GemesGroup), original preservada |

**Lectura:** app de bajo riesgo de datos (no hay pagos ni PII fuerte), pero con **mala higiene de
transporte** (cleartext + resultados de un tercero por HTTP) y un **mecanismo de actualización
frágil** (Dropbox). El trust-all es la peor señal de código, pero está muerto.
