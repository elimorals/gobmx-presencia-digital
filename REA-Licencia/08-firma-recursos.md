# 08 · Firma, recursos y limitaciones

## Firma del APK

- **No verificada.** `apksigner` no está disponible en el entorno de análisis, y el APK proviene
  del SSD (no de la tienda). No se afirma el esquema de firma (v1/v2/v3) ni el certificado.
- El APK incluye un `stamp-cert-sha256` a nivel raíz (sello de origen típico de Google Play), pero
  no se verificó criptográficamente.
- Para cerrar esto: `apksigner verify --print-certs base.apk` en un entorno con build-tools.

## Hashes

Ver `hashes.sha256`. APK base:

```
8216279428d74bf623056772168463a285f487a955c033f01b9f1dc1c45f83c4  base.apk  (30,548,056 bytes)
```

## Recursos conservados en este REA

- `AndroidManifest.xml` — manifiesto decodificado.
- `decoded/` — salida completa de apktool (smali, recursos, assets, incl. `assets/public/` con el bundle JS).
- `evidence/` — `seed.sql`, `capacitor.config.json`, `cordova_plugins.js`, `apktool.log`.

## Limitaciones del análisis (honestas)

1. **Estático puro.** La app nunca se ejecutó; no hubo intercepción de tráfico.
2. **Rutas de API exactas no determinadas** — se construyen en runtime; sólo se fijaron los hosts
   base (doc 03).
3. **Longitud del PIN no confirmada** (plantilla Angular; doc 04).
4. **Hallazgo 2 (openDB sin llave)** es una debilidad **probable**, pendiente de confirmar con
   dinámica el orden de ejecución (doc 05).
5. **Firebase project / project_id** no recuperado; no se afirma.
6. **Firma no verificada** (apksigner ausente).

Siguiente paso natural si se quiere cerrar lo abierto: Frida/MITM sobre el dispositivo —
interceptar `wslfd.sct.gob.mx` (rutas y tokens reales) y observar el orden de `openDB`.
