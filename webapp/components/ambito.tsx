import Link from "next/link";
import { fmt, urlCon } from "@/lib/datos";
import { ORDENES, PODERES } from "@/components/ui";

const BUSQUEDA = "https://github.com/elimorals/gobmx-presencia-digital/blob/main/apps/inventario/BUSQUEDA-PODERES.md";
const SECCIONES: [string, string][] = [["/tramites", "Trámites"], ["/apps", "Apps"], ["/sitios", "Sitios"]];
const ETQ_PODER: Record<string, string> = { ejecutivo: "Ejecutivo", legislativo: "Legislativo", judicial: "Judicial", autonomo: "Autónomo" };
const ETQ_ORDEN: Record<string, string> = { federal: "Federal", estatal: "Estatal", municipal: "Municipal" };

export function textoAmbito(orden?: string, poder?: string) {
  if (!orden && !poder) return "Nacional (todo)";
  const p = poder ? (poder === "autonomo" && orden === "federal" ? "Autónomos nacionales" : `Poder ${ETQ_PODER[poder]}`) : "";
  return [orden ? ETQ_ORDEN[orden] : "Todos los órdenes", p].filter(Boolean).join(" · ");
}

/**
 * Filtro por ámbito: orden (federal/estatal/municipal) × poder. Cuenta sobre `items` ya filtrados por todo lo
 * demás (búsqueda, entidad…), así que un 0 significa "no hay" en ese cruce y se muestra, no se esconde.
 */
export function MatrizAmbito({ items, base, params, orden, poder, que, ceroVerificado }: {
  items: { orden?: string | null; poder?: string | null }[];
  base: string;
  params: Record<string, string | undefined>;
  orden?: string;
  poder?: string;
  que: string;
  ceroVerificado?: boolean; // apps y sitios: los ceros están respaldados por búsqueda dirigida
}) {
  const n = new Map<string, number>(), tot = new Map<string, number>();
  let sinOrden = 0;
  for (const x of items) {
    if (!x.orden) { sinOrden++; continue; }
    n.set(`${x.orden}|${x.poder}`, (n.get(`${x.orden}|${x.poder}`) ?? 0) + 1);
    tot.set(x.orden, (tot.get(x.orden) ?? 0) + 1);
    tot.set(String(x.poder), (tot.get(String(x.poder)) ?? 0) + 1);
  }
  const todos = items.length - sinOrden;
  const resto = { ...params, orden: undefined, poder: undefined, pagina: undefined };
  const href = (o?: string, p?: string) => urlCon(base, { ...resto, orden: o, poder: p });
  const on = (o?: string, p?: string) => ((orden ?? "") === (o ?? "") && (poder ?? "") === (p ?? "") ? " on" : "");

  return (
    <div className="panel" style={{ marginBottom: 16 }}>
      <h3 style={{ display: "flex", justifyContent: "space-between", gap: 8, flexWrap: "wrap" }}>
        <span>Orden × poder · {que}</span><span className="faint" style={{ fontFamily: "var(--f-sans)" }}>toca para filtrar</span>
      </h3>
      <div className="tabla-scroll" style={{ border: 0, background: "transparent" }}>
        <div className="mx" role="table" aria-label={`Orden por poder, ${que}`}>
          <span />
          {PODERES.map(([p, t]) => (
            <Link key={p} className={`h${on(undefined, p)}`} href={href(undefined, p)} aria-label={t}>
              <span className="largo">{t}</span><span className="corto" aria-hidden>{t.slice(0, 5)}.</span>
            </Link>
          ))}
          <span className="h">Total</span>
          {ORDENES.map(([o, t]) => (
            <Fila key={o}>
              <Link className={`r${on(o)}`} href={href(o)}>{t}</Link>
              {PODERES.map(([p, tp]) => {
                const v = n.get(`${o}|${p}`) ?? 0;
                return (
                  <Link key={p} className={`c${v ? "" : " z"}${on(o, p)}`} href={href(o, p)} title={`${t} · ${tp}: ${fmt.format(v)}`}>
                    {fmt.format(v)}{o === "federal" && p === "autonomo" && <small>nacionales</small>}
                  </Link>
                );
              })}
              <span className="t">{fmt.format(tot.get(o) ?? 0)}</span>
            </Fila>
          ))}
          <Link className={`r nac${on()}`} href={href()}>Nacional</Link>
          {PODERES.map(([p]) => <span key={p} className="t">{fmt.format(tot.get(p) ?? 0)}</span>)}
          <span className="t">{fmt.format(todos)}</span>
        </div>
      </div>
      <p className="mx-leyenda">
        Las celdas en <b>0</b> (borde punteado) indican que no hay {que} en ese cruce con los filtros actuales.
        {ceroVerificado && <> En apps y sitios son ausencias reales, respaldadas por búsqueda dirigida en tiendas y portales (<a href={BUSQUEDA} rel="noopener">BUSQUEDA-PODERES.md</a>).</>}
        {" "}«Autónomo» en el orden federal son los autónomos nacionales (INE, Banxico, INEGI, CNDH…).
        {sinOrden > 0 && ` ${sinOrden} registro(s) no oficiales quedan fuera de la matriz.`}
      </p>
      <p className="chips" style={{ marginTop: 10 }}>
        <span className="chip oro">{textoAmbito(orden, poder)}</span>
        {(orden || poder) && <>
          <span className="faint">Ver este ámbito en:</span>
          {SECCIONES.filter(([b]) => b !== base).map(([b, t]) => <Link key={b} className="chip" href={urlCon(b, { orden, poder })}>{t}</Link>)}
          <Link className="chip" href={href()}>Quitar ×</Link>
        </>}
      </p>
    </div>
  );
}

function Fila({ children }: { children: React.ReactNode }) {
  return <>{children}</>;
}
