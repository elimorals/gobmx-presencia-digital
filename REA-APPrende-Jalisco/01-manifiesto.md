# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 2.0.5, compileSdk 35.

## Permisos
| Permiso | Para qué (inferido) |
|---|---|
| `GET_ACCOUNTS`, `USE_CREDENTIALS` | cuentas/autenticación (Firebase Auth / Google) |
| `RECORD_AUDIO`, `MODIFY_AUDIO_SETTINGS` | grabación (contenido de cursos / mensajería) |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | archivos (documentos/evidencia de trámites) |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `WAKE_LOCK` | red / background |
| `com.android.vending.CHECK_LICENSE` | licencia de Play |

Conjunto **moderado**. No pide ubicación, SMS ni contactos directos (más allá de `GET_ACCOUNTS`).
`RECORD_AUDIO` es el permiso sensible; coherente con contenido educativo/mensajería.

## Flags
- Sin `usesCleartextTraffic` ni `networkSecurityConfig` en el manifiesto → por defecto HTTP en
  claro bloqueado (consistente con los backends HTTPS, doc 03).
- Contenedor Cordova (`CordovaActivity`/WebView) + receptores de Firebase/FCM.
