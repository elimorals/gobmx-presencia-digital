# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 1.0.48, compileSdk 33.

## Permisos

| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red (webservice + pago) |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (centros/oficinas cercanas) |
| `WRITE_EXTERNAL_STORAGE` | guardar comprobantes/archivos (cordova-plugin-file) |
| `POST_NOTIFICATIONS`, `WAKE_LOCK` | notificaciones / UX |
| (cámara) | vía `cordova-plugin-camera` (lectura de recibo/foto) |

Conjunto **acotado** para una app de pago de servicios. No pide contactos, SMS, teléfono ni
ubicación en segundo plano.

## Flags

- **Sin `usesCleartextTraffic`** en el manifiesto → HTTP en claro bloqueado por defecto
  (consistente con el backend HTTPS, doc 04).
- Sin `networkSecurityConfig` por recurso → política por defecto (CAs del sistema). Sin pinning
  declarado.

## Componentes

- Cordova: `MainActivity` + `InAppBrowser` (para el flujo de pago 3DS, doc 03).
- WebView de Cordova que carga el bundle local `assets/www/index.html`.
