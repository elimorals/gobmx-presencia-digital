# Manifiesto — permisos, flags, legacy

Fuente: `AndroidManifest.xml` decodificado. versionName 3.0.0, minSdk 26, target 34.

## Permisos

| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (mapa de tráfico fronterizo, módulos cercanos) |
| `CALL_PHONE` | llamada directa a contactos consulares / emergencia |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | almacenamiento (contenido descargado) |

Conjunto acotado: no pide cámara, micrófono, contactos ni SMS. `CALL_PHONE` permite marcar sin
confirmación del usuario (coherente con botón de "llamar al consulado").

## Flags de seguridad (los importantes)

- **`android:usesCleartextTraffic="true"`** → **HTTP en claro habilitado globalmente**. Combinado
  con los endpoints `http://` del SAT (doc 03), el tráfico de contenido puede ir sin TLS. Es el
  hallazgo de red central.
- **`android:allowBackup="true"`** → la base SQLite local (contenido/contactos cacheados) es
  respaldable vía ADB/backup. Bajo riesgo (no hay credenciales de pago aparentes), pero mala
  práctica por defecto.
- Sin `android:networkSecurityConfig` → no hay política por recurso; con `usesCleartextTraffic=true`
  el cleartext aplica a todo dominio.

## Señales de código legacy

- `<uses-library android:name="org.apache.http.legacy">` → usa el **cliente HTTP de Apache
  obsoleto** (removido de Android por defecto desde API 28; se re-añade como librería). Pila de
  red antigua.
- `android:appComponentFactory="android.support.v4.app.CoreComponentFactory"` → **support library
  v4** (no AndroidX) → base de código **vieja**.
- Solo 2 Activities (`SplashActivity` launcher + `MainActivity`), navegación por Fragments.

## Componentes

`SplashActivity` (exportada, launcher) y `MainActivity` (no exportada). No hay servicios,
receivers ni providers propios declarados — app simple de contenido + consultas.
