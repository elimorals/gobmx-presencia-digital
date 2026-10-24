import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  cacheComponents: true,
  partialPrefetching: true,
  // data/tramites.json lo copia scripts/copiar-datos.mjs (prebuild) y se lee con fs en el servidor
  outputFileTracingIncludes: { "/**": ["./data/tramites.json"] },
};

export default nextConfig;
