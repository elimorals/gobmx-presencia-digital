# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se pagó ni se llamó a ningún
endpoint. JMAS Móvil es de las apps **mejor construidas** del acervo; los hallazgos son menores.

## Positivos (postura correcta)
- **P1 — Sin trust-all, validación TLS estándar.** `provideJmasOkHttpClient` arma el cliente sin
  `TrustManager`/`hostnameVerifier` custom (doc 03) → **no** replica el antipatrón de
  Infonavit/EDOMEX/ProcurApp.
- **P2 — HTTPS al dominio oficial, sin cleartext.** `https://jmaschih.gob.mx/jmas` (doc 03).
- **P3 — Pago delegado a BBVA hosteado.** La app no maneja datos de tarjeta (doc 04).
- **P4 — Permisos mínimos.** INTERNET + CAMERA (QR) + NETWORK_STATE (doc 01).
- **P5 — Arquitectura moderna.** Compose + Hilt + Retrofit + Room, con `ResultCall` para manejo de
  error y `TokenAuthenticator` para refresh (doc 02, 05).

## Mejorables (Bajo)
- **H1 (Bajo) — Token JWT en SharedPreferences planas.** Sin `EncryptedSharedPreferences`/Keystore
  (doc 05) → recuperable con root o respaldo. Fix: cifrar con MasterKey.
- **H2 (Bajo) — Sin certificate pinning.** Validación por defecto correcta, pero una app con auth
  y pagos debería pinnear su dominio (doc 03).

## Límites (sin inventar)
- Seguridad de la **URL de pago** (predecibilidad/reuso) y de la sesión BBVA: server-side, no
  observable (doc 04).
- `android:allowBackup` y caducidad del JWT: no confirmados; modulan el impacto de H1 (doc 05).
- **Integridad:** binario re-firmado por APKPure (doc 06) → conclusiones de arquitectura, no de
  integridad del artefacto original.
