# 04 · Hallazgos

| # | Hallazgo | CWE | Severidad | Estado | Evidencia |
|---|----------|-----|-----------|--------|-----------|
| H1 | NSC `base-config cleartextTrafficPermitted=true` global | CWE-319 (potencial) | **BAJA** | CONFIRMADO | `res/xml/network_security_config.xml`; backends reales HTTPS |
| H2 | `http://localhost/` embebido (dev, inerte) | CWE-489 (menor) | **INFO** | CONFIRMADO | strings DEX |
| H3 | Trust-all TLS desactivando validación | CWE-295 | — | **AUSENTE-SANO** | `getUnsafeOkHttpClient`=0, `trustAllCerts`=0; `CertificatePinner` presente |
| H4 | Secretos / API keys hardcodeados | CWE-798/312 | — | **AUSENTE-SANO** | sin `AIza…`, sin tokens en DEX; sin `.env`/bundle |
| H5 | IPs internas RFC1918 en producción | CWE-200 | — | **AUSENTE-SANO** | 0 coincidencias |
| H6 | Sobre-permisología | — | — | **AUSENTE-SANO** | 10 permisos, todos coherentes con app de audio |

## Conclusión

**Descarga Cultura UNAM es la app más limpia del lote 4.** Firma auténtica UNAM, pila nativa
ordenada (Retrofit/OkHttp + Fresco + DataStore), backends HTTPS, trust-all ausente, pinning
disponible, cero secretos, permisos mínimos. El único apunte es higiénico (H1: apagar el cleartext
global del NSC aunque no se use).

Sirve de **contraejemplo** en el mapa de proveedores: no toda app de gobierno/universidad hereda los
defectos sistémicos; una app enfocada y bien construida sale sana.

## Remediación (menor)

1. `cleartextTrafficPermitted=false` en el NSC (o eliminar el `base-config`), ya que no se usa HTTP.
2. Retirar `http://localhost/` del build de producción.
