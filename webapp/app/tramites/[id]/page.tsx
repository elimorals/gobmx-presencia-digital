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
  // ponytail: búsqueda web de la homoclave; enlazar la ficha oficial directa cuando se verifique su patrón de URL
  const buscarGob = `https://www.google.com/search?q=${encodeURIComponent(`"${t.homoclave}" site:gob.mx`)}`;

  return (
    <article className="ficha">
      <p className="faint">
        <Link href="/tramites">Trámites</Link>
        {t.orden !== "federal" && <> · <Link href={`/entidades/${slug(t.estado)}`}>{t.estado}</Link></>}
      </p>
      <h1>{t.nombre_tramite}</h1>
      <p className="meta" style={{ marginBottom: 16 }}>
        <Canal en_linea={t.en_linea} />
        <span className="chip">{t.tipo_registro === "servicio" ? "Servicio" : "Trámite"}</span>
        {t.programa_social && <span className="chip oro">Programa social</span>}
      </p>
      {t.descripcion_ciudadana && <p style={{ fontSize: "1.1rem", maxWidth: "70ch" }}>{t.descripcion_ciudadana}</p>}

      <div className="panel" style={{ margin: "20px 0" }}>
        <dl>
          <dt>Dependencia</dt><dd>{t.dependencia}{t.siglas ? ` (${t.siglas})` : ""}</dd>
          {t.unidad_administrativa && <><dt>Área</dt><dd>{t.unidad_administrativa}</dd></>}
          <dt>Dónde aplica</dt><dd>{lugar(t)}</dd>
          <dt>Cómo se hace</dt><dd>{canales.length ? canales.join(" · ") : "El catálogo no lo reporta"}</dd>
          {t.dirigido && <><dt>Dirigido a</dt><dd>{t.dirigido}</dd></>}
          {t.frecuencia ? <><dt>Solicitudes reportadas</dt><dd>{fmt.format(t.frecuencia)}</dd></> : null}
          <dt>Homoclave</dt><dd className="mono">{t.homoclave}</dd>
          {t.nivel_digitalizacion_sfp && <><dt>Digitalización (SFP)</dt><dd>{t.nivel_digitalizacion_sfp}</dd></>}
          <dt>Actualizado en el catálogo</dt><dd>{t.fecha_actualizacion ?? "sin fecha"}</dd>
          {t.hosts?.length ? <><dt>Sitios citados</dt><dd>{t.hosts.map((h) => <div key={h}><a href={`https://${h}`} rel="nofollow noopener">{h}</a></div>)}</dd></> : null}
        </dl>
      </div>

      {t.descripcion && t.descripcion !== t.descripcion_ciudadana && (
        <>
          <h2>Descripción oficial</h2>
          <p style={{ maxWidth: "70ch" }}>{t.descripcion}</p>
        </>
      )}
      {t.modalidad && <p className="dim">Modalidad: {t.modalidad}</p>}

      <div className="aviso" style={{ marginTop: 24 }}>
        Requisitos, costos y fundamento legal están en la ficha oficial.{" "}
        <a href={buscarGob} rel="noopener">Buscar la homoclave en sitios .gob.mx</a>. Confirma con la dependencia antes de acudir.
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
