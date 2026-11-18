# 03 · Hallazgos

| # | Hallazgo | CWE | Severidad | Estado | Evidencia |
|---|----------|-----|-----------|--------|-----------|
| H1 | Capacitor `cleartext:true` + `allowMixedContent:true` global | CWE-319 (enabler) | **MEDIA** | CONFIRMADO (config) / impacto A-CONFIRMAR | `assets/capacitor.config.json` |
| H2 | `allowNavigation` a host de **DNS dinámico** `prisma-apps.servehttp.com` | CWE-1327 | BAJA | CONFIRMADO (allowlist), no usado en JS | `capacitor.config.json`; grep del bundle = 0 usos |
| H3 | Trust-all / WebView SSL bypass | CWE-295 | — | **AUSENTE-SANO** | símbolos de framework, sin override `proceed()` de la app |
| H4 | White-label multi-estado (sufijo "global") | — | — | **AUSENTE** | solo residuo `tlaxcala` (×15), sin otros estados |
| H5 | Secretos hardcodeados en el bundle | CWE-798 | — | **AUSENTE** | grep de AIza/sk_/pk_/api_key/token = 0 |
| H6 | Firma | — | INFO | re-firmada APKPure | `CN=Android, O=Google Inc.` (no auténtica) |

## Veredicto

**Comparativamente sano.** Para el cruce de proveedores de licencia de conducir, **DC Soluciones
(Tlaxcala) no replica el defecto TLS trust-all de Cosmocolor** (EDOMEX/Jalisco/Chiapas): usa una
arquitectura Capacitor con API HTTPS a `gob.mx`, sin trust-all, con pago por redirección (no PAN en
cliente). El único punto real es la config de Capacitor que **permite** cleartext/mixto (H1) y el
host de DNS dinámico allowlisteado (H2) — higiene, no explotación demostrada.

## Qué queda sin confirmar (honesto)

- Validación TLS **efectiva** del WebView en runtime (los símbolos SSL son del framework; no se
  observó override). Requeriría dinámico.
- El flujo real de la pasarela OpenPay (fuera del APK).
