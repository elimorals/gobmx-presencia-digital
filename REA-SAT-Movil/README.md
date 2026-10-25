# Ingeniería inversa de SAT Móvil 2.4.4 — índice

Análisis **estático** (docs 00–10) del bundle XAPK `mx.gob.sat` con REA (apktool 3.0.3 + JADX
headless 0.7.1 + `strings`/`unzip` sobre los `.so`), **más un pase dinámico acotado** en dispositivo
físico sin root (doc 11). Es la app oficial del Servicio de Administración Tributaria (SAT) de México
para consulta de servicios, **e.firma portable** y verificación de identidad **SAT ID**.

> **Diferencia estructural clave con el resto del inventario gob.mx:** SAT Móvil **no es una
> app Android nativa**, es una app **Flutter** (Dart AOT). La lógica, los textos y los
> endpoints no están en el DEX ni en `res/values`, sino en `lib/arm64-v8a/libapp.so`
> (snapshot Dart) y en los assets de `flutter_assets/`. Por eso el método es distinto al de
> CFE Contigo o jw-library.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila Flutter/Dart, módulos, mapa de hallazgos, contraste |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, FileProviders, servicio en background, superficie |
| 02 | [02-arquitectura-flutter.md](02-arquitectura-flutter.md) | Flutter 3.x / Dart 3.10, `libapp.so`, `libflutter.so`, `libdartjni.so`, assets |
| 03 | [03-autenticacion.md](03-autenticacion.md) | OAuth2 **ROPC** (`grant_type=password`), SAT ID, captcha propio, biometría |
| 04 | [04-red-endpoints.md](04-red-endpoints.md) | **`.env` en claro**, prod/UAT/dev, gateway Zuul, **IP de Azure filtrada**, sin pinning |
| 05 | [05-cripto-efirma.md](05-cripto-efirma.md) | **e.firma portable** (firmado local), PointyCastle, asn1lib, AES-GCM, secure_storage |
| 06 | [06-biometria-satid.md](06-biometria-satid.md) | SAT ID: cámara + audio + **video**, TensorFlow Lite on-device, liveness |
| 07 | [07-sdks-dependencias.md](07-sdks-dependencias.md) | SBOM (NOTICES, 3,655 secciones), paquetes Dart, plugins, sin Firebase Analytics |
| 08 | [08-pantallas-flujos.md](08-pantallas-flujos.md) | Declaración anual, citas, chat, denuncias, CFDI/carta porte, QR, opinión de cumplimiento |
| 09 | [09-firma-recursos.md](09-firma-recursos.md) | Firma del bundle, splits, assets, `commit.txt`, procedencia |
| 10 | [10-extraccion-profunda.md](10-extraccion-profunda.md) | **Árbol de código Dart (129 archivos)**, decode con smali, correcciones de precisión |
| 11 | [11-dinamica.md](11-dinamica.md) | **Ejecución en dispositivo físico** (sin root): Flutter/Impeller vivo, TFLite no carga al arranque, límites |

## Artefactos

Toda la decodificación se materializa **en este directorio** (los binarios/propietarios quedan
locales, fuera de git por `.gitignore`; los textos sí se versionan).

| Carpeta / archivo | Versionado | Contenido |
|---|---|---|
| `decompiled/` | sí | `MainActivity.a()` (configureFlutterEngine) de JADX |
| `AndroidManifest.xml` | sí | Manifiesto decodificado completo del APK base |
| `strings.json` | sí | `res/values/strings.xml` (72 entradas, **todas de framework**) |
| `xapk-manifest.json` | sí | `manifest.json` original del bundle XAPK |
| `hashes.sha256` | sí | SHA-256 de los 5 APK del bundle |
| `NOTICES.txt` | sí | SBOM descomprimido (34,697 líneas de licencias OSS) |
| `analisis/dart-tree-sat_movil_2.txt` | sí | 129 rutas `.dart` propias recuperadas de `libapp.so` |
| `res-decoded/base/` | **no** | apktool `d` completo: `res/`, **`smali/`**, manifest, `apktool.yml` (~60 MB) |
| `flutter_assets/` | **no** | Assets Flutter completos (fuentes, SVG/PNG, `.env.*`, manifiestos, shaders) |
| `libs/arm64-v8a/` | **no** | Los 7 `.so` (libapp, libflutter, libtensorflowlite_c, libdartjni, …) |

