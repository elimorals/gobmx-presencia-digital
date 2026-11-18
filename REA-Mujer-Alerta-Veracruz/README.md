# REA — Veracruz Mujer Alerta (`com.artech.nuevoalerta.alertamujer`)

Ingeniería inversa estática (REA) de la app de **alerta de violencia de género** de SSP Veracruz.
Producto **GeneXus** (vendor Artech), cruce con `REA-SEG-Movil`.

- [`00-RESUMEN.md`](00-RESUMEN.md) — identidad, pila, hallazgos, conclusión del cruce.
- [`01-manifiesto.md`](01-manifiesto.md) — permisos, componentes, NSC.
- [`02-red-endpoints.md`](02-red-endpoints.md) — backend `http://alertagenero.sspver.gob.mx/alerta/`.
- [`03-tls-cleartext.md`](03-tls-cleartext.md) — **H1 CRÍTICA: cleartext (CWE-319)**.
- [`04-permisos-almacenamiento.md`](04-permisos-almacenamiento.md) — PII (ubicación/contactos/cámara).
- [`05-genexus-cruce-segmovil.md`](05-genexus-cruce-segmovil.md) — el defecto es del despliegue, no de GeneXus.
- [`06-hallazgos.md`](06-hallazgos.md) — tabla consolidada.

**Hallazgo principal:** la alerta + ubicación de una mujer en riesgo viaja por **HTTP en claro**
(NSC lo permite). CRÍTICA, clase CWE-319 (como 911-CDMX). Trust-all AUSENTE: no comparte el defecto
de Infonavit/Cosmocolor. `decompiled/` y `evidence/` gitignorados.
