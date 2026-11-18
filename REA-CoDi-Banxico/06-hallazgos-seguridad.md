# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se enrolaron cuentas ni se
llamó a ningún endpoint. **CoDi Banxico es la app con mejor postura del acervo**, así que esta
sección es, inusualmente, casi toda de positivos.

## Positivos (postura)

### P1 (destacado) — Certificate pinning correcto
`httpclient.security.a.checkServerTrusted` valida la cadena contra las CAs del sistema **y**
exige que el SHA-256 del cert hoja esté en una lista fijada; si no, lanza (doc 04, código
citado). Postura TLS más fuerte del inventario; lo opuesto a Infonavit/Licencia EDOMEX.

### P2 — Llaves en AndroidKeyStore atadas a biometría
`AndroidKeyStore` + `setUserAuthenticationRequired` → llaves no exportables, cuyo uso exige
autenticación del usuario (doc 03).

### P3 — Firma EC de operaciones + AES-GCM
Firma digital con curvas elípticas (no repudio de los cobros) y cifrado autenticado AES-GCM
(doc 03).

### P4 — Higiene de sesión y permisos
Timeout de inactividad (`InactivityWorker`/`SessionTimeoutReceiver`), biometría, cambio de
contraseña; permisos acotados (sin cámara, ubicación, SMS). Ofuscación R8 (doc 01–02).

## Observaciones (menores)

### O1 (Bajo) — Permiso de overlay `SYSTEM_ALERT_WINDOW`
La app declara `SYSTEM_ALERT_WINDOW` (dibujar sobre otras apps), inusual en una app bancaria
(doc 01). No se confirmó su uso en código (ofuscado); podría ser para avisos/alertas en primer
plano. Se reporta como superficie a revisar, **no** como abuso comprobado.

### O2 (informativo) — `READ_CONTACTS` y `Exportar` CSV
La app lee contactos (elegir a quién cobrar) y exporta el historial a CSV (`CoDi.csv`). Manejo
de datos a tener en cuenta, sin hallazgo de seguridad asociado desde el análisis estático.

## Límites (sin inventar)

- **Ofuscación R8:** el motor cripto (`securityservices.engine.*`) y el armado del mensaje CoDi
  están ofuscados; se confirmaron almacén (AndroidKeyStore), biometría y algoritmos, pero no los
  parámetros exactos (curva EC, derivación, IV/nonce) método a método (doc 03).
- **Origen de los pines** (`f6915a`): el mecanismo de verificación es correcto (doc 04); de dónde
  salen los fingerprints fijados (hardcodeados/remotos) no se trazó hasta el call site.
- **Uso de `SYSTEM_ALERT_WINDOW`:** declarado, no confirmado en código.
- No se tocó ningún endpoint ni se enroló cuenta (alcance/ética).
