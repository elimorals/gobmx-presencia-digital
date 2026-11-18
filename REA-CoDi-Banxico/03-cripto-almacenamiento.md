# Criptografía y almacenamiento

Confirmado por la estructura de las clases `ddspim.mobile.kt.securityservices.engine.*` (motor
cripto) + constantes en el DEX. El motor está ofuscado (métodos `a/c/e/g/i` → variantes de
cifrar/descifrar/firmar/hashear/codificar sobre `String`), pero los **algoritmos y el almacén de
llaves** son observables.

## Almacén de llaves — AndroidKeyStore atado a biometría

- **`AndroidKeyStore`** (constante en el DEX) → las llaves se generan/guardan en el **almacén de
  llaves del sistema** (respaldado por hardware/TEE cuando el dispositivo lo soporta), no en
  ficheros propios.
- **`setUserAuthenticationRequired`** → las llaves **requieren autenticación del usuario**
  (biometría/credencial) para usarse. Combinado con los permisos `USE_BIOMETRIC`/`USE_FINGERPRINT`
  (doc 01), significa que firmar un cobro o descifrar credenciales **exige la biometría**.
- Almacenamiento auxiliar: `CoDi_SP` (SharedPreferences) y `codi.db` (SQLite) para estado/
  historial; los secretos sensibles se protegen con las llaves del Keystore, no en claro.

## Algoritmos (constantes del DEX)

| Primitiva | Uso (inferido) |
|---|---|
| **EC** (156 referencias) + `SHA-256withECDSA`/SHA-512 | **firma digital de las operaciones CoDi** (esquema de Banxico: cada mensaje de cobro va firmado con la llave EC del cobrador) |
| **AES/GCM/NoPadding** | cifrado **autenticado** (confidencialidad + integridad) de datos locales/payloads |
| AES/CBC/PKCS7Padding, AES/CBC/PKCS5Padding | cifrado por compatibilidad |
| **HmacSHA256** | MAC / derivación |
| SHA-256 / SHA-512 / SHA-1 | hashing (SHA-1 probablemente legacy/compat; el pinning usa SHA-256, doc 04) |

## Valoración

Postura criptográfica **ejemplar** para una app de pagos:
- llaves en **AndroidKeyStore** con **uso atado a biometría** (no exportables, no en claro),
- **firma EC** de las operaciones (no repudio),
- **AES-GCM** (cifrado autenticado, no solo CBC).

Contrasta con el resto del acervo (p. ej. 911 CDMX guarda PII en SharedPreferences planas;
Infonavit/Licencia EDOMEX desactivan TLS). Aquí la cripto está bien elegida y bien anclada.

## Límite (sin inventar)

El motor `securityservices.engine.*` está **ofuscado**: se confirman el almacén (AndroidKeyStore),
el requisito de autenticación y los algoritmos, pero los **parámetros exactos** (tamaños de llave,
curva EC concreta, esquema de derivación, IV/nonce handling de GCM) no se recuperaron método a
método. Se reporta lo observable con evidencia; lo fino queda tras la ofuscación.
