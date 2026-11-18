# Resumen — FiREL 1.0.14 (`gob.mx.android.firel`)

## Identidad

- **App:** FiREL — Firma Electrónica del Poder Judicial de la Federación (CJF). Enrolamiento de
  identidad para emitir/usar la e.firma/FIEL en trámites judiciales.
- **Package (store):** `gob.mx.android.firel` · **package real del código:**
  `com.fielnet.notificaciones_colegio_notarios_mvvm` (launcher `…ui.login.LoginActivity`).
- **versión** 1.0.14 (versionCode 37). **#43** del inventario (≈100 K).
- **Firma:** re-firmada por APKPure (`CN=Android, O=Google Inc.`) → no auténtica del CJF.

## Arquitectura (en una línea)

App **nativa Android (Kotlin, MVVM + DataBinding)** de **FielNet / IQSEC**, white-label reusado
de una app de **Colegio de Notarios**. Red con **Volley** + **SOAP** (SimpleXML, envelopes).
9 872 clases. Biometría con **Identy.io** (`com.identy.*`, `com.identy.face`) + ONNX
(`libonnxruntime.so`, `assets/identy_data/*.ort`) + libs nativas `libfinger-native-lib.so` /
`libface-native-lib.so`.

## Qué captura y envía (doc 03)

INE (OCR + foto) · rostro (match + liveness/anti-spoof) · **huellas sin contacto** (Identy) ·
**firma autógrafa** · **video de verificación** (`RequestSendVideoMeet`) · ubicación · → genera
el **certificado** (`RequestGenerateCertificate`). Todo por SOAP a IQSEC.

## Proveedores (doc 04)

| Proveedor | Para qué | Dominio |
|---|---|---|
| **IQSEC** | backend propio (SOAP) | `iqsec-masivos.com.mx/svf/wssvf/`, `investigacion.iqsec.com.mx` |
| **Identy.io** | biometría huella/rostro + licencia | `licensemgr.identy.io`, `bancolicensemgr.identy.io` |
| **ShuftiPro** | KYC / verificación de identidad | `api.shuftipro.com` |

## Postura (balance)

| Aspecto | Veredicto |
|---|---|
| PII | 🔴 Extrema (INE + rostro + huellas + firma + video) |
| TLS | ⚠️ **Contradictorio**: `CertificatePinner` **y** `getUnsafeOkHttpClient` coexisten (doc 04) |
| Terceros | ⚠️ 3 proveedores en el flujo biométrico (uno extranjero) |
| Cadena de suministro | ⚠️ White-label de "Colegio de Notarios" (FielNet/IQSEC) |
| Firma | ⚠️ Re-firmada por APKPure |
| SOAP | ⚠️ `tempuri.org` (namespace por defecto) |

## Lo que NO se pudo (límites)

- **Si la biometría viaja con TLS seguro o inseguro:** el método `getUnsafeOkHttpClient` existe,
  pero no se ató a una clase propia ni se confirmó su uso en las llamadas de biometría (R8 +
  posible librería). Es el dato más importante pendiente; se reporta como **bandera**, no como
  hecho.
- **Dónde se guarda la llave/certificado emitido:** `RequestGenerateCertificate` lo genera del
  lado servidor (IQSEC); el almacenamiento local fino no se confirmó.
