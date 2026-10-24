import type { Metadata } from "next";
import { Suspense } from "react";
import { Dispersion, METRICAS, ordenar, SelectorMetrica, Tabla } from "@/components/comparar";
import { inv, uno } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Comparar municipios",
  description: "Municipios comparados: trámites, porcentaje en línea, apps, sitios, subdominios y presupuesto.",
};

const valida = (k: string | undefined, d: string) => (k && METRICAS.some((m) => m.k === k) ? k : d);

async function Contenido({ searchParams }: PageProps<"/municipios">) {
  const sp = await searchParams;
  const ord = valida(uno(sp.ordenar), "tramites");
  const x = valida(uno(sp.x), "poblacion_2020");
  const y = valida(uno(sp.y), "apps");
  return (
    <>
      <SelectorMetrica base="/municipios" ordenar={ord} x={x} y={y} />
      <Tabla filas={ordenar(inv.municipios, ord)} municipal ordenarPor={ord} base="/municipios" />
      <h2 style={{ marginTop: 28 }}>Gráfica</h2>
      <Dispersion filas={inv.municipios} x={x} y={y} municipal />
    </>
  );
}

export default function Municipios(props: PageProps<"/municipios">) {
  return (
    <section className="seccion">
      <h1>Municipios</h1>
      <p className="texto dim">
        Solo municipio con municipio. Aparecen los {inv.municipios.length} municipios con algún registro: app, sitio,
        subdominio mapeado, trámites publicados o datos de presupuesto. El presupuesto solo existe para{" "}
        {inv.municipios.filter((m) => m.egresos_mxn).length} de ellos; en el resto las tasas salen “s/d”.
      </p>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Contenido {...props} />
      </Suspense>
    </section>
  );
}
