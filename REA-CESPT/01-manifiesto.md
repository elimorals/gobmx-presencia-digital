# Manifiesto — permisos y flags

Fuente: `AndroidManifest.xml` decodificado (apktool). versionName 3.1.1, compileSdk 36.

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `CAMERA` | lectura de medidor / adjuntar foto en reporte de fugas |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicar fugas / oficinas / geocodificación |
| `READ/WRITE_EXTERNAL_STORAGE` (`maxSdkVersion=32`) | adjuntar/guardar en versiones viejas |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE`, `FOREGROUND_SERVICE` | push (FCM) |
| `VIBRATE`, `WAKE_LOCK` | UX |

Conjunto **acotado** para una app de servicios de agua con reporte georreferenciado.

## Flags de `<application>` (los relevantes)

```
android:usesCleartextTraffic="true"
android:networkSecurityConfig="@xml/network_security_config"
android:allowBackup="true"
android:extractNativeLibs="true"
```

- **`usesCleartextTraffic="true"`** — habilita HTTP en claro. El detalle fino lo define el NSC,
  que **también** lo permite de forma global (doc 04, hallazgo H1).
- **`networkSecurityConfig`** presente → ver doc 04: su `base-config` permite cleartext a todos
  los dominios y confía en CAs del usuario.
- **`allowBackup="true"`** (H3, Bajo): el estado de la app sería respaldable por ADB/nube. La
  config de Capacitor pone `BackupWebStorage="none"` (doc 02), lo que mitiga parcialmente la
  fuga del almacenamiento web; aun así, lo recomendable en una app con login es `false`.

## Componentes
Runtime de Capacitor (`BridgeActivity`), plugins Cordova (InAppBrowser), Firebase (FCM). Sin
componentes exportados peligrosos observados más allá de lo estándar de Capacitor.
