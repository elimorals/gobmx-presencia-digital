# 02 — Arquitectura: Flutter (Dart AOT), no nativa

## Corrección de pila con evidencia

El triage por `unzip -l` del APK base marcó "nativa Kotlin/Java" porque el **base no contiene los
`.so` de Flutter** — están en el split de arquitectura. Confirmación de que es Flutter:

- APK base: `assets/flutter_assets/...` (fuentes, graphics, json, `flutter_assets/assets/cert/`).
- Split `config.arm64_v8a.apk`:
  - `lib/arm64-v8a/libapp.so` — **8 258 480 bytes**, snapshot **Dart AOT** (código de la app).
  - `lib/arm64-v8a/libflutter.so` — 10 554 680 bytes, motor Flutter.
- Motor: **Flutter Engine 3.3.4 (stable)**, engine hash `e2621f417ff57ccab752a59bd8c938ade1bb9b4c`
  (cadenas de `libflutter.so`). Es un engine de ~2022: **runtime dated** (deuda, no vulnerabilidad por sí).

## El DEX no tiene lógica de negocio

`rea__search_android_classes(query="com.moobky")` sobre las **3 948** clases del DEX devuelve
**una sola** coincidencia: `com.moobky.MiPolicia.R` (recursos). La actividad de entrada es
`com.example.mipolicia_ssc.MainActivity` (un `FlutterActivity`), y el resto del DEX es el embedding de
Flutter (`io.flutter.*`) más el lado Android de los plugins. **No hay cliente HTTP, almacenamiento ni
cripto propios en el DEX** — todo eso ocurre en Dart dentro de `libapp.so`.

Implicación metodológica: el análisis profundo se hace sobre `libapp.so` (cadenas, endpoints,
símbolos de `dart:io` SecurityContext, tokens) y sobre los assets. La lógica Dart en sí (control de
flujo, estructuras de datos, manejo de PII) **no es recuperable a código fuente** desde el snapshot
AOT sin herramienta especializada de snapshots Dart; se marca como muro en el doc 08. Es el mismo
patrón que SAT Móvil y Urbani (ambas Flutter) ya documentadas en este repo.

## Qué SÍ se recuperó del snapshot

- Lista de endpoints y dominios (doc 03) — las URLs viajan como literales en el snapshot.
- Nombres de paquetes/plugins Dart (`package:geolocator/`, `package:http/`, …) (doc 05).
- Símbolos de `dart:io` relacionados con TLS (`SecurityContext_*`, `BadCertificateCallback`) (doc 04).
- Secretos embebidos (token `Bearer`, token en ruta) (doc 03/07).
