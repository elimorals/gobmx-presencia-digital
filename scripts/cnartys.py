"""Descarga y normaliza el CSV público de trámites del Catálogo Nacional (CNARTyS, CONAMER).

El portal publica cada día un archivo con fecha en el nombre:
  https://www.catalogonacional.gob.mx/sujetosobligados/TramitesPublicadosCSV/tramites_publicados_DD_MM_YYYY.csv
Observado el 2026-10-24 (ver servicios/harvest-subnacional.md):
  - el archivo del día D se genera a las ~02:31 UTC del día D+1 (20:31 hora del centro, día D);
  - el servidor solo conserva los 2 más recientes (los anteriores dan 404);
  - por eso la regla es "hoy (UTC) bajas el de ayer" y, si no está, se retrocede día por día.
curl/urllib directo funciona (sin reto de Akamai en esta ruta), a diferencia del buscador del portal.

Escribe:
  servicios/cnartys/tramites.json   un registro por línea, ordenado por id (diff legible entre cortes)
  servicios/cnartys/meta.json       archivo, fecha de corte, sha256, conteos
  servicios/cnartys/hosts.txt       hosts *.gob.mx mencionados en los textos de los trámites
y agrega a sitios/subdominios-consolidado.csv los hosts que aún no estén (fuente=cnartys-tramites).

uso:
  python3 -I scripts/cnartys.py actualizar [--csv RUTA] [--dias 7]   # sin --csv descarga el más reciente
  python3 -I scripts/cnartys.py sondear [--dias 7]                   # qué fechas existen hoy en el servidor
  python3 -I scripts/cnartys.py prueba                               # autoprueba sin red
"""
import csv
import datetime as dt
import hashlib
import json
import re
import ssl
import sys
import tempfile
import unicodedata
import urllib.error
import urllib.request
from collections import Counter
from pathlib import Path

RAIZ = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(RAIZ))
sys.path.insert(0, str(RAIZ / "scripts"))
from gobmx import ENTIDADES  # noqa: E402

URL = "https://www.catalogonacional.gob.mx/sujetosobligados/TramitesPublicadosCSV/tramites_publicados_{}.csv"
DESTINO = RAIZ / "servicios/cnartys"
CABECERA = "id,nivelGobierno,nivelGobiernoDetalle"
ESTADO_ALIAS = {"Veracruz de Ignacio de la Llave": "Veracruz", "Michoacán de Ocampo": "Michoacán",
                "Coahuila de Zaragoza": "Coahuila"}
# nombre de localidad en el catálogo → municipio INEGI
MUNICIPIO_ALIAS = {("Quintana Roo", "Playa del Carmen"): "Solidaridad"}
RE_EMAIL = re.compile(r"\S+@\S+")
RE_HOST = re.compile(r"\b((?:[a-z0-9-]+\.)+gob\.mx)\b", re.I)


def _ctx():
    try:
        import certifi  # en macOS el Python de python.org no trae CAs; el runner sí
        return ssl.create_default_context(cafile=certifi.where())
    except ImportError:
        return ssl.create_default_context()


def candidatas(hoy=None, dias=7):
    """Fechas a intentar, de la más reciente a la más vieja: ayer, antier… (UTC)."""
    hoy = hoy or dt.datetime.now(dt.timezone.utc).date()
    return [hoy - dt.timedelta(days=i) for i in range(1, dias + 1)]


def nombre(fecha):
    return fecha.strftime("%d_%m_%Y")


def _pedir(fecha, metodo="GET"):
    # Akamai da 403 a un UA propio como "gobmx-inventario (+github…)"; el de urllib, curl o Mozilla pasan (2026-10-24)
    req = urllib.request.Request(URL.format(nombre(fecha)), method=metodo, headers={"User-Agent": "Mozilla/5.0"})
    return urllib.request.urlopen(req, timeout=180, context=_ctx())


def sondear(dias=7, hoy=None):
    """[(fecha, status, bytes, last_modified)] para cada candidata. Solo HEAD."""
    out = []
    for f in candidatas(hoy, dias):
        try:
            with _pedir(f, "HEAD") as r:
                out.append((f.isoformat(), r.status, int(r.headers.get("Content-Length") or 0),
                            r.headers.get("Last-Modified")))
        except urllib.error.HTTPError as e:
            out.append((f.isoformat(), e.code, 0, None))
        except urllib.error.URLError as e:
            out.append((f.isoformat(), f"error: {e.reason}", 0, None))
    return out


