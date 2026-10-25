# 08 — SDKs de terceros

Inventario de librerías embebidas, por nº de referencias en `classes*.dex` y por presencia
de paquete / librería nativa.

## Mapa de SDKs

| SDK | Evidencia | Uso |
|---|---|---|
| **Firebase** | 1,556 refs `Lcom/google/firebase` | FCM (push), Auth, Analytics, Crashlytics, Installations, (Remote Config probable) |
| **Retrofit2** | `Lretrofit2/` | Cliente HTTP declarativo (API v4) |
| **OkHttp3** | `Lokhttp3/` | Transporte HTTP/2, pinning |
| **Gson** | `Lcom/google/gson` | Serialización JSON de DTOs |
| **Google Maps SDK** | `Lcom/google/android/gms/maps` | Mapas (Mi Taxi, movilidad, ubicaciones) |
| **ML Kit** | `Lcom/google/mlkit` + `libbarhopper_v3.so` | Lectura de QR/códigos de barras |
| **CameraX** | `libimage_processing_util_jni.so`, `libsurface_util_jni.so` | Cámara para QR/fotos de reporte |
| **Spirtech Calypso** | `Lcom/spirtech/android`, `Lcom/spirtech/ybo` + nativo | Tarjeta de transporte HCE (doc 05) |
| **Mercado Pago** | endpoints `api.mercadopago.com/...` | Pagos (doc 06) |
| **Glide** | `Lcom/bumptech/glide` (51 refs) | Carga/caché de imágenes |
| **Lottie** | `Lcom/airbnb/lottie` | Animaciones vectoriales |
| **Google Tink** | `com/google/crypto/tink/...` (shaded) | Cripto (probable EncryptedSharedPreferences / security-crypto) |
| **Jetpack DataStore** | `libdatastore_shared_counter.so` | Almacenamiento clave-valor moderno |
| **WorkManager** | `androidx.work.impl.*` (servicios/receivers) | Tareas en segundo plano (sync Calypso, etc.) |
| **PairIP** | `com.pairip.application.*` + `libpairipcore.so` | Anti-tamper (doc 07) |

## Configuración Firebase embebida

Extraída de `resources.arsc` (credenciales de **cliente**, presentes en todo APK público;
la API key se muestra enmascarada):

```
Proyecto (storage bucket):  app-cdmx---prod.appspot.com
Firebase App ID:            1:911152330019:android:de35e655e0174fbff9c896
GCM / Sender ID:            911152330019
API key (Android/Maps):     AIzaSy…2Vwk   (clave de cliente, restringible por huella)
```

> Son **claves de cliente**, restringibles por huella de firma de la app y por API
> habilitada en GCP — no son secretos de servidor. Mismo criterio que en CFE Contigo: no
> constituyen por sí mismas una filtración, pero **conviene verificar en la consola de GCP**
> que la API key esté restringida a esta app (huella SHA-1 + paquete) y solo a las APIs que
> usa (Maps SDK, Firebase). Una key Android sin restricción permitiría abuso de cuota de Maps.
> El valor completo está en `resources.arsc` (SSD), no se versiona.

## Doble ecosistema (vs. CFE)

A diferencia de CFE Contigo (que duplicaba Google + **Huawei HMS**), App CDMX es
**solo Google/Firebase**: no se hallaron paquetes HMS/AGConnect. Es una app distribuida en
Google Play (y side-load), sin canal AppGallery.

## Privacidad — telemetría junto a servicios sensibles

Firebase Analytics + Crashlytics + los permisos `ACCESS_ADSERVICES_ATTRIBUTION` /
`ACCESS_ADSERVICES_AD_ID` conviven en el mismo proceso que botón de pánico, denuncia
anónima y canal anticorrupción. Para un modelo de amenazas de privacidad del denunciante,
conviene revisar qué eventos de esos módulos llegan a Analytics (idealmente, ninguno con
PII o contenido de la denuncia).
