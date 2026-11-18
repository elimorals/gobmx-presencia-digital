# REA · Licencia Digital Tlaxcala (`com.dcsoluciones.licenciadigitalglobal`)

Ingeniería inversa estática del APK público (vendor **DC Soluciones**, SMYT Tlaxcala). Parte del
**cruce de proveedores de licencia de conducir** del lote 5.

- [00 · Resumen](00-RESUMEN.md) — identidad, pila, veredicto del cruce.
- [01 · Manifiesto](01-manifiesto.md) — permisos + config de Capacitor.
- [02 · Red, TLS y pago](02-red-tls-pago.md) — endpoints, TLS, OpenPay.
- [03 · Hallazgos](03-hallazgos.md) — tabla con CWE/severidad/estado.

**TL;DR:** Capacitor/Quasar (JS legible), API **HTTPS** `applic.smyt.tlaxcala.gob.mx`, pago por
redirección (OpenPay). **No** replica el defecto trust-all de Cosmocolor → el defecto multi-estado
**no generaliza** a todos los vendors. Flags menores: Capacitor `cleartext:true`/`allowMixedContent`
(H1, MEDIA) y host de DNS dinámico en la allowlist (H2, BAJA). Re-firmada APKPure.

`decompiled/` y `evidence/` gitignorados.
