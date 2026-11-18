# Resumen — Bienvenido a México 3.0.0 (`com.tcs.programapaisano`)

## Identidad
- **App:** Bienvenido a México — **Programa Paisano** (SAT / ANAM). Info, contactos consulares,
  consulta de permisos de importación vehicular, mapa de tráfico fronterizo, denuncias.
- **Package:** `com.tcs.programapaisano` (`tcs` = Tata Consultancy Services, desarrollador) ·
  **versión** 3.0.0 (versionCode 35). minSdk 26, target 34.
- **#32** del inventario por instalaciones (≈500 K).
- **Firma:** AUTÉNTICA (`CN=Angel Fernandez, OU=AGCTI, O=SAT`).

## Pila
**Nativa Android (Kotlin/Java)**, 1 `classes.dex`, 1 268 clases, **sin ofuscar**, arquitectura
MVC clásica. Código **legacy** (usa `android.support.v4` y `org.apache.http.legacy`, no AndroidX).
Sin Flutter/Unity/.NET/WebView-wrapper.

## Arquitectura (clases propias, `decompiled/class-map.txt`)
- `Connection/` → `ConnectionManager`, `GenericConnection` (capa HTTP genérica)
- `WebServices/` → `ConsultaPermisoDao`, **`DenunciaDao`**, `ModulosDao`, `ProcesamientoDao`
- `Entities/` → `ConsultaPermisoEntity`, **`DenunciaEntity`**, **`FoliosDenunciaEntity`**, `ContactoEntity`, `LocationManagerSingleton`, …
- `DBManager/` → SQLite (`SQLiteConnectionFactory` + DAOs)
- `Parsers/`, `Mappers/` → XML/SOAP parsing
- **`ConfiguracionAmbiente`** → selector de ambiente (prod/UAT/dev) · `Constants` → URLs

## Endpoints embebidos (`evidence/endpoints.txt`)
| Tipo | URL |
|---|---|
| Prod SAT (HTTP) | `http://m.sat.gob.mx/...listfeed.aspx`, `http://www.sat.gob.mx/...listfeed.aspx` |
| Prod SIAT (HTTPS) | `https://movil.siat.sat.gob.mx/`, `https://siat.sat.gob.mx/wcfcpitv` (WCF SOAP) |
| **UAT (pruebas)** | `http://piuat.sat.gob.mx/...`, `https://moviluat.siat.sat.gob.mx/` |
| **DEV (Azure)** | `http://develmobile.cloudapp.net/` |
| **QA (hosting gratis)** | `http://satmovilqd.gear.host` |
| Externo | `https://www.banjercito.com.mx/registro` (permiso vehicular) |

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ⚠️ `usesCleartextTraffic=true` + endpoints HTTP del SAT |
| Higiene de build | ⚠️ UAT/DEV/QA en producción (incl. `gear.host` y Azure dev) |
| Firma | ✅ Auténtica del SAT |
| Código | ⚠️ Legacy (support.v4, Apache HTTP), `allowBackup=true` |
| PII | 🔎 Denuncia + permisos usan la capa con cleartext (doc 04) |

**Lectura:** firma auténtica y arquitectura transparente, pero **postura de red débil**:
cleartext global, contenido del SAT por HTTP y un mapa de ambientes de desarrollo/pruebas
(incluido un host gratuito) embebido en el binario de producción.
