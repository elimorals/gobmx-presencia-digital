# SDKs y dependencias (SBOM)

`flutter_assets/NOTICES.Z` (gzip) agrega los avisos de licencia de todo lo enlazado. Descomprimido
(`NOTICES.txt`, 2º pase) son **34,697 líneas / 1.5 MB**. Incluye dependencias C/C++ del motor
(boringssl, angle, dawn, expat, brotli, icu…) y los paquetes Dart. Abajo, lo relevante para
seguridad/funcionalidad.

## Paquetes Dart propios y de app (de `libapp.so`, por nº de referencias)

| Paquete | Refs | Rol |
|---|---|---|
| `package:flutter/` | 445 | Framework |
| `package:pointycastle/` | 157 | **Cripto** (AES/RSA) — doc 05 |
| `package:sat_movil_2/` | 135 | **Código propio del SAT** (app) |
| `package:syncfusion_flutter_pdf/` | 80 | Generación de PDF (acuses/CFDI) |
| `package:xml/` + `petitparser` + `html`/`csslib` | 66 + 54 + 20 | Parseo XML/HTML (respuestas ASP.NET/JSF) |
| `package:get/` | 25 | Estado/navegación (**GetX**) |
| `package:dio/` + `http` | 24 + 14 | **Clientes HTTP** — doc 04 |
| `package:youtube_player_iframe/` | 23 | Videos educativos |
| `package:pdfx/` + `printing` | 17 + 6 | Render/impresión de PDF |
| `package:infinite_scroll_pagination/` | 17 | Listas paginadas |
| `package:jni/` | 14 | **Puente Dart↔JNI** (`libdartjni.so`) |
| `package:rxdart/` | 11 | Reactivo |
| `package:flutter_local_notifications/` | 11 | Notificaciones locales |
| `package:crypto/` | 10 | Hash/HMAC |
| `package:flutter_secure_storage/` | 8 | Tokens/e.firma (Keystore) |
| `package:timezone/`, `qr_code_scanner/` | 7 | Zona horaria, escáner QR |
| `package:uuid/`, `sqflite_common/`, `permission_handler_platform_interface/`, `local_auth_platform_interface/` | 6 | IDs, SQLite, permisos, biometría |
| `camera_android_camerax`, `image_picker`, `webview_flutter_android` | 5 | Cámara, selector, WebView |

## Otros paquetes notables (del SBOM NOTICES)

`encrypt`, `asn1lib`, `base32` (cripto/e.firma), `email_validator`, `cached_network_image`,
`barcode`, `carousel_slider`, `auto_size_text`, `another_flushbar`, `dots_indicator`,
`circular_profile_avatar`, `app_settings`, `appcheck`, `device_apps`, `external_app_launcher`,
`fallback_root_certificates`, `archive`, `intl`, `permission_handler`.

- `device_apps` / `external_app_launcher` / `appcheck`: detectar y abrir otras apps instaladas
  (p. ej. abrir el navegador o verificar apps). `appcheck` aquí es el paquete de *comprobación de
  apps instaladas*, no necesariamente Firebase App Check.
- `fallback_root_certificates`: bundle de CA raíz adicional (hipótesis en doc 04: entornos por IP).

## Plugins Android (del manifiesto/DEX)

`flutter_background_service`, `flutter_inappwebview_android`, `image_picker`, `url_launcher`,
`share_plus`, `printing`, `flutter_local_notifications`, `camerax`, `zxing (CaptureActivity)`,
`workmanager`, `room`, `profileinstaller`, `startup`.

## Lo que NO está (y es significativo)

- **Sin Firebase Analytics / Crashlytics / FCM** (contraste con CFE, cargado de Firebase + Huawei
  HMS). No hay `firebase_*` en NOTICES ni en `libapp.so`, ni servicios FCM en el manifiesto.
  **Confirmado (doc 10):** las notificaciones son **locales** — `push_notification_service.dart` +
  `flutter_local_notifications` + `events_service` leyendo `/satmovil/v1/events/`. Es *sync +
  notificación local* vía el servicio en background, no push remoto tipo FCM.
- **Sin anti-fraude comercial** tipo ThreatMetrix (que sí trae CFE).
- **Sin Huawei HMS** — distribución solo Google Play (el `stamp` lo confirma, doc 09).

La app depende sobre todo de **paquetes Dart open-source de pub.dev** y de un backend propio del
SAT, con mínima telemetría de terceros. Es un stack más "limpio" de terceros que CFE, a cambio de
un endurecimiento de cliente más débil.
