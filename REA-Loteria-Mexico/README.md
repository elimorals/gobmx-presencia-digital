# Ingeniería inversa de Lotería de México 1.3.26 — índice

Análisis **estático** del APK `loterianacional.mx` con REA (JADX headless + apktool). La app
**nunca se ejecutó**. Es la app de la **Lotería Nacional (Lotenal)** — consulta de resultados de
sorteos y verificación de billetes. **#48 por instalaciones** del inventario gob.mx (≈100 K).

> **Nativa Kotlin/Java, white-label del proveedor español GemesGroup (Madrid).** No es una app
> de compra: consulta resultados (sorteos) y verifica cupones; **no maneja pagos**. El mismo
> codebase sirve loterías de España (ONCE) y Portugal (Santa Casa). Hallazgos: **`usesCleartextTraffic="true"`**
> con resultados servidos por **HTTP desde un dominio de tercero** (`nacionalloteria.com`),
> **chequeo de nueva versión vía un enlace público de Dropbox**, y un **helper trust-all TLS**
> presente pero **sin llamador** (código muerto, no un MITM confirmado).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, white-label, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (AdServices), cleartext |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Nativa, clases propias, GemesGroup |
| 03 | [03-red-endpoints-tls.md](03-red-endpoints-tls.md) | Endpoints, cleartext, trust-all inerte, Dropbox |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Firma GemesGroup (España), procedencia |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK (versionado) |
| `decompiled/SSLHelper-trustall.java` | Cuerpo del trust-all + nota de "sin llamador" (no versionado) |
| `evidence/endpoints.txt`, `evidence/firma.txt` | endpoints y firma (no versionados) |

## 5 hallazgos principales

1. **(Medio) Cleartext + resultados por HTTP desde un tercero.** `usesCleartextTraffic="true"` y
   los resultados/sorteos se consultan por **`http://www.nacionalloteria.com/...`** (dominio
   comercial de **tercero**, no gob.mx) y `http://www.lotenal.gob.mx`. Un MITM puede **leer o
   alterar los resultados mostrados** (doc 03).
2. **(Bajo-Medio) Chequeo de versión vía Dropbox público.** `CheckNewVersionAvalaibleTask` lee
   `https://www.dropbox.com/s/iwydmo8nft35s1q/version` (y OneDrive) → la "última versión" la
   controla un **archivo en almacenamiento personal de un tercero**; mecanismo frágil y
   dependiente de una cuenta Dropbox (doc 03).
3. **(Bajo, a nota) Trust-all TLS presente pero inerte.** `SSLHelper.enableSSLSocket()` instala un
   `HostnameVerifier` que devuelve `true` + `TrustManager` vacío como **default global**, pero
   **no se halló ningún llamador** en el smali → **código muerto**. Se reporta como riesgo latente
   (podría activarse en una versión futura), **no como MITM confirmado** (doc 03).
4. **(Bajo) Tracking publicitario.** `AD_ID` + `ACCESS_ADSERVICES_*` + `RefreshAdIntervalTask` →
   la app sirve anuncios con atribución (doc 01).
5. **Sin pagos.** No hay compra de billetes ni pasarela ni permisos/clases de pago: es consulta
   de resultados + verificación de cupón + anuncios. White-label multi-país de GemesGroup (doc 02).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
endpoints. **Firma** de **GemesGroup** (Madrid, España) — el certificado identifica al proveedor
que compiló la app, no a Lotenal; firma original preservada (no re-firmada por APKPure).
