import type { Metadata } from "next";
import { Suspense } from "react";
import { Comparador } from "@/components/comparador";
import { inv } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Comparar municipios",
  description: "Compara municipios: trámites, porcentaje en línea, apps, sitios, subdominios y egreso.",
};

export default function Municipios(props: PageProps<"/municipios">) {
  return (
    <section className="seccion">
      <h1>Municipios</h1>
      <p className="texto dim">
        Elige de 2 a 6 municipios. Aparecen los {inv.municipios.length} con algún registro: app, sitio, subdominio
        mapeado, trámites publicados o presupuesto. El egreso (INEGI EFIPEM) solo existe para{" "}
        {inv.municipios.filter((m) => m.egresos_mxn).length} de ellos; el PEF no se desglosa por municipio.
      </p>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Comparador filas={inv.municipios} municipal base="/municipios" searchParams={props.searchParams} />
      </Suspense>
    </section>
  );
}