def descargar(dias=7, hoy=None):
    """Baja el CSV más reciente disponible. Devuelve (fecha, ruta). RuntimeError si ninguno sirve."""
    intentos = []
    for f in candidatas(hoy, dias):
        try:
            with _pedir(f) as r:
                cuerpo = r.read()
        except urllib.error.HTTPError as e:
            intentos.append(f"{nombre(f)}: {e.code}")
            continue
        if not cuerpo.decode("utf-8-sig", "replace").startswith(CABECERA):
            intentos.append(f"{nombre(f)}: 200 pero no es el CSV esperado ({cuerpo[:60]!r})")
            continue
        ruta = Path(tempfile.gettempdir()) / f"tramites_publicados_{nombre(f)}.csv"
        ruta.write_bytes(cuerpo)
        return f, ruta
    raise RuntimeError("ningún CSV disponible en los últimos %d días: %s" % (dias, "; ".join(intentos)))


# ---------- normalización ----------

def limpio(s):
    return " ".join(str(s or "").split())


def poder_de(detalle):
    d = limpio(detalle).replace("\xa0", " ")
    if d.startswith("Poder Judicial"):
        return "judicial"
    if d.startswith("Poder Legislativo"):
        return "legislativo"
    if d.startswith("Organismo con Autonomía Constitucional") or d.startswith("Organismos con jurisdicción contenciosa"):
        return "autonomo"  # tribunales administrativos: autónomos, misma regla que clasificar_orden_poder
    if d.startswith("Administración Pública") or d == "Municipio o Alcaldía":
        return "ejecutivo"
    raise ValueError(f"nivelGobiernoDetalle desconocido: {detalle!r} (agregar a poder_de)")


def estado_de(r):
    if r["nivelGobierno"] == "Federal":
        return "Nacional"
    e = ESTADO_ALIAS.get(limpio(r["estado"]), limpio(r["estado"]))
    if e not in ENTIDADES:
        raise ValueError(f"estado desconocido: {r['estado']!r} (id {r['id']})")
    return e


def fecha_iso(s):
    try:
        return dt.datetime.strptime(s.strip()[:10], "%m/%d/%Y").date().isoformat()
    except ValueError:
        return None


def hosts_de(*textos):
    t = RE_EMAIL.sub(" ", " ".join(textos))
    return sorted({h.lower() for h in RE_HOST.findall(t)})


def normalizar_fila(r):
    from clasificar_orden_poder import clasificar
    orden = limpio(r["nivelGobierno"]).lower()
    if orden not in ("federal", "estatal", "municipal"):
        raise ValueError(f"nivelGobierno desconocido: {r['nivelGobierno']!r}")
    estado = estado_de(r)
    mun = limpio(r["municipio"]) or None
    if mun:
        mun = MUNICIPIO_ALIAS.get((estado, mun), mun)
    canal = limpio(r["opcionRealizarTramite"])
    cl = canal.lower()
    reg = {
        "id": int(r["id"]), "homoclave": limpio(r["homoclave"]), "nombre_tramite": limpio(r["nombre"]),
        "tipo_registro": {"Trámite": "tramite", "Servicio": "servicio"}.get(limpio(r["tramiteServicio"])),
        "tipo": limpio(r["tipo"]) or None, "categoria": limpio(r["categoria"]) or None,
        "dependencia": limpio(r["dependencia"]), "siglas": limpio(r["acronimoDependencia"]) or None,
        "unidad_administrativa": limpio(r["unidadAdministrativa"]) or None,
        "nivel": orden, "estado": estado, "orden": orden, "poder": poder_de(r["nivelGobiernoDetalle"]),
        "subtipo": None, "municipio": mun if orden == "municipal" else None,
        "nivel_gobierno_detalle": limpio(r["nivelGobiernoDetalle"]).replace("\xa0", " "),
        # canal: null = el catálogo no lo reporta (no es "no")
        "en_linea": ("medios electr" in cl) if canal else None,
        "presencial": ("presencial" in cl) if canal else None,
        "telefono": ("telef" in cl) if canal else None,
        "canales_txt": canal or None,
        "frecuencia": int(r["frecuencia"]) if r["frecuencia"].strip().isdigit() else None,
        "dirigido": limpio(r["dirigido"]) or None,
        "fecha_actualizacion": fecha_iso(r["fechaActualizacion"]),
        "descripcion_ciudadana": limpio(r["descripcionCiudadana"]) or None,
        "descripcion": limpio(r["descripcion"]) or None,
        "_fuente": "cnartys",
    }
    if reg["poder"] == "ejecutivo":
        reg["subtipo"] = clasificar({"dependencia": reg["dependencia"], "estado": estado,
                                     "nivel": "federal" if orden == "federal" else orden}).get("subtipo")
    # opcionales: solo si traen dato
    extra = {"modalidad": limpio(r["modalidad"]) or None, "tags": limpio(r["tags"]) or None,
             "programa_social": True if r["programaSocial"].strip() == "True" else None,
             "oculto": True if r["oculto"].strip() == "1" else None,
             "scian": {k: limpio(r[k]) for k in ("sector", "subsector", "rama", "subrama", "clase") if limpio(r[k])} or None,
             "hosts": hosts_de(r["descripcion"], r["descripcionCiudadana"], r["opcionRealizarTramite"],
                               r["modalidad"], r["tags"]) or None}
    reg.update({k: v for k, v in extra.items() if v is not None})
    return reg