> Los artefactos pesados y propietarios del SAT (APK, `.so`, fuentes, SVG, `.env`, `flutter_assets/`,
> `res-decoded/`) **no se versionan** — ver `.gitignore` — pero **ya viven en este directorio**. El
> bundle original (XAPK y splits) permanece en el SSD en `/Volumes/PortableSSD/apps-gobmx-mx/`.

## 6 hallazgos principales

1. **Configuración de red en texto plano dentro del APK.** `flutter_assets/.env.production`,
   `.env.uat` y `.env.dev` viajan **sin cifrar** en el binario y listan todos los endpoints,
   el flujo OAuth2 y el gateway. Es justo lo contrario de CFE Contigo, que ocultó sus
   endpoints con cifrado de strings DexGuard. En runtime se carga **`.env.production`** (doc 04).
2. **Fuga de infraestructura interna UAT/dev.** Los `.env.uat` y `.env.dev` apuntan SAT ID a
   una **IP pública de Azure hardcodeada `https://13.85.190.35:3002`** (captcha en `:3001`),
   HTTPS sobre IP con puertos no estándar. No es prod, pero queda embarcado en la app de tienda
   y revela topología interna (doc 04).
3. **OAuth2 con `grant_type=password` (ROPC).** El usuario entrega RFC + contraseña (CIEC) y
   la app las postea directo al endpoint de token vía gateway **Zuul** (Netflix OSS). Flujo
   desaconsejado por OAuth 2.1; funcional pero expone credenciales al cliente (doc 03).
4. **e.firma portable: firmado criptográfico en el dispositivo.** La app embebe **PointyCastle**
   (AES-GCM, RSA), **asn1lib** (parseo de `.cer`/`.key`) y `crypto`/`encrypt`; maneja
   "Certificados de e.firma" y rutas `/dwh/efirma/`, `/dwh/digitalcertificate/`. Opera con la
   FIEL del contribuyente localmente, guardada en `flutter_secure_storage` (doc 05).
5. **Verificación de identidad con ML on-device.** Permisos de **cámara + micrófono + captura
   de video**, `camerax`, `qr_code_scanner` y una librería nativa **TensorFlow Lite**
   (`libtensorflowlite_c.so`, 4.7 MB) sostienen el enrolamiento/liveness de **SAT ID** (doc 06).
6. **Endurecimiento de cliente débil.** **Sin certificate pinning**, **sin detección de
   root/Frida/emulador**, sin ofuscación del snapshot Dart (strings legibles), `allowBackup`
   ya está en `false` pero varios `FileProvider` de plugins Flutter usan rutas amplias. La
   superficie de análisis estático es alta comparada con CFE (docs 02, 04, 05).

## Alcance, propósito y límites

Análisis **original** (observaciones, tablas y notas de arquitectura/seguridad) de una app
pública del gobierno mexicano que se posee, con fines de investigación de seguridad y
documentación del ecosistema gob.mx. **No redistribuye** el APK, los `.so`, las fuentes, los
assets ni el contenido propiedad del SAT (ver `.gitignore`). Los fragmentos de configuración
(`.env`) y de código citados son mínimos y solo sustentan hallazgos. **No se accede a los
servidores del SAT, no se prueban credenciales ni se envía tráfico**: todo el análisis es
estático sobre el binario que se posee.

## Limitaciones

Observaciones estáticas; no establecen comportamiento en runtime. **Firma del APK no verificada**
por las herramientas (el bundle viene de APKPure, no de Google Play; el split base trae solo
`stamp-cert-sha256`, la firma v2/v3 vive en el bloque de firma y no se validó — ver doc 09).
La lógica de negocio está compilada a **Dart AOT nativo (ARM64)**: se recuperan endpoints,
nombres de paquetes, textos y constantes por `strings`, pero **no el flujo de control** — para
eso haría falta instrumentación dinámica (Frida sobre `libapp.so`) o decompilación AOT, fuera
del alcance de este trabajo. Los IDs (`13.85.190.35`, hosts `cloudb/clouda`) se reportan tal
como aparecen en el binario; su estado operativo no se comprobó.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
