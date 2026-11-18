# 03 · TLS y cleartext

## Cleartext — AUSENTE-SANO

- Manifest **sin** `usesCleartextTraffic`; `targetSdk=34` ⇒ cleartext **bloqueado por defecto**.
- **Sin** `networkSecurityConfig` permisivo. No se confía en CAs de usuario.
- Backend 100% HTTPS (ver doc 02).

**No repite el defecto de cleartext de 911-CDMX / Veracruz Mujer Alerta** (clúster D del mapa).

## Trust-all / validación TLS — A-CONFIRMAR (muro Dart)

- **No aplica el antipatrón del clúster A** (`getUnsafeOkHttpClient`/`ServiceGenerator` de OkHttp/
  Retrofit): esta app es Flutter, no usa OkHttp. Ese patrón es de apps nativas Kotlin/Java.
- En Dart, la validación TLS se controla con `HttpClient.badCertificateCallback` /
  `SecureSocket.onBadCertificate`. En los strings de `libapp.so` aparece el símbolo
  **`SecureSocket_RegisterBadCertificateCallback`**.
  - ⚠️ **Matiz importante (no inventar causa):** ese símbolo es parte del **runtime de Dart
    (`dart:io`) y está presente en TODA app Flutter**, use o no un callback permisivo. **No es
    evidencia de trust-all.** No se pudo determinar estáticamente si la app registra un callback que
    acepte certificados inválidos — el cuerpo vive en el AOT.
- **Conclusión:** no hay evidencia de desactivación de TLS, pero tampoco se puede confirmar la
  validación efectiva. Se marca **A-CONFIRMAR**; requeriría análisis dinámico (reflutter/Frida con
  hook a `badCertificateCallback`).

## Pinning

No se detectó certificate pinning (ni en Dart ni en NSC). Es validación TLS por defecto del sistema
(si el cliente Dart no la desactiva), sin pinning — postura estándar, no un defecto.
