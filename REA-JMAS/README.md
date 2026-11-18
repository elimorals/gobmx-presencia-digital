# Ingeniería inversa de JMAS Móvil 3.0.3 — índice

Análisis **estático** del APK `mx.gob.jmasmovil` con REA (JADX headless 0.7.1). La app **nunca
se ejecutó**. Es la app oficial de la **Junta Municipal de Agua y Saneamiento de Chihuahua
(JMAS)** — consulta de contratos, recibos, historial y **pago de agua**. **#39 por
instalaciones** del inventario gob.mx (≈100 K).

> **Nativa moderna y bien construida (Jetpack Compose + Hilt + Retrofit + Room).** De las apps
> con **mejor higiene** del acervo: HTTPS al dominio oficial, **sin `usesCleartextTraffic`**, y
> **sin el antipatrón trust-all** — el `OkHttpClient` se arma limpio, sin `TrustManager` ni
> `hostnameVerifier` custom (lo opuesto a Infonavit/Licencia EDOMEX/ProcurApp). El **pago se
> delega a una URL hosteada de BBVA**, así que la app **no maneja datos de tarjeta**. Lo
> mejorable es menor: token JWT en `SharedPreferences` planas y ausencia de certificate pinning.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, postura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, flags |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Compose/Hilt/Retrofit/Room, capas |
| 03 | [03-red-tls.md](03-red-tls.md) | Base URL HTTPS, `OkHttpClient` limpio, pinning |
| 04 | [04-pagos.md](04-pagos.md) | `getPaymentURL` → BBVA hosteado (sin tarjeta en la app) |
| 05 | [05-auth-almacenamiento.md](05-auth-almacenamiento.md) | JWT, TokenAuthenticator, SharedPreferences |
| 06 | [06-firma-recursos.md](06-firma-recursos.md) | Re-firmada por APKPure |
| 07 | [07-hallazgos-seguridad.md](07-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `decompiled/networkmodule.java` | `provideJmasOkHttpClient`/`Retrofit` + notas (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 5 hallazgos principales

1. **(Positivo) Sin trust-all y sin cleartext.** `NetworkModule.provideJmasOkHttpClient` arma el
   `OkHttpClient` solo con timeouts + `AuthInterceptor` + `TokenAuthenticator` — **sin**
   `sslSocketFactory`/`TrustManager`/`hostnameVerifier` custom → validación TLS estándar del
   sistema, no la desactivación que sí tienen otras apps del acervo (cuerpo citado en doc 03).
2. **(Positivo) Pago sin datos de tarjeta en la app.** `RemoteRepository.getPaymentURL` pide una
   URL de pago al backend y la abre en `ExternalLinkScreen`; la pasarela es **BBVA hosteada** →
   la app no captura ni transmite PAN/CVV (doc 04).
3. **(Positivo) HTTPS al dominio oficial.** `BuildConfig.BASE_URL = https://jmaschih.gob.mx/jmas`;
   sin `usesCleartextTraffic`; sin endpoints `http://` propios (doc 03).
4. **(Bajo) Token JWT en `SharedPreferences` planas.** `SharedPreferencesManager` usa
   `getSharedPreferences` estándar, **no** `EncryptedSharedPreferences`/Keystore → el token de
   sesión queda legible si el dispositivo está comprometido o respaldado (doc 05).
5. **(Bajo) Sin certificate pinning.** La validación por defecto es correcta, pero una app con
   auth y pagos debería pinnear su dominio (doc 03).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app, no se pagó nada ni
se llamó a ningún endpoint. **Firma** re-firmada por APKPure (`CN=Android, O=Google Inc.`) →
arquitectura sí, integridad no. Lo que ocurre del lado del servidor (y de la pasarela BBVA) no es
observable estáticamente.
