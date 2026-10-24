import Link from "next/link";
import { fmt, fmtCompacto, lugar, type Tramite } from "@/lib/datos";

export function Canal({ en_linea }: { en_linea: boolean | null }) {
  if (en_linea === true) return <span className="chip ok">En línea</span>;
  if (en_linea === false) return <span className="chip no">Sin opción en línea</span>;
  return <span className="chip" title="El catálogo no reporta cómo se realiza">Canal sin dato</span>;
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
        {t.frecuencia ? <span title="Volumen reportado al catálogo">{fmtCompacto.format(t.frecuencia)} solicitudes</span> : null}
      </div>
    </li>
  );
}

export function Barras({ filas, max, sufijo = "" }: { filas: { etiqueta: string; valor: number; href?: string; nota?: string }[]; max?: number; sufijo?: string }) {
  const tope = max ?? Math.max(1, ...filas.map((f) => f.valor));
  return (
    <div className="barras" role="list">
      {filas.map((f) => (
        <div className="barra" role="listitem" key={f.etiqueta}>
          <span title={f.nota}>{f.href ? <Link href={f.href}>{f.etiqueta}</Link> : f.etiqueta}</span>
          <span className="pista" aria-hidden>
            <span className="relleno" style={{ width: `${(100 * f.valor) / tope}%`, display: "block" }} />
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
