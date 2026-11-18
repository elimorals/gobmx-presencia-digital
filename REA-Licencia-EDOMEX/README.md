# Ingeniería inversa de Licencia Digital EDOMEX 16 — índice

Análisis **estático** del APK `mx.com.cosmocolor.movapp` (base del bundle XAPK) con REA
(JADX headless 0.7.1 + apktool 3.0.3). La app **nunca se ejecutó**. Es la licencia de
conducir digital del **Estado de México (SEMOV)**, #23 por instalaciones del inventario
gob.mx (500 K+). Desarrollada por el proveedor **Cosmocolor** (backend en el dominio
privado `licenciamanejo.com`, no en `*.edomex.gob.mx`).

> **Hallazgo crítico (P1):** la ruta de red principal (`ServiceGenerator`) construye un
> cliente OkHttp con un `TrustManager` que **acepta cualquier certificado** y un
> `hostnameVerifier` que **devuelve `true`** → **toda validación TLS está desactivada**.
> Esa ruta transporta activación de licencia, **pagos con tarjeta**, **OTP por SMS** y
> verificación facial. Misma clase de defecto que Mi Cuenta Infonavit (`getUnsafeOkHttpClient`).
> A diferencia de la licencia **federal** (`REA-Licencia`, que cifraba con llave = PIN), aquí
> la base de datos sí usa **SQLCipher con llave aleatoria por instalación** (mejor postura de
> almacenamiento, peor de transporte).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, `usesCleartextTraffic=true`, `allowBackup=false` |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Kotlin nativo, Hilt/Retrofit/Room/Compose, **dos pilas de red**, white-label multi-estado |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | Endpoints en `licenciamanejo.com`, **cleartext de impresión**, restos de Chiapas |
| 04 | [04-tls-mitm.md](04-tls-mitm.md) | **CRÍTICO: validación TLS desactivada** en la ruta activa (código citado) |
| 05 | [05-almacenamiento-cripto.md](05-almacenamiento-cripto.md) | SQLCipher + llave aleatoria (`room.db.id`), AES/CBC |
| 06 | [06-qr-totp-validacion.md](06-qr-totp-validacion.md) | QR por **TOTP**, firma electrónica, modo verificador (`ValidateQrUrlUseCase`) |
| 07 | [07-pagos-nfc-identidad.md](07-pagos-nfc-identidad.md) | **Pago con tarjeta in-app** sobre la ruta trust-all, NFC, Regula, Face API, OTP SMS |
| 08 | [08-firma-recursos.md](08-firma-recursos.md) | Firma re-firmada por APKPure (no auténtica de SEMOV) |
| 09 | [09-hallazgos-seguridad.md](09-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `decompiled/*.smali` | Clases clave: `ServiceGenerator`, `trustAllCerts$1`, `NetworkModule`, `RandomKeyGenerator`, `AES256Cipher`, `ValidateQrUrlUseCase` |
| `AndroidManifest.xml` | Manifiesto decodificado |
| `hashes.sha256` | SHA-256 del XAPK y del APK base |

> Lo propietario (APK, smali completo, recursos) **no se versiona** (`.gitignore`); vive en
> `descargas-apk/licencia-edomex/` y `evidence/`.

## 7 hallazgos principales

1. **(P1) Validación TLS desactivada en la ruta de red activa.**
   `ServiceGenerator$Companion.createService` instala `trustAllCerts$1`
   (`checkServerTrusted`/`checkClientTrusted` vacíos) y `hostnameVerifier { true }`
   (`sslSocketFactory`+`hostnameVerifier` en el `OkHttpClient$Builder`). La usan
   `LicenseInteractorImpl`, **`PaymentInteractorImpl`**, **`PhoneVerificationInteractorImpl`**,
   `CatalogInteractorImpl`, `ConfigInteractorImpl`, `LetterScreenInteractorImpl`,
   `AppointmentInteractorImpl` y `FaceApiInteractorImpl` → **MITM sobre licencia, pago, OTP y
   biometría** (doc 04). Sin certificate pinning en ninguna parte.
2. **(P2) Tráfico en claro.** `usesCleartextTraffic="true"` sin `networkSecurityConfig`, y el
   endpoint de impresión es **`http://impresion.licenciamanejo.com/objetosImpresionApi/`**
   (doc 01, 03).
3. **(P2) Pago con tarjeta dentro de la app sobre el canal MITM-eable.** Captura número de
   tarjeta/CVV/titular (`views.card.*`) y lo envía vía `PaymentInteractorImpl` →
   `ServiceGenerator` (trust-all) (doc 07).
4. **(P3) Restos de otro estado (Chiapas) en el binario de EDOMEX.** Endpoints
   `chiapas-verifica-infraccion-*.run.app` y `www.sspc.chiapas.gob.mx` embebidos: es un
   **white-label multi-estado** de Cosmocolor; fuga de superficie de otros despliegues (doc 02, 03).
5. **(P3) `PreviewActivity` de Jetpack Compose exportada** (artefacto de desarrollo) (doc 01).
6. **(Positivo) Almacenamiento mejor que la licencia federal.** Base de datos Room sobre
   **SQLCipher** con llave **aleatoria por instalación** (`RandomKeyGenerator` + `SecureRandom`,
   guardada como `room.db.id`), `allowBackup=false`, y `AES/CBC/PKCS5Padding` (no ECB) (doc 05).
7. **(Procedencia) Binario re-firmado por APKPure** (`CN=Android, O=Google Inc.`): **no
   verificable** como el artefacto auténtico de SEMOV/Cosmocolor (doc 08).

## Alcance y límites

Análisis **original** y **estático** de una app pública del gobierno del Estado de México, con
fines de investigación de seguridad. **No se ejecutó** la app, no se dispararon flujos de pago,
OTP ni biometría, no se llamó a ningún endpoint y **no se redistribuye** el APK. Los endpoints
documentados son rutas presentes en el binario, no secretos ni datos de usuarios. La ruta exacta
de aprovisionamiento del *seed* TOTP y de la firma electrónica, y el proveedor de pagos, no se
resolvieron por completo y se marcan como límites, no como conclusiones.
