# 00 · Resumen ejecutivo

## Identidad

| Campo | Valor |
|---|---|
| App | Mi Cuenta Infonavit |
| Package | `com.infonavit.micuentainfonavit` |
| versionName / versionCode | 5.3.48 / 99 |
| minSdk / targetSdk | 24 / 36 |
| Firma | `CN=serviciosweb.infonavit.org.mx, OU=SGTI, O=INFONAVIT, L=CDMX, C=MX` · SHA256withRSA |
| SHA-256 cert | `84:B7:C1:48:B8:3F:92:28:32:DD:45:8F:58:05:34:A2:04:72:7B:56:DA:EC:64:06:FB:30:4A:15:49:11:13:1D` |
| sha256 APK | `1f824c78a48f1e06a3a203c9e912d6d291954be3b1e389ef7ff6b93d4ba0b6bd` |
| Clases / recursos | 20 093 / 1 930 |
| Dueño | INFONAVIT (gobierno de México) |

Package **verificado con `inspect_android_package` antes de documentar** (regla dura): coincide,
no está mal nombrado como pasó con otro artefacto del SSD.

## Pila tecnológica

- **Nativo Android (Kotlin/Java)** — no Flutter, no React Native. Arquitectura por capas con
  nombres en claro: `com.infonavit.data` (red), `com.infonavit.domain` (modelos/repos/util),
  `com.infonavit.micuentainfonavit.ui.*` (pantallas), `usecases`, `foundations`.
- **Red:** Retrofit2 + OkHttp3 + Gson; además SOAP/JAX-WS (SimpleXML) para servicios legados.
- **Doble ecosistema de servicios móviles:** Google (GMS/Firebase/Maps/Places) **y** Huawei HMS
  (maps, safetydetect) para dispositivos sin Google.
- **PDF:** PDFium nativo (`libmodpdfium.so`) + iText; **OpenPGP:** BouncyCastle + PGPainless.
- **Sin ofuscación ni packer** (ver doc 06): el código decompila limpio.

## Mapa de módulos (features)

`login`, `registro`, `recuperar_contrasena`, `biometric`, `token_infonavit`, `nip_infonatel`,
`aclaracion_pagos`, `aportaciones_extraordinarias`, `reestructuras`, `resumen_ahorros(_v2)`,
`savings`/`saving_v2`, `movements`, `citas`, `oficinas`, `constancia`, `seguimiento_caso`,
`correccion_datos`/`actualizar_datos`, `profile`, `calculadora_uma`, `mejoravit`,
`vivienda_bienestar`, `encuesta_satisfaccion`, `bitacora_seguridad`, `trazas`, `pdf_view`,
`location`, `avisoprivacidad`. Detalle en [08-pantallas-features.md](08-pantallas-features.md).

## Hallazgos (tabla)

| ID | Severidad | Hallazgo | Doc |
|---|---|---|---|
| H1 | **Crítica** | TLS sin validación en producción: TrustManager acepta todo + HostnameVerifier `true`, cliente usado por todos los servicios Retrofit; sin pinning. **Demostrado en vivo** por MITM sin root ([11](11-analisis-dinamico.md)) | [03](03-tls-certificado.md) |
| H2 | **Alta** | Credenciales de servicio hardcodeadas en el APK: usuario/clave de pago en texto plano; auth/MQ "cifradas" con AES-ECB bajo llave embebida en el mismo binario | [04](04-autenticacion-credenciales.md) |
| H3 | **Media** | `CryptoHelper` usa `AES/ECB/NoPadding` con `SecretKeySpec` de llave hardcodeada | [05](05-cripto-almacenamiento.md) |
| H4 | **Media** | Hosts/IPs internos y de QA embebidos en el build de prod (`servicioswebinternoqa`, `portalmciqa`, `10.81.1.56:8065`, `091402AQ249.infonavit.net`, deeplink `10.82.1.41:10039`) | [01](01-manifiesto.md), [02](02-red-endpoints.md) |
| H5 | **Media** | Sin anti-tamper: no hay detección de root/Frida/emulador, ni SafetyNet/Play Integrity, ni ofuscación | [06](06-ofuscacion-proteccion.md) |
| H6 | **Baja** | 3 API keys de Google (`AIza…`) en recursos; `FileProvider` comparte toda la carpeta externa (`external-path "."`) aunque no exportado | [07](07-sdks-terceros.md), [01](01-manifiesto.md) |
| — | Positivo | `cleartextTrafficPermitted="false"` global; `allowBackup=false`; `EncryptedSharedPreferences`/AndroidKeyStore presentes; APK firmado por cert propio de INFONAVIT (no re-firmado) | [01](01-manifiesto.md), [05](05-cripto-almacenamiento.md), [09](09-firma-recursos.md) |

La combinación **H1 (MITM trivial) + H2 (credenciales en el binario)** es la más grave: un
atacante en posición de red puede interceptar y modificar todo el tráfico, y además extraer del
APK las credenciales de los servicios de backend (incluido el de pagos).

## Alcance

Estático completo + **dinámico** (doc 11): app instalada en Motorola XT2435-3 (Android 16, sin
root), firma v3 verificada en vivo y **H1 demostrado con MITM sin root** sobre un endpoint real.

Límites: no se verificaron reglas del Firebase RTDB (`micreditoinfonavit-15f0d`), no se
descifraron las credenciales base64 (se documenta el mecanismo, no el secreto), no se hizo login
ni se capturó tráfico autenticado (alcance acordado). Las ramas de "entorno no productivo" existen
como strings pero están muertas en este build (`isProductiveEnviroment()` retorna `true`).
