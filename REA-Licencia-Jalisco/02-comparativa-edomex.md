# Comparativa con Licencia EDOMEX — el mismo defecto TLS (crítico)

Jalisco y EDOMEX son el **mismo producto white-label de Cosmocolor**:
- Package base común: `mx.com.cosmocolor.movapp` (Jalisco añade `.jal`).
- Backend común: `licenciamanejo.com`.
- Misma clase de red: `mx.com.cosmocolor.movapp.webservice.ServiceGenerator`.

## El defecto (cuerpo decompilado, verificado)

`ServiceGenerator.Companion.createService(Class<S>)` — envoltorio por el que pasan **todas** las
llamadas a la API — construye el `OkHttpClient` así (extracto literal, completo en
`decompiled/ServiceGenerator.createService.java`):

```java
TrustManager[] trustManagerArr = { new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain, String authType) { }   // vacío
    public void checkServerTrusted(X509Certificate[] chain, String authType) { }   // VACÍO
    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
}};
SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(null, trustManagerArr, new SecureRandom());
...
builder.sslSocketFactory(ctx.getSocketFactory(), (X509TrustManager) trustManagerArr[0]);
builder.hostnameVerifier((str, session) -> createService$lambda$1$lambda$0(str, session));
```

Y el hostnameVerifier:

```java
private static final boolean createService$lambda$1$lambda$0(String str, SSLSession s) {
    return true;   // acepta cualquier hostname
}
```

### Qué significa (sin inventar)
- **`checkServerTrusted` vacío** → el cliente acepta **cualquier** certificado de servidor, sin
  validar la cadena ni la CA.
- **`hostnameVerifier` → `return true`** → acepta cualquier nombre de host.
- **`SSLContext "SSL"`** → selecciona una familia de protocolos antigua/débil.
- Se aplica vía `builder.sslSocketFactory(...)` al `OkHttpClient` que **`createService` usa para
  todos los servicios** (BASE_URL `https://api1.licenciamanejo.com/multi/appmov/`).

**Resultado:** la validación TLS está **completamente desactivada**. El `https://` es decorativo;
un atacante en la red (Wi-Fi público, DNS spoofing, proxy) puede **interceptar y modificar** todo
el tráfico sin que la app lo note. Es el mismo antipatrón de Infonavit (`getUnsafeOkHttpClient`)
y de Licencia EDOMEX.

## Diferencia con EDOMEX
Ninguna en lo esencial: EDOMEX tenía el `checkServerTrusted` vacío + `hostnameVerifier=true` en
el mismo `ServiceGenerator.createService`; Jalisco lo replica **idéntico**. La única variación es
el `BASE_URL` y algunos residuos de estado (doc 03). → El defecto es **del producto Cosmocolor**,
heredado por cada despliegue estatal.

## Implicación de alcance
Como es un defecto de producto multi-estado, **cualquier otra "Licencia Digital" basada en
`mx.com.cosmocolor.movapp`** (otros estados) probablemente comparte el mismo MITM. No se
verificaron otras aquí (fuera de alcance), pero el patrón lo sugiere con fuerza — se reporta como
hipótesis fundada, no como hecho sobre apps no analizadas.
