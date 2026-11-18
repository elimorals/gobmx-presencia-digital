# Ingeniería inversa de DENUE (Directorio INEGI) 3.1 — índice

Análisis **estático** del APK `mx.org.inegi.denuemv` con REA (JADX headless). La app **nunca se
ejecutó**. Es la app oficial del **Directorio Estadístico Nacional de Unidades Económicas
(DENUE)** del INEGI — mapa/consulta de establecimientos económicos (datos abiertos). **#47 por
instalaciones** del inventario gob.mx (≈100 K).

> **Nativa Android (Kotlin/Java), ofuscada con R8, de datos PÚBLICOS.** Consume el DENUE (datos
> abiertos de negocios), con **mapa de Google**, **realidad aumentada** (cámara) y Street View.
> Al ser datos públicos, el riesgo de PII es bajo: lo único sensible es la **ubicación del
> usuario**. Lo observable: una **Google Maps API key embebida** (normal, pero debe estar
> restringida), **ads + Google Analytics + AD_ID** en una app de estadística federal (observación
> de privacidad), y un build **viejo** (targetSdk 33, GMS Analytics deprecado). Transporte HTTPS
> a dominios oficiales, sin bypass TLS. Firma **auténtica del INEGI**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación, cámara, AD_ID) |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Nativa R8, módulos (mapa/AR/ficha/widget) |
| 03 | [03-red-endpoints-denue.md](03-red-endpoints-denue.md) | Endpoints INEGI, ads/analytics, TLS |
| 04 | [04-mapas-ubicacion.md](04-mapas-ubicacion.md) | Google Maps API key, AR, Street View, ubicación |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma auténtica INEGI |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/firma.txt` +
`evidence/maps-api-key.txt` + `decompiled/clases-propias.txt` (no versionados).

## 5 hallazgos principales

1. **(Bajo-Medio) Google Maps API key embebida.** `AIzaSy…` en el meta-data del manifiesto y en
   `strings.xml` (valor en `evidence/`). Es **normal** que una key de Maps viaje en el cliente,
   pero debe estar **restringida por package + SHA-1**; si no lo está, cualquiera puede extraerla
   y consumir cuota de Google a nombre del INEGI (la restricción es server-side en Google y **no
   es verificable estáticamente**) (doc 04).
2. **(Bajo) Publicidad + analítica en app de estadística federal.** `pagead2.googlesyndication.com`
   (ads), Google Analytics (GMS, deprecado) y permiso `com.google.android.gms.permission.AD_ID`
   → recolección de identificadores con fines de anuncios/medición en una app del INEGI.
   Observación de privacidad (doc 03).
3. **(Bajo) Build obsoleto.** targetSdk 33, `com.google.android.gms.analytics` **deprecado**,
   enlaces `goo.gl` (apagado por Google), `MAPS_RECEIVE` legacy con **mismatch** de nombre
   (`denuemv` declarado vs `denue` usado) → deuda técnica (doc 01, 02).
4. **Transporte HTTPS sin bypass (positivo).** Endpoints oficiales `https://www.inegi.org.mx/…`;
   **sin** `usesCleartextTraffic`, **sin** endpoints `http://` propios, **sin** `TrustManager`
   trust-all ni `getUnsafeOkHttpClient` → validación TLS por defecto (doc 03).
5. **Datos públicos, PII mínima (positivo).** El DENUE es open data; la app no maneja
   credenciales ni PII sensible. Lo único sensible es la **ubicación** del usuario (para "cerca
   de mí") y la cámara (para AR/Street View). Firma **auténtica del INEGI** (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK ni la Maps key (valor solo en `evidence/`, `.gitignore`). Observaciones
**estáticas**: no se ejecutó la app ni se llamó a ningún endpoint. El código propio está
**ofuscado con R8** (nombres minificados), lo que limita el trazado función por función de la UI;
la postura de seguridad se estableció con manifiesto, strings, librerías y clases, con evidencia.
La **restricción** de la Maps key vive en la consola de Google (server-side) y no es verificable
desde el binario.
