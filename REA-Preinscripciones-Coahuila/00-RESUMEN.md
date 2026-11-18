# 00 · Resumen ejecutivo — Preinscripciones Coahuila

## Identidad

| Campo | Valor |
|---|---|
| App | Preinscripciones Coahuila |
| Package | `com.luca.preinscripciones` |
| versionName / versionCode | 1.0.0 / 22 |
| minSdk / targetSdk | 24 / 36 |
| Firma | ⚠️ **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica** |
| SHA-256 cert | `726f35b6270805f5476e871065c48dd5a4f1416ff9fb6412870f044c0afc9617` (genérico APKPure) |
| sha256 XAPK | `1f7a50b94086363b2f9fb2adaf45a8d2a89fd2ba499b04ce8f50a865cbfde2d6` |
| Clases / recursos | 1 331 / 208 |
| Dueño | Secretaría de Educación de Coahuila (SEDU) — backend `seduinscripciones` |
| Proveedor | **LUCA** (`com.luca.*`) |

Package **verificado con `inspect_android_package`** sobre el APK base extraído del XAPK antes de
documentar (regla dura): coincide `com.luca.preinscripciones`.

> **Proveniencia:** el acervo es re-firmado por la tienda. El análisis de *código* (bytecode/strings)
> es válido; la **firma no prueba autenticidad** — cruzar con el canal oficial antes de divulgar.

## Pila tecnológica

- **Flutter** — `libflutter.so` + `libapp.so` (Dart AOT) en el split `config.arm64_v8a`;
  `assets/flutter_assets/` en el base. Tipografía Exo.
- **Muro:** el Dart AOT (`libapp.so`) no decompila; endpoints y símbolos sí aparecen como strings,
  la **lógica de red/validación TLS en runtime no** (requeriría dinámico: Frida/reflutter).
- Sin React Native, sin .NET, sin Capacitor.

## Permisos

`INTERNET`, `CAMERA`, `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION`, `ACCESS_NETWORK_STATE`.
Superficie modesta y coherente con el flujo (cámara = captura de documentos; ubicación = escuelas
cercanas). Sin SMS, sin contactos, sin permisos privilegiados.

## Backend

Proveedor LUCA aloja los web services en **infraestructura Azure genérica** (no `gob.mx`):
- `https://servicios.mexicocentral.cloudapp.azure.com/wservice_azure/preinscripciones/*.php`
  (VM Azure Cloud App) — incluye `w_service_curp.php` y `w_service_datos_del_hermano.php`.
- `https://seduinscripciones.azurewebsites.net/preinscripciones/reportes/preinscripcion.php`
  (Azure App Service).
Detalle en [02-red-endpoints.md](02-red-endpoints.md).

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | PII de menores (CURP, datos del hermano, ubicación) concentrada en infra Azure genérica de proveedor, no gob.mx | — (privacidad/gobernanza) | **MEDIA-BAJA** | CONFIRMADO |
| H2 | Endpoints de demo/plantilla en el release de producción | CWE-489 | BAJA | CONFIRMADO |
| H3 | TLS/cleartext | CWE-319/295 | — | **AUSENTE-SANO** |

Detalle en [04-hallazgos.md](04-hallazgos.md).

## Veredicto de una línea

App **relativamente limpia**: HTTPS, sin cleartext, sin secretos hardcodeados, sin trust-all
evidenciable. El punto a revisar es de **gobernanza de datos** — PII de menores (CURP, hermanos) en
infraestructura Azure de un proveedor privado, fuera de dominios `gob.mx`.
