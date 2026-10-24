import type { Metadata } from "next";
import { Codigo, G, Indice, MenuGuia, Notas } from "@/components/guia";

export const metadata: Metadata = {
  title: "Guía de la CLI gobmx",
  description: "La CLI gobmx comando por comando: flags, ejemplos con salida real y scripts para regenerar el inventario.",
};

const QUE: Record<string, string> = {
  stats: "Resumen del inventario: totales, conteos de apps por nivel/sector/estatus y las matrices orden × poder de apps, sitios y servicios (con las celdas en 0).",
  servicios: "Busca trámites y servicios: los del registro nacional (tres órdenes), los federales de la SFP que no están en él, el catálogo curado y las brechas. Columnas: servicio · nombre · homoclave · orden · poder · estado · municipio · en línea · solicitudes · brecha.",
  brechas: "Los trámites con más solicitudes que se marcan sin opción en línea, cruzados con apps y sitios de la misma dependencia.",
  catalogo: "Metadatos del corte de trámites cargado. Con --sondear consulta al servidor oficial qué archivos diarios existen; con --actualizar baja el más reciente y lo normaliza.",
  comparar: "Compara entidades (--orden estatal) o municipios (--orden municipal) con trámites, % en línea, apps, sitios, subdominios, cobertura de poderes y tasas por población y presupuesto. Rechaza federal.",
  apps: "Busca apps móviles de gobierno. Columnas: nombre · siglas · orden · poder · estado · sector · estatus · id Android.",
  sitios: "Busca portales web. Columnas: siglas · dependencia · orden · poder · estado · url.",
  permisos: "Permisos peligrosos y rastreadores por app (las 387 con versión Android).",
  dep: "Todo lo de una dependencia (por nombre o siglas): sus apps con id de tienda y sus sitios.",
};

const NOTAS: Record<string, string[]> = {
  servicios: [
    "--estado es parcial y sin mayúsculas: 'tabasco' sirve; los federales tienen 'Nacional'.",
    "--en-linea sin_dato = el registro no reporta canal; no significa 'no'.",
    "--busca busca la frase tal cual (subcadena), a diferencia de la web y el MCP, que buscan palabra por palabra.",
    "Con --json sale el registro completo (descripción, canales, hosts, nivel_digitalizacion_sfp…).",
    "No uses --sector ni --estatus en servicios: los trámites del registro nacional no tienen esos campos y quedarían fuera; solo las brechas y el catálogo curado los traen.",
  ],
  comparar: [
    "--ordenar acepta rutas con punto: tramites, pct_tramites_en_linea, apps, apps_activas, sitios, subdominios, cobertura_poderes, por_100k_hab.apps, por_100k_hab.tramites, por_mil_mdp.sitios, egreso_por_hab, instalaciones_por_hab, transferencias_pef_mxn… Las filas sin dato van al final.",
    "--nombres acepta coincidencias parciales sin acentos: \"nuevo leon, jalisco\".",
    "--orden federal devuelve código de salida 2 con la explicación.",
  ],
  catalogo: ["--actualizar escribe en servicios/cnartys/ y agrega hosts nuevos a sitios/subdominios-consolidado.csv.", "Solo existen en el servidor los 2 archivos más recientes."],
  apps: ["--nivel es el campo heredado; para filtrar usa --orden y --poder.", "--estatus: activa · retirada · 'sin actualizar >2 años' · desconocido."],
  sitios: ["--sector y --estatus no aplican a sitios."],
};

const SCRIPTS: [string, string][] = [
  ["scripts/regenerar.sh", "Regenera todo en orden: subdominios, clasificación orden/poder, apps (métricas de tienda), QA, trámites, dashboard y datos de la web. SIN_RED=1 para no consultar tiendas ni el registro."],
  ["scripts/cnartys.py actualizar | sondear | prueba", "Baja y normaliza el CSV diario de trámites; sondear dice qué fechas hay; prueba corre la autoprueba sin red."],
  ["scripts/build_web_data.py", "Empaqueta webapp/data/ (inventario, subdominios, SFP) con las funciones de gobmx.py."],
  ["scripts/build_guia.py", "Regenera webapp/data/guia.json: esquemas y respuestas reales del MCP y salidas reales de la CLI que usa esta guía."],
  ["scripts/test_orden_poder.py", "Pruebas del clasificador, comparar, trámites, métricas y brechas."],
  ["scripts/qa.py", "QA de consistencia del inventario (duplicados, enums, campos)."],
];

