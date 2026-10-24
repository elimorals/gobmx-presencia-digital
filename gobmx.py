#!/usr/bin/env python3
"""gobmx — consulta el inventario de presencia digital del gobierno de México.

uso:
  gobmx apps [--nivel N] [--orden O] [--poder P] [--estado E] [--sector S] [--estatus T] [--busca TXT] [--json]
  gobmx sitios [--nivel N] [--orden O] [--poder P] [--estado E] [--busca TXT] [--json]
  gobmx servicios [--orden O] [--poder P] [--estado E] [--municipio M] [--busca TXT]
                  [--en-linea si|no|sin_dato] [--min-frecuencia N] [--por-frecuencia] [--limite N] [--json]
  gobmx brechas [--orden O] [--estado E] [--limite N] [--json]   # trámites de alto volumen sin canal en línea
  gobmx catalogo [--sondear] [--actualizar] [--csv RUTA]         # corte del CNARTyS; qué días hay; refrescar
  gobmx permisos [--busca TXT] [--json]   # permisos peligrosos y trackers por app
  gobmx stats                      # resumen + matrices orden×poder (apps, sitios, servicios)
  gobmx dep "<dependencia>"        # todo (apps+sitios) de una dependencia
  gobmx comparar --orden estatal|municipal [--nombres "Jalisco,Nuevo León"] [--ordenar por_100k_hab.apps] [--json]

  orden: federal | estatal | municipal      poder: ejecutivo | legislativo | judicial | autonomo
  `comparar` solo compara estatal↔estatal o municipal↔municipal; rechaza federal.

Lee los JSON junto a este archivo. Sin dependencias externas. Este módulo es también la
única implementación de las métricas por entidad/municipio (la usan el MCP y el build).
"""
import argparse
import csv
import json
import sys
import unicodedata
from collections import Counter
from functools import lru_cache
from pathlib import Path

RAIZ = Path(__file__).resolve().parent

ORDENES = ("federal", "estatal", "municipal")
PODERES = ("ejecutivo", "legislativo", "judicial", "autonomo")
# nombres exactamente como en apps.json (con acentos) → clave INEGI
ENTIDADES = {
    "Aguascalientes": "01", "Baja California": "02", "Baja California Sur": "03", "Campeche": "04",
    "Coahuila": "05", "Colima": "06", "Chiapas": "07", "Chihuahua": "08", "Ciudad de México": "09",
    "Durango": "10", "Guanajuato": "11", "Guerrero": "12", "Hidalgo": "13", "Jalisco": "14",
    "Estado de México": "15", "Michoacán": "16", "Morelos": "17", "Nayarit": "18", "Nuevo León": "19",
    "Oaxaca": "20", "Puebla": "21", "Querétaro": "22", "Quintana Roo": "23", "San Luis Potosí": "24",
    "Sinaloa": "25", "Sonora": "26", "Tabasco": "27", "Tamaulipas": "28", "Tlaxcala": "29",
    "Veracruz": "30", "Yucatán": "31", "Zacatecas": "32",
}
MSG_FEDERAL = ("No se compara el orden federal: su presupuesto, su extensión territorial y la población "
               "que atiende (todo el país) no son comparables con los de una entidad o un municipio. "
               "Usa orden='estatal' (entidad↔entidad) u orden='municipal' (municipio↔municipio).")


def sin_acentos(s):
    return "".join(c for c in unicodedata.normalize("NFD", str(s or "")) if unicodedata.category(c) != "Mn").lower()


def cargar(sub):
    out = []
    d = RAIZ / sub
    for f in sorted(d.glob("*.json")):
        try:
            data = json.loads(f.read_text())
            out += data if isinstance(data, list) else [data]
        except (ValueError, OSError):
            pass
    return out


def apps():
    # apps.json consolidado si existe; si no, los segmentos
    j = RAIZ / "apps/apps.json"
    return json.loads(j.read_text()) if j.exists() else cargar("apps/inventario")


def sitios():
    return cargar("sitios")


