# 05 · Criptografía local (SQLCipher) y datos en el dispositivo

El hallazgo de mayor impacto del análisis. Todo con evidencia del bundle JS y `seed.sql`.

## Qué se guarda

Base `sct.db` (SQLCipher). Ubicación:
- Android: `${applicationStorageDirectory}/databases/sct.db`
- iOS: `${applicationStorageDirectory}/Library/LocalDatabase/sct.db`

Esquema (`assets/seed.sql`, incrustado en el APK):

| Tabla | Columnas sensibles |
|---|---|
| `user` | `curp`, `phone`, `email`, **`password`**, **`pin`** |
| `licenses` | `nombre`, `appellido_paterno/materno`, **`rfc`**, `nacionalidad`, `num_licencia`, `inicio/fin_vigencia`, `num_expediente_med`, `tipo_licencia`, `capacidades`, `empresa`, `transporte`, `curp` … (licencia federal completa) |
| `remarks` | anotaciones por licencia |
| `media` | `mime`, `value` (medios de la licencia — probable foto/firma en base64) |

> Es decir: la DB local contiene **CURP, RFC, teléfono, email, la contraseña de la cuenta, el PIN
> y la licencia federal de conducir completa** (incluido expediente médico).

## Cómo se cifra

`DatabaseService` (en `main-es2015.js`):

```js
let DatabaseService = class DatabaseService {
  constructor(plt, sqlitePorter, sqlite, http) { ... }
  openDB(key) {
    return ...(function* () {
      return this.sqlite.create({ name: 'sct.db', location: 'default', key: key })
        .then((db) => { this.database = db; this.seedDatabase(); return db; });
    });
  }
  // seedDatabase() hace this.http.get('assets/seed.sql', ...) y lo importa con sqlitePorter
}
```

La `key` que recibe `openDB` es **el PIN del usuario** (`model.pin`), pasado desde `pin-login` y
`pin-verification` (ver doc 04).

## Hallazgo 1 — llave de baja entropía protege PII fuerte

`SQLCipher` deriva la clave AES con PBKDF2, lo que **encarece** el ataque por intento, pero el
secreto de entrada es un **PIN numérico corto** (4–6 dígitos ⇒ 10,000–1,000,000 combinaciones).
Con el archivo `sct.db` extraído del dispositivo (p. ej. backup, dispositivo rooteado o robado),
el espacio de llaves es **enumerable offline**. La PII protegida es alta (CURP, RFC, contraseña,
licencia). → **llave de cifrado débil para el valor del dato**.

- **Ceiling / lo que no se afirma:** no se verificó el número de iteraciones PBKDF2 ni los
  parámetros de KDF que usa esta versión de `cordova-sqlcipher-adapter` (podría ser el default de
  SQLCipher 4: 256k iteraciones). Eso ajusta el coste del bruteforce pero no cambia que el dominio
  del PIN es pequeño y enumerable. La vía de corrección es llave de alto entropía en Keystore/Keychain
  envuelta por el PIN (o `biometric`), no el PIN como llave directa.

## Hallazgo 2 — ruta que abre la DB SIN llave

`UserService.getUser()` (en `common-es2015.js`):

```js
getUser() {
  this.databaseService.openDB();            // <-- sin argumento ⇒ key = undefined
  return this.databaseService.getDatabaseInstance().then((db) => ...);
}
```

`openDB()` sin argumento pasa `key: undefined` a `sqlite.create`. Con `cordova-sqlcipher-adapter`,
una llave vacía/indefinida abre la base **sin cifrar**.

- **Ceiling / duda honesta (no verificada en runtime):** el impacto real depende del orden de
  ejecución. Si `getUser()` corre **después** de un `openDB(pin)` previo, `create` probablemente
  devuelve/usa la instancia ya abierta con llave (no re-crea sin cifrar). Si corre **antes** (p. ej.
  primer arranque, o un camino que no pasa por PIN), podría crear/abrir `sct.db` **sin cifrado**,
  dejando la PII en claro en disco. No se determinó estáticamente cuál ocurre; **se marca como
  debilidad probable a confirmar con dinámica**, no como hecho.

## Hallazgo 3 — `password` y `pin` almacenados localmente

La tabla `user` guarda `password` y `pin` como columnas `text`. No hay evidencia en el esquema de
hash/salt; se guardan tal cual dentro de la DB (cuyo único cifrado es el del PIN). Si el Hallazgo 2
aplica en algún camino, quedarían en claro.

## Correcciones sugeridas (resumen)

1. No usar el PIN como llave SQLCipher. Generar llave aleatoria de 256 bits, guardarla en
   **Android Keystore / iOS Keychain** y desbloquearla con PIN/biometría.
2. Eliminar la ruta `openDB()` sin llave (exigir `key` siempre).
3. No persistir `password` en claro en local; idealmente no persistirla.
4. Quitar el host de QA del build de producción (doc 03).
