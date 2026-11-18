# Firma del APK, recursos y procedencia

## ⚠️ Procedencia: APK re-firmado por APKPure (no es el binario original de la ATY)

El APK se descargó de **APKPure** con `apkeep` (fuente sin cuenta de Google). La verificación
de firma muestra el certificado **genérico de Google**, no uno de la ATY ni del proveedor:

```
Signer #1 certificate DN:       CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
Signer #1 SHA-256:              fa7fa11ac645e13335b37e23d343910972645704707d171ea4ba3ebf55602cd8
Source Stamp Signer DN:         CN=Android, OU=Android, O=Google Inc., …
Source Stamp SHA-256:           3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

- Es el patrón de un APK **re-empaquetado y re-firmado** por el pipeline de redistribución de
  APKPure (mismo Source Stamp genérico que CFE Contigo en este repo).
- **Consecuencia:** este binario **no es verificable como el artefacto auténtico publicado por
  la ATY**. Sirve para análisis de arquitectura (lo que hace este documento); **ninguna
  conclusión de integridad** debe asumir que no fue alterado por el redistribuidor.
- Para el binario auténtico: descargar desde **Google Play con cuenta** (o del canal oficial) y
  comparar el certificado real del emisor.

## Identidad real del autor (del binario, no de la firma)

Aunque la firma es genérica, el **contenido** sí identifica al origen:
- Namespace `com.embsoft.vinden` + ruta de build del dev en `libapi-keys.so` (doc 05).
- Dominios del proveedor **MRP Technology** (`mrptechnology.mx`) y de la ATY
  (`transporteyucatan.org.mx`, `appvayven.yucatan.gob.mx`).

## Estructura

- `classes.dex`, `classes2.dex`, `classes3.dex` (multidex, código nativo Vinden).
- `assets/www/` → bundle React (pantalla de pago) con source maps.
- `lib/{arm64-v8a,armeabi-v7a,x86,x86_64}/` → `libapi-keys.so`, `libimage_processing_util_jni.so`,
  `libsurface_util_jni.so` (CameraX).
- `resources.arsc`, `res/`.
- Protos de Firebase (`messaging_event.proto`, `client_analytics.proto`).

## Hashes

SHA-256 del APK en [`hashes.sha256`](hashes.sha256).
