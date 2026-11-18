# Arquitectura — Xamarin.Forms clásico y XALZ

## Pila (con evidencia)
- `.so`: `libmonodroid.so`, `libmonosgen-2.0.so`, `libmono-btls-shared.so`, `libmono-native.so`,
  `libxamarin-app.so` → **Mono/Xamarin clásico** (no .NET MAUI).
- `assemblies/`: DLLs en claro por nombre pero **comprimidas con `XALZ`** (LZ4). Cabecera
  `XALZ` + `uint32` índice + `uint32` tamaño sin comprimir + bloque LZ4. Se descomprimieron a PE
  .NET reales (magic `MZ`, "Mono/.Net assembly") y se analizaron.
- App propia: `Cre.Comercializacion.Mobile.XForms.dll` (lógica compartida) +
  `...XForms.Android.dll` (head Android). Dependencias: Newtonsoft.Json,
  **Microsoft.WindowsAzure.Storage**, Plugin.{Connectivity,Media,Permissions,CurrentActivity}.

## MVVM (de la metadata managed)
- **Views (XAML):** `MainView`, `DetailStationView`, `ReportView`, `PermissionsView`,
  `Controls/RattingBar` (barra de calificación).
- **Services:** `RestService` (cliente HTTP genérico, método `Get` async →
  `RestService+<Get>d__2`), y un servicio por amenidad calificable: `ConvenienceStoreService`
  (tienda), `RestaurantService`, `SanitaryService` (baños), `WateringService` (agua),
  `OtherService`, más `NavigationService`.
- Cadena legible `ComplaintOrCommentAboutTheServiceStation` → la app central es la **queja/comentario
  sobre la estación**.

## "Función por función" en .NET (alcance)
El cuerpo C# textual no se reconstruye (no hay ILSpy en el entorno). La profundidad aquí es a
nivel **metadata/IL/strings/estructura**: tipos, servicios, literales (#US: endpoints, rutas), y
ausencia de patrones de bypass TLS. Es la misma profundidad usada en REA-ICVNL (.NET MAUI).

## Build obsoleto
DLLs fechadas 2020-10-22, versión 2.2.1, targetSdk 29, Xamarin.Forms clásico (**EOL** desde mayo
2024). Runtime y dependencias (incluida WindowsAzure.Storage, ya deprecada a favor de
`Azure.Storage.Blobs`) sin actualizar → deuda técnica y de seguridad por falta de mantenimiento.
