# Red y endpoints

Todos recuperados de `assets/appsettings.json` (en claro). Correlacionados con los nombres de
servicio que la propia config declara (no inventados).

## Backends (`Hosts`)

| Clave | URL | Nota |
|---|---|---|
| `seguimientoAcademico` | `https://seguimientoacademico.seg.guanajuato.gob.mx/` | boletas/avance académico |
| `autorizacionAcceso` | `https://autorizacionacceso.seg.guanajuato.gob.mx/` | autorización/acceso (scope `autorizacion.acceso`) |
| `notificaciones` | `https://notificaciones.seg.guanajuato.gob.mx/` | push backend |
| `cursos` | `https://escuelapadresapi.seg.guanajuato.gob.mx/` | escuela de padres / cursos |
| `paseLista` | `https://paselista-d4fyfrebhbhxdnfa.mexicocentral-01.azurewebsites.net/` | **asistencia — hosting Azure crudo** |
| `administracionGeneral` | `https://administraciongeneralsegmovil.seg.guanajuato.gob.mx/` | administración |

## Descarga de certificados (`Certificado.URL`)

```
https://certificacionesfec.seg.guanajuato.gob.mx/ServiceDownload.ashx?cadenaFecCorta={folio}&insGUI=a&modId=1
```
Handler ASP.NET (`.ashx`) que **descarga un certificado por `{folio}`** (cadena FEC corta).

## Observaciones

1. **HTTPS en todos (positivo).** No hay endpoints `http://` propios; el transporte es sano y
   consistente con la ausencia de `usesCleartextTraffic` (doc 01).
2. **`paseLista` en `azurewebsites.net` (menor).** El servicio de pase de lista corre en el
   **dominio por defecto de Azure App Service**, no bajo `seg.guanajuato.gob.mx`. Sigue siendo
   HTTPS, pero:
   - expone el proveedor de hosting y el nombre interno del recurso (`paselista-d4fyfrebhbhxdnfa`),
   - rompe la consistencia de dominio (dificulta pinning/políticas por dominio).
   Es una observación de arquitectura, no una vulnerabilidad en sí.
3. **`ServiceDownload.ashx?cadenaFecCorta={folio}` — posible IDOR (pregunta abierta).** Si el
   `{folio}` es predecible o enumerable y el endpoint **no exige sesión/autorización por folio**,
   un tercero podría descargar certificados ajenos. **No verificable estáticamente** (requeriría
   probar el endpoint, fuera de alcance/autorización). Se reporta como **superficie a revisar**,
   no como hallazgo confirmado.

## Pinning / validación TLS

No se observó config de pinning por recurso ni se decompiló la capa .NET/MSAL (doc 02), así que
no se puede afirmar si hay certificate pinning. La confianza recae, por defecto, en las CAs del
sistema. Marcado como límite.
