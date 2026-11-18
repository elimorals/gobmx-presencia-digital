# Resumen — CFE Proveedores 1.0.7 (`mx.com.cfe.proveedores`)

## Identidad
- **App:** CFE Proveedores — portal de proveedores de la Comisión Federal de Electricidad:
  concursos/licitaciones, consorcios, datos del proveedor.
- **Package:** `mx.com.cfe.proveedores` · **versión** 1.0.7 (versionCode 20240618, build 2024-06-18).
- **#49** del inventario por instalaciones (≈100 K). compileSdk 33.
- **Firma:** re-firmada por APKPure (`CN=Android, O=Google Inc.`).

## Pila
**Xamarin.Forms clásico (.NET/Mono)** — `libmonosgen-2.0.so`, `libmonodroid.so`,
`libxamarin-app.so` + `assemblies/assemblies.blob` (store **XABA v1**, DLLs comprimidas
**XALZ/LZ4**). Se extrajeron los ensamblados; el propio es **`DemoCFE`** (1.27 MB, .NET Standard
2.0, 519 tipos) + **`DemoCFE.Android`** (508 KB). Dependencias: Xamarin.Forms, Newtonsoft.Json,
Rg.Plugins.Popup, System.Net.Http.

> **El nombre interno del proyecto es "DemoCFE"** — un codebase "Demo" en producción.

## Lo observable (DemoCFE.dll)
- **Backend API:** `https://enlacesec.cfe.mx/api/CFEService/` (HTTPS, oficial CFE).
- **Portal:** `https://msc.cfe.mx/Aplicaciones/NCFE/Concursos/Proveedores/`.
- **Entidades:** `CFE.MSC.Entities` — IdProveedor, NombreProveedor, ProveedorTitular, Consorcios,
  SugerenciaProveedor → gestión de proveedores y concursos.
- **Red:** `DemoCFE.Data.RestService` (HttpClientHandler). Vistas: Login, MainPageLogin,
  RecoverPassword (Token/Password), MyServices.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| TLS del cliente | ✅ Validación estándar; **sin** trust-all/bypass; sin pinning |
| Backend | ✅ HTTPS oficial `enlacesec.cfe.mx` |
| Secretos | ✅ No se hallaron `client_secret`/API keys embebidos |
| Cleartext | ⚠️ `usesCleartextTraffic=true` + NSC con 2 IPs internas CFE (10.4.70.22, 10.220.1.71) |
| Backup | ⚠️ `allowBackup=true` |
| Higiene | ⚠️ Nombre "DemoCFE" en producción; residuos de infra interna |
| Firma | ⚠️ Re-firmada por APKPure |

## Qué NO se puede (límite)
La lógica C# textual no se decompiló (sin ILSpy en el entorno); el análisis es a nivel
metadata/IL/literales. El detalle del protocolo de auth y de las rutas bajo `/CFEService/` vive
en los cuerpos de método de `DemoCFE.Data.RestService` (4 métodos) — identificados, no
desensamblados a C#. Datos de proveedor (RFC, razón social) — es portal B2G, no maneja tarjetas
ni PII de ciudadanos.
