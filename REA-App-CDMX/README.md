# Ingeniería inversa de App CDMX 4.5.2 — índice

Análisis **estático** del APK base de `mx.gob.cdmx.adip.apps` (bundle XAPK) con REA
(JADX headless + apktool), más una **capa dinámica** (doc 11): la app se instaló desde Google
Play y se ejecutó en un dispositivo real (Android 16) para documentar UI, deeplinks y el
license check de PairIP. Es la **super-app oficial
del Gobierno de la Ciudad de México**, desarrollada por la ADIP (Agencia Digital de
Innovación Pública). Agrupa ~24 servicios ciudadanos en un solo binario: pagos, Mi Taxi,
movilidad integrada (tarjeta de transporte por NFC), Llave CDMX (identidad única), botón
de pánico, Clínica Condesa, LOCATEL, documentos digitales y más.

## Documentos

| # | Archivo | Tema |
|---|---------|------|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila tecnológica, mapa de módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | 26 permisos, 151 componentes, 13 exportados, deeplinks, FileProvider, widgets |
| 02 | [02-modulos-servicios.md](02-modulos-servicios.md) | Los ~24 servicios de la super-app y su reparto por paquete |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | API `apiapp.cdmx.gob.mx/v4`, Mercado Pago, Llave, Mundial 2026, otros hosts |
| 04 | [04-autenticacion-llave.md](04-autenticacion-llave.md) | Llave CDMX (SSO ciudadano), biometría, almacenamiento de sesión |
| 05 | [05-nfc-calypso-movilidad.md](05-nfc-calypso-movilidad.md) | **Spirtech Calypso HCE**: emulación de tarjeta de transporte por NFC |
| 06 | [06-pagos-mercadopago.md](06-pagos-mercadopago.md) | Pagos, Mercado Pago, recarga de tarjeta Mi, SafPayments |
| 07 | [07-proteccion-pairip.md](07-proteccion-pairip.md) | **Google PairIP** (anti-tamper), librerías nativas, cert pinning |
| 08 | [08-sdks-terceros.md](08-sdks-terceros.md) | Firebase, Retrofit/OkHttp, Maps, ML Kit, Glide, Tink, Lottie |
| 09 | [09-firma-recursos.md](09-firma-recursos.md) | Firma no verificada, estructura del bundle, hashes |
| 10 | [10-recursos-decodificados.md](10-recursos-decodificados.md) | **Decode completo (apktool)**: FileProvider acotado, APDU, assets PairIP, WebView, datasets |
| 11 | [11-runtime-ui.md](11-runtime-ui.md) | **Dinámico**: app ejecutada (Android 16), license check PairIP, home, deeplinks, Movilidad |

## Artefactos

| Archivo / carpeta | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado completo (1,137 líneas) |
| `xapk-manifest.json` | `manifest.json` original del bundle XAPK |
| `hashes.sha256` | SHA-256 de los 20 APK del bundle |
| `evidence/endpoints.txt` | 54 URLs extraídas de los DEX (no versionado) |
| `res-decoded/xml/` | XML decodificados con apktool (FileProvider, APDU, widgets) (no versionado) |
| `decompiled/LlavePreferencesManager.java.md` | Decompilado JADX de la clase de sesión Llave |
| `decompiled/pagos-dtos.md` | Decompilado del módulo `.pagos` (sin datos de tarjeta) |

> Los artefactos pesados y propietarios (APK, DEX, `.so`, `resources.arsc`, `ca.cert.der`)
> **no se versionan** — ver `.gitignore`. Viven en el SSD en
> `/Volumes/PortableSSD/apps-gobmx-mx/extraidos/mx.gob.cdmx.adip.apps/`.

## 6 hallazgos principales

1. **Super-app de 24 servicios en un binario.** A diferencia de CFE Contigo (un solo
   dominio: energía), App CDMX concentra el ecosistema digital completo de la CDMX:
   `mx.gob.cdmx.adip.{pagos, mitaxi, movilidadintegrada, seguridad, clinicacondesa,
   documentosdigitales, llave, botonpanico, locatel, denunciadigital, calidadaire,
   anticorrupcion, …}`. 105 Activities, 21 Services, 20 Receivers (doc 02).
2. **Endpoints en claro — API monolítica CodeIgniter.** Toda la red cuelga de
   `https://apiapp.cdmx.gob.mx/index.php/v4/` con ~25 sub-servicios enrutados por
   `index.php` (patrón CodeIgniter). PairIP **no cifra strings**, así que la superficie
   de API queda completamente visible (doc 03). Contraste directo con el DexGuard de CFE.
