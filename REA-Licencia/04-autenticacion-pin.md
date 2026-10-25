# 04 · Autenticación y PIN

Reconstruido de las rutas Angular y los servicios en `assets/public/`. Estático.

## Pantallas del flujo

`welcome → login → (pin-assigment | pin-login) → pin-verification → code-verification → get-license`

| Ruta | Módulo | Rol (inferido del nombre + código visible) |
|---|---|---|
| `login` | `pages-login-login-module` | Autenticación inicial contra WSLFD (credenciales). Detalle del request no literal — ver doc 03. |
| `pin-assigment` | `pages-pin-assigment-*` | Alta del PIN local la primera vez |
| `pin-login` | `pages-pin-login-*` | Reingreso con PIN; abre la DB y carga el usuario |
| `pin-verification` | `pages-pin-verification-*` | Verificación del PIN; abre la DB y guarda el usuario |
| `code-verification` | `pages-code-verification-*` | Verificación por código (probable OTP/SMS; no se afirma el canal) |
| `get-license` | `pages-get-license-*` | Descarga/obtención de la licencia tras autenticar |

## El PIN

- **Numérico.** Validador de entrada `/[0-9]/` (sólo dígitos) en las páginas de PIN.
- **Longitud: no determinada estáticamente.** La define la plantilla Angular (número de casillas),
  compilada en el módulo; no se encontró un literal `maxlength`/comparación de longitud fiable.
  No se afirma 4 vs 6 dígitos. (Lo típico en estos flujos es 4–6; se confirmaría en runtime.)

## Uso del PIN como llave de cifrado

El dato central: el PIN **no sólo** autentica localmente, **es la llave de la DB SQLCipher**.

```js
// pages-pin-login-pin-login-module-es2015.js
let model = new PinModel(this.pinForm);
this.dbService.openDB(model.pin)
  .then(_ => { this.db.getUser().then((data) => {
    this.storage.set("user", JSON.stringify(data));
    this.licenseService...
```

```js
// pages-pin-verification-pin-verification-module-es2015.js
let usr = JSON.parse(val); usr.pin = model.pin;
this.dbService.openDB(model.pin)
  .then(_ => { this.userService.saveUser(JSON.stringify(usr)) ... });
```

`openDB(key)` → `this.sqlite.create({ name:'sct.db', location:'default', key })` (ver doc 05).
Implicación de seguridad y severidad: **doc 05**.

## Observaciones

- Tras `pin-verification`, el objeto usuario (con `usr.pin`) se persiste vía `saveUser`
  (en la tabla `user`, que tiene columna `pin` y `password` — ver `seed.sql`).
- El usuario también se copia a `Storage` de Ionic (`this.storage.set("user", ...)`). Según el
  motor de `@ionic/storage`, ese store puede **no** estar cifrado (SQLite/IndexedDB plano). No se
  determinó el backend de `Storage` configurado; se marca como punto a verificar, no se afirma.
