# 07 · SDKs y librerías de terceros

Inventario por paquetes top-level en smali (excluye `com.infonavit`, `android*`, `kotlin*`,
`com.google.android.*` ya cubiertos). No se ejecutó nada; es presencia estática de clases.

## Servicios móviles

| SDK | Paquete / evidencia | Uso |
|---|---|---|
| Google Play Services (GMS) | `com.google.android.gms.*` | base, measurement/analytics, Maps, Places, common |
| Firebase | `com.google.firebase.*`, `firebaseinitprovider`, `micreditoinfonavit-15f0d.firebaseio.com` | Analytics, Installations, Realtime DB (reglas no verificadas) |
| Google Maps / Places | manifiesto `AutocompleteActivity`, `google_maps_key` | mapa y buscador de oficinas |
| **Huawei HMS** | `com.huawei.*`, AGConnect, `appid 104066105` | maps `6.11.0.300`, safetydetect `6.7.0.304`, dynamic-api — soporte a dispositivos sin Google |

## Red / serialización

| SDK | Paquete | Uso |
|---|---|---|
| OkHttp3 + Retrofit2 | `com.squareup`, `retrofit2.*` | cliente REST (ver doc 03) |
| Gson | `com.google.gson` | JSON (deserializadores custom en `RetrofitService`) |
| SimpleXML | `org.simpleframework` | SOAP/JAX-WS (servicios legados, doc 02) |
| RxJava | `io.reactivex`, `org.reactivestreams` | asincronía |

## PDF, documentos y cripto

| SDK | Paquete / lib | Uso |
|---|---|---|
| PDFium | `com.shockwave` + `libmodpdfium.so`/`libjniPdfium.so`/`libmodft2.so`/`libmodpng.so` | render de PDF (constancias, estados) |
| iText | `com.itextpdf` | generación/manipulación de PDF |
| BouncyCastle | `org.bouncycastle` | primitivas cripto (base de PGP y TLS legado) |
| **PGPainless + GnuPG** | `org.pgpainless`, `org.gnupg` | OpenPGP — cifrado/firma de datos o documentos (feature a confirmar) |

La presencia de una pila OpenPGP completa (PGPainless sobre BouncyCastle) es poco común; sugiere
cifrado/firma PGP de algún payload o documento. No se trazó el punto de uso en estático.

## UI / utilidades

| SDK | Paquete | Uso |
|---|---|---|
| Glide | `com.bumptech` | carga de imágenes |
| StepView | `com.kofigyan` | indicador de pasos (wizards de registro) |
| Skeleton/Shimmer | `com.ethanhua`, `io.supercharge` | placeholders de carga |
| JustifiedTextView y otros | `com.codesgood`, `com.cottacush`, `com.github` | widgets varios |
| slf4j | `org.slf4j` | logging (fachada) |

## API keys en recursos (H6)

Tres llaves Google (`AIza…`) embebidas:

- `res/values/strings.xml` → `google_maps_key` (Maps). Esperable en cliente, pero **debe estar
  restringida por package + firma SHA-1** en la consola de Google; no se puede verificar la
  restricción desde el APK.
- Una `AIza…` adicional dentro de `com.google.android.libraries.places.internal` (Places).

Las llaves `AIza` de Maps/Places son de cliente por diseño; el riesgo es abuso de cuota si no
están restringidas. No son secretos de backend (eso es el doc 04).

## Telemetría

GMS measurement + Firebase Analytics + `app-measurement.com`; Huawei `metrics*.hicloud.com` /
`dbankcloud.cn`. Permisos `AD_ID` y Privacy Sandbox declarados (doc 01). Install Referrer de
Play. No se evaluó qué PII se envía (requiere dinámico).
