"""Prueba end-to-end del MCP remoto (Streamable HTTP) que sirve la web en /mcp.

Hace el handshake, lista las tools y llama a cada una (incluye comparar federal, que debe rechazarse).
uso: .venv/bin/python -I mcp-server/test_remoto.py [URL]   (default: https://gobmx-presencia-digital.vercel.app/mcp)
"""
import asyncio
import json
import sys

from mcp import ClientSession
from mcp.client.streamable_http import streamablehttp_client

URL = sys.argv[1] if len(sys.argv) > 1 else "https://gobmx-presencia-digital.vercel.app/mcp"


async def main():
    async with streamablehttp_client(URL) as (r, w, _):
        async with ClientSession(r, w) as s:
            info = await s.initialize()
            print("servidor:", info.serverInfo.name, info.serverInfo.version)
            nombres = {t.name for t in (await s.list_tools()).tools}
            print("tools:", sorted(nombres))
            assert {"stats", "buscar_tramites", "tramite", "comparar", "brechas_volumen", "buscar_apps",
                    "buscar_sitios", "buscar_subdominio", "catalogo_tramites", "matriz_orden_poder_tramites"} <= nombres

            async def llamar(nombre, args):
                res = await s.call_tool(nombre, args)
                assert not res.isError, f"{nombre} devolvió error: {res.content}"
                txt = res.content[0].text
                print(f"OK {nombre}({args}) -> {len(txt)} chars")
                return json.loads(txt)

            st = await llamar("stats", {})
            assert st["resumen"]["tramites"] > 30000
            b = await llamar("buscar_tramites", {"texto": "licencia", "estado": "Tabasco"})
            assert b["total"] > 0
            t = await llamar("tramite", {"id": b["items"][0]["id"]})
            assert t["ficha_oficial"].startswith("https://www.gob.mx/public/tramites/detalleTramite.xhtml?homoclave=")
            await llamar("buscar_tramites", {"orden": "municipal", "municipio": "Mérida", "en_linea": "si"})
            m = await llamar("matriz_orden_poder_tramites", {"estado": "Yucatán"})
            assert "estatal" in m
            await llamar("brechas_volumen", {"orden": "estatal", "limite": 5})
            c = await llamar("comparar", {"orden": "estatal", "nombres": "Campeche, Tlaxcala", "ordenar_por": "pct_tramites_en_linea"})
            assert [f["estado"] for f in c["filas"]] == ["Tlaxcala", "Campeche"] or len(c["filas"]) == 2
            fed = await llamar("comparar", {"orden": "federal"})
            assert "no son comparables" in fed["error"]
            a = await llamar("buscar_apps", {"orden": "estatal", "poder": "judicial", "limite": 3})
            assert all(x["poder"] == "judicial" for x in a)
            await llamar("buscar_sitios", {"texto": "sat", "limite": 3})
            await llamar("buscar_subdominio", {"texto": "imss", "solo_activos": True, "limite": 5})
            k = await llamar("catalogo_tramites", {})
            assert k["fecha_corte"]
    print("\n✅ MCP remoto OK:", URL)


if __name__ == "__main__":
    asyncio.run(main())
