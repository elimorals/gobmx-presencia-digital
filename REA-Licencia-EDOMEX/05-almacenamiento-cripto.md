# Almacenamiento y criptografía

A diferencia de la licencia **federal** (`REA-Licencia`: SQLCipher con llave = PIN, y una ruta
que abría la DB sin llave), EDOMEX tiene una postura de almacenamiento **más sólida**.

## Base de datos cifrada (SQLCipher)

- Room (`db.MovilidadDataBase`) sobre **SQLCipher**: el smali contiene **3 762** referencias a
  `net/sqlcipher`, **12** a `SupportFactory` (el puente SQLCipher↔Room) y PBKDF2
  (`PBKDF2WithHmacSHA*`, `PBKDF2KeySpec`) para derivar la llave.
- Entidades cifradas en la DB: `LicenseEntity` (la licencia), `ElectronicSignData` (firma
  electrónica), `DataIsoEntity`, `ConfigEntity`.

## La llave: aleatoria por instalación

`decompiled/RandomKeyGenerator.smali` → `generate(int)`:

```smali
const-string v1, "room.db.id"
new-instance v0, Ljava/security/SecureRandom;
SecureRandom->nextBytes([B)                      # material aleatorio
... Preferences$Companion->getString("room.db.id")   # se persiste/lee en Preferences
```

La passphrase de SQLCipher es un valor **aleatorio generado con `SecureRandom`** la primera vez
y guardado bajo la clave `room.db.id` en `Preferences`. No es un PIN ni un valor fijo → corrige
la debilidad de la app federal.

**Matiz honesto:** la llave vive en las *SharedPreferences* de la app. Existe
`utils.ObscuredSharedPreferences` (prefs **ofuscadas**, no cifradas con Keystore). Si `Preferences`
se respalda en `ObscuredSharedPreferences`, la protección de la llave es **ofuscación, no cifrado
respaldado por hardware**: en un dispositivo con root la llave es recuperable y con ella la DB.
`allowBackup="false"` (doc 01) mitiga el respaldo por ADB. No se confirmó si `Preferences` usa
además `EncryptedSharedPreferences`/Keystore → se deja como límite, sin afirmar lo contrario.

## Cifrado simétrico de propósito general

`decompiled/AES256Cipher.smali`:

```smali
const-string v1, "AES"
...IvParameterSpec([B)          # IV provisto
...SecretKeySpec([B, "AES")     # llave provista
const-string p2, "AES/CBC/PKCS5Padding"
```

**AES/CBC/PKCS5Padding** (modo CBC, **no ECB** — a diferencia de Apoyo Ciudadano INE). La llave y
el IV se reciben como parámetro; no se halló una llave fija embebida en esta clase. Correcto
siempre que el IV sea aleatorio por operación (no se trazó el origen del IV en cada llamada →
límite).

## Firma electrónica local

`domain.GetESignatureUseCase` + `db.entity.ElectronicSignData` → la app guarda/usa una firma
electrónica de la licencia localmente (en la DB cifrada), insumo del QR (doc 06).

> Resumen: almacenamiento = **la fortaleza** de esta app. Transporte (doc 04) = su debilidad.
