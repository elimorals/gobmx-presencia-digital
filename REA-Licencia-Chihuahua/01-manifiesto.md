# 01 · Manifiesto y permisos

- **Package:** `com.gob.licdig` · versionName 615.12.11 (vc 14) · minSdk 22 · targetSdk 35.
- `AndroidManifest.xml` (xmltree de aapt2) en esta carpeta.

## Permisos declarados

| Permiso | Uso plausible |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `CAMERA`, `FLASHLIGHT` | escaneo de credencial / captura |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (¿verificación?) |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | autenticación biométrica |
| `VIBRATE` | feedback |

Superficie de permisos **razonable** para una app de licencia con escaneo + biometría; sin SMS,
contactos, llamadas ni almacenamiento amplio.

## Banderas de red en el manifiesto

- **`android:usesCleartextTraffic="true"`** (global) — y **no** hay `networkSecurityConfig` que lo
  acote. Config permisiva en una app con `targetSdk 35` (que por defecto bloquearía cleartext).
- `capacitor.config.json`: **`allowMixedContent: true`** → el WebView acepta recursos HTTP dentro de
  páginas HTTPS. Refuerza el punto anterior.
