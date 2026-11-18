# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: wallet de identidad (CURP, licencia, tarjeta de circulación) → el impacto se mide en
exposición de PII y tokens.

## H1 (Medio) — Logging de cuerpos HTTP en producción
`ServiceGenerator.createService` añade `HttpLoggingInterceptor` nivel `BODY` a todas las peticiones
(código en doc 04). Vuelca a **logcat** CURP, datos de licencia/tarjeta de circulación, OTP y
**tokens de sesión**. Fuga de PII y credenciales por los logs. **Fix:** `Level.NONE` en release.

## H2 (Bajo) — Sin certificate pinning
Validación TLS por defecto, sin `CertificatePinner` (doc 04). **No** es trust-all (no desactiva la
validación, a diferencia de Infonavit/Licencia EDOMEX), pero una app de identidad debería pinnear
sus dominios. **Fix:** añadir pinning a `*.bajacalifornia.gob.mx`.

## H3 (Bajo) — Entornos/sandbox embebidos en producción
7 variantes de URL + `sandbox.ebajacalifornia.gob.mx` en el build de producción (doc 04): fuga de
infraestructura interna. **Fix:** dejar solo producción.

## H4 (Bajo, no confirmado) — Sesión en SharedPreferences sin cifrado confirmado
El token de sesión usa `SharedPreferences`; no se confirmó cifrado por Keystore (doc 05).
Combinado con H1, el token tiene más de una vía de exposición. **Fix:** `EncryptedSharedPreferences`
+ Keystore.

## Pregunta abierta — Tokens de compartir tarjeta
`TokenCompartirTarjeta` comparte la tarjeta de circulación con terceros (doc 03). Si el token es
predecible o no caduca, superficie de acceso indebido. **No verificable estáticamente** (vive en el
backend). A revisar del lado del servidor. **No se afirma que exista.**

## Positivos

- **Auth sólida:** registro verificado por CURP + OTP SMS (Firebase) + **reCAPTCHA Enterprise**
  (doc 03).
- **Transporte HTTPS** a dominios oficiales de BC; sin cleartext; **sin trust-all** (doc 04).
- **Código nativo legible** (MVP, poca ofuscación) — auditable.
- **Permisos acotados** (salvo `READ_PHONE_STATE`); no pide ubicación/contactos/lectura de SMS
  (doc 01).

## Límites (sin inventar)
- Generación/validación de tokens (sesión, compartir) = backend, no observable.
- Cifrado real del token local = no confirmado (H4).
- Firma re-firmada por APKPure → conclusiones de arquitectura, no de integridad.
