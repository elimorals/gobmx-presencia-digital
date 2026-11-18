# 00 · Resumen ejecutivo — Descarga Cultura UNAM

## Identidad

| Campo | Valor |
|---|---|
| App | Descarga Cultura UNAM |
| Package | `mx.unam.codc.android.descargaculturaunam` (verificado con `inspect_android_package`) |
| versionName / versionCode | 4.9.1 / 49001 |
| minSdk / targetSdk | 23 / 36 |
| Firma | **Auténtica UNAM** — `CN=UNIVERSIDAD NACIONAL AUTONOMA DE MEXICO, OU=UNAM, O=UNIVERSIDAD NACIONAL AUTONOMA DE MEXICO, L=MEXICO, ST=D.F., C=MX` |
| SHA-256 cert | `7aab76d22af7ef675236513c1e36b3eca6dc681891d06840ba2c77233709c098` |
| sha256 XAPK | `854897319b31cf092db4d5aaa185fbd0cbb437a6b13f6e21c200832d0242214e` |
| Clases / recursos | 9 585 / 892 |
| Dueño | UNAM (Coordinación de Difusión Cultural) |

## Pila tecnológica

- **Nativa Android (Kotlin/Java)** — no Flutter, no React Native, no .NET. `DataBinderMapperImpl`
  (Android Data Binding) confirma app nativa con MVVM.
- **Red:** Retrofit2 + OkHttp3, con `okhttp3.CertificatePinner` presente (posible pinning).
- **Imágenes:** Facebook **Fresco** (`libimagepipeline.so`, `libnative-filters.so`,
  `libnative-imagetranscoder.so`).
- **Almacenamiento:** AndroidX DataStore (`libdatastore_shared_counter.so`).
- **Servicios:** reproducción de audio en primer plano (contenido cultural), Firebase
  (Analytics/Crashlytics/Installations).

## Propósito

App de **contenido cultural en audio** de la UNAM (podcasts, lecturas, música). Descarga y
reproducción de audio. Perfil funcional de **bajo riesgo** — confirmado.

## Veredicto de seguridad

**SANO / bajo riesgo** — contraejemplo útil frente a las apps con defectos. No repite ningún
patrón crítico del `analisis/MAPA-PROVEEDORES-SDK.md` (ni trust-all, ni cleartext real, ni
secretos, ni sobre-permisología). Detalle en [03-red-tls.md](03-red-tls.md) y
[04-hallazgos.md](04-hallazgos.md).

## Hallazgos (tabla)

| # | Hallazgo | Severidad | Estado |
|---|----------|-----------|--------|
| H1 | NSC `base-config cleartextTrafficPermitted=true` global (pero backends son HTTPS y trust-anchors solo `system`) | **BAJA** | CONFIRMADO |
| H2 | `http://localhost/` embebido (artefacto de desarrollo, inerte) | **INFO** | CONFIRMADO |
| H3 | Trust-all TLS (`getUnsafeOkHttpClient`/`trustAllCerts`) | — | **AUSENTE-SANO** |
| H4 | Secretos hardcodeados / API keys / IPs internas | — | **AUSENTE-SANO** |
