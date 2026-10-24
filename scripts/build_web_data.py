"""Empaqueta el inventario para la web de Vercel (webapp/data/).

Las métricas salen de gobmx.py (misma implementación que CLI, MCP y dashboard viejo). Escribe:
  inventario.json   resumen, matrices orden×poder, entidades, municipios, brechas, apps (+permisos), sitios,
                    brechas por canal y catálogo curado
  subdominios.json  los ~12.8k hosts .gob.mx (filas compactas) con entidad/municipio si el slug está mapeado
  sfp.json          trámites federales de la SFP que no están ligados a uno del CNARTyS (id negativo = -_id)
Los trámites del CNARTyS NO van aquí: la web los lee en el servidor desde servicios/cnartys/tramites.json.
uso: python3 -I scripts/build_web_data.py
"""
import csv
import json
import re
import sys
import unicodedata
from collections import Counter
from pathlib import Path

R = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(R))
import gobmx  # noqa: E402

OP = ("orden", "poder", "subtipo", "municipio")
DEST = R / "webapp/data"


def slug(s):
    s = "".join(c for c in unicodedata.normalize("NFD", str(s or "").lower()) if unicodedata.category(c) != "Mn")
    return re.sub(r"[^a-z0-9]+", "-", s).strip("-")


def apps():
    perm = {p.get("android_id"): p for p in gobmx.permisos()}
    out, vistas = [], Counter()
    for a in gobmx.apps():
        p, i = a.get("play") or {}, a.get("appstore") or {}
        clave = a.get("android_id") or (f"ios-{a['ios_id']}" if a.get("ios_id") else slug(a.get("nombre")))
        vistas[clave] += 1
        if vistas[clave] > 1:
            clave = f"{clave}-{vistas[clave]}"
        pr = perm.get(a.get("android_id")) or {}
        out.append({"clave": clave, "nombre": a.get("nombre"), "siglas": a.get("siglas"), "dependencia": a.get("dependencia"),
                    **{k: a.get(k) for k in OP}, "estado": a.get("estado"), "sector": a.get("sector"),
                    "estatus": a.get("estatus"), "android": a.get("android_id"), "ios": a.get("ios_id"),
                    "requiere_cuenta": a.get("requiere_cuenta"), "desarrollador": a.get("desarrollador_tienda"),
                    "funciones": a.get("funciones"), "notas": a.get("notas"),
                    "instalaciones": p.get("realInstalls") or 0, "instalaciones_txt": p.get("installs"),
                    "play_score": p.get("score"), "play_ratings": p.get("ratings"), "play_version": p.get("version"),
                    "ios_score": i.get("averageUserRating"), "ios_ratings": i.get("userRatingCount"),
                    "proposito": a.get("proposito"),
                    "permisos": {"total": pr.get("permisos_total"), "peligrosos": pr.get("permisos_peligrosos") or [],
                                 "trackers": pr.get("trackers") or [], "notas": pr.get("notas"),
                                 "fecha": pr.get("fecha")} if pr else None})
    return out


def sitios():
    out = []
    for s in gobmx.sitios():
        er = s.get("estado_real") or []
        er = er if isinstance(er, list) else [er]
        out.append({"dependencia": s.get("dependencia"), "siglas": s.get("siglas"), **{k: s.get(k) for k in OP},
                    "estado": s.get("estado"), "url": s.get("url"), "tipo": s.get("tipo"),
                    "plataforma": s.get("plataforma"), "anti_bot": s.get("anti_bot"), "autenticacion": s.get("autenticacion"),
                    "servicios_en_linea": s.get("servicios_en_linea"), "api_publica": s.get("api_publica"),
                    "evidencia": [e for e in er if isinstance(e, dict)],
                    "resultado": er[-1].get("resultado") if er and isinstance(er[-1], dict) else None,
                    "proposito": s.get("proposito"), "notas": s.get("notas")})
    return out


def subdominios():
    f = R / "sitios/subdominios-consolidado.csv"
    filas = []
    if f.exists():
        with f.open() as fh:
            for row in csv.DictReader(fh):
                lug = gobmx.lugar_de_slug(row.get("dependencia_o_estado") or "") or (None, None)
                act = str(row.get("activo_dotgob") or "")
                filas.append([row["host"], row.get("fuente"), "activo" if act.startswith("activo") else act or None,
                              row.get("dependencia_o_estado"), lug[0], lug[1]])
    return filas


def sfp():
    """cnts-federal que siguen en servicios() (no ligados al CNARTyS), con forma de trámite para la web."""
    out = []
    for s in gobmx.servicios():
        if s.get("_id") is None:
            continue
        out.append({"id": -int(s["_id"]), "homoclave": s.get("homoclave"), "nombre_tramite": s.get("nombre_tramite"),
                    "dependencia": s.get("dependencia"), "siglas": s.get("siglas"), "orden": "federal",
                    "poder": s.get("poder"), "subtipo": s.get("subtipo"), "estado": "Nacional", "municipio": None,
                    "en_linea": True if s.get("nivel_digitalizacion") == "Servicios punta a punta" else None,
                    "nivel_digitalizacion_sfp": s.get("nivel_digitalizacion"), "frecuencia": None,
                    "posible_duplicado": bool(s.get("posible_duplicado_cnartys")), "fuente": "sfp"})
    return out


t = gobmx.tramites()
comp = gobmx.comparar("estatal")
res = gobmx.resumen()
subs = subdominios()
data = {
    "catalogo": gobmx.catalogo_meta(),
    "resumen": {
        "apps": len(gobmx.apps()), "sitios": len(gobmx.sitios()),
        "subdominios": {"total": len(subs), "activos": sum(1 for x in subs if x[2] == "activo")},
        "servicios": res["servicios"], "tramites": len(t), "permisos": res["permisos"],
        "tramites_por_orden": dict(Counter(x["orden"] for x in t)),
        "tramites_por_poder": dict(Counter(x["poder"] for x in t)),
        "tramites_por_canal": dict(Counter("sin_dato" if x["en_linea"] is None else "en_linea" if x["en_linea"]
                                           else "no_en_linea" for x in t)),
    },
    "orden_poder": {"apps": res["orden_poder"]["apps"], "sitios": res["orden_poder"]["sitios"],
                    "tramites": gobmx.matriz_orden_poder(t)},
    "cobertura_ceros": res["cobertura_ceros"],
    "subdominios_mapeo": res["subdominios_mapeo"],
    "metodologia": gobmx.METODOLOGIA,
    "referencia_federal": comp["referencia_federal"],
    "entidades": comp["filas"],
    "municipios": gobmx.metricas("municipal"),
    "brechas": gobmx.brechas_volumen(limite=200),
    "brechas_canal": json.loads((R / "servicios/brechas.json").read_text()),
    "curado": json.loads((R / "servicios/catalogo-conamer.json").read_text()),
    "apps": apps(),
    "sitios": sitios(),
}
DEST.mkdir(parents=True, exist_ok=True)
for nombre, obj in (("inventario.json", data), ("subdominios.json", subs), ("sfp.json", sfp())):
    (DEST / nombre).write_text(json.dumps(obj, ensure_ascii=False, separators=(",", ":")))
    print(f"webapp/data/{nombre}: {(DEST / nombre).stat().st_size // 1024} KB")
print(f"{len(data['apps'])} apps ({sum(1 for a in data['apps'] if a['permisos'])} con permisos), {len(data['sitios'])} sitios, "
      f"{len(subs)} subdominios, {len(data['municipios'])} municipios")
