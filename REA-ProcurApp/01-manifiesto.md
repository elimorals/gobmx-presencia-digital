# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 2.3.5, compileSdk 35.

## Permisos (sensibles, coherentes con denuncias)

| Permiso | Para qué |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación de la denuncia / botón de pánico |
| `CAMERA` | evidencia fotográfica de la denuncia |
| `ACCESS_MEDIA_LOCATION` | geolocalización embebida en las fotos (EXIF) |
| `CALL_PHONE` | llamada directa a emergencias/fiscalía |
| `READ_CONTACTS`, `WRITE_CONTACTS` | **contactos de pánico** (a quién avisar) |
| `READ_PROFILE`, `WRITE_PROFILE` | perfil del usuario/denunciante |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | adjuntar/guardar evidencia |
| `FOREGROUND_SERVICE`, `WAKE_LOCK` | servicio (tracking/pánico) |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | notificaciones (FCM) |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |

**Lectura:** el conjunto es consistente con una app de denuncias (ubicación + cámara +
media-location para evidencia con geolocalización, call_phone para emergencia, contactos para
pánico). `ACCESS_MEDIA_LOCATION` + `CAMERA` implica que las fotos de evidencia pueden llevar
coordenadas EXIF del lugar/denunciante — PII adicional que viaja al servidor (por el canal
inseguro del doc 03).

## Componentes

- ~60 Activities (una familia por módulo: `DenunciaActivity`, `DenunciaGeneroActivity`,
  `PanicoActivity`, `ContactosPanicoActivity`, `PerfilGeneticoActivity`,
  `PersonaDesaparecidaActivity`, `ExtorcionActivity`, `CartasActivity`, `LoginActivity`,
  `RegistroActivity`, `RecoverPassActivity`…). Ver doc 02.
- Room (persistencia local) + Retrofit (red) + Firebase (FCM/Analytics).

## Flags

- Sin `usesCleartextTraffic` → por defecto HTTP en claro bloqueado. **Pero** el problema no es
  cleartext: es que las rutas HTTPS **no validan el certificado** (doc 03), lo que el flag de
  cleartext no cubre.
- No se declara `networkSecurityConfig` por recurso; la (des)configuración de TLS vive en el
  código (`UnsafeOkHttpClient`).
