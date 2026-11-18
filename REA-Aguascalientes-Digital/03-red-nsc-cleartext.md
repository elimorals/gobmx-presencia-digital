# Red — NSC con cleartext a producción y logging BODY

## Network Security Config (`evidence/network_security_config.xml`)

```xml
<base-config cleartextTrafficPermitted="false">   <!-- bien: default sin cleartext -->
    <trust-anchors><certificates src="system" /></trust-anchors>
</base-config>
<domain-config cleartextTrafficPermitted="true">   <!-- ⚠️ cleartext HABILITADO para: -->
    <domain includeSubdomains="true">epagos.aguascalientes.gob.mx</domain>        <!-- PAGOS -->
    <domain includeSubdomains="true">egobierno2.aguascalientes.gob.mx</domain>    <!-- API GEA -->
    <domain includeSubdomains="true">eservicios2.aguascalientes.gob.mx</domain>   <!-- servicios/licencia -->
    <domain includeSubdomains="true">expedientedigital.aguascalientes.gob.mx</domain>
    <domain includeSubdomains="true">interfaces.c5ags.gob.mx</domain>            <!-- C5 emergencias -->
    <domain includeSubdomains="true">apidesarrollo.c5ags.gob.mx</domain>         <!-- DEV -->
    <domain includeSubdomains="true">desarrolloweb.aguascalientes.gob.mx</domain> <!-- DEV -->
    <domain includeSubdomains="true">189.254.244.184</domain>                    <!-- IP cruda -->
    <!-- + mdt.mx, ryta.com.mx, streamlock.net, s3.amazonaws.com, openweathermap.org -->
</domain-config>
```

### H1 (Medio-Alto) — Cleartext HTTP a producción, incluido el gateway de pagos
El `domain-config` **permite `http://`** a los backends de producción: la **API de pagos
`epagos`**, las APIs del GEA (`egobierno2`/`eservicios2`), el **expediente digital** y el **C5 de
emergencias** (`interfaces.c5ags`). Si una petición de pago/trámite/emergencia cae a HTTP (o un
atacante la fuerza), viaja **sin TLS** → MITM puede leer o alterar referencias de pago, CURP,
tarjeta de circulación o datos de emergencia. La base `cleartext=false` es correcta, pero la
excepción incluye justo lo sensible.

### H3 (Bajo-Medio) — Dev + IP cruda en producción
`apidesarrollo.c5ags.gob.mx`, `desarrolloweb.aguascalientes.gob.mx` y la IP `189.254.244.184`
quedaron en el NSC publicado → fuga de infraestructura interna y señal de build no saneado.

## H2 (Medio) — Logging HTTP nivel BODY (código citado)

`di.NetworkModule.provideHttpLoggingInterceptor` (en `decompiled/NetworkModule-tls.java`):

```java
HttpLoggingInterceptor i = new HttpLoggingInterceptor(...);
i.level(HttpLoggingInterceptor.Level.BODY);   // vuelca CUERPOS completos a logcat
i.redactHeader(HttpHeaders.AUTHORIZATION);     // redacta solo el header Authorization
```

Este interceptor se inyecta en los 15 clientes. **Redacta el header `Authorization`** (bien,
mitiga el robo de token por logs) **pero NO redacta los cuerpos**: a `logcat` van CURP, tarjeta
de circulación, referencias de pago y datos de trámite. Cualquier app con acceso a logs (o un
dispositivo comprometido / build de depuración) los recoge. Debería ser `Level.NONE` en release.

## Postura TLS (positivo)

Un barrido del DEX **no** halló `getUnsafeOkHttpClient`, `checkServerTrusted` vacío ni
`hostnameVerifier` trust-all. Es decir, **la validación TLS no está desactivada** (al contrario de
ProcurApp, Licencia EDOMEX e Infonavit). `okhttp3.CertificatePinner` está **referenciado** en el
binario, pero no confirmé que haya un pin configurado para cada dominio; además, el cleartext
permitido a esos mismos dominios **socavaría** cualquier pinning. Se reporta como "pinning
referenciado, configuración por-cliente no confirmada", sin afirmar de más.
