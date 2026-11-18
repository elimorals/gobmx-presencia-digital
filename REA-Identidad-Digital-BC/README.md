# Ingeniería inversa de Identidad Digital BC 6.0.8 — índice

Análisis **estático** del APK `com.gobbc.identidaddigital` con REA (JADX headless). La app
**nunca se ejecutó**. Es la app oficial de **identidad digital del Gobierno de Baja California**
(GEBC): wallet ciudadano con CURP, licencia de conducir, **tarjeta de circulación** y documentos
digitales. **#33 por instalaciones** del inventario gob.mx (≈100 K).

> **Nativa Kotlin/Java (MVP), bien estructurada, sin Flutter/.NET.** Wallet de identidad completo:
> registro con verificación por CURP + OTP SMS (Firebase) + reCAPTCHA Enterprise, y **compartir
> tarjetas de circulación con terceros** vía tokens/QR. El hallazgo central no es TLS (usa HTTPS a
> dominios oficiales y **no** desactiva la validación, a diferencia de Infonavit/Licencia EDOMEX)
> sino que **registra los cuerpos HTTP completos en producción** (`HttpLoggingInterceptor` nivel
> `BODY`) — fuga de PII y tokens a logcat.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes |
| 02 | [02-arquitectura.md](02-arquitectura.md) | MVP nativo, módulos, SDKs |
| 03 | [03-identidad-tarjetas.md](03-identidad-tarjetas.md) | CURP, licencia, tarjeta de circulación, compartir |
| 04 | [04-red-tls-logging.md](04-red-tls-logging.md) | Endpoints HTTPS, TLS, **logging de cuerpos** |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `decompiled/ServiceGenerator.java` | Cliente Retrofit (logging + TLS) (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 6 hallazgos principales

1. **(Medio) Logging de cuerpos HTTP en producción.** `ServiceGenerator.createService` añade un
   `HttpLoggingInterceptor` con `setLevel(Level.BODY)` a **todas** las peticiones. En una app de
   identidad, eso vuelca a **logcat** los cuerpos completos: CURP, datos de licencia/tarjeta de
   circulación, OTP y los **tokens de sesión/acceso**. Cualquier app con acceso a logs (o vía
   ADB/malware) puede capturarlos (doc 04).
2. **(Bajo) Validación TLS por defecto, sin pinning (pero sin trust-all).** `createService`
   **no** instala `certificatePinner` ni `sslSocketFactory`/`TrustManager` custom → usa la
   validación estándar contra CAs del sistema. **No** desactiva la validación (a diferencia de
   Infonavit/Licencia EDOMEX) — postura aceptable, pero sin la capa extra de pinning para una app
   de identidad (doc 04).
3. **(Bajo) Entornos embebidos en producción.** `ServiceGenerator` deja 7 variantes de URL
   (`BASE_URL`, `BASE_URL_DES` desarrollo, `BASE_URL_OLD`, `BASE_URL_PROD_NEW`, …) y un endpoint
   **`sandbox.ebajacalifornia.gob.mx`** → fuga de infraestructura interna (doc 04).
4. **Compartir tarjeta de circulación con terceros.** Módulo `CompartirTarjeta` +
   `TarjetaCirculacionCompartida` + `TokenCompartirTarjeta` + lista de personas: el ciudadano
   comparte su tarjeta de circulación digital con otros usuarios mediante tokens/QR (doc 03).
   Superficie de abuso si los tokens son débiles — no verificable estáticamente.
5. **Auth razonable.** Registro verificado por **CURP** (`www.gob.mx/curp`) + **OTP SMS**
   (Firebase) + **reCAPTCHA Enterprise** (anti-bot). Patrón adecuado (doc 03).
6. **Firma re-firmada por APKPure.** `CN=Android, O=Google Inc.` → arquitectura sí, integridad no
   (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. El almacenamiento del token de sesión usa `SharedPreferences` (se vio referencia
a `AndroidKeyStore`, pero no se confirmó que cifre el token → marcado como no verificado). **Firma**
re-firmada por APKPure → no verificable como auténtica del GEBC.