@lru_cache(maxsize=1)
def tramites():
    """Trámites del CNARTyS (federal + estatal + municipal), normalizados por scripts/cnartys.py."""
    f = RAIZ / "servicios/cnartys/tramites.json"
    return json.loads(f.read_text()) if f.exists() else []


def catalogo_meta():
    f = RAIZ / "servicios/cnartys/meta.json"
    return json.loads(f.read_text()) if f.exists() else {}


def servicios(cnartys=True):
    """Servicios con orden/poder: brechas + catálogo curado + cnts-federal (SFP) + CNARTyS.
    cnts-federal.json (6k trámites) no trae los campos en disco: se clasifican al cargar con el mismo
    clasificador que el resto (scripts/clasificar_orden_poder.py). Con cnartys=True se agregan los
    trámites del CNARTyS y se quitan de cnts los ya ligados a uno de ellos (`homoclave_sfp`: por homoclave
    o por nombre+siglas único; el registro CNARTyS trae `nivel_digitalizacion_sfp`). Los cnts que quedan
    y comparten nombre+siglas con un federal del CNARTyS sin poder ligarse 1:1 llevan
    `posible_duplicado_cnartys: true` (no se quitan: el cruce es ambiguo)."""
    items = cargar("servicios")
    if cnartys and tramites():
        sys.path.insert(0, str(RAIZ / "scripts"))
        from cnartys import llave_nombre  # noqa: E402
        ligados = {t["homoclave_sfp"] for t in tramites() if "homoclave_sfp" in t}
        llaves = {llave_nombre(t["nombre_tramite"], t.get("siglas")) for t in tramites() if t["orden"] == "federal"}
        items = [s for s in items if not ("_id" in s and s.get("homoclave", "").strip() in ligados)]
        for s in items:
            if "_id" in s and llave_nombre(s.get("nombre_tramite"), s.get("siglas")) in llaves:
                s["posible_duplicado_cnartys"] = True
    if any("orden" not in s for s in items):
        sys.path.insert(0, str(RAIZ / "scripts"))
        from clasificar_orden_poder import clasificar  # noqa: E402
        for s in items:
            if "orden" not in s:
                s.setdefault("nivel", "federal")
                s.setdefault("estado", "Nacional")
                s.update(clasificar(s))
    return items + (tramites() if cnartys else [])


def filtra_servicios(items, municipio=None, en_linea=None, min_frecuencia=None, por_frecuencia=False):
    """Filtros propios de trámites (CLI y MCP). en_linea: 'si' | 'no' | 'sin_dato' (canal no reportado)."""
    if municipio:
        m = sin_acentos(municipio)
        items = [i for i in items if m in sin_acentos(i.get("municipio"))]
    if en_linea:
        quiere = {"si": True, "no": False, "sin_dato": None}[sin_acentos(en_linea).replace("-", "_")]
        items = [i for i in items if "en_linea" in i and i["en_linea"] is quiere]
    if min_frecuencia:
        items = [i for i in items if (i.get("frecuencia") or 0) >= min_frecuencia]
    if por_frecuencia:
        items = sorted(items, key=lambda i: -(i.get("frecuencia") or -1))
    return items


def permisos():
    """Permisos por app, con orden/poder/subtipo/municipio de la app (join por android_id)."""
    j = RAIZ / "apps/permisos/permisos.json"
    if not j.exists():
        return []
    por_id = {a.get("android_id"): a for a in apps() if a.get("android_id")}
    out = json.loads(j.read_text())
    for p in out:
        a = por_id.get(p.get("android_id"), {})
        for k in ("orden", "poder", "subtipo", "municipio"):
            p.setdefault(k, a.get(k))
    return out


def coincide(i, nivel=None, orden=None, poder=None, estado=None, sector=None, estatus=None,
             texto=None, campos=()):
    """Filtro común CLI/MCP. nivel/estado/sector/estatus: substring; orden/poder: exacto."""
    for k, v in (("nivel", nivel), ("estado", estado), ("sector", sector), ("estatus", estatus)):
        if v and v.lower() not in str(i.get(k, "")).lower():
            return False
    for k, v in (("orden", orden), ("poder", poder)):
        if v and sin_acentos(v) != sin_acentos(i.get(k)):
            return False
    if texto:
        blob = " ".join(str(i.get(c, "")) for c in campos).lower()
        if texto.lower() not in blob:
            return False
    return True


