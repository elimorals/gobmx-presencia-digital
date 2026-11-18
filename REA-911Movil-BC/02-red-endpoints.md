# 02 · Red y endpoints

Fuente: `assets/app.config` (Expo) + strings de `assets/index.android.bundle` (Hermes). La lógica
de red está compilada en el bytecode Hermes → **no se pudo leer el código de las llamadas**; lo
documentado son los **hosts embebidos** (strings/config), no la construcción exacta de cada request.

## Backends (gob.mx, oficiales de BC)

| Host | Rol inferido | Esquema visto |
|------|--------------|---------------|
| `066movilbc.seguridadbc.gob.mx` | API principal 066/911 móvil | (NSC permite http) |
| `911bc.gob.mx` | Portal/API 911 BC | `http://` **y** https |
| `www.seguridadbc.gob.mx` | Seguridad Pública BC | `http://` **y** https |
| `tijuana.c4bcdev.gob.mx` | **Entorno de desarrollo** (`c4bcdev`) de Tijuana | `http://` **y** https |

Los 4 están **whitelisteados para cleartext** en el NSC (ver [03](03-tls-cleartext.md)).

## Observaciones

- El bundle solo contiene **2** ocurrencias de `http://` (una de ellas `www.seguridadbc.gob.mx`);
  el resto de hosts http vienen del `app.config`. El grueso de URLs del bundle son de
  documentación de librerías (react.dev, reactnavigation.org…), no endpoints de la app.
- **No se hallaron** llaves/API keys ni tokens en claro en los strings del bundle (ni `AIza…`, ni
  `api_key`, ni `bearer`). Si existen, están tras el muro Hermes o del lado servidor.
- SDKs de terceros detectados por lib/strings: Expo, React Native (Hermes), Firebase Cloud
  Messaging, Google ML Kit (barcode).
