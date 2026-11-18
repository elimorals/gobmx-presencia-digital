# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app, **no se disparó la alerta** ni se
llamó a ningún endpoint. Contexto: **app de pánico ciudadano** → el dato (ubicación + identidad +
evidencia en una emergencia) es de alta sensibilidad y el canal debe ser íntegro.

## H1 (Medio, a confirmar) — `usesCleartextTraffic="true"` global sin NSC
El manifiesto permite HTTP en claro para todo el tráfico, sin `networkSecurityConfig` (doc 01,
04). En una app de pánico, un endpoint `http://` expondría la alerta y la ubicación a MITM. **No
se confirmó** la URL real del backend (tras el muro AOT+PairIP), así que se reporta como
**bandera a confirmar**, no como MITM demostrado. **Fix:** poner el flag en `false` y, si algún
host legacy lo requiere, acotarlo por dominio en un NSC — nunca el backend del pánico.

## H2 (Medio) — Ubicación hacia OneSignal (tercero)
`OneSignalSDK.DotNet.Android.Location.Binding` → el proveedor push de terceros recibe la
ubicación del ciudadano, además del backend propio (doc 04). **Fix:** deshabilitar el tracking de
ubicación de OneSignal si no es imprescindible; limitar el dato al backend de la central.

## H3 (Medio) — Permisos de emergencia extremos + `CALL_PRIVILEGED`
Ubicación, cámara, micrófono, contactos, **llamada privilegiada**, phone-state (doc 01).
Coherente con un botón de pánico, pero amplio; `CALL_PRIVILEGED` es inusual para una app de
tienda. El uso real de cada permiso no es verificable (muro). **Fix:** minimizar a lo que el
flujo de alerta exige; justificar `CALL_PRIVILEGED`.

## Observaciones de postura
- **(Positivo) AOT + PairIP:** alta resistencia a ingeniería inversa y tamper (doble capa).
- **(Procedencia) Firma re-firmada por APKPure** (doc 05) → no auténtica; conclusiones de
  arquitectura, no de integridad.

## Límites (el muro, sin inventar)
Por **AOT** (sin IL/PE: 0 PE, 0 metadata CLI en el `.so`) **+ PairIP** (DEX protegido), **no son
recuperables estáticamente**:
- el **backend/URL**, el **hub de SignalR** y las rutas de API de Cepo/Puebla;
- la **estructura y el cifrado del payload** del pánico (qué envía exactamente, y si va cifrado);
- el **pinning TLS** y si el canal real usa http o https;
- la lógica de auth/registro y el almacenamiento local.

Vía: análisis **dinámico** (Frida sobre el runtime .NET) tras **evadir PairIP** — ejecución,
probablemente root; fuera de alcance. Por el tipo de dato, se recomienda **divulgación
responsable** al H. Ayuntamiento de Puebla y al proveedor Cepo para verificar H1 del lado servidor.
