# 08 · Firma, recursos y limitaciones

## Procedencia y firma

- APK obtenido **de Google Play** (instalado en Motorola XT2435-3 y extraído con `adb pull`), no del
  SSD. Es la build oficial distribuida.
- Firma **no verificada** criptográficamente (apksigner no disponible en el entorno). Hay
  `stamp-cert-sha256` (sello de origen Play) pero no se validó.
- Cierre: `apksigner verify --print-certs base.apk` en un entorno con build-tools.

## Bundle (splits extraídos del dispositivo)

| APK | Tamaño | Contenido |
|---|---|---|
| `base.apk` | 33 MB | DEX (3), recursos, assets (`.szip`), manifiesto |
| `split_config.arm64_v8a.apk` | 17 MB | `.so` nativas (SQLCipher, Conscrypt, ML Kit, Duktape, toolChecker, packer) |
| `split_config.en.apk` | 96 KB | Recursos de idioma (inglés) |
| `split_config.xxhdpi.apk` | 154 KB | Recursos de densidad |

Hashes SHA-256 de los cuatro en `hashes.sha256`.

## Recursos conservados en este REA

- `AndroidManifest.xml` — manifiesto decodificado.
- `decoded/` — salida de apktool del `base.apk` (smali, recursos, assets incl. `.szip`).
- `evidence/` — `network_security_config.xml` (vacío), `rasp-strings.txt` (conteos de detección), `apktool.log`.
- `_apk/` — los 4 APK originales extraídos del dispositivo.

## Limitaciones del análisis (honestas)

1. **Estático puro.** La app nunca se ejecutó; no hubo tráfico interceptado.
2. **Lógica de negocio no recuperada.** Vive en la definición Veritran empacada (`.szip`) e
   interpretada por Duktape; no es decompilable como Kotlin. No se desempacó el `.szip` (doc 02).
3. **Endpoints del backend no determinados** — no hay literal en claro (doc 03).
4. **Derivación de llave SQLCipher no observada** — gestionada por Veritran/`vtcrypto` (doc 05).
5. **RASP sólo evidenciado por strings** — existencia del código de detección, no su ejecución (doc 04).
6. **Firma no verificada.**

## Siguiente paso si se quiere profundizar

Dinámica de alta dificultad por el RASP: habría que neutralizar detección de root/Frida/debugger
(p. ej. Frida con anti-RASP + unpinning) sobre el Motorola, y/o desempacar los `.szip` del runtime
Veritran, para recuperar flujos, endpoints del gateway y manejo de secretos. Es un esfuerzo mayor y
separado; este REA cubre el descubrimiento **estático** completo.
