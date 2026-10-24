import type { Metadata } from "next";
import { fmt, inv } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Brechas: mucha demanda, sin opción en línea",
  description: "Los trámites con más solicitudes reportadas que el Catálogo Nacional marca sin opción en línea.",
};

export default function Brechas() {
  return (
    <section className="seccion">
      <h1>Mucha demanda, sin opción en línea</h1>
      <p className="dim" style={{ maxWidth: "75ch" }}>
        Los {inv.brechas.length} trámites con más solicitudes reportadas que el Catálogo Nacional marca sin opción en línea
        (los que no reportan canal no entran). Para cada uno se cruza si la dependencia ya tiene una app o un sitio en el
        inventario: ahí el canal existe, pero el trámite no se ofrece por él. El volumen es el que la dependencia reporta al
        catálogo, que no documenta el periodo.
      </p>
      <div className="tabla-scroll">
        <table>
          <thead>
            <tr><th>Trámite</th><th>Dependencia</th><th>Dónde</th><th className="n">Solicitudes</th><th>¿Tiene app o sitio?</th></tr>
          </thead>
          <tbody>
            {inv.brechas.map((b) => {
              const canales = [...b.apps_dependencia, ...b.sitios_dependencia];
              return (
                <tr key={b.homoclave}>
                  <td style={{ whiteSpace: "normal", minWidth: 280 }}>{b.nombre_tramite}</td>
                  <td>{b.siglas}</td>
                  <td>{b.orden === "federal" ? "Federal" : b.municipio ? `${b.municipio}, ${b.estado}` : b.estado}</td>
                  <td className="n">{fmt.format(b.frecuencia ?? 0)}</td>
                  <td style={{ whiteSpace: "normal" }}>{canales.length ? <span className="chip oro">Sí: {canales.slice(0, 2).join(", ")}</span> : <span className="chip">No</span>}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </section>
  );
}
