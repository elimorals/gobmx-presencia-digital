# Flujo de alerta / pánico y ubicación

> **Importante:** el detalle del flujo vive en `Cepo.CiudadanoMAUI` (AOT) tras PairIP (doc 02),
> así que lo de abajo es **arquitectura inferida de los ensamblados y permisos**, no el payload
> exacto. Lo no confirmable se marca como tal. **No se disparó la alerta.**

## Canal de tiempo real: SignalR

El ensamblado incluye `Microsoft.AspNetCore.SignalR.Client`,
`Microsoft.AspNetCore.Http.Connections.Client` y `SignalR.Client.Core`. SignalR establece un
**canal persistente** (handshake + `negotiate` + hub, sobre WebSocket/SSE/long-polling) — el
patrón estándar para que una app de pánico **transmita ubicación y estado en tiempo real a una
central** (C5/9-1-1 municipal) y reciba confirmaciones. La URL del hub **no es recuperable**
(muro), pero la presencia del cliente SignalR confirma la arquitectura de streaming.

## Qué puede enviar la alerta (por permisos)

Los permisos (doc 01) acotan la superficie del botón de pánico:
- **Ubicación** (FINE/COARSE + FOREGROUND_SERVICE) → posición, probablemente en vivo durante la
  alerta vía SignalR.
- **Audio** (RECORD_AUDIO) y **foto** (CAMERA) → evidencia multimedia.
- **Contactos** (READ_CONTACTS) → contactos de emergencia a notificar.
- **Llamada** (CALL_PHONE/CALL_PRIVILEGED) → marcado directo a emergencias.
- **Identidad de dispositivo** (READ_PHONE_STATE).

**No confirmado (muro):** si la alerta adjunta todo lo anterior por defecto, si la ubicación se
transmite de forma continua, y la **estructura/cifrado** del mensaje. No se infiere el contenido
del payload — solo el conjunto de capacidades que los permisos habilitan.

## Ubicación a terceros

`OneSignalSDK.DotNet.Android.Location.Binding` (doc 04) indica que **OneSignal** (push, tercero)
también está cableado con ubicación. Para una app de pánico, compartir ubicación con el proveedor
push es una observación de privacidad relevante (doc 06).
