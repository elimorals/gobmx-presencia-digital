# Arquitectura Flutter — el muro

## Confirmación

- `assets/flutter_assets/` presente (`AssetManifest.bin`, 506 entradas de assets) + `libapp.so`
  (Dart AOT) + `libflutter.so` en el split de arquitectura. Confirmado Flutter.
- **No hay `.env`** en `flutter_assets` (a diferencia de Pemex ASISTE): solo `AssetManifest.bin`
  y recursos (`assets/images/config.svg` es un ícono, no configuración). → la config y el
  backend van **dentro del snapshot Dart**.

## Por qué es un muro

La lógica de negocio (backend `*.queretaro.gob.mx`, auth, trámites, uso de ubicación/contactos/
micrófono) se compila al **snapshot Dart AOT** dentro de `libapp.so`. JADX solo ve el embedding
de Flutter + SDKs; no hay bytecode Java/Kotlin con esa lógica. En el DEX **no** aparecen
endpoints propios del gobierno de Querétaro — solo endpoints de SDKs (doc 03), lo que confirma
que el backend vive en el snapshot.

Recuperarlo requiere herramientas de Flutter (**reflutter**, **blutter**) o **análisis dinámico**
(Frida sobre el runtime Dart) — ambas **dinámicas / fuera del alcance** estático de este
repositorio. Se documenta el muro y lo recuperable del DEX (permisos, SDKs), sin inventar el
contenido del snapshot. Mismo veredicto que Pemex ASISTE, SAT Móvil, Urbani, Qrobús.
