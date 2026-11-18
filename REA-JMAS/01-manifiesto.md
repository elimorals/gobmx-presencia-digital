# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 3.0.3, compileSdk 36.

## Permisos (mínimos)
| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red (API REST) |
| `CAMERA` | escaneo de **QR** del contrato/recibo (`QRCodeScannerActivity`) |
| `com.android.vending.CHECK_LICENSE` | licencia de Play |
| `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | patrón AndroidX |

No pide ubicación, contactos, SMS, teléfono ni almacenamiento amplio. **Higiene de permisos
excelente** para una app con pagos.

## Componentes
- `MainActivity` (Compose, single-activity) + `QRCodeScannerActivity` (escaneo).
- `JMASMovilApplication` (Hilt). Sin servicios/receivers exportados peligrosos.

## Flags
- **Sin `usesCleartextTraffic`** → HTTP en claro bloqueado por defecto (consistente con el
  backend HTTPS, doc 03).
- Sin `networkSecurityConfig` por recurso → política por defecto (CAs del sistema; sin pinning,
  doc 03).
