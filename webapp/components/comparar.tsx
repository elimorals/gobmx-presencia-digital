import Link from "next/link";
import { fmt, fmtPesos, slug, urlCon } from "@/lib/datos";

export type Fila = Record<string, unknown> & { estado: string; municipio?: string | null };
type Fmt = "n" | "pct" | "pesos" | "dec" | "de4";
type Metrica = { k: string; t: string; f: Fmt; g: "abs" | "tram" | "hab" | "mdp" | "pef" | "pres" };

export const GRUPOS: [Metrica["g"], string][] = [
  ["abs", "Absolutos"],
  ["tram", "Trámites"],
  ["hab", "Por habitante · Censo 2020"],
  ["mdp", "Por presupuesto propio · cada mil millones de pesos de egreso (INEGI)"],
  ["pef", "Presupuesto de Egresos de la Federación · transferencias Ramo 28 + Ramo 33"],
  ["pres", "Presupuesto y población"],
];

// rutas con punto, como `gobmx comparar --ordenar`; las que empiezan con "_" se calculan aquí
export const METRICAS: Metrica[] = [
  { k: "apps", t: "Apps", f: "n", g: "abs" },
  { k: "apps_activas", t: "Apps activas", f: "n", g: "abs" },
  { k: "sitios", t: "Sitios web", f: "n", g: "abs" },
  { k: "subdominios", t: "Subdominios .gob.mx", f: "n", g: "abs" },
  { k: "instalaciones", t: "Instalaciones Android", f: "n", g: "abs" },
  { k: "cobertura_poderes", t: "Poderes con presencia digital", f: "de4", g: "abs" },
  { k: "tramites", t: "Trámites publicados", f: "n", g: "tram" },
  { k: "pct_tramites_en_linea", t: "% de trámites en línea", f: "pct", g: "tram" },
  { k: "tramites_sin_dato_canal", t: "Trámites sin dato de canal", f: "n", g: "tram" },
  { k: "_digital_100k", t: "Apps + sitios por 100 mil hab.", f: "dec", g: "hab" },
  { k: "por_100k_hab.apps", t: "Apps por 100 mil hab.", f: "dec", g: "hab" },
  { k: "por_100k_hab.apps_activas", t: "Apps activas por 100 mil hab.", f: "dec", g: "hab" },
  { k: "por_100k_hab.sitios", t: "Sitios por 100 mil hab.", f: "dec", g: "hab" },
  { k: "por_100k_hab.subdominios", t: "Subdominios por 100 mil hab.", f: "dec", g: "hab" },
  { k: "por_100k_hab.tramites", t: "Trámites por 100 mil hab.", f: "dec", g: "hab" },
  { k: "instalaciones_por_hab", t: "Instalaciones por habitante", f: "dec", g: "hab" },
  { k: "por_mil_mdp.apps", t: "Apps por mil mdp de egreso", f: "dec", g: "mdp" },
  { k: "por_mil_mdp.apps_activas", t: "Apps activas por mil mdp", f: "dec", g: "mdp" },
  { k: "por_mil_mdp.sitios", t: "Sitios por mil mdp", f: "dec", g: "mdp" },
  { k: "por_mil_mdp.subdominios", t: "Subdominios por mil mdp", f: "dec", g: "mdp" },
  { k: "transferencias_pef_mxn", t: "Transferencias PEF (R28 + R33)", f: "pesos", g: "pef" },
  { k: "_pef_por_hab", t: "PEF por habitante", f: "pesos", g: "pef" },
  { k: "_digital_por_mmdp_pef", t: "Apps + sitios por mil mdp del PEF", f: "dec", g: "pef" },
  { k: "_tramites_por_mmdp_pef", t: "Trámites por mil mdp del PEF", f: "dec", g: "pef" },
  { k: "poblacion_2020", t: "Población 2020", f: "n", g: "pres" },
  { k: "egresos_mxn", t: "Egreso anual (INEGI)", f: "pesos", g: "pres" },
  { k: "egreso_por_hab", t: "Egreso por habitante", f: "pesos", g: "pres" },
];
export const META = Object.fromEntries(METRICAS.map((m) => [m.k, m]));

