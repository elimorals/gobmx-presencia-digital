# 07 — Firebase, Google Pay y claves Google

> Las API keys de Google se documentan **redactadas** (prefijo + `…`). Los valores completos están
> en el artefacto del SSD (`strings.xml`, `AndroidManifest.xml`), no se versionan aquí.

## Configuración Firebase (en claro en el APK)

De `res/values/strings.xml` (compilado desde `google-services.json`):

```
project_id            = proyecto-ruta-f8800
google_app_id         = 1:296185281418:android:c4b04a1e20b2ed1b122673
firebase_database_url = https://proyecto-ruta-f8800.firebaseio.com
google_storage_bucket = proyecto-ruta-f8800.appspot.com
google_api_key              = AIzaSyCkqs…  (redactada)
google_crash_reporting_api_key = AIzaSyCkqs…  (misma key, redactada)
default_web_client_id = 296185281418-5pqakak7h4hhd95pmsku8tv3s3bo3vu7.apps.googleusercontent.com
crashlytics mapping_file_id = 7f65a650284349caaac290e1e30003e0
```

El `default_web_client_id` (OAuth) sugiere **inicio de sesión con Google** disponible.

## Servicios Firebase presentes (plugins `io.flutter.plugins.firebase.*`)

| Servicio | Plugin / clase |
|---|---|
| Core | `firebase.core.FlutterFirebaseCorePlugin` |
| **Crashlytics** (+ NDK) | `firebase.crashlytics.*` + `libcrashlytics*.so` (crash nativo) |
| **Cloud Messaging** (push) | `firebase.messaging.*` (servicio FG + receiver + init provider) |
| **Remote Config** | `firebase.firebaseremoteconfig.FirebaseRemoteConfigPlugin` |
| **Storage** | `firebase.storage.FlutterFirebaseStoragePlugin` |
| **App Check** | `firebase.appcheck.*` (SafetyNet + Play Integrity + Debug, por manifest) |
| Installations | `firebase.installations.*` |
| Sessions / Analytics | `firebase.sessions.*`, datatransport (cct) |

**App Check** con Play Integrity + SafetyNet es un control anti-abuso positivo: ata las peticiones a
Firebase a dispositivos/apps legítimos. **Crashlytics NDK** (`libcrashlytics*.so`) reporta también
crashes nativos.

## Google Pay / Wallet

```
manifest: <meta-data com.google.android.gms.wallet.api.enabled = true />
plugin:   io.flutter.plugins.pay_android.*  (GooglePayHandler, PaymentsUtil, PayButtonView)
```

El pago se hace con **Google Pay** vía el plugin Flutter `pay`. **No** se observó Mercado Pago u
otra pasarela (a diferencia de App CDMX). El `PaymentDataRequest` concreto (gateway, merchant,
montos) lo arma el Dart y **no se recuperó**.

## Claves Google en el artefacto

| Clave | Ubicación | Valor (redactado) |
|---|---|---|
| Google **Maps** API key | `AndroidManifest.xml` (`com.google.android.geo.API_KEY`) | `AIzaSyCqw6-…` |
| Firebase / **Crash** API key | `strings.xml` (`google_api_key`) | `AIzaSyCkqs…` |

Ambas están **en claro** en el APK. Es lo estándar en Android (van en `google-services.json` y en el
manifest), pero **solo es seguro si están restringidas** en Google Cloud por huella SHA-1 del
certificado + nombre de paquete (Maps/Android) y por `Application restrictions` (API key). Como el
APK está **re-firmado** (doc 09), una restricción por SHA-1 del certificado original podría no
corresponder al artefacto redistribuido. No se verificó el estado de restricción (requiere la consola
del proyecto). Se reportan **expuestas**; el impacto depende de su configuración. Ver doc 10 (H4).

## Google Maps

Plugin `io.flutter.plugins.googlemaps.*` completo: marcadores, **polilíneas**, **heatmaps**,
**clusters**, tile overlays. Coherente con mapa de **rutas y paradas** de transporte y
geolocalización en vivo (Geolocator, doc 08).
