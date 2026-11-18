# Arquitectura Flutter — el muro

## Confirmación

- `assets/flutter_assets/` presente; `libapp.so` (Dart AOT) + `libflutter.so` en el split de
  arquitectura. Triage preliminar la marcó "nativa" (las `.so` no están en el base) —
  **corrección: es Flutter**.
- El DEX es el embedding de Flutter + SDKs (Sentry, reCAPTCHA, Firebase, AdServices).
- **No hay `.env`** empaquetado (a diferencia de Pemex ASISTE): la configuración/backend va
  dentro del snapshot Dart.

## Por qué es un muro

La lógica (backend propio, auth, pago, manejo de la tarjeta, ubicación, biometría) se compila en
el **snapshot Dart AOT** dentro de `libapp.so`. JADX solo ve el host Flutter; no hay bytecode
Java/Kotlin con esa lógica. Recuperarla requiere **reflutter/blutter** o **Frida** (dinámico,
fuera de alcance). Se documenta el muro y lo recuperable del DEX (SDKs, doc 03), sin inventar el
contenido del snapshot.
