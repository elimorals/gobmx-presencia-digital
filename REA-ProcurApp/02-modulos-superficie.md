# Módulos y superficie

`project`/`search_android_classes` sobre el package propio: **~697 clases**, **sin ofuscar**,
organizadas en **MVVM por módulo de feature** (`model` / `network` (Retrofit) / `persistence`
(Room) / `view` (Activities) / `viewmodel`).

## Módulos de feature (cada uno con su `GetDataService*` Retrofit)

| Módulo | Qué hace | Servicio de red |
|---|---|---|
| `denuncia` | denuncia ciudadana general | `GetDataServiceDenuncia` + `GetDenunciasRepository` |
| `denuncia_casahabitacion` | denuncia de robo a casa-habitación | `GetDataServiceDenunciaCasa` |
| `denuncia_genero` | **denuncia por violencia de género** | `GetDataServiceDenunciaGenero` + `GetGeneroRepository` |
| `extorcion` | reporte de extorsión | `GetDataServiceExtorcion` + `GetExtorcionRepository` |
| `emergencias` | emergencias | `GetDataServiceEmergencia` |
| `panico` | **botón de pánico + contactos de pánico** | `GetDataServicePanico` |
| `perfil_genetico` | **perfil genético (ADN): solicitud, citas, muestras** | `GetDataServicePerfilGenetico`, `PostSolicitudRepository`, `Cita*` |
| `persona_desaparecida` | reporte/consulta de personas desaparecidas | `GetDataServicePersonaDes` |
| `antecedentes` | **cartas de no-antecedentes por folio** | `GetDataServiceCartas` + `CartasFolioRepository` |
| `investigacion` | seguimiento de investigación (QR, seguimientos) | `GetDataServiceInvestigacion` |
| `vehiculo_robado` | reporte/consulta de vehículo robado | `DataServiceVehiculo` + `GetVehiculoRepository` |
| `revision_vehicular` | revisión vehicular | `GetDataServiceRevision` |
| `citas` | agenda de citas | `GetDataServiceCitas` |
| `registro` / login | alta, login, recuperar contraseña, perfil | `GetDataService` (registro) |
| `quejas_sugerencias` | quejas/sugerencias | `GetDataServiceQuejas` |
| `notificaciones`, `twitter`, `menu`, `utils` | soporte | varios |

## Clientes de red (`utils`)

Múltiples instancias Retrofit: `RetrofitClientInstance` (default, **valida TLS**),
`RetrofitClientInstancePROD`, `RetrofitClientInstanceGenetica`, `RetrofitClientInstanceWS_CITAS`
(**estos tres usan `UnsafeOkHttpClient`, doc 03**), `RetrofitClientRoot`,
`RetrofitClientArchivos` (subida/descarga de archivos de evidencia), `RetrofitClientCatalogo`.

## Lectura de superficie

La app concentra **la gama más sensible de datos de procuración de justicia**: denuncias
(incluida violencia de género), pánico con ubicación y contactos, **perfil genético/ADN**,
personas desaparecidas, extorsión. Cada módulo sube esos datos a los backends de la FGE. Que
**el cliente de producción y el de genética** usen un canal TLS que no valida al servidor
(doc 03) convierte esta superficie en el peor escenario de exposición de PII del acervo.
