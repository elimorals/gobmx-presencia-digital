# Denuncia, permisos de importación y PII

La app tiene dos flujos que manejan datos del usuario, ambos sobre la capa de red con cleartext
habilitado (doc 01, 03).

## Denuncia (`DenunciaDao` / `DenunciaEntity` / `FoliosDenunciaEntity`)

El Programa Paisano permite **levantar denuncias** (típicamente por abuso/extorsión de
autoridades en el cruce fronterizo). Las clases involucradas:
- `Fragments.DenunciaFragment` — la UI del formulario.
- `WebServices.DenunciaDao` — envía la denuncia al servicio.
- `Entities.DenunciaEntity` — el modelo de la denuncia.
- `Entities.FoliosDenunciaEntity` + `Parsers.FoliosDenunciaParser` — el folio de acuse.

**PII:** una denuncia de este tipo normalmente incluye identidad del denunciante, descripción del
hecho, lugar/fecha y posiblemente la autoridad señalada. Los campos exactos de `DenunciaEntity`
no se volcaron campo por campo, pero el flujo existe y usa la misma capa `Connection` que permite
HTTP (doc 03). **Riesgo:** si el envío de la denuncia va por un endpoint `http://`, los datos del
denunciante podrían viajar sin TLS. No se confirmó estáticamente a cuál de los endpoints
(prod HTTPS SIAT vs. los HTTP) se manda la denuncia → se marca como **superficie a revisar**, no
como hecho. *(Para confirmarlo habría que decompilar `DenunciaDao` y resolver qué URL de
`ConfiguracionAmbiente` usa; queda como profundización.)*

## Permisos de importación vehicular (`ConsultaPermisoDao` / `TramiteDePermisoFragment`)

- `ConsultaPermisoDao` / `ConsultaPermisoParser` / `ConsultaPermisoEntity` / `DetallePermisoEntity`
  → **consulta de permisos de importación** (el permiso temporal de vehículos que tramitan los
  paisanos). El trámite de alta enlaza a `https://www.banjercito.com.mx/registro` (Banjército, el
  emisor del permiso, HTTPS).
- La consulta se resuelve contra el **SIAT** (`siat.sat.gob.mx/wcfcpitv`, WCF SOAP, HTTPS).

## Ubicación

`Entities.LocationManagerSingleton` + permisos de ubicación → se usa para el **mapa de tráfico**
(`MapadeTraficoFragment`) y para ubicar módulos/dependencias cercanas. No se observó envío de
ubicación a un tercero publicitario (no hay AdServices).

## Lectura

Es una app de **contenido + trámites de consulta**, no transaccional de dinero. El dato más
sensible es la **denuncia**; su transporte exacto queda como pregunta abierta (doc 06), honesto:
el flujo existe, la capa de red permite HTTP, pero no se confirmó que la denuncia concreta viaje
sin TLS.
