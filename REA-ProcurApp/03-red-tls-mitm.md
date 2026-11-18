# Red y TLS — validación desactivada en producción (CRÍTICO)

## El código (decompilado, NO ofuscado)

`mx.gob.pgjguanajuato.procurapp.utils.UnsafeOkHttpClient` (cuerpo en
`decompiled/UnsafeOkHttpClient.java`):

```java
public static OkHttpClient getUnsafeOkHttpClient() {
    TrustManager[] trustManagerArr = { new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}   // ← VACÍO: acepta cualquier cert
        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    }};
    SSLContext sSLContext = SSLContext.getInstance("SSL");                 // protocolo débil/deprecado
    sSLContext.init(null, trustManagerArr, new SecureRandom());
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.sslSocketFactory(sSLContext.getSocketFactory(), (X509TrustManager) trustManagerArr[0]);
    builder.hostnameVerifier((hostname, session) -> true);                 // ← SIEMPRE true: acepta cualquier host
    return builder.build();
}
```

`checkServerTrusted` vacío + `hostnameVerifier` que devuelve `true` = **validación TLS totalmente
desactivada**. Es, carácter por carácter, el mismo antipatrón que **Infonavit** (`getUnsafeOkHttpClient`)
y **Licencia EDOMEX** (`TrustManager` vacío + `hostnameVerifier=true`).

## Quién lo usa — xref en smali (no inferencia)

Desensamblado con apktool, `grep` de invocaciones a
`UnsafeOkHttpClient;->getUnsafeOkHttpClient`:

```
utils/RetrofitClientInstancePROD        ← cliente de PRODUCCIÓN
utils/RetrofitClientInstanceGenetica    ← perfil genético (ADN)
utils/RetrofitClientInstanceWS_CITAS    ← web service de citas
```

- **`RetrofitClientInstance` (el default) NO lo invoca** → `getRetrofitInstance()` arma el
  Retrofit con el OkHttpClient por defecto, que **sí valida**. Es decir, la app **sí sabe** hacer
  TLS correcto en una ruta, pero las rutas de producción, genética y citas usan la insegura.

## Backends

Del DEX: `https://portal.fgeguanajuato.gob.mx/` y `https://webservice.fgeguanajuato.gob.mx/`.
Son **HTTPS**, pero por el cliente inseguro el HTTPS **no protege**: un atacante en la red
(Wi-Fi público, red comprometida, operador) presenta **cualquier certificado** y la app lo
acepta → puede **leer y modificar** el tráfico.

## Impacto

Las rutas afectadas transportan lo más sensible de la app (doc 02, 04):
- **denuncias** (general, casa-habitación, **género**) — identidad del denunciante, relato, ubicación, fotos,
- **pánico** — ubicación en tiempo real y contactos,
- **perfil genético** — solicitudes y citas de muestras de **ADN**,
- **antecedentes**, **persona desaparecida**, **extorsión**.

Un MITM puede: leer la denuncia y la identidad de la víctima, alterar o suprimir un reporte,
falsear una respuesta del servidor, o interceptar la ubicación de un botón de pánico. En una
Fiscalía, el impacto es sobre la **seguridad física de víctimas y denunciantes**.

## Fix

Eliminar `UnsafeOkHttpClient`; usar el OkHttpClient por defecto (validación de CAs del sistema)
en **todos** los clientes; preferiblemente añadir **certificate pinning** a los dominios
`*.fgeguanajuato.gob.mx`; subir de `SSL` a TLS 1.2/1.3.

> Observación estática: no se ejecutó la app ni se interceptó tráfico real; la desactivación de
> validación está probada por el **código** y el **xref**, que es prueba suficiente del defecto.
