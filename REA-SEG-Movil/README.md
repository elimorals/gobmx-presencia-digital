# Ingeniería inversa de SEG Móvil 1.0.32 — índice

Análisis **estático** del APK `com.artech.sdsigueme.sigueme` con REA (JADX headless +
inspección de archivo). La app **nunca se ejecutó**. Es la app oficial de la **Secretaría de
Educación de Guanajuato (SEG)** — seguimiento académico, escuela de padres, cursos, pase de
lista y descarga de certificados. **#21 por instalaciones** del inventario gob.mx (≈500 K).

> **Generada con GeneXus (firma Artech), stack .NET.** El launcher es un wrapper
> `crc64…MainActivity` (convención de Java Callable Wrappers de Xamarin/.NET) y la configuración
> es `assets/appsettings.json` (convención .NET) → app **GeneXus para Smart Devices** sobre
> runtime .NET. El hallazgo clave es que **toda la configuración de backend y de identidad
> viaja en claro en `appsettings.json`**: 6 hosts (casi todos HTTPS sobre `seg.guanajuato.gob.mx`)
> y la configuración de **Azure AD B2C**. Postura de transporte **sana** (HTTPS) y auth
> delegada a Microsoft Identity; sin client secret embebido (correcto para cliente público).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, GeneXus/.NET, auth, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (cámara, micrófono), componentes |
| 02 | [02-arquitectura-genexus-net.md](02-arquitectura-genexus-net.md) | GeneXus SD + .NET, ExoPlayer, límites |
| 03 | [03-auth-azure-b2c.md](03-auth-azure-b2c.md) | Azure AD B2C, tenants, login social |
| 04 | [04-red-endpoints.md](04-red-endpoints.md) | 6 backends, `appsettings.json`, Azure websites |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma Artech/GeneXus (preservada), procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos y preguntas abiertas |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `evidence/appsettings.json` | Configuración completa (backends + Azure B2C) (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 6 hallazgos principales

1. **Toda la configuración de backend e identidad en `appsettings.json` (en claro).** 6 hosts
   de API + IDs de cliente/tenant de Azure AD B2C. No son secretos (los client/tenant IDs son
   públicos por diseño en OAuth, y **no hay client secret**), pero expone el mapa completo de la
   infraestructura de la SEG (doc 03, 04).
2. **Auth delegada a Azure AD B2C (positivo).** Microsoft Identity con tenant
   `seggtob2c.onmicrosoft.com` + login con redes sociales (política `B2C_1_InicioSesionRedesSociales`).
   Patrón correcto: sin credenciales propias en el cliente (doc 03).
3. **Backends HTTPS sobre dominio oficial (positivo).** `seguimientoacademico`, `autorizacionacceso`,
   `notificaciones`, `escuelapadresapi`, `administraciongeneralsegmovil` — todos
   `https://*.seg.guanajuato.gob.mx` (doc 04).
4. **Un endpoint en dominio de hosting crudo.** `paseLista` =
   `https://paselista-…mexicocentral-01.azurewebsites.net/` — el servicio de pase de lista
   (asistencia) corre en el dominio por defecto de **Azure App Service**, no bajo
   `seg.guanajuato.gob.mx`. HTTPS (bien), pero expone el hosting y rompe la consistencia de
   dominio (doc 04).
5. **Descarga de certificados parametrizada por folio.** `certificacionesfec.seg.guanajuato.gob.mx/ServiceDownload.ashx?cadenaFecCorta={folio}&insGUI=a&modId=1`
   → si los folios son predecibles, superficie de **IDOR**; no verificable estáticamente, se
   deja como pregunta abierta (doc 04, 06).
6. **GeneXus/.NET = lógica no en el DEX.** La lógica de negocio la genera GeneXus y corre en el
   runtime .NET; el DEX es host. El análisis a fondo de la lógica queda como límite (doc 02).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. Los IDs de Azure documentados son **identificadores públicos de OAuth**, no
secretos. La lógica generada por GeneXus (.NET) no se decompiló función por función; se
documenta la arquitectura, la configuración embebida y la postura — con evidencia.
