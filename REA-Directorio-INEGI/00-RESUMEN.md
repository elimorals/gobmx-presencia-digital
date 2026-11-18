# Resumen — DENUE 3.1 (`mx.org.inegi.denuemv`)

## Identidad
- **App:** DENUE — Directorio Estadístico Nacional de Unidades Económicas, **INEGI**. Mapa y
  consulta de establecimientos económicos (datos abiertos).
- **Package:** `mx.org.inegi.denuemv` · **versión** 3.1 (versionCode 61). compileSdk 33.
- **#47** del inventario por instalaciones (≈100 K).
- **Firma:** AUTÉNTICA del INEGI (`CN=Alberto Ramos Lazcano, O=INEGI, L=AGS`).

## Arquitectura
Nativa Android (Kotlin/Java, 2007 clases, 1 dex, **ofuscada con R8**; única `.so` = `libblur.so`,
efecto de UI). Módulos propios (`decompiled/clases-propias.txt`):
- `ficha/` — ficha del establecimiento (BottomSheet) + `GaleriaActivity` + `StreetViewActivity`
- `ar/` — **realidad aumentada** (`ArActivity`, cámara) para ver establecimientos
- `widget/` — widget de inicio · `historial/`, `favorito/` · `analytics/google/` (Google Analytics)

## Datos y red
- **Datos públicos** (DENUE open data). Endpoints HTTPS oficiales:
  `https://www.inegi.org.mx/app/mapa/denue/default.aspx` y `/temas/directorio/`.
- Terceros: **ads** (`pagead2.googlesyndication.com`), **Google Analytics** (deprecado), goo.gl.
- **Google Maps** (API key embebida, doc 04).

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| PII | ✅ Mínima (datos públicos; solo ubicación/cámara del usuario) |
| Transporte | ✅ HTTPS oficial, sin bypass TLS, sin cleartext |
| Firma | ✅ Auténtica del INEGI |
| Maps key | ⚠️ Embebida (restricción no verificable) |
| Privacidad | ⚠️ Ads + Analytics + AD_ID en app federal |
| Mantenimiento | ⚠️ Build viejo (targetSdk 33, GMS Analytics/goo.gl deprecados) |

**Lectura:** app de **bajo riesgo** — consume datos abiertos, no maneja credenciales ni PII
sensible, y su transporte es sano. Las observaciones son de **privacidad** (telemetría/ads) y
**mantenimiento** (build obsoleto), más la buena práctica pendiente de restringir la Maps key.

## Límite
Código propio **ofuscado (R8)** → el trazado función por función de la UI es limitado; la data
de establecimientos se carga en helpers ofuscados (`o4.b`/`o4.c`) desde los endpoints HTTPS del
INEGI. Documentado como límite, sin inventar.
