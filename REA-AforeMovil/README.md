# Ingeniería inversa de AforeMóvil 6.0.15 — índice

Análisis **estático** del APK base del bundle XAPK `mx.com.procesar.aforemovil` con REA
(JADX headless 0.7.1 + apktool 3.0.3). La aplicación **nunca se ejecutó**. Es la app oficial
pública de **Procesar / CONSAR** para consultar y mover la cuenta individual del **SAR**
(Sistema de Ahorro para el Retiro): la app de AFORE del gobierno mexicano con la que un
trabajador administra su ahorro para el retiro desde el teléfono.

> Fecha: 2026-10-24. Artefacto en el SSD `/Volumes/PortableSSD/apps-gobmx-mx/`.
> Firma verificada (v2+v3, `O=procesar`) con **Source Stamp de Google Play** → artefacto
> auténtico distribuido por Play (ver doc 09).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila tecnológica, mapa de módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes exportados, feature modules, superficie de ataque |
| 02 | [02-autenticacion-sesion.md](02-autenticacion-sesion.md) | Login SAR token, NIP, FIDO/UAF (Daon), GestoPago, sesión (timeout/expiración) |
| 03 | [03-biometria-identidad.md](03-biometria-identidad.md) | Daon xFace (liveness pasivo), Sensory voz, ML Kit OCR, captura de documento (INE/IFE) |
| 04 | [04-cripto-almacenamiento.md](04-cripto-almacenamiento.md) | AndroidKeyStore / Daon SecureKeyStore (hardware), Realm, DataStore, backup |
| 05 | [05-red-endpoints.md](05-red-endpoints.md) | `api.esar.io`, TrustX, Firebase, enlaces gob.mx; sin pinning; Dynatrace |
| 06 | [06-ofuscacion-proteccion.md](06-ofuscacion-proteccion.md) | R8, ocultamiento de claves nativo, `toolChecker` (anti-tamper), empaquetado |
| 07 | [07-operaciones-afore.md](07-operaciones-afore.md) | Los 27 módulos de negocio: retiros, traspasos, ahorro, NIP, citas, metas |
| 08 | [08-sdks-terceros.md](08-sdks-terceros.md) | Daon IdentityX, Sensory, Firebase, Dynatrace, Maps, Dexter, Play Core, ML Kit |
| 09 | [09-recursos-firma-estructura.md](09-recursos-firma-estructura.md) | Firma, Source Stamp, estructura XAPK, librerías nativas, recursos |
| 10 | [10-analisis-dinamico.md](10-analisis-dinamico.md) | Dispositivo real (Android 16, sin root): superficie exportada en runtime |

## Artefactos

| Carpeta / archivo | Contenido |
|---|---|
| `decompiled/` | Volcados de métodos de JADX: `BaseApp.onCreate`, `RealmConfigs.initRealmConfigs`, `FacialActivity.onCreate`, `IdxFacialCaptureActivity.onCreate` |
| `evidence/` | Capturas e insumos (no versionado) |
| `AndroidManifest.xml` | Manifiesto decodificado completo (Maps API key redactada) |
| `xapk-manifest.json` | `manifest.json` original del bundle XAPK |
| `hashes.sha256` | SHA-256 de los 20 APK del bundle (base + 19 splits) |

> Los artefactos pesados y propietarios de Procesar (APK, recursos decodificados por apktool,
> `.so`, DEX, imágenes) **no se versionan** — ver `.gitignore`. Viven en el SSD en
> `/Volumes/PortableSSD/apps-gobmx-mx/`.

## 6 hallazgos principales

1. **Doble biometría de identidad, no solo huella.** La app integra **Daon IdentityX/xFace**
   (detección facial, **liveness pasivo**, calidad, detector de mascarilla, captura de documento)
   y **Sensory** (reconocimiento de voz), además de **ML Kit** (OCR + barcode) para leer la
   credencial INE. El alta/recuperación hace prueba de vida facial + lectura de identificación.
   Nueve `.so` de Daon y claves ocultas en nativo (`libkeys.so`, `libidx_facial_keys.so`).
   Ver docs 03, 06, 08.
