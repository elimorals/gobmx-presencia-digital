# Manifiesto — permisos y flags

Fuente: `AndroidManifest.xml` decodificado. versionName 1.3.26, compileSdk 36.

## Permisos

| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | consulta de resultados |
| `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE` | caché/descarga (resultados, ¿APK de actualización?) |
| `AD_ID`, `ACCESS_ADSERVICES_TOPICS/AD_ID/ATTRIBUTION` | **publicidad / atribución** (AdServices) |
| `c2dm…RECEIVE` | notificaciones push (FCM) |
| `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE`, `WAKE_LOCK` | servicio/alarma (`alarmChecker`, aviso de sorteo) |
| `READ_BASIC_PHONE_STATE`, `VIBRATE` | UX |

**Lectura:** no pide cámara, ubicación, contactos, micrófono ni biometría. El bloque notable es
**AdServices + AD_ID** → tracking publicitario (doc 05). Para una app de resultados de lotería,
los permisos son por lo demás acotados.

## Flag crítico: cleartext habilitado

```xml
<application ... android:usesCleartextTraffic="true">
```

Permite tráfico **HTTP en claro** en toda la app. Combinado con los endpoints HTTP de resultados
(doc 03), habilita MITM sobre el contenido mostrado. No hay `networkSecurityConfig` por recurso.

## Componentes

- `CuponActivity` (verificación de billete) como pantalla principal.
- `StartAtBootServiceReceiver` + `alarmChecker` → alarma/aviso de sorteos tras el arranque.
- Firebase (FCM) estándar.
