# Extracción profunda: código Dart, decode completo y artefactos

Este documento recoge el segundo pase, en el que **toda la decodificación se materializó dentro de
este directorio** (antes solo vivía en el SSD) y se exprimió el resto del bundle. Lo nuevo frente a
los docs 00–09: el **árbol de código Dart propio de la app** (recuperado de `libapp.so`), el decode
apktool **con smali**, los `flutter_assets` completos, los `.so`, y varias correcciones de precisión.

## Qué se materializó en el directorio

| Ruta (local, en `.gitignore`) | Contenido | Tamaño |
|---|---|---|
| `res-decoded/base/` | apktool `d` completo: `res/`, **`smali/` + `smali_classes2/`**, `AndroidManifest.xml`, `apktool.yml`, `assets/`, `original/`, `unknown/` | ~60 MB |
| `flutter_assets/` | Assets Flutter completos (fuentes, SVG/PNG, `.env.*`, `AssetManifest.bin`, `FontManifest.json`, `NativeAssetsManifest.json`, `shaders/`, `packages/`) | ~3.7 MB |
| `libs/arm64-v8a/` | Los 7 `.so` (libapp, libflutter, libtensorflowlite_c, libdartjni, …) | ~26 MB |
| `analisis/dart-tree-sat_movil_2.txt` | **129 rutas `.dart` propias** recuperadas de `libapp.so` | — |
| `NOTICES.txt` | SBOM descomprimido (**34,697 líneas**) — versionable (texto de licencias OSS) | 1.5 MB |

Los binarios/propietarios siguen **sin versionarse** (`.gitignore`); los `.md`, `NOTICES.txt`,
`AndroidManifest.xml`, `strings.json`, `hashes.sha256`, `xapk-manifest.json` y `decompiled/` sí.

## Árbol de código Dart propio (`package:sat_movil_2`) — 129 archivos

Reconstruido de literales en `libapp.so` (Dart AOT deja las rutas de los `.dart` en claro). No es
el código fuente, pero **sí el mapa exacto de módulos** de la app.

```
lib/
├── main.dart · routes.dart · animation_start.dart
├── pages/            (68)
│   ├── home/                     (30)  ← pantallas de servicios autenticados
│   ├── sidebar/                  (18)  ← menú lateral / secciones
│   ├── password_process_flow/    (17)  ← SAT ID: obtención/recuperación de contraseña
│   │   └── utils/ document.dart · quote.dart · solicitant_information.dart
│   ├── login/                    (2)
│   └── UrlPdfViewerPage.dart
├── services/         (34)        ← ver tabla abajo
└── utils/            (24)        ← biometric, qr_scan_page, dialogs, pantone, user_mode, íconos SAT…
```

### `services/` — el inventario funcional real (34)

| Servicio | Función fiscal |
|---|---|
| `login_service` | Login OAuth2 ROPC (doc 03) |
| `identification_service` | **SAT ID** / identificación |
| `certificates_service` | **Certificados de e.firma** (doc 05) |
| `information_from_csf_service` | **Constancia de Situación Fiscal (CSF)** |
| `regime_service` | **Régimen fiscal** del contribuyente |
| `obligations_service` | **Obligaciones** fiscales |
| `economical_activity_service` | **Actividad económica** |
| `stamps_service` | **Sellos** (digitales / CFDI) |
| `address_service` | **Domicilio** fiscal |
| `userInfoService` | Datos del usuario |
| `message_service` | **Buzón / mensajes** |
| `documents_service` | Documentos (acuses/PDF) |
| `contact_service` | Medios de contacto |
| `events_service` | Eventos / calendario fiscal (lee `/satmovil/v1/events/`) |
| `push_notification_service` | **Notificaciones locales** (sin FCM, ver abajo) |
| `local_auth_service` | Biometría de desbloqueo |
| `shared_preferences_service` | Preferencias |
| `tutorials_service`, `about_of_service`, `other_apps_service` | Tutoriales, "acerca de", otras apps gob |
| `notifications/` (14 archivos) | Un *notification mapper* por dominio: address, calendar, certificates, contact, economical_activity, events, **mailbox** (buzón), message, obligations, regime, stamps, identification + `notifications_counter` |

