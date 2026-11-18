# Hallazgos de seguridad (priorizados)

Todos estáticos; la app no se ejecutó. Cada hallazgo cita la evidencia (clase/método del smali en
`decompiled/` o campo del manifiesto).

## P1 — Crítico

**1. Validación TLS desactivada en la ruta de red activa (MITM).**
`ServiceGenerator$Companion.createService` instala un `X509TrustManager` con
`checkServerTrusted`/`checkClientTrusted` **vacíos** (`trustAllCerts$1`) y un `hostnameVerifier`
que **devuelve `true`**, y los aplica con `sslSocketFactory(...)` + `hostnameVerifier(...)` sobre
el `OkHttpClient$Builder`. Es la ruta que usan `LicenseInteractorImpl`, `PaymentInteractorImpl`,
`PhoneVerificationInteractorImpl`, `CatalogInteractorImpl`, `ConfigInteractorImpl`,
`LetterScreenInteractorImpl`, `AppointmentInteractorImpl` y `FaceApiInteractorImpl`. Sin
certificate pinning en todo el APK. → **MITM sobre licencia, pago (tarjeta/CVV), OTP SMS y
biometría.** (doc 04). Fix: eliminar el trust-all, usar validación estándar + pinning en la ruta
`ServiceGenerator`, igual que ya hace `NetworkModule`.

## P2 — Alto

**2. Pago con tarjeta in-app sobre el canal MITM-eable.**
Captura número de tarjeta/CVV/titular (`views.card.*`) y los envía por `PaymentInteractorImpl` →
`ServiceGenerator` (trust-all). Datos de tarjeta interceptables/alterables (doc 07). Fix: pasarela
PCI o, como mínimo, la ruta de red validada + pinning.

**3. Tráfico en claro.** `usesCleartextTraffic="true"` sin `networkSecurityConfig`, y el endpoint
de impresión es `http://impresion.licenciamanejo.com/objetosImpresionApi/` (datos de la licencia
en texto plano) (doc 01, 03). Fix: HTTPS en impresión + NSC que prohíba cleartext.

## P3 — Medio / bajo

**4. Restos de otro estado (Chiapas) en el binario de EDOMEX.** Endpoints
`chiapas-verifica-infraccion-*.run.app` y `www.sspc.chiapas.gob.mx` + `ChiapasVerificaInterceptor`
embebidos (white-label Cosmocolor). Filtra infraestructura de otros despliegues y amplía la
superficie (doc 02, 03). Fix: compilar por estado sin arrastrar config de otros.

**5. `androidx.compose.ui.tooling.PreviewActivity` exportada** — artefacto de desarrollo en el
release (doc 01). Fix: excluir del manifiesto de release.

**6. Contenido del QR escaneado al logcat.** `ValidateQrUrlUseCase` hace `Log.d` con el contenido
del QR (doc 06). Impacto bajo en Android moderno; conviene quitar el log en release.

## Postura positiva

- **SQLCipher con llave aleatoria por instalación** (`RandomKeyGenerator` + `SecureRandom`,
  `room.db.id`) → corrige la debilidad llave=PIN de la licencia **federal** (doc 05).
- `allowBackup="false"` (mitiga exfil por ADB de la llave/DB).
- **AES/CBC/PKCS5Padding** (no ECB).
- La licencia, firma electrónica y PII se guardan en la DB **cifrada**.

## Límites (no se afirma causa sin evidencia)

- Origen del *seed* TOTP y de la firma electrónica (aprovisionamiento desde `cosmokeys`) — no
  trazado. El QR podría o no ser falsificable según eso.
- Proveedor de pagos concreto — no identificado en el binario.
- Si `Preferences` añade `EncryptedSharedPreferences`/Keystore sobre la llave SQLCipher — no
  confirmado; `ObscuredSharedPreferences` (ofuscación) existe en el APK.
- `RECORD_AUDIO` declarado sin flujo de uso claro.
- Binario re-firmado por APKPure → sin garantía de integridad frente al original (doc 08).
