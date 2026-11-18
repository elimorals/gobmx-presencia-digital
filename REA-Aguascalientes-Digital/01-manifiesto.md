# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 26.5.5, compileSdk 36.

## Permisos sensibles (coherentes con una super-app)

| Permiso | Para qué (módulo) |
|---|---|
| `ACCESS_FINE_LOCATION` / `COARSE` / `FOREGROUND_SERVICE_LOCATION` | emergencia (`LocationService`), mapa |
| screening/lectura de llamadas (`READ_CALL_LOG`/`READ_PHONE_STATE`/CallScreeningService) | **anti-extorsión** (`MyCallScreeningService`, `CallReceiver`) — detecta/filtra llamadas |
| `CAMERA` | captura de documentos, QR, tarjeta de soluciones |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | push (Firebase) |
| `INTERNET`, `ACCESS_NETWORK_STATE`, red/estado | 15 clientes Retrofit |

El módulo **extorsión** registra un `CallScreeningService`/`CallReceiver`: la app **inspecciona
llamadas entrantes** (para alertar números de extorsión). Es funcionalidad declarada del
servicio, pero es una superficie de privacidad notable (acceso a metadatos de llamadas). El
módulo **emergencia** corre un `LocationService` en primer plano (ubicación en tiempo real).

> El detalle completo de permisos está en `AndroidManifest.xml`. La app declara un
> `networkSecurityConfig` por recurso — ahí está la debilidad de red (doc 03).

## Componentes

- `SplashActivity` → `LoginActivity`/`MainActivity` (Hilt). Múltiples Activities por módulo
  (Licencia, Tarjeta, Mapa, Chat/WebSocket, Extorsión, WebView, NativeStream).
- `ServicioMensajesFirebase` (FCM), `LocationService`, `MyCallScreeningService`,
  `RytaPlaybackService` (streaming).

## Flags

- `android:networkSecurityConfig="@xml/network_security_config"` → **el punto clave** (doc 03):
  base sin cleartext, pero un `domain-config` que lo **habilita** para producción.
