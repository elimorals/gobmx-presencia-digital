import { buscar } from "@/lib/datos";

// GET /api/tramites?q=&orden=&estado=&municipio=&en_linea=si|no|sin_dato&orden_por=reciente&pagina=1
export async function GET(req: Request) {
  const p = new URL(req.url).searchParams;
  const g = (k: string) => p.get(k) || undefined;
  const r = buscar(
    { q: g("q"), orden: g("orden"), estado: g("estado"), municipio: g("municipio"), en_linea: g("en_linea"), orden_por: g("orden_por") },
    Math.max(1, Number(p.get("pagina")) || 1),
  );
  return Response.json(r, { headers: { "Cache-Control": "public, s-maxage=3600, stale-while-revalidate=86400" } });
}
