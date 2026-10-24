import Link from "next/link";
import { fmt, fmtCompacto, lugar, type Tramite } from "@/lib/datos";

export function Canal({ en_linea }: { en_linea: boolean | null }) {
  if (en_linea === true) return <span className="chip ok">En línea</span>;
  if (en_linea === false) return <span className="chip no">Sin opción en línea</span>;
  return <span className="chip" title="No se reporta cómo se realiza">Canal sin dato</span>;
}

export function TramiteItem({ t }: { t: Tramite }) {
  return (
    <li className="item">
      <h3>
        <Link href={`/tramites/${t.id}`}>{t.nombre_tramite}</Link>
      </h3>
      <div className="meta">
        <span>{t.siglas ?? t.dependencia}</span>
        <span>{lugar(t)}</span>
        <Canal en_linea={t.en_linea} />
        {t.frecuencia ? <span>{fmtCompacto.format(t.frecuencia)} solicitudes</span> : null}
        {t.fuente === "sfp" && t.nivel_digitalizacion_sfp ? <span className="chip oro">{t.nivel_digitalizacion_sfp}</span> : null}
      </div>
    </li>
  );
}

export function Barras({ filas, max, sufijo = "" }: { filas: { etiqueta: string; valor: number; href?: string }[]; max?: number; sufijo?: string }) {
  const tope = max ?? Math.max(1, ...filas.map((f) => f.valor));
  return (
    <div className="barras" role="list">
      {filas.map((f) => (
        <div className="barra" role="listitem" key={f.etiqueta}>
          <span>{f.href ? <Link href={f.href}>{f.etiqueta}</Link> : f.etiqueta}</span>
          <span className="pista" aria-hidden>
            <span className="relleno" style={{ width: `${(100 * f.valor) / tope}%` }} />
          </span>
          <span className="v">{fmt.format(f.valor)}{sufijo}</span>
        </div>
      ))}
    </div>
  );
}

export function Cifra({ n, l }: { n: string | number; l: string }) {
  return (
    <div className="cifra">
      <div className="n">{typeof n === "number" ? fmt.format(n) : n}</div>
      <div className="l">{l}</div>
    </div>
  );
}

export function Paginacion({ pagina, paginas, url }: { pagina: number; paginas: number; url: (p: number) => string }) {
  if (paginas <= 1) return null;
  return (
    <nav className="paginacion" aria-label="Paginación">
      {pagina > 1 && <Link className="boton sec" href={url(pagina - 1)}>← Anterior</Link>}
      <span className="dim">{pagina} de {fmt.format(paginas)}</span>
      {pagina < paginas && <Link className="boton sec" href={url(pagina + 1)}>Siguiente →</Link>}
    </nav>
  );
}

export function Select({ name, label, valor, opciones, todos = "Todos" }: { name: string; label: string; valor?: string; opciones: (string | [string, string])[]; todos?: string }) {
  return (
    <label>{label}
      <select name={name} defaultValue={valor ?? ""}>
        <option value="">{todos}</option>
        {opciones.map((o) => {
          const [v, t] = Array.isArray(o) ? o : [o, o];
          return <option key={v} value={v}>{t}</option>;
        })}
      </select>
    </label>
  );
}

export const ORDENES: [string, string][] = [["federal", "Federal"], ["estatal", "Estatal"], ["municipal", "Municipal"]];
export const PODERES: [string, string][] = [["ejecutivo", "Ejecutivo"], ["legislativo", "Legislativo"], ["judicial", "Judicial"], ["autonomo", "Autónomo"]];

export function MatrizOrdenPoder({ titulo, m }: { titulo: string; m: Record<string, Record<string, number>> }) {
  return (
    <div>
      <h3>{titulo}</h3>
      <div className="tabla-scroll">
        <table>
          <thead><tr><th>Orden</th>{PODERES.map(([, t]) => <th key={t} className="n">{t}</th>)}</tr></thead>
          <tbody>
            {ORDENES.map(([o, t]) => (
              <tr key={o}><td>{t}</td>{PODERES.map(([p]) => <td key={p} className="n">{fmt.format(m[o]?.[p] ?? 0)}</td>)}</tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
