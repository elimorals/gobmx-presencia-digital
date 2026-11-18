# 00 · Resumen ejecutivo — Tren Maya Boletos

## Identidad

| Campo | Valor |
|---|---|
| App | Tren Maya Boletos |
| Package | `com.indra.trenmaya.pro` |
| versionName / versionCode | 2.7.4 / 200700040 |
| minSdk / targetSdk | 29 / 36 |
| Firma | ⚠️ **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica** |
| SHA-256 cert | `4cf6d4188a52cdd1439ff4e62def94e1d0ace4b55e5fd2df042839887893c41f` (genérico APKPure) |
| SHA-256 XAPK | `c219c96c782f9d5dc19cc40a325677734cae1197006b450b52bd4c2d361fc840` |
| SHA-256 base apk | `48444c4f11648f34fd2cf0361b51e85657153af56d66d8488966f08d03eae885` |
| Clases / recursos | 13 939 / 1 180 |
| Dueño | Grupo Olmeca-Maya-Mexica (operador Tren Maya) · desarrollo: **Indra** |

Package **verificado con `inspect_android_package`** antes de documentar (regla dura): coincide
`com.indra.trenmaya.pro`.

> **Proveniencia:** el acervo analizado viene de APKPure y está re-firmado con el cert genérico
> `O=Google Inc.`. El bytecode no cambia al re-firmar (el análisis de código es válido), pero la
> **firma no prueba autenticidad** — cruzar contra el canal oficial (Google Play) antes de divulgar.

## Pila tecnológica

- **Nativa Android (Kotlin/Java)** — sin Flutter, sin React Native, sin Cordova, sin GeneXus.
  13 939 clases, sin muro de bytecode JS/Dart. Decompila limpio con JADX.
- **Red:** OkHttp/Retrofit (clientes HTTP estándar). Backends HTTPS.
- **Datos/telemetría:** MongoDB Realm/Atlas (`data.mongodb-api.com/.../realmsdkmetrics-zmhtm`),
  Firebase/Crashlytics, Google app-measurement (Analytics).
- **Pago:** **Clip** + **Stripe SDK** (Clip es el procesador mexicano; usa Stripe por debajo).
- **Publicidad/atribución:** Google AdServices (`AD_ID`, `ACCESS_ADSERVICES_*`), googlesyndication,
  Facebook SDK.
- **Nativo:** solo `libdatastore_shared_counter.so` (AndroidX DataStore); el split de arch es
  `armeabi_v7a` (32-bit) pese a que el XAPK se pidió arm64 — APKPure sirvió el split v7a.

## Backends (todos HTTPS)

- **API de venta de boletos:** `https://mobility.ventaboletostrenmaya.com.mx/trenmaya-web-0.31.0/`
  (dominio del proveedor, no `gob.mx`; versión web `0.31.0` embebida en la ruta).
- **Portal institucional:** `https://www.trenmaya.gob.mx/` (avisos, destinos, transparencia).
- **Operador:** `https://grupoolmecamayamexica.com.mx/`.
- **Métricas:** `https://data.mongodb-api.com/app/realmsdkmetrics-zmhtm/endpoint/metric_webhook`.

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | TLS y cleartext correctos — sin trust-all, sin http:// de backend, targetSdk 36 | — | — | ✅ **AUSENTE-SANO** |
| H2 | Telemetría/publicidad de terceros en app de gobierno (AD_ID, Analytics, Facebook, Realm) | CWE-359 | **BAJA-MEDIA** | CONFIRMADO |
| H3 | Versión del backend web expuesta en la ruta (`trenmaya-web-0.31.0`) | CWE-200 | **BAJA** | CONFIRMADO |
| H4 | Secretos hardcodeados | CWE-798 | — | ✅ **AUSENTE** (sin valores en claro) |
| H5 | Firma re-firmada APKPure (proveniencia) | — | INFO | CONFIRMADO |

Detalle en [04-hallazgos.md](04-hallazgos.md).
