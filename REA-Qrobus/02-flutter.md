# Arquitectura Flutter — el muro

## Confirmación

- `assets/flutter_assets/` presente; `libapp.so` (Dart AOT) + `libflutter.so` en el split
  `config.*`. El triage preliminar la marcó "nativa" porque esas `.so` no están en el APK base
  (van en el split de arquitectura) — **corrección: es Flutter**.
- El DEX es el embedding de Flutter + SDKs: **Stripe** (`api.stripe.com`), XMP/metadata.

## Por qué es un muro

La lógica de negocio (planeación de viaje, pago con Stripe, auth, manejo de la tarjeta/NFC,
ubicación) se compila en el **snapshot Dart AOT** dentro de `libapp.so` — no hay bytecode
Java/Kotlin legible con esa lógica. JADX solo ve el host Flutter.

Recuperarla requiere herramientas de Flutter (**reflutter**, **blutter**) o **análisis dinámico**
(Frida sobre el runtime Dart) — ambas **dinámicas / fuera del alcance** estático de este
repositorio. Se documenta el muro y lo recuperable del DEX (red/pagos, doc 03), sin inventar el
contenido del snapshot.
