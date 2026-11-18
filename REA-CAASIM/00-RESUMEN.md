# 00 · Resumen ejecutivo — CAASIM (agua Hidalgo)

## Identidad

| Campo | Valor |
|---|---|
| App | CAASIM (Comisión de Agua y Alcantarillado de Sistemas Intermunicipales, Hidalgo) |
| Package | `mx.gob.hidalgo.caasim.app` (verificado con `inspect_android_package`) |
| versionName / versionCode | 1.0.39 / 40 |
| minSdk / targetSdk | 23 / 36 |
| Firma | ⚠️ **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica**; cruzar contra el canal oficial |
| SHA-256 cert | `ad:57:75:61:5e:59:93:f8:95:2f:e9:f1:bd:1b:ec:5e:9b:3e:59:49:88:1b:19:3b:1f:82:cc:07:76:dc:a4:8d` |
| SHA-256 XAPK | `3958f4652de89766c69912964c000eafd156d3f9ca213b89ea7625bbe891446c` |
| SHA-256 base APK | `51e3ef7984d39896ed613e5d8741e90cfaffbc9d9be846bc7d1fff36b3a3e0ea` |
| Clases / recursos | 10 404 / 1 184 |
| Dueño | CAASIM (organismo de agua, Gobierno de Hidalgo) |

## Pila tecnológica

- **Nativa Android (Kotlin/Java)** — sin Flutter, sin React Native, sin .NET. No hay `libapp.so`/
  `libflutter.so`/`index.android.bundle` en el base ni en los splits.
- **Red:** OkHttp/Retrofit (ships `assets/PublicSuffixDatabase.list` y la maquinaria
  `getX509TrustManagerOrNull$okhttp` — huella de OkHttp estándar).
- **Nativo:** PDFium (`libjniPdfium.so`, `libmodpdfium.so`, `libmodft2.so`, `libmodpng.so`) para
  render de recibos PDF; GIF (`libpl_droidsonroids_gif.so`); DataStore.
- **Pagos:** pasarela **Adquira México** (`www.adquiramexico.com.mx`).

## Backends

- **API de la app:** `servicioscaasim.hidalgo.gob.mx` (aparece en **http** y **https**).
- **Vendor:** `caasimapps.com` (dominio del proveedor de la app).
- **Pasarela de pago:** `www.adquiramexico.com.mx` (HTTPS) — **mismo procesador que Tesorería CDMX**
  (que usa `adquiracloud.mx`). Cruce de proveedor de pagos CDMX ↔ Hidalgo.

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | NSC habilita **cleartext HTTP** para el dominio de la API (`servicioscaasim.hidalgo.gob.mx`) + `caasimapps.com` en una app de **pagos de agua** | CWE-319 | **ALTA** | CONFIRMADO (config); A-CONFIRMAR qué llamada concreta usa http |
| H2 | Pasarela de pago **Adquira México** compartida con Tesorería CDMX (cruce de proveedor) | — | INFO | CONFIRMADO |
| H3 | **Sin trust-all TLS** — no es el defecto de Infonavit/Cosmocolor | CWE-295 | — | **AUSENTE-SANO** |
| H4 | Proveniencia: APK re-firmada por la tienda; la firma no prueba autenticidad | — | INFO | CONFIRMADO |
| H5 | Permisos amplios (`CAMERA`, `CALL_PHONE`, `READ_PHONE_STATE`, `MANAGE_DOCUMENTS`, `READ/WRITE_USER_DICTIONARY`) para un autoservicio de agua | CWE-250 | BAJA | CONFIRMADO |

Detalle en [02-red-endpoints-tls.md](02-red-endpoints-tls.md) y [03-hallazgos.md](03-hallazgos.md).

## Cruce con el mapa (`analisis/MAPA-PROVEEDORES-SDK.md`)

- **Clúster D (cleartext):** suma CAASIM — pago/agua con cleartext permitido al dominio de la API.
- **Clúster C (backends/proveedores compartidos):** **Adquira** une Tesorería CDMX y CAASIM Hidalgo
  (procesador de pagos cross-estado), análogo a `egbs5.com.mx` (Tesorería CDMX ↔ Japay).
- **Clúster A (trust-all):** **no** aplica — CAASIM es sano en TLS.