const num = (v: unknown) => (typeof v === "number" ? v : null);

export function valor(f: Fila, ruta: string): number | null {
  const a = valor0(f, "por_100k_hab.apps"), s = valor0(f, "por_100k_hab.sitios");
  const pef = num(f.transferencias_pef_mxn), pob = num(f.poblacion_2020);
  switch (ruta) {
    case "_digital_100k": return a === null || s === null ? null : +(a + s).toFixed(3);
    case "_pef_por_hab": return pef && pob ? Math.round(pef / pob) : null;
    case "_digital_por_mmdp_pef": return pef ? +(((num(f.apps) ?? 0) + (num(f.sitios) ?? 0)) / (pef / 1e9)).toFixed(3) : null;
    case "_tramites_por_mmdp_pef": return pef ? +((num(f.tramites) ?? 0) / (pef / 1e9)).toFixed(3) : null;
    default: return valor0(f, ruta);
  }
}

function valor0(f: Fila, ruta: string): number | null {
  let v: unknown = f;
  for (const k of ruta.split(".")) v = v && typeof v === "object" ? (v as Record<string, unknown>)[k] : null;
  return num(v);
}

export function formato(v: number | null, f: Fmt = "n") {
  if (v === null) return "s/d";
  if (f === "pct") return `${v} %`;
  if (f === "pesos") return fmtPesos.format(v);
  if (f === "dec") return v.toLocaleString("es-MX", { maximumFractionDigits: 2 });
  if (f === "de4") return `${v}/4`;
  return fmt.format(v);
}

export function ordenar<T extends Fila>(filas: T[], ruta: string) {
  return [...filas].sort((a, b) => {
    const x = valor(a, ruta), y = valor(b, ruta);
    if (x === null) return y === null ? 0 : 1; // sin dato al final, como en el CLI
    if (y === null) return -1;
    return y - x;
  });
}

export const clave = (f: Fila) => (f.municipio ? `${f.municipio}, ${f.estado}` : f.estado);

