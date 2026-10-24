// Copia los trámites normalizados del repo (../servicios/cnartys) a webapp/data antes de build/dev.
// inventario.json ya vive en webapp/data (lo genera scripts/build_web_data.py en la raíz del repo).
import { copyFileSync, existsSync, mkdirSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

const web = join(dirname(fileURLToPath(import.meta.url)), "..");
const origen = join(web, "..", "servicios", "cnartys", "tramites.json");
if (!existsSync(origen)) {
  console.error(`falta ${origen}: corre python3 -I scripts/cnartys.py actualizar en la raíz del repo`);
  process.exit(1);
}
mkdirSync(join(web, "data"), { recursive: true });
copyFileSync(origen, join(web, "data", "tramites.json"));
console.log("data/tramites.json copiado");
