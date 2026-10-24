import Link from "next/link";
import { MatrizAmbito } from "@/components/ambito";
import { Barras, Cifra } from "@/components/ui";
import { filtrar, fmt, fmtCompacto, inv, slug } from "@/lib/datos";

export default function Inicio() {
  const r = inv.resumen;
  const po = r.tramites_por_orden as Record<string, number>;
  const ranking = inv.entidades
    .filter((e) => e.pct_tramites_en_linea !== null && e.tramites - e.tramites_sin_dato_canal >= 30)
    .sort((a, b) => (b.pct_tramites_en_linea ?? 0) - (a.pct_tramites_en_linea ?? 0));

  return (
    <>
      <section className="heroe">
        <h1>¿Qué trámite necesitas hacer?</h1>
        <p className="lead">
          {fmt.format(r.tramites)} trámites y servicios de gobierno —federales, estatales y municipales— en un solo buscador,
          con lo que dice el Catálogo Nacional sobre si se pueden hacer en línea.
        </p>
        <form action="/tramites" className="busca" role="search">
          <input type="search" name="q" placeholder="Ej. licencia de conducir, acta de nacimiento, predial" aria-label="Buscar trámite" />
          <button className="boton" type="submit">Buscar</button>
        </form>
        <p className="faint" style={{ marginTop: 10 }}>
          Corte del catálogo: {inv.catalogo.fecha_corte}. Se actualiza a diario.
        </p>
      </section>

      <section className="seccion rejilla" aria-label="Cifras">
        <Cifra n={po.federal ?? 0} l="trámites federales" />
        <Cifra n={po.estatal ?? 0} l="trámites estatales" />
        <Cifra n={po.municipal ?? 0} l="trámites municipales" />
        <Cifra n={r.apps} l="apps de gobierno" />
        <Cifra n={r.sitios} l="portales web" />
        <Cifra n={r.subdominios.total} l="subdominios .gob.mx" />
      </section>

      <section className="seccion dos">
        <div className="panel">
          <h2>Trámites que se pueden hacer en línea</h2>
          <p className="dim">
            Porcentaje de los trámites estatales que el catálogo marca con opción en línea. Referencia federal:{" "}
            {inv.referencia_federal.pct_tramites_en_linea} %. Solo entidades con 30 o más trámites con dato de canal.
          </p>
          <Barras
            filas={ranking.map((e) => ({ etiqueta: e.estado, valor: e.pct_tramites_en_linea ?? 0, href: `/entidades/${slug(e.estado)}` }))}
            max={100}
            sufijo=" %"
          />
        </div>
        <div className="panel">
          <h2>Mucha demanda, sin opción en línea</h2>
          <p className="dim">Los trámites con más solicitudes reportadas que hoy solo se hacen en persona o por teléfono.</p>
          <ul className="lista">
            {inv.brechas.slice(0, 6).map((b) => (
              <li key={b.homoclave} className="item">
                <h3 style={{ fontSize: "1rem" }}>{b.nombre_tramite}</h3>
                <div className="meta">
                  <span>{b.siglas}</span>
                  <span>{b.municipio ?? (b.orden === "federal" ? "Federal" : b.estado)}</span>
                  <span>{fmtCompacto.format(b.frecuencia ?? 0)} solicitudes</span>
                </div>
              </li>
            ))}
          </ul>
          <p style={{ marginTop: 12 }}>
            <Link href="/brechas">Ver las 200 brechas →</Link>
          </p>
        </div>
      </section>

      <section className="seccion">
        <h2>Presencia por orden de gobierno y poder</h2>
        <p className="dim texto">Toca una celda para ver ese ámbito: por ejemplo, el poder judicial estatal o los autónomos nacionales.</p>
        <div className="dos">
          <MatrizAmbito items={filtrar({}, true)} base="/tramites" params={{}} que="trámites" />
          <MatrizAmbito items={inv.apps} base="/apps" params={{}} que="apps" ceroVerificado />
          <MatrizAmbito items={inv.sitios} base="/sitios" params={{}} que="sitios" ceroVerificado />
        </div>
      </section>

      <section className="seccion aviso">
        Este sitio no es oficial. Junta datos públicos para que encontrar un trámite sea más fácil. Antes de ir a una oficina,
        confirma requisitos y costos con la dependencia: el catálogo no siempre está al día.
      </section>
    </>
  );
}
