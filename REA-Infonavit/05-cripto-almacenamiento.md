# 05 · Criptografía y almacenamiento

## `CryptoHelper` — AES/ECB con llave embebida (H3, Media)

`com.infonavit.domain.Util.CryptoHelper` (`evidence/smali/CryptoHelper.smali`) es el helper que
"protege" las credenciales de servicio del doc 04. Esquema observado:

```
decrypt(String s):
    bytes = Base64.getDecoder().decode(s)
    bytes = aesDecrypt(bytes)            # AES/ECB/NoPadding con SecretKeySpec(key,"AES")
    bytes = xorBytes(bytes)              # paso XOR adicional
    return new String(bytes)
```

- Constructor `(<init>(String,String,String,String,String))`: recibe cinco strings (el material
  de llave/semilla — los `"9B&cV0sT"`, `"Zq!8Rt5$kLm2"`, `"mQ#4vR8z"`, `"Xy6$P1wU"` del `<clinit>`
  de `RetrofitService`/`EnviromentRetrofit`, más una quinta cadena) y los pasa por `getBytes`.
- **`AES/ECB/NoPadding`** es modo débil: ECB no oculta patrones de bloque y no usa IV. Combinado
  con una **llave hardcodeada en el mismo APK**, el cifrado es reversible por cualquiera que tenga
  el binario; el paso XOR añadido es ofuscación, no seguridad.

Problema de diseño, no de implementación puntual: la confidencialidad de las credenciales depende
de un secreto que viaja junto a ellas. (Ver recomendación en doc 04.)

> No se ejecutó el descifrado; el esquema se lee del smali. No se afirma el valor en claro de las
> credenciales.

## Inventario de primitivas criptográficas

Conteo de literales sobre todo el DEX (incluye BouncyCastle/PGPainless, no solo código INFONAVIT):
`SHA-256` (89), `RSA` (82), `SHA-512`, `AES` (49), `AES/GCM/NoPadding` (19), `AES/CBC/PKCS5Padding`
(6), `AES/ECB/NoPadding` (5+), `DES` (18, de BouncyCastle), `PBKDF2`, `HmacSHA256/512`, etc. La
mayoría proviene de las librerías de cripto; el uso **propio** de INFONAVIT confirmado es el
`AES/ECB` de `CryptoHelper`. La presencia de `AES/GCM/NoPadding` corresponde a
`EncryptedSharedPreferences` (abajo), que es el modo correcto.

## Almacenamiento seguro (positivo)

El módulo biométrico usa almacenamiento respaldado por hardware correctamente:

- `com.infonavit.micuentainfonavit.ui.biometric.BiometricPreferences` y `BiometricCryptoManager`
  usan `EncryptedSharedPreferences` / `MasterKey` / `AndroidKeyStore`.

Es decir: el secreto biométrico (y lo que guarde ese módulo) se protege con una llave del
AndroidKeyStore y AES-GCM, no con el esquema débil de `CryptoHelper`. `CryptoHelper` se usa para
las credenciales de servicio embebidas, no para los datos del usuario en reposo.

## Backup

`allowBackup="false"` + `fullBackupContent="false"` en el manifiesto → el sistema no respalda los
datos de la app (positivo frente a extracción vía `adb backup`).

## Resumen

| Aspecto | Estado |
|---|---|
| Credenciales de servicio | AES/ECB + XOR con llave embebida → reversible (débil) |
| Datos biométricos en reposo | EncryptedSharedPreferences + AndroidKeyStore (correcto) |
| Backup del sistema | deshabilitado (correcto) |
| Tráfico en reposo→red | HTTPS pero sin validación TLS (ver doc 03) |
