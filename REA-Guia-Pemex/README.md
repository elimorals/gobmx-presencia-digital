# Ingeniería inversa de Guía Pemex 3.6.3 — índice

Análisis **estático** del APK `com.pemex.guiapemex` con REA (inspección de archivo). La app
**nunca se ejecutó**. Es la app oficial de **Petróleos Mexicanos (Pemex)** para localizar
estaciones de servicio. **#42 por instalaciones** del inventario gob.mx (≈100 K).

> **Flutter — lógica en el snapshot Dart (muro).** `assets/flutter_assets/` + `libapp.so`/
> `libflutter.so` en el split de arquitectura; la lógica vive en el snapshot Dart AOT, no en el
> DEX → no decompilable estáticamente. Pero el bundle Flutter trae un **`.env` en claro con
> SECRETOS**: la **API key del backend propio de Pemex**, más Google Maps, TomTom y Azure App
> Insights. Firma **auténtica de Pemex** (`O=PEMEX`). Hermana de **Pemex ASISTE** (misma empresa,
> mismo backend `api-cloud.pemex.com`, mismo patrón `flutter_dotenv`) — pero Guía Pemex filtra
> **más** secretos.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, `.env`, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación), componentes |
| 02 | [02-flutter-muro.md](02-flutter-muro.md) | Flutter/Dart AOT, `.env` con secretos |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | `api-cloud.pemex.com/guiapemex`, mapas |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Firma auténtica de Pemex, procedencia |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/flutter.env`
(secretos reales, **no** versionado), `evidence/firma.txt` (no versionado).

## 5 hallazgos principales

1. **(Medio-Alto) API key del backend de Pemex en claro en el `.env`.** `assets/flutter_assets/.env`
   incluye `APP_SERVICES_ENDPOINT=https://api-cloud.pemex.com/guiapemex` **y**
   `APP_SERVICES_API_KEY=<clave>` en texto plano, recuperable de cualquier copia del APK. No es
   una clave pública por diseño: es la credencial de acceso al backend propio de Pemex (doc 02,
   05). Valor real solo en `evidence/`.
2. **(Bajo-Medio) Llaves de Google Maps y TomTom embebidas.** `GOOGLE_MAPS_API_KEY` y
   `TOM_TOM_KEY` en el mismo `.env`. Si no están restringidas por package/SHA1 o referer, son
   abusables (consumo a cargo de Pemex) (doc 02, 03).
3. **(Bajo) Clave de Application Insights + ColorMap.** `APPSINSIGHTS_INSTRUMENTATION_KEY`
   (telemetría; baja sensibilidad) y `COLOR_MAP_*_KEY` en claro (doc 02).
4. **Flutter = lógica en el snapshot Dart (muro).** `libapp.so` (Dart AOT); el DEX es embedding.
   Rutas completas de API bajo `/guiapemex`, auth y manejo de datos no observables
   estáticamente (doc 02).
5. **Firma auténtica de Pemex (positivo).** El APK está firmado por `O=PEMEX` y la firma
   verifica → binario íntegro del emisor; APKPure solo añadió un Source Stamp al redistribuirlo
   (doc 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK ni el `.env` (los valores reales de las claves viven solo en
`evidence/`, `.gitignore`). Observaciones **estáticas**: no se ejecutó la app, no se usó ninguna
de las claves ni se llamó a ningún endpoint. La lógica Dart no se desensambló (muro). Dado que
se exponen credenciales activas, el doc 05 recomienda **divulgación responsable** a Pemex y
**rotación** de la API key.
