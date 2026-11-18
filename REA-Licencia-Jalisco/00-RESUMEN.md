# Resumen — Licencia Digital Jalisco 1.2.5 (`mx.com.cosmocolor.movapp.jal`)

## Identidad
- **App:** Licencia Digital Jalisco (etiqueta "Licencia Digital") — Secretaría de Transporte de
  Jalisco. Licencia de conducir digital.
- **Package:** `mx.com.cosmocolor.movapp.jal` · **versión** 1.2.5 (versionCode 20).
- **#38** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
Nativa Android (Kotlin/Java, 6 dex, **22 183 clases**), Retrofit + RxJava2 + Gson + **SQLCipher**
+ Compose. Package de código base **`mx.com.cosmocolor.movapp`** (el white-label; el sufijo
`.jal` es solo el applicationId por estado). Proveedor: **Cosmocolor**.

## El punto central (en una línea)
Es el **mismo producto white-label que Licencia EDOMEX** (`REA-Licencia-EDOMEX/`) y **replica su
defecto crítico de TLS**: `ServiceGenerator.Companion.createService` desactiva por completo la
validación de certificado y de hostname para TODO el tráfico de la API.

## Defecto crítico (doc 02, con código)
```
checkServerTrusted(...) { }                 // vacío → acepta cualquier certificado de servidor
hostnameVerifier verify(...) { return true } // acepta cualquier hostname
SSLContext.getInstance("SSL")                // protocolo débil
builder.sslSocketFactory(socketFactory, trustAllTrustManager)
BASE_URL = https://api1.licenciamanejo.com/multi/appmov/   // createService envuelve TODAS las llamadas
```
→ MITM sobre emisión/consulta de licencia, auth, verificación facial (Regula) y PII.

## Red (doc 03)
| Endpoint | Nota |
|---|---|
| `https://api1.licenciamanejo.com/multi/appmov/` | API principal (TLS anulado por H1) |
| `http://impresion.licenciamanejo.com/objetos` | **CLEARTEXT** |
| `https://faceapi.licenciamanejo.com/` + `*.regulaforensics.com` | verificación facial/documento (Regula) |
| `https://api2.licenciamanejo.com/edomex/...` · `prod.cosmokeys.edomex...` | **residuo EDOMEX** |
| `https://chiapas-verifica-infraccion-…run.app/` | **residuo Chiapas** |

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| TLS | 🔴 **Desactivado** (trust-all + hostnameVerifier=true) en toda la API |
| Cleartext | ⚠️ `impresion.licenciamanejo.com` por HTTP |
| Almacenamiento | ✅ SQLCipher (DB cifrada) |
| Higiene de build | ⚠️ Residuos de EDOMEX y Chiapas embebidos |
| Firma | ⚠️ Re-firmada por APKPure |

**Lectura:** el hallazgo más importante no es solo que Jalisco esté roto — es que el defecto es
**del producto de Cosmocolor** y, por tanto, **todo estado** que use esta licencia white-label
(EDOMEX confirmado, Jalisco confirmado) hereda el mismo MITM.
