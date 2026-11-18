# REA · Preinscripciones Coahuila

Ingeniería inversa estática de la app **Preinscripciones Coahuila** (`com.luca.preinscripciones`),
preinscripción escolar de la SEDU Coahuila (proveedor LUCA). Lote 4 (apps ≥100k instalaciones).

- [00-RESUMEN.md](00-RESUMEN.md) — identidad, pila, veredicto.
- [01-manifiesto.md](01-manifiesto.md) — permisos, componentes.
- [02-red-endpoints.md](02-red-endpoints.md) — backends Azure, CURP/hermano, demo residue.
- [03-tls-cleartext.md](03-tls-cleartext.md) — cleartext/trust-all/secretos.
- [04-hallazgos.md](04-hallazgos.md) — hallazgos con severidad.

**Pila:** Flutter (Dart AOT, muro). **Firma:** re-firmada APKPure (no auténtica).
**Veredicto:** relativamente limpia (HTTPS, sin cleartext, sin secretos, sin trust-all
evidenciable); el punto es de **gobernanza** — PII de menores en infra Azure de proveedor, no gob.mx.

`decompiled/` y `evidence/` están gitignored (artefactos de trabajo).
