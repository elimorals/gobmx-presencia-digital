import "server-only";
import { readFileSync } from "node:fs";
import { join } from "node:path";
import inventario from "@/data/inventario.json";
import subdominiosCrudos from "@/data/subdominios.json";
import sfpCrudos from "@/data/sfp.json";

export type Tramite = {
  id: number; // negativo = trámite federal del dataset de la SFP (no ligado a uno del catálogo)
  homoclave: string;
  nombre_tramite: string;
  tipo_registro?: "tramite" | "servicio" | null;
  tipo?: string | null;
  categoria?: string | null;
  dependencia: string;
  siglas: string | null;
  unidad_administrativa?: string | null;
  orden: "federal" | "estatal" | "municipal";
  poder: string;
  subtipo?: string | null;
  estado: string;
  municipio: string | null;
  en_linea: boolean | null;
  presencial?: boolean | null;
  telefono?: boolean | null;
  canales_txt?: string | null;
  frecuencia: number | null;
  dirigido?: string | null;
  fecha_actualizacion?: string | null;
  descripcion_ciudadana?: string | null;
  descripcion?: string | null;
  modalidad?: string;
  tags?: string;
  programa_social?: boolean;
  hosts?: string[];
  nivel_digitalizacion_sfp?: string | null;
  homoclave_sfp?: string;
  fuente?: "sfp";
  posible_duplicado?: boolean;
};

export type Inventario = typeof inventario;
export type Entidad = Inventario["entidades"][number];
export type Municipio = Inventario["municipios"][number];
export type App = Inventario["apps"][number];
export type Sitio = Inventario["sitios"][number];

export const inv = inventario;

export function sinAcentos(s: string | null | undefined) {
  return (s ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
}

// --- trámites: ~32k del catálogo + ~4k federales de la SFP; se leen una vez por instancia ---
let cache: { lista: Tramite[]; texto: string[]; porId: Map<number, Tramite> } | null = null;

function cargar() {
  if (!cache) {
    const catalogo: Tramite[] = JSON.parse(readFileSync(join(process.cwd(), "data", "tramites.json"), "utf8"));
    const lista = [...catalogo, ...(sfpCrudos as Tramite[])];
    cache = {
      lista,
      texto: lista.map((t) => sinAcentos(`${t.nombre_tramite} ${t.dependencia} ${t.siglas ?? ""} ${t.homoclave} ${t.tags ?? ""}`)),
      porId: new Map(lista.map((t) => [t.id, t])),
    };
  }
  return cache;
}

export function tramite(id: number) {
  return cargar().porId.get(id) ?? null;
}

export type Filtros = {
  q?: string;
  orden?: string;
  estado?: string;
  municipio?: string;
  en_linea?: string; // si | no | sin_dato
  orden_por?: string; // frecuencia (default) | reciente
};

export const POR_PAGINA = 25;

export function paginar<T>(items: T[], pagina: number, porPagina = POR_PAGINA) {
  const total = items.length;
  const paginas = Math.max(1, Math.ceil(total / porPagina));
  const p = Math.min(Math.max(1, pagina), paginas);
  return { total, paginas, pagina: p, items: items.slice((p - 1) * porPagina, p * porPagina) };
}

export function buscar(f: Filtros, pagina = 1) {
  const { lista, texto } = cargar();
  const palabras = sinAcentos(f.q).split(/\s+/).filter(Boolean);
  const mun = sinAcentos(f.municipio);
  const res: Tramite[] = [];
  for (let i = 0; i < lista.length; i++) {
    const t = lista[i];
    if (f.orden && t.orden !== f.orden) continue;
    if (f.estado && t.estado !== f.estado) continue;
    if (mun && !sinAcentos(t.municipio).includes(mun)) continue;
    if (f.en_linea === "si" && t.en_linea !== true) continue;
    if (f.en_linea === "no" && t.en_linea !== false) continue;
    if (f.en_linea === "sin_dato" && t.en_linea !== null) continue;
    if (palabras.length && !palabras.every((p) => texto[i].includes(p))) continue;
    res.push(t);
  }
  if (f.orden_por === "reciente") res.sort((a, b) => (b.fecha_actualizacion ?? "").localeCompare(a.fecha_actualizacion ?? ""));
  else res.sort((a, b) => (b.frecuencia ?? -1) - (a.frecuencia ?? -1)); // por defecto: lo más solicitado primero
  return paginar(res, pagina);
}

export function topPorLugar(estado: string, orden: "estatal" | "municipal" | "federal", n = 10, municipio?: string) {
  return cargar()
    .lista.filter((t) => t.estado === estado && t.orden === orden && (!municipio || t.municipio === municipio))
    .sort((a, b) => (b.frecuencia ?? -1) - (a.frecuencia ?? -1))
    .slice(0, n);
}

// --- subdominios ---
export type Subdominio = { host: string; fuente: string; activo: string | null; dep: string; estado: string | null; municipio: string | null };
export const subdominios: Subdominio[] = (subdominiosCrudos as (string | null)[][]).map(([host, fuente, activo, dep, estado, municipio]) => ({
  host: host as string, fuente: fuente as string, activo, dep: dep as string, estado, municipio,
}));

// --- utilidades ---
export function slug(s: string) {
  return sinAcentos(s).replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");
}

export function entidadPorSlug(s: string) {
  return inv.entidades.find((e) => slug(e.estado) === s) ?? null;
}

export const ESTADOS = inv.entidades.map((e) => e.estado).sort((a, b) => a.localeCompare(b, "es"));

export function conteo<T>(items: T[], f: (x: T) => string | null | undefined) {
  const c = new Map<string, number>();
  for (const x of items) {
    const k = f(x) || "sin dato";
    c.set(k, (c.get(k) ?? 0) + 1);
  }
  return [...c.entries()].sort((a, b) => b[1] - a[1]);
}

export const fmt = new Intl.NumberFormat("es-MX");
export const fmtCompacto = new Intl.NumberFormat("es-MX", { notation: "compact", maximumFractionDigits: 1 });
export const fmtPesos = new Intl.NumberFormat("es-MX", { style: "currency", currency: "MXN", notation: "compact", maximumFractionDigits: 1 });

export function lugar(t: { orden?: string | null; estado?: string | null; municipio?: string | null }) {
  if (t.orden === "federal") return "Federal";
  return t.municipio ? `${t.municipio}, ${t.estado}` : t.estado ?? "";
}

export const uno = (v: string | string[] | undefined) => (Array.isArray(v) ? v[0] : v) || undefined;

export function urlCon(base: string, params: Record<string, string | number | undefined>) {
  const q = new URLSearchParams(Object.entries(params).filter(([, v]) => v !== undefined && v !== "").map(([k, v]) => [k, String(v)]));
  const s = q.toString();
  return s ? `${base}?${s}` : base;
}
