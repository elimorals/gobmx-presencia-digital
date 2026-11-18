# Red — endpoints y certificate pinning

## Endpoints (HTTPS, infraestructura de Banxico)

- `https://sintp.codi.org.mx` y `https://sintp2.codi.org.mx` (dominio oficial `codi.org.mx`).
- Rutas de API: `codi-registro`, `codi-consulta`, `codi-catalogos`, `codi-claves`.
- Todo **HTTPS**; sin `usesCleartextTraffic`; sin endpoints `http://` propios.

## Certificate pinning — implementado correctamente (hallazgo destacado)

`org.banxico.ddispim.codi.httpclient.security.a` es un **`X509TrustManager` personalizado**. Su
`checkServerTrusted` (decompilado completo en `decompiled/pinning-checkServerTrusted.java`) hace,
en este orden:

```java
public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    if (chain.length == 0) throw new IllegalArgumentException("ERROR: X509Certificate is empty");
    // 1) VALIDACIÓN ESTÁNDAR contra las CAs del sistema (NO es trust-all)
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init((KeyStore) null);
    for (TrustManager tm : tmf.getTrustManagers())
        ((X509TrustManager) tm).checkServerTrusted(chain, authType);   // lanza si la cadena no es válida
    // 2) PINNING: SHA-256 del certificado hoja debe estar en la lista fijada f6915a
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(chain[0].getEncoded());
    String fp = a(md.digest());                 // base64 del hash
    boolean ok = false;
    for (String pinned : this.f6915a)
        if (pinned != null && pinned.equalsIgnoreCase(fp)) { ok = true; break; }
    if (!ok) throw new CertificateException("ERROR: No trusted fingerprint was found");
}
```

### Por qué está bien hecho

1. **No es trust-all.** Primero delega en el `TrustManager` del sistema (`TrustManagerFactory` +
   `init(null)`), que valida la **cadena completa** contra las CAs raíz del dispositivo. Si la
   cadena es inválida, **lanza** y no continúa. (Contrasta con Infonavit/Licencia EDOMEX, cuyo
   `checkServerTrusted` está **vacío** = acepta cualquier cert.)
2. **Pinning por fingerprint del hoja.** Además, calcula el **SHA-256 del certificado hoja** y
   exige que coincida con uno de los **fingerprints fijados** (`f6915a`, `String[]` que recibe el
   constructor). Si ninguno coincide → `CertificateException`.
3. Resultado: un MITM necesitaría **a la vez** una cadena válida para una CA del sistema **y** un
   certificado cuyo SHA-256 esté en la lista fijada → en la práctica, **MITM no viable** salvo
   compromiso del propio pin.

### Matiz honesto

- La lista de pines `f6915a` se pasa al constructor; su origen exacto (hardcodeada, remota, o
  derivada) no se trazó hasta el call site — pero el **mecanismo de verificación** está
  confirmado y es correcto.
- `getAcceptedIssuers`/`checkClientTrusted` también están implementados (no son stubs vacíos).

## Conclusión

CoDi Banxico tiene la **postura TLS más fuerte del acervo gob.mx**: validación estándar +
certificate pinning por SHA-256 del hoja. Es exactamente lo que una app de pagos debe hacer, y
lo contrario de las apps críticas del inventario que desactivan la validación.
