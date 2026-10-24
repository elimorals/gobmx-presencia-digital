import Link from "next/link";
import { BarrasMetrica, clave, Dispersion, GRUPOS, METRICAS, ordenar, Tabla, valor, type Fila } from "@/components/comparar";
import { uno, urlCon } from "@/lib/datos";

const MAX = 6;
const valida = (k: string | undefined, d: string) => (k && METRICAS.some((m) => m.k === k) ? k : d);

/** Comparación lado a lado (2 a 6) + dispersión + tabla completa. Solo estatal↔estatal o municipal↔municipal. */
export async function Comparador({ filas, municipal, base, searchParams }: {
  filas: Fila[]; municipal?: boolean; base: string; searchParams: Promise<Record<string, string | string[] | undefined>>;
}) {
  const sp = await searchParams;
  const todos = new Set(filas.map(clave));
  let sel = (uno(sp.sel) ?? "").split("|").filter((k) => todos.has(k));
  const agregar = uno(sp.agregar);
  if (agregar && todos.has(agregar) && !sel.includes(agregar) && sel.length < MAX) sel = [...sel, agregar];
  const ord = valida(uno(sp.ordenar), "tramites");
  const x = valida(uno(sp.x), "egreso_por_hab"), y = valida(uno(sp.y), "_digital_100k");
  const estado = { sel: sel.join("|") || undefined, ordenar: uno(sp.ordenar), x: uno(sp.x), y: uno(sp.y) };
  const S = sel.map((k) => filas.find((f) => clave(f) === k)!);
  const opciones = filas.filter((f) => !sel.includes(clave(f))).map(clave).sort((a, b) => a.localeCompare(b, "es"));
  const nombre = municipal ? "municipio" : "entidad";

  return (
    <>
      <div className="panel" style={{ marginBottom: 20 }}>
        <h2 style={{ fontSize: "1.15rem" }}>Comparar {municipal ? "municipios" : "entidades"} ({sel.length}/{MAX})</h2>
        <div className="chips" style={{ marginBottom: 10 }}>
          {S.map((f) => (
            <Link key={clave(f)} className="chip oro" href={urlCon(base, { ...estado, sel: sel.filter((k) => k !== clave(f)).join("|") || undefined })}>
              {clave(f)} ×
            </Link>
          ))}
          {sel.length === 0 && <span className="faint">Elige al menos 2.</span>}
        </div>
        {sel.length < MAX && (
          <form action={base} className="busca">
            {estado.sel && <input type="hidden" name="sel" value={estado.sel} />}
            {estado.ordenar && <input type="hidden" name="ordenar" value={estado.ordenar} />}
            <select name="agregar" defaultValue="" aria-label={`Agregar ${nombre}`}>
              <option value="">＋ agregar {nombre}</option>
              {opciones.map((o) => <option key={o} value={o}>{o}</option>)}
            </select>
            <button className="boton" type="submit">Agregar</button>
          </form>
        )}
        <p className="faint" style={{ marginTop: 10, marginBottom: 0 }}>
          ¿Y el orden federal? No se compara: su presupuesto, su extensión y la población que atiende (todo el país) no
          tienen punto de comparación con los de una {municipal ? "entidad o un municipio" : "entidad"}.
        </p>
      </div>

      {S.length >= 2 && GRUPOS.map(([g, titulo]) => {
        const ms = METRICAS.filter((m) => m.g === g && S.some((f) => valor(f, m.k) !== null));
        if (!ms.length) return null;
        return (
          <section key={g} className="seccion">
            <h2 style={{ fontSize: "1.1rem" }}>{titulo}</h2>
            <div className="dos">{ms.map((m) => <BarrasMetrica key={m.k} m={m} filas={S} />)}</div>
          </section>
        );
      })}

      <section className="seccion" id="grafica">
        <h2>Digitalización vs presupuesto</h2>
        <form action={base} className="filtros" style={{ marginBottom: 12 }}>
          {estado.sel && <input type="hidden" name="sel" value={estado.sel} />}
          <label>Eje horizontal<select name="x" defaultValue={x}>{METRICAS.map((m) => <option key={m.k} value={m.k}>{m.t}</option>)}</select></label>
          <label>Eje vertical<select name="y" defaultValue={y}>{METRICAS.map((m) => <option key={m.k} value={m.k}>{m.t}</option>)}</select></label>
          <div style={{ display: "flex", alignItems: "end" }}><button className="boton" type="submit">Graficar</button></div>
        </form>
        <Dispersion filas={filas} x={x} y={y} sel={sel} hrefPunto={(f) => {
          const k = clave(f);
          const nuevo = sel.includes(k) ? sel.filter((s) => s !== k) : sel.length < MAX ? [...sel, k] : sel;
          return urlCon(base, { ...estado, sel: nuevo.join("|") || undefined }) + "#grafica";
        }} />
        {!municipal && x === "egreso_por_hab" && y === "_digital_100k" && (
          <p className="faint" style={{ marginTop: 8 }}>
            Prueba también: <Link href={urlCon(base, { ...estado, x: "_pef_por_hab", y: "_digital_100k" })}>contra PEF por habitante</Link> ·{" "}
            <Link href={urlCon(base, { ...estado, x: "_pef_por_hab", y: "pct_tramites_en_linea" })}>% de trámites en línea contra PEF por habitante</Link>
          </p>
        )}
      </section>

      <section className="seccion">
        <h2>Todas {municipal ? "los municipios" : "las entidades"}</h2>
        <Tabla filas={ordenar(filas, ord)} municipal={municipal} ordenarPor={ord} base={base} sel={sel} extra={estado} />
      </section>
    </>
  );
}
