# 10 · Hallazgos de seguridad (consolidado)

Análisis **estático**. Severidad cualitativa (impacto × facilidad de explotación en el contexto de
una app financiera de gobierno con PII). Cada hallazgo apunta a su evidencia.

## H1 · Validación TLS deshabilitada en producción — **Crítica**

El cliente OkHttp de todos los servicios (`getUnsafeOkHttpClient`) usa un `X509TrustManager` cuyo
`checkServerTrusted` está vacío y un `HostnameVerifier` que retorna `true` siempre; sin certificate
pinning. `isProductiveEnviroment()` = `true`, apuntando a hosts reales.

- Evidencia: `evidence/smali/RetrofitService$3.smali`, `RetrofitService$4.smali`,
  `RetrofitService.smali` (getUnsafeOkHttpClient), `EnviromentRetrofit.smali`. Doc [03](03-tls-certificado.md).
- Impacto: MITM trivial sobre auth, token, PII, pagos. `cleartextTrafficPermitted=false` no mitiga.
- Fix: validación por defecto de plataforma + pinning real; quitar `SSLContext("SSL")`.

## H2 · Credenciales de servicio hardcodeadas — **Alta**

`RetrofitService` embebe usuario/clave de pago **en texto plano** (`USER_PAYMENT`,
`PASSWORD_PAYMENT`) y de auth/MQ "cifradas" con AES-ECB bajo llave del propio APK.

- Evidencia: `<clinit>` de `evidence/smali/RetrofitService.smali`. Doc [04](04-autenticacion-credenciales.md).
- Impacto: suplantación de la app ante el backend; el "cifrado" es reversible con el binario.
- Fix: tokens efímeros emitidos por servidor; rotar credenciales expuestas.

## H3 · Criptografía débil en `CryptoHelper` — **Media**

`AES/ECB/NoPadding` + XOR con `SecretKeySpec` de llave hardcodeada para proteger las credenciales.

- Evidencia: `evidence/smali/CryptoHelper.smali`. Doc [05](05-cripto-almacenamiento.md).
- Impacto: refuerza H2 (confidencialidad ilusoria). ECB no oculta patrones, sin IV.
- Fix: no cifrar secretos con llave embebida; si se necesita cifrar en cliente, AES-GCM con llave
  del AndroidKeyStore (como ya hace el módulo biométrico).

## H4 · Hosts/IPs internos y de QA en el build de prod — **Media**

`servicioswebinternoqa.infonavit.org.mx`, `portalmciqa.infonavit.org.mx`, `10.81.1.56:8065`,
`091402AQ249.infonavit.net:8065`, deeplink `http://10.82.1.41:10039`.

- Evidencia: `AndroidManifest.xml`, `EnviromentRetrofit.smali`, grep de strings. Docs [01](01-manifiesto.md), [02](02-red-endpoints.md).
- Impacto: fuga de topología interna (IPs privadas, hostnames de máquina, entorno QA). Las ramas QA
  están muertas en runtime (`isProductiveEnviroment=true`) pero los datos quedan embebidos.
- Fix: separar configs por build type; no incluir hosts internos/QA en release.

## H5 · Sin ofuscación ni anti-tamper — **Media**

Código en claro, sin packer, sin detección root/Frida/emulador, sin SafetyNet/Play Integrity, sin
verificación de firma en runtime.

- Evidencia: nombres en claro en todo el smali; libs nativas solo PDFium; grep de marcadores vacío.
  Doc [06](06-ofuscacion-proteccion.md).
- Impacto: facilita reempaquetado, hooking y lectura de H1–H4.
- Fix: R8 + ofuscación, detección de integridad, attestation.

## H6 · FileProvider amplio + API keys en recursos — **Baja**

`provider_paths` comparte `external-path "."` (toda la carpeta externa) aunque el provider no está
exportado; 3 llaves `AIza` de Google en recursos (Maps/Places, deben estar restringidas).

- Evidencia: `res-decoded/provider_paths.xml`, `res/values/strings.xml`. Docs [01](01-manifiesto.md), [07](07-sdks-terceros.md).
- Impacto: alcance excesivo del FileProvider; posible abuso de cuota de las API keys si no están
  restringidas por package+firma.
- Fix: acotar `external-path` a subdirectorios necesarios; restringir las keys en la consola Google.

## Controles positivos

| Control | Estado |
|---|---|
| `cleartextTrafficPermitted="false"` | HTTP en claro bloqueado globalmente |
| `allowBackup="false"` / `fullBackupContent="false"` | sin backup de datos por el sistema |
| EncryptedSharedPreferences + AndroidKeyStore (módulo biométrico) | almacenamiento correcto de secreto biométrico |
| Firma con certificado propio de INFONAVIT | artefacto no re-firmado (autenticidad, doc 09) |
| Mayoría de componentes `exported="false"` | superficie exportada mínima (2 activities con deeplink) |

## Matriz

| ID | Severidad | Esfuerzo de explotación | Estado |
|---|---|---|---|
| H1 | Crítica | Bajo (posición de red) | confirmado estáticamente |
| H2 | Alta | Bajo (abrir APK) | confirmado (pago en claro); auth/MQ reversibles, no descifradas |
| H3 | Media | — | confirmado |
| H4 | Media | — | confirmado |
| H5 | Media | — | confirmado (ausencia de controles) |
| H6 | Baja | Medio | confirmado |

## Límites del análisis

Sin ejecución ni tráfico en vivo: no se montó MITM para demostrar H1, no se descifraron las
credenciales de H2, no se verificaron las reglas del Firebase RTDB ni la restricción de las API
keys. Las severidades son juicio sobre evidencia estática; la explotación real depende de factores
(posición de red, configuración del backend) no observables desde el APK.
