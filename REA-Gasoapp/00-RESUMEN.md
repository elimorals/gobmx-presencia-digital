# Resumen — Gasoapp 2.2.1 (`cre.comercializacion.mobile`)

## Identidad
- **App:** Gasoapp — Comisión Reguladora de Energía (CRE). Localiza estaciones de servicio y
  permite **calificar/quejarse** sobre precios y amenidades (tienda, restaurante, baños, agua),
  con **foto de evidencia**.
- **Package:** `cre.comercializacion.mobile` · **versión** 2.2.1 (versionCode 327916). targetSdk 29.
- **#31** del inventario por instalaciones (≈500 K).
- **Firma:** AUTÉNTICA (`O=Comisión Reguladora de Energía`, firmada por un dev de la CRE).

## Pila
**Xamarin.Forms clásico (.NET/Mono)** — `libmonodroid.so`/`libmonosgen-2.0.so`, DLLs en
`assemblies/` comprimidas **XALZ** (LZ4). Se descomprimieron (header `XALZ`+size+bloque LZ4) a PE
.NET reales y se analizaron con las tools managed de REA. Build de **2020** (Xamarin clásico, EOL).

## Estructura (MVVM, de la metadata)
- **Views:** MainView, DetailStationView, ReportView, PermissionsView, RattingBar.
- **Services:** `RestService` (cliente HTTP, `Get` async), ConvenienceStoreService,
  RestaurantService, SanitaryService, WateringService, OtherService, NavigationService —
  califican cada amenidad de la estación.
- DeviceInfo (deviceId), dependencia `Microsoft.WindowsAzure.Storage`.

## Config embebida (limpia, de los #US strings)
| Clave | Valor |
|---|---|
| BaseUrl prod | `https://api-gasoapp.cre.gob.mx/` |
| BaseUrl **QA** | `https://api-gasoapp-qa.cre.gob.mx/` (dejado en prod) |
| BaseUrl Azure crudo | `https://api-estacionservicio-327800.gasoapp.p.azurewebsites.net/` |
| GetBlobStorage | `UriContainerEvidencia?deviceId={0}&permisoId={1}` |

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS en los 3 endpoints |
| TLS | ✅ Sin bypass (validación estándar del sistema) |
| Secretos | ✅ Sin AccountKey/SAS/API key hardcodeados |
| Config | ⚠️ QA + dominio Azure crudo en el binario de prod |
| Evidencia | ⚠️ Blob por deviceId+permisoId (auth no verificable estáticamente) |
| Mantenimiento | ⚠️ Xamarin clásico EOL, DLLs de 2020, targetSdk 29 |
| Firma | ✅ Auténtica de la CRE |

## Lo que NO se puede (límites)
El cuerpo C# textual no se reconstruye (sin ILSpy); el análisis es metadata/strings/estructura.
La autorización real del Blob (SAS en runtime) y las rutas completas de la API bajo cada BaseUrl
no son observables estáticamente. Marcado como límite, sin inventar.
