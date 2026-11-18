# 03 · Hallazgos

## H1 — Endpoint de prueba en producción · MEDIA · CONFIRMADO (CWE-489)

El bundle de producción tiene **activo** `url: 'https://servicios.sspsonora.gob.mx/mstest2'` (las
variantes `/ms` y `/mstest` quedaron comentadas). Una app de **seguridad de la mujer** apuntando a
infraestructura de **prueba** en el build público: riesgo de datos en un entorno no productivo y de
disponibilidad. Mismo patrón que SAT/VaYVen/IMSS/911Móvil BC/Tuzobús (clúster G del mapa).

## H2 — NSC permite cleartext (vestigial) · BAJA · CONFIRMADO (config)

`network_security_config.xml` con `cleartextTrafficPermitted=true` (`includeSubdomains`). El único
endpoint HTTP (`http://apps.sspsonora.gob.mx/...SendLocation`) está **comentado**; el tráfico activo
es HTTPS. Es higiene (retirar el permiso de cleartext), no una fuga activa.

> **Matiz honesto:** NO se confirma el patrón pánico→cleartext de Veracruz Mujer Alerta. Aquí la
> ubicación de la usuaria **sí viaja cifrada** en el código activo. Es un **contraejemplo** útil.

## H3 — Sin trust-all, sin secretos de app · AUSENTE-SANO · CONFIRMADO

- Sin `getUnsafeOkHttpClient` ni desactivación de validación TLS; los `X509TrustManager`/
  `checkServerTrusted` del DEX son de OkHttp/librerías.
- Sin secretos en el bundle JS: el único identificador es el **client-ID OAuth de Google**
  (`…apps.googleusercontent.com`), que es **público por diseño**.

## Proveniencia

Re-firmada por APKPure (`CN=Android, O=Google Inc.`) — la firma no prueba autenticidad; cruzar
contra el canal oficial de SSP Sonora.

## Cruce con el mapa

- Clúster G (test en prod): suma `mstest2`.
- **NO** entra al clúster D (cleartext activo) ni al A (trust-all). Contraejemplo de "pánico→cleartext".
