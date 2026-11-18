# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.

## H1 (CRÍTICO) — Validación TLS desactivada en toda la API
`ServiceGenerator.Companion.createService` (envoltorio de TODO el cliente Retrofit,
`https://api1.licenciamanejo.com/multi/appmov/`) instala un `X509TrustManager` con
`checkServerTrusted` **vacío**, un `hostnameVerifier` que **`return true`** y `SSLContext "SSL"`
(doc 02, cuerpos citados). → **MITM** puede leer/alterar emisión y consulta de licencia, auth,
verificación facial (Regula) y PII del titular. **Fix:** quitar el trust-all; validar contra CAs
del sistema (idealmente pinning).

## H2 (Alto) — Defecto de producto multi-estado
Es el **mismo** `ServiceGenerator` del white-label Cosmocolor que Licencia EDOMEX, con el
**mismo** trust-all (doc 02). El fallo no es de Jalisco: lo hereda **cada estado** que use esta
licencia digital. Riesgo sistémico. **Fix:** corregir en el producto base de Cosmocolor y
redistribuir a todos los estados.

## H3 (Medio) — Cleartext HTTP
`http://impresion.licenciamanejo.com/objetos` viaja sin TLS (doc 03). **Fix:** forzar HTTPS.

## H4 (Bajo-Medio) — Residuos cross-estado + logging BODY
- En el build de Jalisco quedan endpoints de **EDOMEX** (`api2…/edomex`, `prod.cosmokeys.edomex…`)
  y **Chiapas** (`…run.app`): fuga de infraestructura de otros despliegues (doc 03).
- El `OkHttpClient` tiene `HttpLoggingInterceptor` a nivel **BODY** (doc 02): el cuerpo de las
  peticiones/respuestas (con PII) se registra en logcat.

## Positivos
- **SQLCipher** (DB local cifrada, doc 04) — aunque eclipsado por H1.
- Código decompilable (facilita auditoría y corrección).

## Límites (sin inventar)
- Derivación de la llave de SQLCipher no confirmada (doc 04).
- No se enumeraron todas las rutas de la API bajo `/multi/appmov/` (no se tocó el endpoint).
- La hipótesis de que **otros** estados Cosmocolor comparten H1 no se verificó aquí (solo EDOMEX y
  Jalisco están confirmados).
- Firma re-firmada por APKPure → conclusiones de arquitectura, no de integridad del artefacto.

## Recomendación
Por la severidad (licencia + PII + facial con TLS anulado) y el alcance (multi-estado), procede
**divulgación responsable** a Cosmocolor y a las Secretarías de Transporte (Jalisco, EDOMEX, y
cualquier otro estado con `mx.com.cosmocolor.movapp`).
