# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 3.6.3, compileSdk 35.

## Permisos
| Permiso | Para qué |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | localizar estaciones cercanas (función central) |
| `INTERNET` | consulta al backend + mapas |
| `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | patrón AndroidX |

Conjunto **mínimo**: solo ubicación + red. No pide cámara, micrófono, contactos, SMS,
teléfono ni ubicación en segundo plano. Higiene de permisos **buena** (coherente con un
localizador de estaciones).

## Flags
- Sin `usesCleartextTraffic` en el manifiesto → HTTP en claro bloqueado por defecto (consistente
  con backend/mapas HTTPS).
- Runtime Flutter (FlutterActivity/engine). Sin `networkSecurityConfig` por recurso → política
  por defecto (CAs del sistema); pinning, de existir, viviría en la capa Dart (no observable).
