# 00 · Resumen ejecutivo — Jalisco Alerta

## Identidad

| Campo | Valor |
|---|---|
| App | Jalisco Alerta |
| Package | `mx.erlia.jal.android` (verificado con `inspect_android_package`) |
| versionName / versionCode | 2.38.0 / 209171940 |
| minSdk / targetSdk | 24 / 36 |
| Firma | `CN=Android, OU=Android, O=Google Inc.` → **re-firmada por APKPure (NO auténtica)** |
| SHA-256 cert | `1f:5a:ef:24:f3:1c:c8:d0:2b:91:ae:5b:e6:24:51:54:33:d0:c3:ad:82:ea:11:ab:30:d3:6d:fb:96:9f:88:c6` |
| sha256 XAPK | `339864251b2229ec7e50850a29698a8d1c0411fcf320a8368bba7b64f3c03953` |
| sha256 APK base | `27735306462ceac834436784d9fe30d3052cfc0629bf0e9ff798b45809c96281` |
| Clases / recursos | 24 553 / 1 190 |
| Dueño declarado | Gobierno de Jalisco (app de alerta/emergencias) · proveedor **Erlia** (`erlia.mx`) |

> **Proveniencia:** el APK del acervo está re-firmado por APKPure. El análisis de **código** es
> válido (el bytecode no cambia al re-firmar), pero la firma **no** prueba autenticidad: cruzar
> contra el canal oficial antes de divulgar.

## Pila tecnológica

- **React Native 0.81.5 + Expo** (confirmado): `assets/index.android.bundle` = **Hermes bytecode
  v96** (12.6 MB); libs `libreactnative.so`, `libhermes.so`, `libjsi.so`, `libfbjni.so`,
  `libreanimated.so` en el split `config.arm64_v8a`. New Architecture (codegen libs presentes).
- **Push:** Expo push (`exp.host/--/api/v2/push`) + FCM/C2DM.
- **Terceros nativos:** Sentry (`libsentry.so`, crash reporting), ML Kit Barcode (`libbarhopper_v3.so`,
  escaneo QR).
- Deeplink: `erliaapp://` (+ App Links `https`).

## Mapa de módulos (observado)

App de alerta ciudadana: ubicación en tiempo real (foreground service location), cámara/linterna,
botón de alerta, notificaciones push, calidad del aire (`aire.jalisco.gob.mx`), blog/FAQ. La lógica
vive en el bundle Hermes (JS compilado) — los strings se extraen, el flujo exacto no.

## Backends (del bundle Hermes)

| Host | Rol |
|---|---|
| `https://api.edl.jaliscoalerta.com` | **API principal** (EDL) |
| `https://edl.jaliscoalerta.com` · `https://blog.jaliscoalerta.com` | portal / blog |
| `https://aire.jalisco.gob.mx` · `https://airejalisco.digital` | calidad del aire |
| `https://erlia.mx/...` | aviso de privacidad / FAQ del proveedor |
| `https://exp.host`, `https://classic-assets.eascdn.net` | Expo push / EAS CDN |

**Todos HTTPS.** No se halló endpoint `http://` de backend.

## Hallazgos (tabla)

| Sev | Hallazgo | Estado |
|---|---|---|
| 🟢 SANO | **No cleartext, no trust-all, backends HTTPS** — NO repite el patrón de 911-CDMX | CONFIRMADO (estático) |
| 🟡 BAJA | Sentry DSN público embebido | CONFIRMADO |
| ⚪ INFO | Superficie de permisos amplia (ubicación background + cámara + SYSTEM_ALERT_WINDOW) | CONFIRMADO |
| ⚪ INFO | Re-firmada APKPure (proveniencia) | CONFIRMADO |

Detalle en los capítulos. **Muro:** la lógica JS es Hermes bytecode; el comportamiento de red en
runtime no se confirma por estático (p. ej. pinning/validación nativa de OkHttp no se pudo atar).
