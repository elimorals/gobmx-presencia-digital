# 01 · Manifiesto

Package `sspc.gn.cientifica` v1.0 (code 19), minSdk 24, targetSdk 33. Manifest decodificado en
`AndroidManifest.xml`.

## Permisos

| Permiso | Para qué |
|---------|----------|
| `CAMERA`, `RECORD_AUDIO`, `RECORD_VIDEO` | **captura de evidencia** de la denuncia (foto/audio/video) |
| `CALL_PHONE` | llamada directa (¿911/GN?) |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | adjuntar/guardar evidencia |
| `READ_PHONE_STATE` | estado del teléfono (identificación de dispositivo) |
| `com.google.android.c2dm.permission.RECEIVE`, `READ_GSERVICES` | **FCM** (push) |

Sin permisos de ubicación declarados (notable para una app de denuncia: la georreferencia, si
existe, no se apoya en permiso de localización fina/gruesa en el manifiesto).

## Configuración de red

- `android:usesCleartextTraffic="true"` (global) — **sin** `networkSecurityConfig`.
  Esto **permite** HTTP en claro para toda la app, aunque el backend observado es HTTPS (ver doc 02).
  En una app federal de denuncia, lo correcto es `usesCleartextTraffic="false"`.
