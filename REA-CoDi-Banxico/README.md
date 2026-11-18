# Ingeniería inversa de CoDi Banxico 2.5.2 — índice

Análisis **estático** del APK `org.banxico.dgspim.mobile.codi` con REA (JADX headless). La app
**nunca se ejecutó**. Es la app oficial de **CoDi** (Cobro Digital) del **Banco de México
(Banxico)** — cobros entre personas/negocios por QR ligados a cuentas bancarias vía SPEI.
**#35 por instalaciones** del inventario gob.mx (≈100 K).

> **Nativa Kotlin/Java, ofuscada (R8), con la mejor postura de seguridad del acervo.** Es la app
> del **cobrador** (genera QR de cobro; por eso **no pide CAMERA**). Hace **certificate pinning
> correcto** (validación de cadena contra CAs del sistema **más** fijado del SHA-256 del cert
> hoja), guarda llaves en **AndroidKeyStore con `setUserAuthenticationRequired`** (atadas a
> biometría), firma los mensajes CoDi con **curvas elípticas (EC)** y cifra con **AES-GCM**.
> Endpoints HTTPS oficiales (`*.codi.org.mx`). **Firma auténtica de Banxico.** El contraste con
> Infonavit/Licencia EDOMEX (que **desactivan** la validación TLS) es total.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, postura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (NFC, overlay, biometría), componentes |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Nativa ofuscada, módulos (cobro, seguridad, httpclient) |
| 03 | [03-cripto-almacenamiento.md](03-cripto-almacenamiento.md) | AndroidKeyStore, EC, AES-GCM, biometría |
| 04 | [04-red-pinning.md](04-red-pinning.md) | **Certificate pinning correcto**, endpoints CoDi |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma **auténtica de Banxico** |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos (mayormente positivos) + observaciones |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `decompiled/pinning-checkServerTrusted.java` | El TrustManager con pinning (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 6 hallazgos principales

1. **Certificate pinning correcto (positivo, destacado).** `httpclient.security.a` es un
   `X509TrustManager` que **primero** valida la cadena contra las CAs del sistema (no es
   trust-all) **y luego** exige que el **SHA-256 del certificado hoja** esté en una lista de
   fingerprints fijados; si no, lanza `CertificateException("No trusted fingerprint was found")`.
   Código citado en doc 04. Es la postura TLS más fuerte del acervo gob.mx.
2. **Llaves en AndroidKeyStore atadas a biometría (positivo).** Uso de `AndroidKeyStore` +
   `setUserAuthenticationRequired` → las llaves criptográficas requieren autenticación del
   usuario (biometría/PIN) para usarse. Las credenciales de cuenta no se guardan en claro (doc 03).
3. **Firma de mensajes con curvas elípticas (EC).** 156 referencias a EC + SHA-256/512 +
   HmacSHA256 → firma digital de las operaciones CoDi conforme al esquema de Banxico (doc 03).
4. **Cifrado autenticado AES-GCM** (además de AES-CBC para compatibilidad) (doc 03).
5. **Endpoints HTTPS oficiales.** `https://sintp.codi.org.mx` / `sintp2.codi.org.mx` y rutas
   `codi-registro`/`codi-consulta`/`codi-claves`/`codi-catalogos` — todo HTTPS sobre la infra
   de Banxico, protegido por el pinning (doc 04).
6. **(Observación) `SYSTEM_ALERT_WINDOW`.** La app declara el permiso de **overlay** (dibujar
   sobre otras apps), inusual en banca; su uso exacto no se confirmó en código. Más biometría,
   timeout de inactividad y ofuscación R8 completan una postura defensiva sólida (doc 01, 06).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app, no se enrolaron
cuentas reales ni se llamó a ningún endpoint. El código está **ofuscado (R8)**; se decompilaron
y citaron las clases de seguridad clave (TrustManager/pinning), y la cripto se confirmó por la
estructura de clases + constantes (algoritmos, `AndroidKeyStore`). Los parámetros exactos de
derivación de llaves dentro del motor ofuscado se marcan como límite. **Firma auténtica de
Banxico** → binario íntegro del emisor.
