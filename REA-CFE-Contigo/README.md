# Ingeniería inversa de CFE Contigo 7.0.0 — índice

Análisis **estático** del APK `mx.com.cfe.cfecontigo` (base del bundle XAPK) con REA
(JADX headless 0.7.1 + apktool). La aplicación **nunca se ejecutó**. Es la app oficial
pública de la Comisión Federal de Electricidad, la **#1 por instalaciones** del inventario
gob.mx (≈37.45 millones en Google Play).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila tecnológica, mapa de módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes exportados, deeplinks, superficie de ataque |
| 02 | [02-autenticacion.md](02-autenticacion.md) | OAuth2 (access/refresh token), OTP por correo, captcha, biometría, OIDC |
| 03 | [03-cripto-almacenamiento.md](03-cripto-almacenamiento.md) | AndroidKeyStore, AES-GCM/CBC, PBKDF2, `crmkey_pub.pem`, backup |
| 04 | [04-red-endpoints.md](04-red-endpoints.md) | OkHttp, Basic/Bearer, TLS, por qué no hay URLs en claro |
| 05 | [05-ofuscacion-proteccion.md](05-ofuscacion-proteccion.md) | **DexGuard string encryption**, `.so` empaquetada, anti-fraude |
| 06 | [06-pagos-tarjetas.md](06-pagos-tarjetas.md) | Tokenización de tarjetas, cargos recurrentes, pago con puntos |
| 07 | [07-sdks-terceros.md](07-sdks-terceros.md) | Firebase, Huawei HMS, AppAuth/OIDC, ZXing, LexisNexis ThreatMetrix |
| 08 | [08-pantallas-flujos.md](08-pantallas-flujos.md) | 31 Activities, trámites, canal de anticorrupción |
| 09 | [09-firma-recursos.md](09-firma-recursos.md) | Firma del APK, recursos, assets, estructura del XAPK |
| 10 | [10-analisis-dinamico.md](10-analisis-dinamico.md) | **Captura en dispositivo real**: backend `appcfecontigo.cfe.mx` |

## Artefactos

| Carpeta | Contenido |
|---|---|
| `decompiled/` | Volcados de clases/métodos de JADX (p.ej. `App.onCreate` cayó a smali) |
| `evidence/` | Capturas e insumos (no versionado) |
| `AndroidManifest.xml` | Manifiesto decodificado completo |
| `strings.json` | `res/values/strings.xml` (258 entradas) |
| `xapk-manifest.json` | `manifest.json` original del bundle XAPK |
| `hashes.sha256` | SHA-256 de los 4 APK del bundle |

> Los artefactos pesados y propietarios de CFE (APK, recursos decodificados por apktool,
> `.so`, `.bks`, `.pem`, DEX) **no se versionan** — ver `.gitignore`. Viven en el SSD en
> `/Volumes/PortableSSD/apps-gobmx-mx/`.

## 6 hallazgos principales

1. **Protección comercial fuerte, atípica en gob.mx.** El código tiene **cifrado de
   strings estilo DexGuard** (cada clase lleva `$$a: byte[]` + `$$c(int,byte,short):String`
   que descifra literales en runtime), la librería nativa `libb1bd.so` está **empaquetada**
   (entropía ≈1.000, sin section headers ni símbolos, 0 strings útiles) y embebe el SDK
   anti-fraude **LexisNexis ThreatMetrix**. Por eso no hay endpoints en claro (ver docs 04, 05).
   El análisis **dinámico** (doc 10) sorteó esto: el backend real es
   **`https://appcfecontigo.cfe.mx`** (todo por HTTPS, sin cleartext).
2. **App financiera real, no solo consulta.** Tokeniza tarjetas (guarda `token`/`bin`/`last4`,
   nunca el PAN completo), soporta **cargos recurrentes** y **pago con puntos**, y agenda
   citas en centros CAC. Autenticación **OAuth2** con `access_token`/`refresh_token` y
   `mustChangePassword`, más **OTP por correo** y captcha propio (doc 02, 06).
3. **Doble distribución Google + Huawei.** Stack de push y servicios duplicado: Firebase
   (Auth, Analytics, Crashlytics, FCM, In-App Messaging) **y** Huawei Mobile Services
   (HMS Push, AGConnect, GRS) — la app se publica también en AppGallery (doc 07).
4. **FileProvider con `files-path "/"`.** El `provider` interno expone toda la carpeta
   `files/` de la app (y `external-path`/`cache-path` en `.`). Mala práctica igual que
   la observada en jw-library (doc 01, 03).
5. **Higiene de red correcta en el manifiesto.** `usesCleartextTraffic="false"`,
   `allowBackup="false"`, sin `networkSecurityConfig` propio (usa el default de Android 14+
   que bloquea cleartext). No se detectaron dominios dev/QA en claro (doc 04).
6. **Credenciales de cliente Firebase embebidas** (API key Android, app id, database URL
   `cfe-contigo.firebaseio.com`). Son claves de cliente restringibles por huella, no
   secretos de servidor, pero conviene verificar sus restricciones en GCP (doc 03, 07).

## Alcance, propósito y límites

Análisis **original** (observaciones, tablas y notas de arquitectura/seguridad) de una app
pública del gobierno mexicano que se posee, con fines de investigación de seguridad y
documentación del ecosistema gob.mx. **No redistribuye** el APK, los recursos, los assets
ni el contenido propiedad de CFE (ver `.gitignore`). Los fragmentos de metadatos de código
incluidos son mínimos y solo sustentan hallazgos. No se evaden protecciones ni se accede a
datos de usuarios; todo el análisis es estático sobre el binario.

## Limitaciones

Observaciones estáticas; no establecen comportamiento en runtime. **Firma del APK no
verificada** por el motor (el binario viene de APKPure, no de Google Play — ver doc 09 y la
advertencia de procedencia). El cifrado de strings de DexGuard y el empaquetado nativo
impiden recuperar endpoints, claves y lógica de red por análisis estático puro; confirmarlos
requeriría instrumentación dinámica sobre un dispositivo propio, fuera del alcance de este
trabajo.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
