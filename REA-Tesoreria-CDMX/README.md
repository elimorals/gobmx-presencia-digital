# Ingeniería inversa de Tesorería CDMX — índice

Análisis **estático** del APK `mx.wdd.sefin` (base del bundle XAPK) con REA
(apktool 3.x + lectura del bundle web de Capacitor) + build-tools de Android
(aapt2, apksigner). La app **nunca se ejecutó**. Es la app oficial de la
**Secretaría de Administración y Finanzas (SAF) del Gobierno de la Ciudad de
México** para pagos de tesorería (predial, agua, tenencia/control vehicular,
líneas de captura), **#22 por instalaciones** del inventario gob.mx (≈500 K+).

> Es una app **híbrida Capacitor/Ionic/Angular** (como Licencia Federal Digital):
> la lógica viaja en un bundle JS **legible** dentro de `assets/public/`. Eso
> expuso endpoints, el esquema de autenticación y **credenciales de pago
> embebidas en el cliente** — el hallazgo central del análisis.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila, módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (incl. `DUMP`, `RECORD_AUDIO`), `usesCleartextTraffic=true`, NSC con IP interna |
| 02 | [02-arquitectura-capacitor.md](02-arquitectura-capacitor.md) | Capacitor + Angular/Ionic, plugins, estructura del bundle |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | 27 endpoints: backend Laravel (siscorApis/srp), pasarelas, **endpoint pre-producción** |
| 04 | [04-autenticacion.md](04-autenticacion.md) | **Laravel Passport ROPC**, `client_secret` hardcodeado, biometría |
| 05 | [05-pagos-banorte-bbva.md](05-pagos-banorte-bbva.md) | **Credenciales de comercio Banorte en el bundle**, BBVA, adquira |
| 06 | [06-sdks-plugins.md](06-sdks-plugins.md) | Firebase/FCM, Pusher, MLKit barcode, plugins Capacitor |
| 07 | [07-firma-recursos.md](07-firma-recursos.md) | Firma **auténtica** (`O=cweb`), estructura XAPK |
| 08 | [08-hallazgos-seguridad.md](08-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `decompiled/endpoints.txt` | 27 endpoints extraídos del bundle JS |
| `AndroidManifest.xml` | Manifiesto decodificado |
| `hashes.sha256` | SHA-256 del XAPK y del APK base |
| `evidence/` | bundle web extraído, manifiesto apktool, NSC (NO versionado) |

> Lo propietario (APK/XAPK, `evidence/`, `decompiled/`) **no se versiona** (`.gitignore`).

## 5 hallazgos principales

1. **Credenciales de comercio Banorte embebidas en el cliente.** En
   `main.*.js`, la función `getOriginalChain()` arma la cadena de pago con
   `merchantId`, `name` y `password` **hardcodeados** en el bundle JS distribuido
   (valores en `evidence/`, no se reproducen aquí). Cualquiera que abra el APK los
   lee (doc 05, 08).
2. **`client_secret` de Laravel Passport hardcodeado.** El login usa OAuth2
   **ROPC** (`grant_type=password`) con `client_id:5` y un `client_secret` fijo en
   `main.*.js`. Un secreto de cliente en un binario distribuido no es secreto
   (doc 04, 08).
3. **Tráfico en claro habilitado.** `android:usesCleartextTraffic="true"` y el
   `network_security_config` permite **cleartext** al dominio de pagos
   `tesoreriacdmx.finanzas.cdmx.gob.mx`, a `sac.finanzas.cdmx.gob.mx` y a una
   **IP interna `10.1.181.71`** (doc 01, 08).
4. **Endpoint de pre-producción en el binario de producción.**
   `https://prepro.adquiracloud.mx/clb/endpoint/flapMexico` quedó embebido en el
   bundle (doc 03, 08).
5. **Permisos inusuales para una app de pagos.** Declara `DUMP` (normalmente
   `signature|privileged`, no se concede a apps de tienda) y `RECORD_AUDIO`,
   sin flujo que los justifique a la vista (doc 01, 08).

## Alcance y límites

Análisis **original** de una app pública del gobierno de la CDMX que se posee, con
fines de investigación de seguridad. **No redistribuye** el APK ni los secretos
(los valores completos quedan en `evidence/`, `.gitignore`). Los endpoints son
rutas presentes en el binario, no datos de usuarios. Observación **estática**: no
se ejecutó la app ni se llamó a ningún endpoint. **Firma**: certificado auténtico
del emisor (`O=cweb`), verifica — binario redistribuido por APKPure sin alterar la
firma (doc 07).

---
