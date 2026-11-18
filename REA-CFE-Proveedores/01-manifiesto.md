# Manifiesto — permisos, cleartext, backup

Fuente: `AndroidManifest.xml` decodificado (apktool). versionName 1.0.7, compileSdk 33.

## Permisos (mínimos)
| Permiso | Para qué |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | red (API REST) |
| `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` | guardar/adjuntar documentos de concurso |
| `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | patrón AndroidX |

Conjunto **acotado**: sin cámara, ubicación, contactos, SMS ni teléfono. Coherente con un portal
B2G de proveedores.

## Flags de seguridad (aquí está el ruido)
```
android:usesCleartextTraffic="true"
android:networkSecurityConfig="@xml/network_security_config"
android:allowBackup="true"
```
- **`usesCleartextTraffic="true"`** → HTTP en claro permitido a nivel app (no se bloquea el
  tráfico sin TLS).
- **`network_security_config`** (ver `evidence/network_security_config.xml`): un `domain-config`
  con `cleartextTrafficPermitted="true"` para **2 IPs privadas de la red interna de la CFE**:
  `10.4.70.22` y `10.220.1.71` (RFC1918). Residuo de entorno interno/desarrollo embebido en el
  build de producción → fuga de topología interna (doc 04, 07).
- **`allowBackup="true"`** → los datos de la app pueden respaldarse vía ADB/backup del sistema
  (doc 07).

> La API pública (`enlacesec.cfe.mx`) usa `https://` por su propia URL, así que en la práctica va
> cifrada; pero con `usesCleartextTraffic=true` nada impide tráfico HTTP si una URL cambiara a
> `http://`, y las 2 IPs internas sí están habilitadas para claro.

## Componentes
Runtime de Xamarin.Forms (`MainActivity` de Xamarin) + `android.app.Application`. Sin componentes
exportados peligrosos observados.