El subsistema `notifications/` es un espejo 1:1 de los dominios: la app sincroniza cambios por
dominio y levanta una notificación local por cada uno (nuevo mensaje en buzón, obligación próxima,
sello emitido, cambio de domicilio, etc.).

### `pages/password_process_flow/` (17) — el flujo de identidad

Sus utils (`document.dart`, `quote.dart` = cita, `solicitant_information.dart`) confirman un flujo
guiado de **recuperación/obtención de la contraseña (CIEC)** que pide documento de identidad, datos
del solicitante y agenda/valida una cita — alineado con SAT ID (doc 03, 06).

## Correcciones de precisión respecto a los docs 00–09

1. **FileProviders (doc 01): bien acotados, NO mala práctica.** Los `res/xml` reales son:
   - `flutter_image_picker_file_paths.xml` → `<cache-path path="." />`
   - `flutter_printing_file_paths.xml` → `<cache-path path="/share/" />`
   - `flutter_share_file_paths.xml` → `<cache-path path="share_plus/" />`
   - `provider_paths.xml` → `<external-path path="." />`
   Todos apuntan a **cache/external acotado**, no a `files-path "/"`. A diferencia de CFE y
   jw-library, aquí **no** se expone la raíz de `files/`. Punto a favor de SAT Móvil.
2. **TensorFlow Lite (doc 06): capacidad presente, uso NO evidenciado.** `libtensorflowlite_c.so`
   es TFLite real (símbolos `kTfLite*` confirmados), pero **no hay modelo `.tflite` en el bundle**,
   **no hay binding Dart** (`tflite_flutter`/`google_mlkit` no están en NOTICES ni en `libapp.so`)
   y **no hay cadenas de liveness/face**. Lo único ML-adyacente en Dart es `camera`/`camerax` y
   `jni`. → No se puede afirmar liveness. Hipótesis (sin confirmar): el modelo se descarga en
   runtime, o la `.so` entra como dependencia transitiva. **Se retira la afirmación de "liveness".**
3. **Notificaciones (doc 07): locales, confirmado, sin Firebase.** `package:sat_movil_2/services/
   push_notification_service.dart` + `notifications/*` + `flutter_local_notifications` +
   `AndroidNotificationChannel`, alimentadas por `events_service` desde `/satmovil/v1/events/`.
   **No hay FCM/Firebase.** El "push" es realmente *sync + notificación local* vía el servicio en
   background.
4. **SBOM (doc 07): el real son 34,697 líneas** de `NOTICES.txt` (la cifra "3,655" era una
   estimación de grep del primer pase).
5. **Plugins (doc 00): solo 5 vía registrant clásico.** `GeneratedPluginRegistrant` registra
   FlutterLocalNotifications, Jni, JniFlutter, flutter_inappwebview y flutter_background_service.
   Los demás (image_picker, share_plus, printing, camera, secure_storage, local_auth, url_launcher)
   usan **auto-registro del embedding v2**. No cambia qué hace la app, solo cómo se cablean.
6. **MethodChannel nativo = `com.sat_movil/utils`** (campo `u` de `MainActivity`, de smali). Único
   canal plataforma; el resto es Dart puro.

## Lo que queda fuera del estático (honesto)

- **Cuerpos de función Dart**: `libapp.so` es AOT ARM64; se recuperan rutas, nombres y literales,
  no el flujo. Para el detalle de cada request (headers, payloads, orden) hace falta Frida sobre
  `libapp.so` + captura de red en dispositivo propio.
- **Origen del modelo TFLite** (descarga vs embebido vs transitivo) — requiere dinámica.
- **Verificación de firma v2/v3** del APK — requiere `apksigner` (no instalado), ver doc 09.
