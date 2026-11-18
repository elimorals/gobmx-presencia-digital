# Informe de divulgación responsable — Mi Cuenta Infonavit

**Para:** INFONAVIT — Subdirección General de Tecnologías de la Información / área de ciberseguridad.
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **CRÍTICA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

La app **Mi Cuenta Infonavit** desactiva en el build de producción la **validación del certificado
y del hostname TLS**. Un atacante de red (MITM) puede interceptar o alterar el tráfico de
autenticación, pagos y datos personales del trabajador, pese a usar HTTPS.

## App afectada

- **Nombre:** Mi Cuenta Infonavit
- **Package:** `com.infonavit.micuentainfonavit`
- **Pila:** nativa Android (Kotlin/Java). Versión/SHA-256: ver `hashes.sha256` (a solicitud).

## Detalle técnico (clase de vulnerabilidad)

- **CWE-295: Improper Certificate Validation.**
- El código de producción construye el cliente HTTP mediante un patrón tipo
  `getUnsafeOkHttpClient`: `X509TrustManager` que acepta cualquier cadena y `HostnameVerifier`
  que no valida el host. **Sin certificate pinning.**
- Efecto: el canal TLS no autentica al servidor → MITM transparente.

## Impacto

- Interceptación/alteración de credenciales de acceso, operaciones de **pago** y **PII** del
  derechohabiente (CURP, NSS, datos de crédito).

## Alcance y método (ética)

Análisis **estático** de APK público; sin ataque a infraestructura ni a usuarios.

## Remediación recomendada

1. Eliminar el `TrustManager`/`HostnameVerifier` permisivos del build de producción.
2. Usar validación TLS estándar + **certificate pinning** de los dominios del Infonavit.
3. Revisar el pipeline de build para impedir que configuraciones de depuración lleguen a release.

## Divulgación

Acuse solicitado; plazo sugerido 90 días desde el primer contacto efectivo.
