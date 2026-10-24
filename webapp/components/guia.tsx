import Link from "next/link";
import guia from "@/data/guia.json";

export const G = guia;
export const SITIO = process.env.NEXT_PUBLIC_SITIO ?? "https://gobmx-presencia-digital.vercel.app";
export const RAW = "https://raw.githubusercontent.com/elimorals/gobmx-presencia-digital/main";

const PAGINAS: [string, string][] = [["/guia", "Inicio"], ["/guia/mcp", "MCP"], ["/guia/cli", "CLI"], ["/guia/api", "API y datos"]];

export function MenuGuia({ actual }: { actual: string }) {
  return (
    <nav className="chips" aria-label="Secciones de la guía" style={{ margin: "0 0 20px" }}>
      {PAGINAS.map(([h, t]) => (
        <Link key={h} href={h} className={`chip${h === actual ? " oro" : ""}`} aria-current={h === actual ? "page" : undefined}>{t}</Link>
      ))}
    </nav>
  );
}

export function Indice({ items }: { items: [string, string][] }) {
  return (
    <div className="panel" style={{ margin: "0 0 24px" }}>
      <b>En esta página</b>
      <ol style={{ margin: "8px 0 0", paddingLeft: 20 }}>{items.map(([id, t]) => <li key={id}><a href={`#${id}`}>{t}</a></li>)}</ol>
    </div>
  );
}

export function Codigo({ children, titulo }: { children: string; titulo?: string }) {
  return (
    <figure style={{ margin: "0 0 16px" }}>
      {titulo && <figcaption className="faint" style={{ marginBottom: 4 }}>{titulo}</figcaption>}
      <pre style={{ margin: 0 }}><code>{children}</code></pre>
    </figure>
  );
}

type Prop = { type?: string | string[]; enum?: string[]; default?: unknown; description?: string; minimum?: number; maximum?: number };
type Esquema = { properties?: Record<string, Prop>; required?: string[] };

/** Tabla de parámetros generada del inputSchema que publica el servidor MCP (no se escribe a mano). */
export function Parametros({ esquema }: { esquema: Esquema }) {
  const props = Object.entries(esquema.properties ?? {});
  if (!props.length) return <p className="dim">Sin parámetros.</p>;
  const req = new Set(esquema.required ?? []);
  return (
    <div className="tabla-scroll" style={{ marginBottom: 12 }}>
      <table>
        <thead><tr><th>Parámetro</th><th>Tipo</th><th>Valores</th><th>Por defecto</th><th>Qué es</th></tr></thead>
        <tbody>
          {props.map(([k, p]) => {
            // el validador pone MAX_SAFE_INTEGER como tope técnico de los enteros: no es un límite real
            const max = p.maximum !== undefined && p.maximum < 1e9 ? p.maximum : undefined;
            const rango = [p.minimum !== undefined && `≥ ${p.minimum}`, max !== undefined && `≤ ${max}`].filter(Boolean).join(", ");
            return (
              <tr key={k}>
                <td className="mono">{k}{req.has(k) && p.default === undefined ? " *" : ""}</td>
                <td>{Array.isArray(p.type) ? p.type.join(" | ") : p.type ?? "—"}</td>
                <td className="ancho">{p.enum ? <span className="chips">{p.enum.map((v) => <code key={v} className="chip">{v}</code>)}</span> : rango || "libre"}</td>
                <td className="mono">{p.default === undefined || p.default === "" ? "—" : JSON.stringify(p.default)}</td>
                <td className="ancho">{p.description ?? ""}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export function Notas({ items }: { items: string[] }) {
  if (!items.length) return null;
  return (
    <div className="aviso" style={{ margin: "0 0 12px" }}>
      <b>Ojo:</b>
      <ul style={{ margin: "4px 0 0", paddingLeft: 18 }}>{items.map((n) => <li key={n}>{n}</li>)}</ul>
    </div>
  );
}
