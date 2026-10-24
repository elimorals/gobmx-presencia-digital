import type { Metadata } from "next";
import { Suspense } from "react";
import { Comparador } from "@/components/comparador";
import { fmt, inv } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Comparar entidades",
  description: "Compara entidades: trámites, porcentaje en línea, apps, sitios, subdominios, egreso y transferencias del PEF.",
};

export default function Entidades(props: PageProps<"/entidades">) {
  const pef = inv.entidades.find((e) => e.transferencias_pef_anio)?.transferencias_pef_anio;
  const eg = inv.entidades.find((e) => e.egresos_anio)?.egresos_anio;
  return (
    <section className="seccion">
      <h1>Entidades</h1>
      <div className="texto dim">
        <p>
          Elige de 2 a 6 entidades para verlas lado a lado. Población: Censo 2020 (INEGI). Egreso propio: INEGI EFIPEM {eg}.
          Presupuesto de Egresos de la Federación: transferencias Ramo 28 + Ramo 33, PEF {pef} aprobado (SHCP). “s/d” es dato
          no disponible, nunca cero.
        </p>
        <p>
          <b>Trámites</b> cuenta lo que cada gobierno estatal publica en el registro nacional: mide cuánto captura cada entidad,
          no cuántos trámites tiene (Jalisco publica 1). Referencia federal, sin comparar: {fmt.format(inv.referencia_federal.tramites)} trámites,{" "}
          {inv.referencia_federal.pct_tramites_en_linea} % en línea.
        </p>
      </div>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Comparador filas={inv.entidades} base="/entidades" searchParams={props.searchParams} />
      </Suspense>
    </section>
  );
}