export default function GuiaCli() {
  return (
    <section className="seccion texto" style={{ maxWidth: 900 }}>
      <h1>CLI gobmx</h1>
      <MenuGuia actual="/guia/cli" />
      <p>
        Consulta el inventario desde la terminal. Un solo archivo (<code>gobmx.py</code>), Python 3, sin dependencias externas.
        Es también la única implementación de las métricas: el MCP y la web usan sus funciones.
      </p>
      <Indice items={[["instalar", "Instalar"], ["convenciones", "Convenciones"], ...G.cli.map((c) => [`cmd-${c.comando}`, c.comando] as [string, string]),
        ["python", "Como módulo de Python"], ["scripts", "Scripts de mantenimiento"]]} />

      <h2 id="instalar">Instalar</h2>
      <Codigo>{`git clone https://github.com/elimorals/gobmx-presencia-digital
cd gobmx-presencia-digital
python3 gobmx.py -h            # lista de comandos
python3 gobmx.py servicios -h  # flags de un comando
git pull                       # trae el corte de trámites más reciente`}</Codigo>

      <h2 id="convenciones">Convenciones</h2>
      <ul>
        <li><b>--orden</b>: federal · estatal · municipal (exacto). <b>--poder</b>: ejecutivo · legislativo · judicial · autonomo (exacto, sin acento).</li>
        <li><b>--estado</b>, <b>--sector</b>, <b>--estatus</b>, <b>--busca</b>: coincidencia parcial, sin distinguir mayúsculas.</li>
        <li><b>--json</b>: salida JSON completa para encadenar con <code>jq</code> u otros programas. Sin él: una línea por registro, con “·” donde no hay dato y “no” cuando el valor es falso.</li>
        <li><code>null</code> en JSON y “s/d” en tablas significan dato no disponible, nunca cero.</li>
      </ul>

      {G.cli.map((c) => (
        <article key={c.comando} id={`cmd-${c.comando}`} className="panel" style={{ marginBottom: 20 }}>
          <h3 className="mono" style={{ fontFamily: "var(--f-mono)" }}>gobmx {c.comando}</h3>
          <p>{QUE[c.comando]}</p>
          <Notas items={NOTAS[c.comando] ?? []} />
          <details style={{ marginBottom: 12 }}>
            <summary style={{ cursor: "pointer", color: "var(--guinda)", fontWeight: 600 }}>Ayuda completa (-h)</summary>
            <pre><code>{c.ayuda}</code></pre>
          </details>
          <Codigo titulo={`Ejemplo real (${G.generado})`}>{`$ ${c.ejemplo}\n${c.salida}`}</Codigo>
        </article>
      ))}

      <h2 id="python">Como módulo de Python</h2>
      <Codigo>{`import gobmx
r = gobmx.comparar("estatal", "Campeche, Tlaxcala", ordenar_por="pct_tramites_en_linea")
for f in r["filas"]:
    print(f["estado"], f["tramites"], f["pct_tramites_en_linea"])

gobmx.tramites()                     # los trámites del registro nacional (lista de dicts)
gobmx.servicios()                    # registro nacional + SFP + curado + brechas, sin duplicados
gobmx.brechas_volumen("estatal", limite=10)
gobmx.metricas("municipal")          # filas de comparación municipal`}</Codigo>

      <h2 id="scripts">Scripts de mantenimiento</h2>
      <div className="tabla-scroll">
        <table>
          <thead><tr><th>Script</th><th>Qué hace</th></tr></thead>
          <tbody>{SCRIPTS.map(([s, d]) => <tr key={s}><td className="mono ancho">{s}</td><td className="ancho">{d}</td></tr>)}</tbody>
        </table>
      </div>
    </section>
  );
}
