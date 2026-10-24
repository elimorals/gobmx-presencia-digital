import type { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import { Suspense } from "react";
import { Canal } from "@/components/ui";
import { fmt, lugar, slug, tramite } from "@/lib/datos";

export async function generateMetadata({ params }: PageProps<"/tramites/[id]">): Promise<Metadata> {
  const t = tramite(Number((await params).id));
  if (!t) return { title: "Trámite no encontrado" };
  return { title: t.nombre_tramite, description: t.descripcion_ciudadana?.slice(0, 160) ?? `${t.dependencia} · ${lugar(t)}` };
}

async function Ficha({ params }: PageProps<"/tramites/[id]">) {
  const t = tramite(Number((await params).id));
  if (!t) notFound();
  const canales = [t.presencial && "Presencial", t.en_linea && "En línea", t.telefono && "Por teléfono"].filter(Boolean);
  // ficha pública de gob.mx por homoclave; verificado 2026-10-24: 30/30 al azar (los tres órdenes) abren,
  // pero algunos de CDMX dan 404 (4 de 10). No se puede saber de antemano: el aviso lo explica.
  const ficha = `https://www.gob.mx/public/tramites/detalleTramite.xhtml?homoclave=${encodeURIComponent(t.homoclave)}`;

  return (
    <article className="ficha">
      <p className="migas">
        <Link href="/tramites">Trámites</Link>
        {t.orden !== "federal" && <> · <Link href={`/entidades/${slug(t.estado)}`}>{t.estado}</Link></>}
      </p>
      <h1>{t.nombre_tramite}</h1>
      <div className="chips" style={{ marginBottom: 16 }}>
        <Canal en_linea={t.en_linea} />
        <span className="chip">{t.tipo_registro === "servicio" ? "Servicio" : "Trámite"}</span>
        {t.programa_social && <span className="chip oro">Programa social</span>}
      </div>
      {t.descripcion_ciudadana && <p className="texto" style={{ fontSize: "1.08rem" }}>{t.descripcion_ciudadana}</p>}

      <div className="panel" style={{ margin: "20px 0" }}>
        <dl>
          <dt>Dependencia</dt><dd>{t.dependencia}{t.siglas ? ` (${t.siglas})` : ""}</dd>
          {t.unidad_administrativa && <><dt>Área</dt><dd>{t.unidad_administrativa}</dd></>}
          <dt>Dónde aplica</dt><dd>{lugar(t)}</dd>
          <dt>Cómo se hace</dt><dd>{canales.length ? canales.join(" · ") : "Sin dato"}</dd>
          {t.dirigido && <><dt>Dirigido a</dt><dd>{t.dirigido}</dd></>}
          {t.frecuencia ? <><dt>Solicitudes reportadas</dt><dd>{fmt.format(t.frecuencia)}</dd></> : null}
          <dt>Homoclave</dt><dd className="mono">{t.homoclave}</dd>
          {t.nivel_digitalizacion_sfp && <><dt>Nivel de digitalización (SFP)</dt><dd>{t.nivel_digitalizacion_sfp}</dd></>}
          {t.fecha_actualizacion && <><dt>Última actualización</dt><dd>{t.fecha_actualizacion}</dd></>}
          {t.hosts?.length ? <><dt>Sitios citados</dt><dd>{t.hosts.map((h) => <div key={h}><a href={`https://${h}`} rel="nofollow noopener">{h}</a></div>)}</dd></> : null}
        </dl>
      </div>

      {t.descripcion && t.descripcion !== t.descripcion_ciudadana && (
        <>
          <h2>Descripción oficial</h2>
          <p className="texto">{t.descripcion}</p>
        </>
      )}
      {t.modalidad && <p className="dim">Modalidad: {t.modalidad}</p>}
      {t.fuente === "sfp" && (
        <p className="aviso">
          Registro del dataset federal de la SFP, con menos detalle: sin descripción ni canales.
          {t.posible_duplicado ? " Puede ser una versión anterior de otro trámite con el mismo nombre." : ""}
        </p>
      )}

      <div className="panel" style={{ marginTop: 20 }}>
        <p style={{ marginBottom: 12 }}>Requisitos, costos, pasos y fundamento legal están en la ficha oficial.</p>
        <a className="boton" href={ficha} rel="noopener">Ver ficha oficial en gob.mx</a>
        <p className="faint" style={{ marginTop: 12, marginBottom: 0 }}>
          Si la ficha no abre, el trámite no está publicado en gob.mx: búscalo en el portal de {t.siglas ?? t.dependencia}
          {t.hosts?.length ? <> (por ejemplo <a href={`https://${t.hosts[0]}`} rel="nofollow noopener">{t.hosts[0]}</a>)</> : null}{" "}
          con la homoclave <span className="mono">{t.homoclave}</span>. Confirma con la dependencia antes de acudir.
        </p>
      </div>
    </article>
  );
}

export default function Pagina(props: PageProps<"/tramites/[id]">) {
  return (
    <section className="seccion">
      <Suspense fallback={<p className="cargando">Cargando trámite…</p>}>
        <Ficha {...props} />
      </Suspense>
    </section>
  );
}
