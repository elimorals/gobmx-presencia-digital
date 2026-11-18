# Resumen — CESPT (CesptApp) 3.1.1 (`com.cespt.cesptApp`)

## Identidad
- **App:** CESPT — Comisión Estatal de Servicios Públicos de Tijuana (agua/alcantarillado, BC).
  Consulta/pago de recibos, reporte de fugas, citas.
- **Package:** `com.cespt.cesptApp` · **versión** 3.1.1 (versionCode 53). APK ~135 MB.
- **#37** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
**Híbrida Capacitor/Ionic** (`assets/capacitor.config.json` + `assets/public/*.js`, bundle
Angular troceado). `appId` default `io.ionic.starter` (no personalizado). Plugins: Push
Notifications, InAppBrowser (Cordova), cámara. La lógica vive en el bundle web.

## Red / pagos / identidad
| Qué | Detalle |
|---|---|
| Backend propio | `https://www.cespt.gob.mx/API/...` (HTTPS) + páginas ASP.NET (`.aspx`) |
| Identidad | OAuth con **Llave Baja California** (`www.llave.bajacalifornia.gob.mx/oauth.xhtml`) |
| **Pago** | InAppBrowser → `https://www.cespt.gob.mx/pago/pcm.aspx` → pasarela **Bancomer** |
| Mapas | Google Maps Geocoding (`maps.googleapis.com`) con **API key embebida** |

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Endpoints propios | ✅ HTTPS |
| Pago | ✅ InAppBrowser a Bancomer → tarjeta en página del banco, no en la app |
| Identidad | ✅ Delegada a Llave BC (OAuth) |
| **Config de red (NSC)** | ⚠️ **cleartext global + confía en CAs de usuario → MITM trivial** |
| Secretos | ⚠️ Google Maps API key embebida (baja) |
| Backup | ⚠️ `allowBackup=true` (mitigado por BackupWebStorage=none) |

## Lectura
App de agua con buenas decisiones de alto nivel (pago delegado al banco, identidad a Llave BC,
API HTTPS), pero con una **config de red permisiva** (cleartext global + CAs de usuario) que
abre MITM sobre todo el tráfico — el agujero concreto a cerrar. La lógica es JS legible (sin
source maps), decompilable del bundle.
