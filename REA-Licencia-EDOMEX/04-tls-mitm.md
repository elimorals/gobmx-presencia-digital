# TLS: validación desactivada en la ruta activa (CRÍTICO)

Hallazgo principal, confirmado **por código** (no por strings) en
`decompiled/ServiceGenerator$Companion.smali` y
`decompiled/ServiceGenerator$Companion$createService$trustAllCerts$1.smali`.

## El TrustManager acepta todo

`ServiceGenerator$Companion$createService$trustAllCerts$1` implementa `X509TrustManager` con los
dos métodos de validación **vacíos**:

```smali
.method public checkClientTrusted([Ljava/security/cert/X509Certificate;Ljava/lang/String;)V
    return-void
.end method
.method public checkServerTrusted([Ljava/security/cert/X509Certificate;Ljava/lang/String;)V
    return-void        # ← no valida la cadena del servidor: acepta CUALQUIER certificado
.end method
```

(`getAcceptedIssuers` devuelve un arreglo vacío.) Un `checkServerTrusted` que no lanza excepción
acepta cualquier certificado, incluido el de un atacante en la ruta.

## El verificador de hostname devuelve `true`

`createService$lambda$1$lambda$0(String, SSLSession)` — el `HostnameVerifier`:

```smali
.method private static final createService$lambda$1$lambda$0(Ljava/lang/String;Ljavax/net/ssl/SSLSession;)Z
    const/4 p0, 0x1
    return p0          # ← true para cualquier hostname
.end method
```

## Cómo se arma el cliente

En `createService(Class)` (smali, líneas ~201-327):

```
new-instance ... trustAllCerts$1           # TrustManager que acepta todo
SSLContext->getInstance("SSL")
SSLContext->init(null, {trustAllCerts}, SecureRandom)
SSLContext->getSocketFactory()
OkHttpClient$Builder->sslSocketFactory(socketFactory, trustAllCerts)   # línea 307
OkHttpClient$Builder->hostnameVerifier({ true })                        # línea 314
OkHttpClient$Builder->build()
Retrofit$Builder->client(okHttpClient)
```

No hay `CertificatePinner` en ninguna clase del APK (grep sobre todo el smali: 0 coincidencias).

## Por qué importa: es la ruta ACTIVA

`ServiceGenerator.createService` (o el de `cameralibrary`) lo invocan:

| Interactor | Flujo |
|---|---|
| `LicenseInteractorImpl` | Consulta/activación de licencia + PII |
| **`PaymentInteractorImpl`** | **Pago con tarjeta** (número, CVV, titular) — doc 07 |
| **`PhoneVerificationInteractorImpl`** | **OTP por SMS** (verificación de teléfono) |
| `CatalogInteractorImpl`, `ConfigInteractorImpl` | Catálogos y configuración remota |
| `LetterScreenInteractorImpl`, `AppointmentInteractorImpl` | Oficios y citas |
| `FaceApiInteractorImpl` (`cameralibrary`) | **Verificación facial** |

Todo eso viaja por un canal donde **un atacante en red (Wi-Fi hostil, proxy, DNS spoof) puede
interceptar y modificar el tráfico sin que la app lo detecte**: leer PII y datos de tarjeta,
capturar el OTP, o alterar respuestas de activación/validación.

## Contraste: la ruta Hilt sí valida

`di.NetworkModule.provideOkHttpClient` (`decompiled/NetworkModule.smali`) arma el cliente solo
con interceptores + timeouts, **sin** `sslSocketFactory`/`TrustManager`/`hostnameVerifier`
custom → usa la validación estándar contra las CAs del sistema. Es la ruta secundaria. La
coexistencia hace el defecto fácil de pasar por alto en revisión.

## Comparación con el acervo

Misma clase de defecto que **Mi Cuenta Infonavit** (`getUnsafeOkHttpClient`, también sin
pinning). Aquí es, si cabe, peor por los datos que transporta (pago + OTP + biometría).

> Severidad: **P1 (crítico)**. Evidencia estática; no se ejecutó un MITM real.
