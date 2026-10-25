# 11 — Análisis dinámico (dispositivo real, sin root)

Pase dinámico sobre **Motorola XT2435-3, Android 16 (SDK 36), sin root**, por cable (adb). Se
instaló el bundle (`install-multiple`) y se observó el **arranque en frío**. Evidencia en
`evidence/logcat-*.txt`, `evidence/endpoints-urbani.txt`, `evidence/config-city-MTY.json.txt`,
`evidence/pantalla-arranque.png`.

> **Resultado clave:** aunque el packer cifra el Dart (doc 03), la app tiene **logging verboso
> activo en release** (canal `flutter [GENERAL]/[NETWORK]`). El logcat **reveló el backend real,
> los endpoints y la arquitectura** que el análisis estático no pudo recuperar. El pase dinámico
> fue, con diferencia, el más productivo de este REA.

## Instalación y arranque

```
install-multiple base + config.armeabi_v7a + config.xhdpi → Success
primaryCpuAbi = armeabi-v7a    (el dispositivo soporta 32-bit vía abilist32)
Proceso vivo tras lanzar → el packer NO aborta pese al re-firmado
Carga nativa: libbaiduprotect_sdk-…so cargada por nativeloader (packer activo en runtime)
Procesos: com.miruta.mty  +  com.miruta.mty_zygote (appZygote)  +  com.miruta.mty:e (isolated, o.setCompoundDrawables…)
```

## Backend real recuperado (HTTPS, respuestas 200 en vivo)

```
https://static-cdd-urbani-app-prod.urbani.io/api/v1/configs/cities/{MTY,NA}   (config por ciudad)
https://app-static-prod.urbani.io/app/v2/support/url
https://auth-prod.urbani.io/app/data/service/states                          (geocercas de ciudades)
https://cdn.urbani.mx/splash/monterrey_v1.mp4                                 (CDN de assets)
```

Endpoints **autenticados** detectados por nombre (omitidos en el log por estar sin sesión):
**`messages`, `mobility`, `payments`, `urbicoins`**.

> **El backend es `urbani.io` (dominio privado), no gob.mx.** Urbani lo opera una empresa privada
> para el municipio; el dato de transporte municipal pasa por infraestructura de un tercero
> comercial. Todo observado fue **HTTPS** (coherente con cleartext bloqueado, doc 04).

## Arquitectura (server-driven UI)

La respuesta de `configs/cities/MTY` (guardada en `evidence/config-city-MTY.json.txt`) define la UI
desde el servidor:

```json
{"data":{"schemaVersion":"1.9.19",
 "city":{"id":"MTY","displayName":"Monterrey, N.L.","hasCoverage":true},
 "navBar":{"tabs":["movilidad","miCiudad","urbiPay","saldo","cuenta"]},
 "features":{"ads":true,"rpmServices":true,"eventsServices":true,"urbaniTv":true,
             "nfcButton":{"enabled":false},"qrAccess":{"legacyQr":true},
             "locateTransport":{"renderer":"NATIVE"}, ...}}
```

- Pestañas: **movilidad, miCiudad, urbiPay, saldo, cuenta**.
- **urbiPay** = wallet de pago; **urbicoins** = moneda/puntos interna; **urbaniTv**, anuncios (ads).
- La app resuelve rutas modularmente (`ROUTE.MODULAR`, `RouteRegistry`, módulo DI `authentication`),
  y usa **Mixpanel** para analítica (`[MIXPANEL] Navega a /…`).

## Flujo de arranque observado

1. Lee token: **keychain vacío → fallback ObjectBox vacío → logged-out** (confirma
   `flutter_secure_storage` + ObjectBox como almacén de sesión).
2. `CityConfig` cold start `regionId=MTY`; pide config de ciudad.
3. GPS del dispositivo = coords de **CDMX** → **fuera de los polígonos** de cobertura → región `NA`.
4. Navega a `/auth/no-coverage` → pantalla **"No coverage in your city"** (captura). Botones
   *Exit* / *Register or sign in*. (El dispositivo está en CDMX; Urbani solo cubre MTY.)

No se avanzó a registro/login para **no crear una cuenta real ni disparar el KYC facial** en el
teléfono. Los endpoints autenticados quedan identificados por nombre, no ejercitados.

## Superficie exportada (verificada en vivo)

- `am start .MainActivity` y el deeplink `am start -d urbani://navigate` desde fuera **arrancan el
  flujo normal** (CityConfig → no-coverage), no dan acceso privilegiado sin sesión: **falla
  cerrada**. El enrutamiento del deeplink lo resuelve el Dart; no se observó bypass.
- Los servicios/receivers peligrosos (HCE, FCM, JobService) siguen protegidos por permisos de
  sistema/firma (doc 01).

## Sesión autenticada (el usuario inició sesión real)

Con el usuario **logueado** se ejercitaron los endpoints autenticados que antes se omitían. Mapa de
API capturado en vivo (`evidence/logcat-sesion.txt`; **cuerpos con PII no se extraen**, IDs/tokens
redactados). Convención observada: `/app/g/` lecturas, `/app/p/` operaciones con sesión.

