# Ingeniería inversa de Licencia Digital Jalisco 1.2.5 — índice

Análisis **estático** del APK `mx.com.cosmocolor.movapp.jal` con REA (JADX headless). La app
**nunca se ejecutó**. Es la licencia de conducir digital de la **Secretaría de Transporte de
Jalisco**. **#38 por instalaciones** del inventario gob.mx (≈100 K).

> **White-label Cosmocolor — el MISMO defecto crítico que Licencia EDOMEX.** Comparte el
> package base `mx.com.cosmocolor.movapp` y el backend `licenciamanejo.com` con la Licencia
> Digital del Estado de México (`REA-Licencia-EDOMEX/`). Replica **exactamente** su defecto
> crítico: `ServiceGenerator.Companion.createService` instala un `X509TrustManager` con
> `checkServerTrusted` **vacío** + `hostnameVerifier` que devuelve **`true`** + `SSLContext
> "SSL"` → **validación TLS totalmente desactivada** sobre TODO el tráfico de la API. Y arrastra
> los mismos **residuos cross-estado** (EDOMEX, Chiapas). Es la confirmación de que el defecto
> es del **producto multi-estado del proveedor**, no de un despliegue aislado.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, white-label, defecto crítico |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes |
| 02 | [02-comparativa-edomex.md](02-comparativa-edomex.md) | **TLS trust-all idéntico a EDOMEX (código citado)** |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | Endpoints, cleartext, residuos cross-estado, Regula |
| 04 | [04-almacenamiento-cripto.md](04-almacenamiento-cripto.md) | SQLCipher, licencia/PII |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado),
`decompiled/ServiceGenerator.createService.java` (el defecto, no versionado),
`evidence/firma.txt` (no versionado).

## 5 hallazgos principales

1. **(CRÍTICO) Validación TLS desactivada en toda la API.** `createService` (envoltorio de
   TODO el cliente Retrofit, `BASE_URL = https://api1.licenciamanejo.com/multi/appmov/`)
   usa un `X509TrustManager` con `checkServerTrusted` **vacío** y un `hostnameVerifier` que
   **`return true`** (cuerpos citados en doc 02 y `decompiled/`). Un MITM puede leer o alterar
   todo: emisión/consulta de licencia, auth, verificación facial (Regula), PII. Idéntico a
   Infonavit y Licencia EDOMEX.
2. **(Alto) Confirma defecto de producto multi-estado.** Mismo `mx.com.cosmocolor.movapp.webservice.ServiceGenerator`
   que EDOMEX, con el mismo trust-all → el fallo lo hereda **cada estado** que use el
   white-label de Cosmocolor, no es un error puntual de Jalisco (doc 02).
3. **(Medio) Cleartext HTTP** — `http://impresion.licenciamanejo.com/objetos` (objetos/impresión
   de la licencia) viaja sin TLS (doc 03).
4. **(Bajo-Medio) Residuos cross-estado embebidos** — en el binario de **Jalisco** aparecen
   `api2.licenciamanejo.com/edomex/...`, `prod.cosmokeys.edomex.licenciamanejo.com` (EDOMEX) y
   `chiapas-verifica-infraccion-…run.app` (Chiapas): fuga de infraestructura de otros
   despliegues (doc 03).
5. **Almacenamiento con SQLCipher** (como EDOMEX): DB local cifrada; cadena `sqlcipher_export('plaintext')`
   presente. Mejor que guardar PII en claro, pero el transporte anulado (H1) lo eclipsa (doc 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. **Firma:** re-firmada por APKPure (`CN=Android, O=Google Inc.`) → arquitectura
sí, integridad no. Por severidad (licencia + datos personales + facial, con TLS anulado) y por
ser un **defecto de producto que afecta a varios estados**, procede **divulgación responsable**
a Cosmocolor / las Secretarías de Transporte involucradas.
