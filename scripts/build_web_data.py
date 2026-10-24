"""Empaqueta el inventario para la web de Vercel (webapp/data/inventario.json).

Las métricas salen de gobmx.py (misma implementación que CLI, MCP y dashboard viejo). Los trámites del
CNARTyS NO van aquí: la web los lee en el servidor desde servicios/cnartys/tramites.json.
uso: python3 -I scripts/build_web_data.py
"""
import csv
import json
import sys
from collections import Counter
from pathlib import Path

R = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(R))
import gobmx  # noqa: E402

OP = ("orden", "poder", "subtipo", "municipio")


def apps():
    out = []
    for a in gobmx.apps():
        p, i = a.get("play") or {}, a.get("appstore") or {}
        out.append({"nombre": a.get("nombre"), "siglas": a.get("siglas"), "dependencia": a.get("dependencia"),
                    **{k: a.get(k) for k in OP}, "estado": a.get("estado"), "sector": a.get("sector"),
                    "estatus": a.get("estatus"), "android": a.get("android_id"), "ios": a.get("ios_id"),
                    "instalaciones": p.get("realInstalls") or 0, "play_score": p.get("score"),
                    "ios_score": i.get("averageUserRating"), "proposito": a.get("proposito")})
    return out


def sitios():
    out = []
    for s in gobmx.sitios():
        er = s.get("estado_real") or [{}]
        out.append({"dependencia": s.get("dependencia"), "siglas": s.get("siglas"), **{k: s.get(k) for k in OP},
                    "estado": s.get("estado"), "url": s.get("url"), "tipo": s.get("tipo"),
                    "plataforma": s.get("plataforma"), "anti_bot": s.get("anti_bot"),
                    "resultado": er[-1].get("resultado") if isinstance(er, list) and er else None,
                    "proposito": s.get("proposito")})
    return out


def subdominios():
    f = R / "sitios/subdominios-consolidado.csv"
    total = activos = 0
    if f.exists():
        with f.open() as fh:
            for row in csv.DictReader(fh):
                total += 1
                activos += str(row.get("activo_dotgob", "")).startswith("activo")
    return {"total": total, "activos": activos}


t = gobmx.tramites()
comp = gobmx.comparar("estatal")
data = {
    "catalogo": gobmx.catalogo_meta(),
    "resumen": {
        "apps": len(gobmx.apps()), "sitios": len(gobmx.sitios()), "subdominios": subdominios(),
        "servicios": len(gobmx.servicios()), "tramites": len(t),
        "tramites_por_orden": dict(Counter(x["orden"] for x in t)),
        "tramites_por_poder": dict(Counter(x["poder"] for x in t)),
        "tramites_por_canal": dict(Counter("sin_dato" if x["en_linea"] is None else "en_linea" if x["en_linea"]
                                           else "no_en_linea" for x in t)),
    },
    "metodologia": gobmx.METODOLOGIA,
    "referencia_federal": comp["referencia_federal"],
    "entidades": comp["filas"],
    "municipios": gobmx.metricas("municipal"),
    "brechas": gobmx.brechas_volumen(limite=200),
    "apps": apps(),
    "sitios": sitios(),
}
dest = R / "webapp/data/inventario.json"
dest.parent.mkdir(parents=True, exist_ok=True)
dest.write_text(json.dumps(data, ensure_ascii=False, separators=(",", ":")))
print(f"{dest.relative_to(R)}: {len(data['apps'])} apps, {len(data['sitios'])} sitios, {len(data['entidades'])} entidades, "
      f"{len(data['municipios'])} municipios, {len(data['brechas'])} brechas ({dest.stat().st_size // 1024} KB)")
