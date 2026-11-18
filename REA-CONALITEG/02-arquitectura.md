# Arquitectura — lector nativo y catálogo federado

## Pila

- **Nativa Android (Kotlin/Java).** 1 `classes.dex`; paquete propio
  `gob.mx.conaliteg.bibliotecadigital` con subpaquetes `adapters/`, `model/`, `views/`
  (patrón MVC). Sin Flutter (`flutter_assets`), sin Unity (`il2cpp`), sin .NET (`assemblies`),
  sin bundle web (`assets/www`) → es una app Android convencional, **decompilable**.
- Firebase Analytics (`app-measurement.com`) para telemetría de uso.

## Función

Lector/agregador de **biblioteca digital** de la CONALITEG: libros de texto gratuitos y
contenidos educativos. El catálogo **federa** fuentes propias y de terceros (doc 00):

- **Propias CONALITEG:** `api`, `middleware`, `libros`, `secundaria`, `milesdehistorias`
  (todas `*.conaliteg.gob.mx`).
- **Editoriales / universidades:** FCE, UPN, UNAM, Grupo SM, Trillas, Cervantes Virtual,
  Academia.org, Fonoteca Nacional (dominios listados en el NSC, doc 03).

## Clases propias (12) y el lector de libros

`search_android_classes` lista 12 clases propias: `MainActivity`, `BookActivity`,
**`WebViewActivity`**, `DigitalLibraryApplication`, `model.{Book,DataModel,Library,Subcategory}`,
`adapters.LinearLayoutFixManager`, `views.{KeyboardEditText,ViewPagerFix}`. El código de soporte
está **ofuscado/minificado** (R8: tipos `S2.a`, `F1.a0`, campos `f5039R`…), pero las Activities y
el modelo conservan nombres legibles.

- **`model.DataModel`** es un POJO (listas `catalog`/`collaborator`/`historic`/`material` de
  `Library` + `revision`). No es el cliente HTTP; el catálogo se descarga en `MainActivity` y se
  vuelca en este modelo.
- **`WebViewActivity.onCreate`** (decompilado a smali por la ofuscación, extractos en
  `decompiled/webviewactivity-smali.txt`) **lee cada libro en un WebView**:

  ```
  settings.setJavaScriptEnabled(true)        // JS habilitado
  settings.setBuiltInZoomControls(true)
  webView.addJavascriptInterface(this, "Android")   // expone la Activity como "Android"
  webView.setWebViewClient(<cliente>)        // sin onReceivedSslError → TLS por defecto (valida)
  webView.loadUrl(URL_BOOK)                  // carga el libro desde la URL del catálogo
  ```

### Observaciones del lector

1. **Puente JS `"Android"` aparentemente inerte.** Se expone la propia `WebViewActivity` a JS como
   `Android`, pero la clase **no declara métodos `@JavascriptInterface` visibles** (solo
   `onCreate`/`onBackPressed`/`<init>`). En API 24+ (minSdk 24) solo los métodos anotados son
   invocables desde JS → **nada parece ser llamable** desde el contenido del libro. Es un patrón
   heredado sin métodos expuestos; riesgo bajo. *(Matiz: no se volcó la tabla de anotaciones
   método a método; si hubiera un método anotado oculto, el riesgo subiría — se reporta como
   "aparentemente inerte", no como hecho absoluto.)*
2. **El libro se carga con `loadUrl(URL_BOOK)` desde la URL del catálogo** — que, por el NSC
   (doc 03), puede ser **HTTP en claro** (dominios `libros`/`secundaria`/`milesdehistorias.conaliteg.gob.mx`
   y editoriales). Esto conecta la debilidad del NSC con el flujo real: **el contenido de los
   libros puede viajar sin TLS y ser alterado por un MITM** (doc 05, H1).
3. **TLS no se desactiva en el WebView:** no se observó `onReceivedSslError` override → ante error
   de certificado, el WebView cancela (comportamiento por defecto). La debilidad es el cleartext
   permitido, no una omisión de validación.
