# Código nativo — arquitectura, red, auth y TLS (decompilado)

Decompilación **función por función** de los paquetes propios `com.embsoft.vinden.*` con
REA (JADX headless 0.7.1). Cuerpos citados en `decompiled/native/`. 1 872 clases propias de
16 303 totales. La app no se ejecutó.

## Mapa de módulos (del grafo de clases)

| Paquete | Clases | Rol |
|---|---:|---|
| `module.home` | 532 | Pantalla principal, mapa, navegación |
| `module.session` | 264 | Login / registro / sesión |
| `network.gateway` | 182 | Gateways de API (accounts, balances, routes) |
| `module.configuration` | 154 | Ajustes, perfil |
| `module.route` | 119 | Rutas / GPS |
| `common.view` | 109 | UI compartida (diálogos de tarjeta, etc.) |
| `common.network` | 40 | **Cliente HTTP, interceptores, token, API key** |
| `module.balance` / `balance_purchase` | 28 / 25 | Monedero y **recarga (pago)** |
| `data.dao` | 9 | greenDAO (BD local) |
| `helper.webview` | 7 | Puente WebView (`BIWebViewManager`, `CustomWebViewClient`) |
| `common.preferences` | 6 | SharedPreferences (`AppPreferences`, `DataStorageWrapper`) |

Arquitectura VIPER (Contract/Interactor/Presenter/Router/DataManager por módulo) con Hilt
(inyección) y Retrofit + OkHttp.

## Construcción del cliente HTTP y TLS — **SIN pinning** (verificado en código)

`ApiModule.provide()` → crea `NetworkManagerImp(BASE_URL_SERVICE, gson, [PrimaryAppInterceptor], ctx)`
(`decompiled/native/ApiModule.provide.java`). El `OkHttpClient` se arma en
`NetworkManagerImp.createOkHttpClient()` (`decompiled/native/NetworkManagerImp.createOkHttpClient.java`):

```java
s0.a aVar2 = new s0.a();               // OkHttpClient.Builder
aVar2.f711x = bf.c.b(30L, SECONDS);    // callTimeout 30s
aVar2.a(60L, SECONDS);                  // connectTimeout 60s
aVar2.f713z = bf.c.b(60L, SECONDS);    // readTimeout 60s
aVar2.f696h = true; aVar2.f697i = true; // retry/followRedirects (defaults)
for (RequestInterceptor ri : this.interceptors) { aVar2.f691c.add( ... ); }
return new s0(aVar2);
```

**Veredicto (evidencia en código):** el builder solo fija timeouts y añade el interceptor de
la app. **No hay `certificatePinner(...)`, ni `sslSocketFactory(...)`/`TrustManager`
personalizado, ni `hostnameVerifier(...)`.** La app **no implementa TLS pinning**: confía en
el almacén de confianza del sistema y en el `network_security_config` (que, además, permite
cleartext en dominios de producción y de pago — doc 03/08). Es el único constructor de
`OkHttpClient` de la clase (la ruta alterna `getAlternateRouteRetrofit()` reutiliza el mismo
Retrofit/cliente). No se halló ninguna clase propia de pinning/TrustManager/HostnameVerifier
en el grafo (`search_android_classes`). *Límite:* no se auditaron las 16 303 clases una por
una; la conclusión se basa en el constructor del cliente y en la búsqueda de clases.

> Tampoco es un `getUnsafeOkHttpClient` (como el crítico de Infonavit): no desactiva la
> validación — simplemente **no añade** defensa extra. El trust por defecto sí valida la
> cadena TLS; el problema es la combinación con el cleartext permitido.

## Interceptores

Solo un interceptor propio, `PrimaryAppInterceptor.interceptRequest`
(`decompiled/native/PrimaryAppInterceptor.interceptRequest.java`): añade el header
**User-Agent**. No hay interceptor de pinning ni de reintento de auth aquí.

## Headers de autenticación y API key (código)

El token y la API key se añaden en `ApiClientImp.modifyRequestHeaders`
(`decompiled/native/ApiClientImp.modifyRequestHeaders.java`). Tres modos
(`AuthorizationModeApp`):

