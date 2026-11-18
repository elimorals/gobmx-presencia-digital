# SDKs y dependencias

Fuente: archivos `*.properties` del APK (versiones de librerías), strings de `.dex`, y el
bundle web (`package.json` implícito en los imports).

## Google / Firebase (nativo)

De los `*.properties` embebidos:

- **Firebase**: `firebase-analytics`, `firebase-auth` (+ `firebase-auth-interop`),
  `firebase-iid-interop`, `firebase-measurement-connector`, `firebase-messaging`
  (vía `messaging_event.proto`), `firebase-encoders`. → Analytics, **Auth**, push (FCM),
  medición.
- **Play Services**: `play-services-maps` (mapas), `play-services-location` (GPS/rastreo),
  `play-services-auth` + `play-services-auth-api-phone` (**login Google + autollenado de OTP
  por SMS**), `play-services-ads-identifier`, `play-services-measurement*`,
  `play-services-cloud-messaging`, `play-services-base/basement`.
- **Play Core**: `app-update` (`app-update.properties`, in-app updates) e **`integrity`**
  (`integrity.properties` → Play Integrity API, atestación del dispositivo/app).
- **recaptcha** (`recaptcha.properties`, `recaptcha.net/api3`).
- **places** (`places.properties` + `getPlacesKey` del `.so`, doc 05).

## Red / tiempo real

- **Microsoft SignalR** (`com.microsoft.signalr.*`) → hub `bus-hub` en tiempo real (doc 03).
- **OkHttp3** (carpeta `okhttp3/` en el APK) → cliente HTTP nativo.
- **RxJava** (referencias `ReactiveX/RxJava` en strings).

## Almacenamiento local

- **greenDAO** con **cifrado de base de datos** (`greenrobot.org/greendao/documentation/
  database-encryption/` en strings) → ORM sobre SQLite, probablemente con SQLCipher. La llave
  podría derivarse de `NativeKeysHelper.sharedPrefKey()` (doc 05); no verificado el enlace
  exacto.
- **Google Tink** (`type.googleapis.com/google.crypto.tink.*`: AesGcm, AesCtrHmacAead, Hmac,
  HpkePublic/Private, KmsEnvelope) → criptografía para almacenamiento/datos sensibles.

## Terceros

- **Facebook SDK** (`developers.facebook.com/docs/android`, Graph API, app-events) → login
  social y/o eventos de app.
- **Google Ads / Privacy Sandbox** (`googleadservices.com/pagead/conversion`,
  `pagead2.googlesyndication.com`, permisos `ACCESS_ADSERVICES_*`) → medición de conversiones.
- **MercadoPago, PayPal, Braintree** (pagos, doc 04).

## Capa web

React (Create React App; `main.*.js` + chunk + `asset-manifest.json`), con los SDKs JS de
MercadoPago/PayPal cargados desde CDN. Bundle pequeño (732 KB) con source maps incluidos.

> No se enumeró cada versión exacta de los `*.properties` para no alargar; están en el APK si
> se requiere el detalle de versiones.
