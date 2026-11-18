# Ingeniería inversa de Aguascalientes Digital 26.5.5 — índice

Análisis **estático** del APK `mx.gob.ags.refrendo` con REA (JADX headless). La app **nunca se
ejecutó**. Es la super-app oficial del **Gobierno del Estado de Aguascalientes (GEA)**: refrendo
y control vehicular, licencia de conducir, tarjeta de circulación, multas, pagos, expediente
digital, emergencias, anti-extorsión, asistente y noticias. **#46 por instalaciones** del
inventario gob.mx (≈100 K).

> **Super-app nativa Kotlin moderna (Hilt + Room + Moshi + Retrofit, MVVM; 19 071 clases).** Bien
> construida en lo estructural, con **15 clientes Retrofit separados** por backend. **No
> desactiva la validación TLS** (sin el antipatrón trust-all de ProcurApp/EDOMEX/Infonavit) —
> eso es lo correcto. Pero su `network_security_config` **permite tráfico HTTP en claro a los
> backends de producción, incluido el gateway de pagos `epagos`**, y el logging HTTP está en
> nivel **BODY** en todos los clientes. Firma **auténtica del GEA**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación, llamadas, cámara), componentes |
| 02 | [02-modulos-tramites.md](02-modulos-tramites.md) | Módulos, 15 clientes Retrofit, PII |
| 03 | [03-red-nsc-cleartext.md](03-red-nsc-cleartext.md) | **NSC: cleartext a producción + pagos**, logging BODY |
| 04 | [04-pagos-epagos.md](04-pagos-epagos.md) | ePagos/CitiBanamex, Wallet tokenizado |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma auténtica del GEA |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `evidence/network_security_config.xml` | NSC decodificado (no versionado) |
| `decompiled/NetworkModule-tls.java` | Cuerpo de `provideHttpLoggingInterceptor` + notas TLS (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 6 hallazgos principales

1. **(Medio-Alto) Cleartext HTTP permitido a producción, incluido el gateway de pagos.** El
   `network_security_config` tiene `base-config cleartextTrafficPermitted="false"` (bien) pero un
   `domain-config` con **`cleartextTrafficPermitted="true"`** que cubre `epagos.aguascalientes.gob.mx`
   (**pagos**), `egobierno2`/`eservicios2`/`expedientedigital.aguascalientes.gob.mx` (APIs y
   expediente), e `interfaces.c5ags.gob.mx` (**C5 emergencias**). El tráfico de pago/trámite/
   emergencia **puede viajar sin TLS** → MITM (doc 03).
2. **(Medio) Logging HTTP nivel BODY en los 15 clientes.** `provideHttpLoggingInterceptor` usa
   `level(BODY)` → vuelca a **logcat** los cuerpos completos (CURP, tarjeta de circulación,
   referencias de pago, datos de trámite). Redacta solo el header `Authorization`; **los cuerpos
   no** (doc 03, código citado).
3. **(Bajo-Medio) Entornos de desarrollo + IP cruda en el NSC de producción.**
   `apidesarrollo.c5ags.gob.mx`, `desarrolloweb.aguascalientes.gob.mx` y la IP `189.254.244.184`
   quedaron en el build publicado → fuga de infraestructura interna (doc 03).
4. **(Positivo) NO desactiva la validación TLS.** No se halló `getUnsafeOkHttpClient` ni
   `TrustManager`/`hostnameVerifier` trust-all → validación por defecto contra CAs del sistema.
   `okhttp3.CertificatePinner` está referenciado (config por-cliente no confirmada); contrasta con
   ProcurApp/EDOMEX/Infonavit (doc 03).
5. **(Positivo) Pagos con wallet tokenizado.** `WalletRequest`/`WalletTokenResponse`/`TokenResponse`
   + ePagos (gateway CitiBanamex) → la tarjeta se tokeniza, no se almacena en claro en la app
   (doc 04). *(La cleartext-a-epagos socava esta buena práctica.)*
6. **(Observación) Superficie amplia y sensible.** API de **CURP**, **screening de llamadas
   anti-extorsión** (`MyCallScreeningService` lee llamadas entrantes), **ubicación/emergencia**
   (`LocationService`), licencia de conducir, tarjeta de circulación y expediente digital
   (doc 01–02).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. La **firma es auténtica del GEA** (`O=GOBAGS`), así que los defectos están en el
código oficial. La configuración exacta de pinning por cliente y las reglas del servidor de
ePagos no son verificables estáticamente — marcado como límite, sin inventar.