/** Barras de una métrica para los seleccionados; sin dato = "s/d" sin barra (nunca cero). */
export function BarrasMetrica({ m, filas }: { m: Metrica; filas: Fila[] }) {
  const vals = filas.map((f) => ({ f, v: valor(f, m.k) }));
  const tope = Math.max(0, ...vals.map((x) => x.v ?? 0)) || 1;
  return (
    <div className="panel">
      <h3 style={{ fontSize: "1rem" }}>{m.t}</h3>
      <div className="barras">
        {vals.map(({ f, v }) => (
          <div className="barra" key={clave(f)}>
            <span>{f.municipio ?? f.estado}</span>
            <span className="pista" aria-hidden>{v !== null && <span className="relleno" style={{ width: `${(100 * Math.max(v, 0)) / tope}%` }} />}</span>
            <span className="v">{formato(v, m.f)}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export function Tabla({ filas, municipal, ordenarPor, base, sel, extra }: {
  filas: Fila[]; municipal?: boolean; ordenarPor: string; base: string; sel: string[]; extra: Record<string, string | undefined>;
}) {
  const cols = ["tramites", "pct_tramites_en_linea", "apps", "apps_activas", "sitios", "subdominios", "cobertura_poderes",
    "_digital_100k", "egreso_por_hab", ...(municipal ? [] : ["_pef_por_hab", "_digital_por_mmdp_pef"])];
  if (!cols.includes(ordenarPor)) cols.push(ordenarPor);
  return (
    <div className="tabla-scroll">
      <table>
        <thead>
          <tr>
            <th>{municipal ? "Municipio" : "Entidad"}</th>
            {cols.map((c) => (
              <th key={c} className="n">
                <Link href={urlCon(base, { ...extra, ordenar: c })} style={{ color: c === ordenarPor ? "var(--guinda)" : "inherit" }}>
                  {META[c].t}{c === ordenarPor ? " ↓" : ""}
                </Link>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {filas.map((f) => (
            <tr key={clave(f)} style={sel.includes(clave(f)) ? { outline: "2px solid var(--oro-punto)", outlineOffset: -2 } : undefined}>
              <td>
                {municipal
                  ? <Link href={urlCon("/tramites", { estado: f.estado, municipio: f.municipio ?? "", orden: "municipal" })}>{clave(f)}</Link>
                  : <Link href={`/entidades/${slug(f.estado)}`}>{f.estado}</Link>}
              </td>
              {cols.map((c) => <td key={c} className="n">{formato(valor(f, c), META[c].f)}</td>)}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const marcas = (max: number) => [0, 1, 2, 3, 4].map((i) => (max * i) / 4);
function corto(v: number, f: Fmt) {
  if (f === "pesos" || Math.abs(v) >= 10000) return v >= 1e9 ? `${(v / 1e9).toFixed(0)}mm` : v >= 1e6 ? `${(v / 1e6).toFixed(1)}M` : `${Math.round(v / 1000)}k`;
  return v >= 10 ? Math.round(v).toLocaleString("es-MX") : v.toFixed(1);
}

/** Dispersión con rejilla y marcas; seleccionados en dorado con nombre. Sin dato en un eje = fuera. */
export function Dispersion({ filas, x, y, sel = [] }: { filas: Fila[]; x: string; y: string; sel?: string[] }) {
  const mx = META[x], my = META[y];
  const pts = filas.map((f) => ({ f, x: valor(f, x), y: valor(f, y) })).filter((p) => p.x !== null && p.y !== null) as { f: Fila; x: number; y: number }[];
  if (pts.length < 2) return <p className="dim">Sin datos suficientes para graficar {mx.t.toLowerCase()} contra {my.t.toLowerCase()}.</p>;
  const W = 640, H = 380, m = { l: 52, r: 16, t: 16, b: 44 };
  const xmax = Math.max(...pts.map((p) => p.x)) * 1.05 || 1, ymax = Math.max(...pts.map((p) => p.y)) * 1.1 || 1;
  const X = (v: number) => m.l + (v / xmax) * (W - m.l - m.r);
  const Y = (v: number) => H - m.b - (v / ymax) * (H - m.t - m.b);
  const orden = [...pts].sort((a, b) => Number(sel.includes(clave(a.f))) - Number(sel.includes(clave(b.f)))); // seleccionados encima
  return (
    <figure style={{ margin: 0 }}>
      <svg className="grafica" viewBox={`0 0 ${W} ${H}`} role="img" aria-label={`${my.t} contra ${mx.t}`}>
        {marcas(xmax).map((v) => (
          <g key={`x${v}`}>
            <line className="rejilla-g" x1={X(v)} x2={X(v)} y1={m.t} y2={H - m.b} />
            <text x={X(v)} y={H - m.b + 16} textAnchor="middle">{corto(v, mx.f)}</text>
          </g>
        ))}
        {marcas(ymax).map((v) => (
          <g key={`y${v}`}>
            <line className="rejilla-g" x1={m.l} x2={W - m.r} y1={Y(v)} y2={Y(v)} />
            <text x={m.l - 6} y={Y(v) + 4} textAnchor="end">{corto(v, my.f)}</text>
          </g>
        ))}
        <text x={W - m.r} y={H - 8} textAnchor="end">{mx.t} →</text>
        <text x={m.l} y={m.t - 4} textAnchor="start">↑ {my.t}</text>
        {orden.map((p) => {
          const on = sel.includes(clave(p.f));
          const der = X(p.x) > W * 0.65;
          return (
            <g key={clave(p.f)}>
              <circle className={`p${on ? " sel" : ""}`} cx={X(p.x)} cy={Y(p.y)} r={on ? 6.5 : 4.5}>
                <title>{`${clave(p.f)}: ${formato(p.x, mx.f)} · ${formato(p.y, my.f)}`}</title>
              </circle>
              {on && <text className="etq" x={X(p.x) + (der ? -10 : 10)} y={Y(p.y) + 4} textAnchor={der ? "end" : "start"}>{p.f.municipio ?? p.f.estado}</text>}
            </g>
          );
        })}
      </svg>
      <figcaption className="faint">{pts.length} con dato en ambos ejes. En dorado, los seleccionados; toca o pasa el cursor sobre un punto para ver su nombre y valores.</figcaption>
    </figure>
  );
}
