"""Genera webapp/data/guia.json: el material real con que se arma /guia en la web.

- MCP remoto: esquema de cada tool tal como lo publica el servidor + una llamada real y su respuesta (recortada).
- MCP local: nombre, descripción y esquema de cada tool (stdio contra mcp-server/server.py).
- CLI: la ayuda (-h) de cada comando y la salida real de un ejemplo (primeras líneas).
- API: una respuesta real de /api/tramites.
Nada se escribe a mano: si cambia una tool o un flag, se vuelve a correr este script.
uso: .venv/bin/python -I scripts/build_guia.py [URL_BASE]   (requiere el paquete mcp<2 del venv)
"""
import asyncio
import datetime as dt
import json
import subprocess
import sys
import urllib.request
from pathlib import Path

from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client
from mcp.client.streamable_http import streamablehttp_client

R = Path(__file__).resolve().parent.parent
BASE = (sys.argv[1] if len(sys.argv) > 1 else "https://gobmx-presencia-digital.vercel.app").rstrip("/")

EJEMPLOS_REMOTO = {
    "stats": {},
    "buscar_tramites": {"texto": "licencia", "estado": "Tabasco"},
    "tramite": {"id": 94922},
    "matriz_orden_poder_tramites": {"estado": "Yucatán"},
    "brechas_volumen": {"orden": "estatal", "limite": 3},
    "comparar": {"orden": "estatal", "nombres": "Campeche, Tlaxcala", "ordenar_por": "_pef"},
    "buscar_apps": {"orden": "estatal", "poder": "judicial", "limite": 2},
    "buscar_sitios": {"texto": "sat", "limite": 2},
    "buscar_subdominio": {"texto": "predial", "solo_activos": True, "limite": 3},
    "catalogo_tramites": {},
}
EJEMPLOS_REMOTO["comparar"]["ordenar_por"] = "transferencias_pef_mxn"

CLI = [
    ("stats", "python3 gobmx.py stats", 14),
    ("servicios", 'python3 gobmx.py servicios --estado Tabasco --busca licencia --por-frecuencia --limite 5', 8),
    ("brechas", "python3 gobmx.py brechas --orden estatal --limite 4", 10),
    ("catalogo", "python3 gobmx.py catalogo", 14),
    ("comparar", 'python3 gobmx.py comparar --orden estatal --nombres "Campeche,Tlaxcala,Yucatán"', 12),
    ("apps", "python3 gobmx.py apps --orden estatal --poder judicial", 8),
    ("sitios", "python3 gobmx.py sitios --orden federal --poder autonomo", 8),
    ("permisos", "python3 gobmx.py permisos --busca IMSS", 8),
    ("dep", "python3 gobmx.py dep IMSS", 10),
]


def recorta(x, n_lista=2, n_txt=220):
    """Respuesta legible: listas a n elementos (con aviso), textos largos cortados."""
    if isinstance(x, list):
        out = [recorta(v, n_lista, n_txt) for v in x[:n_lista]]
        return out + [f"… {len(x) - n_lista} más"] if len(x) > n_lista else out
    if isinstance(x, dict):
        return {k: recorta(v, n_lista, n_txt) for k, v in x.items()}
    if isinstance(x, str) and len(x) > n_txt:
        return x[:n_txt] + "…"
    return x


async def remoto():
    out = []
    async with streamablehttp_client(f"{BASE}/mcp") as (r, w, _):
        async with ClientSession(r, w) as s:
            info = await s.initialize()
            for t in (await s.list_tools()).tools:
                args = EJEMPLOS_REMOTO.get(t.name, {})
                res = await s.call_tool(t.name, args)
                txt = res.content[0].text if res.content else ""
                try:
                    resp = recorta(json.loads(txt), n_lista=2 if t.name != "comparar" else 2)
                except ValueError:
                    resp = txt[:600]
                out.append({"nombre": t.name, "titulo": t.title, "descripcion": t.description,
                            "esquema": t.inputSchema, "ejemplo": args, "respuesta": resp, "error": res.isError})
            return {"servidor": f"{info.serverInfo.name} {info.serverInfo.version}", "instrucciones": info.instructions, "tools": out}


async def local():
    p = StdioServerParameters(command=str(R / ".venv/bin/python"), args=["-I", str(R / "mcp-server/server.py")])
    async with stdio_client(p) as (r, w):
        async with ClientSession(r, w) as s:
            await s.initialize()
            return [{"nombre": t.name, "descripcion": t.description, "esquema": t.inputSchema}
                    for t in (await s.list_tools()).tools]


def cli():
    py = str(R / ".venv/bin/python")
    out = []
    for nombre, cmd, n in CLI:
        ayuda = subprocess.run([py, "-I", "gobmx.py", nombre, "-h"], cwd=R, capture_output=True, text=True).stdout
        args = cmd.split(" ", 2)[2]
        import shlex
        salida = subprocess.run([py, "-I", "gobmx.py", *shlex.split(args)], cwd=R, capture_output=True, text=True)
        lineas = (salida.stdout or salida.stderr).rstrip().splitlines()
        texto = "\n".join(lineas[:n]) + (f"\n… ({len(lineas) - n} líneas más)" if len(lineas) > n else "")
        out.append({"comando": nombre, "ayuda": ayuda.strip(), "ejemplo": cmd, "salida": texto})
    return out


def api():
    u = f"{BASE}/api/tramites?q=predial&estado=Yucat%C3%A1n&en_linea=si"
    try:
        import certifi, ssl
        ctx = ssl.create_default_context(cafile=certifi.where())
    except ImportError:
        ctx = None
    with urllib.request.urlopen(u, timeout=60, context=ctx) as r:
        return {"url": u, "respuesta": recorta(json.loads(r.read()), n_lista=2)}


async def main():
    data = {"generado": dt.date.today().isoformat(), "base": BASE, "remoto": await remoto(),
            "local": await local(), "cli": cli(), "api": api()}
    dest = R / "webapp/data/guia.json"
    dest.write_text(json.dumps(data, ensure_ascii=False, indent=1))
    print(f"{dest.relative_to(R)}: {len(data['remoto']['tools'])} tools remotas, {len(data['local'])} locales, "
          f"{len(data['cli'])} comandos ({dest.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    asyncio.run(main())
