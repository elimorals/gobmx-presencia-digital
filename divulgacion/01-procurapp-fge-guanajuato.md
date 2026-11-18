# Informe de divulgación responsable — ProcurApp (FGE Guanajuato)

**Para:** Fiscalía General del Estado de Guanajuato (FGE GTO) — Dirección General de Análisis y
Tecnologías de la Información / área de ciberseguridad.
**De:** [TUS DATOS: nombre, correo de contacto, PGP opcional]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **CRÍTICA**
**Estado:** divulgación privada; se solicita acuse y plazo de corrección de 90 días antes de publicar.

## Resumen ejecutivo

La app oficial **ProcurApp** desactiva por completo la validación del certificado TLS en los
clientes de red que atienden **producción**, el servicio de **perfil genético (ADN)** y el de
**citas**. En consecuencia, un atacante en la misma red que la víctima (Wi-Fi público, red
comprometida) puede interceptar y **leer o alterar** el tráfico de funciones altamente sensibles:
denuncias (incluida violencia de género), **botón de pánico** y solicitudes/datos de **ADN**. El
transporte es nominalmente HTTPS, pero la desactivación de la validación lo vuelve ineficaz.

## App afectada

- **Nombre:** ProcurApp
- **Package:** `mx.gob.pgjguanajuato.procurapp`
- **Firma:** certificado auténtico de la PGJ/FGE (el binario analizado es el oficial).
- **Pila:** nativa Android (Kotlin/Java, Retrofit + Room).
- **Versión / SHA-256:** ver `hashes.sha256` del análisis (se comparte a solicitud).

## Detalle técnico (clase de vulnerabilidad)

- **CWE-295: Improper Certificate Validation.**
- La app incluye una utilidad `UnsafeOkHttpClient.getUnsafeOkHttpClient()` cuyo `X509TrustManager`
  implementa `checkServerTrusted` **vacío** (acepta cualquier certificado) y un `HostnameVerifier`
  que **siempre devuelve verdadero**, sobre un `SSLContext` permisivo.
- **Verificado por referencias cruzadas (xref) en el bytecode**, ese cliente inseguro es el que
  usan los componentes de red de **producción**, de **genética (ADN)** y de **citas**. El cliente
  Retrofit por defecto sí valida correctamente — es decir, el equipo sabe hacerlo bien, pero las
  rutas más sensibles quedaron sobre el cliente inseguro.
- Backends observados: `https://portal.fgeguanajuato.gob.mx/` y
  `https://webservice.fgeguanajuato.gob.mx/` (el HTTPS no protege por la desactivación de validación).

## Impacto

- **Confidencialidad:** interceptación de denuncias, datos del denunciante, ubicación del botón de
  pánico y datos de perfil genético por un atacante de red (MITM).
- **Integridad:** posible **alteración** de una denuncia o de una alerta de pánico en tránsito.
- **Seguridad física:** por el tipo de dato (víctimas, violencia de género, pánico), el riesgo
  trasciende lo informático.

## Alcance y método (ética)

Análisis **estático** de un APK público obtenido de tienda; **no** se atacó infraestructura, **no**
se interceptó tráfico real, **no** se enviaron denuncias ni se accedió a datos de personas. El
hallazgo es una propiedad del código del binario oficial.

## Remediación recomendada

1. Eliminar `UnsafeOkHttpClient`/`getUnsafeOkHttpClient` del binario de producción.
2. Usar un único `OkHttpClient` con validación estándar para **todas** las rutas (producción,
   genética y citas), como ya hace el cliente por defecto.
3. Añadir **certificate pinning** a los dominios propios para defensa en profundidad.
4. Revisar en el servidor controles de autorización por folio (ver pregunta abierta de IDOR en
   cartas/antecedentes por folio) — a confirmar del lado servidor.

## Divulgación

Se solicita acuse de recibo. Plazo sugerido: **90 días** desde el primer contacto efectivo antes
de publicar detalle técnico. Disponible para coordinar validación y aclaraciones.
