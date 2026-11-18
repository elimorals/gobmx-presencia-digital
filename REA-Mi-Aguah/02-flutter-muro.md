# Arquitectura Flutter — el muro

## Confirmación
- `assets/flutter_assets/` presente; `libapp.so` (Dart AOT) + `libflutter.so` en el split
  `config.arm64_v8a`. El triage sobre el APK base marcaría "nativa" porque las `.so` no están en
  el base — **es Flutter**.
- **No hay `.env`** en `flutter_assets` (solo `FontManifest`/`NativeAssetsManifest`): la config y
  el endpoint de pago van dentro del snapshot Dart (algunos dominios quedan legibles como strings
  en `libapp.so`, doc 04).

## Por qué es un muro
La lógica (auth, flujo de pago, manejo de tarjeta, pasarela) se compila en el **snapshot Dart
AOT** dentro de `libapp.so`. JADX solo ve el host Flutter; no hay bytecode Java/Kotlin con esa
lógica. Recuperarla requiere **reflutter/blutter** o **Frida** (dinámico, fuera de alcance). Se
documenta el muro y lo recuperable de strings (doc 03/04), sin inventar el contenido del snapshot.