2. **App financiera transaccional del SAR.** No es consulta: ejecuta **traspasos**, **retiros**
   (desempleo, matrimonio, ahorro voluntario), **ahorro** (en línea, efectivo, domiciliación,
   con beneficios), **NIP** (activación/recuperación), **citas** y **metas**. Autenticación por
   **SAR token** contra `api.esar.io/sartoken/` y autorización biométrica de operaciones
   (**GestoPago** vía FIDO/UAF). Ver docs 02, 07.
3. **Endpoints en recursos, no en código.** La base de API vive en `res/values/strings.xml`
   (`server_url=https://api.esar.io:443/aforemovil/`, login `api.esar.io/sartoken/`), no como
   const-string en el DEX. El backend es **Procesar/e-SAR** (`esar.io`). Analítica biométrica
   a **TrustX** (nube de Daon, `mobilemetrics.*.trustx*.com`). Ver doc 05.
4. **Higiene de datos en reposo ejemplar.** `allowBackup="false"`, `data_extraction_rules` y
   `backup_rules` excluyen **todo** (sharedpref, database, file, root, external) de respaldo y
   transferencia de dispositivo; FileProvider expone únicamente `cache/images/`. Mejor que
   jw-library y CFE (que exponían `root-path "/"`). Ver docs 01, 04.
5. **Sin certificate pinning.** `network_security_config.xml` solo tiene `<debug-overrides>`
   (confía CA de usuario en builds depurables); **no hay `<pin-set>`** ni `okhttp3.CertificatePinner`
   en el código. En release se usa el almacén de CA del sistema sin fijación. MITM con una CA de
   sistema en un dispositivo propio/rooteado sería viable. Ver doc 05.
6. **Componentes sensibles exportados, pero fallan cerradas.** `FacialActivity` e
   `IdxFacialCaptureActivity` (captura facial/identidad), `HomeActivity` y `PreexistenceActivity`
   están `exported="true"` y sus `onCreate` no validan sesión local. **Verificado en dispositivo
   real** (doc 10): lanzadas en frío desde fuera, fallan cerradas — `FacialActivity` crashea por
   falta de parámetros, `IdxFacialCaptureActivity` exige PIN/huella (keystore Daon), `Home` queda
   vacía y `Preexistence` muestra diálogo de incompatibilidad. Explotabilidad por intents ingenuos
   baja; un intent con los parámetros correctos queda como vector abierto. Ver docs 01, 02, 03, 10.

## Alcance, propósito y límites

Análisis **original** (observaciones, tablas y notas de arquitectura/seguridad) de una app
pública del gobierno mexicano que se posee, con fines de investigación de seguridad y de
documentación del ecosistema gob.mx. **No redistribuye** el APK, los recursos, los assets ni el
contenido propiedad de Procesar/CONSAR (ver `.gitignore`). Los fragmentos de metadatos de código
incluidos son mínimos y solo sustentan hallazgos. No se evaden protecciones ni se accede a datos
de usuarios; todo el análisis es estático sobre el binario que se posee.

## Limitaciones

Observaciones estáticas; no establecen comportamiento en runtime. La **base de API real** del
backend (`api.esar.io`) y el host de TrustX se recuperaron de `strings.xml`, y un segundo pase
estático resolvió: **Realm cifrado** en ambas BD, **token Bearer+refresh** (docs 02, 04) y la
**ausencia de guard de sesión** en las activities faciales (docs 01, 03). El pase dinámico en dispositivo real
(Android 16, **sin root**, doc 10) confirmó que la **superficie exportada falla cerrada**. Quedan
abiertos, bloqueados por la falta de root: la **procedencia de la llave de Realm**
(`realmEncryptionKey()` no se ubicó en su clase), si el **refresh token** se guarda cifrado o en
DataStore en claro, y los secretos de cliente en `libkeys.so` — todos exigen leer el sandbox de la
app o instrumentar con Frida (que además chocaría con el anti-tamper de Daon). Los nombres
ofuscados por R8 (clases de una letra como `V`, `W8.l`) se
interpretan por símbolos y strings conservados.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
