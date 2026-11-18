# Protección — RASP, SQLCipher, TLS

## RASP: `libtool-checker.so`

Librería nativa de **detección de herramientas de análisis** (RASP — Runtime Application
Self-Protection). Por nombre y por el patrón Veritran, verifica en ejecución señales de:

- **root** (su/superuser, Magisk),
- **instrumentación** (Frida/Xposed/gadgets),
- **emulador** / entorno de depuración,
- posibles comprobaciones de integridad/tamper del propio APK.

Consecuencia práctica: el **análisis dinámico** (para abrir el muro Veritran, doc 02) tendría
que **evadir primero este RASP**, lo que normalmente exige root y parcheo — fuera del alcance
estático/sin-root de este repositorio. Es la misma barrera que en Banco del Bienestar.

> No se ejecutó la app, así que no se confirma qué comprueba exactamente `tool-checker` ni su
> reacción (cerrar, degradar). Se reporta su presencia y rol, no su comportamiento en runtime.

## Cifrado local: `libsqlcipher.so`

Presencia de **SQLCipher** → la base de datos local está **cifrada**. La llave y su derivación
(¿desde credencial? ¿Keystore? ¿PIN?) se gestionan en la capa Veritran interpretada, **no
observable** desde el DEX. Contrasta favorablemente con apps del acervo que guardan PII en
`SharedPreferences` planas (911 CDMX) o SQLite sin cifrar.

## TLS: `libconscrypt_jni.so`

**Conscrypt** empaquetado (proveedor TLS moderno de Google). Permite al app controlar la pila
TLS (incluido, potencialmente, pinning) de forma independiente a la del sistema. Si hay
certificate pinning, lo implementaría esta capa — pero **no es verificable estáticamente**
porque el cableado vive en el bundle Veritran.

## Balance

Pila de protección **fuerte y coherente** con una app bancaria: RASP + DB cifrada + TLS
dedicado + biometría con anti-spoofing (doc 03). La contrapartida para este análisis es que
**todo lo interesante queda tras el muro**: la postura se infiere de la presencia de las libs,
no de su comportamiento.
