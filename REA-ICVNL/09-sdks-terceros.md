# 09 — SDKs y dependencias de terceros

De los **424 ensamblados** del AssemblyStore, descontando el framework (`System.*`,
`Microsoft.Maui.*`, `Microsoft.Extensions.*`, `Mono.*`, `Xamarin.AndroidX.*`,
`Java.Interop`, `netstandard`, `mscorlib`), las dependencias de terceros/propias relevantes son:

| Ensamblado | Tamaño | Función |
|---|---|---|
| **`ICV_APP.dll`** | 703 KB | La app (código propio ICVNL) |
| `zxing.dll` | 504 KB | **ZXing.Net** — QR (lectura/generación local, doc 06) |
| `CommunityToolkit.Maui.dll` + `.Core` | 549+159 KB | Toolkit MAUI (UI, helpers) |
| `CommunityToolkit.Maui.Camera.dll` + `Camera.MAUI.dll` | 95+69 KB | **Cámara** (FaceLiveness/QR, doc 06) |
| `CommunityToolkit.Mvvm.dll` | 147 KB | MVVM (ViewModels) |
| `Mopups.dll` | 181 KB | **Popups** (los `PopUp*`, doc 02) |
| `GoogleGson.dll` | 174 KB | Binding de Gson (serialización, lado Android) |
| `Plugin.StoreReview.dll` | 29 KB | Pedir reseña en tienda (`PopUpCalifica`) |
| `AsyncAwaitBestPractices.dll` | 18 KB | Utilidades async |
| `Jsr305Binding.dll`, `WindowsBase.dll` | — | Soporte |

## Lo que NO está (observaciones negativas con evidencia)

- **Sin SDK biométrico/liveness nativo** (no Daon, FaceTec, Acuant, AWS, Onfido, Regula) → el
  liveness es web (doc 06).
- **Sin SDK de pagos nativo** (no Banorte, OpenPay, Conekta, MercadoPago, Stripe) → pagos por el
  portal web (doc 05).
- **Sin Firebase / Crashlytics / Sentry / AppCenter / analytics** evidentes entre los
  ensamblados → telemetría de terceros mínima o ausente en el plano nativo (postura de
  privacidad favorable; lo que haga el portal web no es observable).
- Serialización con `System.Text.Json` (nativo .NET) en `ISeguridad` + `GoogleGson` del lado
  binding.

> Observaciones por inventario de ensamblados y literales. No se descarta que el **portal web**
> cargado en el WebView incluya analítica/terceros propios; eso queda fuera del APK.
