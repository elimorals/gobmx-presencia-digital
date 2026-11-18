# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: portal **B2G de proveedores** (datos empresariales/fiscales, concursos); sin pagos con
tarjeta ni PII de ciudadanos.

## H1 (Bajo-Medio) — Cleartext global + IPs internas de la CFE en el NSC de producción
`android:usesCleartextTraffic="true"` + `network_security_config` que permite cleartext a
**`10.4.70.22`** y **`10.220.1.71`** (IPs privadas RFC1918 de la red interna de la CFE) — doc 01,
04. Impacto: (a) **fuga de topología interna** (segmentos de red de la CFE embebidos en una app
pública); (b) HTTP en claro habilitado y no prevenido a nivel app. La API pública es HTTPS por su
URL, pero nada lo fuerza. **Fix:** quitar `usesCleartextTraffic`, eliminar las IPs internas del
NSC de producción, forzar HTTPS.

## H2 (Bajo) — `allowBackup="true"`
Permite respaldar los datos de la app (sesión/token persistido) vía ADB/backup del sistema
(doc 01). **Fix:** `allowBackup="false"` o reglas de backup que excluyan credenciales.

## H3 (Bajo) — Codebase "DemoCFE" en producción
El ensamblado propio es `DemoCFE`/`DemoCFE.Android` (doc 02): la app oficial se compila desde un
proyecto llamado "Demo". No es una vulnerabilidad, pero es una señal de higiene/madurez de
release (y un indicio de posible código de demostración residual). **Fix:** renombrar/limpiar.

## H4 (Bajo) — Sin certificate pinning
Validación TLS estándar sin pinning (doc 05). Para una app de licitaciones es menor; se
recomienda pinning de `*.cfe.mx` dado el valor de los datos de concurso.

## Positivos
- **P1 — TLS no se desactiva.** Sin trust-all / `DangerousAccept*` / callback que devuelva true;
  `HttpClientHandler` por defecto → validación estándar (doc 05). Lo opuesto a ProcurApp/EDOMEX.
- **P2 — Backend HTTPS oficial** (`enlacesec.cfe.mx`, `msc.cfe.mx`) y **sin secretos embebidos**
  (doc 04).
- **P3 — Permisos mínimos** (red + almacenamiento; sin cámara/ubicación/contactos) (doc 01).

## Límites (sin inventar)
- **C# textual no decompilado** (sin ILSpy): el detalle de auth, hashing de contraseña y rutas
  bajo `/CFEService/` vive en los 4 métodos de `DemoCFE.Data.RestService`, identificados a nivel
  metadata/IL, no reconstruidos a C#.
- **Integridad:** binario re-firmado por APKPure (doc 06) → conclusiones de arquitectura, no de
  integridad del artefacto original de la CFE.
