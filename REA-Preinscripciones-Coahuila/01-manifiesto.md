# 01 · Manifiesto

Fuente: `AndroidManifest.xml` (decodificado con apktool) y `evidence/manifest-xmltree.txt`
(`aapt2 dump xmltree`).

## Identidad
- Package `com.luca.preinscripciones`, versionName 1.0.0, versionCode 22.
- minSdk 24, targetSdk 36.

## Permisos (5)
| Permiso | Uso probable |
|---|---|
| `INTERNET` | red |
| `ACCESS_NETWORK_STATE` | estado de conexión |
| `CAMERA` | captura de documentos (CURP/comprobantes) |
| `ACCESS_COARSE_LOCATION` | escuelas cercanas |
| `ACCESS_FINE_LOCATION` | escuelas cercanas |

Más el permiso de firma autogenerado por Flutter
`com.luca.preinscripciones.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`.

**Sin** `READ_SMS`, `READ_CALL_LOG`, `READ_CONTACTS`, `ACCESS_BACKGROUND_LOCATION`, ni permisos
privilegiados. Superficie modesta.

## Componentes
- **1 activity exportada** — la `MainActivity` launcher de Flutter (exported=true por ser el punto
  de entrada; estándar).
- **1 receiver exportado** — estándar de Flutter/plugins.
- Resto **no exportado**: 1 service, 2 activities, 1 provider (`FileProvider`, exported=false).

No hay deeplinks sensibles ni componentes exportados fuera de lo normal para Flutter.

## Red
- **Sin** `android:usesCleartextTraffic` en el manifest.
- **Sin** `android:networkSecurityConfig`.
- Con targetSdk 36 (≥28), el default de Android **bloquea cleartext** salvo que una NSC lo permita;
  no hay NSC. Ver [03-tls-cleartext.md](03-tls-cleartext.md).
