# 02 · Red, TLS y cleartext — Tuzobús

## Postura de red (CONFIRMADA, sana)

- `usesCleartextTraffic="false"` en el manifest → el runtime bloquea HTTP en claro (targetSdk 36).
- `allowBackup="false"` → el almacenamiento local no se extrae por `adb backup`.
- **No** se halló trust-all a nivel nativo/DEX: sin `getUnsafeOkHttpClient`, sin `X509TrustManager`
  con `checkServerTrusted` vacío, sin `HostnameVerifier` permisivo. (La validación TLS efectiva del
  cliente RN/fetch vive en Hermes — ver abajo.)
- Firma **auténtica** del Gobierno de Hidalgo (no re-firmada por tienda).

**No repite el defecto de 911-CDMX** (cleartext en app de transporte/pago). Contraejemplo limpio.

## Endpoints recuperables

Del `strings` sobre `index.android.bundle` (Hermes) y los DEX, el único backend propio recuperable:

- **`https://apis-tests.hidalgo.gob.mx`** → **entorno de PRUEBAS** (`apis-tests`) embebido en el
  build de producción. HTTPS (no es cleartext), pero es infraestructura de pruebas en una app
  publicada. Ver H1 (CWE-489).

El resto de hosts recuperados son de librerías/documentación (reactnavigation.org, react.dev,
notifee.app, etc.), no backend. El endpoint de **producción real** no apareció como literal: o se
construye dinámicamente en el bundle Hermes, o su string no es recuperable por `strings`.

## Muro Hermes (límite honesto)

`assets/index.android.bundle` es **bytecode Hermes**, no JS legible. De él sólo se recuperan strings
parciales. Quedan **sin confirmar** por estático:

- El endpoint de producción real y si convive con `apis-tests`.
- La **pasarela de pago / manejo de tarjeta** del flujo de recarga (no apareció literal de
  Conekta/Openpay/Paynet/etc.).
- Secretos/API keys en el bundle.
- La validación TLS efectiva del cliente HTTP de RN en runtime.

Confirmarlo requiere **análisis dinámico** (Frida, o `hbctool`/`hermes-dec` sobre el bundle).
