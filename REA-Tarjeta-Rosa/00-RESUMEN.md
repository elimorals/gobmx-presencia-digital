# Resumen — Tarjeta Rosa 1.0.5 (`gob.guanajuato.TarjetaRosa`)

## Identidad

- **App:** Tarjeta Rosa — programa social de apoyo a mujeres del Gobierno del Estado de
  Guanajuato (GEG).
- **Package:** `gob.guanajuato.TarjetaRosa` · **versión** 1.0.5 (versionCode 10).
- **minSdk** 24 · **targetSdk** 36. · **#15** del inventario por instalaciones (≈1 M).
- **Firma:** re-firmada por APKPure (`CN=Android, O=Google Inc.`) → no auténtica del GEG.

## Arquitectura (en una línea)

Cáscara nativa Android (Kotlin/Java) que carga el portal `https://app-tarjeta-rosa.guanajuato.gob.mx`
en un WebView. 12 clases propias; el resto son AndroidX/Firebase/GMS. La lógica y la PII viven
en el servidor.

Clases propias (`search_android_classes`):
`MainActivity`, `WebViewConfigurator`, `WebAppInterface`, `NetworkHelper`, `DeviceRegister`,
`NotificationHelper`, `PermissionManager`, `FirebaseMessagingService`, `ForegroundService`,
`bootReceiver`, `BuildConfig`, `R`.

## Qué hace el APK (lo nativo)

- Monta el WebView y lo configura (`WebViewConfigurator.configureWebView`).
- Expone un **puente JavaScript** (`WebAppInterface`) al web app: `openUrl`, `deviceRegister`,
  `checkConnectionAndReload`.
- Registra el token de **Firebase Cloud Messaging** (push) vía `DeviceRegister`.
- Pide **cámara** y **lectura de fotos** para captura de documentos (foregroundServiceType=camera).
- Pantalla de error offline, servicio en primer plano, receptor de arranque.

## Postura de seguridad (balance)

| Aspecto | Veredicto |
|---|---|
| TLS del WebView | ✅ **No** desactiva validación (sin `onReceivedSslError`) |
| Cleartext | ✅ Sin `usesCleartextTraffic`; URL base HTTPS a `.guanajuato.gob.mx` |
| PII en el binario | ✅ Mínima (todo del lado del servidor) |
| Puente JS | ⚠️ `openUrl` abre intents arbitrarios sin validar; adjunto a todas las páginas |
| File access del WebView | ⚠️ `setAllowFileAccess(true)` innecesario |
| Navegación | ⚠️ Sin `shouldOverrideUrlLoading` → no restringida al dominio |
| Firma | ⚠️ Re-firmada por APKPure (no auténtica) |

**Lectura:** es una de las apps con **mejor higiene de transporte** del lote (no rompe TLS, sin
cleartext), pero su riesgo concreto es el **puente JS demasiado abierto** combinado con una
navegación no acotada al dominio oficial.
