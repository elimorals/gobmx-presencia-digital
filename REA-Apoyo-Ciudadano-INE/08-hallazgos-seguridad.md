# 08 — Hallazgos de seguridad y privacidad (priorizados)

Severidad orientativa: **P1** alto · **P2** medio · **P3** bajo/deuda · **✓** positivo. Todo es
observación **estática**; ninguna afirmación implica que un endpoint fuera llamado o un dato filtrado
en runtime.

## P1 — Transmisión de PII electoral de terceros empaquetada

La app captura la **credencial para votar** del ciudadano que respalda (foto anverso/reverso, datos
por OCR, **foto en vivo** y **firma**), la empaqueta y la sube con `SessionService.uploadZip(TypedFile,
…)` / `generaCodigo(File, …)` (doc 02, 03). Es PII sensible de **terceros**, con biometría facial y
firma. **Mitigante:** va sobre HTTPS (sin cleartext por política, doc 01) y hacia el backend del INE
con firma auténtica. **Riesgo residual:** amplitud del dato y su combinación con ubicación/dispositivo
(doc 05). *Evidencia:* `decompiled/SessionService.metodos.txt`, modelos `Credential*`, `DatosCiudadano`.

## P2 — Endpoints y llaves de Twitter "protegidos" solo por cifrado con llave en el APK

`assets/config.properties` guarda `BASE_PRE*`, `PRODUCTION`, `DEVELOPMENT`, `CONSUMER_K/SK` como texto
**AES cifrado** (comentarios *"Cifrado ECB/CBC"*). La llave de descifrado reside en el binario → es
**recuperable**; el cifrado ofusca, no protege (doc 04). *Evidencia:* `evidence/config.properties`,
`AES/...` + `SecretKeySpec` en `evidence/dex-strings-notables.txt`.

## P2 — Criptografía débil: `AES/ECB/NoPadding`

Presente en el DEX junto a `SecretKeySpec`/`IvParameterSpec`/PBKDF. **ECB** no oculta patrones de
bloque y **NoPadding** es frágil; mal modo para proteger datos (doc 04). No se aisló la cadena de
llamada exacta a cuerpo de método, así que se reporta la **presencia del modo débil**, no el dato
preciso que cifra. *Evidencia:* `evidence/dex-strings-notables.txt`.

## P3 — Pila de red en fin de vida: Square Retrofit 1.x

`retrofit.RestAdapter` (+ RxJava 1). Retrofit 1.x está sin mantenimiento desde ~2016; deuda técnica en
una app que transporta PII electoral (doc 03). *Evidencia:* `retrofit/RestAdapter` en
`evidence/dex-strings-notables.txt`.

## P3 — Recolección de datos sociales + ubicación + estado del teléfono + telemetría

`model.SocialLayer.Fb*` (perfil/empleador/ubicación de Facebook) + Twitter + `ACCESS_FINE_LOCATION` +
`READ_PHONE_STATE` + `ACCESS_ADSERVICES_AD_ID` + bitácora a **Firestore** (`FireStoreBitacoraBeta`).
Superficie de recolección amplia del lado del auxiliar, además de la telemetría a Google (doc 05).
*Evidencia:* clases `SocialLayer`/`firestore`, `permisos.txt`.

## P3 — Realm sin confirmación de cifrado

La BD local Realm (`librealm-jni.so`) guarda apoyos pendientes (PII) antes de subirlos. No se confirmó
si habilita el cifrado de Realm. `allowBackup=false` reduce la exposición vía adb, pero un dispositivo
rooteado podría leer la DB si está sin cifrar. Queda como **pregunta abierta**, no veredicto (doc 04).

## ✓ Positivos

- **Firma auténtica del INE** (`O=Instituto Nacional Electoral`, verifica; sin re-firma) — doc 07.
- **Sin tráfico en claro por política** (`usesCleartextTraffic` ausente, API 28+) — doc 01.
- **Indicadores de validación TLS estricta / pinning** (`X509TrustManagerExtensions`,
  `StrictHostnameVerifier`; sin trust-all) — doc 03.
- **`allowBackup=false`** — doc 01.
- **Controles anti-fraude**: Play Integrity + reCAPTCHA/`generaCaptcha` + OTP SMS — doc 03, 06.
- **Aviso de protección de datos in-app** (`PreDataProtectionManifestActivity`) — doc 05.

## Comparación con las otras dos apps del INE del acervo

| | Valida INE-QR | **Apoyo Ciudadano** |
|---|---|---|
| Red | **Ninguna** (sin permiso INTERNET) | HTTPS (sube paquete ciudadano) |
| Dato | Se queda en el dispositivo | **Sale del dispositivo** (ZIP al backend) |
| Postura | Excelente (mínima) | Correcta en transporte, amplia en recolección |

Apoyo Ciudadano es intrínsecamente más expuesta que Valida INE-QR porque **su función es transmitir**
PII; dentro de eso, aplica controles razonables (HTTPS, firma auténtica, anti-fraude), con deuda en
cripto local (AES/ECB, llave en APK) y amplitud de telemetría/recolección social.
