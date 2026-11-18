# 02 · Red y endpoints

Los endpoints se recuperaron como **strings dentro de `libapp.so`** (Dart AOT). La lógica que los
invoca no es visible estáticamente (muro Dart); se listan los hosts observados, no el flujo.

## Backends de producción (del proveedor LUCA, infra Azure)

Todos **HTTPS**.

### VM Azure Cloud App — `servicios.mexicocentral.cloudapp.azure.com`
Ruta `/wservice_azure/preinscripciones/`:
- `w_service_curp.php` — **consulta por CURP** (identidad nacional del menor).
- `w_service_datos_del_hermano.php` — **datos del hermano** (más PII de menores).
- `w_actualiza_preinscripcion.php` — alta/actualización de la preinscripción.
- `escuelas_cercanas.php` — escuelas por ubicación.
- `w_service_colonias.php` — catálogo de colonias.

### Azure App Service — `seduinscripciones.azurewebsites.net`
- `/preinscripciones/reportes/preinscripcion.php` — reporte/comprobante.

> **Observación.** El backend NO está en dominios `gob.mx`: es un **VM genérico de Azure**
> (`*.cloudapp.azure.com`) y un **App Service** (`*.azurewebsites.net`), hosteados por el proveedor
> LUCA. Web services PHP que manejan CURP, datos del hermano y ubicación de **menores**. HTTPS
> mitiga el tránsito, pero la concentración de PII de menores en infra de proveedor (fuera de
> gob.mx) es el punto de gobernanza/residencia de datos (ver H1).

## Residuo de plantilla (demo endpoints en producción)

Empacados en el release pero no son backend propio:
- `https://jsonplaceholder.typicode.com/todos/1` — API de demo.
- `https://pokeapi.co/api/v2/ability/` — API de demo.
- `https://icanhazip.com/`, `https://one.one.one.one` — detección de IP pública.

Son residuo de la plantilla Flutter del proveedor. Riesgo bajo (APIs públicas de terceros), pero no
deberían ir en un build de producción (ver H2, CWE-489).
