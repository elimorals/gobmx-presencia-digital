# Servicios estatales y municipales: dónde están (#2)

> **Actualización 2026-10-24: la conclusión anterior estaba equivocada.** El catálogo nacional SÍ
> publica los trámites estatales y municipales de forma central, en un CSV diario. Ya están integrados
> en `servicios/cnartys/` (31,802 trámites: 5,583 federales, 14,608 estatales, 11,611 municipales).
> Abajo queda lo que se intentó el 2026-10-21 y por qué falló, porque sigue siendo útil.

## La fuente que sí funciona (2026-10-24)

```
https://www.catalogonacional.gob.mx/sujetosobligados/TramitesPublicadosCSV/tramites_publicados_DD_MM_YYYY.csv
```

Comprobado el 2026-10-24 con `curl` y `urllib` desde una IP residencial en México:

| Hecho | Evidencia |
|---|---|
| Descarga directa, sin reto de Akamai en esta ruta | HTTP 200, 28,168,411 bytes, `application/octet-stream` |
| El archivo del día D se genera a las **~02:31 UTC del día D+1** (20:31 hora del centro, día D) | `Last-Modified: Sat, 24 Oct 2026 02:31:37 GMT` para `23_10_2026`; `Fri, 23 Oct 2026 02:31:05 GMT` para `22_10_2026` |
| El servidor **solo guarda los 2 más recientes** | el 24/10: `23_10` y `22_10` → 200; `21_10` a `01_10` y `02_04_2025` → 404 |
| El de hoy todavía no existe durante el día | `24_10_2026` → 404 el 24/10 a las 18:35 UTC |
| `catalogonacional.gob.mx` redirige (308) a `www.` | se usa `www.` directo |
| Akamai da 403 a un User-Agent propio | `gobmx-inventario (+github…)` → 403; el de urllib, `curl/8.x` o `Mozilla/5.0` → 200 |
| `22_10` y `23_10` eran idénticos byte a byte | mismo tamaño y hash: no siempre hay cambios de un día a otro |

**Regla de descarga:** hoy (UTC) se baja el de ayer; si no está, se retrocede día por día (hasta 7).
Lo hace `scripts/cnartys.py`, y `.github/workflows/refresh-tramites.yml` lo corre a diario a las 04:30 UTC.

**Pendiente de verificar:**
- **Fines de semana.** Los archivos de fines de semana pasados ya no se pueden consultar (solo quedan
  2), así que no se sabe si el sábado y el domingo se generan. Se sabrá el 25/10 después de las 02:31
  UTC: si existe `24_10_2026` (sábado), sí. Mientras tanto, el respaldo de 7 días cubre los dos casos.
  Para checarlo: `python3 -I scripts/cnartys.py sondear`.
- **Desde el runner de GitHub (IP de datacenter).** Solo se probó desde IP residencial. Las tiendas de
  apps bloquean IPs de datacenter; si Akamai hace lo mismo aquí, el workflow falla de forma visible
  (no commitea nada) y el refresco se corre en local con `gobmx catalogo --actualizar`.

## Calidad del dato (léase antes de comparar)

- **Cobertura muy desigual por entidad.** CDMX 3,712 registros, Estado de México 3,108; Jalisco 1,
  Baja California 4, Nayarit 17. Mide cuánto captura cada entidad en el catálogo, no cuántos trámites tiene.
- **Municipios:** solo 76 de ~2,470 tienen registros.
- **Canal sin dato:** 1,543 registros no dicen cómo se realiza el trámite (1,532 son de CDMX); para
  esos `en_linea` es `null`, no `false`.
- `informacionadicional` y las columnas de lenguas indígenas vienen vacías. El CSV no trae
  requisitos, costos ni fundamento legal (eso solo está en la ficha web).

## Lo que se intentó el 2026-10-21 (y por qué no encontró nada)

El buscador del portal (`/Buscador/SearchInspeccionResult?...&estadoId=<ID>`, detrás de Akamai, llamado
desde Chrome real) devolvía "No hay información disponible" para los 32 estados. Ese buscador lista
**regulaciones, inspecciones e inspectores**, no trámites ciudadanos, así que su vacío no decía nada sobre
los trámites. El dataset abierto de la SFP (`cnts-federal.json`) es 99% federal. El error fue generalizar
"el buscador no tiene datos subnacionales" a "el dato no existe centralizado".
