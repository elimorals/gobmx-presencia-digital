import type { Metadata } from "next";
import Link from "next/link";
import { Suspense } from "react";
import { TramiteItem } from "@/components/ui";
import { buscar, ESTADOS, fmt, type Filtros } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Buscar trámites",
  description: "Buscador de trámites y servicios federales, estatales y municipales del Catálogo Nacional.",
};

const uno = (v: string | string[] | undefined) => (Array.isArray(v) ? v[0] : v) || undefined;

async function Resultados({ searchParams }: PageProps<"/tramites">) {
  const sp = await searchParams;
  const f: Filtros = {
    q: uno(sp.q), orden: uno(sp.orden), estado: uno(sp.estado), municipio: uno(sp.municipio),
    en_linea: uno(sp.en_linea), orden_por: uno(sp.orden_por),
  };
  const pagina = Math.max(1, Number(uno(sp.pagina)) || 1);
  const r = buscar(f, pagina);
  const url = (p: number) => {
    const q = new URLSearchParams(Object.entries({ ...f, pagina: String(p) }).filter(([, v]) => v) as [string, string][]);
    return `/tramites?${q}`;
  };

  return (
    <>
      <form className="panel" action="/tramites" role="search" style={{ marginBottom: 20 }}>
        <div className="busca">
          <input type="search" name="q" defaultValue={f.q} placeholder="¿Qué trámite buscas?" aria-label="Buscar trámite" />
          <button className="boton" type="submit">Buscar</button>
        </div>
        <div className="filtros">
          <label>Orden de gobierno
            <select name="orden" defaultValue={f.orden ?? ""}>
              <option value="">Todos</option>
              <option value="federal">Federal</option>
              <option value="estatal">Estatal</option>
              <option value="municipal">Municipal</option>
            </select>
          </label>
          <label>Entidad
            <select name="estado" defaultValue={f.estado ?? ""}>
              <option value="">Todas</option>
              {ESTADOS.map((e) => <option key={e}>{e}</option>)}
            </select>
          </label>
          <label>Municipio
            <input type="text" name="municipio" defaultValue={f.municipio} placeholder="Ej. Mérida" />
          </label>
          <label>¿En línea?
            <select name="en_linea" defaultValue={f.en_linea ?? ""}>
              <option value="">Cualquiera</option>
              <option value="si">Sí</option>
              <option value="no">No</option>
              <option value="sin_dato">Sin dato</option>
            </select>
          </label>
          <label>Ordenar
            <select name="orden_por" defaultValue={f.orden_por ?? ""}>
              <option value="">Más solicitados</option>
              <option value="reciente">Actualizados recientemente</option>
            </select>
          </label>
        </div>
      </form>

      <p className="dim" aria-live="polite">
        {fmt.format(r.total)} resultados{r.paginas > 1 ? ` · página ${pagina} de ${fmt.format(r.paginas)}` : ""}
      </p>
      {r.total === 0 ? (
        <p>Sin resultados. Prueba con menos palabras o quita filtros. Algunas entidades casi no publican trámites en el catálogo.</p>
      ) : (
        <ul className="lista">{r.items.map((t) => <TramiteItem key={t.id} t={t} />)}</ul>
      )}
      {r.paginas > 1 && (
        <nav className="paginacion" aria-label="Paginación">
          {pagina > 1 && <Link className="boton sec" href={url(pagina - 1)}>← Anterior</Link>}
          {pagina < r.paginas && <Link className="boton sec" href={url(pagina + 1)}>Siguiente →</Link>}
        </nav>
      )}
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
