# 00 · Resumen ejecutivo — Botón de Auxilio (Escudo Urbano, Jalisco)

## Identidad

| Campo | Valor |
|---|---|
| App | Botón de Auxilio (Escudo Urbano) |
| Package | `com.c5.boton_auxilio_jalisco_flutter` |
| versionName / versionCode | 2.1.7 / 39 |
| minSdk / targetSdk | 25 / 36 |
| Firma | **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica** |
| SHA-256 cert | `9625a0f416a4335c0f601faf5adbf1450e72373aece16a4ee6569d84fe325edb` |
| sha256 XAPK | `ac51c6ad1f0ec5a4e12eef41adc3551b188423096c576d2dc71d4eb603bc3953` |
| sha256 APK base | `de1977d42cdcd0d4398e921f768085ccc763fe41be64cf1e89f558038e774413` |
| Clases / recursos | 7 987 / 425 |
| Dueño | C5 Jalisco (Escudo Urbano) — Gobierno de Jalisco |

Package **verificado con `inspect_android_package`** antes de documentar: coincide con
`com.c5.boton_auxilio_jalisco_flutter`.

> **Proveniencia:** el APK se adquirió re-firmado por APKPure (firma genérica de Google Inc.), así
> que la firma **no** prueba autenticidad. El bytecode/assets no cambian al re-firmar, por lo que el
> análisis de código es válido; para autenticidad, cruzar contra el canal oficial del C5 Jalisco.

## Pila tecnológica

- **Flutter** — `libflutter.so` + `libapp.so` (Dart AOT) en el split `config.arm64_v8a`, más
  `libdatastore_shared_counter.so`. 7 987 clases → además del motor Flutter hay muchos plugins
  Java/Kotlin (Firebase, GMS).
- **Backend:** proyecto **Firebase/GCP** `boton-de-auxilio-escudo-urbano.appspot.com` (string en
  `libapp.so`). Firebase API key (`AIzaSy…`) presente en recursos.
- **Muro de análisis estático:** el Dart AOT en `libapp.so` oculta la lógica real (envío de la
  alerta, uso de `APP_KEY`, descifrado de los endpoints). Lo que sigue se confirmó hasta donde el
  estático alcanza; lo no confirmable se marca explícitamente.

## Permisos sensibles

`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `READ_PHONE_STATE`, `READ_CONTACTS`, `INTERNET`,
`VIBRATE`, `WAKE_LOCK`, `ACCESS_NETWORK/WIFI_STATE`, `AD_ID` + `ACCESS_ADSERVICES_*`.
**Sin** ubicación en segundo plano, **sin** SMS, **sin** audio. 2 componentes exportados (normal).

## Hallazgos (tabla)

| # | Hallazgo | Clase | Severidad | Estado |
|---|----------|-------|-----------|--------|
| 1 | `.env` empaca `APP_KEY` (217 chars, secreto) junto a los endpoints API cifrados | CWE-312/798 | **ALTA** | **A-CONFIRMAR** (muro Dart) |
| 2 | Endpoints de **prueba** empacados en producción (`*_PRUEBAS` + flag `PRUEBAS`) | CWE-489 | MEDIA | CONFIRMADO |
| 3 | Firebase API key (`AIzaSy…`) en recursos | CWE-312 | BAJA | CONFIRMADO |
| 4 | TLS/cleartext **sano** (sin `usesCleartextTraffic`, targetSdk 36, sin NSC permisivo) | — | — | **AUSENTE-SANO** |

**Cruce con el mapa (`analisis/MAPA-PROVEEDORES-SDK.md`):** **NO** repite el cleartext/TLS-off de
911-CDMX — en el eje TLS esta app está sana. El patrón que sí comparte es el **`.env` en
`flutter_assets`** (como Guía Pemex), aquí con una diferencia a favor: los endpoints **no** están en
claro (van cifrados); el problema es el secreto `APP_KEY` viajando en el cliente.

Detalle: [01-manifiesto.md](01-manifiesto.md) · [02-red-firebase.md](02-red-firebase.md) ·
[03-env-secretos.md](03-env-secretos.md) · [04-tls-cleartext.md](04-tls-cleartext.md) ·
[05-hallazgos.md](05-hallazgos.md).
