# Arquitectura Flutter / Dart AOT

La decisión de arquitectura que domina todo lo demás: **SAT Móvil está escrita en Flutter**, no
en Android nativo. Esto cambia dónde vive la información y cómo se analiza.

## Por qué es Flutter (evidencia)

- Manifiesto: `<meta-data android:name="flutterEmbedding" android:value="2"/>`, `MainActivity`
  con `io.flutter.embedding.android.NormalTheme` y `SplashScreenDrawable`.
- `flutter_assets/` completo en el APK base (`AssetManifest.bin`, `FontManifest.json`,
  `NOTICES.Z`, `shaders/`, `packages/`).
- `MainActivity` decompilada (ver `decompiled/MainActivity.a.java`) es un `FlutterActivity`:
  solo registra plugins y un MethodChannel. Toda la lógica es Dart.
- `res/values/strings.xml` no tiene **ni un** string de negocio (ver `strings.json`): en Flutter
  los textos viven en el código Dart y los assets.

## Qué hay en cada librería nativa (`config.arm64_v8a.apk`)

| `.so` | Tamaño | Rol |
|---|---|---|
| `libapp.so` | 11,666,352 | **Snapshot Dart AOT** — TODA la app del SAT compilada a ARM64 |
| `libflutter.so` | 11,107,920 | Motor Flutter (render Skia/Impeller, Dart VM). Cadena: **Dart VM 3.10.0 stable (2025-11-06)** |
| `libtensorflowlite_c.so` | 4,733,864 | **TensorFlow Lite** nativo — inferencia on-device (liveness SAT ID, doc 06) |
| `libdartjni.so` | 131,248 | **jnigen** — puente Dart ↔ Java/Android (`package:jni`) |
| `libimage_processing_util_jni.so` | 32,528 | Utilidades de imagen de CameraX |
| `libsurface_util_jni.so` | 4,896 | Utilidades de surface de CameraX |
| `libdatastore_shared_counter.so` | 7,112 | Jetpack **DataStore** (contador compartido entre procesos) |

## Qué hay en `flutter_assets/` (APK base)

| Asset | Contenido |
|---|---|
| `.env.production` / `.env.uat` / `.env.dev` | **Configuración de red en texto plano** (doc 04) |
| `assets/commit.txt` | `521065f7` — commit del repositorio que generó el build |
| `AssetManifest.bin` | Índice binario de assets (5,160 bytes) |
| `FontManifest.json` | Fuentes: `geomanist`, iconos `SAT.ttf`, `indicators.ttf`, familia `fluttericon` |
| `NOTICES.Z` | Avisos de licencia (gzip) → **SBOM**, 3,655 secciones (doc 07) |
| `NativeAssetsManifest.json` | Vacío (`{"native-assets":{}}`) |
| `assets/images/*.svg`, `*.png` | Logos SAT, `tapiz.svg`, `resources0..22.svg` |
| `packages/flutter_inappwebview/...` | Recursos del WebView (incl. juego t-rex offline) |
| `packages/youtube_player_iframe/assets/player.html` | Reproductor de YouTube embebido |
| `shaders/` | Shaders del motor |

## Patrón de arquitectura de la app (inferido de los paquetes Dart)

- **Estado:** GetX (`package:get`) + `rxdart` → controllers/bindings, navegación por `Get.to`.
- **Red:** `dio` (cliente principal, interceptores) + `http` (secundario). Config por
  `flutter_dotenv` (`DotEnv`) leyendo `.env.production`.
- **Persistencia:** `flutter_secure_storage` (tokens, e.firma) + `sqflite` (datos locales) +
  DataStore (contador/flags).
- **Serialización:** `xml` + `petitparser` + `html`/`csslib` → parseo de respuestas XML/HTML
  (varios servicios del SAT devuelven ASP.NET/JSF; ver `__VIEWSTATE` en strings y doc 08).
- **PDF:** `syncfusion_flutter_pdf` (genera), `pdfx` (renderiza), `printing` (imprime/comparte).

## Consecuencia para el análisis

**Lo que se recupera en estático:** endpoints, nombres de paquetes/clases Dart, textos, rutas de
API, constantes cripto y de assets — todo por `strings` sobre `libapp.so`, porque Dart AOT deja
los literales en claro y **no hay ofuscación de strings** (contraste total con el DexGuard de CFE).

**Lo que NO se recupera en estático:** el flujo de control, los cuerpos de función y el orden de
llamadas. Dart AOT compila a código máquina ARM64 sin bytecode intermedio legible; no hay
decompilador Dart AOT maduro. Para trazar lógica real haría falta Frida/instrumentación sobre
`libapp.so` en un dispositivo propio — fuera del alcance estático de este trabajo.
