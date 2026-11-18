# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 2.2.1, targetSdk 29 (antiguo).

## Permisos
| Permiso | Para qué |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `LOCATION_HARDWARE` | localizar estaciones cercanas |
| `CAMERA` | foto de **evidencia** de la queja (doc 04) |
| `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE` | manejo de la foto |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | red |

Conjunto **acotado y coherente** con un localizador + reporte con foto: ubicación, cámara,
almacenamiento. No pide contactos, SMS, teléfono ni ubicación en segundo plano.

## Componentes
- Launcher `crc64….SplashActivity` (prefijo `crc64` = Java Callable Wrapper de Xamarin → la UI y
  la lógica corren en el runtime .NET/Mono, doc 02).
- `targetSdk 29` (Android 10) — build viejo; hoy Play exige targetSdk mucho más alto para
  actualizaciones, señal de app sin mantenimiento reciente.

## Flags
- `WRITE_EXTERNAL_STORAGE` sin `maxSdkVersion` y targetSdk 29 → almacenamiento legacy (previo a
  scoped storage estricto). Deuda de modernización, no vulnerabilidad directa.
- Transporte HTTPS (doc 03); no se observó `usesCleartextTraffic` activo.
