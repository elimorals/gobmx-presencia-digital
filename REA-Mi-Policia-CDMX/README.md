# Ingeniería inversa de Mi Policía (CDMX) 3.2.5 — índice

Análisis **estático** del APK `com.moobky.MiPolicia` (base del bundle + split `arm64_v8a`)
con REA (JADX headless 0.7.1 + apktool) y extracción de cadenas del snapshot Dart. La app
**nunca se ejecutó**. Es la app oficial de la **Secretaría de Seguridad Ciudadana de la CDMX
(SSC)**: cuadrantes, contacto con policía, reglamento de tránsito y ubicación. #12 por
instalaciones del inventario gob.mx (≈1 M+ en Google Play).

> **Corrección de pila (con evidencia).** El triage preliminar la marcó "nativa Kotlin/Java".
> **Es Flutter**: `assets/flutter_assets/` en el base y `lib/arm64-v8a/libapp.so` (8.2 MB, snapshot
> Dart AOT) + `libflutter.so` en el split. El DEX **no tiene lógica de negocio**: la única clase
> `com.moobky.*` es `com.moobky.MiPolicia.R` (recursos); las 3 948 clases del DEX son el embedding
> de Flutter y el lado Android de los plugins. Toda la lógica y los endpoints viven en `libapp.so`,
> como en SAT Móvil y Urbani.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila Flutter, backend SSC, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación en segundo plano, **DUMP**), componentes, flags |
| 02 | [02-arquitectura-flutter.md](02-arquitectura-flutter.md) | Flutter 3.x, DEX = embedding, Dart en `libapp.so`, cómo se verificó |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | **Endpoints reales** `*.ssc.cdmx.gob.mx` recuperados del snapshot Dart |
| 04 | [04-almacenamiento-cripto-pinning.md](04-almacenamiento-cripto-pinning.md) | Cert DigiCert G2 embebido, primitivas de pinning Dart, almacenamiento |
| 05 | [05-sdks-plugins.md](05-sdks-plugins.md) | Plugins Flutter (ubicación, mapas, llamada directa, WebView, barcode) |
| 06 | [06-firma-recursos.md](06-firma-recursos.md) | **Firma por un desarrollador individual** (no la SSC), estructura, recursos |
| 07 | [07-hallazgos-seguridad.md](07-hallazgos-seguridad.md) | **Hallazgos priorizados** (token Bearer hardcodeado, firma, DUMP) |
| 08 | [08-limites-codigo-dart.md](08-limites-codigo-dart.md) | Qué NO es recuperable: la lógica/PII dentro del snapshot Dart |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `evidence/endpoints-libapp.txt` | URLs/dominios extraídos de `libapp.so` |
| `evidence/secretos-hardcoded.txt` | Token Bearer y token de URL embebidos (valores completos; redactados en los `.md`) |
| `AndroidManifest.xml` | Manifiesto decodificado |
| `hashes.sha256` | SHA-256 del APK base y del split arm64 |

> Lo pesado/propietario (APK, `libapp.so`, `libflutter.so`, cert, DEX, `evidence/`) **no se versiona**
> (`.gitignore`).

## 6 hallazgos principales

1. **Token `Bearer` estático hardcodeado en el cliente** (`libapp.so`) más un **token de API en la
   ruta** del endpoint de cuadrantes (`/cuadrante/<token>/`). Quien extraiga el `.so` puede llamar a
   las APIs de la SSC con esas credenciales fijas. Valores en `evidence/`, redactados aquí (doc 03, 07).
2. **App de seguridad firmada por un desarrollador individual** (`CN=Saul Espinosa Ruiz`), no por la
   SSC CDMX. La cadena de confianza y las actualizaciones dependen de una llave personal, no
   institucional (doc 06, 07).
3. **Permiso `DUMP` declarado** — permiso `signature|privileged` que el sistema no concede a apps de
   tienda; inusual en una app de este tipo. Además `ACCESS_BACKGROUND_LOCATION` (doc 01, 07).
4. **Backend y superficie de API de la SSC expuestos** — `api-p.ssc.cdmx.gob.mx` (reglamento de
   tránsito: `busca_placa`, `elemento_cuadrante`, `acreditado_post`), `cuadrantes.ssc.cdmx.gob.mx`,
   `cms.ssc.cdmx.gob.mx`, `ucs.ssc.cdmx.gob.mx:8443`, todos por HTTPS (doc 03).
5. **Indicio de pinning a la raíz DigiCert G2** — un `.pem` de la raíz DigiCert Global Root G2 viene
   embebido como `ssc-cdmx-gob-mx.pem` y las primitivas `SecurityContext`/`BadCertificateCallback` de
   Dart están presentes; el cableado exacto vive en el snapshot y no se aisló → **indicio, no hecho**
   (doc 04).
6. **Postura por lo demás razonable** — todas las APIs por HTTPS, sin `networkSecurityConfig` que
   habilite cleartext (el único `http://` referenciado, un `.xlsx` de avisos, quedaría bloqueado por
   defecto), permisos sin SMS/contactos/almacenamiento (doc 01, 04).

## Alcance y límites

Análisis **original** de una app pública del gobierno de la CDMX que se posee, con fines de
investigación de seguridad. **No redistribuye** el APK ni el snapshot Dart. Los endpoints
documentados son rutas presentes en el binario, no secretos de usuarios ni datos extraídos.
Observaciones **estáticas**: no se ejecutó la app, no se disparó el contacto de emergencia ni se
llamó a ningún endpoint. **Firma**: certificado de desarrollador individual (`CN=Saul Espinosa
Ruiz`), firma original preservada (no re-firmada por el redistribuidor). La **lógica de negocio y el
manejo de PII viven en el snapshot Dart AOT** (`libapp.so`), que no se desensambló a código fuente:
lo que va más allá de la forma de los endpoints queda documentado como límite, no inventado (doc 08).

---
