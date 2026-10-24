import type { Metadata } from "next";
import { Suspense } from "react";
import { Barras, ORDENES, Paginacion, PODERES, Select } from "@/components/ui";
import { conteo, ESTADOS, inv, lugar, paginar, sinAcentos, uno, urlCon } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Sitios web de gobierno",
  description: "Portales web de gobierno: plataforma, protección anti-bot, autenticación y evidencia fechada de si responden.",
};

const PLATAFORMAS = [...new Set(inv.sitios.map((s) => s.plataforma).filter(Boolean) as string[])].sort();
const ANTIBOT = [...new Set(inv.sitios.map((s) => s.anti_bot).filter(Boolean) as string[])].sort();

async function Lista({ searchParams }: PageProps<"/sitios">) {
  const sp = await searchParams;
  const f = { q: uno(sp.q), orden: uno(sp.orden), poder: uno(sp.poder), estado: uno(sp.estado), plataforma: uno(sp.plataforma), anti_bot: uno(sp.anti_bot) };
  const q = sinAcentos(f.q);
  const sitios = inv.sitios
    .filter((s) => (!f.orden || s.orden === f.orden) && (!f.poder || s.poder === f.poder) && (!f.estado || s.estado === f.estado))
    .filter((s) => (!f.plataforma || s.plataforma === f.plataforma) && (!f.anti_bot || s.anti_bot === f.anti_bot))
    .filter((s) => !q || sinAcentos(`${s.dependencia} ${s.siglas} ${s.url} ${s.proposito}`).includes(q));
  const pag = paginar(sitios, Number(uno(sp.pagina)) || 1, 30);
  const hayFiltros = Object.entries(f).some(([k, v]) => k !== "q" && v);
  const barras = (pares: [string, number][], k: string) =>
    pares.slice(0, 8).map(([e, v]) => ({ etiqueta: e, valor: v, href: urlCon("/sitios", { ...f, [k]: e === "sin dato" ? undefined : e }) }));

  return (
    <>
      <form className="panel" action="/sitios" style={{ marginBottom: 16 }}>
        <div className="busca">
          <input type="search" name="q" defaultValue={f.q} placeholder="Dependencia o dominio" aria-label="Buscar sitio" />
          <button className="boton" type="submit">Buscar</button>
        </div>
        <details className="mas" open={hayFiltros}>
          <summary>Filtrar</summary>
          <div className="filtros">
            <Select name="orden" label="Orden" valor={f.orden} opciones={ORDENES} />
            <Select name="poder" label="Poder" valor={f.poder} opciones={PODERES} />
            <Select name="estado" label="Entidad" valor={f.estado} opciones={ESTADOS} todos="Todas" />
            <Select name="plataforma" label="Plataforma" valor={f.plataforma} opciones={PLATAFORMAS} todos="Todas" />
            <Select name="anti_bot" label="Anti-bot" valor={f.anti_bot} opciones={ANTIBOT} todos="Cualquiera" />
          </div>
          <div className="acciones">
            <button className="boton" type="submit">Aplicar</button>
            {hayFiltros && <a className="boton sec" href="/sitios">Quitar filtros</a>}
          </div>
        </details>
      </form>

      <div className="dos" style={{ marginBottom: 20 }}>
        <div className="panel"><h3>Por plataforma</h3><Barras filas={barras(conteo(sitios, (s) => s.plataforma), "plataforma")} /></div>
        <div className="panel"><h3>Por anti-bot</h3><Barras filas={barras(conteo(sitios, (s) => s.anti_bot), "anti_bot")} /></div>
      </div>

      <p className="dim">{sitios.length} sitios</p>
      <ul className="lista">
        {pag.items.map((s, i) => (
          <li className="item" key={`${s.url}-${i}`}>
            <h3>{s.url ? <a href={s.url} rel="noopener">{s.dependencia}</a> : s.dependencia}</h3>
            <div className="meta">
              <span>{lugar(s)}</span>
              {s.tipo && <span className="chip">{s.tipo}</span>}
              {s.plataforma && <span>{s.plataforma}</span>}
              {s.anti_bot && s.anti_bot !== "ninguno" && <span className="chip oro">anti-bot: {s.anti_bot}</span>}
              {s.autenticacion && <span>acceso: {String(s.autenticacion)}</span>}
            </div>
            {s.url && <p className="mono">{s.url}</p>}
            {s.evidencia.length > 0 && (
              <p className="faint">Comprobado {s.evidencia[s.evidencia.length - 1].fecha}: {s.resultado}</p>
            )}
            {s.proposito && <p>{s.proposito}</p>}
          </li>
        ))}
      </ul>
      <Paginacion pagina={pag.pagina} paginas={pag.paginas} url={(p) => urlCon("/sitios", { ...f, pagina: p })} />
    </>
  );
}

export default function Sitios(props: PageProps<"/sitios">) {
  return (
    <section className="seccion">
      <h1>Sitios web de gobierno</h1>
      <p className="texto dim">Cada afirmación sobre un portal lleva evidencia fechada: cuándo se comprobó y qué respondió.</p>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Lista {...props} />
      </Suspense>
    </section>
  );
}
