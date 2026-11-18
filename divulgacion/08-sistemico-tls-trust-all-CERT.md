# Informe de divulgación responsable (sistémico) — Antipatrón TLS trust-all en apps gob.mx

**Para:** CERT-MX (Centro Nacional de Respuesta a Incidentes Cibernéticos, Guardia Nacional) — con
copia a UNAM-CERT. Es un patrón de **ecosistema**: se dirige a un CERT nacional, no a una sola
dependencia, para coordinar la corrección con todas las afectadas.
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **CRÍTICA** (sistémica)
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

Un mismo antipatrón de seguridad —**desactivar por completo la validación del certificado TLS** en
el cliente HTTP— aparece de forma **independiente y repetida** en múltiples apps oficiales de
gobierno mexicano, publicadas por dependencias distintas y por equipos de desarrollo sin relación
entre sí. No es una librería compartida con un bug: es el **mismo snippet copiado** (típicamente de
foros como StackOverflow) reimplementado una y otra vez. El efecto es que, aunque las apps usan
HTTPS, **el canal no autentica al servidor**: un atacante de red (MITM) puede leer o alterar el
tráfico —incluyendo autenticación, pagos, denuncias y datos de ADN— sin que la app lo note.

Se divulga como **patrón sistémico** porque corregir app por app deja el resto expuesto; el valor
está en que un CERT nacional emita una guía y coordine la remediación con todas las dependencias.

## La clase de vulnerabilidad

- **CWE-295: Improper Certificate Validation.**
- Firma del antipatrón: un `javax.net.ssl.X509TrustManager` cuyo `checkServerTrusted` tiene el
  cuerpo **vacío** (acepta cualquier cadena de certificados) + un `HostnameVerifier` que **devuelve
  `true`** siempre, cableados en un `OkHttpClient`/`Retrofit`. Comúnmente el método se llama
  literalmente `getUnsafeOkHttpClient()` o vive en una clase `ServiceGenerator` / `UnsafeOkHttpClient`.
- Resultado: TLS sin autenticación de servidor → MITM transparente (incluso contra pinning ausente).

## Casos confirmados (análisis estático de APKs públicos)

| App | Dependencia | Clase portadora | Alcance | Evidencia |
|-----|-------------|-----------------|---------|-----------|
| Mi Cuenta Infonavit | INFONAVIT | `com.infonavit.data.RetrofitService.getUnsafeOkHttpClient()` | **todos** los servicios | CONFIRMADO |
| Licencia Digital EDOMEX | SEMOV EdoMéx (prod. Cosmocolor) | `mx.com.cosmocolor.movapp…ServiceGenerator.createService` | toda la API | CONFIRMADO |
| Licencia Digital Jalisco | Transporte Jalisco (prod. Cosmocolor) | `mx.com.cosmocolor.movapp.jal…ServiceGenerator` | toda la API | CONFIRMADO |
| ProcurApp | FGE Guanajuato | `utils.UnsafeOkHttpClient.getUnsafeOkHttpClient()` | PROD / Genética(ADN) / Citas | CONFIRMADO |
| FiREL | CJF (prod. FielNet/IQSEC) | `getUnsafeOkHttpClient` coexiste con `CertificatePinner` | no atada a ruta prod | **A CONFIRMAR** |

**Orígenes independientes:** 3 confirmados (Infonavit · producto Cosmocolor · ProcurApp). Cosmocolor
es un producto único replicado en EDOMEX y Jalisco (con integración de verificación de infracciones
para Chiapas detectada por código reutilizado; una app de licencia de Chiapas **no** se confirmó).
FiREL se
reporta como bandera a confirmar: el método existe pero no se pudo atar por análisis estático a una
ruta de producción (ofuscación R8).

## Por qué importa el patrón (no solo los casos)

El mismo error, reimplementado por equipos distintos, indica que **la práctica es endémica** en el
desarrollo de apps de gobierno: es muy probable que exista en apps no incluidas en esta muestra. Una
guía del CERT (y un control de revisión en los procesos de contratación/publicación) ataca la causa
raíz; corregir solo las 4 confirmadas no.

## Impacto

- **Infonavit:** MITM sobre credenciales, pagos y PII del derechohabiente (CURP, NSS, crédito).
- **Cosmocolor (EDOMEX/Jalisco/+Chiapas):** MITM sobre activación de licencia, **tarjeta/CVV**,
  **OTP SMS** y verificación facial.
- **ProcurApp (FGE):** MITM sobre **denuncias, pánico y resultados de ADN** — el más sensible.

## Alcance y método (ética)

Análisis **estático** de APKs públicos de tienda. No se interceptó tráfico real, no se atacó
infraestructura y no se usó dato de ningún usuario. El hallazgo es una propiedad del código de los
binarios publicados.

## Remediación recomendada

1. **A las dependencias:** eliminar el `TrustManager`/`HostnameVerifier` permisivos del build de
   producción; usar validación TLS estándar + **certificate pinning**; forzar actualización.
2. **Al proveedor Cosmocolor:** corregir el producto base y publicar a todos los estados que lo usan.
3. **Al CERT (causa raíz):** emitir guía de "no trust-all en producción" para desarrollo gob.mx y
   añadir una verificación estática (p. ej. detección de `checkServerTrusted` vacío) en el pipeline
   de publicación/contratación.

## Divulgación

Ruta: CERT-MX como coordinador, con copia a UNAM-CERT y a cada dependencia. Plazo sugerido 90 días.
Los informes individuales por app están disponibles (`divulgacion/01`, `02`, `03`, `06`). Se ofrece
coordinar la confirmación de FiREL y del tercer estado Cosmocolor (Chiapas).
