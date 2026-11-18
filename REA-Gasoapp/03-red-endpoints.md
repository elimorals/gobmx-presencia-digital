# Red y endpoints

Recuperados **limpios** de los #US strings del DLL propio descomprimido (ver
`decompiled/config-endpoints.txt`).

## 3 BaseUrls en el binario de producción

| Entorno | URL | Nota |
|---|---|---|
| Producción | `https://api-gasoapp.cre.gob.mx/` | oficial, HTTPS |
| **QA** | `https://api-gasoapp-qa.cre.gob.mx/` | **entorno de pruebas dejado embebido** en el build de prod |
| Azure crudo | `https://api-estacionservicio-327800.gasoapp.p.azurewebsites.net/` | servicio de estaciones en el **dominio por defecto de Azure App Service** |

### Observaciones
1. **QA en producción (Bajo).** `api-gasoapp-qa.cre.gob.mx` quedó en el binario. Expone la
   existencia y el nombre del entorno de pruebas; si ese entorno es alcanzable y menos protegido,
   amplía la superficie. Debería eliminarse del build de prod.
2. **Dominio Azure crudo (Bajo).** `api-estacionservicio-327800.gasoapp.p.azurewebsites.net` corre
   en `azurewebsites.net` (no bajo `cre.gob.mx`): expone el hosting y un identificador interno
   (`327800`), y rompe la consistencia de dominio (dificulta pinning/políticas por dominio). HTTPS,
   así que no es vulnerabilidad en sí.

## Enlaces de contenido (no endpoints de datos)
`http://www.gob.mx/cre`, términos y condiciones de Gasoapp, nota del DOF (marco legal),
`https://noimage.gob.mx/` (placeholder de imagen).

## TLS (postura)
- **Sin bypass de validación.** No se hallaron `ServerCertificateCustomValidationCallback`,
  `DangerousAcceptAnyServerCertificateValidator`, `RemoteCertificateValidationCallback` trust-all
  ni manipulación de `ServicePointManager` en los ensamblados → **validación de certificado
  estándar** del sistema. Contrasta favorablemente con Infonavit/Licencia EDOMEX.
- **Pinning:** no se observó certificate pinning (confianza en CAs del sistema). Sin pinning, pero
  sin desactivar validación tampoco.
- Los 3 endpoints son **HTTPS**; no se hallaron endpoints `http://` de datos.

## Sin secretos
No se hallaron `AccountKey`/SAS de Azure Storage, API keys ni `ClientSecret` embebidos en los
DLLs descomprimidos (doc 04).
