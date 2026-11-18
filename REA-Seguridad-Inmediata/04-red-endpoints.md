# Red y endpoints

## Lo recuperable (endpoints de SDKs en claro)

De `strings` sobre los `.so` y el DEX, los únicos endpoints en claro son de **SDKs de terceros**:

- **OneSignal:** `https://api.onesignal.com/` + el SDK `OneSignalSDK.DotNet` con
  **`Android.Location.Binding`** → push y, con el binding de ubicación, OneSignal recibe la
  ubicación del ciudadano. HTTPS.
- Placeholders de framework (`https://www.example.com`, namespaces XMP de Adobe) — no son de la app.

## Lo NO recuperable (el backend propio)

El **backend de Cepo/Puebla, el hub de SignalR y las rutas de API** **no** aparecen en claro: el
ensamblado `Cepo.CiudadanoMAUI` está en AOT nativo (0 PE, 0 metadata CLI — doc 02) y el DEX bajo
PairIP. Es decir, **el endpoint al que viaja la alerta de pánico es desconocido estáticamente**.

## Transporte (postura)

- **`usesCleartextTraffic="true"`** global y **sin `networkSecurityConfig`** (doc 01). Esto
  **permite** HTTP en claro a cualquier host. Dos lecturas honestas:
  - Si el backend del pánico resuelve a `http://`, la alerta/ubicación viajaría **sin TLS**
    (MITM) — el peor caso para una app de emergencia.
  - Si en la práctica usa `https://` (lo habitual con SignalR + OneSignal), el flag estaría
    activado sin necesidad — mala higiene, no explotación directa.
  - **No se puede distinguir cuál** sin ver la URL (muro). Se reporta el flag como hallazgo
    (doc 06, Medio "a confirmar"), no como MITM confirmado.
- **Pinning TLS:** no verificable (la pila de red es .NET en AOT). Marcado como límite.
