# 08 — Límites: la lógica Dart es el muro

Este análisis es **estático** y la app es **Flutter AOT**. Es importante separar lo que se observó con
evidencia de lo que **no** es recuperable, para no inventar.

## Qué NO se recuperó (y por qué)

La lógica de negocio de Mi Policía está compilada a código máquina dentro de `libapp.so` (snapshot
**Dart AOT**). A diferencia del DEX (JADX reconstruye Java), un snapshot Dart AOT **no se desensambla a
código fuente** con el herramental de este análisis. Por tanto **no** se afirma, porque no se pudo
leer el código que lo decide:

- **Qué envía exactamente cada flujo** (el botón de contacto/emergencia, el reporte de cuadrante, el
  escaneo): se conoce la **forma de los endpoints** (doc 03), no el cuerpo de cada petición ni qué
  campos de PII arma la app.
- **Cómo persiste la sesión y los tokens del usuario** localmente (no se vio un store cifrado evidente
  en los plugins, doc 04, pero la lógica de guardado es Dart).
- **Cómo se cablea el pinning**: el cert DigiCert G2 está embebido y las primitivas `SecurityContext`
  existen, pero el código Dart que crea el contexto y acepta/rechaza certificados no se aisló → pinning
  es **indicio, no hecho** (doc 04).
- **El uso real de `DUMP`**: declarado en el manifiesto, sin código observable que lo ejerza (doc 07).

## Qué SÍ es sólido (con evidencia)

- Pila Flutter 3.3.4, DEX sin lógica (doc 02).
- Inventario de endpoints y dominios de la SSC (literales del snapshot, doc 03).
- Secretos embebidos: token `Bearer` y token de ruta (cadenas del snapshot, doc 03/07).
- Cert DigiCert G2 embebido y símbolos de TLS de Dart (doc 04).
- Permisos, componentes y ausencia de NSC (manifiesto, doc 01).
- Firma del desarrollador individual + Source Stamp de APKPure (`apksigner`, doc 06).
- Plugins Flutter (doc 05).

## Cómo profundizar (fuera de este alcance estático)

- Volcado del snapshot Dart con herramienta especializada (p. ej. reflutter / blutter) para reconstruir
  clases y funciones Dart — recupera la lógica que aquí es muro.
- Análisis **dinámico** con Frida sobre `libapp.so` (hook de `package:http` y de `SecurityContext`) para
  ver peticiones reales y confirmar el pinning — requiere dispositivo y repackage; no se hizo (estático).
