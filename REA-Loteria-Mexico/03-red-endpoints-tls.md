# Red, endpoints, TLS y actualización

Endpoints extraídos del smali de `loterianacional.mx.*` (ver `evidence/endpoints.txt`).

## Endpoints

| URL | Esquema | Nota |
|---|---|---|
| `www.lotenal.gob.mx` | **HTTP** | oficial, pero en claro |
| `www.nacionalloteria.com/mexico/resultados-loteria-nacional.php` | **HTTP** | **TERCERO** — resultados |
| `www.nacionalloteria.com/mexico/sorteo-…` | **HTTP** | **TERCERO** — detalle de sorteo |
| `www.loterianacional.gob.mx/documentos/listapremios/` | HTTPS | oficial (lista de premios) |
| `www.dropbox.com/s/iwydmo8nft35s1q/version` | HTTPS | **chequeo de versión** |
| `onedrive.live.com/download.aspx` | HTTPS | chequeo de versión alterno |
| `www.juegosonce.es/…` | HTTP | ONCE (España) — white-label |
| `www.jogossantacasa.pt/web/` | HTTPS | Santa Casa (Portugal) — white-label |

## H1 (Medio) — Resultados por HTTP desde un dominio de tercero

`usesCleartextTraffic="true"` (doc 01) + los resultados/sorteos se consultan por **HTTP** contra
**`nacionalloteria.com`** — un **dominio comercial de tercero**, no de Lotenal ni gob.mx. Dos
problemas:
1. **Transporte en claro:** un MITM puede **leer o alterar los resultados** que ve el usuario (p.
   ej., mostrar un número ganador falso).
2. **Dependencia de un tercero** para el dato oficial de resultados de la Lotería Nacional (riesgo
   de integridad/disponibilidad fuera del control del gobierno).

## H2 (Bajo-Medio) — Chequeo de versión vía Dropbox público

`CheckNewVersionAvalaibleTask` consulta `https://www.dropbox.com/s/iwydmo8nft35s1q/version` (y
OneDrive como alterno) para saber si hay nueva versión. La "última versión" la define un **archivo
en una cuenta personal de Dropbox** de un tercero:
- mecanismo **frágil** (si la cuenta/enlace cae, el chequeo se rompe),
- superficie de **integridad**: quien controle ese archivo controla el aviso de actualización. Si
  además guiara a descargar un APK (permiso `WRITE_EXTERNAL_STORAGE` presente), sería un vector de
  distribución fuera de Play — **no se confirmó** que descargue un APK, solo que lee el "version"
  de Dropbox; se deja como superficie a revisar.

## H3 (Bajo, nota honesta) — Trust-all TLS presente pero INERTE

`loterianacional.mx.util.SSLHelper.enableSSLSocket()` instala un trust-all global (cuerpo en
`decompiled/SSLHelper-trustall.java`):
- `HostnameVerifier.verify()` → `return true` (acepta cualquier host),
- `X509TrustManager.checkServerTrusted()` → **vacío** (acepta cualquier cert),
- `HttpsURLConnection.setDefaultSSLSocketFactory(...)` → lo pone como **default del proceso**.

Es el mismo antipatrón que desactivó TLS en Infonavit/Licencia EDOMEX/ProcurApp. **PERO**: un
`grep` sobre todo el smali muestra que **ninguna clase llama a `enableSSLSocket()`** (solo
aparece dentro de las 3 clases de `SSLHelper`). → **código muerto / no invocado**. Por eso se
reporta como **riesgo latente** (presente en el binario, podría activarse en una versión futura o
por reflexión), **no como un MITM confirmado**. La verificación TLS efectiva sobre los endpoints
HTTPS es, por tanto, la del sistema; el riesgo real de MITM viene del **cleartext (H1)**, no de
este helper.

## Pinning

No hay certificate pinning. La validación TLS de los endpoints `https://` es la estándar del
sistema (el trust-all no está cableado, H3).
