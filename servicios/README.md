# servicios/

Análisis de **trámites y servicios** y por qué canal se pueden hacer (web / app / presencial / WhatsApp).

- `cnartys/` — **Catálogo Nacional de Regulaciones, Trámites y Servicios** (CONAMER), los tres órdenes:
  31,802 trámites del corte 2026-10-23. Lo genera `scripts/cnartys.py` desde el CSV público diario;
  regla de descarga, evidencia y calidad del dato en [`harvest-subnacional.md`](harvest-subnacional.md).
- `cnts-federal.json` — dataset abierto de la SFP: 6,125 trámites federales con `nivel_digitalizacion`
  (cómo se obtuvo: [`FUENTE-CONAMER.md`](FUENTE-CONAMER.md)). 2,133 se ligan con un trámite del CNARTyS
  (1,976 por homoclave, 157 por nombre+siglas único en ambos lados, renombres como `SCT-` → `SICT-`) y
  no se repiten en `servicios()`. Quedan 3,992; 146 de ellos llevan `posible_duplicado_cnartys: true`
  (mismo nombre y siglas que un federal del CNARTyS, pero el cruce no es 1:1).

## Sin duplicados

- El CNARTyS publica 364 registros repetidos (misma homoclave y mismo lugar, versión vieja + nueva).
  `scripts/cnartys.py` deja la más reciente y guarda los ids viejos en `ids_anteriores`: 32,166 → 31,802.
- Total en `gobmx.servicios()`: 31,802 CNARTyS + 3,992 SFP + 9 curados + 7 brechas = **35,810**. Los 9
  curados son una capa de detalle (canales, URL) sobre trámites que ya están; no son trámites adicionales.
- `catalogo-conamer.json` — muestra curada de trámites federales de alto uso, con canales y URL.
- `brechas.json` — brechas cualitativas al cruzar apps + sitios. Las brechas con volumen salen en vivo
  de `gobmx.py brechas` / tool MCP `brechas_volumen`.
- El análisis en prosa vive en [`../analisis/comparacion.md`](../analisis/comparacion.md) §5.

## Esquema de `cnartys/tramites.json`

Un objeto por línea, ordenados por `id`. Campos siempre presentes:

| campo | de la columna del CSV | nota |
|---|---|---|
| `id`, `homoclave`, `nombre_tramite` | `id`, `homoclave`, `nombre` | texto con espacios colapsados |
| `tipo_registro` | `tramiteServicio` | `tramite` · `servicio` · null |
| `tipo`, `categoria`, `dependencia`, `siglas`, `unidad_administrativa` | ídem (`acronimoDependencia` → `siglas`) | |
| `orden` (= `nivel`) | `nivelGobierno` | `federal` · `estatal` · `municipal` |
| `poder` | `nivelGobiernoDetalle` | Administración Pública / Municipio → `ejecutivo`; Poder Judicial → `judicial`; Poder Legislativo → `legislativo`; Autonomía Constitucional y tribunales contenciosos → `autonomo`. Un valor nuevo hace fallar el script a propósito |
| `subtipo` | — | del clasificador del repo (paraestatal, desconcentrado…) solo para ejecutivo |
| `estado` | `estado` | nombre corto del repo (`Veracruz`, `Michoacán`, `Coahuila`); federal = `Nacional` |
| `municipio` | `municipio` | solo si es municipal; `Playa del Carmen` → `Solidaridad` (INEGI) |
| `en_linea`, `presencial`, `telefono` | `opcionRealizarTramite` | booleanos; **null = el catálogo no reporta canal** |
| `canales_txt` | `opcionRealizarTramite` | texto original |
| `frecuencia` | `frecuencia` | volumen que reporta el catálogo (no documenta el periodo); null si vacío |
| `dirigido`, `fecha_actualizacion` (ISO), `descripcion_ciudadana`, `descripcion` | ídem | |

Opcionales (solo si traen dato): `modalidad`, `tags`, `programa_social`, `oculto`, `scian`
(`sector`…`clase`), `hosts` (`.gob.mx` citados en los textos, sin correos), `nivel_digitalizacion_sfp`
(federales que casan por homoclave con `cnts-federal.json`).
