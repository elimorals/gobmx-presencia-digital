# 05 — Licencia, vehículos y trámites

ICVNL es el Instituto de Control Vehicular de Nuevo León: la app cubre licencia de conducir,
vehículos del contribuyente y trámites/pagos del estado.

## Vehículos

- `ISeguridad.ObtenerVeh` → POST `…/ObtenerVeh`, respuesta con `VehiculoEntity`.
- UI: página `Pages.Vehiculos`, literal "Placa:", `imgPlaca`. Expone **placa** y datos del
  vehículo del contribuyente autenticado.
- `CertVehRequest` / `ValidaCertLicRequest` → validación de certificados de vehículo/licencia.

## Licencia de conducir

- `ObtenerLicBio` → datos de licencia (sufijo "Bio" = incluye referencia biométrica, doc 06).
- `ImgLicencia` → **imagen** de la licencia (se guarda/convierte; ver `convertBase64StringToPdfAndStoreIt`,
  `getBase64FromBlobData` en el WebView, doc 07-08).
- `AgregaLicencia` + `PopUpAgregaLic` / `PopUpLicencia` / `PopUpLicOff` → alta y modo **offline**
  de la licencia (`ResLicenciaEntity`). El QR (doc 06) sirve para mostrar/validar la licencia
  digital.
- `BiometricoLicRequest` → petición que asocia biométrico a la licencia (doc 06).

## Trámites y pagos → WebView

- Páginas `Pages.Tramites` y `Pages.Detalle` cargan el **portal web** vía `ICustomWebView`
  usando `ISeguridad.UrlTramite` / `UrlPortal` (doc 07).
- **No se observó pasarela de pago nativa** en `ICV_APP.dll`: no hay literales de Banorte,
  OpenPay, Conekta, MercadoPago, multicobros, tarjeta/CVV, etc. (búsqueda ASCII+UTF-16 negativa).
  La conclusión con evidencia: **los pagos de trámites se realizan dentro del WebView del portal
  `icvnl.gob.mx`**, no en código nativo. Lo que ocurra en ese portal no es observable desde el
  APK (fuera de alcance estático; ver doc 07 y H2).

> No se afirma qué pasarela usa el portal ni cómo cobra: no hay evidencia en el binario. Se
> documenta el límite, no se rellena.
