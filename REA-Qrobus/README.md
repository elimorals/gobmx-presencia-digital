# Ingeniería inversa de Qrobús 12.7.9 — índice

Análisis **estático** del APK `mx.gob.iqt.qrobus` con REA (JADX headless + inspección de
archivo). La app **nunca se ejecutó**. Es la app oficial de **Qrobús** — transporte público de
Querétaro (IQT, Instituto Queretano del Transporte): planeación de viajes, pago y tarjeta.
**#19 por instalaciones** del inventario gob.mx (≈500 K).

> **Flutter — lógica en el snapshot Dart (muro).** La app está hecha con Flutter
> (`flutter_assets/`, `libapp.so`); la lógica vive en el snapshot Dart AOT, no en el DEX → no
> decompilable estáticamente. Lo observable desde el DEX es la **superficie de red**: el backend
> corre en un **dominio de proveedor privado** (`admin.qrobici.com`, no `gob.mx`) y los **pagos
> van por Stripe**. Permisos amplios de **ubicación en segundo plano** + NFC + cámara.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, red/pagos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación de fondo, NFC, cámara) |
| 02 | [02-flutter.md](02-flutter.md) | Flutter/Dart AOT, muro |
| 03 | [03-red-pagos.md](03-red-pagos.md) | `qrobici.com`, Stripe, dominio de proveedor |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Re-firmada por APKPure |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos y límites |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/firma.txt`
(no versionado).

## 5 hallazgos principales

1. **Flutter = lógica en el snapshot Dart (muro).** `libapp.so` (Dart AOT); el DEX es embedding.
   Rutas completas de API, auth y manejo de pago no observables estáticamente (doc 02).
2. **Backend en dominio de proveedor privado.** La API es `https://admin.qrobici.com/api/v1/travels/trip`
   — el servicio corre en **`qrobici.com`** (dominio comercial del proveedor), **no** bajo
   `*.gob.mx`/`iqt`. HTTPS (bien), pero la infraestructura del transporte público queda en manos
   de un tercero y fuera del dominio gubernamental (doc 03).
3. **Pagos por Stripe.** El DEX incluye el SDK de Stripe (`api.stripe.com/v1/`) → el cobro de
   viajes/recargas se procesa con Stripe. La *publishable key* (pública por diseño) vive en el
   snapshot Dart (doc 03).
4. **Ubicación en segundo plano.** `ACCESS_BACKGROUND_LOCATION` + `FOREGROUND_SERVICE_LOCATION` →
   la app puede rastrear ubicación **en background** (seguimiento de viaje/unidades). Permiso
   sensible; su uso exacto está en el snapshot Dart (doc 01).
5. **Firma re-firmada por APKPure.** `CN=Android, O=Google Inc.` → arquitectura sí, integridad
   no (doc 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. La lógica Dart no se desensambló (muro); se documenta la superficie de red, los
permisos y la firma, con evidencia. **Firma** re-firmada por APKPure → no verificable como
auténtica del IQT.
