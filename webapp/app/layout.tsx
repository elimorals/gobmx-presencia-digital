import type { Metadata, Viewport } from "next";
import Link from "next/link";
import { IBM_Plex_Mono, IBM_Plex_Sans, Newsreader } from "next/font/google";
import { inv } from "@/lib/datos";
import "./globals.css";

const sans = IBM_Plex_Sans({ variable: "--f-sans", subsets: ["latin"], weight: ["400", "600"] });
const mono = IBM_Plex_Mono({ variable: "--f-mono", subsets: ["latin"], weight: ["400"] });
const disp = Newsreader({ variable: "--f-disp", subsets: ["latin"], weight: ["600", "700"] });

const SITIO = process.env.NEXT_PUBLIC_SITIO ?? "https://gobmx-presencia-digital.vercel.app";

export const metadata: Metadata = {
  metadataBase: new URL(SITIO),
  title: { default: "Trámites y gobierno digital de México", template: "%s · gobmx" },
  description:
    "Busca entre los trámites federales, estatales y municipales del Catálogo Nacional, y compara la presencia digital de los gobiernos de México: apps, sitios y trámites en línea.",
  openGraph: { type: "website", locale: "es_MX", siteName: "gobmx · presencia digital" },
};

export const viewport: Viewport = {
  themeColor: [
    { media: "(prefers-color-scheme: light)", color: "#fbf8f1" },
    { media: "(prefers-color-scheme: dark)", color: "#1b1815" },
  ],
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html lang="es-MX" className={`${sans.variable} ${mono.variable} ${disp.variable}`}>
      <body>
        <header className="cabecera">
          <div className="contenedor">
            <Link href="/" className="marca">gob<b>mx</b> · presencia digital</Link>
            <nav className="nav" aria-label="Principal">
              <Link href="/tramites">Trámites</Link>
              <Link href="/entidades">Entidades</Link>
              <Link href="/brechas">Brechas</Link>
              <Link href="/apps">Apps</Link>
              <Link href="/acerca">Acerca</Link>
            </nav>
          </div>
        </header>
        <main className="contenedor">{children}</main>
        <footer className="pie">
          <div className="contenedor">
            <p>
              Datos públicos: Catálogo Nacional de Regulaciones, Trámites y Servicios (CONAMER), corte{" "}
              {inv.catalogo.fecha_corte}; tiendas de apps; Certificate Transparency. Proyecto independiente, no oficial.
            </p>
            <p>
              <a href="https://github.com/elimorals/gobmx-presencia-digital">Código y datos (MIT)</a> ·{" "}
              <Link href="/acerca">Metodología</Link>
            </p>
          </div>
        </footer>
      </body>
    </html>
  );
}
