import type { Metadata, Viewport } from "next";
import Link from "next/link";
import { IBM_Plex_Mono, IBM_Plex_Sans, Newsreader } from "next/font/google";
import { SelectorTema } from "@/components/tema";
import "./globals.css";

const sans = IBM_Plex_Sans({ variable: "--f-sans", subsets: ["latin"], weight: ["400", "600"] });
const mono = IBM_Plex_Mono({ variable: "--f-mono", subsets: ["latin"], weight: ["400"] });
const disp = Newsreader({ variable: "--f-disp", subsets: ["latin"], weight: ["600", "700"] });

const SITIO = process.env.NEXT_PUBLIC_SITIO ?? "https://gobmx-presencia-digital.vercel.app";

export const metadata: Metadata = {
  metadataBase: new URL(SITIO),
  title: { default: "Trámites y gobierno digital de México", template: "%s · gobmx" },
  description:
    "Busca trámites federales, estatales y municipales y compara la presencia digital de los gobiernos de México: apps, sitios, subdominios y trámites en línea.",
  openGraph: { type: "website", locale: "es_MX", siteName: "gobmx · presencia digital" },
};

export const viewport: Viewport = {
  themeColor: [
    { media: "(prefers-color-scheme: light)", color: "#fbf8f1" },
    { media: "(prefers-color-scheme: dark)", color: "#1b1815" },
  ],
};

const SCRIPT_TEMA = `(function(){try{var t=localStorage.getItem("tema");if(t==="light"||t==="dark")document.documentElement.setAttribute("data-theme",t)}catch(e){}})()`;

const NAV = [
  ["/tramites", "Trámites"],
  ["/entidades", "Entidades"],
  ["/municipios", "Municipios"],
  ["/apps", "Apps"],
  ["/sitios", "Sitios"],
  ["/subdominios", "Subdominios"],
  ["/brechas", "Brechas"],
  ["/guia", "Guía"],
  ["/acerca", "Acerca"],
];

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html lang="es-MX" className={`${sans.variable} ${mono.variable} ${disp.variable}`} suppressHydrationWarning>
      <head>
        <script dangerouslySetInnerHTML={{ __html: SCRIPT_TEMA }} />
      </head>
      <body>
        <header className="cabecera">
          <div className="contenedor">
            <div className="fila">
              <Link href="/" className="marca">gob<b>mx</b><span className="sub"> · presencia digital</span></Link>
              <SelectorTema />
            </div>
            <nav className="nav" aria-label="Principal">
              {NAV.map(([href, txt]) => <Link key={href} href={href}>{txt}</Link>)}
            </nav>
          </div>
        </header>
        <main className="contenedor">{children}</main>
        <footer className="pie">
          <div className="contenedor">
            <p>Proyecto independiente y no oficial, construido con datos públicos.</p>
            <p>
              <a href="https://github.com/elimorals/gobmx-presencia-digital">Código y datos (MIT)</a> ·{" "}
              <Link href="/acerca">Fuentes y metodología</Link> · <Link href="/guia">CLI, MCP y API</Link>
            </p>
          </div>
        </footer>
      </body>
    </html>
  );
}
