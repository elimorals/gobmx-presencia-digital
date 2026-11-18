# Resumen — Seguridad Inmediata 3.7.5 (`com.alertacontigo.mx`)

## Identidad

- **App:** Seguridad Inmediata / "Alerta Contigo" — **botón de pánico / alerta ciudadana** del
  **H. Ayuntamiento de Puebla**. Proveedor: **Cepo** (ensamblado `Cepo.CiudadanoMAUI`).
- **Package:** `com.alertacontigo.mx` · **versión** 3.7.5 (versionCode 136). compileSdk 36.
- **#50** del inventario por instalaciones (≈163 K). **Firma:** re-firmada por APKPure.

## Pila (en una línea)

**.NET MAUI** con **compilación AOT** (`libaot-*.dll.so`, 173 ensamblados) **envuelta en PairIP**
(`com.pairip.application.Application` + permiso `CHECK_LICENSE`). Doble protección: AOT (sin
metadata CLI) + PairIP (anti-tamper). → **muro para el análisis estático**.

## Lo confirmado (sin ejecutar)

- **Ensamblado propio:** `Cepo.CiudadanoMAUI` (AOT nativo; `evidence/dotnet-assemblies.txt`).
- **Tiempo real:** `Microsoft.AspNetCore.SignalR.Client` → hub persistente (central/C5).
- **Push:** `OneSignalSDK.DotNet` **con binding de Ubicación** → OneSignal recibe ubicación.
- **UI:** CommunityToolkit.Maui/Mvvm + MPowerKit.Lottie + InputKit.
- **Transporte:** `usesCleartextTraffic="true"` global, **sin** `networkSecurityConfig`.
- **Permisos:** FINE/COARSE location, CAMERA, RECORD_AUDIO, READ_CONTACTS, CALL_PRIVILEGED,
  CALL_PHONE, READ_PHONE_STATE, FOREGROUND_SERVICE, RECEIVE_BOOT_COMPLETED.

## Postura (balance)

| Aspecto | Veredicto |
|---|---|
| Transporte | ⚠️ `usesCleartextTraffic=true` global (sin NSC) — crítico para pánico **si** el backend usa http (no confirmado) |
| Privacidad | ⚠️ Ubicación a OneSignal (tercero) + permisos de emergencia extremos |
| Protección RE | ✅ AOT + PairIP (alta resistencia) |
| Lógica/backend | 🧱 Muro (AOT+PairIP): backend, hub, payload, cripto, pinning = desconocidos |
| Firma | ⚠️ Re-firmada por APKPure |

## El muro (lo que NO se puede afirmar)

Backend/URL, hub de SignalR, estructura del payload de la alerta (qué exactamente envía:
ubicación/audio/foto/identidad/contactos), cifrado de la alerta y TLS pinning → compilados a AOT
nativo (sin IL/PE recuperable: 0 headers PE, 0 metadata BSJB en el `.so`) y el DEX bajo PairIP.
**Desconocidos estáticamente.** Vía: análisis dinámico (Frida sobre el runtime .NET) tras
**evadir PairIP** — fuera de alcance. Documentado como límite, sin inventar.
