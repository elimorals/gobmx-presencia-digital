# 00 · Resumen ejecutivo — Licencia Digital Tlaxcala

## Identidad

| Campo | Valor |
|---|---|
| App | Licencia Digital TLX |
| Package | `com.dcsoluciones.licenciadigitalglobal` |
| versionName / versionCode | 1.3 / 26 |
| minSdk / targetSdk | 23 / 35 |
| Firma | **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica**; cruzar con el canal oficial de SMYT Tlaxcala |
| sha256 XAPK | `93a3f14d0f3b0377b6fd09920fcf1f8040ae84665d01d1a2b1705f67ffaea4ca` |
| sha256 base APK | `e4dac376c02c9895dfb70b02e576ce6956081d5a80d24b9b0f9cbc4fd294bbf1` |
| Clases / recursos | 2 655 / 370 |
| Dueño | SMYT Tlaxcala (Secretaría de Movilidad y Transporte) · **vendor DC Soluciones** |

Package **verificado con `inspect_android_package` antes de documentar** (coincide).

## Pila tecnológica

- **Capacitor + Quasar/Vue** (híbrida). El `webDir` es `dist/spa`; el bundle JS vive en
  `assets/public/` y es **legible (no ofuscado)** — a diferencia de Cosmocolor (nativo Retrofit).
- Plugins Capacitor: `@capacitor/browser`, `filesystem`, `share`, `splash-screen`. **Sin** plugin de
  cámara/geolocalización nativo.
- Realtime: **Pusher**. Pago: redirección a URL de pasarela (**OpenPay**). Referencia a **CoDi** (Banxico).

## Resultado del cruce de proveedores de licencia

**DC Soluciones (Tlaxcala) NO comparte el defecto de Cosmocolor.** Arquitectura distinta (web
Capacitor vs nativo), **API principal por HTTPS a `gob.mx`**, sin trust-all. El defecto TLS
multi-estado de Cosmocolor **no generaliza** a todos los vendors de licencia; este es comparativamente
sano. (Ver `analisis/MAPA-PROVEEDORES-SDK.md`.)

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | Capacitor `cleartext:true` + `allowMixedContent:true` global | CWE-319 (enabler) | **MEDIA** | CONFIRMADO (config); impacto A-CONFIRMAR (API principal es HTTPS) |
| H2 | `allowNavigation` a `prisma-apps.servehttp.com` (**DNS dinámico No-IP/servehttp**) | CWE-1327 (higiene/proveniencia) | BAJA | CONFIRMADO en allowlist; **no** usado en el JS actual (residuo) |
| H3 | Trust-all / WebView SSL bypass | CWE-295 | — | **AUSENTE-SANO** (símbolos de Capacitor, sin override `proceed()` de la app) |
| H4 | White-label multi-estado (sufijo "global") | — | — | **AUSENTE** — solo residuo "tlaxcala"; build de un solo estado |

## Pila de red

- **API principal:** `https://applic.smyt.tlaxcala.gob.mx` (SMYT Tlaxcala, HTTPS, gob.mx) +
  `/broadcasting/auth` (Pusher). Sin endpoints `http://` de backend propios en el bundle.