def matriz_orden_poder(items):
    """Conteo orden×poder con TODAS las celdas (incluye 0). La fila aparte cuenta no_oficial (null)."""
    c = Counter((i.get("orden"), i.get("poder")) for i in items)
    m = {o: {p: c.get((o, p), 0) for p in PODERES} for o in ORDENES}
    m["sin_orden(no_oficial)"] = {"null": sum(v for (o, _), v in c.items() if o is None)}
    return m


# ---------- métricas por entidad / municipio (única implementación) ----------

@lru_cache(maxsize=1)
def _slugs():
    f = RAIZ / "sitios/subdominios/slugs.json"
    return json.loads(f.read_text()) if f.exists() else {"estados": {}, "municipios": {}}


def lugar_de_slug(slug):
    """slug de dominio (dependencia_o_estado del CSV) → (estado, municipio|None), o None si no asignado."""
    s = _slugs()
    if slug in s["estados"]:
        return s["estados"][slug], None
    if slug in s["municipios"]:
        e, m = s["municipios"][slug]
        return e, m
    return None


@lru_cache(maxsize=1)
def subdominios_por_lugar():
    """({(estado, municipio|None): [hosts, activos]}, cobertura del mapeo)."""
    f = RAIZ / "sitios/subdominios-consolidado.csv"
    por, total, asignados, a_mun = {}, 0, 0, 0
    if f.exists():
        with f.open() as fh:
            for row in csv.DictReader(fh):
                total += 1
                lug = lugar_de_slug(row.get("dependencia_o_estado") or "")
                if not lug:
                    continue
                asignados += 1
                a_mun += lug[1] is not None
                c = por.setdefault(lug, [0, 0])
                c[0] += 1
                c[1] += str(row.get("activo_dotgob", "")).startswith("activo")
    return por, {"hosts": total, "asignados": asignados, "a_entidad": asignados - a_mun, "a_municipio": a_mun,
                 "no_asignados": total - asignados,
                 "pct_asignado": round(100 * asignados / total, 1) if total else None,
                 "slugs_mapeados": len(_slugs()["estados"]) + len(_slugs()["municipios"])}


def presupuesto(tipo):
    """datos/presupuesto/{entidades|municipios}.json indexado por (estado, municipio|None). {} si no existe."""
    f = RAIZ / f"datos/presupuesto/{tipo}.json"
    if not f.exists():
        return {}
    try:
        filas = json.loads(f.read_text())
    except ValueError:
        return {}
    return {(r.get("estado"), r.get("municipio") if tipo == "municipios" else None): r for r in filas}


def _tasa(x, base, escala):
    return round(x / base * escala, 3) if base else None


def _tramites_canal(trs):
    """Conteos de canal: el % en línea se calcula solo sobre los que reportan canal (null si ninguno)."""
    con = [t for t in trs if t.get("en_linea") is not None]
    en = sum(1 for t in con if t["en_linea"])
    return {"tramites_en_linea": en, "tramites_sin_dato_canal": len(trs) - len(con),
            "pct_tramites_en_linea": round(100 * en / len(con), 1) if con else None}


