# REA — eSIT Móvil + Hidalgo (`com.esitmovil`)

Ingeniería inversa estática de la app del **Sistema Integral Tributario (SIT)** de Hidalgo.

- **Pila:** React Native + Hermes (bundle en bytecode = muro JS).
- **Firma:** re-firmada por APKPure — no auténtica (cruzar contra canal oficial).
- **Backend:** `atributariaw1.hidalgo.gob.mx`, `portaltributario.hidalgo.gob.mx` (HTTPS).
- **Veredicto de seguridad:** **sano en TLS/cleartext** (contraejemplo); higiene de permisos
  mejorable (`DUMP` inerte, `RECORD_AUDIO` amplio).

Capítulos:
- [00-RESUMEN.md](00-RESUMEN.md) — identidad, pila, hallazgos.
- [01-manifiesto.md](01-manifiesto.md) — permisos, cleartext/NSC.
- [02-red-tls-hallazgos.md](02-red-tls-hallazgos.md) — backends, TLS, secretos, proveniencia.

`decompiled/` y `evidence/` están gitignorados (artefactos de trabajo).
