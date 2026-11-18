# Informe de divulgación responsable — Tesorería CDMX

**Para:** Secretaría de Administración y Finanzas de la CDMX (SAF) — Tesorería / área de
tecnología. (Proveedor del desarrollo: entidad firmante `O=cweb`.)
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **ALTA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

La app oficial **Tesorería CDMX** incluye **credenciales de comercio y secretos de autenticación
embebidos en claro** en el bundle JavaScript distribuido con el APK: credenciales del comercio con
**Banorte** y el `client_secret` de OAuth2 (Laravel Passport). Un atacante puede extraerlos de
cualquier copia del APK. Además, el tráfico hacia el dominio de pagos se permite por cleartext.

## App afectada

- **Nombre:** Tesorería CDMX
- **Package:** `mx.wdd.sefin`
- **Firma:** auténtica (`O=cweb`) — el binario es el oficial.
- **Pila:** híbrida Capacitor/Angular (bundle JS legible), backend Laravel. SHA-256: ver `hashes.sha256`.

## Detalle técnico

- **CWE-798: Use of Hard-coded Credentials / CWE-312.**
  - En el bundle JS (`main.*.js`) hay credenciales del **comercio Banorte** (identificador,
    nombre y **contraseña** del comercio) en texto plano.
  - El **`client_secret`** del cliente OAuth2 de Laravel Passport (login ROPC con `client_id:5`)
    está hardcodeado en el cliente — un secreto que debería vivir solo en el servidor.
- **CWE-319 (cleartext a pagos):** `usesCleartextTraffic="true"` y el `network_security_config`
  permite cleartext al dominio de pagos (`tesoreriacdmx.finanzas.cdmx.gob.mx`) y a una IP interna
  (`10.1.181.71`); además un endpoint de **preproducción** (`prepro.adquiracloud.mx`) embebido.
- Permisos inusuales (`DUMP`, `RECORD_AUDIO`) sin flujo visible.

> Los valores concretos de las credenciales **no** se incluyen en este informe; se entregan al
> afectado por canal seguro si lo solicita.

## Impacto

- **Abuso de las credenciales de comercio** (Banorte) y del `client_secret` → posibilidad de
  suplantar al cliente OAuth / operar contra la pasarela con la identidad del comercio.
- MITM sobre el tráfico de pagos por el cleartext permitido.

## Remediación recomendada

1. **Rotar de inmediato** las credenciales del comercio Banorte y el `client_secret` de Passport.
2. Mover **todo** secreto al servidor; el cliente nunca debe portar `client_secret` ni la
   contraseña del comercio (usar flujos sin secreto en cliente).
3. Eliminar el cleartext y el endpoint de preproducción del build de producción; retirar permisos
   no usados.

## Divulgación

Por haber **secretos activos** embebidos, se recomienda **priorizar la rotación** aun antes del
plazo. Acuse solicitado; plazo sugerido 90 días para el detalle público.