def _fila(clave, aps, sts, subs, pres, trs=()):
    estado, municipio = clave
    pob = pres.get("poblacion_2020")
    eg = pres.get("egresos_inegi") or {}
    pef = pres.get("transferencias_pef") or {}
    egresos = eg.get("total_mxn")
    r28, r33 = pef.get("ramo28_mxn"), pef.get("ramo33_mxn")
    poderes = {p: sum(1 for x in aps + sts if x.get("poder") == p) for p in PODERES}
    base = {"apps": len(aps), "apps_activas": sum(1 for a in aps if a.get("estatus") == "activa"),
            "sitios": len(sts), "subdominios": subs[0], "tramites": len(trs)}
    inst = sum(((a.get("play") or {}).get("realInstalls") or 0) for a in aps)
    fila = {"estado": estado, "cve_ent": pres.get("cve_ent") or ENTIDADES.get(estado)}
    if municipio is not None:
        fila.update(municipio=municipio, cve_mun=pres.get("cve_mun"))
    fila.update({
        **base, **_tramites_canal(trs), "instalaciones": inst, "subdominios_activos": subs[1],
        "poderes": poderes, "cobertura_poderes": sum(1 for v in poderes.values() if v),
        "poblacion_2020": pob, "egresos_mxn": egresos, "egresos_anio": eg.get("anio"),
        "egreso_por_hab": _tasa(egresos, pob, 1) if egresos and pob else None,
        "transferencias_pef_mxn": r28 + r33 if r28 is not None and r33 is not None else None,
        "transferencias_pef_anio": pef.get("anio"),
        "presupuesto_notas": pres.get("notas"),
        "por_100k_hab": {k: _tasa(v, pob, 1e5) for k, v in base.items()} if pob else None,
        "instalaciones_por_hab": _tasa(inst, pob, 1) if pob else None,
        "por_mil_mdp": {k: _tasa(v, egresos, 1e9) for k, v in base.items()} if egresos else None,
    })
    return fila


def metricas(orden, _apps=None, _sitios=None, _tramites=None):
    """Filas comparables de un orden: 'estatal' (las 32 entidades) o 'municipal' (municipios con
    algún registro, subdominio mapeado o fila de presupuesto). ValueError para federal/nacional."""
    o = sin_acentos(orden)
    if o not in ("estatal", "municipal"):
        raise ValueError(MSG_FEDERAL if o in ("federal", "nacional") else f"orden inválido '{orden}'. {MSG_FEDERAL}")
    mun = o == "municipal"

    def clave(x):
        return (x.get("estado"), x.get("municipio") if mun else None)

    aps = [a for a in (_apps if _apps is not None else apps()) if a.get("orden") == o]
    sts = [s for s in (_sitios if _sitios is not None else sitios()) if s.get("orden") == o]
    trs = [t for t in (_tramites if _tramites is not None else tramites()) if t.get("orden") == o]
    subs, _ = subdominios_por_lugar()
    pres = presupuesto("municipios" if mun else "entidades")
    if mun:
        claves = ({clave(x) for x in aps + sts + trs if x.get("municipio")} | {k for k in subs if k[1]}
                  | set(pres))
    else:
        claves = {(e, None) for e in ENTIDADES}
    por_lugar = {}
    for t in trs:
        por_lugar.setdefault(clave(t), []).append(t)
    filas = [_fila(k, [a for a in aps if clave(a) == k], [s for s in sts if clave(s) == k],
                   subs.get(k, [0, 0]), pres.get(k, {}), por_lugar.get(k, [])) for k in claves]
    return sorted(filas, key=lambda f: (f["estado"] or "", f.get("municipio") or ""))


METODOLOGIA = ("Solo estatal↔estatal o municipal↔municipal (nunca federal). Tasas por 100 mil habitantes "
               "(Censo 2020 INEGI) y por cada mil millones de pesos de egreso (INEGI EFIPEM). null = dato no "
               "disponible, nunca cero. cobertura_poderes = poderes (de 4) con ≥1 app o sitio del orden comparado. "
               "tramites = registros del CNARTyS de ese orden y lugar: mide cuánto captura cada entidad en el "
               "catálogo, no cuántos trámites tiene (Jalisco publica 1). pct_tramites_en_linea se calcula solo "
               "sobre los que reportan canal; tramites_sin_dato_canal dice cuántos no lo reportan.")


def valor(fila, metrica):
    """Métrica por ruta con punto: 'apps', 'por_100k_hab.sitios', 'por_mil_mdp.apps'… (None si falta)."""
    v = fila
    for k in metrica.split("."):
        v = v.get(k) if isinstance(v, dict) else None
    return v


