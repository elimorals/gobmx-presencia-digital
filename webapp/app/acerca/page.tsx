import type { Metadata } from "next";
import { fmt, inv } from "@/lib/datos";

export const metadata: Metadata = { title: "Acerca y metodología" };

const RAW = "https://raw.githubusercontent.com/elimorals/gobmx-presencia-digital/main";

export default function Acerca() {
  const c = inv.catalogo;
  return (
    <section className="seccion" style={{ maxWidth: "75ch" }}>
      <h1>Acerca y metodología</h1>
      <p>
        Inventario independiente y abierto (MIT) de la presencia digital del Estado mexicano: trámites, apps, sitios web y
        subdominios <code>.gob.mx</code> de los tres órdenes de gobierno. Todo sale de fuentes públicas, por observación
        pasiva: sin escaneo intrusivo, sin cuentas ajenas, sin datos personales. No es un sitio oficial.
      </p>

      <h2>Trámites</h2>
      <p>
        Fuente: el CSV público diario del Catálogo Nacional de Regulaciones, Trámites y Servicios (CONAMER). Corte cargado:{" "}
        <b>{c.fecha_corte}</b>, {fmt.format(c.registros)} trámites ({c.duplicados_internos_quitados} registros repetidos del
        catálogo se dejaron en su versión más reciente). El archivo de cada día aparece a las 20:31 hora del centro y se
        actualiza aquí a diario.
      </p>
      <ul>
        <li><b>En línea</b> significa que el catálogo marca “Medios electrónicos”. “Canal sin dato” significa que no lo reporta; no es lo mismo que “no”.</li>
        <li><b>Solicitudes</b> es el volumen que la dependencia reporta al catálogo; el catálogo no documenta el periodo.</li>
        <li>La cobertura es muy desigual: hay entidades con miles de trámites y otras con uno. Compara con eso en mente.</li>
        <li>El catálogo no trae requisitos, costos ni fundamento legal; están en la ficha oficial de cada trámite.</li>
      </ul>

      <h2>Comparaciones</h2>
      <p>{inv.metodologia}</p>

      <h2>Datos y herramientas</h2>
      <ul>
        <li><a href={`${RAW}/servicios/cnartys/tramites.json`}>Trámites normalizados (JSON, uno por línea)</a></li>
        <li><a href={`${RAW}/apps/apps.json`}>Apps (JSON)</a> · <a href={`${RAW}/sitios/subdominios-consolidado.csv`}>Subdominios (CSV)</a></li>
        <li>API de este sitio: <code>/api/tramites?q=predial&amp;estado=Yucatán&amp;en_linea=si</code></li>
        <li>CLI <code>gobmx.py</code> y servidor MCP <code>gobmx-inventario</code>: ver el <a href="https://github.com/elimorals/gobmx-presencia-digital">repositorio</a>.</li>
      </ul>
    </section>
  );
}
