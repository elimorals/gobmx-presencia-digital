import type { Metadata } from "next";
import { Suspense } from "react";
import { ORDENES, Paginacion, Select, TramiteItem } from "@/components/ui";
import { buscar, ESTADOS, fmt, uno, urlCon, type Filtros } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Buscar trámites",
  description: "Buscador de trámites y servicios federales, estatales y municipales de México.",
};

async function Resultados({ searchParams }: PageProps<"/tramites">) {
  const sp = await searchParams;
  const f: Filtros = {
    q: uno(sp.q), orden: uno(sp.orden), estado: uno(sp.estado), municipio: uno(sp.municipio),
    en_linea: uno(sp.en_linea), orden_por: uno(sp.orden_por),
  };
  const r = buscar(f, Number(uno(sp.pagina)) || 1);
  const hayFiltros = Boolean(f.orden || f.estado || f.municipio || f.en_linea || f.orden_por);

  return (
    <>
      <form className="panel" action="/tramites" role="search" style={{ marginBottom: 16 }}>
        <div className="busca">
          <input type="search" name="q" defaultValue={f.q} placeholder="¿Qué trámite buscas?" aria-label="Buscar trámite" />
          <button className="boton" type="submit">Buscar</button>
        </div>
        <details className="mas" open={hayFiltros}>
          <summary>Filtrar por lugar y canal</summary>
          <div className="filtros">
            <Select name="orden" label="Orden de gobierno" valor={f.orden} opciones={ORDENES} />
            <Select name="estado" label="Entidad" valor={f.estado} opciones={ESTADOS} todos="Todas" />
            <label>Municipio
              <input type="text" name="municipio" defaultValue={f.municipio} placeholder="Ej. Mérida" />
            </label>
            <Select name="en_linea" label="¿Se puede en línea?" valor={f.en_linea} todos="Cualquiera"
              opciones={[["si", "Sí"], ["no", "No"], ["sin_dato", "Sin dato"]]} />
            <Select name="orden_por" label="Ordenar" valor={f.orden_por} todos="Más solicitados"
              opciones={[["reciente", "Actualizados recientemente"]]} />
          </div>
          <div className="acciones">
            <button className="boton" type="submit">Aplicar</button>
            {hayFiltros && <a className="boton sec" href={urlCon("/tramites", { q: f.q })}>Quitar filtros</a>}
          </div>
        </details>
      </form>

      <p className="dim" aria-live="polite">{fmt.format(r.total)} resultados</p>
      {r.total === 0 ? (
        <p>Sin resultados. Prueba con menos palabras o quita filtros. Algunas entidades publican muy pocos trámites.</p>
      ) : (
        <ul className="lista">{r.items.map((t) => <TramiteItem key={t.id} t={t} />)}</ul>
      )}
      <Paginacion pagina={r.pagina} paginas={r.paginas} url={(p) => urlCon("/tramites", { ...f, pagina: p })} />
    </>
  );
}

export default function Tramites(props: PageProps<"/tramites">) {
  return (
    <section className="seccion">
      <h1>Trámites</h1>
      <Suspense fallback={<p className="cargando">Buscando…</p>}>
        <Resultados {...props} />
      </Suspense>
    </section>
  );
}
