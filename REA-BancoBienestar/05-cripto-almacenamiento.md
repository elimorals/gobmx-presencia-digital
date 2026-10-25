# 05 · Criptografía y almacenamiento local

Estático. Se identifican las librerías y su presencia; el uso exacto (derivación de llaves, qué se
guarda) vive en la capa Veritran/`.szip` y **no** se determinó.

## Librerías criptográficas presentes

| Librería | Evidencia | Rol |
|---|---|---|
| **SQLCipher** (Zetetic) | `net/zetetic`, `lib/arm64-v8a/libsqlcipher.so` | Base de datos SQLite cifrada |
| **`com.veritran.vtcrypto`** | smali | Primitivas cripto del runtime Veritran |
| **SpongyCastle** | `org/spongycastle` | Bouncy Castle reempaquetado (cripto general) |
| **Conscrypt** | `org/conscrypt`, `libconscrypt_jni.so` | TLS / proveedor JCE |

## Almacenamiento local

- Presencia de **SQLCipher** indica base(s) cifrada(s) en el dispositivo. A diferencia de Licencia
  Federal Digital (donde se vio que la llave = PIN del usuario), aquí **no se pudo observar la
  derivación de la llave**: la apertura de la DB la gestiona el runtime Veritran/`vtcrypto`, no un
  servicio en claro. **No se afirma** de dónde sale la llave.
- No se identificó un esquema `seed.sql` legible (la definición de datos es parte del paquete Veritran).

## Qué quedaría por confirmar (dinámica)

- De dónde proviene la llave de SQLCipher (Keystore, PIN, derivada del servidor).
- Qué datos se persisten localmente y si incluyen credenciales/tokens.
- Uso real de `vtcrypto` (cifrado de payloads, almacenamiento de secretos).

Todo esto requiere desempacar el `.szip` o instrumentar el runtime, bloqueado por el RASP (doc 04).

## Contraste

| | Licencia Federal Digital | Banco del Bienestar |
|---|---|---|
| DB cifrada | SQLCipher | SQLCipher |
| Llave observable en estático | **Sí** (= PIN) | **No** (gestionada por Veritran/vtcrypto) |
| Cripto adicional | — | SpongyCastle, Conscrypt, vtcrypto |
