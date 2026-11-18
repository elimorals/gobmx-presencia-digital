# 00 · Resumen ejecutivo — Llamada Segura Hidalgo

## Identidad

| Campo | Valor |
|---|---|
| App | Llamada Segura Hidalgo (No Más Extorsión) |
| Package | `mx.gob.hidalgo.nomasextorsion` |
| versionName / code | 2.3.13 / 43 |
| minSdk / targetSdk | 22 / 36 |
| Firma | **Auténtica** · `CN=Hidalgo Seguro, OU=C5i, O=Hidalgo, L=Pachuca, ST=Hidalgo` |
| cert SHA-256 | `f3c0906231695e2c97c4ed34f7c0852926eef65d4184315dbcff6bb54d50d6be` |
| sha256 xapk | `a014c85f7afa73f028c42e38a671a89866806d6a844a96c000b6dbca3c5197fe` |
| Clases / recursos | 2 081 / 572 |
| Dueño | C5i Hidalgo (Gobierno del Estado de Hidalgo) |

Package **verificado con `inspect_android_package` antes de documentar** (regla dura): coincide.
Nota: APKPure sirvió el split `armeabi_v7a` (32-bit) pese a pedir arm64; no afecta el análisis de
bytecode y la firma es la **original** (no re-firmada por APKPure).

## Pila tecnológica

- **Nativa Android** (Java/Kotlin), app pequeña (base 4.9 MB, 1 dex, 2 081 clases). Sin Flutter/RN/
  .NET.
- Clases propias: `Activities.{MainActivity,ReportNumber,SettingsActivity,SplashActivity}`,
  **`ExtortionCallScreeningService`** (usa la API `CallScreeningService` de Android para filtrar
  llamadas entrantes contra una lista de números de extorsión), `MyFirebaseMessagingService` (FCM),
  `PhoneFunctions.CustomBroadcastReceiver`.
- Backend: **C5 Hidalgo** por HTTPS; Firebase (RTDB + mensajería).

## Función

Filtra/identifica llamadas de extorsión (CallScreeningService) y permite **reportar números**;
descarga la lista de números desde el C5 y envía analítica/reportes.

## Hallazgos (tabla)

| # | Hallazgo | Severidad | Estado |
|---|----------|-----------|--------|
| H1 | **Postura de red sana** — HTTPS exclusivo, sin trust-all, sin NSC, `cleartext=false`, targetSdk 36 | — | **AUSENTE-SANO** (contraejemplo) |
| H2 | Firebase RTDB `nomasextorsion-4d133.firebaseio.com` — reglas de acceso desconocidas | BAJA | **A-CONFIRMAR** |
| H3 | API key de Firebase (`AIzaSy…`) en recursos — por diseño, restringible | BAJA | CONFIRMADO |
| H4 | Permisos `READ_CALL_LOG` + `READ_PHONE_STATE` + `SYSTEM_ALERT_WINDOW` | INFO | coherente con la función |

**Veredicto:** app de seguridad **bien construida** en lo esencial. **No** repite el cleartext de
911-CDMX ni el trust-all de Infonavit/Cosmocolor. Contraejemplo limpio de app de seguridad estatal.
Detalle de red/TLS en [02-red-tls-hallazgos.md](02-red-tls-hallazgos.md).
