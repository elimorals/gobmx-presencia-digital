# 07 · Recursos web y esquema de datos

## Estructura `assets/public/`

La SPA Ionic/Angular completa (ver doc 02 para el árbol). Puntos de interés:

- `assets/seed.sql` — esquema inicial de `sct.db` (ver doc 05). Se carga en runtime con
  `this.http.get('assets/seed.sql')` y se importa vía `sqliteporter`.
- `pages-*-module-es*.js` — una pantalla por módulo lazy; confirman los flujos y los **dos tipos de
  licencia**: **Transportista** y **Ferroviario** (`artf`).
- `svg/`, `assets/{img,icon,fonts}` — recursos visuales (plantillas de la licencia, íconos).

## Tipos de licencia (de los módulos)

| Tipo | Módulos |
|---|---|
| **Transportista** | `pages-licenses-pages-Transportista-license-detail`, `...-license-scan-details` |
| **Ferroviario** (`artf`) | `pages-licenses-pages-Ferroviario-license-detail-artf`, `...-license-scan-details-artf` |

`license-scan-details` + el barcodescanner sugieren un flujo de **verificación por escaneo** del
código de otra licencia (p. ej. inspector escanea la del conductor). No se afirma el contenido del
código escaneado (requeriría dinámica).

## Esquema de la DB (`seed.sql`)

Cuatro tablas: `user`, `licenses`, `remarks`, `media` (detalle y PII en doc 05).

**Defecto de integridad (menor):** las claves foráneas de `remarks` y `media` referencian
`licences(id)` (con "c"), pero la tabla se llama `licenses` (con "s"). La FK apunta a una tabla
inexistente; en SQLite las FK no se aplican salvo `PRAGMA foreign_keys=ON`, así que probablemente
pasa inadvertido, pero es un error de esquema.

## Notas

- Comentarios al final de `seed.sql` dejan SQL muerto: `/*DELETE FROM user,*/`,
  `/*INSERT or IGNORE INTO screen...*/` — restos de desarrollo, sin efecto.
