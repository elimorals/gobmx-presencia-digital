"""Comprobación mínima de orden/poder: clasificador, QA, matriz y `comparar` (rechaza federal).
uso: python3 -I scripts/test_orden_poder.py
"""
import sys
from pathlib import Path

R = Path(__file__).resolve().parent
sys.path.insert(0, str(R)); sys.path.insert(0, str(R.parent))
import clasificar_orden_poder  # noqa: E402
import gobmx  # noqa: E402
import qa  # noqa: E402

clasificar_orden_poder._autoprueba()
qa._autoprueba()

for o in ("federal", "Federal", "nacional"):
    try:
        gobmx.comparar(o)
        raise AssertionError(f"comparar('{o}') debió rechazarse")
    except ValueError as e:
        assert "no son comparables" in str(e)
assert gobmx.main(["comparar", "--orden", "federal"]) == 2

r = gobmx.comparar("estatal", "Jalisco, nuevo leon")
assert [f["estado"] for f in r["filas"]] == ["Jalisco", "Nuevo León"], r["filas"]
f = r["filas"][0]
assert f["apps"] >= f["apps_activas"] >= 0 and 0 <= f["cobertura_poderes"] <= 4
assert len(gobmx.metricas("estatal")) == 32
rk = [gobmx.valor(x, "por_100k_hab.apps") for x in gobmx.comparar("estatal", ordenar_por="por_100k_hab.apps")["filas"]]
con = [v for v in rk if v is not None]
assert con == sorted(con, reverse=True) and rk[:len(con)] == con  # ranking desc, sin dato al final
assert set(gobmx.resumen()["orden_poder"]) == {"apps", "sitios", "servicios"}
if not r["presupuesto_disponible"]:
    assert f["por_100k_hab"] is None and f["por_mil_mdp"] is None  # null, nunca 0
assert all(x.get("municipio") for x in gobmx.metricas("municipal"))

m = gobmx.matriz_orden_poder([{"orden": "federal", "poder": "ejecutivo"}, {"orden": None, "poder": None}])
assert m["municipal"]["legislativo"] == 0 and m["federal"]["ejecutivo"] == 1 and m["sin_orden(no_oficial)"]["null"] == 1
assert {s["orden"] for s in gobmx.servicios() if s.get("_id") is not None} == {"federal"}  # cnts clasificado al cargar
import cnartys  # noqa: E402
cnartys._autoprueba()
if gobmx.tramites():  # capa CNARTyS: orden/poder del origen, métricas y brechas
    t = gobmx.tramites()
    assert {x["orden"] for x in t} <= set(gobmx.ORDENES) and {x["poder"] for x in t} <= set(gobmx.PODERES)
    assert all(x["estado"] in gobmx.ENTIDADES for x in t if x["orden"] != "federal")
    hc = {x["homoclave"] for x in t}
    assert not any(s.get("_id") is not None and s["homoclave"] in hc for s in gobmx.servicios())  # sin duplicar cnts
    for f in gobmx.metricas("estatal"):
        p = f["pct_tramites_en_linea"]
        assert (p is None) == (f["tramites"] - f["tramites_sin_dato_canal"] == 0) and (p is None or 0 <= p <= 100)
    ref = gobmx.comparar("estatal")["referencia_federal"]
    assert ref["tramites"] > 0 and 0 <= ref["pct_tramites_en_linea"] <= 100
    b = gobmx.brechas_volumen(limite=10)
    fr = [x["frecuencia"] for x in b]
    assert fr and all(fr) and fr == sorted(fr, reverse=True)
print("test_orden_poder: OK")
