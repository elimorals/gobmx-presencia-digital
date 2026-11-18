# Red y TLS

## Backend
- `BuildConfig.BASE_URL = https://jmaschih.gob.mx/jmas` (HTTPS, dominio oficial de la JMAS
  Chihuahua), armado en `NetworkModule.provideJmasRetrofit`:
  ```java
  new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(new ResultCallAdapterFactory()).build();
  ```
- Sin endpoints `http://` propios en el DEX; sin `usesCleartextTraffic` (doc 01).

## Postura TLS — limpia (el hallazgo positivo)
`NetworkModule.provideJmasOkHttpClient` (cuerpo decompilado, en `decompiled/networkmodule.java`):

```java
new OkHttpClient.Builder()
    .connectTimeout(1, MINUTES).readTimeout(2, MINUTES).writeTimeout(2, MINUTES).callTimeout(2, MINUTES)
    .addInterceptor(authInterceptor)      // inyecta el JWT (doc 05)
    .authenticator(tokenAuthenticator)    // refresh del token en 401
    .build();
```

- **No** instala `sslSocketFactory`/`X509TrustManager`, **no** `hostnameVerifier`, **no**
  `CertificatePinner` → usa la **validación TLS por defecto** del sistema (cadena contra CAs del
  sistema, hostname verificado).
- Es decir, **NO** replica el antipatrón trust-all de Infonavit / Licencia EDOMEX / ProcurApp
  (donde `checkServerTrusted` queda vacío). Confirmado con el código, no por ausencia de strings.
- El cliente de `AuthApi` (`provideAuthOkHttpClient`) sigue el mismo patrón limpio.

## Límite
- **Sin certificate pinning** → la confianza recae en las CAs del sistema. Correcto, pero una app
  con auth + pagos ganaría robustez pinneando `jmaschih.gob.mx` (doc 07).
