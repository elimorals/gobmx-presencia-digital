# 03 · TLS / cleartext — hallazgo crítico

## H1 — Cleartext HTTP en el backend de la alerta (CWE-319) · CONFIRMADO · CRÍTICA

La app transmite la **alerta de violencia de género + ubicación** a un backend **sin TLS**:

```
http://alertagenero.sspver.gob.mx/alerta/
```

El `network_security_config` (`res/8G.xml`) **permite explícitamente** cleartext para ese host:

```xml
<network-security-config>
  <domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="false">alertagenero.sspver.gob.mx</domain>
  </domain-config>
</network-security-config>
```

Es un *allowlisting* deliberado de cleartext para el host exacto del backend. No es un residuo: la
app está configurada para hablar HTTP con su único servidor.

### Impacto

Un atacante de red (WiFi público, red móvil comprometida, MITM) puede **leer o alterar** la alerta:
ubicación en tiempo real de una mujer en riesgo, datos de la víctima, contactos. Puede **suprimir**
la alerta (bloquear el POST) o **falsificarla**. En una app cuya función es salvaguardar la vida ante
violencia de género, el canal inseguro es un riesgo directo a la integridad física.

Misma **clase** (CWE-319) que 911-CDMX (`REA-911-CDMX`): emergencias sobre HTTP.

## H2 — Trust-all TLS · AUSENTE-SANO

Se buscó el antipatrón de Infonavit/Cosmocolor (`getUnsafeOkHttpClient`, `checkServerTrusted` vacío,
`TrustAllCerts`, `HostnameVerifier` trust-all). **No se halló.** Las únicas apariciones de
`X509TrustManager` en el DEX son strings **internas de OkHttp** (`getX509TrustManagerOrNull$okhttp`,
el mensaje "Use the sslSocketFactory overload that accepts a X509TrustManager"). La app **no**
desactiva la validación de certificados.

> Matiz honesto: que el trust-all esté ausente no mejora H1 — el backend es HTTP plano, así que no
> hay TLS que validar. El problema es la ausencia total de TLS, no su mala validación.

## Remediación

1. Migrar `alertagenero.sspver.gob.mx` a **HTTPS** y quitar el `domain-config` de cleartext del NSC.
2. Añadir **certificate pinning** del dominio SSP Veracruz.
3. Forzar actualización obligatoria.
