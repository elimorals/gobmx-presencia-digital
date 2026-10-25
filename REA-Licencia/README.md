# REA-Licencia · Licencia Federal Digital (`mx.sct.licencia`)

Ingeniería inversa **estática** de la app oficial **Licencia Federal Digital** (SCT/SICT),
v1.0.49. La app nunca se ejecuta. Parte del proyecto `gob-mx-reverse-engineering`.

## TL;DR

App **híbrida Capacitor/Ionic/Angular** (la lógica es JS legible, sin hardening). Backend
**`wslfd.sct.gob.mx/api`**. Guarda la **licencia federal completa + CURP, RFC, email, password y
PIN** en una DB **SQLCipher local** cuya **llave es el PIN numérico del usuario** — y existe una
ruta que abre esa DB **sin llave**. Detalle en el doc 05.

## Índice

| Doc | Contenido |
|---|---|
| [00-RESUMEN](00-RESUMEN.md) | Identidad, pila, flujos, hallazgos, contraste con el pipeline |
| [01-manifiesto](01-manifiesto.md) | Package, versión, 13 permisos, componentes, deeplink |
| [02-arquitectura-capacitor](02-arquitectura-capacitor.md) | Pila híbrida, bundle, servicios Angular, puente nativo |
| [03-red-endpoints](03-red-endpoints.md) | WSLFD prod + QA, advanced-http, confianza TLS |
| [04-autenticacion-pin](04-autenticacion-pin.md) | Login, PIN, verificación por código |
| [05-cripto-sqlcipher-datos-locales](05-cripto-sqlcipher-datos-locales.md) | **SQLCipher, llave = PIN, ruta sin llave, PII** |
| [06-sdks-plugins](06-sdks-plugins.md) | Plugins Cordova, Capacitor, Firebase, barcodescanner |
| [07-recursos-web-esquema](07-recursos-web-esquema.md) | `assets/public`, `seed.sql`, tipos de licencia |
| [08-firma-recursos](08-firma-recursos.md) | Firma no verificada, hashes, limitaciones |

## Artefacto

| | |
|---|---|
| Package | `mx.sct.licencia` |
| Versión | 1.0.49 (versionCode 49) |
| APK | `@arm64-v8a`, 30,548,056 bytes |
| SHA-256 | `8216279428d74bf623056772168463a285f487a955c033f01b9f1dc1c45f83c4` |
| minSdk / targetSdk | 27 / 33 |

## Metodología

apktool 3.0.3 (smali + recursos + assets) y JADX headless vía REA para identidad/manifiesto;
el grueso del análisis es lectura directa del bundle JS en `assets/public/` (Angular minificado,
no ofuscado). Verificado el package real con dos herramientas **antes** de documentar.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
