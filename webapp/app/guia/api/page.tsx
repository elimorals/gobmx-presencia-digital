import type { Metadata } from "next";
import { Codigo, G, Indice, MenuGuia, Notas, RAW, SITIO } from "@/components/guia";

export const metadata: Metadata = {
  title: "Guía de la API y los datos",
  description: "API /api/tramites, campos de cada registro (trámite, app, sitio, subdominio, entidad) y descarga de los datos crudos.",
};

type Campo = [string, string, string];

const PARAMS: Campo[] = [
  ["q", "texto", "Palabras a buscar (todas deben aparecer) en nombre, dependencia, siglas, homoclave y etiquetas. Sin acentos ni mayúsculas."],
  ["orden", "federal | estatal | municipal", "Orden de gobierno."],
  ["poder", "ejecutivo | legislativo | judicial | autonomo", "Poder."],
  ["estado", "texto exacto", "Entidad con nombre corto: Veracruz, Michoacán, Coahuila, Estado de México… Federales: Nacional."],
  ["municipio", "texto", "Coincidencia parcial sin acentos."],
  ["en_linea", "si | no | sin_dato", "sin_dato = no se reporta canal (no es 'no')."],
  ["orden_por", "reciente", "Por defecto, de más a menos solicitudes; 'reciente' ordena por fecha de actualización."],
  ["pagina", "entero ≥ 1", "25 resultados por página."],
];

const TRAMITE: Campo[] = [
  ["id", "entero", "Identificador del registro. Negativo = trámite federal del dataset de la SFP (sin descripción ni canales)."],
  ["homoclave", "texto", "Clave oficial del trámite. Sirve para la ficha de gob.mx: /public/tramites/detalleTramite.xhtml?homoclave=…"],
  ["nombre_tramite", "texto", "Nombre oficial."],
  ["tipo_registro", "tramite | servicio | null", "Si es trámite (obligación) o servicio."],
  ["tipo, categoria", "texto | null", "Tipo (Solicitud, Licencia, Constancia…) y categoría temática."],
  ["dependencia, siglas, unidad_administrativa", "texto", "Quién lo atiende."],
  ["orden, poder", "enum", "Del registro de origen: Administración Pública y Municipio → ejecutivo; Poder Judicial/Legislativo; autonomía constitucional y tribunales contenciosos → autonomo."],
  ["subtipo", "texto | null", "Paraestatal, desconcentrado, universidad autónoma… (solo ejecutivo)."],
  ["estado, municipio", "texto | null", "Entidad (nombre corto; 'Nacional' si es federal) y municipio INEGI si es municipal."],
  ["en_linea, presencial, telefono", "booleano | null", "Canales. null = no se reporta."],
  ["canales_txt", "texto | null", "Canales como vienen en el origen."],
  ["frecuencia", "entero | null", "Solicitudes que reporta la dependencia (periodo no documentado)."],
  ["dirigido", "texto | null", "Social, Empresarial o Ambos."],
  ["fecha_actualizacion", "AAAA-MM-DD | null", "Última actualización en el registro."],
  ["descripcion_ciudadana, descripcion", "texto | null", "Descripción para la ciudadanía y la oficial."],
  ["modalidad, tags, scian", "opcionales", "Modalidad, etiquetas y clasificación SCIAN (sector…clase) cuando existen."],
  ["hosts", "lista | opcional", "Dominios .gob.mx citados en los textos (sin correos)."],
  ["programa_social, oculto", "true | opcional", "Solo aparecen si son verdaderos."],
  ["homoclave_sfp, nivel_digitalizacion_sfp, cruce_sfp", "opcionales", "Liga con el dataset de la SFP (por homoclave o por nombre+siglas únicos) y su nivel de digitalización."],
  ["ids_anteriores", "lista | opcional", "Versiones repetidas del mismo trámite que el origen publicaba y se quitaron."],
];

