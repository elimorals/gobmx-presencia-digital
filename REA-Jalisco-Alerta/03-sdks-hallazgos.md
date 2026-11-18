# 03 · SDKs terceros y hallazgos

## SDKs / librerías de terceros

| SDK | Evidencia | Nota |
|---|---|---|
| **Expo** | `exp.host/--/api/v2/push`, `classic-assets.eascdn.net`, `expo-modules-core` | framework/push/OTA |
| **React Native 0.81.5** | libs + Hermes bundle | New Architecture |
| **Sentry** | `libsentry.so` + DSN en bundle | crash reporting (ver hallazgo) |
| **ML Kit Barcode** | `libbarhopper_v3.so` | escaneo QR |
| **Firebase / FCM** | permisos C2DM, receptores | push |
| Badges de launcher | ~20 permisos badge | contador de notificaciones |

## Hallazgos

### 🟡 BAJA — Sentry DSN público embebido (CWE-312)
- DSN en el bundle: `https://d64ff195084df32ccc0387bb64dd9dfe@o4510069627879424.ingest.us.sentry.io/4511214478098432`.
- Un DSN de Sentry va embebido en el cliente **por diseño** (es una clave pública de ingesta). El
  riesgo es acotado: un tercero podría **enviar eventos/ruido falso** al proyecto Sentry; **no**
  permite leer datos ni crashes ajenos.
- **Remediación:** opcional — rate-limiting / `tunnel` de Sentry si el ruido es un problema. No es
  una fuga de secreto crítico.

### ⚪ INFO — Superficie de permisos
- `ACCESS_BACKGROUND_LOCATION` + `CAMERA` + `SYSTEM_ALERT_WINDOW`. Coherente con app de alerta, pero
  la ubicación en segundo plano exige justificación y aviso de privacidad explícito.

### ⚪ INFO — Proveniencia
- Re-firmada por APKPure (`CN=Android, O=Google Inc.`). El sha256 del APK del acervo no sirve para
  probar autenticidad; cruzar contra Google Play oficial.

### 🟢 SANO (positivos, confirmados por estático)
- **No cleartext** (sin `usesCleartextTraffic`, targetSdk 36, sin http:// de backend).
- **No trust-all** (sin marcadores de desactivación TLS en el bundle).
- Backends del producto por **HTTPS**.
- **No** se hallaron API keys de Google Maps / AIza ni credenciales hardcodeadas en el bundle.

## Cruce con el mapa de proveedores (`analisis/MAPA-PROVEEDORES-SDK.md`)

- **Hipótesis "apps de pánico → cleartext/TLS-off (patrón 911-CDMX)": NO se cumple aquí.** Jalisco
  Alerta es de las apps de emergencia **limpias** en TLS/cleartext. Es un contraejemplo útil: no
  todas las apps de alerta repiten el defecto.
- Vendor nuevo al inventario: **Erlia** (`erlia.mx`, backend `jaliscoalerta.com`), stack Expo/RN.

## Límites

La lógica JS es **Hermes bytecode** (muro): los endpoints y strings se extraen, pero el flujo de
red en runtime, el manejo de tokens y la validación TLS efectiva de OkHttp **no** se confirman por
estático. Para eso haría falta análisis dinámico (Frida/proxy).
