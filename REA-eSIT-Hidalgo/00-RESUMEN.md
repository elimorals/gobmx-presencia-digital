# 00 · Resumen ejecutivo — eSIT Móvil + Hidalgo

## Identidad

| Campo | Valor |
|---|---|
| App | eSITmovil (Sistema Integral Tributario — Hidalgo) |
| Package | `com.esitmovil` |
| versionName / versionCode | 1.23 / 23 |
| minSdk / targetSdk | 24 / 34 |
| compileSdk | 35 |
| Firma | ⚠️ **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — **no auténtica** |
| SHA-256 cert | `880cdb17c1cad9ba5ef97475bcabd024b741b16966f9cd29598213b6ff725ba2` (genérico APKPure) |
| sha256 XAPK | `27d61d3fbf5c00d30acda674048b1d9012a4f2e28cbd5152f6f97ca039afd205` |
| sha256 base apk | `60d803f2e65ae7d21106f09e21d7b9d9826b35626f9d8c3fcde341a5f7968e4d` |
| Dueño | Gobierno del Estado de Hidalgo (Secretaría de Finanzas / tributario) |

Package **verificado con el `manifest.json` del XAPK y `aapt2`** antes de documentar: coincide
`com.esitmovil`.

## Pila tecnológica

- **React Native + Hermes** — bundle `assets/index.android.bundle` en **bytecode Hermes** (muro
  para el análisis estático de la lógica JS, como Flutter AOT). Libs nativas en el split
  `config.arm64_v8a.apk`: `libhermes.so`, `libreactnative.so`, `libjsi.so`, `libfbjni.so`.
- Dependencias RN detectadas: `react-native-vision-camera` (`libVisionCamera.so` + CAMERA/AUDIO),
  `react-native-svg`, `react-native-screens`, `react-native-pdf` (`libpdfium.so`), filtros de
  imagen (`libnative-filters.so`, `libandroidlame.so`).
- 3 DEX (base) + el bundle Hermes.

## Backend (red)

- `https://atributariaw1.hidalgo.gob.mx` — API tributaria (dominio oficial de Hidalgo).
- `https://portaltributario.hidalgo.gob.mx/Aviso_de_Privacidad.html` — portal/aviso.
- Ambos **HTTPS**. No se halló backend por `http://` en claro.

## Función

App del **Sistema Integral Tributario (SIT)** de Hidalgo: trámites/pagos tributarios estatales.
La suite **Bluetooth** completa (SCAN/CONNECT/ADVERTISE/ADMIN) sugiere emparejamiento con
terminal/lector (POS o impresora) además del flujo en app.

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | **TLS/cleartext correcto** — sin `usesCleartextTraffic`, sin NSC, targetSdk 34 (cleartext bloqueado por defecto), backends HTTPS; sin trust-all | — | — | **AUSENTE-SANO** |
| H2 | Permiso privilegiado `android.permission.DUMP` declarado (no otorgable a app normal → inerte, pero bandera de manifiesto descuidado) | CWE-250 | BAJA | CONFIRMADO |
| H3 | `RECORD_AUDIO` en app tributaria (probable dependencia de VisionCamera, sin uso evidente de audio) | CWE-250 | BAJA | A-CONFIRMAR |
| H4 | Re-firmada por APKPure → la firma no prueba autenticidad; cruzar contra el canal oficial | — | INFO | CONFIRMADO |

**No repite el patrón de 911-CDMX** (cleartext) ni el de Infonavit/Cosmocolor (trust-all). Es un
**contraejemplo sano** en TLS, con higiene de permisos mejorable.

## Muro (lo no confirmable estáticamente)

El bundle Hermes impide confirmar: valores de secretos/API keys, construcción exacta de los
endpoints, y si el cliente RN valida TLS en runtime. Requeriría `hermes-dec` / Frida. Marcado como
no confirmado, sin inventar.