3. **Tarjeta de transporte por NFC (Calypso HCE).** Embebe el SDK **Spirtech Calypso**
   (`com.spirtech.android.hce.calypso.*` + `libcalypsoHceNatives.so` + `apduservice.xml`):
   el teléfono **emula una tarjeta Calypso** de Movilidad Integrada por Host Card
   Emulation. Es la función técnicamente más compleja de la app (doc 05).
4. **Protegida con Google PairIP, no DexGuard.** La `Application` es
   `com.pairip.application.Application` y trae `libpairipcore.so` (599 KB): el hardening
   anti-tamper de Google Play (Automatic Integrity Protection). Protege integridad y flujo,
   pero **deja los literales de string legibles** (doc 07).
5. **Llave CDMX como identidad única.** Autenticación centralizada vía Llave CDMX
   (`mx.gob.cdmx.adip.llave`, `llave.cdmx.gob.mx`), con sesión en `LlavePreferencesManager`
   y biometría (`USE_BIOMETRIC`). Varios módulos exigen Llave para operar (doc 04).
6. **Pagos reales con Mercado Pago.** Integra Mercado Pago
   (`api.mercadopago.com/semovi/appcdmx/v1/transactions/confirm`, `/MP/Payment`,
   `/MP/Challenge`) para recarga de tarjeta Mi y trámites de pago. Credenciales de
   cliente Firebase embebidas (proyecto `app-cdmx---prod`, doc 06, 08).

## Hallazgos del decode profundo (apktool + decompilado, doc 10)

7. **FileProvider correctamente acotado.** `file_paths.xml` e `image_share_filepaths.xml`
   limitan el acceso a `Pictures/`, `Download/`, `images/`, `image_provider/`. **No** repite
   el `files-path "/"` abierto de jw-library y CFE — punto a favor de App CDMX.
8. **Sesión de Llave CDMX sin cifrar.** `LlavePreferencesManager` guarda el blob de sesión
   (`KEY_USER_INFO`, con `idLlave` y nombre) en **SharedPreferences planas**
   (`prefs.putString`), sin EncryptedSharedPreferences ni Tink (que sí está en el binario).
   Mitigado por `allowBackup=false`; expuesto en root/forense (doc 04, `decompiled/`).
9. **22 blobs cifrados de PairIP + WebView de rutas + datasets offline.** Assets con nombres
   aleatorios (~150 KB) = código protegido de PairIP; `assets/web/` es la app de mapas de
   Movilidad Integrada; `res/raw` embarca Metro/Metrobús/Cablebús/ciclovías en GeoJSON.
10. **HCE sin desbloqueo.** `apduservice.xml` usa `requireDeviceUnlock="false"` y AIDs
    dinámicos: la tarjeta de transporte responde con el teléfono bloqueado (UX tap-and-go).
11. **Pagos sin datos de tarjeta en el cliente (confirmado).** Decompilado el módulo `.pagos`
    (33 DTOs): `FormPasarela` solo lleva referencias de cobro (línea de captura, cuenta,
    adeudos), **ningún campo de tarjeta**; el pago se delega a la web de Mercado Pago
    (`getPaymentUrl` + `CheckoutBrickFragment`). Sin SDK nativo de MP. PCI-friendly (doc 06).

## Permisos de alto riesgo (vs. CFE)

`ACCESS_BACKGROUND_LOCATION` + `FOREGROUND_SERVICE_LOCATION` (seguimiento de ubicación en
segundo plano — Mi Taxi / botón de pánico), `NFC` (tarjeta de transporte), `CAMERA`
(QR/ML Kit), `WRITE_CALENDAR`, `WRITE_EXTERNAL_STORAGE` + `requestLegacyExternalStorage`,
`SET_ALARM`, `ACCESS_ADSERVICES_*` (atribución de anuncios).

## Alcance, propósito y límites

Análisis **original** (observaciones, tablas y notas de arquitectura/seguridad) de una app
pública del gobierno de la CDMX que se posee, con fines de investigación de seguridad y
documentación del ecosistema gob.mx. **No redistribuye** el APK, los recursos ni los assets
(ver `.gitignore`). Los fragmentos de metadatos de código incluidos son mínimos y solo
sustentan hallazgos. No se evaden protecciones ni se accede a datos de usuarios; todo el
análisis es estático sobre el binario.

## Limitaciones

Observaciones estáticas; no establecen comportamiento en runtime. **Firma del APK no
verificada** por el motor (el binario viene del SSD, no de Google Play directo — ver doc 09).
PairIP protege integridad y flujo de control en runtime, por lo que el comportamiento real
de las rutas protegidas y la lógica del HCE Calypso solo se confirmarían con instrumentación
dinámica sobre un dispositivo propio, fuera del alcance de este trabajo.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
