import type { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import { fmt, inv, lugar, slug } from "@/lib/datos";

export function generateStaticParams() {
  return inv.apps.map((a) => ({ clave: a.clave }));
}

const app = async (params: Promise<{ clave: string }>) => {
  const c = decodeURIComponent((await params).clave);
  return inv.apps.find((a) => a.clave === c) ?? null;
};

export async function generateMetadata({ params }: PageProps<"/apps/[clave]">): Promise<Metadata> {
  const a = await app(params);
  return a ? { title: a.nombre, description: a.proposito ?? `${a.dependencia} · ${lugar(a)}` } : { title: "App no encontrada" };
}

export default async function FichaApp({ params }: PageProps<"/apps/[clave]">) {
  const a = await app(params);
  if (!a) notFound();
  const p = a.permisos;
  const ios = a.ios ? String(a.ios).replace(/^id/, "") : null;
  return (
    <article className="seccion ficha">
      <p className="migas"><Link href="/apps">Apps</Link>{a.orden !== "federal" && a.estado && <> · <Link href={`/entidades/${slug(a.estado)}`}>{a.estado}</Link></>}</p>
      <h1>{a.nombre}</h1>
      <div className="chips" style={{ marginBottom: 16 }}>
        <span className={`chip ${a.estatus === "activa" ? "ok" : ""}`}>{a.estatus ?? "sin estatus"}</span>
        {a.sector && <span className="chip">{a.sector}</span>}
        {a.requiere_cuenta && <span className="chip oro">Requiere cuenta</span>}
      </div>
      {a.proposito && <p className="texto" style={{ fontSize: "1.08rem" }}>{a.proposito}</p>}
      <div className="acciones" style={{ marginBottom: 20 }}>
        {a.android && <a className="boton" href={`https://play.google.com/store/apps/details?id=${a.android}`} rel="noopener">Google Play</a>}
        {ios && <a className="boton sec" href={`https://apps.apple.com/mx/app/id${ios}`} rel="noopener">App Store</a>}
      </div>
      <div className="panel">
        <dl>
          <dt>Dependencia</dt><dd>{a.dependencia}{a.siglas ? ` (${a.siglas})` : ""}</dd>
          <dt>Dónde</dt><dd>{lugar(a)}</dd>
          <dt>Orden y poder</dt><dd>{a.orden ?? "no oficial"} · {a.poder ?? "—"}{a.subtipo ? ` · ${a.subtipo}` : ""}</dd>
          {a.desarrollador && <><dt>Publica en la tienda</dt><dd>{a.desarrollador}</dd></>}
          {a.instalaciones ? <><dt>Instalaciones (Google Play)</dt><dd>{fmt.format(a.instalaciones)}{a.instalaciones_txt ? ` (${a.instalaciones_txt})` : ""}</dd></> : null}
          {a.play_score ? <><dt>Calificación Google Play</dt><dd>{Number(a.play_score).toFixed(1)} · {fmt.format(Number(a.play_ratings ?? 0))} opiniones</dd></> : null}
          {a.ios_score ? <><dt>Calificación App Store</dt><dd>{Number(a.ios_score).toFixed(1)} · {fmt.format(Number(a.ios_ratings ?? 0))} opiniones</dd></> : null}
          {a.play_version && <><dt>Versión</dt><dd>{a.play_version}</dd></>}
        </dl>
      </div>
      {a.funciones?.length ? (<><h2 style={{ marginTop: 24 }}>Qué hace</h2><ul>{a.funciones.map((f) => <li key={f}>{f}</li>)}</ul></>) : null}

      <h2 style={{ marginTop: 24 }}>Permisos y privacidad</h2>
      {p ? (
        <div className="panel">
          <p>{p.total ?? "?"} permisos en total; <b>{p.peligrosos.length} peligrosos</b>{p.trackers.length ? `; ${p.trackers.length} rastreadores` : "; sin rastreadores detectados"}.</p>
          {p.peligrosos.length > 0 && <div className="chips" style={{ marginBottom: 10 }}>{p.peligrosos.map((x) => <span key={x} className="chip no mono">{x}</span>)}</div>}
          {p.trackers.length > 0 && <div className="chips" style={{ marginBottom: 10 }}>{p.trackers.map((x) => <span key={x} className="chip oro">{x}</span>)}</div>}
          {p.notas && <p className="dim">{p.notas}</p>}
          {p.fecha && <p className="faint">Revisado: {p.fecha}</p>}
        </div>
      ) : <p className="dim">Sin análisis de permisos (solo existe para apps con versión Android).</p>}
      {a.notas && <p className="aviso" style={{ marginTop: 20 }}>{a.notas}</p>}
    </article>
  );
}
