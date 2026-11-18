# Ingeniería inversa de Mi Aguah 1.2.11 — índice

Análisis **estático** del APK `mx.gob.aguadehermosillo.miaguah` con REA (JADX headless +
inspección de archivo). La app **nunca se ejecutó**. Es la app oficial de **Agua de Hermosillo**
(organismo operador de agua potable, Sonora): consulta y **pago de recibos de agua**. **#45 por
instalaciones** del inventario gob.mx (≈100 K).

> **Flutter — lógica en el snapshot Dart (muro).** `assets/flutter_assets/` + `libapp.so`/
> `libflutter.so` (en el split arm64). La lógica de negocio y el flujo de pago viven en el
> snapshot Dart AOT → no decompilable estáticamente. Lo recuperable: el **backend de pago**
> `https://ssl1.agua.gob.mx/pagatuagua/` (HTTPS), un **formulario de tarjeta in-app** (manejo de
> tarjeta/CVV en Dart, pasarela no identificable estáticamente), Firebase + Google Maps +
> **AdServices** (atribución de publicidad). Firma **re-firmada por APKPure**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, pago, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (cámara, biometría, AdServices) |
| 02 | [02-flutter-muro.md](02-flutter-muro.md) | Flutter/Dart AOT, el muro |
| 03 | [03-pagos.md](03-pagos.md) | Backend `pagatuagua`, tarjeta in-app |
| 04 | [04-red-sdks.md](04-red-sdks.md) | Endpoints, Firebase, Maps, AdServices |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos y límites |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/firma.txt`
(no versionado).

## 5 hallazgos principales

1. **Flutter = lógica en el snapshot Dart (muro).** El flujo de pago, el manejo de tarjeta y la
   pasarela viven en `libapp.so` (Dart AOT); no decompilable estáticamente (doc 02).
2. **Backend de pago `https://ssl1.agua.gob.mx/pagatuagua/documentos/`** — HTTPS, en el dominio
   `agua.gob.mx` (doc 03/04).
3. **Formulario de tarjeta in-app.** El snapshot Dart maneja `card`/`tarjeta`/`CVV` (decenas de
   referencias), es decir la captura de tarjeta ocurre en la app; **qué pasarela y si tokeniza
   (PCI) no es verificable estáticamente** (doc 03) — pregunta abierta, no se afirma.
4. **AdServices + AD_ID en app de pago de agua.** `ACCESS_ADSERVICES_ATTRIBUTION`/`AD_ID` +
   Install Referrer → atribución de publicidad (recolección de identificadores con fines de
   anuncios), innecesaria para la función (doc 01/04) — observación de privacidad.
5. **Firma re-firmada por APKPure** (`CN=Android, O=Google Inc.`) → arquitectura sí, integridad
   no (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se realizó pago
alguno. La lógica Dart no se desensambló (muro); se documenta la superficie de red, el backend
de pago, los SDKs y la firma, con evidencia. **Firma** re-firmada por APKPure → no verificable
como auténtica de Agua de Hermosillo.
