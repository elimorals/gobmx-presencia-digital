# 03 · TLS / validación de certificado — H1 (Crítica)

**Hallazgo:** el build de producción deshabilita por completo la validación de certificado y de
hostname TLS para **todo** el tráfico HTTPS de la app, sin certificate pinning que lo compense.

Evidencia: `com.infonavit.data.RetrofitService.getUnsafeOkHttpClient()` y sus clases internas
`RetrofitService$3` (TrustManager) y `RetrofitService$4` (HostnameVerifier); cableado en
`com.infonavit.data.enviroment.EnviromentRetrofit`. Copias smali en `evidence/smali/`.

## 1. TrustManager que acepta cualquier certificado

`evidence/smali/RetrofitService$3.smali` — implementa `X509TrustManager`:

```smali
.method public checkClientTrusted([Ljava/security/cert/X509Certificate;Ljava/lang/String;)V
    return-void                         # no valida nada
.method public checkServerTrusted([Ljava/security/cert/X509Certificate;Ljava/lang/String;)V
    return-void                         # ACEPTA cualquier certificado de servidor
.method public getAcceptedIssuers()[Ljava/security/cert/X509Certificate;
    const/4 p0, 0x0
    new-array p0, p0, [Ljava/security/cert/X509Certificate;
    return-object p0                    # emisores aceptados = []
```

`checkServerTrusted` vacío = se confía en **cualquier** cadena de certificados, incluida una
autofirmada por un atacante.

## 2. HostnameVerifier que acepta cualquier host

`evidence/smali/RetrofitService$4.smali`:

```smali
.method public verify(Ljava/lang/String;Ljavax/net/ssl/SSLSession;)Z
    const/4 p0, 0x1
    return p0                           # verify() -> true SIEMPRE
```

Un certificado emitido para cualquier CN/SAN pasa la verificación.

## 3. Ensamblado en `getUnsafeOkHttpClient()`

`RetrofitService.getUnsafeOkHttpClient()` (smali):

```smali
new-array v0, v0, [Ljavax/net/ssl/TrustManager;       # array con el TrustManager$3
...RetrofitService$3;-><init>()V
const-string v1, "SSL"                                 # SSLContext.getInstance("SSL")  (protocolo obsoleto)
...SSLContext;->init([KeyManager;[TrustManager;SecureRandom)V
...SSLContext;->getSocketFactory()
...OkHttpClient$Builder;->sslSocketFactory(factory, X509TrustManager$3)
...RetrofitService$4;-><init>()V
...OkHttpClient$Builder;->hostnameVerifier(HostnameVerifier$4)
```

El builder además añade un `UserAgentInterceptor`. No hay ninguna llamada a
`CertificatePinner$Builder->add` en código de INFONAVIT (confirmado: `grep 'sha256/'` sobre
`smali*/com/infonavit` devuelve vacío; las clases `okhttp3.CertificatePinner*` presentes son de la
librería, sin uso). **No hay pinning.**

## 4. Es el cliente de producción para todos los servicios

`com.infonavit.data.enviroment.EnviromentRetrofit` construye cada servicio con este cliente:

- `getRetrofitBuild(String)` — **builder genérico** — llama `getUnsafeOkHttpClient()` (línea 429).
- Lo invocan directamente además `getApiServiceCesi` (l. 61), `getApiServiceJax` (l. 241) y otros.
- Las bases son productivas porque `RetrofitService.isProductiveEnviroment()` retorna `true`:

```smali
.method public static isProductiveEnviroment()Z
    const/4 v0, 0x1
    return v0
```

Es decir, en este APK las ramas apuntan a `serviciosweb.infonavit.org.mx`,
`micuenta.infonavit.org.mx`, `map.infonavit.org.mx`, etc., y todas usan el cliente sin validación.

## Impacto

Un atacante con posición de red (Wi-Fi hostil, ARP/DNS spoofing, proxy malicioso) puede
presentar un certificado autofirmado y **interceptar y modificar en claro** todo el tráfico
HTTPS: credenciales de login, token, datos personales del derechohabiente, operaciones de pago y
las propias credenciales de servicio (doc 04). El `cleartextTrafficPermitted="false"` **no
mitiga** esto: aplica a HTTP, no a HTTPS con validación rota.

## Recomendación

Eliminar `getUnsafeOkHttpClient()` y usar el `TrustManager`/validación por defecto de la
plataforma; si se requiere endurecer, añadir `CertificatePinner` real con pines de la CA/hoja de
`*.infonavit.org.mx`. No usar `SSLContext.getInstance("SSL")` (usar TLS por defecto).

## Confirmación dinámica (en vivo)

H1 **se demostró empíricamente** en el Motorola XT2435-3 (Android 16, sin root): un MITM con
mitmproxy **sin instalar la CA de sistema** interceptó el tráfico HTTPS y la app aceptó el
certificado de mitmproxy, exponiendo en claro:

```
POST https://serviciosweb.infonavit.org.mx/omni/mobile-version/api/v1/check → 200 OK
  x-api-key: 2/s5T4F8al…  user-agent: MCI/5.3.48 (XT2435-3; Android 16; Scale/3.00)
  body: {"arg1":"00000000000","arg2":"5.3.48","arg3":"IAPP","arg4":"ANDROID"}
```

En una app con validación TLS normal, ese MITM sin CA de sistema habría fallado el handshake. El
éxito confirma el trust-all. Detalle en [11-analisis-dinamico.md](11-analisis-dinamico.md),
evidencia en `evidence/mitm/captura-legible.txt`.

## Límite

La demostración en vivo es sobre un endpoint no autenticado (check de versión); no se hizo login.
El resto del tráfico usa el mismo cliente trust-all (builder común), por lo que se comportaría
igual, pero no se capturó tráfico autenticado (fuera del alcance acordado).
