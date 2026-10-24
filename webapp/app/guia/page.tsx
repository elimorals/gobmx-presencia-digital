import type { Metadata } from "next";
import Link from "next/link";
import { Codigo, G, Indice, MenuGuia, SITIO } from "@/components/guia";
import { fmt, inv } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Guía: web, MCP, CLI y API",
  description: "Cómo consultar el inventario de trámites, apps y sitios del gobierno de México: MCP remoto y local, CLI, API y datos crudos.",
};

const CAMINOS: [string, string, string, string, string][] = [
  ["Esta web", "Cualquier persona", "Nada", "Diario", "/tramites"],
  ["MCP remoto", "Periodistas, analistas, quien usa Claude o Cursor", "Un cliente MCP", "Diario", "/guia/mcp"],
  ["MCP local", "Quien quiere permisos por app, dependencia o trabajar sin internet", "Python 3 + uv + clonar el repo", "Cuando actualizas tu copia", "/guia/mcp#local"],
  ["CLI gobmx", "Desarrolladores, scripts, terminal", "Python 3 (sin dependencias) + clonar el repo", "Cuando actualizas tu copia", "/guia/cli"],
  ["API /api/tramites", "Apps y sitios que integran el buscador", "Una petición HTTP", "Diario", "/guia/api"],
  ["Datos crudos", "Análisis masivo, pipelines", "Descargar JSON/CSV", "Diario (trámites) o mensual", "/guia/api#crudos"],
];

const PROBLEMAS: [string, string][] = [
  ["El cliente no conecta al MCP remoto",
    `Revisa que la URL termine en /mcp y use https (${SITIO}/mcp). El servidor solo habla Streamable HTTP: si tu cliente solo soporta stdio (versiones viejas de Claude Desktop y otros), usa el puente mcp-remote: npx -y mcp-remote ${SITIO}/mcp.`],
  ["Mi cliente pide el transporte SSE o un /sse",
    "Ese transporte (2024) ya no se sirve. Configura el cliente como Streamable HTTP (en Claude Code: --transport http) o usa mcp-remote."],
  ["Una llamada GET a /mcp responde 405",
    "Es normal: el servidor no guarda sesión, así que no abre flujos GET. Los clientes MCP hablan por POST."],
  ["El MCP local no arranca o tarda más de 30 s",
    "uv run --with \"mcp<2\" en caché fría puede pasar del tiempo de espera del cliente. Crea el venv una vez (uv venv .venv && VIRTUAL_ENV=.venv uv pip install \"mcp<2\") y registra .venv/bin/python -I mcp-server/server.py. Revisa con claude mcp list."],
  ["ImportError o 'mcp' sin FastMCP en el MCP local",
    "El servidor usa mcp 1.x. En 2.x FastMCP cambió de nombre. Instala \"mcp<2\". Y no crees una carpeta llamada mcp en la raíz: ensombrece el paquete."],
  ["La ficha oficial de gob.mx da 404",
    "Ese trámite está en el registro nacional pero no publicado en gob.mx (pasa con parte de CDMX). Búscalo en el portal de la dependencia con su homoclave."],
  ["gobmx catalogo --actualizar falla con 403",
    "Akamai rechaza algunos User-Agent y a veces IPs de servidores. El script usa Mozilla/5.0; si falla desde tu red, prueba desde otra o usa --csv con un archivo bajado a mano desde el navegador. Solo existen los 2 archivos más recientes."],
  ["SSL: CERTIFICATE_VERIFY_FAILED en macOS",
    "El Python de python.org no trae certificados. Usa el del venv (trae certifi) o corre /Applications/Python 3.x/Install Certificates.command."],
  ["Una búsqueda no encuentra lo obvio",
    "La búsqueda pide todas las palabras. Usa menos palabras o sinónimos (en Tabasco las licencias dicen 'chófer', no 'conducir'). Las entidades van con nombre corto: Veracruz, Michoacán, Coahuila, Estado de México."],
  ["Un estado casi no tiene trámites",
    "No es error: cada entidad captura lo que quiere en el registro nacional (Jalisco publica 1, Baja California 4). Eso mide la captura, no los trámites reales."],
];

export default function Guia() {
  return (
    <section className="seccion texto">
      <h1>Guía</h1>
      <MenuGuia actual="/guia" />
      <p>
        El mismo inventario —{fmt.format(inv.resumen.servicios)} trámites y servicios de los tres órdenes, {inv.resumen.apps} apps,{" "}
        {inv.resumen.sitios} sitios y {fmt.format(inv.resumen.subdominios.total)} subdominios— se consulta de seis formas.
        Todas leen los mismos archivos del repositorio; ninguna escribe nada. Ejemplos y respuestas de esta guía son reales,
        sacados de producción el {G.generado}.
      </p>
      <Indice items={[["cual", "¿Cuál uso?"], ["rapido", "Empezar en un minuto"], ["recetas", "Recetas"], ["problemas", "Solución de problemas"]]} />

      <h2 id="cual">¿Cuál uso?</h2>
      <div className="tabla-scroll" style={{ marginBottom: 24 }}>
        <table>
          <thead><tr><th>Forma</th><th>Para quién</th><th>Qué necesitas</th><th>Datos al día</th></tr></thead>
          <tbody>{CAMINOS.map(([f, q, n, a, h]) => <tr key={f}><td><Link href={h}>{f}</Link></td><td className="ancho">{q}</td><td className="ancho">{n}</td><td>{a}</td></tr>)}</tbody>
        </table>
      </div>

      <h2 id="rapido">Empezar en un minuto</h2>
      <Codigo titulo="MCP remoto en Claude Code">{`claude mcp add --transport http gobmx ${SITIO}/mcp`}</Codigo>
      <Codigo titulo="CLI">{`git clone https://github.com/elimorals/gobmx-presencia-digital && cd gobmx-presencia-digital
python3 gobmx.py servicios --estado Yucatán --busca predial --en-linea si`}</Codigo>
      <Codigo titulo="API">{`curl "${SITIO}/api/tramites?q=predial&estado=Yucat%C3%A1n&en_linea=si"`}</Codigo>

      <h2 id="recetas">Recetas</h2>
      <p>Preguntas para pegar en Claude (u otro cliente) con el MCP conectado, y la herramienta que usa por debajo.</p>
      <p><Link href="/guia/mcp#recetas">Ver las recetas para periodistas, desarrolladores y ciudadanos →</Link></p>

      <h2 id="problemas">Solución de problemas</h2>
      {PROBLEMAS.map(([p, s]) => (
        <details key={p} className="panel" style={{ marginBottom: 8 }}>
          <summary style={{ cursor: "pointer", fontWeight: 600 }}>{p}</summary>
          <p style={{ margin: "8px 0 0" }}>{s}</p>
        </details>
      ))}
    </section>
  );
}