```java
// API key del servicio: CIFRADA en el binario, descifrada en runtime
String apiKey = CryptoHelper.decrypt(cfg.getAPI_KEY_SERVICE().getApiKey(),
                                     appPreferences.getKy());
// modo 1 (token de usuario):
headers.put(AUTH_HEADER_NAME, tokenType + " " + accessToken);  // Authorization: <tipo> <token>
// modo 2 (API key):
headers.put(cfg.getAPI_KEY_SERVICE().getHeaderName(), apiKey);
// modo 3: Log.e("modifyRequestHeaders","ignored auth")  // sin auth
// siempre:
headers.put(ACCEPT_LANGUAGE, lang=="" ? "es" : lang);
headers.put(APPLICATION_ID, AndroidID.androidId(context));     // Android ID como app-id
headers.put("X-SESSION-DATA-APP", new TrackingEncryptionService().encryptedTrackingId());
```

Puntos clave:
- **Token** adjuntado como `Authorization: <tipoToken> <accessToken>` (estilo Bearer); el tipo
  lo resuelve `AuthorizationTokenManager.getTypeToken()` (JWT: hay `AuthenticationJWT`/`PayloadJWT`).
- **API key del servicio cifrada:** `CryptoHelper.decrypt(apiKey, appPreferences.getKy())` — la
  clave viaja cifrada en el binario y se descifra con una llave por instalación (`getKy()` en
  prefs). **Contrasta con la clave Google en claro del `.so`** (doc 05): esta sí está protegida.
- Envía el **Android ID** (`APPLICATION_ID`) y un **tracking cifrado** (`X-SESSION-DATA-APP`,
  `TrackingEncryptionService`) en cada petición.

## Flujo de autenticación (API de cuentas)

`AccountGatewayImp` (`network.gateway.accounts`) es la API de cuentas completa. `login`
(`decompiled/native/AccountGatewayImp.login.java`):

```java
RequestMethod method = RequestMethod.PATCH;                 // login por PATCH (inusual)
new ApiRequest(null, ApiEndPoints.Auth.loginAccount, PATCH, {}, {}, request, type, 1, null);
```

La superficie de cuentas incluye: `login`, `register`, `resetPassword`, `changePassword`,
`verifyCode` (**OTP**), `verifyCodeDevice` (**verificación de dispositivo / 2FA**),
`resendCode`, `getRefreshAccessToken` (**refresh token**), `validateWallet`, `deleteAccount`,
`updateUser`, `updateTokenNotification`. Endpoints centralizados en `ApiEndPoints.*` (p. ej.
`ApiEndPoints.Auth.loginAccount`). Login/registro verifican por código → hay flujo OTP y de
confirmación de dispositivo.

## Selección de entorno

`AppConfigurations.<clinit>` fija `environment = AppEnvironment.production`
(`decompiled/native/AppConfigurations.clinit.smali`). El build publicado apunta a
**producción**; los entornos dev/QA/prep existen en el enum `AppEnvironment` y en el
`network_security_config`, pero **no son el entorno activo** (doc 03 matizado).

## Ruta alterna (failover)

`NetworkManagerImp` implementa un mecanismo de **ruta alterna**: `HEADER_ALT_ROUTE`,
`alternateRoute`, `existsAlternatePath()`, `getAlternatePath()`, `getAlternateRouteRetrofit()`
y `pingUrl()`. Permite conmutar de base URL/ruta si la principal falla. No se analizó a fondo;
se menciona porque define una segunda vía de red con su propio Retrofit.

## Límites

- JADX puede aliasar nombres (campos OkHttp `f696h`/`f697i` ofuscados por R8; su mapeo a
  `followRedirects`/`retryOnConnectionFailure` es por posición/semántica, no por nombre).
- No se ejecutó la app: esto es comportamiento **declarado en código**, no observado en runtime.
- No se decompiló cada una de las 1 872 clases propias; se priorizaron red, auth y pago.
