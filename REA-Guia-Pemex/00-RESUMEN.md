# Resumen — Guía Pemex 3.6.3 (`com.pemex.guiapemex`)

## Identidad
- **App:** Guía Pemex — Petróleos Mexicanos. Localizador de estaciones de servicio Pemex.
- **Package:** `com.pemex.guiapemex` · **versión** 3.6.3 (versionCode 160). compileSdk 35.
- **#42** del inventario por instalaciones (≈100 K). **Firma:** AUTÉNTICA de Pemex (`O=PEMEX`).

## Pila
**Flutter** (`assets/flutter_assets/`, `libapp.so`/`libflutter.so` en el split de arquitectura).
Lógica en el **snapshot Dart AOT** → muro (como Pemex ASISTE, SAT Móvil, Urbani). El DEX es
embedding. Usa `flutter_dotenv` (`.env` como asset en claro).

## `.env` (en claro — el hallazgo central)
Valores reales en `evidence/flutter.env` (no versionado). Claves presentes:

| Clave | Qué es | Riesgo |
|---|---|---|
| `APP_SERVICES_ENDPOINT` = `https://api-cloud.pemex.com/guiapemex` | backend propio | host (ok) |
| **`APP_SERVICES_API_KEY`** | **credencial del backend de Pemex** | **Medio-Alto** — no es pública por diseño |
| `GOOGLE_MAPS_API_KEY` | mapas Google | Bajo-Medio si no está restringida |
| `TOM_TOM_KEY` | mapas TomTom | Bajo-Medio si no está restringida |
| `APPSINSIGHTS_INSTRUMENTATION_KEY` | telemetría Azure | Bajo |
| `COLOR_MAP_WEB_KEY` / `COLOR_MAP_MOBILE_KEY` | ColorMap | Bajo |

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS (backend y mapas); sin cleartext |
| Permisos | ✅ Acotados (ubicación + internet; sin cámara/contactos) |
| Secretos | ⚠️ **API key del backend + llaves de mapas en claro en `.env`** |
| Lógica | 🧱 Muro Dart AOT |
| Firma | ✅ Auténtica de Pemex |

## vs Pemex ASISTE
Misma empresa, mismo backend (`api-cloud.pemex.com/{asiste|guiapemex}`), mismo patrón
`flutter_dotenv`. Diferencia: **ASISTE** solo filtraba la clave de App Insights; **Guía Pemex**
filtra además la **API key del backend** + Maps + TomTom → peor higiene de secretos.

## Lo que NO se puede (muro)
Rutas de API bajo `/guiapemex`, auth, uso de la ubicación → en el snapshot Dart, no observable.
Vía: reflutter/blutter/Frida (dinámico, fuera de alcance). Documentado como límite, sin inventar.
