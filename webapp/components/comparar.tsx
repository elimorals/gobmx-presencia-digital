import Link from "next/link";
import { fmt, fmtPesos, slug, urlCon } from "@/lib/datos";

type Fila = Record<string, unknown> & { estado: string; municipio?: string | null };

// métricas con ruta de punto, igual que `gobmx comparar --ordenar`
export const METRICAS: { k: string; t: string; f?: "pct" | "pesos" | "dec" }[] = [
  { k: "tramites", t: "Trámites" },
  { k: "pct_tramites_en_linea", t: "% trámites en línea", f: "pct" },
  { k: "apps", t: "Apps" },
  { k: "apps_activas", t: "Apps activas" },
  { k: "instalaciones", t: "Instalaciones" },
  { k: "sitios", t: "Sitios" },
  { k: "subdominios", t: "Subdominios" },
  { k: "subdominios_activos", t: "Subdominios activos" },
  { k: "cobertura_poderes", t: "Poderes con presencia (0-4)" },
  { k: "poblacion_2020", t: "Población 2020" },
  { k: "egresos_mxn", t: "Egreso anual", f: "pesos" },
  { k: "egreso_por_hab", t: "Egreso por habitante", f: "pesos" },
  { k: "transferencias_pef_mxn", t: "Transferencias PEF (R28+R33)", f: "pesos" },
  { k: "por_100k_hab.apps", t: "Apps por 100 mil hab.", f: "dec" },
  { k: "por_100k_hab.sitios", t: "Sitios por 100 mil hab.", f: "dec" },
  { k: "por_100k_hab.tramites", t: "Trámites por 100 mil hab.", f: "dec" },
  { k: "por_mil_mdp.apps", t: "Apps por mil mdp de egreso", f: "dec" },
  { k: "por_mil_mdp.sitios", t: "Sitios por mil mdp de egreso", f: "dec" },
  { k: "instalaciones_por_hab", t: "Instalaciones por habitante", f: "dec" },
];

export function valor(f: Fila, ruta: string): number | null {
  let v: unknown = f;
  for (const k of ruta.split(".")) v = v && typeof v === "object" ? (v as Record<string, unknown>)[k] : null;
  return typeof v === "number" ? v : null;
}

export function formato(v: number | null, f?: string) {
  if (v === null) return "s/d";
  if (f === "pct") return `${v} %`;
  if (f === "pesos") return fmtPesos.format(v);
  if (f === "dec") return v.toLocaleString("es-MX", { maximumFractionDigits: 2 });
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

export function SelectorMetrica({ base, ordenar: actual, x, y }: { base: string; ordenar: string; x: string; y: string }) {
  const opts = METRICAS.map((m) => <option key={m.k} value={m.k}>{m.t}</option>);
  return (
    <form action={base} className="panel" style={{ marginBottom: 16 }}>
      <div className="filtros">
        <label>Ordenar tabla por<select name="ordenar" defaultValue={actual}>{opts}</select></label>
        <label>Gráfica: eje horizontal<select name="x" defaultValue={x}>{opts}</select></label>
        <label>Gráfica: eje vertical<select name="y" defaultValue={y}>{opts}</select></label>
      </div>
      <div className="acciones"><button className="boton" type="submit">Aplicar</button></div>
    </form>
  );
}

export function Tabla({ filas, municipal, ordenarPor, base }: { filas: Fila[]; municipal?: boolean; ordenarPor: string; base: string }) {
  const cols = ["tramites", "pct_tramites_en_linea", "apps", "apps_activas", "sitios", "subdominios", "cobertura_poderes",
    "poblacion_2020", "egreso_por_hab", "por_100k_hab.apps", "por_mil_mdp.apps"];
  if (!cols.includes(ordenarPor)) cols.push(ordenarPor);
  const meta = (k: string) => METRICAS.find((m) => m.k === k)!;
  return (
    <div className="tabla-scroll">
      <table>
        <thead>
          <tr>
            <th>{municipal ? "Municipio" : "Entidad"}</th>
            {cols.map((c) => (
              <th key={c} className="n">
                <Link href={urlCon(base, { ordenar: c })} style={{ color: c === ordenarPor ? "var(--guinda)" : "inherit" }}>
                  {meta(c).t}{c === ordenarPor ? " ↓" : ""}
                </Link>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {filas.map((f) => (
            <tr key={`${f.estado}-${f.municipio ?? ""}`}>
              <td>
                {municipal
                  ? <Link href={urlCon("/tramites", { estado: f.estado, municipio: f.municipio ?? "", orden: "municipal" })}>{f.municipio}, {f.estado}</Link>
                  : <Link href={`/entidades/${slug(f.estado)}`}>{f.estado}</Link>}
              </td>
              {cols.map((c) => <td key={c} className="n">{formato(valor(f, c), meta(c).f)}</td>)}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export function Dispersion({ filas, x, y, municipal }: { filas: Fila[]; x: string; y: string; municipal?: boolean }) {
  const pts = filas.map((f) => ({ f, x: valor(f, x), y: valor(f, y) })).filter((p) => p.x !== null && p.y !== null) as { f: Fila; x: number; y: number }[];
  const mx = METRICAS.find((m) => m.k === x)!, my = METRICAS.find((m) => m.k === y)!;
  if (pts.length < 2) return <p className="dim">Sin datos suficientes para graficar {mx.t} contra {my.t}.</p>;
  const W = 640, H = 380, P = 44;
  const [x0, x1] = [Math.min(...pts.map((p) => p.x)), Math.max(...pts.map((p) => p.x))];
  const [y0, y1] = [Math.min(...pts.map((p) => p.y)), Math.max(...pts.map((p) => p.y))];
  const sx = (v: number) => P + ((v - x0) / (x1 - x0 || 1)) * (W - 2 * P);
  const sy = (v: number) => H - P - ((v - y0) / (y1 - y0 || 1)) * (H - 2 * P);
  return (
    <figure style={{ margin: 0 }}>
      <svg viewBox={`0 0 ${W} ${H}`} role="img" aria-label={`${my.t} contra ${mx.t}`} style={{ width: "100%", height: "auto", background: "var(--panel)", border: "1px solid var(--line)", borderRadius: 10 }}>
        <line x1={P} y1={H - P} x2={W - P} y2={H - P} stroke="var(--line)" />
        <line x1={P} y1={P} x2={P} y2={H - P} stroke="var(--line)" />
        <text x={W / 2} y={H - 10} textAnchor="middle" fontSize="12" fill="var(--ink-dim)">{mx.t}</text>
        <text x={14} y={H / 2} textAnchor="middle" fontSize="12" fill="var(--ink-dim)" transform={`rotate(-90 14 ${H / 2})`}>{my.t}</text>
        {pts.map((p) => {
          const nombre = municipal ? `${p.f.municipio}, ${p.f.estado}` : p.f.estado;
          return (
            <g key={nombre}>
              <circle cx={sx(p.x)} cy={sy(p.y)} r={5} fill="var(--guinda)" fillOpacity={0.75}>
                <title>{`${nombre}: ${formato(p.x, mx.f)} · ${formato(p.y, my.f)}`}</title>
              </circle>
              {!municipal && <text x={sx(p.x) + 7} y={sy(p.y) + 4} fontSize="10" fill="var(--ink-dim)">{p.f.estado.slice(0, 12)}</text>}
            </g>
          );
        })}
      </svg>
      <figcaption className="faint">{pts.length} puntos con dato en ambos ejes. Pasa el cursor (o toca) un punto para ver su valor.</figcaption>
    </figure>
  );
}