def comparar(orden, nombres=None, ordenar_por=None):
    """Compara entidades (orden estatal) o municipios (orden municipal). nombres: lista o texto
    separado por comas (match sin acentos en estado/municipio); vacío = todos. ordenar_por: ruta de
    métrica (ver `valor`) para un ranking descendente, sin dato al final. ValueError si federal."""
    filas = metricas(orden)
    if isinstance(nombres, str):
        nombres = [n.strip() for n in nombres.split(",") if n.strip()]
    if nombres:
        ns = [sin_acentos(n) for n in nombres]
        filas = [f for f in filas if any(n in sin_acentos(f.get("municipio") or f["estado"]) for n in ns)]
    if ordenar_por:
        filas = sorted(filas, key=lambda f: (valor(f, ordenar_por) is None, -(valor(f, ordenar_por) or 0)))
    o = sin_acentos(orden)
    fed = [t for t in tramites() if t.get("orden") == "federal"]
    return {"orden": o, "metodologia": METODOLOGIA, "ordenado_por": ordenar_por or None,
            "referencia_federal": {"tramites": len(fed), **_tramites_canal(fed)},
            "catalogo_corte": catalogo_meta().get("fecha_corte"),
            "presupuesto_disponible": bool(presupuesto("municipios" if o == "municipal" else "entidades")),
            "filas": filas}


def brechas_volumen(orden=None, estado=None, limite=30):
    """Trámites de mayor `frecuencia` que el catálogo declara SIN canal en línea (en_linea=false; los sin
    dato de canal no entran), cruzados con apps y sitios de la misma dependencia (siglas; en estatal y
    municipal además el mismo estado). `frecuencia` es el volumen que reporta el CNARTyS; el catálogo no
    documenta su periodo."""
    # ponytail: cruce por siglas exactas; dependencias sin siglas o con siglas distintas en el inventario no casan
    def llave(x):
        sig = (x.get("siglas") or "").strip().upper()
        return (sig, None if x.get("orden") == "federal" else x.get("estado")) if sig else None
    aps, sts = {}, {}
    for a in apps():
        aps.setdefault(llave(a), []).append(a.get("nombre"))
    for s in sitios():
        sts.setdefault(llave(s), []).append(s.get("url"))
    out = []
    for t in filtra_servicios([t for t in tramites() if t.get("en_linea") is False and t.get("frecuencia")],
                              por_frecuencia=True):
        if (orden and t["orden"] != sin_acentos(orden)) or (estado and sin_acentos(estado) not in sin_acentos(t["estado"])):
            continue
        k = llave(t)
        a, s = (aps.get(k, []), sts.get(k, [])) if k else ([], [])
        out.append({c: t.get(c) for c in ("homoclave", "nombre_tramite", "dependencia", "siglas", "orden",
                                          "estado", "municipio", "frecuencia", "canales_txt")}
                   | {"apps_dependencia": a, "sitios_dependencia": s,
                      "brecha": ("la dependencia ya tiene app o sitio, pero este trámite no se ofrece en línea"
                                 if a or s else "sin canal en línea y sin app ni sitio de la dependencia en el inventario")})
        if len(out) >= limite:
            break
    return out


COBERTURA_CEROS = ("Los ceros de la matriz de apps están respaldados por una búsqueda dirigida en Google Play "
                   "y App Store (apps/inventario/BUSQUEDA-PODERES.md); los de sitios, por sitios/poderes-autonomos.json.")


def resumen():
    """Totales + matrices orden×poder de apps, sitios y servicios (celdas en 0 incluidas)."""
    a, s, sv = apps(), sitios(), servicios()
    return {"apps": len(a), "sitios": len(s), "servicios": len(sv), "permisos": len(permisos()),
            "tramites_cnartys": len(tramites()), "catalogo_corte": catalogo_meta().get("fecha_corte"),
            "orden_poder": {"apps": matriz_orden_poder(a), "sitios": matriz_orden_poder(s),
                            "servicios": matriz_orden_poder(sv)},
            "cobertura_ceros": COBERTURA_CEROS, "subdominios_mapeo": subdominios_por_lugar()[1]}


