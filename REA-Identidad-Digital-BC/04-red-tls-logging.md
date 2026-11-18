# Red, TLS y logging

La clave está en `com.gobbc.identidaddigital.remote.client.ServiceGenerator` (cliente Retrofit/
OkHttp). Cuerpo decompilado en `decompiled/ServiceGenerator.java`.

## `createService` (decompilado)

```java
public static <S> S createService(Class<S> cls) {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);   // ⚠️
    mOkHttpClientNormal.addInterceptor(httpLoggingInterceptor);
    mOkHttpClientNormal.addInterceptor(headerInterceptor());
    Retrofit retrofitBuild = builder.client(
        mOkHttpClientNormal.connectTimeout(120, SECONDS).readTimeout(120, SECONDS).build()).build();
    mRetrofitNormal = retrofitBuild;
    return retrofitBuild.create(cls);
}
```

## H1 (Medio) — Logging de cuerpos HTTP en producción

El cliente añade un **`HttpLoggingInterceptor` con nivel `BODY`** a **todas** las peticiones.
`BODY` vuelca a **logcat** los headers y el **cuerpo completo** de cada request y response. En una
app de identidad, eso significa escribir en el log del dispositivo:

- **CURP**, datos de la **licencia** y de la **tarjeta de circulación**,
- el **OTP SMS**,
- los **tokens** de sesión/acceso (que además viajan por el `headerInterceptor`).

Riesgo: cualquier app con acceso a logs (Android <7 con `READ_LOGS`, OEMs laxos, apps de
diagnóstico, o acceso ADB/malware) puede capturar PII y tokens. Es una fuga clásica de dejar el
logging de depuración en el build de producción. **Fix:** `Level.NONE` en release (o condicionar a
`BuildConfig.DEBUG`).

## H2 (Bajo) — Sin certificate pinning (pero sin trust-all)

`createService` **no** instala `certificatePinner` ni un `sslSocketFactory`/`TrustManager`
personalizado → OkHttp usa la **validación TLS por defecto** contra las CAs del sistema. Un barrido
del DEX **no** halló un trust-all (`checkServerTrusted` vacío, `getUnsafeOkHttpClient`,
`hostnameVerifier` permisivo); los símbolos de `X509TrustManager`/`HostnameVerifier` presentes son
de la propia librería OkHttp.

→ Postura **aceptable** (NO desactiva la validación, al revés que Infonavit/Licencia EDOMEX), pero
**sin la capa extra de pinning** que sería deseable para una app de identidad. **Fix:** añadir
`CertificatePinner` a los dominios de BC.

## Endpoints (todos HTTPS)

| URL | Rol |
|---|---|
| `https://idigital.bajacalifornia.gob.mx/` | API de identidad (producción) |
| `https://appsextssl.ebajacalifornia.gob.mx/` | apps externas SSL |
| `https://tramites.ebajacalifornia.gob.mx/.../controlvehicular` | control vehicular |
| `https://www.ventanillabc.bajacalifornia.gob.mx/tarjeta-de-circulacion-digital/` | tarjeta de circulación |
| `https://www.gob.mx/curp` | verificación CURP |
| `https://sandbox.ebajacalifornia.gob.mx/` | **sandbox embebido en producción** ⚠️ |

Más variantes de entorno en campos de `ServiceGenerator` (`BASE_URL_DES` = desarrollo,
`BASE_URL_OLD`, `BASE_URL_PROD_NEW`). Todo **HTTPS**; sin `usesCleartextTraffic`; sin endpoints
`http://` propios.

## H3 (Bajo) — Entornos embebidos
El `sandbox` y las 7 variantes de URL en producción filtran infraestructura interna (nombres de
host de desarrollo/pruebas). **Fix:** dejar solo producción en el build publicado.
