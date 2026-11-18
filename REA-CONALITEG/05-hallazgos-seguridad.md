# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app ni se llamó a ningún endpoint.
Contexto: app de **contenido educativo gratuito** → sin credenciales de pago ni PII crítica
aparente; el impacto se centra en **integridad de contenido y privacidad de navegación**.

## H1 (Medio) — Cleartext HTTP a backends de producción, y los libros se cargan por esa vía
El `network_security_config` habilita `cleartextTrafficPermitted="true"` para la API y el
middleware de producción (`api`/`middleware`/`libros`/`secundaria`/`milesdehistorias.conaliteg.gob.mx`)
y para varias IPs (doc 03). **Confirmado con código (doc 02):** `WebViewActivity.loadUrl(URL_BOOK)`
carga **cada libro** desde las URLs del catálogo — precisamente los dominios con cleartext
permitido. Es decir, **el contenido de los libros de texto puede viajar por HTTP sin TLS** y un
MITM puede **leer o alterar** lo que lee el estudiante (sustituir un libro, inyectar contenido en
el WebView con JS habilitado). **Fix:** quitar el cleartext; forzar HTTPS en todos los dominios de
contenido propios.

## H2 (Medio) — Confianza en CAs instaladas por el usuario
`<certificates src="user" />` en los `trust-anchors` → MITM **trivial** instalando una CA
(doc 03). **Fix:** eliminar `src="user"`; confiar solo en `system` (y, si se quiere robustez,
añadir pinning).

## H3 (Bajo) — Lector de libros en WebView con JS y puente `"Android"`
`WebViewActivity` lee cada libro en un WebView con `setJavaScriptEnabled(true)` y
`addJavascriptInterface(this, "Android")` (doc 02). La Activity **no declara métodos
`@JavascriptInterface` visibles**, así que en API 24+ el puente **parece inerte** (nada
llamable desde el JS del libro). Riesgo bajo, pero combinado con el cleartext (H1) el contenido
del libro corre con JS habilitado sobre un transporte potencialmente HTTP. **Fix:** quitar el
`addJavascriptInterface` si no se usa; forzar HTTPS. *(Matiz: no se volcó la tabla de anotaciones
método a método; si hubiera un método anotado oculto, subiría a Medio.)*

## H4 (Bajo) — Residuos de desarrollo en producción
El NSC de producción incluye IP privada (`192.168.101.6`), emulador (`10.0.2.2`), loopback y el
subdominio `pruebasistemas.conaliteg.gob.mx` (doc 03): fuga menor de infraestructura interna y
señal de build no saneado. **Fix:** limpiar el NSC para el build de producción.

## Positivos

- **Permisos acotados** (lector: red + notificaciones + almacenamiento; sin cámara/ubicación/
  contactos) (doc 01).
- **Sin PII crítica aparente:** biblioteca gratuita; no se observaron flujos de pago ni de
  credenciales sensibles.
- **Código decompilable** (nativo, sin ofuscar) — transparente para auditoría.

## Límites (sin inventar)

- Se decompiló `WebViewActivity` (lector) y `DataModel`; el resto de la UI (adapters/views
  ofuscados) no se recorrió método a método porque no aporta más allá de lo ya hallado.
- Las referencias a `HostnameVerifier`/`checkServerTrusted` en el DEX son símbolos de librería;
  **no se confirmó** un trust-all en código (distinto de la debilidad del NSC).
- Las rutas exactas de la API bajo `api.conaliteg.gob.mx` no se enumeraron (no se tocó el
  endpoint, por alcance).
- Firma re-firmada por APKPure → conclusiones de arquitectura, no de integridad.
