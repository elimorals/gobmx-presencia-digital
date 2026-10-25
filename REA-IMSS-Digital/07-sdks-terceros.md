# SDKs de terceros

Como las demás apps grandes de gob.mx, mantiene **doble ecosistema Google + Huawei**. El
inventario sale del `assemblies.blob` (plugins Xamarin), del DEX (bindings) y de `assets/`.

## Google / Firebase

| SDK | Evidencia | Rol |
|---|---|---|
| Firebase Analytics | `Plugin.FirebaseAnalytics.dll` | Métricas de uso |
| Firebase Cloud Messaging | `FirebaseInstanceIdReceiver` (manifest) | Push |
| Install Referrer | `BIND_GET_INSTALL_REFERRER_SERVICE` | Atribución |
| Google Maps | `Maps.dll`, deep links `maps` | Ubicación de clínicas/centros |

## Huawei

| SDK | Evidencia | Rol |
|---|---|---|
| Huawei AGConnect | `assets/agconnect-services.json` | Backend-as-a-service / push HMS |

Confirma la distribución también en **Huawei AppGallery** (la app corre en dispositivos sin
Google Play Services).

## Verificación de identidad (KYC) — ver doc 04

- **Mati / MetaMap** — orquestación KYC (dominante, 3,410 refs).
- **FaceTec** — liveness facial, vía integrador `firmaautografa.com`.
- **Acuant** (AssureID, passlive, MedicScan) — documentos de identidad, región EU.

## Plugins Xamarin.Forms

| Plugin | Rol |
|---|---|
| `SlideOverKit.dll` | Menús laterales / overlays |
| `Svg.Platform.dll` | Render SVG (iconografía) |
| `Plugins.Fingerprint.dll` | Biometría local |
| `SQLitePCL.raw` | SQLite local |
| `metadata-extractor-dotnet` | EXIF de imágenes (documentos KYC) |
| `AndHUD`, `Acr.UserDialogs` | HUD de carga / diálogos |
| `LatestVersionPlugin` | Aviso de actualización en tienda |

## Fuentes (assets)

Gran cantidad de tipografías decorativas (`Boogaloo`, `Gaegu`, `Handlee`, `Kaushan Script`,
`Pinyon Script`, `hauora`, `Montserrat`, `NotoSans`, `Roboto`) + fuentes de iconos
(`ExpanderFontIcons`, `ImageEditor_Icons`, `Font_Stepprogress_icon`). El set de "image editor"
y "avatar" sugiere un módulo de edición/recorte de la foto de documento o avatar de perfil.

## Consideraciones de privacidad

Flujo de datos a terceros: Google (Analytics/FCM), Huawei (AGConnect), y **tres proveedores de
KYC** (Mati, FaceTec/firmaautografa, Acuant-EU) que reciben **rostro y documentos de
identidad** de los derechohabientes. Para una institución pública de salud, el procesamiento
biométrico por proveedores privados —algunos fuera de México— es el punto de privacidad más
sensible, a contrastar con el aviso `API-App-IMSS-Digital.pdf` (referenciado en el binario).
