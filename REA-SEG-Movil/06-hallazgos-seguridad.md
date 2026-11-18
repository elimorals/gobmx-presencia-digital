# Hallazgos de seguridad y preguntas abiertas

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.

## Positivos (postura)

### P1 — Transporte HTTPS íntegro
Los 6 backends y la descarga de certificados son HTTPS; sin `usesCleartextTraffic`; sin
endpoints `http://` propios (doc 01, 04).

### P2 — Identidad delegada a Azure AD B2C, sin secretos
Autenticación vía Microsoft Identity (Azure AD + B2C `seggtob2c`), cliente público **sin
client secret** embebido (doc 03). Patrón correcto para una app móvil.

### P3 — Permisos acotados
Solo cámara, micrófono, red y notificaciones; sin ubicación/contactos/SMS (doc 01).

## Observaciones (menores / superficie)

### O1 (Bajo) — Mapa de infraestructura en claro
`appsettings.json` expone **todos** los hosts de la SEG, el tenant B2C, políticas y scopes.
Ninguno es secreto, pero en conjunto es **reconocimiento regalado** de la superficie de la
dependencia (dominios de API, proveedor de identidad, estructura de servicios).

### O2 (Bajo) — `paseLista` en dominio de hosting por defecto
El servicio de asistencia corre en `…azurewebsites.net` en vez de `seg.guanajuato.gob.mx`
(doc 04): expone el hosting y el nombre interno del recurso, y rompe la consistencia de dominio.

## Pregunta abierta (a revisar, no confirmada)

### Q1 — Posible IDOR en descarga de certificados
`ServiceDownload.ashx?cadenaFecCorta={folio}&insGUI=a&modId=1` descarga un certificado por
folio. **Si** los folios son predecibles/enumerables **y** el endpoint no exige autorización
ligada al folio, habría exposición de certificados de terceros. **No verificable estáticamente**
(exigiría probar el endpoint vivo — fuera de alcance y de autorización). Se deja como superficie
que el equipo de la SEG debería revisar del lado del servidor. **No se afirma que exista.**

## Límites (sin inventar)

- **Lógica GeneXus/.NET:** no decompilada función por función; los assemblies no estaban en el
  split descargado (doc 02). Comportamiento exacto de auth, manejo de token y validación de
  certificado del lado cliente = desconocido estáticamente.
- **Pinning TLS:** no observable (viviría en la capa .NET/MSAL).
- **Uso de cámara/micrófono:** se infiere (QR/video), no se confirmó el flujo concreto.
