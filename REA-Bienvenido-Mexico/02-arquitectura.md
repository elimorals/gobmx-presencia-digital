# Arquitectura — MVC nativo (TCS)

Nativa Android, **sin ofuscar** (nombres legibles), arquitectura MVC por capas. Mapa completo de
clases propias en `decompiled/class-map.txt`.

## Capas

- **`Connection/`** — `ConnectionManager`, `GenericConnection`, `ServiceError` +
  `IConnectionManagerDelegate`/`IGenericConnectionDelegate`/`IGenericParser`. Capa HTTP genérica
  (patrón delegate). Es la que materializa las peticiones a los endpoints (doc 03), sobre la pila
  **Apache HTTP legacy** (doc 01).
- **`WebServices/`** — DAOs de servicio: `ConsultaPermisoDao`, `DenunciaDao`, `ModulosDao`,
  `ProcesamientoDao`, `ConsultaAcercaDeDao`. Cada uno llama a un servicio del SAT (SharePoint
  `listfeed.aspx` o WCF SOAP `wcfcpitv`) y entrega a un `Parser`.
- **`Parsers/`** — `ConsultaPermisoParser`, `FoliosDenunciaParser`, `ProcesamientoParser`,
  `ContactosParser`, … Parsean la respuesta XML/SOAP a `Entities`.
- **`Entities/`** — modelos: `ConsultaPermisoEntity`, `DenunciaEntity`, `FoliosDenunciaEntity`,
  `ContactoEntity`, `DependenciaEntity`, `DetallePermisoEntity`, `LocationManagerSingleton`, …
- **`DBManager/`** — persistencia **SQLite** (`SQLiteConnectionFactory` + DAOs por sección:
  contactos, contenido, dependencias, información, módulos, páginas). La app cachea el contenido
  localmente. SQLite **sin cifrado** aparente (no hay SQLCipher en las libs; es contenido público
  cacheado, no PII crítica).
- **`Fragments/`** — UI: `PermisosFragment`/`TramiteDePermisoFragment` (permisos de importación),
  `DenunciaFragment` (denuncias), `MapadeTraficoFragment` (tráfico), `ConsultadePermisoFragment`,
  `ContactoFragment`, `InformacionFragment`, …

## Selector de ambiente

**`ConfiguracionAmbiente`** es la clase que decide a qué ambiente (producción / UAT / desarrollo)
apunta la app. Es el origen de que el binario contenga el **mapa completo de endpoints de todos
los ambientes** (doc 03): en lugar de compilar solo la URL de producción, el build dejó las de
prueba/desarrollo embebidas.

## Lectura

Es una app de **contenido + consultas** (no transaccional de dinero): muestra información del
Programa Paisano, permite consultar permisos de importación y levantar denuncias. La debilidad no
está en la arquitectura (limpia y legible) sino en la **configuración de red** (doc 03) y en el
**código legacy** (doc 01).
