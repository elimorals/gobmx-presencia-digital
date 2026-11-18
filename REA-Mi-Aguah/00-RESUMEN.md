# Resumen — Mi Aguah 1.2.11 (`mx.gob.aguadehermosillo.miaguah`)

## Identidad
- **App:** Mi Aguah — **Agua de Hermosillo** (organismo operador de agua, Sonora). Consulta y
  **pago de recibos** de agua.
- **Package:** `mx.gob.aguadehermosillo.miaguah` · **versión** 1.2.11 (versionCode 108).
- **#45** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
**Flutter** (`flutter_assets/`, `libapp.so`/`libflutter.so` en el split arm64). Lógica en el
**snapshot Dart AOT** → muro (como Pemex ASISTE, Qrobús, SAT Móvil). **No hay `.env`** en
flutter_assets; la config vive en el snapshot.

## Lo observable
- **Backend de pago:** `https://ssl1.agua.gob.mx/pagatuagua/documentos/` (HTTPS).
- **Tarjeta in-app:** el snapshot Dart maneja `card`/`tarjeta`/`CVV` (captura en la app).
- **SDKs:** Firebase (analytics/FCM), Google Maps, **AdServices** (atribución de publicidad).
- **Contacto:** WhatsApp (`wa.me/...`), Twitter, Facebook de Agua de Hermosillo.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ Backend de pago HTTPS (`agua.gob.mx`); único cleartext es el link del sitio www |
| Pagos | ⚠️ Captura de tarjeta in-app; pasarela y tokenización (PCI) **no verificables** (muro Dart) |
| Privacidad | ⚠️ AdServices/AD_ID (tracking publicitario) en app de pago de agua |
| Identidad | ✅ Biometría local (USE_BIOMETRIC/FINGERPRINT) |
| Lógica | 🧱 Muro Dart AOT |
| Firma | ⚠️ Re-firmada por APKPure |

## Lo que NO se puede (muro)
Pasarela de pago exacta, si la tarjeta se tokeniza o viaja al backend, auth, y rutas de API bajo
`pagatuagua` → en el snapshot Dart, no observable estáticamente. Vía: reflutter/blutter/Frida
(dinámico, fuera de alcance). Documentado como límite, sin inventar.
