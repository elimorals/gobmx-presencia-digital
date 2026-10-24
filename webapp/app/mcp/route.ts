// MCP remoto (Streamable HTTP, sin sesión): el inventario gobmx como herramientas de solo lectura.
// Mismos datos que la web (lib/datos.ts). Conectar: { "url": "https://<sitio>/mcp" }.
import { createMcpHandler } from "mcp-handler";
import { z } from "zod";
import { buscar, filtrar, inv, sinAcentos, subdominios, tramite, type Filtros } from "@/lib/datos";

const FICHA = "https://www.gob.mx/public/tramites/detalleTramite.xhtml?homoclave=";
const SOLO_LECTURA = { readOnlyHint: true, openWorldHint: false } as const;
const json = (x: unknown) => ({ content: [{ type: "text" as const, text: JSON.stringify(x) }] });
const contiene = (texto: string | undefined, ...campos: unknown[]) =>
  !texto || sinAcentos(campos.filter(Boolean).join(" ")).includes(sinAcentos(texto));

const orden = z.enum(["federal", "estatal", "municipal"]).optional().describe("Orden de gobierno");
const poder = z.enum(["ejecutivo", "legislativo", "judicial", "autonomo"]).optional().describe("Poder");
const limite = (d: number, max: number) => z.number().int().min(1).max(max).default(d).describe(`Máximo de resultados (≤ ${max})`);

function valor(f: Record<string, unknown>, ruta: string): number | null {
  let v: unknown = f;
  for (const k of ruta.split(".")) v = v && typeof v === "object" ? (v as Record<string, unknown>)[k] : null;
  return typeof v === "number" ? v : null;
}

