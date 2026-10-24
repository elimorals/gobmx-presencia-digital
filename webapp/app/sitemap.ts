import type { MetadataRoute } from "next";
import { inv, slug } from "@/lib/datos";

const SITIO = process.env.NEXT_PUBLIC_SITIO ?? "https://gobmx-presencia-digital.vercel.app";

export default function sitemap(): MetadataRoute.Sitemap {
  const fijas = ["", "/tramites", "/entidades", "/brechas", "/apps", "/acerca"].map((p) => ({ url: `${SITIO}${p}` }));
  return [...fijas, ...inv.entidades.map((e) => ({ url: `${SITIO}/entidades/${slug(e.estado)}` }))];
}
