# Ingeniería inversa de FiREL 1.0.14 — índice

Análisis **estático** del APK `gob.mx.android.firel` con REA (JADX headless). La app **nunca se
ejecutó**. Es la app oficial de **FiREL** — Firma Electrónica del Poder Judicial de la
Federación (Consejo de la Judicatura Federal, CJF). **#43 por instalaciones** del inventario
gob.mx (≈100 K).

> **No es un simple firmador: es una app de ENROLAMIENTO BIOMÉTRICO de identidad.** Construida
> por el proveedor **FielNet / IQSEC** (el paquete real es
> `com.fielnet.notificaciones_colegio_notarios_mvvm` — **reusa el codebase de una app de
> "Colegio de Notarios"**). Captura **INE (OCR + foto), rostro (match + liveness), huellas sin
> contacto (Identy.io + ONNX), firma autógrafa, video de verificación y ubicación**, y
> **genera el certificado** (e.firma/FIEL) vía **SOAP** a backends de IQSEC, más KYC con
> **ShuftiPro**. PII de las más sensibles del acervo. **Postura TLS contradictoria:** coexisten
> `CertificatePinner` (pinning) y un método **`getUnsafeOkHttpClient`** (cliente que suele
> desactivar la validación) — bandera seria **a confirmar** (ver doc 04). Re-firmada por APKPure.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, FielNet/IQSEC, biometría, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (cámara, video, ubicación, sensores) |
| 02 | [02-arquitectura-fielnet.md](02-arquitectura-fielnet.md) | White-label FielNet/IQSEC, MVVM, Volley/SOAP |
| 03 | [03-biometria-identidad.md](03-biometria-identidad.md) | INE/OCR, rostro, huella Identy, firma, video |
| 04 | [04-red-soap-tls.md](04-red-soap-tls.md) | Proveedores, SOAP, **TLS contradictorio** |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure, procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK (versionado) |
| `evidence/endpoints-y-proveedores.txt` | Endpoints + proveedores + notas TLS (no versionado) |
| `decompiled/flujo-identidad-clases.txt` | Mapa de clases del flujo de identidad (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 6 hallazgos principales

1. **Captura y transmite PII biométrica extrema.** INE (OCR + foto), **rostro** (match +
   liveness/anti-spoof), **huellas sin contacto** (Identy.io), **firma autógrafa** y un **video
   de verificación** — todo enviado por SOAP a los backends de IQSEC para generar el certificado
   (doc 03). Es el conjunto de datos más sensible del acervo.
2. **Postura TLS contradictoria (bandera alta, a confirmar).** El DEX contiene **a la vez**
   `okhttp3.CertificatePinner` (pinning) y un método **`getUnsafeOkHttpClient`** +
   `X509TrustManager`/`sslSocketFactory`/`setHostnameVerifier` (patrón de desactivación de TLS,
   como Infonavit). No se pudo atar el método inseguro a una clase propia ni confirmar qué ruta
   de red lo usa (posible librería) → se reporta como **riesgo a verificar**, no como MITM
   confirmado (doc 04).
3. **White-label con fugas de origen.** El paquete real es
   `com.fielnet.notificaciones_colegio_notarios_mvvm` → la app judicial **reusa el codebase de
   una app de "Colegio de Notarios"** del proveedor FielNet/IQSEC. Cadena de suministro: la
   firma electrónica del Poder Judicial corre sobre un producto genérico de un tercero (doc 02).
4. **Tres terceros en el flujo de identidad.** **Identy.io** (biometría + license servers),
   **ShuftiPro** (KYC, empresa extranjera) e **IQSEC** (`iqsec-masivos.com.mx`,
   `investigacion.iqsec.com.mx`). Datos biométricos de ciudadanos pasan por varios terceros
   (doc 04).
5. **SOAP con namespace sin configurar.** Los servicios usan `RequestEnvelope`/`ResponseEnvelope`
   (SOAP) y aparece **`tempuri.org`** (el namespace por defecto de .NET sin personalizar) —
   señal de WSDL no saneado (doc 04).
6. **Firma re-firmada por APKPure** (`CN=Android, O=Google Inc.`) + **JUnit** empaquetado en el
   release (bloat/fuga menor de artefactos de test) (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app, no se capturó
biometría ni se llamó a ningún endpoint. La relación exacta entre el método `getUnsafeOkHttpClient`
y las llamadas de biometría **no se confirmó** (R8/obfuscación + método posiblemente de
librería) — marcado como límite, sin inventar. **Firma** re-firmada por APKPure → arquitectura
sí, integridad no.
