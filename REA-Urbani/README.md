# Ingeniería inversa de Urbani 3.1.5 — índice

Análisis **estático** del App Bundle `com.miruta.mty` con REA (JADX headless 0.7.1 + apktool 3.0.3)
y análisis binario propio. La aplicación **nunca se ejecutó**. **Urbani** (antes/también *MiRuta*) es
la app de **transporte público del área metropolitana de Monterrey, Nuevo León** — una app de
**gobierno municipal**, no federal (no es gob.mx de la Federación).

> Fecha: 2026-10-24. Artefacto en el SSD `/Volumes/PortableSSD/apps-gobmx-mx/extraidos/com.miruta.mty/`.
> Paquete verificado con `inspect_android_package` **antes** de documentar: `com.miruta.mty`,
> Urbani, 3.1.5 (vc 430) — confirmado que **no** es un artefacto mal nombrado.
> Firma v2+v3 presente pero con certificado **genérico `CN=Android`** → APK **re-firmado**
> (mirror/APKPure); autenticidad del publicador original **no verificable** (doc 09).

## Framework y protección (lo que define este análisis)

Es una app **Flutter**: la lógica de negocio está en **Dart** dentro de `libapp.so`. Ese binario
está **cifrado por el packer chino Baidu (LianTian / Baidu Protect)** — entropía 7.995 bits/byte, 0
secciones de snapshot Dart. Estáticamente los endpoints no son legibles; el **pase dinámico**
(doc 11, dispositivo real sin root) los **recuperó del logcat** (la app loguea en release):
backend **`urbani.io`** (operador privado), server-driven UI y feature flags. Lo que **sigue
bloqueado** (requiere root): cripto de la tarjeta, cuerpos de endpoints autenticados y el volcado del
Dart descifrado. El estático mapea: manifest, permisos, componentes, SDKs (DEX y `.so`), config
Firebase/Google, red, NFC y firma.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila tecnológica, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, deeplink `urbani://`, superficie |
| 02 | [02-framework-flutter.md](02-framework-flutter.md) | Flutter, bundle con splits, Dart AOT, build no determinado |
| 03 | [03-packer-baidu.md](03-packer-baidu.md) | **Baidu LianTian / Protect**: cifrado del AOT, evidencia de entropía |
| 04 | [04-red-seguridad.md](04-red-seguridad.md) | network-security-config, cleartext, pinning, endpoints no recuperados |
| 05 | [05-nfc-hce-movilidad.md](05-nfc-hce-movilidad.md) | Tarjeta NFC HCE, AIDs, validadores RD300 |
| 06 | [06-kyc-biometria.md](06-kyc-biometria.md) | Doble KYC: Baidu Face + Veriff + biometría local |
| 07 | [07-firebase-google.md](07-firebase-google.md) | Firebase, Google Pay, claves Google (redactadas) |
| 08 | [08-sdks-terceros.md](08-sdks-terceros.md) | Inventario de `.so` y plugins/SDKs |
| 09 | [09-firma-recursos.md](09-firma-recursos.md) | Firma, re-empaquetado, estructura, recursos, FileProvider |
| 10 | [10-hallazgos-seguridad.md](10-hallazgos-seguridad.md) | 8 hallazgos estáticos + 4 dinámicos + controles positivos |
| 11 | [11-analisis-dinamico.md](11-analisis-dinamico.md) | **Dispositivo real (Android 16, sin root): backend `urbani.io` recuperado del logcat, arquitectura, superficie exportada** |

## Artefactos

| Carpeta / archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado completo (Maps API key redactada) |
| `bundle-manifest.json` | `manifest.json` original del bundle (3 APK) |
| `hashes.sha256` | SHA-256 de los 3 APK + `libapp.so` / `libflutter.so` |
| `res-decoded/` | Recursos decodificados por apktool (`--no-src`), incl. XML de seguridad |
| `evidence/` | `apksigner-verify.txt`, `libapp-entropia.txt`, `logcat-arranque/auth/deeplink.txt`, `endpoints-urbani.txt`, `config-city-MTY.json.txt`, `pantalla-arranque.png` |
| `decompiled/` | smali del base (apktool); **bajo valor**: la lógica es Dart cifrado, no smali |

> Los artefactos pesados y propietarios (APK, `.so`, recursos, smali) **no se versionan**
> (`.gitignore`). Viven en el SSD.

## 8 hallazgos principales

1. **Lógica Dart cifrada por packer chino Baidu** (`libapp.so` entropía 7.995, 0 snapshot). Endpoints/cripto no recuperables estáticamente. (doc 03)
2. **APK re-firmado** con cert genérico `CN=Android` → autenticidad del publicador original no verificable. (doc 09)
3. **Doble KYC biométrico** (Baidu Face + Veriff) en app de gobierno: datos faciales procesados por SDK chino → riesgo de privacidad/soberanía. (doc 06)
4. **Dos API keys Google en claro** (Maps + Firebase); restricción no verificada, agravada por el re-firmado. (doc 07)
5. **Tarjeta NFC HCE** (`requireDeviceUnlock="false"`) ante validadores RD300; cripto del APDU no recuperada. (doc 05)
6. **FileProvider** `cache-path path="/"` (todo el caché). (doc 09)
7. **Cleartext bloqueado** en release (positivo), pero **sin cert pinning** de plataforma. (doc 04)
8. **Solo `armeabi-v7a`** en la fuente; `libed66.so` de función no determinada. (docs 02, 03)

## Alcance, propósito y límites

Análisis **original** de una app pública de gobierno **municipal** que se posee, con fines de
investigación de seguridad y documentación del ecosistema de apps de gobierno en México. **No se
redistribuye** el APK, los `.so`, los recursos ni los assets (ver `.gitignore`). Los fragmentos de
metadatos incluidos son mínimos y solo sustentan hallazgos; las API keys se **redactan**. **No se
evaden protecciones** ni se accede a datos de usuarios: todo es estático sobre el binario que se
posee. El límite duro del pase es el **packer Baidu**, que cifra la lógica Dart; superarlo exigiría
un pase dinámico con root + Frida (fuera de alcance, doc 10).

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
