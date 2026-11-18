# 00 · Resumen ejecutivo — 911 Móvil BC

## Identidad

| Campo | Valor |
|---|---|
| App | 911 Móvil BC |
| Package | `com.c4bc.alerta066m` |
| versionName / versionCode | 7.0.4 / 154 |
| minSdk / targetSdk | 24 / 36 |
| Firma | **Auténtica** — `O=C4, L=Mexicali, ST=Baja California` (C4 de Baja California, emisor real) |
| SHA-256 cert | `4a:91:de:53:af:3f:01:0d:d8:1d:97:16:51:9a:9c:03:c9:21:ea:f9:4f:ab:ba:b7:65:65:dc:4e:55:75:c8:de` |
| sha256 APK | `bea0c561d58b6d309568c0e8bad080aeab7001a669689f0d9facf4d204e70833` |
| Clases | 19 291 |
| Dueño | C4 Baja California (Centro de Control, Comando, Comunicación y Cómputo) |

Package **verificado con `inspect_android_package`** antes de documentar: coincide
`com.c4bc.alerta066m`. Firma auténtica del C4 BC (NO re-firmada por la tienda).

> Nota de adquisición: la primera descarga llegó **truncada** (34 MB, zip sin end-of-central-
> directory); se re-descargó íntegra (123 MB) y sobre esa se analizó. Revisar truncados en
> hermanas del lote 4.

## Pila tecnológica

- **React Native + Expo (Hermes).** La lógica vive en `assets/index.android.bundle`
  (**Hermes bytecode v96** — bytecode compilado, muro para leer la lógica JS; los strings sí se
  extraen). Libs: `libhermestooling.so`, `libexpo-modules-core.so`, `libfbjni.so`,
  `libgesturehandler.so`, ML Kit barcode (`libbarhopper_v3.so`).
- Config de entorno en `assets/app.config` (Expo).
- Deeplink Expo: `exp+911movilbc://`; también esquema `https`.

## Mapa de módulos (features inferidos por permisos/endpoints)

Alerta de auxilio 911 (066 histórico), ubicación en tiempo real (background), grabación de audio,
cámara, notificaciones push (FCM), biometría. App de **pánico/emergencias** ciudadana de BC.

## Hallazgos (tabla)

| # | Hallazgo | Clase | Severidad | Estado |
|---|----------|-------|-----------|--------|
| H1 | **Cleartext HTTP permitido** a 4 dominios de backend (incl. uno **dev**) vía NSC | CWE-319 | **ALTA** | **CONFIRMADO** |
| H2 | **Subdominio de desarrollo en producción** (`tijuana.c4bcdev.gob.mx`) empacado y whitelisteado | CWE-489 | MEDIA | **CONFIRMADO** |
| H3 | TLS trust-all (`getUnsafeOkHttpClient`/trust-all) | CWE-295 | — | **AUSENTE-SANO** (RN; no se halló; sin trust user-CA en NSC) |

Detalle en [03-tls-cleartext.md](03-tls-cleartext.md) y [04-hallazgos.md](04-hallazgos.md).

## Comparación con 911-CDMX

Misma **clase** que 911-CDMX (CWE-319 cleartext en app de emergencias) pero **menos grave**: aquí
el cleartext está acotado por NSC a 4 dominios (no `usesCleartextTraffic=true` global), no se
hallaron 30+ endpoints `http://` ni IP interna ni secreto de Twitter. El muro Hermes impide
confirmar si el tráfico vivo usa http o https; lo CONFIRMADO es que el NSC **lo permite**.
