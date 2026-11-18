# Hallazgos de seguridad (priorizados)

Observaciones **estáticas**. Cada una lleva su evidencia. La app no se ejecutó; donde la
severidad depende del comportamiento en runtime o del servidor, se dice explícitamente.

## 1. Alto — Cleartext (HTTP) permitido sobre producción, pagos, dominio gob.mx y banco

`android:usesCleartextTraffic="true"` + `network_security_config` con
`cleartextTrafficPermitted="true"` para, entre otros:
`api-recaudo.transporteyucatan.org.mx` (API prod), `checkout-recaudo.transporteyucatan.org.mx`
(checkout de pago), **`appvayven.yucatan.gob.mx`** (dominio oficial) y
**`multicobros.banorte.com`** (pasarela Banorte).

- **Evidencia:** `evidence/network_security_config.xml`; `AndroidManifest.xml`.
- **Riesgo:** si cualquiera de esos hosts responde o redirige por HTTP (o un atacante en red
  fuerza downgrade), el tráfico —incluido el de pago y PII— viaja sin TLS → MITM.
- **No afirmado:** que esos hosts sirvan HTTP hoy; lo que consta es que la app lo **permite**.
- **Fix:** quitar los dominios de producción y el bancario de la lista cleartext; poner
  `cleartextTrafficPermitted="false"` por defecto y dejar HTTP solo para `localhost`/IPs de
  desarrollo en builds debug.

## 2. Medio-Alto — Sin TLS pinning (confirmado en el código del cliente)

`NetworkManagerImp.createOkHttpClient()` arma el `OkHttpClient` sin `certificatePinner`, sin
`TrustManager`/`sslSocketFactory` custom y sin `hostnameVerifier`
(`decompiled/native/NetworkManagerImp.createOkHttpClient.java`, doc 09).

- **Evidencia:** código decompilado del único constructor del cliente.
- **Riesgo:** sin pinning, un certificado emitido por una CA de confianza del sistema (o
  instalada en el dispositivo) permite interceptar el tráfico; **sumado al cleartext permitido**
  (hallazgo 1) el margen de MITM sobre pagos y PII es amplio.
- **Matiz:** no es `getUnsafeOkHttpClient` (no desactiva la validación como Infonavit); la
  cadena TLS sí se valida por defecto. Es ausencia de defensa en profundidad, no una
  desactivación activa.
- **Fix:** añadir `CertificatePinner` para los dominios de producción y de pago.

## 3. Medio-Alto — Clave de Google API en claro en `libapi-keys.so`

Clave `AIzaSy…` recuperable con `strings` (`NativeKeysHelper.getPlacesKey`).
- **Evidencia:** `evidence/libapi-keys-strings.txt`.
- **Riesgo:** si no está restringida por package+SHA-1 y por API en Google Cloud, es abusable
  (cuota/costos de Maps/Places).
- **Fix:** restringir la clave en consola; no depender de "ocultarla" en una `.so`.

## 4. Medio — Credencial TEST de MercadoPago en el binario de producción

`initMercadoPago('TEST-…')` en el bundle React de pago.
- **Evidencia:** `decompiled/web-src/MercadoPagoButton.js`.
- **Riesgo/duda:** o es residuo (se sobreescribe en runtime con la de prod) o los cobros van al
  sandbox. **No determinado** sin ejecutar.
- **Fix:** inyectar la public key por entorno en build; no hardcodear la de TEST en release.

## 5. Medio — Entornos no productivos e infraestructura interna embebidos

En el binario de producción: `api-dev/qa/prep…`, `http://checkout-dev…` (sin TLS),
`tokenizacion-dev/qa…`, un **mock de Postman** (`*.mock.pstmn.io`), y en el config de red IPs
internas (`192.168.68.57`, `10.0.2.2`) y públicas sueltas.
- **Evidencia:** `decompiled/endpoints.txt`, `evidence/network_security_config.xml`.
- **Riesgo:** amplía superficie y revela topología/infra interna; un endpoint de QA/dev abierto
  podría tener menos controles.
- **Matiz (código):** el entorno activo es `production` (`AppConfigurations.<clinit>`, doc 09);
  los dev/QA/prep son config muerta que viaja en el release, no el backend en uso.
- **Fix:** separar config por flavor; no enviar dev/QA/mock en el release.

## 6. Bajo — Veredicto de pago decidido en el cliente (a verificar en servidor)

`MercadoPagoButton.js` decide éxito con `collection_status == "approved"` de la URL de retorno
y lo reporta por `AndroidNative.responsePay(true)`.
- **Evidencia:** `decompiled/web-src/MercadoPagoButton.js`.
- **Riesgo:** si el servidor acredita saldo confiando en ese `responsePay` del cliente (y no en
  un webhook/consulta server-side a MercadoPago), sería manipulable. **No comprobado** del lado
  servidor (fuera de alcance estático).
- **Fix:** acreditar saldo solo tras confirmación server-side del pago.

## 7. Bajo — Fuga de identidad del desarrollador

Ruta de build con nombre de usuario (`eduardoezequielargaezmay`), repo y proyecto en
`libapi-keys.so`.
- **Evidencia:** `evidence/libapi-keys-strings.txt`.
- **Riesgo:** información, no explotación directa.

## 8. Informativo — Permiso `WRITE_EXTERNAL_STORAGE` y `CALL_PHONE`

`WRITE_EXTERNAL_STORAGE` (amplio/legacy en targetSdk 36) y `CALL_PHONE` (marca sin confirmación
del usuario; `ACTION_DIAL` no requiere permiso). Revisar si se usan realmente.
- **Evidencia:** `AndroidManifest.xml` (doc 01).

## Cubierto en la decompilación profunda (doc 09)

- **TLS pinning:** verificado en el constructor del cliente → **no hay** (código citado).
- **Auth:** header `Authorization: <tipo> <token>`, API key del servicio cifrada, login PATCH,
  OTP + verificación de dispositivo, refresh token.
- **Red:** entorno activo = production; ruta alterna (failover); interceptor de User-Agent.
- **Pago:** flujo nativo `balance_purchase` → orden server-side → WebView de pasarela.

## Lo que este pase NO cubrió

- Internals del puente WebView (`BIWebViewManager`/`CustomWebViewClient`): qué URL carga y si
  habilita JS sobre contenido no confiable (apuntado en doc 02, no decompilado a fondo).
- Cifrado local a fondo: `greenDAO`/SQLCipher y `DataStorageWrapper` (llave de BD, qué guarda).
- El lado servidor del pago (confirmación del cobro) — fuera de alcance estático.
- Comportamiento en runtime (la app no se ejecutó).
