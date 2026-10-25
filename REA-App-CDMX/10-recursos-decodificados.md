# 10 — Recursos decodificados a profundidad (apktool)

Decodificación completa del APK base con **apktool 3.0.3** (`decoded_file_count: 3789`,
`coverage: partial` por refs no resueltas, normales). Copias de los XML legibles en
`res-decoded/xml/` (no versionado). Esta es la capa que REA no persiste.

## FileProvider — alcance (corrige el pendiente del doc 01)

Hay **dos** configuraciones de rutas. **Ambas están correctamente acotadas** — a diferencia
de jw-library y CFE Contigo, que exponían la raíz `/`:

`res/xml/file_paths.xml` (authority `…apps.fileprovider`):
```xml
<paths>
    <external-files-path name="pictures"  path="Pictures/" />
    <external-files-path name="downloads" path="Download/" />
    <external-files-path name="images"    path="images/" />
</paths>
```

`res/xml/image_share_filepaths.xml`:
```xml
<paths>
    <files-path name="image_provider_images" path="image_provider/" />
</paths>
```

Solo subdirectorios concretos de `getExternalFilesDir()` y de `files/`. **No hay
`files-path "/"` ni `external-path "."`.** Es el patrón correcto: App CDMX **no** repite
la mala práctica de FileProvider vista en las otras dos apps del pipeline.

## HCE Calypso — `res/xml/apduservice.xml`

```xml
<host-apdu-service
    android:description="@string/descriptionSurpassAPDU"
    android:requireDeviceUnlock="false"
    android:requireDeviceScreenOn="false">
    <aid-group android:description="@string/descriptionSurpassAPDU" android:category="other" />
</host-apdu-service>
```

- `aid-group` **sin `<aid-filter>` estáticos** → los AID de la tarjeta Calypso se registran
  **dinámicamente** en runtime (`CardEmulation.registerAidsForService`), coherente con el
  multi-app Spirtech (doc 05).
- `requireDeviceUnlock="false"` + `requireDeviceScreenOn="false"`: la tarjeta responde al
  validador **sin desbloquear ni encender la pantalla** (UX de transporte tap-and-go). Es
  el comportamiento esperado para transporte, pero conviene tenerlo en el modelo de amenazas:
  la emulación opera con el teléfono bloqueado.

## Assets — tres familias

1. **Blobs cifrados de PairIP (22).** Nombres aleatorios de 16 caracteres
   (`HjwJEpYRbK51T2gR`, `dh99tajJh4XMSx7l`, …), tipo `data`, ~147–150 KB cada uno. Son el
   **código protegido que `libpairipcore.so` descifra/ejecuta en runtime** — la firma
   característica de PairIP (doc 07). No son analizables por estática.
2. **WebView de Movilidad Integrada (`assets/web/`).** App web embebida para el mapa de
   rutas: `index.html` (con un SVG gigante `MI_mapa-v07ene2025`, el mapa del Metro/MI con
   fecha 7-ene-2025), `main.js`, `main_rutas.js`, `mis_rutas.js`, `mis_segmentos.js`,
   `mis_intersecciones.js`, `general_intersections.js`, `svg_support.js`, jQuery 3.4.1 slim,
   `styles.css`, y fuentes `MOVILIDAD_INTEGRADA_CDMX.{otf,TTF}`, `METRO-DF.TTF`.
3. **Infra de red:** `assets/ca.cert.der` (cert pinning, doc 07),
   `assets/PublicSuffixDatabase.list` (OkHttp).

## `res/raw` — datasets de transporte offline

La app embarca datos geográficos de transporte (se renderizan en mapa sin red):

| Dataset | Contenido |
|---|---|
| `metro_dataset.json`, `metrobus_dataset.json`, `mexibus_dataset.json` | Líneas Metro / Metrobús / Mexibús |
| `cablebus_dataset.json`, `mexicable_dataset.json`, `trolebus_dataset.json`, `tiu_dataset.json` | Cablebús, Mexicable, Trolebús, TIU |
| `ciclovia_dataset.json`, `intersecciones.json` | Ciclovías e intersecciones |
| `mexico_city_geojson.json` | GeoJSON de la CDMX |
| `dataset_cuadrantes.json`, `dataset_taxistand.json`, `dataset_wifi.json`, `dataset_vehiculo.json` | Cuadrantes de policía, sitios de taxi, puntos WiFi, catálogo de vehículos |
| estilos de mapa | `style_map_{day,night}.json`, `style_map_movilidad_integrada{,_night}.json`, `back_style_map_movilidad.json` |

Más ~20 animaciones Lottie (`*_lottie*.json`: NFC, pánico, taxi, carga, splash, receta),
sonidos (`in/out/ini/fin.mp3`), `mi_card_video_b.mp4` y
`firebase_{common,crashlytics}_keep.xml`.

## Strings y configuración de red

- `res/values/strings.xml`: **2,842 strings** (vs. 258 de CFE) — refleja las ~24 UIs.
- **No hay `networkSecurityConfig`** en el manifiesto ni en `res/xml/`. El pinning se hace
  por código (OkHttp `CertificatePinner` + `ca.cert.der`), no por configuración declarativa.
- `res/xml/locales_config.xml` enumera los 18 idiomas (per-app language, Android 13+).
- `res/xml/splits0.xml`: metadatos de integridad de splits (PairIP / Play Core).
