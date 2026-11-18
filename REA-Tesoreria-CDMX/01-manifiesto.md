# Manifiesto — permisos, cleartext y configuración de red

Fuente: `AndroidManifest.xml` decodificado con `apktool d -s` (copia completa en
[`AndroidManifest.xml`](AndroidManifest.xml)) y
`evidence/apktool/res/xml/network_security_config.xml`.

## Permisos

| Permiso | Uso plausible | Nota |
|---|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | Red | — |
| `CAMERA`, `FLASHLIGHT` | Escaneo de QR (MLKit) de líneas de captura | — |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | Login biométrico (plugin `capacitor-native-biometric`) | — |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación (¿citas/sucursales?) | sin flujo claro en el bundle |
| `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, `WAKE_LOCK`, `VIBRATE` | Push/notificaciones locales | — |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | Guardar comprobantes/PDF (file-opener) | — |
| `BIND_JOB_SERVICE` | WorkManager/FCM | — |
| **`DUMP`** | — | **Inusual.** `android.permission.DUMP` es `signature\|privileged`; el sistema no lo concede a apps de tienda. Sin uso visible. |
| **`RECORD_AUDIO`**, `MODIFY_AUDIO_SETTINGS` | — | **Inusual** en una app de pagos; sin flujo de grabación a la vista. Puede venir arrastrado por una dependencia. |

> **No inventamos la causa** de `DUMP` y `RECORD_AUDIO`: la evidencia estática no
> identifica el flujo que los usaría. Se reportan como superficie declarada sin
> justificación observable (doc 08).

## Tráfico en claro

```xml
<application ... android:usesCleartextTraffic="true" ...
             android:networkSecurityConfig="@xml/network_security_config">
```

`network_security_config.xml`:

```xml
<network-security-config>
  <domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">10.1.181.71</domain>
    <domain includeSubdomains="true">tesoreriacdmx.finanzas.cdmx.gob.mx</domain>
    <domain includeSubdomains="true">sac.finanzas.cdmx.gob.mx</domain>
    <domain includeSubdomains="true">server.arcgisonline.com</domain>
  </domain-config>
</network-security-config>
```

- Cleartext **permitido** al dominio de **pagos** (`tesoreriacdmx…`) y a
  `sac.finanzas…`. Si alguna petición se hace por `http://` a esos hosts, viaja sin
  TLS. Los endpoints en el bundle usan `https://` (doc 03), pero la política lo
  **permite**, lo que amplía la superficie a un downgrade.
- **IP interna `10.1.181.71`** en la lista blanca de cleartext. Es una dirección
  privada (RFC 1918), señal de un host interno/dev que quedó en la config de
  producción. **No hay evidencia de que el bundle JS la invoque** (solo aparece en
  el NSC), así que no afirmamos que la app la use; sí es una fuga de infraestructura
  interna en el binario (doc 08).

## Componentes

Capa nativa típica de Capacitor: `MainActivity` (WebView), servicios de FCM y
WorkManager, y los plugins declarados en `assets/capacitor.plugins.json` (doc 02).
No se observaron `provider`/`receiver` exportados con superficie notable más allá
de los de Firebase/Capacitor estándar.
