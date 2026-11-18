# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 6.0.8, compileSdk 37.

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `CHANGE_NETWORK_STATE` | red |
| `READ_PHONE_STATE` | identificación de dispositivo (modelo `Dispositivo`) |
| `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE` | guardar/compartir PDF de la tarjeta (`TcPdfProvider`) |
| `READ_GSERVICES` | Google Services Framework (Firebase) |
| `VIBRATE` | UX |

Notable: **no** declara `CAMERA` en el badging pese a tener `ScannQrActivity`/`ScannerActivity`
(escaneo de QR para compartir tarjetas) — probablemente use un lector por intent o la cámara se
declara de otra forma; el manifiesto completo está en `AndroidManifest.xml` para verificación. No
pide ubicación, contactos ni SMS directo (el OTP llega por Firebase, no lee SMS). Conjunto
**acotado** salvo `READ_PHONE_STATE`.

## Componentes

MVP nativo: decenas de Activities (`LoginActivity`, `RegistroV2Page1-3`, `LicenciaDigitalActivity`,
`TarjetaCirculacionActivity`, `CompartirTarjetaActivity`, `ScannQrActivity`, `SmsFirebaseActivity`,
`CarteraDigitalActivity`…), Fragments (Home/Dashboard/Notifications) y un
`NotificationsBroadcastReceiver`. Firebase (FCM + Phone Auth) y GMS estándar.

## Flags

- **Sin `usesCleartextTraffic`** en el manifiesto → HTTP en claro bloqueado por defecto
  (consistente con los endpoints HTTPS, doc 04).
- **Sin `networkSecurityConfig`** por recurso → política por defecto; el pinning (que no existe,
  doc 04) tendría que estar en código.