def llave_nombre(nombre_tramite, siglas):
    s = "".join(c for c in unicodedata.normalize("NFD", str(nombre_tramite or "").lower()) if unicodedata.category(c) != "Mn")
    return " ".join(re.sub(r"[^a-z0-9 ]", " ", s).split()), (siglas or "").strip().upper()


def deduplicar(filas):
    """El catálogo publica a veces la misma homoclave dos veces (registro viejo + versión nueva, mismo lugar).
    Queda la más reciente (fecha_actualizacion, id); las otras van en `ids_anteriores`."""
    por_hc = {}
    for x in filas:
        por_hc.setdefault(x["homoclave"] or f"sin-homoclave-{x['id']}", []).append(x)
    out = []
    for grupo in por_hc.values():
        grupo.sort(key=lambda x: (x["fecha_actualizacion"] or "", x["id"]), reverse=True)
        if len(grupo) > 1:
            grupo[0]["ids_anteriores"] = [x["id"] for x in grupo[1:]]
        out.append(grupo[0])
    return out


def cruzar_sfp(filas, cnts):
    """Liga los federales con cnts-federal.json (SFP): por homoclave y, si no, por nombre+siglas cuando el
    par es único en ambos lados (renombres como SCT→SICT). El registro CNARTyS gana `homoclave_sfp`."""
    por_hc = {s["homoclave"].strip(): s for s in cnts}
    fed = [x for x in filas if x["orden"] == "federal"]
    for x in fed:
        if x["homoclave"] in por_hc:
            x["homoclave_sfp"] = x["homoclave"]
    usados = {x["homoclave_sfp"] for x in fed if "homoclave_sfp" in x}
    resto = [s for s in cnts if s["homoclave"].strip() not in usados]
    a = Counter(llave_nombre(x["nombre_tramite"], x["siglas"]) for x in fed if "homoclave_sfp" not in x)
    b = Counter(llave_nombre(s["nombre_tramite"], s.get("siglas")) for s in resto)
    sfp_por_llave = {llave_nombre(s["nombre_tramite"], s.get("siglas")): s for s in resto}
    for x in fed:
        k = llave_nombre(x["nombre_tramite"], x["siglas"])
        if "homoclave_sfp" not in x and a[k] == 1 and b.get(k) == 1:
            x["homoclave_sfp"], x["cruce_sfp"] = sfp_por_llave[k]["homoclave"].strip(), "nombre+siglas"
    for x in fed:
        if "homoclave_sfp" in x:
            x["nivel_digitalizacion_sfp"] = por_hc[x["homoclave_sfp"]].get("nivel_digitalizacion")
    return filas


def normalizar(ruta_csv):
    cnts = RAIZ / "servicios/cnts-federal.json"
    with open(ruta_csv, encoding="utf-8-sig", newline="") as fh:
        filas = deduplicar([normalizar_fila(r) for r in csv.DictReader(fh)])
    if cnts.exists():
        cruzar_sfp(filas, json.loads(cnts.read_text()))
    return sorted(filas, key=lambda x: x["id"])


