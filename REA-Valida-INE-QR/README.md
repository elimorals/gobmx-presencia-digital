# Ingeniería inversa de Valida INE-QR 2.1.0 — índice

Análisis **estático** del APK `mx.ine.rfe.lectorqr` (base del bundle XAPK) con REA
(JADX headless 0.7.1) + `apktool`. La app **nunca se ejecutó**. Es la app oficial
pública del **Instituto Nacional Electoral (INE)** para leer y validar el código QR de
la credencial para votar, **#14 por instalaciones** del inventario gob.mx (1 M+ en
Google Play).

> El hallazgo central es de arquitectura, no una vulnerabilidad: la app **no declara el
> permiso `INTERNET`**, así que la validación del QR es **100 % local/offline**. El motor
> de lectura/validación es un **SDK propietario de terceros ("PersonalCode")** cuyo
> procesamiento vive en **código nativo ofuscado** (`libO0p.so`/`libl1I.so`) — un muro
> para el análisis estático del DEX. El binario está **firmado auténticamente por el INE**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | **Sin permiso INTERNET**, permisos mínimos, componentes, flags de seguridad |
| 02 | [02-validacion-qr.md](02-validacion-qr.md) | Flujo de escaneo, SDK PersonalCode, `pc()` nativo (muro) |
| 03 | [03-sdk-licencia-cuota.md](03-sdk-licencia-cuota.md) | `license.txt` firmada, cuota de escaneos, fecha de validez |
| 04 | [04-datos-credencial.md](04-datos-credencial.md) | Campos extraídos (CIC, CURP, OCR, sección, vigencia), catálogo de municipios |
| 05 | [05-sdks-terceros.md](05-sdks-terceros.md) | MLKit OCR, CameraX, PersonalCode, librerías |
| 06 | [06-firma-recursos.md](06-firma-recursos.md) | Firma auténtica INE, Source Stamp, estructura |
| 07 | [07-hallazgos-seguridad.md](07-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y del APK base |
| `evidence/license.txt` | Licencia firmada del SDK PersonalCode (NO versionado) |
| `evidence/catalogo_municipios_muestra.txt` | Muestra del catálogo INE de entidades/municipios (NO versionado) |
| `evidence/campos-credencial.txt` | Campos de la credencial detectados en strings del DEX |
| `decompiled/*.smali` | Clases clave decompiladas: `Azf`, `Native` (Scandit), `ce`, `vz`, `ResultsActivity`, `LegalsActivity` (NO versionado) |

> Lo propietario (APK/XAPK, `.so`, DEX, assets) **no se versiona** (`.gitignore`); vive
> en `descargas-apk/valida-ine-qr/`.

## 7 hallazgos principales

1. **Sin permiso `INTERNET` → validación 100 % offline.** El manifiesto declara solo
   `CAMERA`, `WAKE_LOCK`, `VIBRATE` y `HIDE_OVERLAY_WINDOWS`. Sin `INTERNET`, la app **no
   puede** enviar los datos de la credencial escaneada a ningún servidor ni contactar un
   backend. No se detectó ningún dominio/endpoint en el DEX (doc 01, 02).
2. **Validación del QR en código nativo ofuscado (muro).**
   `com.personalcode.Azf.pc(Activity, List<byte[]>, boolean): String` es un **método
   nativo** (cuerpo vacío en el DEX); el decode/validación ocurre en `libO0p.so`/
   `libl1I.so`, con nombres ofuscados. No decompilable estáticamente desde el DEX (doc 02).
3. **SDK comercial licenciado con cuota y fecha de validez.** `assets/license.txt`
   contiene una licencia **firmada** (`vcredencial:100d`, fecha `20260604`, atada a dos
   huellas de certificado, blob RSA + firma ECDSA). Los métodos `hasRemaining()`/
   `getRemaining()` imponen una **cuota de escaneos** validada localmente (doc 03).
4. **Extrae CIC, CURP, OCR, sección y vigencia de la credencial.** Strings del DEX
   (`cic=, curp=, ocr=, seccion=, vigencia=`, `tvCIC/tvCURP/tvOCR`) muestran que el QR
   codifica esos campos y la app los parsea y muestra localmente (doc 04).
5. **Firma auténtica del INE.** Certificado `CN=INSTITUTO NACIONAL ELECTORAL,
   O=INSTITUTO NACIONAL ELECTORAL, L=CIUDAD DE MEXICO, C=MX` y la firma verifica → es el
   binario firmado por el emisor real (más fuerte que CFE). Hay un Source Stamp genérico
   de Google por la redistribución vía APKPure (doc 06).
6. **Manifiesto con buena postura de seguridad.** `allowBackup=false`,
   `HIDE_OVERLAY_WINDOWS` (anti-overlay/tapjacking), `useEmbeddedDex=true`, todas las
   actividades `exported=false` salvo el launcher, y un permiso de receiver dinámico a
   nivel `signature` (doc 01).
7. **OCR de MLKit + CameraX, cámara frontal obligatoria.** `com.google.mlkit.vision.
   DEPENDENCIES=ocr` y `camera.front required=true`: además del QR, la app usa OCR para
   leer el texto impreso de la credencial (doc 02, 05).

## Alcance y límites

Análisis **original** de una app pública del gobierno mexicano que se posee, con fines de
investigación de seguridad. **No redistribuye** el APK ni las librerías del INE/PersonalCode
(`.gitignore`). Observaciones **estáticas**: la app **no se ejecutó**, no se escaneó ninguna
credencial real ni se llamó a servicio alguno (no hay ninguno que llamar: sin `INTERNET`).
La lógica de validación criptográfica del QR reside en librerías nativas ofuscadas que **no
se desensamblaron** (fuera de alcance del análisis estático del DEX); se documenta como
muro. La fecha `20260604` de la licencia se reporta como dato observado; **no se verificó**
si el SDK deshabilita la app tras esa fecha, porque eso depende de la aplicación de la
licencia en el código nativo, que no se decompiló.

---
