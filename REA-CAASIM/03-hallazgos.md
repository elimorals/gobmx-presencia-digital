# 03 · Hallazgos

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | NSC habilita cleartext HTTP para el dominio de la API `servicioscaasim.hidalgo.gob.mx` + `caasimapps.com` (app de pagos de agua) | CWE-319 | **ALTA** | CONFIRMADO (config) / A-CONFIRMAR ruta http concreta |
| H2 | Pasarela **Adquira México** compartida con Tesorería CDMX (cruce de proveedor de pagos CDMX↔Hidalgo) | — | INFO | CONFIRMADO |
| H3 | Sin trust-all TLS (no es el defecto Infonavit/Cosmocolor) | CWE-295 | — | **AUSENTE-SANO** |
| H4 | APK re-firmada por la tienda (`CN=Android, O=Google Inc.`): la firma no prueba autenticidad | — | INFO | CONFIRMADO |
| H5 | Permisos amplios (`CALL_PHONE`, `READ_PHONE_STATE`, `MANAGE_DOCUMENTS`, diccionario de usuario) | CWE-250 | BAJA | CONFIRMADO |

## Detalle

**H1 (ALTA).** `network_security_config.xml` reabre cleartext (targetSdk 36 lo bloquearía) para el
dominio de la API y el del proveedor. El DEX referencia `http://servicioscaasim.hidalgo.gob.mx` y
`https://…`. MITM podría leer/alterar tráfico de cuenta/consumo que use http. Remediación: migrar
todo a HTTPS y retirar el `domain-config` de cleartext; añadir pinning a los dominios propios.

**H2 (INFO).** Pagos vía Adquira México (HTTPS). El mismo procesador aparece en Tesorería CDMX
(`adquiracloud.mx`). No es vulnerabilidad en sí; es inteligencia de proveedor compartido para el
mapa (una falla en Adquira cruzaría CDMX e Hidalgo).

**H3 (AUSENTE-SANO).** Verificado explícitamente: sin `getUnsafeOkHttpClient`/trust-all. Contraste
con Infonavit/Cosmocolor/ProcurApp. Se reporta como sano con evidencia, no como "no buscado".

**H4 (INFO).** Proveniencia: artefacto de APKPure re-firmado. El bytecode es válido para análisis;
la firma no sirve para autenticar. Verificar hash contra el APK oficial de Play.

**H5 (BAJA).** `CALL_PHONE` + `READ_PHONE_STATE` + `MANAGE_DOCUMENTS` + diccionario de usuario
exceden un autoservicio de agua. No crítico; amplía superficie.

## Límites

- `usesCleartextTraffic` real = `true` (leído del manifest); los 7 `<domain>` del NSC resolvieron a
  3 hosts distintos por strings (el resto, variantes http/https del mismo).
- La ruta http vs https por endpoint concreto requiere trazar Retrofit (no ejecutado; estático).
- Superficie de componentes exportados (~12) no auditada a fondo — segundo pase si se requiere.
