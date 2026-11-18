# 03 · TLS y cleartext

## Cleartext — AUSENTE-SANO

- Manifest **sin** `android:usesCleartextTraffic="true"`.
- **Sin** `networkSecurityConfig`.
- targetSdk 36 (≥28) → Android bloquea cleartext por default y **no hay NSC** que lo relaje.
- Todos los endpoints observados son `https://`.

→ **No** repite el patrón de cleartext de 911-CDMX / Veracruz Mujer Alerta. Sano.

## Trust-all TLS — AUSENTE (sin evidencia)

En `libapp.so` aparecen los símbolos:
`SecureSocket_RegisterBadCertificateCallback`, `registerBadCertificateCallback`,
`_onBadCertificateWrapper`, `SecurityContext_TrustBuiltinRoots`.

**Son símbolos del runtime estándar de Dart** (`dart:io` `HttpClient`/`SecurityContext`), presentes
en **toda** app Flutter. Su presencia **no** implica que la app sobreescriba
`badCertificateCallback` para aceptar cualquier certificado. Determinar si el callback se cablea a
`return true` requiere leer el Dart AOT (muro) → **dinámico** (Frida/reflutter).

**Veredicto:** no hay evidencia estática de trust-all. **No** se afirma que sea seguro ni que esté
roto — el runtime Dart no es observable aquí. Marcado AUSENTE (sin evidencia), no "confirmado sano".

## Secretos — AUSENTE

Barrido de strings en `libapp.so`, `resources.arsc` y `assets/`:
- Sin API keys (`AIza…`), sin Firebase/Maps key.
- Sin tokens/`Bearer`/`client_secret` hardcodeados (solo la palabra genérica `authorization`).
- Sin `.env` empacado.
- Sin IPs privadas (RFC1918).
- Sin credenciales en URL (`user:pass@` / `?pass=`).