# ---------- CLI ----------

def filtra(items, args, campos_busca):
    return [i for i in items if coincide(
        i, getattr(args, "nivel", None), getattr(args, "orden", None), getattr(args, "poder", None),
        getattr(args, "estado", None), getattr(args, "sector", None), getattr(args, "estatus", None),
        args.busca, campos_busca)]


def imprime(items, args, cols):
    if args.json:
        print(json.dumps(items, ensure_ascii=False, indent=1))
        return
    print(f"{len(items)} resultados\n")
    for i in items:
        linea = "  ".join("no" if i.get(c) is False else str(i.get(c) if i.get(c) not in (None, "") else "·")
                          for c in cols)
        print("•", linea)


def cmd_apps(args):
    items = filtra(apps(), args, ["nombre", "dependencia", "siglas", "proposito"])
    imprime(items, args, ["nombre", "siglas", "orden", "poder", "estado", "sector", "estatus", "android_id"])


def cmd_sitios(args):
    items = filtra(sitios(), args, ["dependencia", "siglas", "url", "proposito"])
    imprime(items, args, ["siglas", "dependencia", "orden", "poder", "estado", "url"])


def cmd_servicios(args):
    items = filtra(servicios(), args, ["servicio", "nombre_tramite", "dependencia", "siglas", "homoclave", "brecha"])
    items = filtra_servicios(items, args.municipio, args.en_linea, args.min_frecuencia, args.por_frecuencia)
    imprime(items[:args.limite] if args.limite else items, args,
            ["servicio", "nombre_tramite", "homoclave", "orden", "poder", "estado", "municipio", "en_linea",
             "frecuencia", "brecha"])


def cmd_brechas(args):
    items = brechas_volumen(args.orden, args.estado, args.limite)
    if args.json:
        print(json.dumps(items, ensure_ascii=False, indent=1))
        return
    print(f"{len(items)} trámites de mayor volumen sin canal en línea (CNARTyS, corte "
          f"{catalogo_meta().get('fecha_corte')})\n")
    for b in items:
        lugar = b["municipio"] or b["estado"]
        canales = b["apps_dependencia"] + b["sitios_dependencia"]
        print(f"• {b['frecuencia']:>12,}  {b['siglas'] or '·'} ({lugar}) — {b['nombre_tramite'][:80]}")
        print(f"      {b['brecha']}" + (f": {', '.join(map(str, canales[:3]))}" if canales else ""))


def cmd_catalogo(args):
    sys.path.insert(0, str(RAIZ / "scripts"))
    import cnartys  # noqa: E402  (red solo con --sondear / --actualizar)
    if args.actualizar or args.csv:
        tramites.cache_clear()
        print(json.dumps(cnartys.actualizar(args.csv), ensure_ascii=False, indent=1))
        return 0
    print(json.dumps(catalogo_meta() or {"error": "sin servicios/cnartys/meta.json: corre gobmx catalogo --actualizar"},
                     ensure_ascii=False, indent=1))
    if args.sondear:
        print("\ndisponibles hoy en el servidor (fecha, HTTP, bytes, last-modified):")
        for fila in cnartys.sondear():
            print(" ", *fila)
    return 0


def cmd_permisos(args):
    items = filtra(permisos(), args, ["nombre", "dependencia", "android_id"])
    if args.json:
        print(json.dumps(items, ensure_ascii=False, indent=1))
        return
    print(f"{len(items)} apps con análisis de permisos\n")
    for p in items:
        peligro = p.get("permisos_peligrosos") or []
        trk = p.get("trackers") or []
        print(f"• {p.get('nombre')} ({p.get('sector') or '·'}) — {len(peligro)} permisos peligrosos"
              f"{', ' + str(len(trk)) + ' trackers' if trk else ''}")
        if peligro:
            print("    ", ", ".join(peligro))


