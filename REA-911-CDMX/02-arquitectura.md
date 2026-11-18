# 02 — Arquitectura y capa de red

## Pila

App **nativa Android** escrita en Java/Kotlin, compilada a 2 DEX (6 313 clases). No usa
ningún framework multiplataforma. Confirmación por contenido del APK:

```
classes.dex        8.9 MB
classes2.dex       5.0 MB
lib/arm64-v8a/libpl_droidsonroids_gif.so   (solo GIF, de terceros)
```

No hay `libflutter.so`/`libapp.so` (Flutter), `libil2cpp`/`libunity` (Unity),
`libassembly-store.so`/`assemblies/*.dll` (.NET MAUI), `assets/public|www` (Capacitor)
ni `index.android.bundle` (React Native). El triage preliminar (nativa) fue correcto.

## Organización del código

Arquitectura clásica Activity/Fragment + Services + SharedPreferences, sin inyección de
dependencias ni capa MVVM observable. Paquetes propios bajo `com.desarrollo.c5.app_066_android_2`:

- `activities/` — pantallas contenedoras (Splash, Registro, Navigation, Configuracion, Alert).
- `fragments/` — la UI real (botón de auxilio, ficha de salud, red de emergencia, chat, extorsión, anuncios, registro, protección de datos).
- `services/` — `NotificationService` (FCM), `SiguemeService` y `LocationService` (ubicación en primer plano).
- `receivers/` — `PhonecallReceiver` (estado de llamada), `SiguemeReceiver`, `SolicitudReceiver`, `DissmisReceiver`.
- `utils/` — infraestructura: `VolleySingleton`, `DataRegister`, `App066PreferenceManager`, `packager/` (modelos de PII), `validator/`, `database/DB_Manager` + `dao/…Sqlite` (SQLite local).
- `model/` — modelos de vista y de SQLite.

## Capa de red: Volley, sin TLS custom

El único cliente HTTP es `utils.VolleySingleton`, un singleton estándar de Volley.
Cuerpo decompilado (cita literal, `decompiled/VolleySingleton.getRequestQueue.java`):

```java
public RequestQueue getRequestQueue() {
    if (this.requestQueue == null) {
        this.requestQueue = Volley.newRequestQueue(this.mContext);
    }
    return this.requestQueue;
}
```

`Volley.newRequestQueue(context)` crea la cola con el **stack por defecto**
(HttpURLConnection). La clase tiene **solo 3 campos** (`instance`, `mContext`,
`requestQueue`) y 4 métodos: **no hay `HurlStack` personalizado, ni `SSLSocketFactory`,
ni `CertificatePinner`, ni `HostnameVerifier` custom**. Conclusión con evidencia:

- **No hay certificate pinning** ni validación TLS reforzada.
- No hay `getUnsafeOkHttpClient` ni desactivación de validación (no es el caso Infonavit);
  simplemente **no hay defensa de transporte**, y como los endpoints son `http://`
  (doc 03), la validación TLS ni siquiera entra en juego.

Las peticiones concretas se construyen en los fragments/services usando las constantes de
`utils.DataRegister` (el catálogo de endpoints) y se encolan con
`VolleySingleton.addToRequestQueue(...)`.

## Persistencia

- **SharedPreferences** (`App066PreferenceManager`) para toda la PII y estado (doc 05).
- **SQLite** (`DB_Manager` + `SolicitudesRedEmergenciaDaoSqlite`) para las solicitudes de
  la red de emergencia. Es SQLite plano (no SQLCipher): no se observan dependencias de
  cifrado de base de datos.
