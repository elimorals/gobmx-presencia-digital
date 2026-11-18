# 00 · Resumen ejecutivo — Internet para el Bienestar

## Identidad

| Campo | Valor |
|---|---|
| App | Internet para el Bienestar (CFE Telecomunicaciones e Internet para Todos / MVNO estatal) |
| Package | `com.qvantel.bienestar.preprod` |
| versionName / versionCode | 2.10.7 / 111 |
| minSdk / targetSdk | 28 / 35 |
| Instalaciones (Play) | ~158 000 |
| Firma | `CN=Android, O=Google Inc.` (genérica) → **re-firmada por APKPure**, NO auténtica. Verificar contra canal oficial. |
| SHA-256 cert | `6e3ff0359ac19a1a5a7970d3539eac6688a90dfd52a8682b1dcd4785f38050da` (de la re-firma, no del emisor real) |
| SHA-256 XAPK | `bcc97980ef303c6006dfa1f2ffdc293088e4b744cf44e24e6d840e45dd74753d` |
| SHA-256 base APK | `4d88ab23c6df294acb555417ad88d23489b05e68d813bc2e44a930c926a97ab8` |
| Clases / recursos | 19 493 / 1 583 |
| Vendor | **Qvantel** (proveedor finlandés de BSS/billing para telecom) |

Package **verificado con `inspect_android_package`** antes de documentar: coincide con
`com.qvantel.bienestar.preprod`.

> **Bandera de entrada confirmada:** el package termina en `.preprod` (entorno de *preproducción*),
> pero los endpoints embebidos son de **producción** (`auth-prod...`, `mapp-prod...`). Es decir, el
> **build de preproducción se publicó en Google Play** apuntando a prod. Ver doc 02.

## Pila tecnológica

- **React Native (Nueva Arquitectura / Fabric) con Hermes** — `assets/index.android.bundle` es
  **bytecode Hermes** (7.4 MB), no JS legible. `libhermes.so`, `libreactnativejni.so`, decenas de
  `libreact_render_*`, `libreanimated.so`, `librnscreens.so` en el split `config.arm64_v8a`.
- Librerías nativas de terceros: **Sentry** (`libsentry.so`), **Crashlytics** (`libcrashlytics*.so`),
  **Facebook Conceal** (`libconceal.so`, cripto de almacenamiento), **PDFium** (`libjniPdfium.so`),
  Fresco (imagen).
- Dependencias JS embebidas en el bundle: **AWS SDK para JS** (toda la superficie de API de AWS
  aparece en la tabla de strings), **Flowable** (motor BPM — strings `flowable.startProcess`,
  `flowable.setVariables`), moment.js, react-navigation.
- **Muro de análisis estático:** el bytecode Hermes empaqueta los strings en una tabla concatenada;
  la extracción fina de endpoints/secretos no es fiable sin decompilar Hermes. Lo marcado abajo como
  A-CONFIRMAR es por este muro, no por falta de revisión.

## Mapa de módulos (features, inferidos de strings del bundle)

Autoservicio MVNO completo: gestión de **SIM/eSIM** (`ICCID`, `MSISDN`, `PUK`, `PIN`,
`esim-device`), **portabilidad** (`port-in-request`, `port-in-status`), **saldo y recargas**
(`balances`, `Prepaid`, `transfer-balance`), **pagos** (vía `paynet.com.mx`, `PaymentMethod`,
`PaymentPage`), **facturación** (`bills_bill-in-pdf`, PDFium), **login/OTP** (`signin-msisdn`,
`sms-otp`, `email-otp`), **ofertas/suscripciones**. Orquestación de procesos vía **Flowable**.

## Hallazgos (tabla)

| # | Hallazgo | Severidad | Estado |
|---|----------|-----------|--------|
| H1 | **Permisos privilegiados de operador telefónico** (SMS, call log, phone state privilegiado, APN, carrier services) en app de 158k | **ALTA** | CONFIRMADO |
| H2 | `usesCleartextTraffic="true"` en app de telecom/pagos | **MEDIA** | CONFIRMADO |
| H3 | Build **`.preprod`** publicado en producción (apunta a prod) | **MEDIA** | CONFIRMADO |
| H4 | `trustAll` presente en el bundle; no atable a ruta por el muro Hermes | — | **A-CONFIRMAR** |
| H5 | Nombres de propiedad de secreto (`ApiKey`/`Bearer`) sin valor confirmable | — | A-CONFIRMAR |

Detalle: [01-manifiesto-permisos.md](01-manifiesto-permisos.md) ·
[02-red-endpoints.md](02-red-endpoints.md) · [03-tls-cleartext.md](03-tls-cleartext.md) ·
[04-hallazgos.md](04-hallazgos.md).
