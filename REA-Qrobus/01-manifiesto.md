# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 12.7.9-release, compileSdk 36.

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `CAMERA` | escaneo de QR (tarjeta/validación de viaje) |
| `NFC` | tarjeta de transporte sin contacto |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (paradas/unidades cercanas) |
| `ACCESS_BACKGROUND_LOCATION` | **ubicación en segundo plano** (seguimiento de viaje) |
| `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_LOCATION` | servicio de ubicación en primer plano |
| `READ_CONTACTS` | ¿compartir viaje / transferir saldo a contacto? (inferido) |
| `READ_EXTERNAL_STORAGE`, `INTERNET`, `POST_NOTIFICATIONS`, `VIBRATE`, `WAKE_LOCK` | estándar |

**Lectura:** conjunto **amplio**. Lo más sensible es `ACCESS_BACKGROUND_LOCATION` (rastreo en
background) combinado con el servicio de ubicación en primer plano — coherente con seguimiento de
viaje en tiempo real, pero es un permiso de alto impacto para privacidad. `READ_CONTACTS` no
tiene un uso evidente desde el manifiesto (la lógica está en el snapshot Dart); se declara como
permiso solicitado, no como uso verificado.

## Flags

- Sin `usesCleartextTraffic` en el manifiesto → por defecto HTTP en claro bloqueado (consistente
  con backend/Stripe HTTPS).
- Runtime Flutter (FlutterActivity/engine); Firebase (FCM) para notificaciones.