const APP: Campo[] = [
  ["nombre, dependencia, siglas", "texto", "Qué app y de quién."],
  ["orden, poder, subtipo, municipio, estado", "texto | null", "Clasificación; null en orden/poder solo para apps no oficiales."],
  ["sector, estatus", "texto", "Sector (seguridad, movilidad, salud…) y estatus (activa, retirada, sin actualizar >2 años, desconocido)."],
  ["android_id / ios_id", "texto | null", "Identificadores de tienda (en el JSON crudo; la web y el MCP remoto los llaman android / ios)."],
  ["requiere_cuenta, proposito, funciones", "varios", "Si pide cuenta, para qué sirve y qué hace."],
  ["play / appstore", "objeto", "Métricas de tienda: instalaciones, calificación, opiniones, versión."],
  ["permisos", "objeto | null", "En apps/permisos/permisos.json: permisos_total, permisos_peligrosos, trackers, notas, fecha."],
];

const SITIO_C: Campo[] = [
  ["dependencia, siglas, url, tipo", "texto", "Qué portal y de quién."],
  ["orden, poder, subtipo, municipio, estado", "texto | null", "Clasificación."],
  ["plataforma, anti_bot, autenticacion", "texto | null", "Con qué está hecho, si filtra bots (Cloudflare, reCAPTCHA, Imperva, Radware…; 'ninguno' o 'desconocido') y cómo se entra."],
  ["servicios_en_linea, api_publica", "varios", "Qué se puede hacer en línea y si hay API pública."],
  ["estado_real", "lista", "Evidencia fechada: fecha, url y resultado de cada comprobación (HTTP 200, TLS inválido…)."],
];

const SUB: Campo[] = [
  ["host", "texto", "Subdominio .gob.mx."],
  ["fuente", "texto", "nuestro-CT · dotgob-mx · ambos · cnartys-tramites (citado en un trámite)."],
  ["activo_dotgob", "texto", "activo:https / activo:http si respondió en el barrido de dotgob-mx; inactivo o vacío si no."],
  ["dependencia_o_estado", "texto", "Etiqueta del dominio; se mapea a entidad o municipio cuando se conoce."],
];

const ENT: Campo[] = [
  ["estado, cve_ent (municipio, cve_mun)", "texto", "Lugar y clave INEGI."],
  ["tramites, tramites_en_linea, tramites_sin_dato_canal, pct_tramites_en_linea", "número | null", "Trámites de ese orden y lugar; el % se calcula sobre los que reportan canal."],
  ["apps, apps_activas, sitios, subdominios, subdominios_activos, instalaciones", "número", "Presencia digital."],
  ["poderes, cobertura_poderes", "objeto, 0-4", "Apps + sitios por poder y cuántos poderes tienen alguno."],
  ["poblacion_2020", "número | null", "Censo 2020 (INEGI)."],
  ["egresos_mxn, egresos_anio, egreso_por_hab", "número | null", "Egreso total (INEGI EFIPEM) y por habitante."],
  ["transferencias_pef_mxn, transferencias_pef_anio", "número | null", "PEF Ramo 28 + Ramo 33 (solo entidades)."],
  ["por_100k_hab, por_mil_mdp, instalaciones_por_hab", "objeto | null", "Tasas por 100 mil habitantes y por mil millones de pesos de egreso."],
];

function Tabla({ filas }: { filas: Campo[] }) {
  return (
    <div className="tabla-scroll" style={{ marginBottom: 20 }}>
      <table>
        <thead><tr><th>Campo</th><th>Tipo</th><th>Qué es</th></tr></thead>
        <tbody>{filas.map(([c, t, d]) => <tr key={c}><td className="mono ancho">{c}</td><td>{t}</td><td className="ancho">{d}</td></tr>)}</tbody>
      </table>
    </div>
  );
}

const CRUDOS: [string, string, string, string][] = [
  ["Trámites (tres órdenes)", "servicios/cnartys/tramites.json", "JSON, un objeto por línea, ~41 MB", "Diario"],
  ["Metadatos del corte", "servicios/cnartys/meta.json", "JSON", "Diario"],
  ["Trámites federales de la SFP", "servicios/cnts-federal.json", "JSON", "Fijo (2026-10-20)"],
  ["Apps", "apps/apps.json", "JSON (también apps/apps.csv)", "Al regenerar"],
  ["Permisos de apps", "apps/permisos/permisos.json", "JSON", "Al regenerar"],
  ["Sitios (por segmento)", "sitios/federal.json", "JSON (un archivo por segmento en sitios/)", "Al regenerar"],
  ["Subdominios", "sitios/subdominios-consolidado.csv", "CSV", "Mensual"],
  ["Brechas por canal", "servicios/brechas.json", "JSON", "Al regenerar"],
  ["Presupuesto y población", "datos/presupuesto/entidades.json", "JSON (y municipios.json)", "Fijo (2026-10-21)"],
  ["Resumen para modelos", "llms.txt", "Texto", "Al cambiar"],
];

