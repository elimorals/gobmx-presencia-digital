import Link from "next/link";

export default function NoEncontrado() {
  return (
    <section className="seccion">
      <h1>No encontramos esa página</h1>
      <p><Link href="/tramites">Buscar un trámite</Link> o volver al <Link href="/">inicio</Link>.</p>
    </section>
  );
}