const handler = createMcpHandler(
  (server) => {
    server.registerTool("stats", {
      title: "Resumen del inventario",
      description: "Totales (trámites, apps, sitios, subdominios), trámites por orden, poder y canal, matrices orden × poder de trámites, apps y sitios, y la fecha de corte de los trámites.",
      inputSchema: z.object({}),
      annotations: SOLO_LECTURA,
    }, async () => json({ resumen: inv.resumen, orden_poder: inv.orden_poder, corte_tramites: inv.catalogo.fecha_corte, nota_ceros: inv.cobertura_ceros }));

    server.registerTool("buscar_tramites", {
      title: "Buscar trámites",
      description: "Busca entre ~35.8 mil trámites y servicios federales, estatales y municipales de México. Devuelve 25 por página, de más a menos solicitados, con id para pedir la ficha con `tramite`. en_linea: si | no | sin_dato (el registro no reporta canal; no es lo mismo que 'no').",
      inputSchema: z.object({
        texto: z.string().optional().describe("Palabras a buscar en nombre, dependencia, siglas u homoclave"),
        orden, poder,
        estado: z.string().optional().describe("Entidad con su nombre corto, p. ej. 'Yucatán', 'Estado de México', 'Veracruz'. Los federales tienen 'Nacional'"),
        municipio: z.string().optional().describe("Municipio (coincidencia parcial, sin acentos)"),
        en_linea: z.enum(["si", "no", "sin_dato"]).optional(),
        orden_por: z.enum(["frecuencia", "reciente"]).default("frecuencia"),
        pagina: z.number().int().min(1).default(1),
      }),
      annotations: SOLO_LECTURA,
    }, async (a) => {
      const f: Filtros = { q: a.texto, orden: a.orden, poder: a.poder, estado: a.estado, municipio: a.municipio, en_linea: a.en_linea, orden_por: a.orden_por === "reciente" ? "reciente" : undefined };
      const r = buscar(f, a.pagina);
      return json({
        total: r.total, pagina: r.pagina, paginas: r.paginas,
        items: r.items.map((t) => ({ id: t.id, homoclave: t.homoclave, nombre: t.nombre_tramite, dependencia: t.dependencia, siglas: t.siglas, orden: t.orden, poder: t.poder, estado: t.estado, municipio: t.municipio, en_linea: t.en_linea, frecuencia: t.frecuencia })),
      });
    });

    server.registerTool("tramite", {
      title: "Ficha de un trámite",
      description: "Ficha completa de un trámite por id (de buscar_tramites): descripción, canales, dependencia, solicitudes reportadas, sitios citados y el enlace a la ficha oficial de gob.mx (requisitos y costos). Algunas fichas de gob.mx dan 404 (p. ej. parte de CDMX): el trámite no está publicado ahí.",
      inputSchema: z.object({ id: z.number().int().describe("id del trámite; negativo = registro del dataset federal de la SFP") }),
      annotations: SOLO_LECTURA,
    }, async ({ id }) => {
      const t = tramite(id);
      return t ? json({ ...t, ficha_oficial: FICHA + encodeURIComponent(t.homoclave) }) : json({ error: `No existe el trámite ${id}` });
    });

    server.registerTool("matriz_orden_poder_tramites", {
      title: "Trámites por orden y poder",
      description: "Cuenta trámites por orden × poder con los mismos filtros de buscar_tramites (sin orden ni poder). Un 0 significa que no hay trámites en ese cruce.",
      inputSchema: z.object({ texto: z.string().optional(), estado: z.string().optional(), municipio: z.string().optional(), en_linea: z.enum(["si", "no", "sin_dato"]).optional() }),
      annotations: SOLO_LECTURA,
    }, async (a) => {
      const m: Record<string, Record<string, number>> = {};
      for (const t of filtrar({ q: a.texto, estado: a.estado, municipio: a.municipio, en_linea: a.en_linea }, true)) {
        (m[t.orden] ??= { ejecutivo: 0, legislativo: 0, judicial: 0, autonomo: 0 })[t.poder] = (m[t.orden]?.[t.poder] ?? 0) + 1;
      }
      return json(m);
    });

    server.registerTool("brechas_volumen", {
      title: "Mucha demanda, sin opción en línea",
      description: "Los trámites con más solicitudes reportadas que el registro marca sin opción en línea, cruzados con las apps y sitios de la misma dependencia (si existen, el canal está pero el trámite no se ofrece por él).",
      inputSchema: z.object({ orden, estado: z.string().optional(), limite: limite(30, 200) }),
      annotations: SOLO_LECTURA,
    }, async ({ orden: o, estado, limite: n }) =>
      json(inv.brechas.filter((b) => (!o || b.orden === o) && contiene(estado, b.estado)).slice(0, n)));

    server.registerTool("comparar", {
      title: "Comparar entidades o municipios",
      description: "Compara entidades (orden='estatal', las 32) o municipios (orden='municipal'): trámites y % en línea, apps, sitios, subdominios, cobertura de poderes, población, egreso INEGI y (entidades) transferencias del PEF Ramo 28+33, con tasas por 100 mil habitantes y por mil millones de pesos. null = sin dato, nunca cero. RECHAZA el orden federal: su presupuesto, extensión y población no son comparables.",
      inputSchema: z.object({
        orden: z.string().describe("'estatal' o 'municipal'"),
        nombres: z.string().optional().describe("Lista separada por comas, p. ej. 'Campeche, Tlaxcala'; vacío = todos"),
        ordenar_por: z.string().optional().describe("Métrica con ruta de punto para un ranking descendente: tramites, pct_tramites_en_linea, apps, por_100k_hab.apps, por_mil_mdp.sitios, egreso_por_hab, transferencias_pef_mxn…"),
      }),
      annotations: SOLO_LECTURA,
    }, async ({ orden: o, nombres, ordenar_por }) => {
      const ord = sinAcentos(o);
      if (ord !== "estatal" && ord !== "municipal")
        return json({ error: "No se compara el orden federal (ni otro): su presupuesto, su extensión y la población que atiende no son comparables con los de una entidad o un municipio. Usa 'estatal' o 'municipal'." });
      let filas = (ord === "estatal" ? inv.entidades : inv.municipios) as unknown as Record<string, unknown>[];
      const ns = (nombres ?? "").split(",").map((s) => sinAcentos(s.trim())).filter(Boolean);
      if (ns.length) filas = filas.filter((f) => ns.some((n) => sinAcentos(String(f.municipio ?? f.estado)).includes(n)));
      if (ordenar_por) filas = [...filas].sort((a, b) => {
        const x = valor(a, ordenar_por), y = valor(b, ordenar_por);
        return x === null ? (y === null ? 0 : 1) : y === null ? -1 : y - x;
      });
      return json({ orden: ord, metodologia: inv.metodologia, referencia_federal: inv.referencia_federal, filas });
    });

    server.registerTool("buscar_apps", {
      title: "Buscar apps de gobierno",
      description: "Apps móviles de gobierno (519) con estatus, instalaciones, calificaciones y análisis de permisos (peligrosos y rastreadores) cuando hay versión Android.",
      inputSchema: z.object({
        texto: z.string().optional(), orden, poder, estado: z.string().optional(), sector: z.string().optional(),
        estatus: z.string().optional().describe("activa | retirada | 'sin actualizar >2 años' | desconocido"),
        solo_permisos_peligrosos: z.boolean().default(false), limite: limite(30, 200),
      }),
      annotations: SOLO_LECTURA,
    }, async (a) => json(inv.apps
      .filter((x) => (!a.orden || x.orden === a.orden) && (!a.poder || x.poder === a.poder) && contiene(a.estado, x.estado))
      .filter((x) => contiene(a.sector, x.sector) && contiene(a.estatus, x.estatus) && (!a.solo_permisos_peligrosos || (x.permisos?.peligrosos.length ?? 0) > 0))
      .filter((x) => contiene(a.texto, x.nombre, x.dependencia, x.siglas, x.proposito))
      .sort((p, q) => (q.instalaciones ?? 0) - (p.instalaciones ?? 0)).slice(0, a.limite)));

    server.registerTool("buscar_sitios", {
      title: "Buscar portales web de gobierno",
      description: "Portales web de gobierno (394) con plataforma, protección anti-bot, autenticación y evidencia fechada de si responden.",
      inputSchema: z.object({ texto: z.string().optional(), orden, poder, estado: z.string().optional(), limite: limite(30, 200) }),
      annotations: SOLO_LECTURA,
    }, async (a) => json(inv.sitios
      .filter((x) => (!a.orden || x.orden === a.orden) && (!a.poder || x.poder === a.poder) && contiene(a.estado, x.estado))
      .filter((x) => contiene(a.texto, x.dependencia, x.siglas, x.url, x.proposito)).slice(0, a.limite)));

    server.registerTool("buscar_subdominio", {
      title: "Buscar subdominios .gob.mx",
      description: "Busca en ~12.8 mil subdominios .gob.mx (Certificate Transparency, dotgob-mx y citados en trámites). 'activo' = respondió a HTTP/S en el último barrido de dotgob-mx.",
      inputSchema: z.object({ texto: z.string().optional().describe("Parte del host"), solo_activos: z.boolean().default(false), estado: z.string().optional(), limite: limite(100, 500) }),
      annotations: SOLO_LECTURA,
    }, async (a) => json(subdominios
      .filter((s) => (!a.texto || s.host.includes(a.texto.toLowerCase())) && (!a.solo_activos || s.activo === "activo") && contiene(a.estado, s.estado))
      .slice(0, a.limite)));

    server.registerTool("catalogo_tramites", {
      title: "Corte de los trámites",
      description: "Metadatos del corte de trámites cargado: fecha, archivo de origen, sha256, conteos, duplicados quitados y la regla de descarga diaria.",
      inputSchema: z.object({}),
      annotations: SOLO_LECTURA,
    }, async () => json(inv.catalogo));
  },
  {
    serverInfo: { name: "gobmx-inventario", version: "1.0.0" },
    instructions:
      "Inventario abierto y no oficial de la presencia digital del gobierno de México: trámites de los tres órdenes, apps, sitios y subdominios. Solo lectura. Para un trámite: buscar_tramites → tramite(id) → ficha_oficial (gob.mx) para requisitos y costos. Para comparar: comparar(orden='estatal'|'municipal'); el orden federal no se compara.",
  },
);

export { handler as GET, handler as POST, handler as DELETE };
