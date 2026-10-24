import type { Metadata } from "next";
import { Codigo, G, Indice, MenuGuia, Notas, Parametros, SITIO } from "@/components/guia";

export const metadata: Metadata = {
  title: "Guía del MCP",
  description: "Servidor MCP del inventario gobmx: conectar, cada herramienta con parámetros y respuestas reales, recetas y diferencias entre el remoto y el local.",
};

const URL_MCP = `${SITIO}/mcp`;

// limitaciones por herramienta (lo que el esquema no dice)
const NOTAS: Record<string, string[]> = {
  stats: ["Los conteos de trámites ya vienen sin duplicados: 364 versiones repetidas del registro nacional se dejaron en la más reciente."],
  buscar_tramites: [
    "La búsqueda pide TODAS las palabras (sin acentos). Si no encuentra, usa menos palabras o sinónimos: en Tabasco las licencias dicen 'chófer', no 'conducir'.",
    "estado va con el nombre corto: 'Veracruz' (no 'Veracruz de Ignacio de la Llave'), 'Michoacán', 'Coahuila', 'Estado de México'. Los federales tienen 'Nacional'.",
    "en_linea='sin_dato' significa que el registro no reporta canal; NO es lo mismo que 'no' (1,532 de los 1,543 sin dato son de CDMX).",
    "frecuencia es el volumen que la dependencia reporta; el registro no documenta el periodo. Sirve para ordenar, no como estadística oficial.",
    "25 resultados por página; usa pagina para seguir. Los ids negativos son trámites federales del dataset de la SFP, con menos detalle.",
  ],
  tramite: [
    "ficha_oficial abre la ficha de gob.mx (requisitos, costos, pasos). En una prueba de 30 al azar abrieron las 30, pero algunas de CDMX dan 404: ese trámite no está publicado en gob.mx.",
    "El registro no trae requisitos ni costos; por eso el enlace.",
  ],
  matriz_orden_poder_tramites: ["Un 0 quiere decir que no hay trámites en ese cruce con esos filtros, no que falte el dato."],
  brechas_volumen: [
    "Solo entran trámites marcados explícitamente sin opción en línea; los 'sin dato' de canal quedan fuera.",
    "El cruce con apps y sitios es por siglas exactas (y misma entidad si no es federal): dependencias con siglas distintas en el inventario no casan.",
  ],
  comparar: [
    "Rechaza orden='federal' a propósito: su presupuesto, extensión y población no tienen punto de comparación con una entidad o un municipio.",
    "null es dato no disponible, nunca cero. El PEF (Ramo 28 + 33) solo existe por entidad; el egreso INEGI, para las 32 entidades y 25 municipios.",
    "tramites mide cuánto captura cada entidad en el registro nacional (Jalisco publica 1), no cuántos trámites tiene.",
  ],
  buscar_apps: ["permisos solo existe para apps con versión Android (387 de 519).", "Ordenadas de más a menos instalaciones en Google Play."],
  buscar_sitios: ["evidencia trae la fecha y el resultado de cada comprobación; un sitio puede haber cambiado después."],
  buscar_subdominio: ["'activo' = respondió a HTTP/S en el último barrido de dotgob-mx (mensual).", "estado solo existe si el dominio está mapeado a una entidad o municipio (62.6 % de los hosts)."],
  catalogo_tramites: ["El archivo del día D aparece ~20:31 hora del centro; el servidor oficial solo guarda los 2 más recientes. Aquí se actualiza a diario."],
};

const RECETAS: [string, string, string][] = [
  ["Periodistas", "Compara Campeche, Tlaxcala y Yucatán: ¿cuánto reciben del PEF por habitante y cuántas apps + sitios tienen por cada mil millones?", "comparar"],
  ["Periodistas", "¿Qué trámites de alto volumen no se pueden hacer en línea en Puebla? Dime si la dependencia ya tiene app o sitio.", "brechas_volumen"],
  ["Periodistas", "Haz un ranking de las 32 entidades por porcentaje de trámites en línea y explica por qué CDMX sale tan bajo.", "comparar (ordenar_por=pct_tramites_en_linea)"],
  ["Periodistas", "¿Qué apps de poderes judiciales estatales piden permisos peligrosos? ¿Cuáles y para qué?", "buscar_apps (poder=judicial, solo_permisos_peligrosos)"],
  ["Periodistas", "¿Cuántos trámites hay por orden de gobierno y poder en Yucatán? ¿Hay alguno del poder legislativo?", "matriz_orden_poder_tramites"],
  ["Ciudadanos", "Necesito renovar mi licencia de manejo en Tabasco: ¿se puede en línea? Dame la ficha oficial.", "buscar_tramites → tramite"],
  ["Ciudadanos", "¿Qué trámites municipales de Mérida se pueden hacer en línea?", "buscar_tramites (orden=municipal, municipio, en_linea=si)"],
  ["Desarrolladores", "Lista los subdominios activos del IMSS y dime cuáles parecen de pruebas o desarrollo.", "buscar_subdominio"],
  ["Desarrolladores", "¿Qué portales federales tienen anti-bot y con qué plataforma están hechos?", "buscar_sitios"],
  ["Desarrolladores", "¿De qué fecha es el corte de trámites y cuántos duplicados se quitaron?", "catalogo_tramites"],
];

