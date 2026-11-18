# Resumen — Pemex ASISTE 3.1.19 (`com.pemex.asiste`)

## Identidad

- **App:** Pemex ASISTE ("Asiste") — Petróleos Mexicanos. Servicios al usuario (localizador de
  estaciones/servicios, contenido en video, notificaciones).
- **Package:** `com.pemex.asiste` · **versión** 3.1.19 (versionCode 730). compileSdk 35.
- **#27** del inventario por instalaciones (≈500 K).
- **Firma:** re-firmada por APKPure (`CN=Android, O=Google Inc.`) → no auténtica de Pemex.

## Pila

**Flutter.** `assets/flutter_assets/` presente (AssetManifest, FontManifest, imágenes
`t_bienvenida`, `.env`) y `libapp.so` + `libflutter.so` en el split `config.armeabi_v7a`. La
lógica de negocio es un **snapshot Dart AOT** (`libapp.so`) → **muro** (como SAT Móvil, Urbani,
Mi Policía). El DEX es el embedding de Flutter; trae **ExoPlayer** (video).

> Corrección de triage: el triage preliminar la marcó "nativa"; **es Flutter**. La causa es la
> misma de siempre: el `libflutter.so`/`libapp.so` viven en el split de arquitectura, no en el
> APK base que lee el triage.

## Lo observable (sin ejecutar)

`assets/flutter_assets/.env` (en claro, `flutter_dotenv`):
```
APP_SERVICES_ENDPOINT=https://api-cloud.pemex.com/asiste
APPSINSIGHTS_INSTRUMENTATION_KEY=<clave Azure App Insights>   # valor en evidence/, no versionado
```

- **Backend:** `https://api-cloud.pemex.com/asiste` (HTTPS, dominio oficial de Pemex).
- **Telemetría:** Azure Application Insights (la *instrumentation key* embebida).

## Postura (balance)

| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS al dominio oficial `api-cloud.pemex.com` |
| Permisos | ✅ Acotados (ubicación + notificaciones; sin cámara/contactos/SMS) |
| Secretos | ⚠️ `.env` en claro con clave de telemetría (baja sensibilidad) |
| Lógica | 🧱 Muro Dart AOT (`libapp.so`) — no decompilable estáticamente |
| Firma | ⚠️ Re-firmada por APKPure |

## Lo que NO se puede (el muro)

Rutas completas de API bajo `/asiste`, auth, manejo de ubicación y de datos → en el snapshot
Dart, no observable estáticamente. Vía: reflutter/blutter o Frida (análisis dinámico, fuera de
alcance). Documentado como límite, sin inventar.