export default function GuiaApi() {
  return (
    <section className="seccion texto" style={{ maxWidth: 900 }}>
      <h1>API y datos</h1>
      <MenuGuia actual="/guia/api" />
      <Indice items={[["api", "GET /api/tramites"], ["campos", "Campos de cada registro"], ["crudos", "Datos crudos"], ["licencia", "Licencia y atribución"]]} />

      <h2 id="api">GET /api/tramites</h2>
      <p>Busca trámites y servicios. Sin cuenta ni llave. Respuesta JSON; se guarda en caché una hora.</p>
      <Tabla filas={PARAMS} />
      <Notas items={[
        "Responde { total, paginas, pagina, items } con los registros completos (campos abajo).",
        "Un estado mal escrito no da error: da 0 resultados. Usa los nombres cortos.",
        "Para un trámite por id usa la ficha de la web (/tramites/{id}) o el MCP (tramite).",
      ]} />
      <Codigo titulo="curl">{`curl "${SITIO}/api/tramites?q=predial&estado=Yucat%C3%A1n&en_linea=si"`}</Codigo>
      <Codigo titulo="JavaScript">{`const u = new URL("${SITIO}/api/tramites");
u.search = new URLSearchParams({ q: "licencia", estado: "Tabasco", pagina: "1" }).toString();
const { total, items } = await (await fetch(u)).json();`}</Codigo>
      <Codigo titulo="Python">{`import requests
r = requests.get("${SITIO}/api/tramites", params={"q": "predial", "estado": "Yucatán", "en_linea": "si"})
print(r.json()["total"])`}</Codigo>
      <details style={{ marginBottom: 24 }}>
        <summary style={{ cursor: "pointer", color: "var(--guinda)", fontWeight: 600 }}>Respuesta real ({G.generado})</summary>
        <p className="faint mono">{G.api.url}</p>
        <pre style={{ maxHeight: 420, overflow: "auto" }}><code>{JSON.stringify(G.api.respuesta, null, 2)}</code></pre>
      </details>

      <h2 id="campos">Campos de cada registro</h2>
      <h3>Trámite</h3>
      <Tabla filas={TRAMITE} />
      <h3>App</h3>
      <Tabla filas={APP} />
      <h3>Sitio</h3>
      <Tabla filas={SITIO_C} />
      <h3>Subdominio</h3>
      <Tabla filas={SUB} />
      <h3>Fila de comparación (entidad o municipio)</h3>
      <Tabla filas={ENT} />

      <h2 id="crudos">Datos crudos</h2>
      <p>Por HTTP, sin autenticación, desde GitHub. Para análisis masivo conviene esto antes que la API.</p>
      <div className="tabla-scroll" style={{ marginBottom: 20 }}>
        <table>
          <thead><tr><th>Qué</th><th>Archivo</th><th>Formato</th><th>Se actualiza</th></tr></thead>
          <tbody>{CRUDOS.map(([q, a, f, c]) => <tr key={a}><td>{q}</td><td className="mono ancho"><a href={`${RAW}/${a}`}>{a}</a></td><td>{f}</td><td>{c}</td></tr>)}</tbody>
        </table>
      </div>
      <Codigo titulo="Ejemplo: trámites en línea por entidad con jq">{`curl -s ${RAW}/servicios/cnartys/tramites.json \\
  | jq -r '.[] | select(.orden=="estatal" and .en_linea==true) | .estado' | sort | uniq -c | sort -rn | head`}</Codigo>

      <h2 id="licencia">Licencia y atribución</h2>
      <p>
        Código y datos derivados bajo licencia MIT. Las fuentes originales son públicas: registro nacional de trámites (CONAMER),
        dataset de la SFP, tiendas de apps, Certificate Transparency, dotgob-mx (MIT), INEGI y SHCP. Cita el proyecto y la fecha
        de corte; para afirmaciones sobre un trámite concreto, enlaza su ficha oficial.
      </p>
    </section>
  );
}
