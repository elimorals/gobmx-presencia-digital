# 03 · Hallazgos

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | Config de red permisiva: `usesCleartextTraffic=true` global (sin NSC) + `allowMixedContent:true` en Capacitor, en app `targetSdk 35` | CWE-319 | **MEDIA-BAJA** | CONFIRMADO (config); explotación A-CONFIRMAR |
| H2 | **NO** hay trust-all / desactivación TLS (contraste con Cosmocolor) | CWE-295 | — | **AUSENTE-SANO** |
| H3 | Sin secretos/API keys en el bundle JS | CWE-312/798 | — | **AUSENTE-SANO** |
| H4 | Re-firmada por APKPure — la firma no prueba autenticidad; cruzar con canal oficial | — | INFO | CONFIRMADO (proveniencia) |

## Cruce de proveedores de licencia (el motivo de este análisis)

**Chihuahua NO es Cosmocolor.** Es un desarrollo **Capacitor** propio de SSP Chihuahua, con backend
HTTPS self-hosted (`licdigital.sspechih.gob.mx:8082`) y **sin el antipatrón trust-all** que define a
las licencias EDOMEX/Jalisco/Chiapas (`mx.com.cosmocolor.movapp.*`). Confirma que el defecto crítico
de Cosmocolor es **del producto Cosmocolor**, no inherente a "apps de licencia de conducir": otros
estados con otro vendor pueden estar sanos en TLS.

Su único punto es de **higiene de configuración** (cleartext/mixed-content permitidos pero no usados
por el backend observado), muy por debajo de la criticidad de Cosmocolor.

## Qué quedó sin confirmar

- Si alguna llamada concreta en runtime cae a HTTP por la config permisiva (requiere dinámico).
- Flujo de pago (si existe) y datos biométricos en tránsito: la app usa biometría; no se observó
  pasarela de pago embebida. No se ejecutó ningún flujo con datos reales (ética).