def _imprime_matriz(titulo, m):
    print(f"\n{titulo} (orden × poder)")
    print(f"  {'':<12}" + "".join(f"{p:>12}" for p in PODERES))
    for o in ORDENES:
        print(f"  {o:<12}" + "".join(f"{m[o][p]:>12}" for p in PODERES))
    print(f"  no_oficial (orden/poder null): {m['sin_orden(no_oficial)']['null']}")


def cmd_stats(args):
    a, s = apps(), sitios()
    print(f"APPS: {len(a)}  |  SITIOS: {len(s)}  |  SERVICIOS: {len(servicios())}"
          f" (CNARTyS {len(tramites())}, corte {catalogo_meta().get('fecha_corte')})  |  PERMISOS: {len(permisos())}\n")
    for campo in ("nivel", "sector", "estatus"):
        c = Counter((x.get(campo) or "—") for x in a)
        print(f"apps por {campo}:", dict(c.most_common()))
    print("apps con Android:", sum(bool(x.get("android_id")) for x in a),
          "| con iOS:", sum(bool(x.get("ios_id")) for x in a))
    r = resumen()
    for k, m in r["orden_poder"].items():
        _imprime_matriz(k.upper(), m)
    print("\n" + r["cobertura_ceros"])
    sm = r["subdominios_mapeo"]
    print(f"subdominios asignados a entidad/municipio: {sm['asignados']} de {sm['hosts']} ({sm['pct_asignado']} %)")


def cmd_dep(args):
    t = args.texto.lower()
    a = [x for x in apps() if t in str(x.get("dependencia", "")).lower() or t in str(x.get("siglas", "")).lower()]
    s = [x for x in sitios() if t in str(x.get("dependencia", "")).lower() or t in str(x.get("siglas", "")).lower()]
    print(f"== {args.texto} ==\nApps ({len(a)}):")
    for x in a:
        print("  •", x.get("nombre"), "—", x.get("android_id") or x.get("ios_id") or "sin ID")
    print(f"Sitios ({len(s)}):")
    for x in s:
        print("  •", x.get("url"), "—", x.get("tipo"))


def cmd_comparar(args):
    try:
        r = comparar(args.orden, args.nombres, args.ordenar)
    except ValueError as e:
        print(f"error: {e}", file=sys.stderr)
        return 2
    if args.json:
        print(json.dumps(r, ensure_ascii=False, indent=1))
        return 0
    print(r["metodologia"], "\n")
    if not r["presupuesto_disponible"]:
        print("(sin datos/presupuesto/*.json: las tasas salen null)\n")
    rf = r["referencia_federal"]
    print(f"referencia federal (no comparable, solo contexto): {rf['tramites']} trámites, "
          f"{rf['pct_tramites_en_linea']} % en línea\n")
    cols =["apps", "apps_activas", "instalaciones", "sitios", "subdominios", "cobertura_poderes",
            "tramites", "pct_tramites_en_linea", "por_100k_hab.apps", "por_mil_mdp.apps", "egreso_por_hab"]
    if args.ordenar and args.ordenar not in cols:
        cols.append(args.ordenar)
    print(f"{'lugar':<34}" + "".join(f"{c.replace('por_100k_hab.', '/100k ').replace('por_mil_mdp.', '/mmdp ')[:12]:>13}"
                                       for c in cols))
    notas = []
    for f in r["filas"]:
        nom = f"{f['municipio']} ({f['estado']})" if f.get("municipio") else f["estado"]
        vals = [valor(f, c) for c in cols]
        print(f"{nom[:33]:<34}" + "".join(f"{'s/d' if v is None else v:>13}" for v in vals))
        if f.get("presupuesto_notas"):
            notas.append(f"  · {nom}: {f['presupuesto_notas']}")
    if notas:
        print("\nnotas de presupuesto:\n" + "\n".join(notas))
    return 0


