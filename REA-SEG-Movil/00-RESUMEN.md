# Resumen — SEG Móvil 1.0.32 (`com.artech.sdsigueme.sigueme`)

## Identidad

- **App:** SEG Móvil — **Secretaría de Educación de Guanajuato** (SEG). Servicios: seguimiento
  académico, escuela de padres, cursos, pase de lista (asistencia), notificaciones y descarga
  de certificados. (Confirmado por los hosts en `appsettings.json`, no inferido.)
- **Package:** `com.artech.sdsigueme.sigueme` (`artech` = GeneXus) · **versión** 1.0.32.
- **#21** del inventario por instalaciones (≈500 K).
- **Firma:** `CN=Artech Genexus, O=Artech` (firma de plataforma GeneXus, preservada).

## Arquitectura (en una línea)

App **generada con GeneXus para Smart Devices** sobre runtime **.NET** (launcher
`crc64…MainActivity` = Java Callable Wrapper de Xamarin/.NET; config `appsettings.json`). Incluye
**ExoPlayer/DASH** (reproducción de video — cursos). La lógica la genera GeneXus; el DEX es host.

## Configuración embebida (`evidence/appsettings.json`, en claro)

**Identidad — Azure AD B2C (Microsoft):**
- `Authority: https://login.microsoftonline.com/<tenantId>`
- B2C social: `seggtob2c.onmicrosoft.com` / `seggtob2c.b2clogin.com`, política
  `B2C_1_InicioSesionRedesSociales`.
- `ClientId`, `TenantId`, `ClientIdSocial` presentes (IDs **públicos** de OAuth; **sin client
  secret** → correcto para cliente móvil público).

**Backends (`Hosts`):**
| Servicio | Host |
|---|---|
| seguimientoAcademico | `https://seguimientoacademico.seg.guanajuato.gob.mx/` |
| autorizacionAcceso | `https://autorizacionacceso.seg.guanajuato.gob.mx/` |
| notificaciones | `https://notificaciones.seg.guanajuato.gob.mx/` |
| cursos (escuela de padres) | `https://escuelapadresapi.seg.guanajuato.gob.mx/` |
| **paseLista** | `https://paselista-…mexicocentral-01.azurewebsites.net/` ⚠️ hosting crudo |
| administracionGeneral | `https://administraciongeneralsegmovil.seg.guanajuato.gob.mx/` |
| Certificado | `https://certificacionesfec.seg.guanajuato.gob.mx/ServiceDownload.ashx?cadenaFecCorta={folio}…` |

## Postura (balance)

| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ Todo HTTPS |
| Identidad | ✅ Delegada a Azure AD B2C; sin secreto en el cliente |
| Dominio | ✅ Mayoría `seg.guanajuato.gob.mx` · ⚠️ `paseLista` en `azurewebsites.net` |
| Config | ⚠️ Mapa completo de infraestructura en claro en `appsettings.json` |
| Certificados | ⚠️ `{folio}` → posible IDOR (pregunta abierta, no verificable estáticamente) |

**Lectura:** app con **buena higiene de transporte e identidad** (HTTPS + Azure B2C, sin
secretos). Lo observable es su **mapa de backend** completo; el único detalle de arquitectura
cuestionable es el endpoint de pase de lista en el dominio por defecto de Azure.
