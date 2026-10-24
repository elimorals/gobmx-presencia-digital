import type { Metadata } from "next";
import { fmt, inv } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Brechas digitales",
  description: "Trámites con mucha demanda y sin opción en línea, servicios que faltan en algún canal y trámites federales clave.",
};

const CANAL: Record<string, string> = { si: "Sí", no: "No", parcial: "Parcial" };
type Canales = { web?: string; app?: string; presencial_obligatorio?: boolean; whatsapp_telefono?: boolean };

function Canales({ c }: { c?: Canales }) {
  if (!c) return null;
  return (
    <div className="chips">
      <span className="chip">Web: {CANAL[c.web ?? ""] ?? c.web ?? "?"}</span>
      <span className="chip">App: {CANAL[c.app ?? ""] ?? c.app ?? "?"}</span>
      {c.presencial_obligatorio && <span className="chip no">Presencial obligatorio</span>}
      {c.whatsapp_telefono && <span className="chip ok">WhatsApp/teléfono</span>}
    </div>
  );
}

export default function Brechas() {
  return (
    <section className="seccion">
      <h1>Brechas digitales</h1>

      <h2>Mucha demanda, sin opción en línea</h2>
      <p className="texto dim">
        Los {inv.brechas.length} trámites con más solicitudes reportadas que se marcan sin opción en línea (los que no reportan
        canal no entran). Se cruza si la dependencia ya tiene una app o un sitio: ahí el canal existe, pero el trámite no se
        ofrece por él. El volumen lo reporta cada dependencia y no trae periodo documentado.
      </p>
      <div className="tabla-scroll">
        <table>
          <thead><tr><th>Trámite</th><th>Dependencia</th><th>Dónde</th><th className="n">Solicitudes</th><th>¿Tiene app o sitio?</th></tr></thead>
          <tbody>
            {inv.brechas.map((b) => {
              const canales = [...b.apps_dependencia, ...b.sitios_dependencia];
              return (
                <tr key={b.homoclave}>
                  <td className="ancho">{b.nombre_tramite}</td>
                  <td>{b.siglas}</td>
                  <td>{b.orden === "federal" ? "Federal" : b.municipio ? `${b.municipio}, ${b.estado}` : b.estado}</td>
                  <td className="n">{fmt.format(b.frecuencia ?? 0)}</td>
                  <td className="ancho">{canales.length ? <span className="chip oro">Sí: {canales.slice(0, 2).join(", ")}</span> : <span className="chip">No</span>}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      <h2 style={{ marginTop: 36 }}>Servicios que faltan en algún canal</h2>
      <p className="texto dim">Brechas detectadas al cruzar el inventario de apps con el de sitios, servicio por servicio.</p>
      <ul className="lista">
        {inv.brechas_canal.map((b) => (
          <li className="item" key={b.servicio}>
            <h3>{b.servicio}</h3>
            <div className="meta" style={{ marginBottom: 6 }}><span>{b.orden}</span><span>{b.estado}</span>{b.sector && <span>{b.sector}</span>}</div>
            <Canales c={b.canales as Canales} />
            <p>{b.brecha}</p>
          </li>
        ))}
      </ul>

      <h2 style={{ marginTop: 36 }}>Trámites federales clave</h2>
      <p className="texto dim">Trámites de alto uso revisados a mano: por qué canal se hacen, con qué identidad y qué falta.</p>
      <ul className="lista">
        {inv.curado.map((c) => (
          <li className="item" key={c.servicio}>
            <h3>{c.url_web ? <a href={c.url_web} rel="noopener">{c.servicio}</a> : c.servicio}</h3>
            <div className="meta" style={{ marginBottom: 6 }}>
              <span>{c.siglas ?? c.dependencia}</span>
              {c.homoclave && <span className="mono">{c.homoclave}</span>}
              {c.nivel_digitalizacion_cnts && <span className="chip oro">{c.nivel_digitalizacion_cnts}</span>}
            </div>
            <Canales c={c.canales as Canales} />
            {c.app_relacionada && <p>App: {c.app_relacionada}</p>}
            {c.autenticacion && <p>Identidad: {String(c.autenticacion)}</p>}
            {c.brecha && <p>{c.brecha}</p>}
          </li>
        ))}
      </ul>
    </section>
  );
}
