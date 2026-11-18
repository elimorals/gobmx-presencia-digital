# REA — CAASIM (agua Hidalgo)

Ingeniería inversa estática de la app **CAASIM** (`mx.gob.hidalgo.caasim.app`, v1.0.39), organismo
de agua del Gobierno de Hidalgo. App nativa Android (OkHttp/Retrofit + PDFium).

- [00-RESUMEN.md](00-RESUMEN.md) — identidad, pila, backends, tabla de hallazgos, cruce con el mapa.
- [01-manifiesto.md](01-manifiesto.md) — permisos y componentes.
- [02-red-endpoints-tls.md](02-red-endpoints-tls.md) — NSC/cleartext, endpoints, pasarela Adquira, trust-all ausente.
- [03-hallazgos.md](03-hallazgos.md) — hallazgos con severidad.
- `AndroidManifest.xml` — manifest decodificado · `hashes.sha256` — hashes del XAPK y base APK.

**Lo relevante:** (1) NSC habilita **cleartext** para el dominio de la API en una app de **pagos de
agua** (CWE-319, ALTA). (2) Pasarela **Adquira México**, el **mismo procesador que Tesorería CDMX**
→ cruce de proveedor de pagos CDMX↔Hidalgo. (3) **Sin trust-all** (sano en TLS). (4) Re-firmada por
APKPure (verificar autenticidad).

`decompiled/` y `evidence/` están gitignorados.
