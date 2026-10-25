# Ingeniería inversa de Mi Cuenta Infonavit 5.3.48 — índice

Análisis **estático** del APK base (`arm64-v8a`) de `com.infonavit.micuentainfonavit` con REA
(JADX headless 0.7.1 + apktool 3.0.3) y utilidades locales. La aplicación **nunca se ejecutó**.
Es la app oficial del **INFONAVIT** (Instituto del Fondo Nacional de la Vivienda para los
Trabajadores) con la que un derechohabiente consulta su saldo de subcuenta de vivienda,
puntos, precalificación, crédito, hace aclaraciones de pago, agenda citas y genera su token.

> Fecha: 2026-10-24. Artefacto en el SSD `/Volumes/PortableSSD/apps-gobmx-mx/apks/`.
> Package verificado con `inspect_android_package` **antes** de escribir cualquier documento
> (regla dura del encargo): `com.infonavit.micuentainfonavit` v5.3.48 (versionCode 99).
> Firmado con certificado propio `O=INFONAVIT, OU=SGTI, CN=serviciosweb.infonavit.org.mx`
> (SHA256withRSA) — **no re-firmado** (ver doc 09).

## Hallazgo principal

La app **deshabilita por completo la validación de certificado y hostname TLS en el build de
producción**: un `TrustManager` que acepta cualquier certificado + un `HostnameVerifier` que
devuelve `true` siempre, cableados en `getUnsafeOkHttpClient()`, que es el cliente usado por
**todos** los servicios Retrofit. No hay certificate pinning que lo compense. Ver
[03-tls-certificado.md](03-tls-certificado.md). Esto habilita MITM sobre auth, pagos y datos
personales pese a que el tráfico en claro está bloqueado.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila, mapa de módulos, tabla de hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, deeplinks, IPs internas, superficie |
| 02 | [02-red-endpoints.md](02-red-endpoints.md) | Hosts prod/QA, gateway, SOAP/JAX-WS, integraciones gob.mx |
| 03 | [03-tls-certificado.md](03-tls-certificado.md) | **Bypass TLS total** (trust-all + accept-all), sin pinning |
| 04 | [04-autenticacion-credenciales.md](04-autenticacion-credenciales.md) | Credenciales hardcodeadas (auth/MQ/pago), Basic Auth, X-API-KEY |
| 05 | [05-cripto-almacenamiento.md](05-cripto-almacenamiento.md) | `CryptoHelper` AES/ECB con llave embebida, KeyStore, EncryptedSharedPreferences |
| 06 | [06-ofuscacion-proteccion.md](06-ofuscacion-proteccion.md) | Sin ofuscación, sin packer, sin root/Frida/SafetyNet |
| 07 | [07-sdks-terceros.md](07-sdks-terceros.md) | Huawei HMS, Firebase/Maps/Places, OkHttp, RxJava, BouncyCastle/PGP, iText, PDFium |
| 08 | [08-pantallas-features.md](08-pantallas-features.md) | Mapa de pantallas y features de negocio |
| 09 | [09-firma-recursos.md](09-firma-recursos.md) | Firma, autenticidad, librerías nativas, recursos |
| 10 | [10-hallazgos-seguridad.md](10-hallazgos-seguridad.md) | Hallazgos consolidados con severidad y evidencia |
| 11 | [11-analisis-dinamico.md](11-analisis-dinamico.md) | Dispositivo real (Android 16, sin root): firma v3 en vivo, **H1 demostrado por MITM**, endpoint real |

## Artefactos

```
_apk/base.apk                 APK analizado (gitignored)   sha256 1f824c78…b0b6bd
AndroidManifest.xml           manifiesto decodificado
res-decoded/                  network_security_config.xml, provider_paths.xml
evidence/apktool.yml          metadatos de build (apktool)
evidence/smali/               pruebas smali de los hallazgos (gitignored, propietario)
decompiled/apktool/           salida completa apktool (smali+recursos, gitignored)
hashes.sha256                 hashes de los documentos
```

## Método y límites

- Herramientas REA: `inspect_android_package`, `search_android_classes`, `inspect_android_class`,
  `decode_android_resources`; apktool `d` para smali+recursos; firma con `keytool`.
- Todo es **observación estática**: el decompilador no prueba comportamiento en runtime. Donde
  no hay evidencia que sostenga una afirmación, se dice explícitamente (p. ej. no se verificaron
  las reglas del Firebase RTDB ni se ejecutó el descifrado de credenciales).
- Se añadió **análisis dinámico** (doc 11): app instalada en Motorola XT2435-3 (Android 16, sin
  root), firma v3 verificada en vivo y **H1 demostrado con un MITM sin root** que capturó tráfico
  real. No se hizo login (alcance acordado). El proxy se removió del teléfono tras la prueba; la
  app queda instalada pendiente de desinstalar.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
