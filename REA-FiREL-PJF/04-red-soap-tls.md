# Red — SOAP, proveedores y postura TLS

## Proveedores y endpoints (strings del DEX + resources)

| Proveedor | Rol | Endpoints |
|---|---|---|
| **IQSEC** (integrador/backend) | servicios propios por **SOAP** | `https://iqsec-masivos.com.mx/svf/wssvf/`, `https://investigacion.iqsec.com.mx` |
| **Identy.io** | biometría huella/rostro + **license manager** | `https://licensemgr.identy.io/nverify/v2`, `https://bancolicensemgr.identy.io/nverify/v1` |
| **ShuftiPro** | KYC / verificación de identidad (tercero) | `https://api.shuftipro.com/`, `.../get/access/token` |
| (SOAP) | namespace | **`tempuri.org`** (por defecto de .NET, sin configurar) |

Observaciones:
- El backend propio es de **IQSEC**, no de `cjf.gob.mx`: la infraestructura de la firma judicial
  vive en dominios del proveedor (`iqsec-masivos.com.mx`). `svf/wssvf` ≈ *servicio de validación
  de firma*; es un WSDL SOAP.
- **`tempuri.org`** es el namespace por defecto de un web service .NET no personalizado → señal
  de WSDL/servicio no saneado (menor).
- Datos biométricos de ciudadanos pasan por **tres** partes (IQSEC, Identy.io, ShuftiPro); una
  (ShuftiPro) es un proveedor **extranjero** de KYC.
- No se recuperaron en claro las claves de cliente de ShuftiPro/Identy en el DEX (no se halló un
  `client_secret` embebido en el barrido) — probablemente se obtienen por token
  (`/get/access/token`) o viven en el servidor.

## Postura TLS — CONTRADICTORIA (bandera alta, a confirmar)

El DEX contiene **las dos cosas a la vez**:

```
# Pinning presente:
okhttp3.CertificatePinner  /  CertificatePinner.kt  (clases okhttp3)

# Cliente "inseguro" presente (patrón de desactivación de validación, como Infonavit):
getUnsafeOkHttpClient            (classes.dex)
X509TrustManager                 (classes.dex, classes3.dex)
sslSocketFactory                 (classes.dex, classes3.dex)
setHostnameVerifier              (classes3.dex)
trustManager                     (classes3.dex)
```

- La presencia de `getUnsafeOkHttpClient` en una app que transmite **INE + biometría + firma** es
  una **señal seria**: ese patrón, cuando se usa, construye un `OkHttpClient` con un
  `X509TrustManager` que acepta cualquier certificado y un `hostnameVerifier` permisivo →
  **desactiva la validación TLS** (lo mismo que Infonavit / Licencia EDOMEX).
- **PERO:** no se pudo **atar** `getUnsafeOkHttpClient` a una clase propia `com.fielnet…` ni
  confirmar **qué ruta de red lo invoca** (el contexto cercano en el DEX no mostró una clase
  propia; puede pertenecer a una **librería** incluida, y el código está minificado con R8). La
  red propia usa **Volley**, que podría o no pasar por ese cliente.
- **Veredicto honesto:** coexisten pinning y un cliente inseguro; **no se confirma** que la
  biometría viaje sin validación. Es el **hallazgo pendiente más importante** y requiere
  análisis dinámico (interceptar el tráfico) o decompilar el `ApiServiceOperator`/`OnboardingService`
  método a método con la obfuscación resuelta. Se reporta como **bandera a verificar, NO como
  MITM confirmado** — sin inventar el resultado.

## Recomendación (para el CJF/IQSEC)

Confirmar del lado del código/servidor que **todas** las llamadas de biometría/INE usan el
cliente **pinneado** y que `getUnsafeOkHttpClient` no está en ninguna ruta de producción
(idealmente, eliminarlo del binario).
