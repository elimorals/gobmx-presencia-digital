# 03 · TLS, certificado y pagos

## TLS / validación de certificado — AUSENTE-SANO

Se buscó explícitamente el antipatrón de las apps críticas del corpus (Infonavit/Cosmocolor) y
**no está presente**:

- **Sin clase `TrustManager` propia** — `search_android_classes("TrustManager")` → **0 coincidencias**
  a nivel de clase de la app.
- **Sin `getUnsafeOkHttpClient`**, sin `trustAllCerts`, sin `NukeSSLCerts`, sin `ServiceGenerator`.
- Las ocurrencias de `X509TrustManager` (11) y la única `checkServerTrusted` en el DEX son
  **referencias de librería** (OkHttp / Stripe SDK / Conscrypt), no una implementación trust-all de
  la app. No se halló un `checkServerTrusted` vacío atado a código propio.
- **targetSdk 36** + sin `usesCleartextTraffic` + sin NSC permisivo → cleartext bloqueado por
  plataforma.

**Veredicto:** la postura TLS es correcta. **No** repite el defecto de 911-CDMX (cleartext) ni el
de Infonavit (trust-all). Es un contraejemplo limpio entre las apps de pago del corpus.

> Límite honesto: el análisis es estático. Que no exista trust-all en el bytecode no prueba el
> comportamiento TLS en runtime (pinning efectivo, por ejemplo); solo que no hay el antipatrón
> conocido. Confirmar pinning requeriría dinámico.

## Pagos

- Pasarela: **Clip** (`Clip`/`CLIP`, 86 refs) sobre **Stripe SDK** (`Stripe`, 48 refs). Clip es el
  adquirente mexicano; el SDK de Stripe maneja la tokenización de tarjeta.
- Mención de `bbva` (4 refs) — posible banco emisor/relacionado, no se ató a un flujo.
- **No se hallaron credenciales de comercio ni llaves de pago en claro** en el DEX (a diferencia de
  Tesorería CDMX). Solo nombres de propiedad `api_key`/`api-key`, sin valores.
- Al usar el SDK de Stripe/Clip, la tokenización de tarjeta ocurre del lado del SDK/pasarela (PAN no
  debería tocar el backend propio) — postura correcta si se usa como está documentado. No se
  verificó el flujo completo (requeriría dinámico).
