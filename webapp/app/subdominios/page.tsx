import type { Metadata } from "next";
import { Suspense } from "react";
import { Barras, Paginacion, Select } from "@/components/ui";
import { conteo, ESTADOS, fmt, inv, paginar, subdominios, uno, urlCon } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Subdominios .gob.mx",
  description: "Más de 12 mil subdominios .gob.mx encontrados en Certificate Transparency, dotgob-mx y textos de trámites.",
};

const FUENTES = [...new Set(subdominios.map((s) => s.fuente))].sort();

async function Lista({ searchParams }: PageProps<"/subdominios">) {
  const sp = await searchParams;
  const f = { q: uno(sp.q), fuente: uno(sp.fuente), activo: uno(sp.activo), estado: uno(sp.estado) };
  const q = (f.q ?? "").toLowerCase();
  const filas = subdominios
    .filter((s) => (!q || s.host.includes(q)) && (!f.fuente || s.fuente === f.fuente) && (!f.estado || s.estado === f.estado))
    .filter((s) => !f.activo || (f.activo === "activo" ? s.activo === "activo" : s.activo !== "activo"))
    .sort((a, b) => Number(b.activo === "activo") - Number(a.activo === "activo") || a.host.localeCompare(b.host));
  const pag = paginar(filas, Number(uno(sp.pagina)) || 1, 50);
  const hayFiltros = Boolean(f.fuente || f.activo || f.estado);

  return (
    <>
      <form className="panel" action="/subdominios" style={{ marginBottom: 16 }}>
        <div className="busca">
          <input type="search" name="q" defaultValue={f.q} placeholder="Parte del host, ej. imss o predial" aria-label="Buscar subdominio" />
          <button className="boton" type="submit">Buscar</button>
        </div>
        <details className="mas" open={hayFiltros}>
          <summary>Filtrar</summary>
          <div className="filtros">
            <Select name="fuente" label="Fuente" valor={f.fuente} opciones={FUENTES} todos="Todas" />
            <Select name="activo" label="Actividad" valor={f.activo} todos="Cualquiera" opciones={[["activo", "Activos (responden)"], ["no", "Inactivos o sin dato"]]} />
            <Select name="estado" label="Entidad (si el dominio está mapeado)" valor={f.estado} opciones={ESTADOS} todos="Todas" />
          </div>
          <div className="acciones">
            <button className="boton" type="submit">Aplicar</button>
            {hayFiltros && <a className="boton sec" href={urlCon("/subdominios", { q: f.q })}>Quitar filtros</a>}
          </div>
        </details>
      </form>

      <div className="dos" style={{ marginBottom: 20 }}>
        <div className="panel"><h3>Por fuente</h3><Barras filas={conteo(filas, (s) => s.fuente).map(([e, v]) => ({ etiqueta: e, valor: v, href: urlCon("/subdominios", { ...f, fuente: e }) }))} /></div>
        <div className="panel"><h3>Dominios con más hosts</h3><Barras filas={conteo(filas, (s) => s.dep).slice(0, 10).map(([e, v]) => ({ etiqueta: e, valor: v, href: urlCon("/subdominios", { ...f, q: e }) }))} /></div>
      </div>

      <p className="dim">{fmt.format(filas.length)} hosts · activos primero</p>
      <div className="tabla-scroll">
        <table>
          <thead><tr><th>Host</th><th>Actividad</th><th>Fuente</th><th>Lugar</th></tr></thead>
          <tbody>
            {pag.items.map((s) => (
              <tr key={s.host}>
                <td className="mono">{s.activo === "activo" ? <a href={`https://${s.host}`} rel="nofollow noopener">{s.host}</a> : s.host}</td>
                <td>{s.activo === "activo" ? <span className="chip ok">activo</span> : <span className="chip">{s.activo ?? "sin dato"}</span>}</td>
                <td>{s.fuente}</td>
                <td>{s.municipio ? `${s.municipio}, ${s.estado}` : s.estado ?? "—"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Paginacion pagina={pag.pagina} paginas={pag.paginas} url={(p) => urlCon("/subdominios", { ...f, pagina: p })} />
    </>
  );
}

export default function Subdominios(props: PageProps<"/subdominios">) {
  const m = inv.subdominios_mapeo;
  return (
    <section className="seccion">
      <h1>Subdominios .gob.mx</h1>
      <p className="texto dim">
        {fmt.format(inv.resumen.subdominios.total)} hosts de Certificate Transparency (crt.sh), del proyecto dotgob-mx y de
        los textos de trámites. “Activo” significa que respondió a HTTP/S en el último barrido de dotgob-mx. {fmt.format(m.asignados)}{" "}
        ({m.pct_asignado} %) están asignados a una entidad o municipio.
      </p>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Lista {...props} />
      </Suspense>
    </section>
  );
}
