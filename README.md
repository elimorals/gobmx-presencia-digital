# gobmx-presencia-digital

Inventario y análisis de la **presencia digital del Estado mexicano**: aplicaciones
móviles, sitios web y servicios en línea de dependencias federales, organismos
autónomos, paraestatales, los tres poderes, estados y municipios.

Todo se construye con **observación pasiva** de fuentes públicas (tiendas de apps,
Certificate Transparency, páginas públicas) y búsqueda web. No hay escaneo intrusivo,
ni login con credenciales ajenas, ni envío de datos personales.

🔗 **Web:** https://gobmx-presencia-digital.vercel.app (Vercel; la versión de GitHub Pages se retiró el 2026-10-24)

Además del inventario, el repo incluye **ingeniería inversa a fondo de 45 apps** del catálogo
(carpetas `REA-*/`): pila tecnológica, red, criptografía, protección y hallazgos de seguridad de
cada una. Ver [§ Ingeniería inversa de apps](#ingeniería-inversa-de-apps-rea).

## Cifras (corte 2026-10-24; trámites al 2026-10-23)

| | |
|---|---:|
| Apps móviles | **519** (387 Android · 371 iOS · 274 en ambas) |
| Sitios web | **394** portales |
| Subdominios `.gob.mx` | **12,867** (CT + dotgob-mx + citados en trámites) |
| Servicios / trámites | **35,810**: 31,802 del Catálogo Nacional (CNARTyS, corte 2026-10-23: 5,583 federales · **14,608 estatales** · **11,611 municipales**) + 3,992 federales de la SFP que no están en él + 16 brechas/clave |
| Apps con análisis de permisos | **387** (todas las que tienen Android; 338 con permisos peligrosos) |

No existe un equivalente oficial: los catálogos del gobierno están en silos separados. Los trámites
estatales y municipales **sí** se publican de forma central, en el CSV diario del Catálogo Nacional,
aunque con cobertura muy desigual por entidad (ver `servicios/harvest-subnacional.md`).

## Estructura

```
gobmx-presencia-digital/
├── README.md                    ← este archivo
├── REA-*/                       ← ingeniería inversa de 10 apps gob.mx (una carpeta por app, con su README)
├── llms.txt                     ← resumen para LLMs (robots y sitemap los sirve la web)
├── apps/
│   ├── inventario/*.json        ← apps por segmento (federal, autónomos, estatal A/B/C+CDMX+municipal)
│   ├── apps.json                ← consolidado + métricas de tienda (genera consolidar_incremental.py)
│   ├── apps.csv                 ← mismo, tabular
│   └── permisos/                ← permisos peligrosos por app (387) + README
├── sitios/
│   ├── *.json                   ← portales web por segmento (esquema-sitios-servicios.md)
│   ├── subdominios/*.txt        ← subdominios por dependencia desde Certificate Transparency
│   └── subdominios-consolidado.csv  ← 12,812 hosts .gob.mx (nuestro CT + dotgob-mx) con flag de actividad
├── servicios/
│   ├── brechas.json             ← servicios que faltan o solo existen en un canal (cruce apps×sitios)
│   ├── catalogo-conamer.json    ← trámites de alto uso con homoclave (CONAMER)
│   ├── cnts-federal.json        ← catálogo federal de la SFP (6,125 trámites, con nivel_digitalizacion)
│   ├── cnartys/                 ← Catálogo Nacional completo, 3 órdenes (genera scripts/cnartys.py)
│   │   ├── tramites.json        ← 31,802 trámites normalizados, uno por línea
│   │   ├── meta.json            ← corte, archivo, sha256, conteos
│   │   └── hosts.txt            ← hosts .gob.mx citados en los trámites
│   └── harvest-subnacional.md   ← de dónde sale lo estatal/municipal y la regla de descarga diaria
├── analisis/
│   ├── comparacion.md           ← documento final: comparación a detalle
│   ├── seguridad.md             ← hallazgos transversales (paquetes de prueba, anti-bot, incidentes)
│   ├── benchmark-existentes.md  ← qué otros directorios/catálogos existen y qué les falta
├── webapp/                      ← web en Vercel (Next.js); datos en webapp/data/inventario.json
├── web/                         ← dashboard estático anterior (ya no se publica)
├── gobmx.py                     ← CLI para consultar el inventario
├── mcp-server/                  ← MCP server (FastMCP) con 8 tools de consulta + README y prueba e2e
└── scripts/                     ← helpers (tiendas, CT, consolidar, qa, stats, regenerar) + esquemas
```

## Web (Vercel) — `webapp/`

**https://gobmx-presencia-digital.vercel.app** · Next.js 16 · tema claro, oscuro o automático · mobile first.

| Sección | Qué hay |
|---|---|
| `/tramites` | Buscador de ~35.8 mil trámites (registro nacional + SFP): texto, entidad, municipio, ¿en línea?, orden por solicitudes o fecha |
| `/tramites/[id]` | Ficha: canales, dependencia, homoclave, solicitudes, sitios citados y botón a la ficha oficial `gob.mx/public/tramites/detalleTramite.xhtml?homoclave=…` (30/30 al azar abren; algunos de CDMX dan 404 y la ficha lo explica) |
| `/entidades`, `/municipios` | Comparar de 2 a 6 lado a lado (absolutos, trámites, por habitante, por egreso INEGI, PEF Ramo 28+33, población); dispersión "Digitalización vs presupuesto" con ejes elegibles, abreviaturas de estado y puntos que se tocan para agregarlos; tabla ordenable. El orden federal no se compara |
| `/entidades/[estado]` | Ficha estatal: trámites más solicitados, municipios, apps y sitios |
| `/apps`, `/apps/[app]` | 519 apps con filtros (entidad, sector, estatus, permisos peligrosos) y ficha con permisos, rastreadores y calificaciones |
| `/sitios` | 394 portales: plataforma, anti-bot, acceso y evidencia fechada |
| `/subdominios` | 12.8 mil hosts `.gob.mx` con búsqueda y filtros de fuente, actividad y entidad |
| `/brechas` | Mucha demanda sin opción en línea (200), brechas por canal y trámites federales clave |
| `/guia`, `/acerca` | CLI, MCP, API y datos crudos; fuentes y metodología |
| `/api/tramites` | JSON: `q`, `orden`, `poder`, `estado`, `municipio`, `en_linea`, `orden_por`, `pagina` |

**Filtro común orden × poder.** En trámites, apps, sitios y el inicio, la matriz Federal/Estatal/Municipal/Nacional ×
Ejecutivo/Legislativo/Judicial/Autónomo filtra al tocar una celda, muestra los ceros (borde punteado: no hay registros
en ese cruce; en apps y sitios respaldados por `apps/inventario/BUSQUEDA-PODERES.md`) y enlaza el mismo ámbito en las
otras secciones.

**Datos.** Los trámites se leen en el servidor (`servicios/cnartys/tramites.json`, copiado en `prebuild`); lo demás
viene de `webapp/data/` (`inventario.json`, `subdominios.json`, `sfp.json`), que genera `scripts/build_web_data.py` con
las mismas funciones de `gobmx.py`.

```bash
python3 -I scripts/build_web_data.py      # regenera webapp/data/*.json
cd webapp && npm install && npm run dev   # http://localhost:3000
```

Despliegue: proyecto de Vercel con **Root Directory = `webapp`** conectado al repo; cada push a `main`
(incluidos los commits diarios del workflow de trámites) redepliega. `NEXT_PUBLIC_SITIO` fija la URL canónica.

### Dashboard estático anterior — `web/`

Ya no se publica (GitHub Pages se retiró el 2026-10-24). Todo lo que mostraba está en la web nueva. Sigue
funcionando local (`cd web && python3 -m http.server`); lo empaqueta `scripts/build_dashboard_data.py` en `web/data.js`.

## CLI — `gobmx`

Consulta el inventario desde la terminal. Único requisito: Python 3, sin dependencias externas.

```bash
python3 gobmx.py stats                    # 519 apps · 394 sitios · 6141 servicios
python3 gobmx.py apps --estado Jalisco    # apps de un estado
python3 gobmx.py apps --sector seguridad --json
python3 gobmx.py sitios --nivel federal
python3 gobmx.py servicios --busca CURP
python3 gobmx.py dep IMSS                  # apps + sitios de una dependencia
python3 gobmx.py apps --orden estatal --poder judicial    # filtros por orden de gobierno y poder
python3 gobmx.py comparar --orden estatal --nombres "Jalisco,Nuevo León"
python3 gobmx.py comparar --orden municipal --nombres Monterrey,Guadalajara
python3 gobmx.py comparar --orden estatal --ordenar por_100k_hab.apps   # ranking de las 32
python3 gobmx.py comparar --orden federal  # se rechaza: no es comparable
python3 gobmx.py comparar --orden estatal --ordenar pct_tramites_en_linea   # % de trámites en línea

# trámites (CNARTyS + SFP + brechas)
python3 gobmx.py servicios --orden municipal --municipio Mérida --en-linea si
python3 gobmx.py servicios --estado Tabasco --busca vehicular --por-frecuencia --limite 10
python3 gobmx.py brechas --limite 20         # mayor volumen sin canal en línea × apps/sitios de la dependencia
python3 gobmx.py catalogo --sondear          # corte cargado y qué archivos diarios hay hoy en el servidor
python3 gobmx.py catalogo --actualizar       # baja el de ayer (o el más reciente) y lo normaliza
```

## Trámites de los tres órdenes (Catálogo Nacional)

`scripts/cnartys.py` baja `tramites_publicados_DD_MM_YYYY.csv` del Catálogo Nacional (CONAMER) y lo
normaliza en `servicios/cnartys/`. El archivo del día D aparece ~02:31 UTC del día D+1 y el servidor
guarda solo los 2 más recientes, así que **hoy se baja el de ayer** y, si no está, se retrocede hasta 7
días. Lo corre a diario `.github/workflows/refresh-tramites.yml`. Detalle, evidencia y lo pendiente de
verificar (fines de semana, IP del runner) en `servicios/harvest-subnacional.md`.

- `orden` y `poder` salen directo de `nivelGobierno` / `nivelGobiernoDetalle` del catálogo.
- Complementa a `cnts-federal.json` (SFP), no lo reemplaza: 2,133 federales se ligan (1,976 por homoclave,
  157 por nombre+siglas único). Esos llevan `nivel_digitalizacion_sfp` y `servicios()` no los duplica.
  El catálogo trae 364 registros repetidos (versión vieja + nueva); queda la más reciente. Detalle en
  `servicios/README.md` § Sin duplicados.
- `comparar` suma `tramites`, `tramites_en_linea`, `tramites_sin_dato_canal` y `pct_tramites_en_linea`
  por entidad/municipio, más una `referencia_federal` de contexto. **Ojo:** el conteo mide cuánto captura
  cada entidad en el catálogo (Jalisco publica 1), y el % se calcula solo sobre los que reportan canal.
- `brechas` / tool `brechas_volumen`: trámites de mayor `frecuencia` (volumen que reporta el catálogo)
  marcados sin canal en línea, cruzados por siglas con apps y sitios del inventario.
- Los hosts `.gob.mx` citados en los trámites entran a `sitios/subdominios-consolidado.csv` con
  `fuente=cnartys-tramites`.

## Orden de gobierno, poder y comparación

Cada app, sitio y servicio lleva `orden` (federal · estatal · municipal), `poder` (ejecutivo ·
legislativo · judicial · autónomo), `subtipo` (paraestatal, desconcentrado, universidad_autonoma,
extinto…) y `municipio` (nombre INEGI cuando el orden es municipal). `nivel` se conserva por
compatibilidad pero mezclaba orden con tipo de ente. Los asigna `scripts/clasificar_orden_poder.py`
con reglas por nivel, patrones de nombre, una tabla de excepciones y una tabla de las 32 fiscalías
estatales (autónoma o dependiente del Ejecutivo, con fuente). Reglas en `scripts/esquema.md`.

**Comparación:** solo estatal↔estatal o municipal↔municipal; nunca federal contra otro orden
(presupuesto, extensión y población no comparables). Métricas por entidad/municipio: apps, apps
activas, instalaciones, sitios, subdominios, cobertura de poderes y, con `datos/presupuesto/`,
tasas por 100 mil habitantes (Censo 2020 INEGI) y por cada mil millones de pesos de egreso (INEGI
EFIPEM). Sin dato, las tasas son `null`, nunca 0. CLI (`comparar`), MCP (`comparar`) y dashboard
(`window.GOBMX.entidades` / `.municipios`) usan la misma función de `gobmx.py`.

## MCP — `gobmx-inventario`

**Remoto, sin instalar nada:** `https://gobmx-presencia-digital.vercel.app/mcp` (Streamable HTTP, sin cuenta, solo
lectura; lo sirve la web en `webapp/app/mcp/route.ts` con `mcp-handler`). 10 tools: `stats`, `buscar_tramites`,
`tramite` (con enlace a la ficha oficial de gob.mx), `matriz_orden_poder_tramites`, `brechas_volumen`, `comparar`
(rechaza federal), `buscar_apps` (con permisos), `buscar_sitios`, `buscar_subdominio`, `catalogo_tramites`.

```bash
claude mcp add --transport http gobmx https://gobmx-presencia-digital.vercel.app/mcp
# Claude web/escritorio: Configuración → Conectores → conector personalizado con esa URL
# Cursor y otros: { "mcpServers": { "gobmx": { "url": "https://gobmx-presencia-digital.vercel.app/mcp" } } }
.venv/bin/python -I mcp-server/test_remoto.py   # prueba end-to-end contra producción
```

**Local (Python):** servidor [MCP](https://modelcontextprotocol.io) de solo lectura: expone el inventario como 10 tools
consultables desde Claude u otro cliente. Instalación, referencia de tools y prueba end-to-end en
[`mcp-server/README.md`](mcp-server/README.md).

```bash
uv venv .venv && VIRTUAL_ENV=.venv uv pip install "mcp<2"
claude mcp add gobmx-inventario -- \
  "$PWD/.venv/bin/python" -I "$PWD/mcp-server/server.py"
claude mcp list   # debe decir: gobmx-inventario ... ✔ Connected
```

## Para agentes / LLMs

- [`llms.txt`](llms.txt) — resumen y enlaces a todos los datos, pensado para modelos.
- Datos crudos por HTTP (sin auth): `apps/apps.json`, `apps/permisos/permisos.json`,
  `sitios/*.json`, `servicios/*.json`, `sitios/subdominios-consolidado.csv`.
- Esquemas en `scripts/esquema.md` y `scripts/esquema-sitios-servicios.md`.

## Regenerar

```bash
scripts/regenerar.sh   # subdominios + clasifica orden/poder + consolida apps + QA + dashboard
SIN_RED=1 scripts/regenerar.sh   # igual, sin consultar tiendas
python3 -I scripts/test_orden_poder.py   # comprobación del clasificador y de comparar
.venv/bin/python -I scripts/build_guia.py   # ejemplos reales (MCP y CLI) para /guia; correr tras cambiar tools o flags
```

La documentación de uso vive en la web: [`/guia`](https://gobmx-presencia-digital.vercel.app/guia) (cuál usar y solución de
problemas), [`/guia/mcp`](https://gobmx-presencia-digital.vercel.app/guia/mcp) (cada tool con parámetros y respuesta real,
recetas, remoto vs local), [`/guia/cli`](https://gobmx-presencia-digital.vercel.app/guia/cli) (cada comando con salida real)
y [`/guia/api`](https://gobmx-presencia-digital.vercel.app/guia/api) (API, campos de cada registro, datos crudos).

Un **GitHub Action** (`.github/workflows/refresh-subdominios.yml`) refresca los subdominios por
Certificate Transparency y reconstruye `web/data.js` una vez al mes. Otro
(`.github/workflows/refresh-tramites.yml`) baja a diario el corte del Catálogo Nacional de trámites.

## Ingeniería inversa de apps REA

Análisis a fondo de **45 apps** del inventario (carpetas `REA-*/`), una carpeta por app, cada una con su propio README e
índice de documentos (identidad, manifiesto, autenticación, red/endpoints, criptografía, almacenamiento,
protección/ofuscación, SDKs, firma y hallazgos). El método base es **estático** con REA (JADX headless
0.7.1 + apktool 3.0.3) sobre el APK; cuando se indica, hay además un **pase dinámico** en dispositivo
físico (sin root salvo nota). Todas las afirmaciones llevan evidencia y se verificó el package real
**antes** de documentar, tras detectar un artefacto mal nombrado en el acervo (ver Banco del Bienestar).

| App | Package | Dependencia | Pila / protección | Hallazgo o rasgo clave |
|---|---|---|---|---|
| [AforeMóvil](REA-AforeMovil/README.md) 6.0.15 | `mx.com.procesar.aforemovil` | Procesar / CONSAR (SAR–AFORE) | Nativa, R8 + anti-tamper (`toolChecker`), ocultamiento nativo de claves | Biometría Daon (FIDO/UAF, liveness), `api.esar.io`, **sin TLS pinning**; firma auténtica de Play (`O=procesar`) |
| [App CDMX](REA-App-CDMX/README.md) 4.5.2 | `mx.gob.cdmx.adip.apps` | GCDMX / ADIP | Nativa, license check **PairIP** · estático + dinámico | Super-app con ~24 servicios (pagos, Mi Taxi, Llave CDMX, NFC de transporte, botón de pánico) |
| [Banco del Bienestar](REA-BancoBienestar/README.md) 7.17.0 | `gob.bancodelbienestar.bcobienapp` | Banco del Bienestar | Low-code **Veritran** (Duktape/`.szip`), **RASP fuerte** (root/Xposed/Frida/emulador/packer nativo) | Lógica bancaria interpretada, no decompilable; **sin endpoints en claro**; el APK del acervo era Aptoide mal nombrado → se reextrajo de Play |
| [BilletesMx](REA-BilletesMx/README.md) 5.5 | `org.banxico.dge.movil` | Banxico | **Unity IL2CPP** 6000.2.7f2 + EasyAR · estático + dinámico (Il2CppDumper) | RA que explica la seguridad de los billetes de la familia G |
| [CFE Contigo](REA-CFE-Contigo/README.md) 7.0.0 | `mx.com.cfe.cfecontigo` | CFE | Nativa, **ofuscada con DexGuard** | App **#1 por instalaciones** del inventario (~37.45 M); backend oculto por la ofuscación |
| [IMSS Digital](REA-IMSS-Digital/README.md) 8.7.4 | `st.android.imsspublico` | IMSS | **Xamarin/.NET** (C# descomprimible) | **#3 por instalaciones** (~32.25 M); se recuperaron **83 endpoints reales** y el stack de identidad — el análisis más revelador del lote |
| [Mi Cuenta Infonavit](REA-Infonavit/README.md) 5.3.48 | `com.infonavit.micuentainfonavit` | INFONAVIT | Nativa (Kotlin/Java) | **Crítico:** el build de producción **desactiva validación de certificado y hostname TLS** (`getUnsafeOkHttpClient`, sin pinning) → MITM sobre auth, pagos y PII |
| [Licencia Federal Digital](REA-Licencia/README.md) 1.0.49 | `mx.sct.licencia` | SCT / SICT | **Híbrida Capacitor/Ionic/Angular** (JS legible, sin hardening) | Guarda licencia + CURP/RFC/email/password/PIN en **SQLCipher con llave = PIN**, y existe una **ruta que abre la DB sin llave**; backend `wslfd.sct.gob.mx` |
| [SAT Móvil](REA-SAT-Movil/README.md) 2.4.4 | `mx.gob.sat` | SAT | **Flutter** (Dart AOT) · estático + dinámico acotado | e.firma portable + SAT ID; lógica y endpoints en `libapp.so`, no en el DEX |
| [Urbani / MiRuta](REA-Urbani/README.md) 3.1.5 | `com.miruta.mty` | Municipal (MTY, Nuevo León) | **Flutter** cifrado por **packer Baidu** · estático + dinámico (logcat) | Transporte público del AMM; backend privado `urbani.io` recuperado del logcat; APK re-firmado (`CN=Android`) |
| [911 CDMX](REA-911-CDMX/README.md) | `com.desarrollo.c5.app_066_android_2` | C5 / CAEPCCM (CDMX) | Nativa Kotlin/Java (Volley), sin ofuscar | **Backend de emergencias 100% HTTP en claro** (ubicación, tipo de sangre, OTP, alerta de auxilio) → MITM; IP interna + secreto Twitter en claro; firma auténtica (`O=CAEPCCM`) |
| [Apoyo Ciudadano INE](REA-Apoyo-Ciudadano-INE/README.md) | `mx.ine.apoyociudadano` | INE | Nativa Kotlin/Java + OpenCV/cardIO/tesseract | Sube PII electoral empaquetada en ZIP; llaves Twitter + endpoints cifrados **AES-ECB** en el APK; cripto débil; firma auténtica del INE |
| [BanjeCel](REA-BanjeCel/README.md) 7.10.3 | `mx.com.banjercito.model.common.VTUserApplicationBJCTMB` | Banjército | Low-code **Veritran** (Duktape), **RASP** (`tool-checker`), SQLCipher | Biometría on-device **facial (liveness/anti-spoof) + voz** (SMMA); lógica interpretada, no decompilable; **firma auténtica de Banjército** |
| [ICVNL](REA-ICVNL/README.md) | `com.icv.app` | Control Vehicular NL | **.NET MAUI/Xamarin** (425 DLL del AssemblyStore) | Cleartext + WebView con puente JS→C#; sin cripto de app; pagos y liveness en portal web; firma auténtica del ICVNL |
| [Licencia Digital EDOMEX](REA-Licencia-EDOMEX/README.md) | `mx.com.cosmocolor.movapp` | SEMOV Edomex | Nativa Kotlin/Java (SQLCipher) | **Crítico: `TrustManager` vacío + `hostnameVerifier=true`** (TLS desactivado) sobre pago/CVV/OTP/facial; restos de Chiapas (white-label Cosmocolor); re-firmada |
| [Mi Policía CDMX](REA-Mi-Policia-CDMX/README.md) | `com.moobky.MiPolicia` | SSC CDMX | **Flutter** (Dart AOT) | Token Bearer + API token **hardcodeados** en `libapp.so`; app de seguridad **firmada por un dev individual** (`CN=Saul Espinosa Ruiz`), no la SSC |
| [Pemex ASISTE](REA-Pemex-ASISTE/README.md) 3.1.19 | `com.pemex.asiste` | Pemex | **Flutter** (Dart AOT) + `flutter_dotenv` | `.env` en claro con backend `api-cloud.pemex.com` + clave de Application Insights (telemetría); lógica en `libapp.so`; re-firmada |
| [SEG Móvil](REA-SEG-Movil/README.md) 1.0.32 | `com.artech.sdsigueme.sigueme` | SEG (Educación Guanajuato) | **GeneXus / .NET** | Config completa en `appsettings.json`: 6 backends HTTPS + **Azure AD B2C** (sin secreto); `paseLista` en `azurewebsites.net`; posible **IDOR** en descarga de certificados por `{folio}` |
| [Tarjeta Rosa](REA-Tarjeta-Rosa/README.md) 1.0.5 | `gob.guanajuato.TarjetaRosa` | GEG (Guanajuato) | Nativa, wrapper de **WebView** | Puente JS `openUrl` abre **intents arbitrarios sin validar** + navegación no acotada; **no desactiva TLS** (sin `onReceivedSslError`); PII en el servidor; re-firmada |
| [Tesorería CDMX](REA-Tesoreria-CDMX/README.md) | `mx.wdd.sefin` | SAF (CDMX) | **Híbrida Capacitor/Angular** (backend Laravel) | **Credenciales de comercio Banorte + `client_secret` de Passport hardcodeados** en el bundle JS; cleartext a pagos; firma auténtica (`O=cweb`) |
| [Valida INE-QR](REA-Valida-INE-QR/README.md) | `mx.ine.rfe.lectorqr` | INE | Nativa Kotlin/Java + SDK nativo **Scandit** | **Validación 100% local** (sin permiso `INTERNET`, cero red); muro nativo para el QR; **firma auténtica del INE** — de las mejores posturas del acervo |
| [Va-y-Ven](REA-VaYVen/README.md) | `gob.yucatan.vayven` | ATY (Yucatán) | Nativa Kotlin/Java + WebView React (pago) · white-label **Vinden**/MRP | Cleartext HTTP en pagos; **sin TLS pinning**; Google API key en `.so`; credencial **TEST de MercadoPago** en el source map; re-firmada |
| [CONALITEG Digital](REA-CONALITEG/README.md) 1.25 | `gob.mx.conaliteg.bibliotecadigital` | CONALITEG (SEP) | Nativa Kotlin/Java (biblioteca digital) | **NSC permite cleartext HTTP** a la API de producción **y confía en CAs del usuario** (`src="user"` → MITM trivial); IP privada + subdominio `pruebasistemas` en producción; re-firmada |
| [Qrobús](REA-Qrobus/README.md) 12.7.9 | `mx.gob.iqt.qrobus` | IQT (Querétaro) | **Flutter** (Dart AOT) | Backend en **dominio de proveedor** (`admin.qrobici.com`, no gob.mx); pagos por **Stripe**; ubicación en segundo plano + NFC; re-firmada |
| [UNE / Bus Sonora](REA-UNE-Sonora/README.md) 5.7.2 | `com.bigbangdev.bussonora` | Sonora (UNE/IMTES) | **Flutter** (Dart AOT) | **Firma de dev individual** (`O=Bus Sonora`), no estatal; **AdServices** (atribución de publicidad) + Sentry + reCAPTCHA; biometría local |
| [CoDi Banxico](REA-CoDi-Banxico/README.md) | `org.banxico.dgspim.mobile.codi` | Banxico | Nativa Kotlin (R8) | **Mejor postura TLS del acervo:** certificate pinning correcto (validación de sistema + SHA-256 del cert hoja) + AndroidKeyStore con biometría obligatoria; firma auténtica de Banxico |
| [Bienvenido a México](REA-Bienvenido-Mexico/README.md) | `com.tcs.programapaisano` | SAT/ANAM (Paisano) | Nativa (TCS, legacy) | **Cleartext HTTP al SAT** + ambientes **dev/UAT/QA** embebidos (Azure dev, QA en `gear.host`); firma auténtica del SAT |
| [Gasoapp](REA-Gasoapp/README.md) | `cre.comercializacion.mobile` | CRE | **Xamarin.Forms** (.NET, 2020 EOL) | QA + dominio Azure crudo embebidos; subida de evidencia a Blob; TLS estándar sin bypass; firma auténtica de la CRE |
| [ProcurApp](REA-ProcurApp/README.md) | `mx.gob.pgjguanajuato.procurapp` | FGE Guanajuato | Nativa Kotlin/Java | **CRÍTICO: `UnsafeOkHttpClient` (TLS desactivado) en los clientes de producción, genética (ADN) y citas** (xref verificado) → MITM sobre denuncias, botón de pánico y perfil genético; firma auténtica de la FGE |
| [Identidad Digital BC](REA-Identidad-Digital-BC/README.md) | `com.gobbc.identidaddigital` | Baja California | Nativa Kotlin (MVP) | **`HttpLoggingInterceptor` BODY en producción** vuelca a logcat CURP, licencia, tarjeta de circulación, OTP y tokens; sin pinning (sin trust-all); re-firmada |
| [APPrende Jalisco](REA-APPrende-Jalisco/README.md) | `mx.gob.jalisco.edu.apprende` | SEJ (Educación Jalisco) | **Ionic/Angular + Cordova** | `ApiKey` hardcodeado + **proyecto Firebase de prueba** (`apprende-prueba`) en producción + RTDB para datos educativos (incl. salud); re-firmada |
| [CESPT](REA-CESPT/README.md) | `com.cespt.cesptApp` | CESPT (agua Tijuana) | **Capacitor/Ionic** | **NSC con cleartext GLOBAL + confianza en CAs del usuario** → MITM trivial; pago delegado a BBVA Bancomer (sin tarjeta en la app); Google Maps key embebida |
| [Licencia Digital Jalisco](REA-Licencia-Jalisco/README.md) | `mx.com.cosmocolor.movapp.jal` | Transporte Jalisco | Nativa Kotlin (SQLCipher) | **CRÍTICO: replica IDÉNTICO el defecto TLS de EDOMEX** (mismo `ServiceGenerator` de Cosmocolor, `checkServerTrusted` vacío + `hostnameVerifier=true`) → **defecto de producto multi-estado**; re-firmada |
| [JMAS Móvil](REA-JMAS/README.md) | `mx.gob.jmasmovil` | JMAS (agua Chihuahua) | Nativa Kotlin (Compose/Hilt) | De las mejor construidas; pago delegado a BBVA; **TLS estándar sin trust-all ni cleartext**; JWT en SharedPreferences planas (menor); re-firmada |
| [Japay](REA-Japay/README.md) | `mx.gob.yucatan.japay.japaymovil` | JAPAY (agua Yucatán) | **Cordova** | **Guarda el PAN en el servidor sin cifrado de cliente** (preocupación PCI); 3DS en gateway tercero `egbs5`; sin pinning pero sin cleartext; re-firmada |
| [Guía Pemex](REA-Guia-Pemex/README.md) | `com.pemex.guiapemex` | Pemex | **Flutter** (Dart AOT) | **`.env` filtra la API key del backend propio de Pemex** (`APP_SERVICES_API_KEY`) + llaves de Google Maps y TomTom en claro; firma auténtica de Pemex |
| [FiREL PJF](REA-FiREL-PJF/README.md) | `gob.mx.android.firel` | CJF (Poder Judicial) | Nativa Kotlin/MVVM | **White-label de una app de "Colegio de Notarios"** (FielNet/IQSEC, backend fuera de gob.mx); enrolamiento biométrico extremo (INE+rostro+huella+firma+video) a 3 terceros (uno extranjero); TLS contradictorio (a confirmar); re-firmada |
| [IPN Oficial](REA-IPN/README.md) | `com.ipnmx.AppIPN` | IPN | **Flutter** (Dart AOT) | Keycloak+Azure AD; backend maneja **credencial del alumno, NSS y pagos Banco Azteca**; **NSC pinnea `www.ipn.mx` pero NO `auth.dsi.ipn.mx`** (el canal sensible); white-label de MyCampus Digital; re-firmada |
| [Mi Aguah](REA-Mi-Aguah/README.md) | `mx.gob.aguadehermosillo.miaguah` | Agua de Hermosillo | **Flutter** (Dart AOT) | Captura de tarjeta in-app sin SDK/claves de pasarela visibles (muro Dart); AdServices; backend `agua.gob.mx` HTTPS; re-firmada |
| [Aguascalientes Digital](REA-Aguascalientes-Digital/README.md) | `mx.gob.ags.refrendo` | GEA (Aguascalientes) | Nativa Kotlin (Hilt/Room) | Super-app con pagos tokenizados (ePagos/CitiBanamex) y TLS no desactivado, pero **NSC permite cleartext a producción incl. el gateway de pagos y C5 emergencias**; `HttpLogging` BODY a logcat; firma auténtica del GEA |
| [Directorio INEGI (DENUE)](REA-Directorio-INEGI/README.md) | `mx.org.inegi.denuemv` | INEGI | Nativa Kotlin (R8) | Datos públicos (DENUE), bajo riesgo; Google Maps key embebida (riesgo de robo de cuota) + ads/analytics con `AD_ID`; TLS OK; firma auténtica del INEGI |
| [Lotería de México](REA-Loteria-Mexico/README.md) | `loterianacional.mx` | Lotenal | Nativa (GemesGroup, ES) | **White-label multi-país** (trae endpoints de ONCE España + Santa Casa Portugal); **resultados por HTTP desde un tercero** (`nacionalloteria.com`) → MITM altera el número ganador; chequeo de versión por Dropbox público |
| [CFE Proveedores](REA-CFE-Proveedores/README.md) | `mx.com.cfe.proveedores` | CFE (B2G) | **Xamarin.Forms** (.NET) | Codebase interno **"DemoCFE"** ("Demo") en producción; `usesCleartextTraffic` + **2 IPs internas de la CFE** en el NSC (fuga de topología); TLS estándar sin bypass; portal de proveedores |
| [Seguridad Inmediata](REA-Seguridad-Inmediata/README.md) | `com.alertacontigo.mx` | Ayto. Puebla (pánico) | **.NET MAUI AOT + PairIP** (doble muro) | Botón de pánico: payload/backend no recuperables (AOT nativo + PairIP); **ubicación a OneSignal** (tercero) + **`usesCleartextTraffic` global** (a confirmar) + permisos extremos (`CALL_PRIVILEGED`); re-firmada |

> **Alcance y ética.** Mismo principio que el inventario: observación de artefactos públicos obtenidos de
> tiendas oficiales, sin atacar infraestructura en vivo ni usar credenciales ajenas. El desempacado de
> binarios endurecidos (Veritran, packer Baidu), el volcado de snapshots Dart cifrados y los cuerpos de
> endpoints autenticados quedan fuera de alcance cuando requieren root. Cada hallazgo de seguridad es
> responsable y está fechado; el propósito es documentar la postura de seguridad del software público.

## Créditos y fuentes

- Subdominios `.gob.mx`: Certificate Transparency (crt.sh) + [dotgob-mx](https://github.com/AlejandroTC/dotgob-mx)
  de Alejandro TC (licencia MIT), de donde se integró su lista y su marca de actividad.
- Trámites: Catálogo Nacional de Regulaciones, Trámites y Servicios (CONAMER), CSV público diario
  `catalogonacional.gob.mx/sujetosobligados/TramitesPublicadosCSV/`; trámites federales con nivel de
  digitalización: dataset abierto de la SFP (datos.gob.mx).
- Métricas de apps: Google Play y App Store (MX). Permisos: Google Play.

### Por qué así

- **Tres ejes paralelos** (`apps/`, `sitios/`, `servicios/`) con la **misma segmentación**
  (federal / autónomos-paraestatales / estatal A-B-C / CDMX / municipal) para poder cruzarlos:
  "este trámite existe en web pero no en app", "esta dependencia tiene app pero el sitio está caído".
- **Cada afirmación sobre un portal lleva evidencia fechada** (`estado_real`): fecha + URL + resultado.
- **El análisis vive aparte de los datos** (`analisis/` vs `apps|sitios|servicios`): los JSON son
  la fuente reutilizable; los `.md` son la lectura.

## Relación con Skills_MX

[Skills_MX](https://github.com/elimorals/Skills_MX) ya tiene **83 MCP servers / 417 tools** que
operan contra muchos de estos portales (SAT, IMSS, CURP/RENAPO, Banxico, predial municipal, DOF…),
cada uno con su sección "Estado real" de qué se validó en vivo. Este repo es el **mapa**; Skills_MX
es la **interacción**. Por cada portal documentado aquí se anota si Skills_MX ya tiene un MCP para él,
para no duplicar y priorizar los que faltan.

## Licencia

MIT. Autor: Elias Rashid Morales Mendoza ([@elimorals](https://github.com/elimorals)).
