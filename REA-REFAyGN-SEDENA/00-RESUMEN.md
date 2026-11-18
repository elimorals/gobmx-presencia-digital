# 00 · Resumen ejecutivo — REFAyGN (SEDENA)

## Identidad

| Campo | Valor |
|---|---|
| App | REFAyGN (Registro Federal de Armas de Fuego y Gestión Nacional) |
| Package | `com.sedena.refam` |
| versionName / versionCode | 1.0.19 / 19 |
| minSdk / targetSdk | 29 / 34 |
| Firma | `CN=Android, O=Google Inc.` — **re-firmada por APKPure**, no auténtica de SEDENA |
| SHA-256 cert | `ebc36a3a256a00e83caac50e6af5141dbd1eee7e0e6bd85d0d2b5dad250a9ca8` (genérico APKPure) |
| SHA-256 XAPK | `d996c3624c084e15321eeb61e65035a0c80c1d422605a3b6bd4b0ade504c7fa7` |
| Clases / recursos | 940 / 145 |
| Dueño | Secretaría de la Defensa Nacional (SEDENA) |

Package **verificado con `inspect_android_package`** sobre el base APK extraído del XAPK:
coincide con `com.sedena.refam`.

> **Proveniencia:** el artefacto fue re-firmado por la tienda (APKPure), así que la firma **no**
> prueba autenticidad. El vínculo con SEDENA se apoya en el backend oficial
> `reefam.sedena.gob.mx` y el package `com.sedena.refam`. Para divulgar, cruzar contra Google Play.

## Pila tecnológica

- **Flutter** — `libflutter.so` + `libapp.so` (Dart AOT, arm64) en el split `config.arm64_v8a`.
  El código Dart compilado a AOT es un **muro** para el análisis estático (no hay bytecode legible;
  solo strings).
- **PDFium** (`libjniPdfium.so`, `libmodpdfium.so`, `libmodft2.so`, `libmodpng.so`) — render de PDF
  (probable visor de constancias/registros).
- **Sin Firebase, sin google-services.**

## Mapa de módulos

App ligera (940 clases, casi todo el código real vive en el Dart AOT). Flujo aparente: registro/
consulta de armas de fuego ante SEDENA + visor PDF de constancias. El detalle funcional queda tras
el muro Dart.

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | Re-firmada por APKPure; autenticidad no probable por firma | — | INFO (proveniencia) | CONFIRMADO |
| H2 | Red bien construida: HTTPS, sin cleartext, permisos mínimos, sin secretos | — | **AUSENTE-SANO** | CONFIRMADO |
| H3 | Validación TLS efectiva del cliente Dart no verificable (muro AOT) | CWE-295 | A-CONFIRMAR | — |

## Veredicto

**App sana en la capa de red** (hasta donde el estático alcanza). Contraejemplo útil: una app de
máxima sensibilidad (registro de armas, SEDENA) que **no** repite ningún defecto del
[mapa de proveedores](../analisis/MAPA-PROVEEDORES-SDK.md) — sin cleartext (clúster D), sin trust-all
(clúster A), sin secretos embebidos. Las únicas reservas son la **proveniencia** (re-firma APKPure) y
el **muro Dart** (la validación TLS real del cliente requeriría análisis dinámico).
