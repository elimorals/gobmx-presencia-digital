# Red y SDKs

El backend propio vive en el snapshot Dart (muro, doc 02); lo observable desde el DEX son los
**SDKs de terceros** y sus endpoints.

## SDKs (strings del DEX)

| SDK | Señal | Rol |
|---|---|---|
| **Firebase Remote Config** | `firebaseremoteconfig.googleapis.com/v1/projects/` | **configuración remota del comportamiento** de la app |
| Firebase Cloud Messaging | `fcmregistrations.googleapis.com` | notificaciones push |
| Firebase Installations | `firebaseinstallations.googleapis.com` | identidad de instalación |
| **ExoPlayer / DASH** | `dashif.org`, `aomedia.org/emsg`, HLS de Apple | reproducción de video |
| ML translate (Google) | `dl.google.com/translate/offline/...` | traducción offline |

## Backend propio

**No** se hallaron endpoints `*.queretaro.gob.mx` ni rutas de API propias en el DEX → el host y
las rutas del gobierno de Querétaro están en el snapshot Dart (`libapp.so`), no observables
estáticamente. Se documenta el hecho (ausencia en el DEX), no se inventa el backend.

## Firebase Remote Config — nota

Que la app use **Remote Config** significa que parte de su comportamiento/flags se decide
server-side (Firebase). No es una vulnerabilidad, pero amplía lo que no es observable
estáticamente: lo que entregue Remote Config en runtime no está en el binario.

## Transporte

Sin `usesCleartextTraffic`; los `http://` presentes en el DEX son namespaces de librerías
(DASH-IF, Adobe XMP, docs de Android/ExoPlayer), **no** endpoints de la app. Pinning: no
verificable (capa de red en Dart). Marcado como límite.
