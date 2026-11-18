# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo observado **estáticamente**; no se ejecutó la app ni se cargó el
portal.

## H1 (Medio) — Puente JS abre intents arbitrarios, sin restricción de navegación

`WebAppInterface.openUrl(String)` (`@JavascriptInterface`) hace
`startActivity(new Intent(ACTION_VIEW, Uri.parse(str)))` **sin validar scheme ni host**
(doc 03, código citado). El puente se adjunta como `Android` y `AndroidInterface` a **todas**
las páginas del WebView, y el `WebViewClient` **no** implementa `shouldOverrideUrlLoading`, así
que la navegación **no está acotada** al dominio oficial.

- **Impacto:** si el WebView renderiza contenido influido por un atacante (XSS/redirección
  abierta en el web app, o navegación a un dominio externo), su JavaScript puede disparar
  intents arbitrarios (abrir URLs de phishing, lanzar deep links/`tel:`/`sms:`/`intent:`) y
  tocar `deviceRegister`.
- **Mitigante:** no se omite la validación TLS (H-positivo) y la URL base es HTTPS, así que un
  MITM pasivo no basta; se requiere un fallo del web app o navegación fuera de dominio.
- **Fix:** validar scheme/host en `openUrl`; un solo nombre de interfaz; `shouldOverrideUrlLoading`
  que mantenga la navegación en `*.guanajuato.gob.mx`.

## H2 (Bajo-Medio) — Acceso a archivos del WebView habilitado sin necesidad

`setAllowFileAccess(true)` + `setAllowContentAccess(true)` (doc 02). En apps modernas el valor
seguro por defecto es `false`. Junto con el puente JS y una navegación no acotada, amplía el
impacto de un compromiso del contenido del WebView (lectura de `file://`/`content://`).
**Fix:** `setAllowFileAccess(false)` salvo que una función concreta lo exija.

## H3 (Bajo) — Doble interfaz JavaScript redundante

El mismo `WebAppInterface` se registra con dos nombres (`Android`, `AndroidInterface`); ambos
exponen los mismos métodos potentes (doc 03). Duplica la superficie sin razón aparente.
**Fix:** registrar una sola interfaz.

## Positivos (postura correcta)

- **P1 — TLS no se desactiva.** El `WebViewClient` no sobreescribe `onReceivedSslError` → ante
  error de certificado, el WebView cancela (comportamiento por defecto). No hay omisión de
  validación como en Infonavit o Licencia EDOMEX (doc 04).
- **P2 — Sin cleartext y dominio oficial.** `BASE_URL` HTTPS a `app-tarjeta-rosa.guanajuato.gob.mx`;
  sin `usesCleartextTraffic`; sin endpoints `http://` propios (doc 01, 04).
- **P3 — PII mínima en el binario.** La PII de beneficiarias (CURP, domicilio, datos del
  programa) se maneja del lado del servidor; el APK solo usa `CAMERA` + `READ_MEDIA_IMAGES`
  para captura de documentos. Permisos acotados, sin ubicación/contactos/SMS.

## Fuera de alcance (límites, sin inventar)

- **Manejo real de la PII:** vive en el portal web (`app-tarjeta-rosa.guanajuato.gob.mx`), del
  lado del servidor — no observable desde el APK. Qué se guarda, cómo y con qué cifrado en el
  servidor no se puede afirmar estáticamente.
- **Contenido dinámico del WebView:** la presencia/ausencia de XSS o redirección abierta en el
  web app no es verificable sin interactuar con el portal (fuera del alcance estático y de
  autorización).
- **Request exacto de `deviceRegister`/`NetworkHelper`:** cuerpo y cabeceras del registro de
  dispositivo no se trazaron a fondo; la pila de red propia es pequeña y el detalle completo
  requeriría análisis dinámico.
