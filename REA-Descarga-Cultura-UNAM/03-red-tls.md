# 03 · TLS / Network Security Config

## NSC (`res/xml/network_security_config.xml`)

```
network-security-config
  base-config
    cleartextTrafficPermitted = true
    trust-anchors
      certificates src="system"
```

Dos lecturas, ambas importantes para no sobre-ni sub-reportar:

1. **`cleartextTrafficPermitted=true` en `base-config` (global).** Técnicamente permite HTTP en
   claro a cualquier host → **H1, severidad BAJA (CWE-319 potencial)**. Pero:
   - los backends reales (`descargacultura.unam.mx`, etc.) son **HTTPS**;
   - el único `http://` del binario es `http://localhost/` (dev, inerte).
   Es decir: **cleartext permitido pero no usado**. Riesgo real bajo; la recomendación higiénica es
   poner `cleartextTrafficPermitted=false` (o restringirlo), ya que targetSdk 36 lo bloquea por
   defecto de todos modos.

2. **`trust-anchors src="system"` (solo CAs del sistema).** **No** confía en CAs de usuario → **no**
   tiene la debilidad de "user-CA trust" que facilita MITM con proxies. Correcto.

## Trust-all / pinning

- **NO** hay `getUnsafeOkHttpClient`, `trustAllCerts` ni `X509TrustManager` vacío (conteo 0). Las
  ocurrencias de `SSLSocketFactory`/`HostnameVerifier`/`checkServerTrusted` son internas de OkHttp,
  no un trust-all propio.
- Está presente `okhttp3.CertificatePinner` → la app **puede** usar certificate pinning (no se
  confirmó estáticamente que esté activo en todas las rutas, pero el antipatrón de desactivar TLS
  **no** existe aquí).

**Veredicto TLS: SANO.** No es el caso Infonavit/Cosmocolor (trust-all) ni el de 911-CDMX
(cleartext real de backend).
