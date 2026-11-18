# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: app de **contenido + consultas** (no transaccional de dinero); el dato más sensible es
la denuncia.

## H1 (Medio) — Cleartext HTTP habilitado + endpoints del SAT en claro
`usesCleartextTraffic="true"` (doc 01) + feeds de contenido del SAT en `http://`
(`m.sat.gob.mx`, `www.sat.gob.mx`, `_layouts/listfeed.aspx`) (doc 03). El contenido mostrado al
paisano puede viajar sin TLS → MITM puede leerlo/alterarlo. **Fix:** forzar HTTPS en todos los
feeds; quitar `usesCleartextTraffic`.

## H2 (Medio) — Ambientes dev/UAT/QA embebidos en producción
`ConfiguracionAmbiente` deja el mapa completo: UAT (`piuat`, `moviluat`), desarrollo
(`develmobile.cloudapp.net`, Azure) y QA (`satmovilqd.gear.host`) (doc 03). Fuga de
infraestructura interna + superficie innecesaria + build no saneado. **Fix:** compilar solo la
URL de producción; eliminar los hosts de prueba del binario público.

## H3 (Bajo-Medio) — Endpoint del SAT en hosting gratuito
`satmovilqd.gear.host` en **gear.host** (hosting gratuito) por HTTP (doc 03). Mala práctica de
infraestructura para una dependencia federal (control/disponibilidad/confianza fuera del
gobierno). **Fix:** migrar a infraestructura oficial; no exponerlo en el cliente.

## H4 (Bajo) — `allowBackup=true` + pila legacy
Base SQLite local respaldable vía backup (doc 01); `org.apache.http.legacy` (cliente HTTP
obsoleto) y `android.support.v4` (no AndroidX). Deuda técnica + superficie. **Fix:**
`allowBackup=false`; migrar a OkHttp/AndroidX.

## Pregunta abierta (a revisar)
### Q1 — ¿La denuncia viaja por HTTP?
El flujo `DenunciaDao`/`DenunciaEntity` (doc 04) usa la capa de red que permite cleartext. No se
confirmó estáticamente a cuál endpoint (HTTPS SIAT vs. HTTP) se envía la denuncia. **Si** va por
un `http://`, los datos del denunciante viajarían sin TLS. Requiere decompilar `DenunciaDao` +
resolver `ConfiguracionAmbiente`. **No se afirma que sea así.**

## Positivos
- **Firma auténtica del SAT** (doc 05) → binario íntegro del emisor.
- **Código sin ofuscar** → transparente para auditoría.
- **Permisos acotados** (ubicación + llamada; sin cámara/contactos/SMS).
- El SIAT (consultas de permisos) **sí** usa HTTPS.

## Límites (sin inventar)
- No se decompiló `DenunciaDao`/`ConfiguracionAmbiente` campo por campo (Q1 queda abierta).
- Los campos exactos de `DenunciaEntity` (qué PII) no se volcaron uno a uno.
- SQLite local: contenido público cacheado, sin cifrado aparente — bajo riesgo, no verificado a
  fondo.
