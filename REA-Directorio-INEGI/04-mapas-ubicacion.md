# Mapas, AR, Street View y ubicación

## Google Maps API key embebida

En el meta-data del manifiesto (`com.google.android.geo.API_KEY`) y en `strings.xml` hay una
**Google Maps API key** (`AIzaSy…`; valor real solo en `evidence/maps-api-key.txt`,
no versionado).

- **Es normal** que una key de Google Maps viaje en el cliente — Google la diseña así. El
  control no es ocultarla sino **restringirla**: en la consola de Google Cloud debe estar atada
  al **package** (`mx.org.inegi.denuemv`) + **huella SHA-1** del certificado de firma, y limitada
  a las APIs de Maps que usa.
- **Riesgo (Bajo-Medio) si NO está restringida:** cualquiera puede extraer la key del APK (como
  se hizo aquí) y consumir **cuota facturable de Google** a nombre del INEGI, o usar las APIs
  habilitadas. **No es verificable estáticamente**: la restricción vive en el lado de Google. Se
  reporta como **buena práctica a confirmar**, no como vulnerabilidad probada.

## Realidad aumentada (cámara)

`ar.ArActivity` + `ar.ficha.ArFichaFragment` → la app tiene un modo de **realidad aumentada**
que usa la **cámara** para ubicar establecimientos en el entorno. Justifica el permiso `CAMERA`.
No se observó subida de imágenes de cámara a ningún servidor (la cámara se usa para overlay AR).

## Street View y galería

`ficha.StreetViewActivity` (Google Street View del establecimiento) y `ficha.GaleriaActivity`
(fotos). Consumen servicios de Google Maps con la misma key.

## Ubicación

`ACCESS_FINE/COARSE_LOCATION` para "establecimientos cerca de mí" y centrar el mapa. Es el
**único dato sensible del usuario** que maneja la app (los establecimientos son datos públicos).
No hay ubicación en segundo plano. El uso exacto (envío de coordenadas al backend para la
consulta "cerca de mí") es plausible pero el detalle vive en los helpers ofuscados; no se
confirmó que las coordenadas viajen a un tercero más allá del INEGI/Google. Marcado como
pregunta menor.