def main(argv=None):
    p = argparse.ArgumentParser(prog="gobmx", description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    sub = p.add_subparsers(dest="cmd", required=True)
    ayudas = {"apps": "apps móviles de gobierno", "sitios": "portales web de gobierno",
              "servicios": "trámites y servicios (CNARTyS + SFP + brechas)", "permisos": "permisos peligrosos y rastreadores por app"}
    for nombre, fn in [("apps", cmd_apps), ("sitios", cmd_sitios), ("servicios", cmd_servicios), ("permisos", cmd_permisos)]:
        sp = sub.add_parser(nombre, help=ayudas[nombre])
        sp.add_argument("--nivel", help="campo heredado (federal, estatal, municipal, paraestatal, autonomo…); mejor usa --orden/--poder")
        sp.add_argument("--estado", help="entidad, coincidencia parcial sin mayúsculas (los federales tienen 'Nacional')")
        sp.add_argument("--sector", help="sector de la app o servicio (seguridad, movilidad, salud, fiscal…), parcial")
        sp.add_argument("--orden", choices=ORDENES, help="orden de gobierno (exacto)")
        sp.add_argument("--poder", choices=PODERES, help="poder (exacto)")
        sp.add_argument("--estatus", help="apps: activa | retirada | 'sin actualizar >2 años' | desconocido (parcial)")
        sp.add_argument("--busca", help="texto libre en nombre, dependencia, siglas…")
        sp.add_argument("--json", action="store_true", help="salida JSON completa en vez de una línea por registro")
        if nombre == "servicios":
            sp.add_argument("--municipio", help="municipio, coincidencia parcial sin acentos")
            sp.add_argument("--en-linea", choices=("si", "no", "sin_dato"),
                            help="canal en línea; sin_dato = el catálogo no reporta canal (no es 'no')")
            sp.add_argument("--min-frecuencia", type=int, help="solo trámites con al menos N solicitudes reportadas")
            sp.add_argument("--por-frecuencia", action="store_true", help="ordena por frecuencia descendente")
            sp.add_argument("--limite", type=int, help="muestra solo los primeros N")
        sp.set_defaults(fn=fn)
    bp = sub.add_parser("brechas", help="trámites de alto volumen sin canal en línea, cruzados con apps y sitios")
    bp.add_argument("--orden", choices=ORDENES); bp.add_argument("--estado")
    bp.add_argument("--limite", type=int, default=30); bp.add_argument("--json", action="store_true")
    bp.set_defaults(fn=cmd_brechas)
    kp = sub.add_parser("catalogo", help="corte del CNARTyS; --sondear: fechas en el servidor; --actualizar: bajar y normalizar")
    kp.add_argument("--sondear", action="store_true", help="consulta al servidor (HEAD) qué archivos diarios hay en los últimos 7 días")
    kp.add_argument("--actualizar", action="store_true", help="baja el de ayer (o el más reciente) y lo normaliza en servicios/cnartys/")
    kp.add_argument("--csv", help="normaliza un CSV ya descargado en vez de bajarlo")
    kp.set_defaults(fn=cmd_catalogo)
    sub.add_parser("stats", help="resumen y matrices orden × poder").set_defaults(fn=cmd_stats)
    dp = sub.add_parser("dep", help="apps y sitios de una dependencia")
    dp.add_argument("texto", help="nombre o siglas de la dependencia (parcial), p. ej. IMSS"); dp.set_defaults(fn=cmd_dep)
    cp = sub.add_parser("comparar", help="compara entidades (estatal) o municipios (municipal); rechaza federal")
    cp.add_argument("--orden", required=True, help="estatal | municipal (federal se rechaza)")
    cp.add_argument("--nombres", help="lista separada por comas; vacío = todos")
    cp.add_argument("--ordenar", help="ranking por métrica: apps, sitios, por_100k_hab.apps, por_mil_mdp.sitios, "
                                      "egreso_por_hab, instalaciones_por_hab…")
    cp.add_argument("--json", action="store_true")
    cp.set_defaults(fn=cmd_comparar)
    args = p.parse_args(argv)
    return args.fn(args)


if __name__ == "__main__":
    sys.exit(main())