| Subdominio | Rutas (status 200 en vivo) | Función |
|---|---|---|
| `auth-prod.urbani.io` | `/app/g/send/v3`, `/app/g/verify/v4`, `/app/p/register/v2`, `/app/p/pin`, `/app/p/refresh-token`, `/app/user/service/state-region` | OTP (envío/verificación), registro, PIN, refresh de token |
| `app.urbani.io` | `/app/g/config/v2`, `/app/g/hour`, `/app/g/userV2`, `/app/g/userBalances`, `/app/g/userFlags`, **`/app/p/userkeys`** | perfil, **saldos**, flags, **llaves de usuario server-side** |
| `app-static-prod.urbani.io` | `/app/g/messages`, `/app/g/rates/v3`, `/app/g/v2/info_coins_points`, `/app/v2/support/url` | mensajes, **tarifas**, info de urbicoins/puntos |
| `user-info.urbani.io` | `/app/histories/{movements,filters,graphics}`, `/app/history/refund`, `/app/rpm/{products,favorite-service}` | **historial de movimientos/transacciones**, reembolsos, servicios RPM |
| `media-manager-prod.urbani.io` | `/campaigns/userCampaigns` | campañas/publicidad — **vía HTTP en claro** (ver D7) |
| `urb-gps-prod.urbiaccess.io` | `/app/branchs/{metro,transmetro,express}` | **GPS en vivo de transporte MTY** (Metro, Transmetro, Ecovía/Express) — dominio de otro operador (`urbiaccess.io`) |
| Cloudflare R2 | `*.r2.cloudflarestorage.com`, `pub-….r2.dev` | almacenamiento de objetos (assets/imágenes) |

Módulos DI inicializados en la sesión: `authentication`, `rpm`, `transport_access`, `user_profile`.
Rutas de pantalla: `/auth/signup-phone` → `/main`. `urbicoins` (18 menciones) y `wallet` (9)
dominan el flujo (urbiPay). El SDK Baidu se inicializa (3 menciones) pero no se completó KYC facial.

> **`/app/p/userkeys`** (3× 200) revela que **las llaves criptográficas del usuario se obtienen del
> servidor** — es parte del diseño de la tarjeta HCE que el estático no mostraba. El **cuerpo** (la
> llave) no se documenta.

## Hallazgos dinámicos nuevos

| # | Hallazgo | Nota |
|---|---|---|
| D1 | **Logging verboso en release** | endpoints, respuestas JSON completas, coords GPS y **FCM token** en logcat. Mala práctica; en Android moderno el logcat es por-app, pero expone datos a herramientas de diagnóstico y a cualquier lectura con depuración. |
| D2 | **FCM token en claro** en logcat | `FCM TOKEN - fW0a4au0…` (redactado). Permite enviar push dirigido si se captura. |
| D3 | **GMS degradado por el re-firmado** | `SecurityException: Unknown calling package name 'com.google.android.gms'` y Phenotype `DEVELOPER_ERROR`. El cert genérico (doc 09) rompe partes de Play Services / App Check en este artefacto. |
| D4 | **Backend en operador privado** (`urbani.io`) | no gob.mx; transporte municipal sobre infraestructura comercial de un tercero. |
| D5 | **Mixpanel** analytics | SDK de analítica no evidente en el estático; rastrea navegación. |
| D6 | Server-driven UI / feature flags | la UI y features (pagos, NFC, QR, TV) las dicta el servidor por ciudad. |
| D7 | **Tráfico HTTP en claro pese al NSC** | `http://media-manager-prod.urbani.io/campaigns/userCampaigns` devolvió **200 en claro** (2×, sin error cleartext). Flutter usa su propia pila de red y **no respeta el `network_security_config`**; el `cleartext=false` solo cubre la pila Android. La app **sí transmite en claro** (campañas). Corrige doc 04. |
| D8 | **Segundo operador: `urbiaccess.io`** | el GPS en vivo del transporte (metro/transmetro/express) pasa por `urb-gps-prod.urbiaccess.io`, dominio distinto de `urbani.io`. |
| D9 | **Llaves de usuario server-side** (`/app/p/userkeys`) | la app descarga llaves del usuario del servidor (diseño de la tarjeta HCE). |
| D10 | **Storage en Cloudflare R2** | assets/objetos en `*.r2.cloudflarestorage.com` / `pub-….r2.dev`. |

## Corrección de atribución

En el logcat aparece `com.spirtech.android.hce.calypso … NOTENROLLED`, pero el PID/uid
(`mx.gob.cdmx.adip.apps`, u0a331) corresponde a **App CDMX**, otra app instalada en el dispositivo —
**no a Urbani**. La tarjeta de Urbani usa su propio HCE (AIDs `F…`, validadores RD300, doc 05), **no
Calypso**. Se verificó por PID antes de atribuir.

## Qué sigue bloqueado (requiere root)

- **Cripto de la tarjeta de transporte** y protocolo APDU: en Dart cifrado; no se loguea.
- **Cuerpos de los endpoints autenticados** (mobility, payments, urbicoins, messages): exigen sesión
  (y no se creó cuenta). MITM tampoco es viable sin root (Flutter ignora CAs de usuario).
- **Volcado de `libapp.so` descifrada**: exige root + Frida sorteando el `EmulatorAcquisition` de
  LianTian.