// remoto ↔ local
const EQUIV: [string, string, string][] = [
  ["stats", "stats", "Igual. El remoto agrega la matriz de trámites y la fecha de corte."],
  ["buscar_tramites", "buscar_servicios", "El local también devuelve brechas y catálogo curado, con min_frecuencia y por_frecuencia; el remoto pagina de 25 en 25 y ordena por frecuencia o fecha."],
  ["tramite", "—", "Solo remoto. En local, buscar_servicios ya devuelve el registro completo."],
  ["matriz_orden_poder_tramites", "(dentro de stats)", "En local la matriz de servicios viene en stats y no acepta filtros."],
  ["brechas_volumen", "brechas_volumen", "Igual."],
  ["comparar", "comparar", "Igual (misma metodología; ambos rechazan federal)."],
  ["buscar_apps", "buscar_apps", "El remoto trae los permisos dentro de cada app y filtra solo_permisos_peligrosos; el local acepta nivel (campo heredado)."],
  ["buscar_sitios", "buscar_sitios", "Igual; el local acepta nivel."],
  ["buscar_subdominio", "buscar_subdominio", "El remoto filtra además por estado."],
  ["catalogo_tramites", "catalogo_tramites", "El local acepta sondear=true (consulta al servidor oficial qué días hay)."],
  ["—", "permisos", "Solo local: análisis de permisos como lista propia, con filtros."],
  ["—", "dependencia", "Solo local: apps (con permisos) y sitios de una dependencia en un llamado."],
];

