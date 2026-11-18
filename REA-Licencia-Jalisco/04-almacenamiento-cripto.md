# Almacenamiento y criptografía

## SQLCipher (DB local cifrada)
Igual que Licencia EDOMEX, la app empaqueta **SQLCipher** (`net.sqlcipher.*` presente en el DEX)
→ la base de datos local donde se guarda la licencia y la PII está **cifrada**. Aparece también
la cadena `SELECT sqlcipher_export('plaintext')` (utilería de SQLCipher para exportar/migrar).

- **Qué guarda (inferido del dominio):** la licencia digital, datos del titular (nombre, CURP/RFC,
  foto), y el estado de verificación facial. El detalle fino (qué columnas, con qué llave) está
  en las capas de datos/Room; no se decompiló columna por columna porque el hallazgo dominante es
  el de transporte (doc 02).
- **Derivación de la llave:** no se confirmó estáticamente (en EDOMEX era una llave aleatoria por
  instalación en `Preferences`/`ObscuredSharedPreferences`; es plausible que Jalisco use el mismo
  mecanismo del white-label, pero **no se verificó aquí** → se deja como no confirmado, sin
  inventar).

## Criptografía observada
- **BouncyCastle** presente (`org.bouncycastle`) — operaciones cripto (probable firma/validación
  de la licencia o del QR).
- **Regula Forensics** para la verificación biométrica/documental (doc 03).

## Matiz importante
El cifrado en reposo (SQLCipher) es correcto, pero queda **eclipsado por H1**: con la validación
TLS desactivada (doc 02), lo que se protege en el disco viaja **sin protección por la red**. Un
MITM obtiene la PII en tránsito aunque la DB local esté cifrada.
