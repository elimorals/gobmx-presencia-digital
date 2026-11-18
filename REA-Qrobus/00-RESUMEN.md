# Resumen — Qrobús 12.7.9 (`mx.gob.iqt.qrobus`)

## Identidad
- **App:** Qrobús — transporte público de Querétaro (IQT). Planeación de viajes, pago, tarjeta.
- **Package:** `mx.gob.iqt.qrobus` · **versión** 12.7.9-release (versionCode 279).
- **#19** del inventario por instalaciones (≈500 K). **Firma:** re-firmada por APKPure.

## Pila
**Flutter** (`flutter_assets/`, `libapp.so`/`libflutter.so` en el split de arquitectura). Lógica
en el **snapshot Dart AOT** → muro (como SAT Móvil, Urbani, Mi Policía, Pemex). El DEX es
embedding + SDKs (Stripe, XMP).

## Lo observable (red / pagos)
- **Backend:** `https://admin.qrobici.com/api/v1/travels/trip` — dominio **de proveedor**
  (`qrobici.com`), no `gob.mx`.
- **Pagos:** **Stripe** (`api.stripe.com/v1/`) embebido.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS (backend y Stripe) |
| Dominio | ⚠️ Backend en `qrobici.com` (proveedor), fuera de `gob.mx` |
| Permisos | ⚠️ Ubicación en segundo plano + NFC + cámara + contactos |
| Lógica | 🧱 Muro Dart AOT (`libapp.so`) |
| Firma | ⚠️ Re-firmada por APKPure |

## Lo que NO se puede (muro)
Rutas de API bajo `/api/v1`, flujo de pago con Stripe (qué datos envía), auth, y el uso real de
la ubicación de fondo → en el snapshot Dart, no observable estáticamente. Vía:
reflutter/blutter/Frida (dinámico, fuera de alcance). Documentado como límite, sin inventar.