def agregar_hosts_consolidado(hosts):
    """Agrega al CSV consolidado los hosts que no estén. Devuelve cuántos agregó."""
    f = RAIZ / "sitios/subdominios-consolidado.csv"
    if not f.exists():
        return 0
    with f.open() as fh:
        ya = {row["host"] for row in csv.DictReader(fh)}
    nuevos = sorted(set(hosts) - ya)
    if nuevos:
        with f.open("a", newline="") as fh:
            w = csv.writer(fh)
            for h in nuevos:  # dependencia_o_estado: misma regla que merge_subdominios.py
                w.writerow([h, "cnartys-tramites", "", h.split(".gob.mx")[0].split(".")[-1]])
    return len(nuevos)


def escribir(filas, fecha, ruta_csv):
    DESTINO.mkdir(parents=True, exist_ok=True)
    (DESTINO / "tramites.json").write_text(
        "[\n" + ",\n".join(json.dumps(x, ensure_ascii=False, separators=(",", ":")) for x in filas) + "\n]\n")
    hosts = sorted({h for x in filas for h in x.get("hosts", [])})
    (DESTINO / "hosts.txt").write_text("\n".join(hosts) + "\n")
    meta = {
        "fuente": "Catálogo Nacional de Regulaciones, Trámites y Servicios (CONAMER), CSV público diario",
        "url": URL.format(nombre(fecha)) if fecha else None,
        "archivo": Path(ruta_csv).name, "fecha_corte": fecha.isoformat() if fecha else None,
        "normalizado": dt.datetime.now(dt.timezone.utc).isoformat(timespec="seconds"),
        "sha256": hashlib.sha256(Path(ruta_csv).read_bytes()).hexdigest(),
        "registros": len(filas),
        "por_orden": dict(Counter(x["orden"] for x in filas)),
        "por_poder": dict(Counter(x["poder"] for x in filas)),
        "entidades_con_registros": len({x["estado"] for x in filas if x["orden"] != "federal"}),
        "municipios_con_registros": len({(x["estado"], x["municipio"]) for x in filas if x["municipio"]}),
        "con_canal_en_linea": sum(1 for x in filas if x["en_linea"]),
        "sin_dato_de_canal": sum(1 for x in filas if x["en_linea"] is None),
        "duplicados_internos_quitados": sum(len(x.get("ids_anteriores", [])) for x in filas),
        "ligados_sfp_por_homoclave": sum(1 for x in filas if "homoclave_sfp" in x and "cruce_sfp" not in x),
        "ligados_sfp_por_nombre_siglas": sum(1 for x in filas if x.get("cruce_sfp") == "nombre+siglas"),
        "hosts_gob_mx": len(hosts),
        "regla_descarga": "el archivo del día D aparece ~02:31 UTC del día D+1; el servidor guarda solo los 2 últimos",
    }
    (DESTINO / "meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1) + "\n")
    meta["hosts_agregados_a_subdominios"] = agregar_hosts_consolidado(hosts)
    return meta


def fecha_de_nombre(ruta):
    m = re.search(r"(\d{2})_(\d{2})_(\d{4})", Path(ruta).name)
    return dt.date(int(m[3]), int(m[2]), int(m[1])) if m else None


def actualizar(ruta_csv=None, dias=7):
    fecha, ruta = (fecha_de_nombre(ruta_csv), Path(ruta_csv)) if ruta_csv else descargar(dias)
    return escribir(normalizar(ruta), fecha, ruta)


def _autoprueba():
    hoy = dt.date(2026, 10, 25)
    assert [nombre(f) for f in candidatas(hoy, 3)] == ["24_10_2026", "23_10_2026", "22_10_2026"]
    assert poder_de("Municipio o Alcaldía") == "ejecutivo"
    assert poder_de("Organismo con Autonomía Constitucional\xa0Estatal") == "autonomo"
    assert poder_de("Poder Judicial Federal") == "judicial"
    try:
        poder_de("Algo Nuevo")
        raise AssertionError("debió rechazar un nivelGobiernoDetalle desconocido")
    except ValueError:
        pass
    base = {k: "" for k in ("id nivelGobierno nivelGobiernoDetalle estado municipio dependencia unidadAdministrativa "
                            "acronimoDependencia tipo categoria homoclave nombre modalidad descripcionCiudadana descripcion "
                            "opcionRealizarTramite fechaActualizacion tramiteServicio programaSocial frecuencia tags oculto dirigido "
                            "sector subsector rama subrama clase").split()}
    r = normalizar_fila({**base, "id": "7", "nivelGobierno": "Municipal", "nivelGobiernoDetalle": "Municipio o Alcaldía",
                         "estado": "Quintana Roo", "municipio": "Playa del Carmen", "dependencia": "Tesorería",
                         "nombre": " Pago\n de predial ", "opcionRealizarTramite": "Presencial | Medios electrónicos",
                         "descripcion": "en https://pagos.solidaridad.gob.mx/x o a correo@finanzas.gob.mx",
                         "fechaActualizacion": "10/24/2025 09:21:57", "frecuencia": "355", "oculto": "0"})
    assert (r["orden"], r["poder"], r["municipio"], r["nombre_tramite"]) == ("municipal", "ejecutivo", "Solidaridad", "Pago de predial")
    assert (r["en_linea"], r["presencial"], r["frecuencia"], r["fecha_actualizacion"]) == (True, True, 355, "2025-10-24")
    assert r["hosts"] == ["pagos.solidaridad.gob.mx"] and "oculto" not in r
    v = normalizar_fila({**base, "id": "8", "nivelGobierno": "Estatal", "nivelGobiernoDetalle": "Administración Pública Estatal",
                         "estado": "Veracruz de Ignacio de la Llave", "dependencia": "SEFIPLAN", "frecuencia": ""})
    assert v["estado"] == "Veracruz" and v["en_linea"] is None and v["frecuencia"] is None and v["municipio"] is None
    fed = {**base, "nivelGobierno": "Federal", "nivelGobiernoDetalle": "Administración Pública Federal",
           "dependencia": "Servicio de Administración Tributaria", "acronimoDependencia": "SAT"}
    f = normalizar_fila({**fed, "id": "9", "homoclave": "SAT-01-025", "nombre": "Inscripción RFC",
                         "fechaActualizacion": "06/01/2026 00:00:00"})
    assert f["estado"] == "Nacional" and f["subtipo"] == "desconcentrado"
    viejo = normalizar_fila({**fed, "id": "3", "homoclave": "SAT-01-025", "nombre": "Inscripción RFC",
                             "fechaActualizacion": "01/01/2020 00:00:00"})
    g = normalizar_fila({**fed, "id": "10", "homoclave": "SICT-2024-1-A", "nombre": "Licencia Federal"})
    h = normalizar_fila({**fed, "id": "11", "homoclave": "SAT-2024-9-A", "nombre": "Aviso X"})
    filas = cruzar_sfp(deduplicar([viejo, f, g, h]), [
        {"homoclave": "SAT-01-025", "nombre_tramite": "Inscripción RFC", "siglas": "SAT", "nivel_digitalizacion": "Información"},
        {"homoclave": "SCT-03-028", "nombre_tramite": "Licencia federal", "siglas": "SAT", "nivel_digitalizacion": "Descargar formatos"},
        {"homoclave": "SAT-00-1", "nombre_tramite": "Aviso X", "siglas": "SAT"}, {"homoclave": "SAT-00-2", "nombre_tramite": "Aviso X", "siglas": "SAT"}])
    by = {x["id"]: x for x in filas}
    assert set(by) == {9, 10, 11} and by[9]["ids_anteriores"] == [3]  # dedup: queda la versión más reciente
    assert by[9]["nivel_digitalizacion_sfp"] == "Información" and "cruce_sfp" not in by[9]
    assert by[10]["homoclave_sfp"] == "SCT-03-028" and by[10]["cruce_sfp"] == "nombre+siglas"
    assert "homoclave_sfp" not in by[11]  # ambiguo (2 en la SFP): no se liga
    assert fecha_de_nombre("/x/tramites_publicados_23_10_2026.csv") == dt.date(2026, 10, 23)
    print("cnartys: OK")


def main(argv):
    import argparse
    p = argparse.ArgumentParser(prog="cnartys", description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    sub = p.add_subparsers(dest="cmd", required=True)
    a = sub.add_parser("actualizar"); a.add_argument("--csv"); a.add_argument("--dias", type=int, default=7)
    s = sub.add_parser("sondear"); s.add_argument("--dias", type=int, default=7)
    sub.add_parser("prueba")
    args = p.parse_args(argv)
    if args.cmd == "prueba":
        _autoprueba()
    elif args.cmd == "sondear":
        for fila in sondear(args.dias):
            print(*fila)
    else:
        print(json.dumps(actualizar(args.csv, args.dias), ensure_ascii=False, indent=1))
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
