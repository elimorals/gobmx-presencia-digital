import "server-only";
import { readFileSync } from "node:fs";
import { join } from "node:path";
import inventario from "@/data/inventario.json";

export type Tramite = {
  id: number;
  homoclave: string;
  nombre_tramite: string;
  tipo_registro: "tramite" | "servicio" | null;
  tipo: string | null;
  categoria: string | null;
  dependencia: string;
  siglas: string | null;
  unidad_administrativa: string | null;
  orden: "federal" | "estatal" | "municipal";
  poder: "ejecutivo" | "legislativo" | "judicial" | "autonomo";
  subtipo: string | null;
  estado: string;
  municipio: string | null;
  en_linea: boolean | null;
  presencial: boolean | null;
  telefono: boolean | null;
  canales_txt: string | null;
  frecuencia: number | null;
  dirigido: string | null;
  fecha_actualizacion: string | null;
  descripcion_ciudadana: string | null;
  descripcion: string | null;
  modalidad?: string;
  tags?: string;
  programa_social?: boolean;
  hosts?: string[];
  nivel_digitalizacion_sfp?: string;
  homoclave_sfp?: string;
};

export type Inventario = typeof inventario;
export type Entidad = Inventario["entidades"][number];
export type App = Inventario["apps"][number];

export const inv = inventario;

// --- trámites: 31k registros, se leen una vez por instancia y se indexan para búsqueda ---
let cache: { lista: Tramite[]; texto: string[]; porId: Map<number, Tramite> } | null = null;

export function sinAcentos(s: string | null | undefined) {
  return (s ?? "").normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
}

function cargar() {
  if (!cache) {
    const lista: Tramite[] = JSON.parse(readFileSync(join(process.cwd(), "data", "tramites.json"), "utf8"));
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
  orden_por?: string; // frecuencia | reciente | relevancia
};

export const POR_PAGINA = 25;

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
  const total = res.length;
  const desde = (pagina - 1) * POR_PAGINA;
  return { total, paginas: Math.max(1, Math.ceil(total / POR_PAGINA)), items: res.slice(desde, desde + POR_PAGINA) };
}

export function topPorLugar(estado: string, orden: "estatal" | "municipal" | "federal", n = 10, municipio?: string) {
  return cargar()
    .lista.filter((t) => t.estado === estado && t.orden === orden && (!municipio || t.municipio === municipio))
    .sort((a, b) => (b.frecuencia ?? -1) - (a.frecuencia ?? -1))
    .slice(0, n);
}

// --- entidades ---
export function slug(s: string) {
  return sinAcentos(s).replace(/[^a-z0-9]+/g, "-").replace(/^-|-$/g, "");
}

export function entidadPorSlug(s: string) {
  return inv.entidades.find((e) => slug(e.estado) === s) ?? null;
}

export const ESTADOS = inv.entidades.map((e) => e.estado).sort((a, b) => a.localeCompare(b, "es"));

export const fmt = new Intl.NumberFormat("es-MX");
export const fmtCompacto = new Intl.NumberFormat("es-MX", { notation: "compact", maximumFractionDigits: 1 });

export function lugar(t: { orden: string; estado: string; municipio?: string | null }) {
  if (t.orden === "federal") return "Federal";
  return t.municipio ? `${t.municipio}, ${t.estado}` : t.estado;
}
