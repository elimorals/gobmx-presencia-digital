# 06 — Biometría (FaceLiveness) y QR

## FaceLiveness: liveness facial en WebView, no SDK nativo

Hallazgo de arquitectura con evidencia fuerte:

- La página `ICV_APP.Pages.PopUpFaceLiveness` **no usa un SDK biométrico nativo**. Sus métodos
  (`CargarWebView` `d__13`, `CargarWebAsync` `d__23`, `disconectHwv` `d__1`, `Button_Clicked`,
  `RequestPermissions` `d__12`) cargan una **WebView** y se comunican por un **puente JS**
  (`jsInterface`, callback `resFaceliveness(`). El modelo `FacelivenessEntity` transporta el
  resultado.
- **No hay ensamblado ni `.so` de biometría** en el store: no aparecen Daon, FaceTec, Acuant,
  AWS Rekognition, Onfido ni Regula entre los 424 DLL ni en los `.so` del split. (Contrasta con
  AforeMóvil, que sí trae 9 `.so` de Daon.)
- Conclusión: el **liveness facial se ejecuta en una página web** servida por el portal del
  ICVNL y cargada en el WebView; la app aporta la cámara (permiso `CAMERA` +
  `HIGH_SAMPLING_RATE_SENSORS`) y recibe el veredicto por el puente JS. El proveedor de liveness
  y el tratamiento del rostro **viven del lado servidor/web**, fuera del APK (ver doc 07 y H2).
- Literal "Unable to find a face" / "Biometrico no válido" → mensajes de la UI nativa ante el
  resultado web.

> No se identifica el proveedor de liveness: no hay evidencia en el binario. Se documenta que es
> web, no se inventa el motor.

## QR: ZXing local

- `PopUpQR` + modelo `QrInfoEntity` + ensamblado **`zxing.dll`** (504 KB, ZXing.Net) → lectura y
  generación de **QR en el dispositivo** (sin servidor).
- Uso: mostrar la **licencia digital como QR** y/o leer QR para validar. Es procesamiento local
  (ZXing), análogo a la postura de Valida INE-QR, aunque aquí el QR es un canal secundario y la
  validación fuerte de licencia pasa por la API/portal.

## Cámara

- `CommunityToolkit.Maui.Camera.dll` + `Camera.MAUI.dll` → acceso a cámara para FaceLiveness y QR.
- El `fileProvider` de MAUI Essentials comparte las imágenes/PDF resultantes de forma acotada
  (doc 01).
