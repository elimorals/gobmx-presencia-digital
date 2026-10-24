import type { Metadata } from "next";
import { Suspense } from "react";
import { ESTADOS, fmtCompacto, inv, sinAcentos } from "@/lib/datos";

export const metadata: Metadata = {
  title: "Apps de gobierno",
  description: "Las apps móviles de los gobiernos federal, estatales y municipales de México, con su estatus en las tiendas.",
};

const uno = (v: string | string[] | undefined) => (Array.isArray(v) ? v[0] : v) || "";

async function Lista({ searchParams }: PageProps<"/apps">) {
  const sp = await searchParams;
  const q = sinAcentos(uno(sp.q));
  const orden = uno(sp.orden);
  const estado = uno(sp.estado);
  const apps = inv.apps
    .filter((a) => (!orden || a.orden === orden) && (!estado || a.estado === estado))
    .filter((a) => !q || sinAcentos(`${a.nombre} ${a.dependencia} ${a.siglas} ${a.proposito}`).includes(q))
    .sort((a, b) => (b.instalaciones ?? 0) - (a.instalaciones ?? 0));
  return (
    <>
      <form className="panel" action="/apps" style={{ marginBottom: 20 }}>
        <div className="busca">
          <input type="search" name="q" defaultValue={uno(sp.q)} placeholder="Nombre, dependencia o para qué sirve" aria-label="Buscar app" />
          <button className="boton" type="submit">Buscar</button>
        </div>
        <div className="filtros">
          <label>Orden
            <select name="orden" defaultValue={orden}>
              <option value="">Todos</option><option value="federal">Federal</option>
              <option value="estatal">Estatal</option><option value="municipal">Municipal</option>
            </select>
          </label>
          <label>Entidad
            <select name="estado" defaultValue={estado}>
              <option value="">Todas</option>
              {ESTADOS.map((e) => <option key={e}>{e}</option>)}
            </select>
          </label>
        </div>
      </form>
      <p className="dim">{apps.length} apps, de más a menos instalaciones (Google Play)</p>
      <ul className="lista">
        {apps.map((a) => (
          <li className="item" key={`${a.nombre}-${a.android ?? a.ios ?? a.dependencia}`}>
            <h3>{a.nombre}</h3>
            <div className="meta">
              <span>{a.siglas ?? a.dependencia}</span>
              <span>{a.orden === "federal" ? "Federal" : a.municipio ? `${a.municipio}, ${a.estado}` : a.estado}</span>
              <span className={`chip ${a.estatus === "activa" ? "ok" : ""}`}>{a.estatus ?? "sin estatus"}</span>
              {a.instalaciones ? <span>{fmtCompacto.format(a.instalaciones)} instalaciones</span> : null}
              {a.android && <a href={`https://play.google.com/store/apps/details?id=${a.android}`} rel="noopener">Google Play</a>}
              {a.ios && <a href={`https://apps.apple.com/mx/app/id${String(a.ios).replace(/^id/, "")}`} rel="noopener">App Store</a>}
            </div>
            {a.proposito && <p className="dim" style={{ margin: "6px 0 0", fontSize: ".92rem" }}>{a.proposito}</p>}
          </li>
        ))}
      </ul>
    </>
  );
}

export default function Apps(props: PageProps<"/apps">) {
  return (
    <section className="seccion">
      <h1>Apps de gobierno</h1>
      <Suspense fallback={<p className="cargando">Cargando…</p>}>
        <Lista {...props} />
      </Suspense>
    </section>
  );
}
