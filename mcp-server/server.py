"""MCP server del inventario de presencia digital del gobierno de México.

Expone como tools el mismo inventario que consulta el CLI gobmx.py (apps, sitios,
servicios, subdominios). Solo lectura sobre los JSON/CSV del repo. Filtros, matriz
orden×poder y métricas de `comparar` vienen de gobmx.py (una sola implementación).

Correr:  uv run --no-project --with "mcp<2" python mcp-server/server.py
"""
import csv
import sys
from collections import Counter
from pathlib import Path

from mcp.server.fastmcp import FastMCP

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))
import gobmx  # noqa: E402  (reusa cargas, filtros y métricas del CLI)

mcp = FastMCP("gobmx-inventario")


def _filtra(items, nivel=None, estado=None, sector=None, estatus=None, texto=None, campos=(),
            orden=None, poder=None):
    return [i for i in items if gobmx.coincide(i, nivel, orden, poder, estado, sector, estatus, texto, campos)]


@mcp.tool()
def stats() -> dict:
    """Resumen del inventario: totales, conteos por nivel/sector/estatus y la matriz
    orden×poder de apps, sitios y servicios (todas las celdas, incluidas las de 0), de qué
    búsqueda respalda los ceros y qué parte de los subdominios está asignada a entidad/municipio."""
    a, s, r = gobmx.apps(), gobmx.sitios(), gobmx.resumen()
    return {
        "apps": len(a), "sitios": len(s), "servicios": r["servicios"],
        "tramites_cnartys": r["tramites_cnartys"], "catalogo_corte": r["catalogo_corte"],
        "permisos": len(gobmx.permisos()),
        "apps_por_nivel": dict(Counter((x.get("nivel") or "—") for x in a).most_common()),
        "apps_por_sector": dict(Counter((x.get("sector") or "—") for x in a).most_common()),
        "apps_por_estatus": dict(Counter((x.get("estatus") or "—") for x in a).most_common()),
        "apps_con_android": sum(bool(x.get("android_id")) for x in a),
        "apps_con_ios": sum(bool(x.get("ios_id")) for x in a),
        "apps_orden_poder": gobmx.matriz_orden_poder(a),
        "sitios_orden_poder": gobmx.matriz_orden_poder(s),
        "servicios_orden_poder": r["orden_poder"]["servicios"],
        "cobertura_ceros": r["cobertura_ceros"],
        "subdominios_mapeo": r["subdominios_mapeo"],
    }


@mcp.tool()
def buscar_apps(nivel: str = "", estado: str = "", sector: str = "", estatus: str = "",
                texto: str = "", orden: str = "", poder: str = "", limite: int = 50) -> list:
    """Busca apps. Filtros opcionales: orden (federal/estatal/municipal), poder
    (ejecutivo/legislativo/judicial/autonomo), nivel (campo heredado), estado (entidad),
    sector (seguridad/movilidad/salud/fiscal/identidad...), estatus (activa/retirada/
    'sin actualizar >2 años'), texto (nombre/dependencia/propósito)."""
    r = _filtra(gobmx.apps(), nivel, estado, sector, estatus, texto,
                ("nombre", "dependencia", "siglas", "proposito"), orden, poder)
    return r[:limite]


@mcp.tool()
def buscar_sitios(nivel: str = "", estado: str = "", texto: str = "", orden: str = "", poder: str = "",
                  limite: int = 50) -> list:
    """Busca portales web de gobierno. Filtros: orden, poder, nivel, estado, texto (dependencia/url/propósito)."""
    r = _filtra(gobmx.sitios(), nivel, estado, None, None, texto,
                ("dependencia", "siglas", "url", "proposito"), orden, poder)
    return r[:limite]


@mcp.tool()
def buscar_servicios(texto: str = "", orden: str = "", poder: str = "", estado: str = "", municipio: str = "",
                     en_linea: str = "", min_frecuencia: int = 0, por_frecuencia: bool = False,
                     limite: int = 50) -> list:
    """Busca trámites/servicios de los tres órdenes: ~32k del Catálogo Nacional (CNARTyS: federal, estatal
    y municipal), los federales de la SFP que no están en él, el catálogo curado y las brechas.
    texto busca en nombre/dependencia/siglas/homoclave. Filtros: orden (federal/estatal/municipal), poder,
    estado (entidad; los federales tienen 'Nacional'), municipio, en_linea ('si' | 'no' | 'sin_dato' = el
    catálogo no reporta canal), min_frecuencia (volumen reportado por el CNARTyS); por_frecuencia ordena
    de mayor a menor volumen. Cada trámite CNARTyS trae canales (en_linea/presencial/telefono), frecuencia,
    descripción, fecha_actualizacion, hosts .gob.mx mencionados y, si es federal y casa por homoclave,
    nivel_digitalizacion_sfp."""
    r = _filtra(gobmx.servicios(), estado=estado, texto=texto, orden=orden, poder=poder,
                campos=("servicio", "nombre_tramite", "dependencia", "brecha", "siglas", "homoclave"))
    r = gobmx.filtra_servicios(r, municipio, en_linea, min_frecuencia, por_frecuencia)
    return r[:limite]


