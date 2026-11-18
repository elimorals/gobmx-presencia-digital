# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se realizó pago.

## Observaciones
### O1 (Bajo-Medio, pregunta abierta) — Captura de tarjeta in-app sin pasarela identificable
El snapshot Dart maneja tarjeta+CVV (doc 03). No se identificó la pasarela ni claves de
tokenización en el binario → **si la tarjeta se tokeniza en el cliente (PCI-friendly) o viaja al
backend `pagatuagua` no es verificable estáticamente**. Superficie a revisar por el operador; no
se afirma un defecto.

### O2 (Bajo) — Tracking publicitario en app de pago de agua
AdServices (`ACCESS_ADSERVICES_*`/`AD_ID`) + Install Referrer + Firebase (doc 01/04): atribución
de publicidad, innecesaria para la función. Observación de **privacidad**, no vulnerabilidad.

## Positivos
- **Backend de pago HTTPS** (`ssl1.agua.gob.mx`); sin cleartext propio salvo el link del sitio
  (doc 03/04).
- **Biometría local** (USE_BIOMETRIC/FINGERPRINT) para el login.
- **Sin `.env`/secretos** expuestos; permisos sin ubicación/contactos/SMS (doc 01/02).

## Límites (el muro, sin inventar)
- **Lógica Dart (Flutter):** en `libapp.so`, no decompilable estáticamente (doc 02). Pasarela de
  pago, manejo/tokenización de tarjeta, auth y rutas de API bajo `pagatuagua` = **desconocidos**
  sin análisis dinámico (reflutter/blutter/Frida), fuera de alcance.
- **Pinning TLS:** no observable (capa de red en Dart).
- **Integridad:** binario re-firmado por APKPure (doc 05) → conclusiones de arquitectura, no de
  integridad.
