# 05 — SDKs y plugins Flutter

Recuperados de `libapp.so` por los prefijos `package:<nombre>/` (cadenas del snapshot). Son las
dependencias Dart; su lado Android está en el DEX como glue.

## Plugins que definen la funcionalidad

| Plugin | Función en la app |
|---|---|
| `background_location`, `geolocator` (+ `_android`, `_platform_interface`) | Ubicación, incluida en **segundo plano** (correlaciona con `ACCESS_BACKGROUND_LOCATION`, doc 01) |
| `google_maps_flutter` (+ `_android`) | Mapa de cuadrantes; HERE Maps también referenciado (doc 03) |
| `flutter_phone_direct_caller` | **Llamada directa** (marca sin pasar por el dialer) — contacto de emergencia (`CALL_PHONE`) |
| `flutter_barcode_scanner` | Escaneo de códigos (`CAMERA`) |
| `flutter_inappwebview` | WebView embebido (contenido/trámites dentro de la app) — superficie de puente JS↔Dart |
| `http` | Cliente HTTP de las APIs de la SSC (doc 03) |
| `connectivity` (+ `_platform_interface`) | Estado de red |
| `google_api_availability` | Disponibilidad de Google Play Services |
| `share_plus` (provider `ShareFileProvider`, doc 01) | Compartir contenido |

## UI / utilidades

`auto_size_text`, `cached_network_image` (+ `flutter_cache_manager`), `carousel_slider`, `chewie`
(video), `custom_text`, `empty_widget`, `flutter_html` + `csslib`/`html` (render de HTML),
`flutter_form_builder` + `form_builder_validators`, `flutter_slidable`, `font_awesome_flutter`,
`intro_slider`, `intl`, `maps_launcher`, `carousel_slider`.

## Lectura

El conjunto es coherente con una app de servicios ciudadanos de seguridad: ubicación (incl. segundo
plano), mapa de cuadrantes, llamada directa de emergencia, escaneo, contenido HTML/WebView y
formularios. No se observaron SDKs de analítica/publicidad de terceros evidentes en las cadenas (ni
Firebase Analytics ni trackers comerciales claros), aunque la confirmación fina depende del snapshot
Dart (doc 08).