@mcp.tool()
def brechas_volumen(orden: str = "", estado: str = "", limite: int = 30) -> list:
    """Brechas con peso: trámites de mayor volumen (campo frecuencia del CNARTyS) que el catálogo declara
    sin canal en línea, cruzados con las apps y sitios de la misma dependencia (por siglas y, si no es
    federal, mismo estado). 'brecha' distingue si la dependencia ya tiene app/sitio (el trámite no se
    ofrece ahí) o no tiene ninguno en el inventario. Filtros: orden, estado."""
    return gobmx.brechas_volumen(orden or None, estado or None, limite)


@mcp.tool()
def catalogo_tramites(sondear: bool = False) -> dict:
    """Metadatos del corte del Catálogo Nacional de trámites cargado: fecha de corte, archivo, sha256,
    conteos por orden/poder, regla de descarga. sondear=true consulta además al servidor (HEAD) qué
    archivos diarios existen en los últimos 7 días (el servidor guarda solo los 2 más recientes; el del
    día D aparece ~02:31 UTC del día D+1). Solo lectura: para refrescar, `gobmx catalogo --actualizar`."""
    out = {"meta": gobmx.catalogo_meta()}
    if sondear:
        sys.path.insert(0, str(ROOT / "scripts"))
        import cnartys  # noqa: E402
        out["disponibles"] = [dict(zip(("fecha", "http", "bytes", "last_modified"), f)) for f in cnartys.sondear()]
    return out


@mcp.tool()
def dependencia(nombre: str) -> dict:
    """Todo lo de una dependencia: sus apps (con permisos si los hay) y sus sitios.
    'nombre' hace match en dependencia o siglas."""
    t = nombre.lower()
    perm = {p.get("android_id"): p for p in gobmx.permisos()}

    def m(x):
        return t in str(x.get("dependencia", "")).lower() or t in str(x.get("siglas", "")).lower()
    aps = []
    for x in gobmx.apps():
        if m(x):
            x = dict(x)
            if x.get("android_id") in perm:
                x["permisos"] = perm[x["android_id"]]
            aps.append(x)
    return {"apps": aps, "sitios": [x for x in gobmx.sitios() if m(x)]}


@mcp.tool()
def permisos(nivel: str = "", estado: str = "", sector: str = "", texto: str = "",
             orden: str = "", poder: str = "", solo_peligrosos: bool = False, limite: int = 50) -> list:
    """Análisis de permisos y privacidad de apps. Cada entrada trae permisos_peligrosos,
    trackers (si hay) y notas. Filtros: orden, poder, nivel, estado (entidad), sector, texto
    (nombre/dependencia/android_id); solo_peligrosos deja solo las que tienen algún permiso peligroso."""
    r = _filtra(gobmx.permisos(), nivel, estado, sector, None, texto,
                ("nombre", "dependencia", "android_id"), orden, poder)
    if solo_peligrosos:
        r = [p for p in r if p.get("permisos_peligrosos")]
    return r[:limite]


@mcp.tool()
def comparar(orden: str, nombres: str = "", ordenar_por: str = "") -> dict:
    """Compara entidades federativas (orden='estatal') o municipios (orden='municipal') con
    métricas normalizadas: apps, apps activas, instalaciones, sitios, subdominios, cobertura de
    poderes y, si hay datos de presupuesto, tasas por 100 mil habitantes y por cada mil millones
    de pesos de egreso, egreso por habitante (INEGI EFIPEM) y transferencias PEF Ramos 28+33
    (complementario; con año y notas, p. ej. CDMX usa el dato trimestral preliminar).
    nombres: lista separada por comas ('Jalisco, Nuevo León'); vacío = todos.
    ordenar_por: ranking descendente por métrica ('apps', 'por_100k_hab.sitios', 'por_mil_mdp.apps',
    'egreso_por_hab', 'instalaciones_por_hab'…); sin dato al final.
    RECHAZA orden='federal': su presupuesto, extensión y población no son comparables."""
    try:
        return gobmx.comparar(orden, nombres, ordenar_por)
    except ValueError as e:
        return {"error": str(e)}


@mcp.tool()
def buscar_subdominio(texto: str = "", solo_activos: bool = False, limite: int = 100) -> list:
    """Busca en los 12k+ subdominios .gob.mx consolidados (CT + dotgob-mx).
    'texto' hace match en el host; 'solo_activos' filtra los marcados activos por dotgob-mx.
    Cada fila trae además estado/municipio cuando el slug de dominio está mapeado (si no, null)."""
    f = ROOT / "sitios/subdominios-consolidado.csv"
    if not f.exists():
        return []
    out = []
    with f.open() as fh:
        for row in csv.DictReader(fh):
            if texto and texto.lower() not in row["host"].lower():
                continue
            if solo_activos and not str(row.get("activo_dotgob", "")).startswith("activo"):
                continue
            lug = gobmx.lugar_de_slug(row.get("dependencia_o_estado") or "")
            row["estado"], row["municipio"] = lug if lug else (None, None)
            out.append(row)
            if len(out) >= limite:
                break
    return out


if __name__ == "__main__":
    mcp.run()