export default function GuiaMcp() {
  const tools = G.remoto.tools;
  return (
    <section className="seccion texto" style={{ maxWidth: 900 }}>
      <h1>MCP</h1>
      <MenuGuia actual="/guia/mcp" />
      <p>
        Un servidor <a href="https://modelcontextprotocol.io">MCP</a> deja que Claude, Cursor u otro asistente consulte el
        inventario por su cuenta: busca trámites, compara entidades, revisa apps. Hay dos: el <b>remoto</b>, sin instalar
        nada, y el <b>local</b>, que corre en tu máquina desde el repositorio. Ambos son de solo lectura.
      </p>
      <Indice items={[["conectar", "Conectar el MCP remoto"], ["como", "Cómo funciona"], ["tools", `Las ${tools.length} herramientas`],
        ["recetas", "Recetas"], ["local", "MCP local"], ["diferencias", "Remoto vs local"]]} />

      <h2 id="conectar">Conectar el MCP remoto</h2>
      <p>URL: <code>{URL_MCP}</code> · transporte Streamable HTTP · sin cuenta · servidor <code>{G.remoto.servidor}</code></p>
      <Codigo titulo="Claude Code">{`claude mcp add --transport http gobmx ${URL_MCP}
claude mcp list        # gobmx ... ✔ Connected`}</Codigo>
      <Codigo titulo="Claude (web y escritorio)">{`Configuración → Conectores → Agregar conector personalizado
Nombre: gobmx    URL: ${URL_MCP}`}</Codigo>
      <Codigo titulo="Cursor, Windsurf, VS Code y otros (mcp.json)">{`{
  "mcpServers": {
    "gobmx": { "url": "${URL_MCP}" }
  }
}`}</Codigo>
      <Codigo titulo="Clientes que solo hablan stdio">{`{
  "mcpServers": {
    "gobmx": { "command": "npx", "args": ["-y", "mcp-remote", "${URL_MCP}"] }
  }
}`}</Codigo>
      <Codigo titulo="Desde Python (SDK oficial, mcp<2)">{`from mcp import ClientSession
from mcp.client.streamable_http import streamablehttp_client

async with streamablehttp_client("${URL_MCP}") as (r, w, _):
    async with ClientSession(r, w) as s:
        await s.initialize()
        res = await s.call_tool("buscar_tramites", {"texto": "predial", "estado": "Yucatán"})
        print(res.content[0].text)   # JSON`}</Codigo>

      <h2 id="como">Cómo funciona</h2>
      <ul>
        <li>Lo sirve esta misma web (<code>webapp/app/mcp/route.ts</code>) con los mismos datos: se actualiza a diario con el corte de trámites.</li>
        <li>No guarda sesión ni pide cuenta. Cada respuesta es un texto JSON.</li>
        <li>Solo lectura: todas las herramientas se declaran <code>readOnlyHint</code>. No escribe, no guarda lo que preguntas.</li>
        <li>Instrucciones que recibe el asistente al conectarse: <i>{G.remoto.instrucciones}</i></li>
        <li>Para probarlo de punta a punta: <code>.venv/bin/python -I mcp-server/test_remoto.py</code> en el repositorio.</li>
      </ul>

      <h2 id="tools">Las {tools.length} herramientas</h2>
      <p className="dim">Parámetros tomados del esquema que publica el servidor. Respuestas reales del {G.generado}, recortadas (listas a 2 elementos, textos largos con “…”).</p>
      {tools.map((t) => (
        <article key={t.nombre} id={`tool-${t.nombre}`} className="panel" style={{ marginBottom: 20 }}>
          <h3 className="mono" style={{ fontFamily: "var(--f-mono)" }}>{t.nombre}</h3>
          <p><b>{t.titulo}.</b> {t.descripcion}</p>
          <Parametros esquema={t.esquema as never} />
          <Notas items={NOTAS[t.nombre] ?? []} />
          <Codigo titulo="Llamada de ejemplo">{`${t.nombre}(${JSON.stringify(t.ejemplo)})`}</Codigo>
          <details>
            <summary style={{ cursor: "pointer", color: "var(--guinda)", fontWeight: 600 }}>Respuesta real</summary>
            <pre style={{ maxHeight: 420, overflow: "auto" }}><code>{JSON.stringify(t.respuesta, null, 2)}</code></pre>
          </details>
        </article>
      ))}

      <h2 id="recetas">Recetas</h2>
      <p>Pégalas tal cual en tu asistente con el MCP conectado. La columna de la derecha dice qué herramienta usará.</p>
      <div className="tabla-scroll" style={{ marginBottom: 24 }}>
        <table>
          <thead><tr><th>Para</th><th>Pregunta</th><th>Herramientas</th></tr></thead>
          <tbody>{RECETAS.map(([p, q, h]) => <tr key={q}><td>{p}</td><td className="ancho">{q}</td><td className="ancho mono">{h}</td></tr>)}</tbody>
        </table>
      </div>
      <p className="dim">
        Consejo: pide que cite la homoclave y el enlace de la ficha oficial, y que diga cuando un dato es “sin dato”; así puedes
        verificar cada afirmación.
      </p>

      <h2 id="local">MCP local</h2>
      <p>
        Corre en tu máquina desde el repositorio (Python, FastMCP). Úsalo si quieres las herramientas <code>permisos</code> y{" "}
        <code>dependencia</code>, trabajar sin internet o modificar el servidor.
      </p>
      <Codigo titulo="Instalar (una vez) y registrar en Claude Code">{`git clone https://github.com/elimorals/gobmx-presencia-digital && cd gobmx-presencia-digital
uv venv .venv && VIRTUAL_ENV=.venv uv pip install "mcp<2"
claude mcp add gobmx-inventario -- "$PWD/.venv/bin/python" -I "$PWD/mcp-server/server.py"
claude mcp list        # gobmx-inventario ... ✔ Connected`}</Codigo>
      <Codigo titulo="Prueba de punta a punta (stdio)">{`uv run --no-project --with "mcp<2" python mcp-server/test_smoke.py`}</Codigo>
      <p>Herramientas del servidor local ({G.local.length}), con sus parámetros:</p>
      {G.local.map((t) => (
        <details key={t.nombre} className="panel" style={{ marginBottom: 8 }}>
          <summary style={{ cursor: "pointer" }}><b className="mono">{t.nombre}</b></summary>
          <p style={{ marginTop: 8, whiteSpace: "pre-line" }}>{t.descripcion}</p>
          <Parametros esquema={t.esquema as never} />
        </details>
      ))}

      <h2 id="diferencias" style={{ marginTop: 28 }}>Remoto vs local</h2>
      <p>Los nombres de algunas herramientas cambian. Esta tabla dice cuál corresponde a cuál.</p>
      <div className="tabla-scroll">
        <table>
          <thead><tr><th>Remoto</th><th>Local</th><th>Diferencia</th></tr></thead>
          <tbody>{EQUIV.map(([r, l, d]) => <tr key={r + l}><td className="mono">{r}</td><td className="mono">{l}</td><td className="ancho">{d}</td></tr>)}</tbody>
        </table>
      </div>
      <ul style={{ marginTop: 12 }}>
        <li><b>Frescura:</b> el remoto se actualiza a diario solo; el local usa tu copia del repositorio (<code>git pull</code>).</li>
        <li><b>Red:</b> el local no sale a internet, salvo <code>catalogo_tramites(sondear=true)</code>.</li>
        <li><b>Respuestas:</b> el local devuelve listas de objetos; el remoto, JSON en texto (las búsquedas de trámites con <code>total</code>, <code>pagina</code>, <code>paginas</code> e <code>items</code>).</li>
      </ul>
    </section>
  );
}
