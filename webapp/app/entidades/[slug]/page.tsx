import type { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import { Cifra, TramiteItem } from "@/components/ui";
import { entidadPorSlug, fmt, inv, slug, topPorLugar } from "@/lib/datos";

export function generateStaticParams() {
  return inv.entidades.map((e) => ({ slug: slug(e.estado) }));
}

export async function generateMetadata({ params }: PageProps<"/entidades/[slug]">): Promise<Metadata> {
  const e = entidadPorSlug((await params).slug);
  return e ? { title: e.estado, description: `Trámites, apps y sitios del gobierno de ${e.estado}.` } : { title: "Entidad no encontrada" };
}

export default async function Entidad({ params }: PageProps<"/entidades/[slug]">) {
  const e = entidadPorSlug((await params).slug);
  if (!e) notFound();
  const apps = inv.apps.filter((a) => a.estado === e.estado && a.orden !== "federal");
  const sitios = inv.sitios.filter((s) => s.estado === e.estado && s.orden !== "federal");
  const municipios = inv.municipios.filter((m) => m.estado === e.estado && m.tramites > 0).sort((a, b) => b.tramites - a.tramites);
  const top = topPorLugar(e.estado, "estatal", 10);

  return (
    <section className="seccion">
      <p className="faint"><Link href="/entidades">Entidades</Link></p>
      <h1>{e.estado}</h1>
      <div className="rejilla seccion">
        <Cifra n={e.tramites} l="trámites estatales en el catálogo" />
        <Cifra n={e.pct_tramites_en_linea === null ? "s/d" : `${e.pct_tramites_en_linea} %`} l="con opción en línea" />
        <Cifra n={e.apps} l={`apps (${e.apps_activas} activas)`} />
        <Cifra n={e.sitios} l="portales web" />
        <Cifra n={e.subdominios} l="subdominios .gob.mx" />
      </div>
      {e.tramites < 30 && (
        <p className="aviso">
          {e.estado} publica muy pocos trámites en el Catálogo Nacional. Que aquí aparezcan pocos no significa que no existan:
          revisa el portal de trámites del gobierno estatal.
        </p>
      )}

      <div className="dos seccion">
        <div>
          <h2>Trámites estatales más solicitados</h2>
          {top.length ? <ul className="lista">{top.map((t) => <TramiteItem key={t.id} t={t} />)}</ul> : <p className="dim">Sin trámites estatales en el catálogo.</p>}
          <p style={{ marginTop: 12 }}><Link href={`/tramites?estado=${encodeURIComponent(e.estado)}`}>Todos los trámites de {e.estado} →</Link></p>
        </div>
        <div>
          <h2>Municipios con trámites publicados</h2>
          {municipios.length ? (
            <ul className="lista">
              {municipios.map((m) => (
                <li className="item" key={m.municipio}>
                  <h3><Link href={`/tramites?estado=${encodeURIComponent(e.estado)}&municipio=${encodeURIComponent(m.municipio ?? "")}&orden=municipal`}>{m.municipio}</Link></h3>
                  <div className="meta">
                    <span>{fmt.format(m.tramites)} trámites</span>
                    <span>{m.pct_tramites_en_linea === null ? "canal sin dato" : `${m.pct_tramites_en_linea} % en línea`}</span>
                  </div>
                </li>
              ))}
            </ul>
          ) : <p className="dim">Ningún municipio de {e.estado} publica trámites en el catálogo.</p>}
        </div>
      </div>

      <div className="dos seccion">
        <div>
          <h2>Apps</h2>
          <ul className="lista">
            {apps.map((a) => (
              <li className="item" key={`${a.nombre}-${a.android ?? a.ios}`}>
                <h3>{a.nombre}</h3>
                <div className="meta">
                  <span>{a.siglas ?? a.dependencia}</span>
                  <span>{a.estatus}</span>
                  {a.android && <a href={`https://play.google.com/store/apps/details?id=${a.android}`} rel="noopener">Google Play</a>}
                  {a.ios && <a href={`https://apps.apple.com/mx/app/id${a.ios}`} rel="noopener">App Store</a>}
                </div>
              </li>
            ))}
          </ul>
        </div>
        <div>
          <h2>Sitios</h2>
          <ul className="lista">
            {sitios.map((s) => (
              <li className="item" key={s.url ?? s.dependencia}>
                <h3>{s.url ? <a href={s.url} rel="noopener">{s.dependencia}</a> : s.dependencia}</h3>
                <div className="meta"><span>{s.tipo}</span>{s.resultado && <span>{s.resultado}</span>}</div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </section>
  );
}
