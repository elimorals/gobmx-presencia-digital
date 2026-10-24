import type { Metadata } from "next";
import Link from "next/link";
import { fmt, inv, slug } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Comparar entidades",
  description: "Las 32 entidades comparadas: trámites publicados, porcentaje en línea, apps, sitios y subdominios.",
};

const v = (x: number | null | undefined, suf = "") => (x === null || x === undefined ? "s/d" : `${fmt.format(x)}${suf}`);

export default function Entidades() {
  const filas = [...inv.entidades].sort((a, b) => b.tramites - a.tramites);
  return (
    <section className="seccion">
      <h1>Entidades</h1>
      <p className="dim" style={{ maxWidth: "75ch" }}>
        Solo se compara entidad con entidad. <b>Trámites</b> cuenta lo que cada gobierno estatal publica en el Catálogo
        Nacional: mide cuánto captura cada entidad, no cuántos trámites tiene (Jalisco publica 1). El % en línea se
        calcula solo sobre los trámites que reportan canal. Referencia federal: {fmt.format(inv.referencia_federal.tramites)}{" "}
        trámites, {inv.referencia_federal.pct_tramites_en_linea} % en línea.
      </p>
      <div className="tabla-scroll">
        <table>
          <thead>
            <tr>
              <th>Entidad</th>
              <th className="n">Trámites</th>
              <th className="n">% en línea</th>
              <th className="n">Sin dato de canal</th>
              <th className="n">Apps</th>
              <th className="n">Apps activas</th>
              <th className="n">Sitios</th>
              <th className="n">Subdominios</th>
              <th className="n">Apps / 100 mil hab.</th>
            </tr>
          </thead>
          <tbody>
            {filas.map((e) => (
              <tr key={e.estado}>
                <td><Link href={`/entidades/${slug(e.estado)}`}>{e.estado}</Link></td>
                <td className="n">{fmt.format(e.tramites)}</td>
                <td className="n">{v(e.pct_tramites_en_linea, " %")}</td>
                <td className="n">{fmt.format(e.tramites_sin_dato_canal)}</td>
                <td className="n">{e.apps}</td>
                <td className="n">{e.apps_activas}</td>
                <td className="n">{e.sitios}</td>
                <td className="n">{fmt.format(e.subdominios)}</td>
                <td className="n">{v(e.por_100k_hab?.apps)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
