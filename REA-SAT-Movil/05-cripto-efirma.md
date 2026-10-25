# Criptografía, e.firma y almacenamiento

El dato de mayor valor que maneja la app es la **e.firma (FIEL)** del contribuyente: su
certificado `.cer` y su llave privada `.key`. La evidencia indica que la app los procesa **en el
dispositivo** ("e.firma portable"), no solo los sube.

## Evidencia de manejo de e.firma (cadenas en `libapp.so`)

- *"Certificados de e.firma"*, *"un certificado de e.firma vigente"*, *"...e.firma portable en tu
  dispositivo..."*
- `CertificatesService`, `CertificatesState`, `CertificateException`.
- Rutas de API: `/dwh/efirma/`, `/dwh/digitalcertificate/`, `/certificates.json`.

## Pila criptográfica (paquetes Dart)

| Paquete | Uso probable |
|---|---|
| **pointycastle** (157 refs) | Cripto pura en Dart: **AES** (GCM/CBC), **RSA**, digests. Cadenas: `AES_GCM_NoPadding`, `AesCipher`, `AESEngine`, `AesCipherNoPadding`. |
| **asn1lib** | Parseo **ASN.1/DER** → leer el `.cer` X.509 y la llave `.key` de la FIEL. |
| **crypto** | Hashing (SHA-256, HMAC) de alto nivel. |
| **encrypt** | Envoltorio de conveniencia sobre PointyCastle (AES/RSA). |
| **base32** | Codificación base32 (posible TOTP/semillas o codificación de datos). |

**Lectura.** La combinación **asn1lib + pointycastle (RSA)** es la firma técnica de una app que
**firma digitalmente en el cliente** con la llave privada de la FIEL: parsea el `.cer`/`.key`,
carga la RSA y produce la firma de un trámite — sin que la llave privada tenga que salir del
dispositivo. AES-GCM se usa para cifrar en reposo lo que se guarda localmente (ver abajo).

## Almacenamiento

| Mecanismo | Paquete / `.so` | Qué guarda (inferido) |
|---|---|---|
| **flutter_secure_storage** | `package:flutter_secure_storage` | `access_token`, `refresh_token`, y muy probablemente material de e.firma. Respaldado por **Android Keystore** (clave AES envuelta por el Keystore; cifra un XML de preferencias). |
| **sqflite** (SQLite) | `package:sqflite_common` | Datos locales de la app (catálogos, caché de trámites). |
| **DataStore** | `libdatastore_shared_counter.so` | Contador/flags compartidos entre proceso app y servicio background. |
| Sistema de archivos | FileProviders (doc 01) | Acuses/PDF temporales para compartir/imprimir. |

## Postura

- **A favor:** `allowBackup="false"` (no se respalda la e.firma a la nube/adb), uso de
  `flutter_secure_storage` (Keystore) en lugar de `SharedPreferences` en claro, y firmado local
  (la llave privada no necesita viajar).
- **A vigilar:**
  1. **Sin ofuscación del snapshot Dart.** Los literales (endpoints, nombres de rutas, posibles
     constantes) son legibles con `strings`. No se hallaron claves/secretos embebidos en este
     pase, pero la ausencia de ofuscación facilita futuros análisis. Contraste con el cifrado de
     strings DexGuard de CFE.
  2. **Dónde queda la llave privada tras cargarla.** PointyCastle opera en memoria Dart (fuera del
     Keystore de hardware). Mientras se firma, la llave RSA está en memoria del proceso; su ciclo
     de vida (si se borra tras usarse) no es determinable en estático.
  3. **`.key` protegida por la contraseña de la FIEL** la introduce el usuario; cómo se custodia
     esa contraseña (secure_storage vs memoria) está en Dart y no se trazó.

## Límite

Todo lo anterior son **capacidades presentes** (las librerías están, las cadenas están), no un
trazado del flujo. Confirmar el momento exacto del firmado, el no-almacenamiento de la llave y el
uso de AES-GCM requeriría instrumentación dinámica sobre `libapp.so`, fuera de este alcance.
