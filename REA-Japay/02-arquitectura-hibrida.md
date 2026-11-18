# Arquitectura — Cordova y respaldos en producción

## Pila

- **Cordova** confirmado: `assets/www/` con `cordova.js`, `cordova_plugins.js` y plugins
  (`cordova-plugin-camera`, `-file`, `-file-transfer`, `-device`). No Flutter/Unity/.NET/Capacitor.
- Lógica propia: `js/japayMovil.js` (108 KB) + `js/functions.js` (82 KB) + `js/framework.js`,
  sobre **jQuery**. HTML por pantalla (`index.html`, `configuracion.html`, `centros.html`,
  `3dsecure.html`, `frmprocesapago.html`).

## Higiene de build — artefactos de desarrollo shipeados (hallazgo, Bajo)

El bundle de producción arrastra copias de respaldo y archivos de desarrollo:

- **Respaldos de la lógica:** `japayMovilRESPALDP.js`, `japayMovil2.js`, `japayMovil3.js`,
  `functionsRESPALDO.js`, `functionsrespaldo2.js`, `function2.js` — múltiples versiones del mismo
  código, de las que la app carga solo una. Aumentan la superficie y revelan historia/edición.
- **Archivos `.php` en un bundle de cliente:** `autoload.php`, `videoa.php`,
  `politica-privacidad.php` — plantillas/residuos del lado servidor empaquetados por error.
- **`js/js-rijndael/`** completa (lib + docker + tests) empaquetada pero **sin uso** (doc 03).

Ninguno es una vulnerabilidad por sí mismo, pero en conjunto son **mala higiene de empaquetado**:
código muerto, posible fuga de rutas/lógica interna y mayor superficie a revisar. **Fix:** un paso
de build que empaquete solo los assets activos y minificados.

## Cómo carga

El WebView de Cordova abre `assets/www/index.html` (local); las llamadas van por AJAX (jQuery) al
backend `urlserv = https://japayaplicacion.yucatan.gob.mx/` (doc 04). El pago sale del WebView
local a un **InAppBrowser** apuntando a un form remoto (doc 03).
