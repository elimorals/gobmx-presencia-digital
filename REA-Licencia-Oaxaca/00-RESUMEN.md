# 00 · Resumen ejecutivo — Licencia Digital GobOax (Oaxaca)

## Identidad

| Campo | Valor |
|---|---|
| App | Licencia Digital GobOax (Oaxaca) |
| Package | `com.gobiernooaxaca.licenciadigital` (**verificado** con `inspect_android_package`) |
| versionName / code | 1.0.1 / 101 |
| minSdk / targetSdk | 20 / **33** |
| Firma | **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica**; cruzar contra Play oficial |
| sha256 cert (APKPure) | `e8370883a623205091df2a8cfaa1ef6d820e3ff6a0e475b9e2ea190a9af33b1e` |
| sha256 XAPK | ver `hashes.sha256` |
| Clases / recursos | 1 581 / 130 |
| Dueño | Gobierno de Oaxaca (SEMOVI) |

## Pila tecnológica

- **Flutter** (base con `assets/flutter_assets`, split `config.arm64_v8a` con `libapp.so` + `libflutter.so`).
  El Dart AOT en `libapp.so` es el muro de análisis estático.
- Plugins Flutter: `geolocator` (ubicación), `path_provider`, `shared_preferences`, `url_launcher`.
  **No** se detectó SDK de pago ni de biometría/facial en el lado Java (podría estar en Dart/WebView).
- Backend: **`https://oaxaca.validatulicencia.mx/`** — producto del vendor **"validatulicencia.mx"**
  (NO es `licenciamanejo.com` de Cosmocolor; NO es dominio `gob.mx`).

## Permisos

`INTERNET`, `ACCESS_FINE_LOCATION`, `ACCESS_NETWORK_STATE` — mínimos y coherentes.

## Veredicto de seguridad

**SANO en la postura observable.** Contraejemplo del defecto Cosmocolor: este es **otro vendor**
(validatulicencia.mx) y **no** comparte el trust-all.

- **TLS/cleartext — AUSENTE-SANO (CONFIRMADO):** sin `usesCleartextTraffic`, sin
  `networkSecurityConfig`, `targetSdk 33` (cleartext bloqueado por defecto), backend HTTPS.
- **Trust-all — AUSENTE-SANO (lado Java):** 0 coincidencias de
  `getUnsafeOkHttpClient`/`checkServerTrusted`/`ServiceGenerator`/`trustAllCerts` en el DEX. No es
  el defecto de Infonavit/Cosmocolor.
- **Trust-all en Dart — A-CONFIRMAR:** la validación TLS efectiva del cliente Flutter vive en
  `libapp.so` (AOT) y no es verificable estáticamente. Requiere dinámico (Frida/reflutter). No se
  afirma ni seguro ni inseguro en esa capa.
- **Secretos — AUSENTE-SANO:** sin `.env` en `flutter_assets` (solo Asset/FontManifest); sin
  endpoints/claves en claro salvo el dominio público del backend.

## Cruce con el mapa de proveedores

Amplía el **cruce de vendors de licencia de conducir**: Oaxaca usa **validatulicencia.mx**, un
proveedor distinto de Cosmocolor (EDOMEX/Jalisco/Chiapas), ZTK (Guanajuato) y licdig (Chihuahua).
**No hereda** el trust-all de Cosmocolor → el defecto multi-estado de Cosmocolor es **del producto
Cosmocolor**, no de "las apps de licencia" en general. Contraejemplo útil.
