import type { Metadata } from "next";

export const metadata: Metadata = { title: "Guía: CLI, MCP y API", description: "Cómo consultar el inventario desde la terminal, desde Claude (MCP) o por HTTP." };

const RAW = "https://raw.githubusercontent.com/elimorals/gobmx-presencia-digital/main";
const SITIO = process.env.NEXT_PUBLIC_SITIO ?? "https://gobmx-presencia-digital.vercel.app";

const TOOLS: [string, string][] = [
  ["stats()", "Totales, conteos por nivel/sector/estatus, matrices orden×poder de apps, sitios y servicios, y el corte de trámites."],
  ["buscar_servicios(texto, orden, poder, estado, municipio, en_linea, min_frecuencia, por_frecuencia, limite)", "Trámites de los tres órdenes. en_linea: si · no · sin_dato."],
  ["brechas_volumen(orden, estado, limite)", "Trámites de mayor volumen sin opción en línea, cruzados con apps y sitios de la dependencia."],
  ["catalogo_tramites(sondear)", "Fecha de corte y conteos; sondear=true dice qué archivos diarios hay en el servidor."],
  ["comparar(orden, nombres, ordenar_por)", "Entidades (estatal) o municipios (municipal) con tasas por población y presupuesto. Rechaza federal."],
  ["buscar_apps(orden, poder, estado, sector, estatus, texto, limite)", "Apps de gobierno."],
  ["buscar_sitios(orden, poder, estado, texto, limite)", "Portales con plataforma, anti-bot, autenticación y evidencia fechada."],
  ["dependencia(nombre)", "Apps (con permisos) y sitios de una dependencia en un llamado."],
  ["permisos(texto, orden, poder, solo_peligrosos, limite)", "Permisos peligrosos y rastreadores por app."],
  ["buscar_subdominio(texto, solo_activos, limite)", "Los ~12.8k subdominios .gob.mx."],
];

export default function Guia() {
  return (
    <section className="seccion texto">
      <h1>Guía: CLI, MCP y API</h1>
      <p>El mismo inventario se consulta de cuatro formas. Todas leen los mismos archivos del repositorio.</p>

      <h2>API de este sitio</h2>
      <pre>{`GET ${SITIO}/api/tramites?q=predial&estado=Yucatán&en_linea=si&pagina=1
# filtros: q, orden (federal|estatal|municipal), estado, municipio,
#          en_linea (si|no|sin_dato), orden_por (reciente), pagina
# respuesta: { total, paginas, pagina, items: [ ... ] }  · 25 por página`}</pre>

      <h2>CLI — gobmx</h2>
      <p>Python 3, sin dependencias externas.</p>
      <pre>{`git clone https://github.com/elimorals/gobmx-presencia-digital
cd gobmx-presencia-digital
python3 gobmx.py stats                                   # resumen + matrices orden × poder
python3 gobmx.py servicios --estado Tabasco --busca vehicular --por-frecuencia --limite 10
python3 gobmx.py servicios --orden municipal --municipio Mérida --en-linea si
python3 gobmx.py brechas --limite 20                     # mucha demanda, sin opción en línea
python3 gobmx.py catalogo --sondear                      # corte cargado y archivos disponibles
python3 gobmx.py apps --orden estatal --poder judicial
python3 gobmx.py sitios --orden federal --poder autonomo
python3 gobmx.py comparar --orden estatal --ordenar pct_tramites_en_linea
python3 gobmx.py permisos --busca IMSS
python3 gobmx.py dep IMSS`}</pre>

      <h2>MCP — gobmx-inventario</h2>
      <p>Servidor de solo lectura para consultar el inventario en lenguaje natural desde Claude u otro cliente MCP.</p>
      <pre>{`uv venv .venv && VIRTUAL_ENV=.venv uv pip install "mcp<2"
claude mcp add gobmx-inventario -- \\
  /ruta/al/repo/.venv/bin/python -I /ruta/al/repo/mcp-server/server.py
claude mcp list    # gobmx-inventario ... ✔ Connected`}</pre>
      <div className="tabla-scroll">
        <table>
          <thead><tr><th>Herramienta</th><th>Qué hace</th></tr></thead>
          <tbody>{TOOLS.map(([n, d]) => <tr key={n}><td className="mono ancho">{n}</td><td className="ancho">{d}</td></tr>)}</tbody>
        </table>
      </div>

      <h2 style={{ marginTop: 28 }}>Datos crudos (HTTP, sin autenticación)</h2>
      <ul>
        <li><a href={`${RAW}/servicios/cnartys/tramites.json`}>Trámites de los tres órdenes</a> (JSON, uno por línea) y <a href={`${RAW}/servicios/cnartys/meta.json`}>metadatos del corte</a></li>
        <li><a href={`${RAW}/apps/apps.json`}>Apps</a> · <a href={`${RAW}/apps/permisos/permisos.json`}>permisos</a></li>
        <li><a href={`${RAW}/sitios/subdominios-consolidado.csv`}>Subdominios (CSV)</a> · <a href={`${RAW}/servicios/brechas.json`}>brechas</a></li>
        <li><a href={`${RAW}/llms.txt`}>llms.txt</a>: resumen pensado para modelos</li>
      </ul>

      <h2>Cómo se actualiza</h2>
      <ul>
        <li>Trámites: a diario (workflow <code>refresh-tramites</code>), y la web se redespliega sola.</li>
        <li>Subdominios: una vez al mes (Certificate Transparency + dotgob-mx).</li>
        <li>Apps y métricas de tienda: en local con <code>scripts/regenerar.sh</code> (las tiendas bloquean IPs de servidores).</li>
      </ul>
    </section>
  );
}
