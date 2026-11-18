# Ingeniería inversa de Seguridad Inmediata 3.7.5 — índice

Análisis **estático** del APK `com.alertacontigo.mx` ("Seguridad Inmediata" / "Alerta Contigo")
con REA. La app **nunca se ejecutó**. Es la app de **botón de pánico / alerta ciudadana** del
**H. Ayuntamiento de Puebla**, **#50 por instalaciones** del inventario gob.mx (≈163 K).

> **.NET MAUI compilada a AOT + protegida con PairIP.** El código de negocio vive en el
> ensamblado propio **`Cepo.CiudadanoMAUI`** (proveedor **Cepo**), compilado a **código nativo
> AOT** (`libaot-*.dll.so`) — sin metadata CLI/PE recuperable — y el DEX está **envuelto en
> PairIP** (`com.pairip.application` + `CHECK_LICENSE`). La combinación hace que **el backend,
> el hub de SignalR y la estructura del payload del pánico NO sean recuperables estáticamente**.
> Lo que sí se confirma: arquitectura de **tiempo real (SignalR)** + push **OneSignal** (con
> binding de ubicación), permisos de emergencia extremos y **`usesCleartextTraffic=true`**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, .NET MAUI/PairIP, el muro, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos de emergencia, cleartext, PairIP |
| 02 | [02-arquitectura-net.md](02-arquitectura-net.md) | .NET MAUI AOT + PairIP, ensamblados, por qué es muro |
| 03 | [03-alerta-panico-ubicacion.md](03-alerta-panico-ubicacion.md) | Flujo de alerta: SignalR, ubicación, evidencia |
| 04 | [04-red-endpoints.md](04-red-endpoints.md) | OneSignal, cleartext, lo no recuperable |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure, procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados y límites |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `evidence/dotnet-assemblies.txt` | 173 ensamblados .NET (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 6 hallazgos principales

1. **(Medio, a confirmar) `usesCleartextTraffic="true"` global en una app de pánico.** El
   manifiesto habilita HTTP en claro para todo el tráfico y **no hay `networkSecurityConfig`**.
   Si el backend del pánico usa `http://`, la alerta y la ubicación viajarían **sin TLS**
   (MITM). **No se pudo confirmar** la URL real (está tras el muro AOT+PairIP) → se reporta como
   bandera, no como MITM confirmado (doc 01, 04).
2. **(Medio) Ubicación hacia un proveedor push de terceros (OneSignal).** Incluye
   `OneSignalSDK.DotNet.Android.Location.Binding` → el SDK de OneSignal recibe la ubicación del
   ciudadano, además del backend propio (doc 04).
3. **(Medio) Permisos de emergencia extremos.** `ACCESS_FINE/COARSE_LOCATION`, `CAMERA`,
   `RECORD_AUDIO`, `READ_CONTACTS`, **`CALL_PRIVILEGED` + `CALL_PHONE`**, `READ_PHONE_STATE`,
   `FOREGROUND_SERVICE`, `RECEIVE_BOOT_COMPLETED` → el botón de pánico puede enviar ubicación,
   capturar audio/foto, leer contactos y hacer llamadas privilegiadas (doc 01).
4. **(Arquitectura) Tiempo real por SignalR.** El ensamblado incluye
   `Microsoft.AspNetCore.SignalR.Client` → canal persistente (negociación + hub) para
   **streaming de ubicación/estado a una central**, típico de C5/9-1-1 (doc 03).
5. **(Protección) .NET MAUI AOT + PairIP = alta resistencia a RE.** Doble capa: AOT (sin IL/PE)
   y PairIP (anti-tamper + licencia). Es el límite del análisis estático (doc 02).
6. **(Procedencia) Firma re-firmada por APKPure**, no del Ayuntamiento de Puebla ni de Cepo →
   arquitectura sí, integridad no (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app, **no se disparó la
alerta** ni se llamó a ningún endpoint. El backend, el hub de SignalR, la estructura del payload
del pánico, el cifrado de la alerta y el pinning TLS **no son recuperables estáticamente** por la
doble capa AOT+PairIP — documentado como muro, **sin inventar**. Por el tipo de dato (pánico
ciudadano), el doc 06 sugiere **divulgación responsable** al Ayuntamiento de Puebla y a Cepo.
