# Red y endpoints

Fuente: `strings` sobre `classes*.dex` (→ `decompiled/endpoints.txt`, 47 URLs) y el
`network_security_config` (→ `evidence/network_security_config.xml`).

> **Entorno activo (verificado en código):** `AppConfigurations.<clinit>` fija
> `environment = AppEnvironment.production` (doc 09). El build publicado usa **producción**;
> los dev/QA/prep de abajo viven en el enum `AppEnvironment` y en el `network_security_config`
> pero no son el entorno activo. Siguen siendo config muerta enviada en el release.

## Entornos (el binario de producción lleva prod, prep, QA y dev)

| Entorno | API | Checkout de pago |
|---|---|---|
| **Producción (ATY)** | `https://api-recaudo.transporteyucatan.org.mx` | `https://checkout-recaudo.transporteyucatan.org.mx/transporte_checkout/checkout` |
| Prep | `https://api-prep.transporte.mrptechnology.mx` | `https://checkout-prep.transporte.mrptechnology.mx/.../checkout` |
| QA | `https://api-qa.transporte.mrptechnology.mx` | `https://checkout-qa.transporte.mrptechnology.mx/.../checkout` |
| Dev | `https://api-dev.transporte.mrptechnology.mx` | **`http://`**`checkout-dev.transporte.mrptechnology.mx/.../checkout` |

- El dominio de producción es de la ATY (`transporteyucatan.org.mx`); los no-productivos son
  del proveedor **MRP Technology** (`mrptechnology.mx`).
- **`checkout-dev` está en `http://`** (sin TLS) embebido en el binario.
- **Mock de Postman en producción:** `https://84984a54-bb03-4913-b040-77016e5c1fff.mock.pstmn.io`.
- Tokenización (datos de tarjeta): `tokenizacion-dev…` y `tokenizacion-qa.transporte.mrptechnology.mx`
  (no se observó el de producción en strings; no lo afirmo).

## Rastreo en vivo (SignalR)

La app usa **SignalR de Microsoft** (`com.microsoft.signalr.*`) contra un hub de camiones:

```
/api-movil/signalr/bus-hub   (en cada entorno: api-recaudo…, api-dev…, api-qa…, api-prep…)
```

Coherente con el permiso de ubicación y el rastreo de unidades en tiempo real.

## Rutas de API observadas (`/api-movil/…`)

```
/api-movil/cuentas/usuario-perfil
/api-movil/cuentas/usuario-tarifa
/api-movil/cuentas/save-fcm-token
/api-movil/notificaciones/{UserID}/...
```

Perfil de usuario, tarifa, registro de token FCM y notificaciones por `UserID`.

## Autenticación de identidad

Referencias a **Google Identity Toolkit / Firebase Auth**:
`identitytoolkit.googleapis.com/v2` y `www.googleapis.com/identitytoolkit/v3/relyingparty`.
Sugiere login federado/Firebase (coherente con permisos `GET_ACCOUNTS`/`USE_CREDENTIALS` y el
SDK de Facebook en doc 06). El detalle del flujo (OTP, correo, social) está en el código
nativo, no verificado byte a byte en este pase.

## `network_security_config` — cleartext permitido (hallazgo)

`<domain-config cleartextTrafficPermitted="true">` incluye, entre otros:

| Dominio | Qué es | Nota |
|---|---|---|
| `api-recaudo.transporteyucatan.org.mx`, `api.recaudo.transporteyucatan.org.mx` | **API de producción** | ⚠️ HTTP permitido |
| `checkout-recaudo.transporteyucatan.org.mx`, `checkout.recaudo…` | **Checkout de pago (prod)** | ⚠️ HTTP permitido |
| `appvayven.yucatan.gob.mx` | **Dominio oficial gob.mx** | ⚠️ HTTP permitido |
| `multicobros.banorte.com` | **Pasarela bancaria Banorte** | ⚠️ HTTP permitido |
| `*.qa-transporte.mrptechnology.mx`, `*.dev-transporte…`, `tokenizacion-dev…` | QA/dev/tokenización | — |
| `qa-transporte.imasd.com.mx`, `transporte.imasd.com.mx`, `qa-checkout.imasd.com.mx` | Proveedor **iMASD** | otro vendor |
| `10.0.2.2`, `192.168.68.57`, `191.101.33.196`, `4.157.186.140`, `4.255.74.88`, `localhost` | IPs/loopback | LAN del dev y emulador |

Permitir cleartext sobre **producción + pagos + el dominio gob.mx + una pasarela bancaria**
es el hallazgo de red más serio (doc 08).

## TLS pinning — NO hay (verificado en el código del cliente)

Revisado el constructor del `OkHttpClient` (`NetworkManagerImp.createOkHttpClient`, doc 09 y
`decompiled/native/NetworkManagerImp.createOkHttpClient.java`): **no hay `certificatePinner`,
ni `TrustManager`/`sslSocketFactory` personalizado, ni `hostnameVerifier`.** La app no hace
pinning; confía en el trust del sistema. No es un `getUnsafeOkHttpClient` (no desactiva la
validación como Infonavit) — simplemente no añade defensa, y combinado con el cleartext
permitido deja la puerta a MITM. El token de usuario va como `Authorization: <tipo> <token>` y
la API key del servicio como header cifrado+descifrado en runtime (doc 09).

## Terceros de red

MercadoPago (`sdk.mercadopago.com`, `mercadopago.com.mx`), PayPal (`paypal.com/sdk/js`),
Braintree (`js.braintreegateway.com`), Firebase (analytics, remote config, installations),
Facebook/Graph, Google Ads (`googleadservices.com`, `pagead2.googlesyndication.com`),
reCAPTCHA (`recaptcha.net/recaptcha/api3`), y contenido oficial
`movilidad.yucatan.gob.mx/files-content/.../avisovayven.htm` (aviso de privacidad).
