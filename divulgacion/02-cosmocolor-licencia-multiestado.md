# Informe de divulgación responsable — Licencia Digital (producto Cosmocolor, multi-estado)

**Para (primario):** **Cosmocolor** (desarrollador/proveedor del producto de licencia de conducir
digital `mx.com.cosmocolor.movapp`).
**Para (copia / afectados):** Secretaría de Movilidad del Estado de México (SEMOV EDOMEX) y
Secretaría de Transporte de Jalisco; y cualquier otra entidad que distribuya una app con el base
package `mx.com.cosmocolor.movapp`.
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **CRÍTICA** — y **sistémica** (defecto de producto replicado en varios estados).
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

Las apps de **licencia de conducir digital** construidas sobre el producto de **Cosmocolor**
(`mx.com.cosmocolor.movapp.*`) **desactivan por completo la validación del certificado TLS** en el
generador de servicios que envuelve **toda la API**. Confirmado de forma **idéntica** en dos
despliegues independientes — **Licencia Digital EDOMEX** (`mx.com.cosmocolor.movapp`) y **Licencia
Digital Jalisco** (`mx.com.cosmocolor.movapp.jal`) — lo que indica que el defecto está en el
**código base del proveedor**, no en la configuración de un estado. Cualquier entidad que use este
white-label está afectada. Un atacante de red (MITM) puede interceptar/alterar activación de
licencia, **pago con tarjeta/CVV**, **OTP SMS** y **verificación facial**.

## Apps afectadas (confirmadas)

- **Licencia Digital EDOMEX** — `mx.com.cosmocolor.movapp` (SEMOV Estado de México).
- **Licencia Digital Jalisco** — `mx.com.cosmocolor.movapp.jal` (Transporte Jalisco).
- (Cualquier otro estado con el mismo base package — a revisar por el proveedor.)
- **Pila:** nativa Kotlin/Java (Retrofit + SQLCipher). Backend del proveedor:
  `https://api1.licenciamanejo.com/...`.

## Detalle técnico (clase de vulnerabilidad)

- **CWE-295: Improper Certificate Validation.**
- En ambos binarios, `ServiceGenerator.createService` (el envoltorio de **toda** la API) instala:
  - un `X509TrustManager` con `checkServerTrusted` **vacío**,
  - un `SSLContext("SSL")`,
  - un `sslSocketFactory` con ese trust-all,
  - un `HostnameVerifier` cuyo cuerpo es **`return true`**.
- Resultado: **validación TLS 100% desactivada** para todas las peticiones de la app. El código es
  idéntico entre EDOMEX y Jalisco (mismo `ServiceGenerator` del producto).
- Observaciones adicionales: endpoint de impresión por **HTTP en claro**
  (`http://impresion.licenciamanejo.com/...`); residuos cross-estado embebidos (EDOMEX en el build
  de Jalisco y referencias de otro estado), señal del white-label multi-estado; `HttpLogging` a
  nivel BODY.

## Impacto

- **Confidencialidad/Integridad:** MITM puede leer o alterar la activación de la licencia, los
  **datos de tarjeta/CVV** del pago, el **OTP SMS** y el flujo de **verificación facial** (PII de
  identidad del conductor).
- **Alcance sistémico:** afecta a todos los estados que distribuyen el producto.

## Alcance y método (ética)

Análisis **estático** de APKs públicos de tienda; **no** se atacó infraestructura ni se interceptó
tráfico real. Hallazgo = propiedad del código de los binarios.

## Remediación recomendada

1. **En el producto de Cosmocolor:** eliminar el trust-all de `ServiceGenerator.createService`;
   usar validación TLS estándar; **publicar actualización a todos los estados** que lo usan.
2. Migrar el endpoint de impresión a HTTPS; retirar el `HttpLogging` BODY de producción.
3. Añadir certificate pinning a los dominios del proveedor.
4. Las Secretarías (EDOMEX, Jalisco, …) deberían exigir al proveedor la corrección y forzar
   actualización obligatoria.

## Divulgación

Al ser un defecto de **producto**, la ruta más eficaz es el proveedor (Cosmocolor), con copia a las
Secretarías afectadas. Plazo sugerido 90 días desde el primer contacto efectivo.
