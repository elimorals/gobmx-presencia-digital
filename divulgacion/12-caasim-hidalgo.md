# Informe de divulgación responsable — CAASIM (Agua Hidalgo)

**Para:** Comisión de Agua y Alcantarillado de Sistemas Intermunicipales (CAASIM), Hidalgo — área de
tecnología. Copia sugerida: Gobierno Digital de Hidalgo.
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **ALTA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

La app oficial **CAASIM** reabre **tráfico HTTP en claro (sin TLS)** para su API de **pagos de agua**,
pese a tener `targetSdk 36` (que bloquearía cleartext por defecto). Un atacante de red puede leer o
alterar el tráfico de consulta/pago del usuario.

## App afectada

- **Nombre:** CAASIM
- **Package:** `mx.gob.hidalgo.caasim.app`
- **Pila:** nativa Android (Kotlin, OkHttp/Retrofit). SHA-256: ver `hashes.sha256`.

## Detalle técnico

- **CWE-319: Cleartext Transmission of Sensitive Information (config).**
  - El `network_security_config` **reabre cleartext** para el dominio de la API
    `servicioscaasim.hidalgo.gob.mx` (y `caasimapps.com`). El DEX referencia el host tanto en `http`
    como en `https`.
  - **A-confirmar:** qué llamada concreta usa `http` (requiere trazar las rutas Retrofit; no se
    afirma que todo el tráfico sea en claro, solo que el canal está permitido).
- **(Positivo) AUSENTE-SANO:** **sin trust-all** (no es el defecto Infonavit/Cosmocolor); solo
  maquinaria OkHttp estándar. Verificado con evidencia.
- **Observación de cruce:** la pasarela de pago es **Adquira México** (`adquiramexico.com.mx`,
  HTTPS) — **el mismo procesador que usa Tesorería CDMX** (`adquiracloud.mx`).
- Permisos amplios para un autoservicio de agua (`CALL_PHONE`, `READ_PHONE_STATE`, `MANAGE_DOCUMENTS`).

## Impacto

- MITM sobre el tráfico de **consulta/pago de agua** por el canal cleartext permitido.

## Alcance y método (ética)

Análisis **estático** del APK público; sin ataque a infraestructura ni uso de datos de usuarios.

## Remediación recomendada

1. Retirar el cleartext del `network_security_config`; forzar HTTPS en toda la API.
2. Confirmar/migrar cualquier ruta que use `http` a `https`.
3. Añadir certificate pinning; revisar los permisos no usados.

## Divulgación

Acuse solicitado; plazo sugerido 90 días.
