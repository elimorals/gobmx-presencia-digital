# Criptografía y almacenamiento local

## Primitivas presentes en el DEX

Constantes de algoritmo observadas como literales (vía `strings` sobre `classes*.dex`):

| Constante | Uso probable |
|---|---|
| `AndroidKeyStore` | Proveedor de claves respaldado por hardware (TEE/StrongBox) |
| `AES/GCM/NoPadding` | Cifrado autenticado de datos locales (sesión, tokens) |
| `AES/CBC/PKCS5Padding` | Cifrado simétrico (compat / payloads) |
| `PBKDF2WithHmacSHA256`, `PBKDF2WithHmacSHA1` | Derivación de clave desde PIN/contraseña |
| `SHA-256` | Hashing / integridad |

Combinación típica: clave maestra en **AndroidKeyStore** (no exportable), datos sensibles
cifrados con **AES-GCM**, y para material derivado de secreto del usuario (PIN) se usa
**PBKDF2**. La coexistencia de GCM y CBC sugiere una capa de compatibilidad o dos usos
distintos (local vs. payload hacia backend).

## Biometría ↔ Keystore

Con `USE_BIOMETRIC`, el patrón esperado es una clave de Keystore creada con
`setUserAuthenticationRequired(true)`; el `BiometricPrompt` autoriza su uso para descifrar el
refresh token guardado. No se confirmó el flag exacto por la ofuscación, pero las piezas
(Keystore + AES-GCM + biometría) están todas presentes.

## Llave pública embebida — `assets/crmkey_pub.pem`

Archivo de **816 bytes** en `assets/`. El nombre (`crmkey` = "CRM key") sugiere una clave
pública para **cifrar payloads** hacia el backend de CRM de CFE, o para **pinning** de una
firma de respuesta. Al ser pública, su exposición no es un secreto; su rol exacto (cifrado
asimétrico de un sobre AES, o verificación de firma) no se confirmó estáticamente.

> El PEM no se versiona (`.gitignore`); vive en `assets/crmkey_pub.pem` del APK extraído en el SSD.

## Almacenes de certificados Huawei — `assets/*.bks`

`grs_sp.bks`, `hmsincas.bks`, `hmsrootcas.bks` son **BouncyCastle KeyStores** del SDK de
Huawei (GRS = Global Routing Service). Contienen las CA raíz/intermedias que HMS usa para
validar sus propios servicios. No son de CFE; vienen con el SDK de Huawei.

## Almacenamiento de datos

- `android:allowBackup="false"` + `data-extraction-rules` con `<cloud-backup/>` vacío →
  **no se respaldan datos** a la nube ni a adb backup. Correcto para app financiera.
- No se detectó SQLCipher ni Realm; el almacenamiento estructurado probable es
  SharedPreferences cifradas (EncryptedSharedPreferences / Jetpack Security) y/o SQLite
  estándar en la zona privada de la app. No se confirmó un `.db` embebido de usuario.

## Red ↔ cripto

- `usesCleartextTraffic="false"` y sin `networkSecurityConfig` propio → TLS obligatorio por
  el default de la plataforma. No se halló evidencia de **certificate pinning** declarativo
  (network_security_config con `<pin-set>`); si existe pinning, está en código (OkHttp
  `CertificatePinner`) ofuscado. Ver doc 04.

## Credenciales de cliente Firebase (en `strings.xml`)

Son claves de **cliente** de Firebase/GCP, restringibles por `applicationId` + huella SHA del
certificado de firma; no son secretos de servidor por sí solas. Aun así, conviene verificar
en la consola de GCP del proyecto `506803695643` que la API key Android esté restringida.

```
google_api_key        = AIzaSyBC_8Af… (redactada; completa en strings.json)
google_app_id         = 1:506803695643:android:795214df7d4a207e
firebase_database_url = https://cfe-contigo.firebaseio.com
default_web_client_id = 506803695643-…apps.googleusercontent.com
```
