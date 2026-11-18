# Claves nativas — `libapi-keys.so`

La app intenta "ocultar" claves de API en una librería nativa, `libapi-keys.so`
(`lib/arm64-v8a/`, también armeabi-v7a/x86/x86_64). Compilada de `api-keys.c`. El patrón no
protege nada: las claves y rutas salen con un simple `strings`
(→ `evidence/libapi-keys-strings.txt`).

## Funciones JNI expuestas

```
Java_com_embsoft_vinden_helper_NativeKeysHelper_getPlacesKey
Java_com_embsoft_vinden_helper_NativeKeysHelper_sharedPrefKey
Java_com_d2dreamdevelopers_myapplication_APIKeyLibrary_getAPIKey
```

- `NativeKeysHelper.getPlacesKey()` → clave de **Google Places/Maps**.
- `NativeKeysHelper.sharedPrefKey()` → sugiere que la **llave de cifrado de SharedPreferences**
  (o su nombre) también se deriva de la `.so` (ver doc 06, greenDAO/almacenamiento).
- `APIKeyLibrary.getAPIKey` del namespace `com.d2dreamdevelopers.myapplication` → código base
  de ejemplo reutilizado (otro indicio del origen white-label).

## Hallazgo 1: clave de Google en claro

```
AIzaSy… (Google API key, 39 chars; completa en evidence/libapi-keys-strings.txt)
YOUR_AWESOME_API_GOES_HERE   ← segundo slot dejado como plantilla
```

La clave sale como string plano del `.so`. Debe tratarse como **expuesta** y conviene
**restringirla** en la consola de Google Cloud (por package + firma SHA-1, y por API) si no lo
está ya. El placeholder `YOUR_AWESOME_API_GOES_HERE` revela que hay un segundo hueco de clave
sin poblar en esa build.

> La clave completa no se versiona (vive en `evidence/`, gitignored) para no redistribuir un
> secreto en claro en el repo.

## Hallazgo 2: fuga de identidad del desarrollador

El `.so` embebe la ruta de compilación:

```
/Users/eduardoezequielargaezmay/Documents/workspace/GitHub/TRANSPORTE_APP_ANDROID/app/src/main/jni/api-keys.c
```

Revela el usuario de macOS del desarrollador, la organización del repo y el nombre del
proyecto (`TRANSPORTE_APP_ANDROID`). Combinado con los namespaces `com.embsoft.vinden` y
`com.d2dreamdevelopers`, y los dominios `mrptechnology.mx`, perfila al proveedor
(**embsoft / MRP Technology**) y a un colaborador por nombre. Es fuga de información, no un
riesgo de explotación directa.

## Contraste: la API key del servicio SÍ está cifrada

No todas las claves están igual de expuestas. La **clave de la API del backend** (no la de
Google) se guarda **cifrada** y se descifra en runtime (doc 09,
`decompiled/native/ApiClientImp.modifyRequestHeaders.java`):

```java
String apiKey = CryptoHelper.decrypt(cfg.getAPI_KEY_SERVICE().getApiKey(),
                                     appPreferences.getKy());
```

- La clave del servicio viaja **cifrada** en el binario y se descifra con una llave por
  instalación (`appPreferences.getKy()`). Mejor práctica que la clave Google en claro del `.so`.
- `NativeKeysHelper.sharedPrefKey()` (en el `.so`) sugiere que el nombre/llave de las
  SharedPreferences también se deriva de la lib nativa.

**Matiz para el informe:** el equipo protege la API key del backend (cifrado + llave por
dispositivo), pero deja la clave de Google **en claro** en la misma app. Es una inconsistencia,
no una ausencia total de higiene de secretos.

## Compilador

`clang 18.0.1` (Android NDK, `+pgo +lto`), sin ofuscación del `.so`: los símbolos JNI y las
cadenas están en claro.
