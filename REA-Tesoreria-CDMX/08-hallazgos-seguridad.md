# Hallazgos de seguridad (priorizados)

Observaciones **estáticas** de una app pública que se posee. No se ejecutó la app
ni se llamó a endpoints. Cada hallazgo cita su evidencia. Severidad = criterio del
analista por impacto potencial, no un CVSS formal.

## 1. Credenciales de comercio Banorte embebidas en el cliente — ALTA

`main.*.js` → `getOriginalChain()` construye la cadena de pago con `merchantId`,
`name` y `password` del **comercio Banorte hardcodeados** en el bundle JS
distribuido. Cualquiera que descomprima el APK los lee (valores en `evidence/`).
- **Impacto:** exposición de credenciales de comercio de una pasarela de pago de
  gobierno. El riesgo real depende de qué permita hacer ese trío contra Banorte/el
  orquestador; no se probó (estático).
- **Recomendación:** mover el armado/firma de la operación al servidor; que el
  cliente nunca vea credenciales de comercio.

## 2. `client_secret` de Laravel Passport hardcodeado — ALTA

Login OAuth2 **ROPC** con `client_id:5` y un `client_secret` de 40 chars fijo en
`main.*.js` (doc 04).
- **Impacto:** un secreto de cliente en un binario público no es secreto; permite
  suplantar al cliente oficial contra el endpoint de token.
- **Recomendación:** usar un cliente **público** de Passport (sin secreto) o PKCE;
  rotar el secreto actual.

## 3. Tráfico en claro habilitado y a dominio de pagos — MEDIA/ALTA

`usesCleartextTraffic="true"` y `network_security_config` permite **cleartext** a
`tesoreriacdmx.finanzas.cdmx.gob.mx` (pagos), `sac.finanzas.cdmx.gob.mx` y a la
**IP interna `10.1.181.71`** (doc 01).
- **Impacto:** habilita downgrade a HTTP en hosts que manejan datos de pago. Las
  URLs del bundle usan `https://`, pero la política lo permite.
- **Recomendación:** quitar `usesCleartextTraffic` y el `cleartextTrafficPermitted`
  de los dominios de pagos; eliminar la IP interna de la config de producción.

## 4. Endpoint de pre-producción en binario de producción — MEDIA

`https://prepro.adquiracloud.mx/clb/endpoint/flapMexico` embebido (doc 03).
- **Impacto:** fuga de infraestructura de pre-pro; si el entorno de pre-pro es más
  débil, es superficie adicional. No se verificó si está activo.
- **Recomendación:** no embeber entornos de prueba en builds de producción.

## 5. IP interna expuesta en el NSC — BAJA/INFO

`10.1.181.71` (RFC 1918) en la lista blanca de cleartext (doc 01). **No hay
evidencia** de que el bundle la invoque; es una fuga de topología interna, no un
canal confirmado.

## 6. Permisos inusuales sin justificación visible — BAJA/INFO

`DUMP` (normalmente `signature|privileged`) y `RECORD_AUDIO` declarados sin flujo
observable que los use (doc 01). **No se determinó la causa** con el análisis
estático; se reportan como superficie declarada a revisar.

## Nota de alcance

El JS legible de Capacitor hace que estos hallazgos sean de lectura directa del
binario. No se evaluó el comportamiento en ejecución, la validación del lado
servidor ni la explotabilidad real de las credenciales. Divulgación responsable: los
valores secretos quedan en `evidence/` (no versionado), no en esta documentación.
