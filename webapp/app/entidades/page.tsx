import type { Metadata } from "next";
import { Suspense } from "react";
import { Dispersion, METRICAS, ordenar, SelectorMetrica, Tabla } from "@/components/comparar";
import { fmt, inv, uno } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Comparar entidades",
  description: "Las 32 entidades comparadas: trámites, porcentaje en línea, apps, sitios, subdominios y presupuesto.",
};

const valida = (k: string | undefined, d: string) => (k && METRICAS.some((m) => m.k === k) ? k : d);

async function Contenido({ searchParams }: PageProps<"/entidades">) {
  const sp = await searchParams;
  const ord = valida(uno(sp.ordenar), "tramites");
  const x = valida(uno(sp.x), "egreso_por_hab");
  const y = valida(uno(sp.y), "por_100k_hab.apps");
  return (
    <>
      <SelectorMetrica base="/entidades" ordenar={ord} x={x} y={y} />
      <Tabla filas={ordenar(inv.entidades, ord)} ordenarPor={ord} base="/entidades" />
      <h2 style={{ marginTop: 28 }}>Gráfica</h2>
      <Dispersion filas={inv.entidades} x={x} y={y} />
    </>
  );
}

export default function Entidades(props: PageProps<"/entidades">) {
  return (
    <section className="seccion">
      <h1>Entidades</h1>
      <div className="texto dim">
        <p>
          Solo se compara entidad con entidad. Las tasas usan población del Censo 2020 (INEGI) y egresos de INEGI EFIPEM;
          “s/d” es dato no disponible, nunca cero.
        </p>
        <p>
          <b>Trámites</b> cuenta lo que cada gobierno estatal publica en el registro nacional de trámites: mide cuánto captura
          cada entidad, no cuántos trámites tiene (Jalisco publica 1). El % en línea se calcula sobre los trámites que reportan
          canal. Referencia federal: {fmt.format(inv.referencia_federal.tramites)} trámites, {inv.referencia_federal.pct_tramites_en_linea} % en línea.
        </p>
      </div>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Contenido {...props} />
      </Suspense>
    </section>
  );
}
