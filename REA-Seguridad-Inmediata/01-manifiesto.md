# Manifiesto — permisos de emergencia, cleartext, PairIP

Fuente: `AndroidManifest.xml` decodificado. versionName 3.7.5, compileSdk 36.

## Permisos (de emergencia, extremos)

| Permiso | Para qué (app de pánico) |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación del ciudadano en la alerta |
| `ACCESS_WIFI_STATE`, `ACCESS_NETWORK_STATE` | geolocalización asistida / estado de red |
| `CAMERA` | captura de foto como evidencia |
| `RECORD_AUDIO`, `READ_MEDIA_AUDIO` | captura/anexo de audio |
| `READ_CONTACTS` | contactos de emergencia |
| **`CALL_PHONE`, `CALL_PRIVILEGED`** | llamada directa a emergencias (privilegiada) |
| `READ_PHONE_STATE` | estado de llamada / identificación de dispositivo |
| `FOREGROUND_SERVICE` | servicio en primer plano (ubicación durante la alerta) |
| `RECEIVE_BOOT_COMPLETED` | reactivar tras reinicio |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE`, `C2D_MESSAGE` | push (OneSignal/FCM) |
| `com.android.vending.CHECK_LICENSE` | **PairIP** / licensing de Play |

**Lectura:** conjunto **coherente con un botón de pánico**, pero amplio: ubicación + cámara +
micrófono + contactos + **llamada privilegiada** + phone-state. `CALL_PRIVILEGED` es inusual
(normalmente reservado a apps de sistema; su uso real no es verificable por el muro, doc 02).

## Flags clave

- **`android:usesCleartextTraffic="true"`** → HTTP en claro permitido para **todo** el tráfico.
  **No hay `res/xml/network_security_config`** (confirmado: no existe el recurso) → no se acota
  por dominio. Hallazgo principal (doc 06): para una app de pánico, cualquier endpoint `http://`
  expondría la alerta/ubicación. Si el backend real usa http no se pudo confirmar (muro), pero
  **el flag está activado**.
- **`android:name="com.pairip.application.Application"`** → la app arranca dentro del runtime
  de **PairIP** (anti-tamper + verificación de licencia de Google Play). Protege el DEX (doc 02).
- `android:allowBackup="true"` — respaldo habilitado (posible exposición de estado local vía
  backup; el contenido no es verificable por el muro).
- `android:extractNativeLibs="false"` — las `.so` AOT se cargan del APK comprimido.

## Componentes

Runtime .NET MAUI + PairIP + OneSignal (receivers/services de notificación) + FCM. La UI y la
lógica corren en el ensamblado `Cepo.CiudadanoMAUI` (AOT), no en clases Java propias.
