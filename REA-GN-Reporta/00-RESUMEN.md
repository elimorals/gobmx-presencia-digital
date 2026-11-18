# 00 · Resumen ejecutivo — GN Reporta

## Identidad

| Campo | Valor |
|---|---|
| App | GN Reporta (denuncia ciudadana a la Guardia Nacional) |
| Package | `sspc.gn.cientifica` |
| versionName / versionCode | 1.0 / 19 |
| minSdk / targetSdk | 24 / 33 |
| Firma | ⚠️ **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **NO auténtica**; cruzar contra Play oficial de la SSPC/GN |
| SHA-256 cert | `893b3525aab92e197d94a2d2b1071f406e804f4e122322ecacf432c1cdaf72a1` (genérico APKPure) |
| sha256 XAPK | `3823f6d64e2f092f62c536097078d29b3efac536c67ae8e5b4170ac58973029e` |
| sha256 base APK | `9d83bfe511a1700536f364217f877309101cb08a4ba408a40c5902a99a68be99` |
| Clases | 1 504 |
| Dueño | Guardia Nacional / SSPC (federal) |

Package **verificado con `inspect_android_package` antes de documentar** (regla dura): coincide.
**Autenticidad:** la firma es la genérica de APKPure, así que no prueba origen; el respaldo es el
backend `certmx.gn.gob.mx` (dominio `gn.gob.mx` de la Guardia Nacional).

## Pila tecnológica

- **Nativa Android (Java/Kotlin)** — red por **Volley** (88 refs). No Flutter/RN/.NET.
- **Couchbase Lite** embebido (base de datos local + capacidad de sync).
- `WebView` presente (1 ref, uso puntual).
- FCM (push), captura de evidencia (cámara/audio/video).

## Backend

- **Único endpoint observado:** `https://certmx.gn.gob.mx/gn_reporta/pfmovilservice.php` (**HTTPS**,
  servicio PHP). Toda la denuncia pasa por este servicio.

## Veredicto rápido

**Mayormente sano en red** — contra la hipótesis inicial (el flag `usesCleartextTraffic=true` hacía
sospechar denuncia en claro). La denuncia va por **HTTPS**; el cleartext está **permitido pero no
usado**. El punto a vigilar es un `SelfSignedSSLSocketFactory` de la **librería Couchbase Lite** (no
atado al backend GN) — ver doc 02.

## Hallazgos (tabla)

| ID | Severidad | Estado | Resumen |
|----|-----------|--------|---------|
| H1 | MEDIA-BAJA | CONFIRMADO | `usesCleartextTraffic="true"` global sin NSC — permitido pero no usado (backend HTTPS). Higiene: una app de denuncia a la GN debería desactivar cleartext por completo |
| H2 | — | **A-CONFIRMAR** | `com.couchbase.lite...SelfSignedSSLSocketFactory` + `checkServerTrusted` presentes, de la **librería Couchbase**; no atados al backend GN (Volley HTTPS). Si se usaran contra un endpoint de sync podrían debilitar TLS, pero no se observó tal endpoint. **No se afirma MITM** |
| H3 | INFO | CONFIRMADO | Permisos de captura de evidencia (`CAMERA`, `RECORD_AUDIO`, `RECORD_VIDEO`) + `CALL_PHONE` + FCM + `READ_PHONE_STATE` — coherentes con denuncia, pero sensibles |
| H4 | — | AUSENTE-SANO | Sin trust-all propio de la app (el `checkServerTrusted` es de librería); sin `getUnsafeOkHttpClient`; sin http:// de backend; sin IPs internas ni secretos en claro hallados |
