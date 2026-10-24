import type { Metadata } from "next";
import Link from "next/link";
import { Suspense } from "react";
import { MatrizAmbito } from "@/components/ambito";
import { Barras, Paginacion, Select } from "@/components/ui";
import { conteo, ESTADOS, fmtCompacto, inv, lugar, paginar, sinAcentos, uno, urlCon } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Apps de gobierno",
  description: "Las apps móviles de los gobiernos federal, estatales y municipales de México: estatus, instalaciones y permisos.",
};

const SECTORES = [...new Set(inv.apps.map((a) => a.sector).filter(Boolean) as string[])].sort();
const ESTATUS = [...new Set(inv.apps.map((a) => a.estatus).filter(Boolean) as string[])].sort();

async function Lista({ searchParams }: PageProps<"/apps">) {
  const sp = await searchParams;
  const f = { q: uno(sp.q), orden: uno(sp.orden), poder: uno(sp.poder), estado: uno(sp.estado), sector: uno(sp.sector), estatus: uno(sp.estatus), peligrosos: uno(sp.peligrosos) };
  const q = sinAcentos(f.q);
  const sinAmbito = inv.apps
    .filter((a) => !f.estado || a.estado === f.estado)
    .filter((a) => (!f.sector || a.sector === f.sector) && (!f.estatus || a.estatus === f.estatus))
    .filter((a) => !f.peligrosos || (a.permisos?.peligrosos.length ?? 0) > 0)
    .filter((a) => !q || sinAcentos(`${a.nombre} ${a.dependencia} ${a.siglas} ${a.proposito}`).includes(q));
  const apps = sinAmbito
    .filter((a) => (!f.orden || a.orden === f.orden) && (!f.poder || a.poder === f.poder))
    .sort((a, b) => (b.instalaciones ?? 0) - (a.instalaciones ?? 0));
  const pag = paginar(apps, Number(uno(sp.pagina)) || 1, 30);
  const hayFiltros = Boolean(f.estado || f.sector || f.estatus || f.peligrosos);
  const barras = (pares: [string, number][], k: string) =>
    pares.slice(0, 8).map(([e, v]) => ({ etiqueta: e, valor: v, href: urlCon("/apps", { ...f, [k]: e === "sin dato" ? undefined : e }) }));

  return (
    <>
      <form className="panel" action="/apps" style={{ marginBottom: 16 }}>
        <div className="busca">
          <input type="search" name="q" defaultValue={f.q} placeholder="Nombre, dependencia o para qué sirve" aria-label="Buscar app" />
          <button className="boton" type="submit">Buscar</button>
        </div>
        <details className="mas" open={hayFiltros}>
          <summary>Filtrar</summary>
          <div className="filtros">
            <input type="hidden" name="orden" value={f.orden ?? ""} />
            <input type="hidden" name="poder" value={f.poder ?? ""} />
            <Select name="estado" label="Entidad" valor={f.estado} opciones={ESTADOS} todos="Todas" />
            <Select name="sector" label="Sector" valor={f.sector} opciones={SECTORES} />
            <Select name="estatus" label="Estatus" valor={f.estatus} opciones={ESTATUS} />
            <Select name="peligrosos" label="Permisos" valor={f.peligrosos} todos="Cualquiera" opciones={[["1", "Con permisos peligrosos"]]} />
          </div>
          <div className="acciones">
            <button className="boton" type="submit">Aplicar</button>
            {hayFiltros && <a className="boton sec" href={urlCon("/apps", { q: f.q, orden: f.orden, poder: f.poder })}>Quitar filtros</a>}
          </div>
        </details>
      </form>

      <MatrizAmbito items={sinAmbito} base="/apps" params={f} orden={f.orden} poder={f.poder} que="apps" ceroVerificado />

      <div className="dos" style={{ marginBottom: 20 }}>
        <div className="panel"><h3>Por estatus</h3><Barras filas={barras(conteo(apps, (a) => a.estatus), "estatus")} /></div>
        <div className="panel"><h3>Por sector</h3><Barras filas={barras(conteo(apps, (a) => a.sector), "sector")} /></div>
      </div>

      <p className="dim">{apps.length} apps, de más a menos instalaciones en Google Play</p>
      <ul className="lista">
        {pag.items.map((a) => (
          <li className="item" key={a.clave}>
            <h3><Link href={`/apps/${encodeURIComponent(a.clave)}`}>{a.nombre}</Link></h3>
            <div className="meta">
              <span>{a.siglas ?? a.dependencia}</span>
              <span>{lugar(a)}</span>
              <span className={`chip ${a.estatus === "activa" ? "ok" : ""}`}>{a.estatus ?? "sin estatus"}</span>
              {a.instalaciones ? <span>{fmtCompacto.format(a.instalaciones)} instalaciones</span> : null}
              {a.permisos?.peligrosos.length ? <span className="chip no">{a.permisos.peligrosos.length} permisos peligrosos</span> : null}
            </div>
            {a.proposito && <p>{a.proposito}</p>}
          </li>
        ))}
      </ul>
      <Paginacion pagina={pag.pagina} paginas={pag.paginas} url={(p) => urlCon("/apps", { ...f, pagina: p })} />
    </>
  );
}

export default function Apps(props: PageProps<"/apps">) {
  return (
    <section className="seccion">
      <h1>Apps de gobierno</h1>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Lista {...props} />
      </Suspense>
    </section>
  );
}
