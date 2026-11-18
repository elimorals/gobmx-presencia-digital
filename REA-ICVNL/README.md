# Ingeniería inversa de ICVNL 2.4 — índice

Análisis **estático** del APK `com.icv.app` (base del bundle XAPK, split arm64-v8a) con REA
(JADX 0.7.1 para el envoltorio Android + extracción del AssemblyStore .NET y análisis managed con
las tools `rea__*`) + apktool + `apksigner`. La app **nunca se ejecutó**. Es la app oficial del
**Instituto de Control Vehicular de Nuevo León (ICVNL)** para licencia de conducir, vehículos y
trámites del estado — **#25 por instalaciones** del inventario gob.mx (500 K+).

> Fecha: 2026-11-17. Firma **auténtica** (`O=ICVNL`, v1+v2+v3, Source Stamp de Play) → artefacto
> íntegro del emisor (doc 10). Artefactos pesados en `evidence/` (no versionados).

> **Corrección de triage (con evidencia):** se catalogó como "nativa Kotlin/Java"; es **.NET
> MAUI/Xamarin**. La lógica vive en ensamblados C# (`ICV_APP.dll`), no en el DEX (doc 02).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila .NET MAUI, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, `usesCleartextTraffic`, `allowBackup`, `INSTALL_PACKAGES`, deep links |
| 02 | [02-arquitectura-dotnet-maui.md](02-arquitectura-dotnet-maui.md) | .NET MAUI, AssemblyStore `XABA`/LZ4, extracción de los 424 DLL, mapa `ICV_APP.*` |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | API `agencias.icvnl.gob.mx:1046`, 16 acciones, `UrlPortal`/`UrlTramite`, TLS sin pinning ni bypass |
| 04 | [04-autenticacion-usuario.md](04-autenticacion-usuario.md) | Login, token de atención, alta/activación/recuperación, CURP/PII |
| 05 | [05-licencia-vehiculos-tramites.md](05-licencia-vehiculos-tramites.md) | Vehículos, licencia (datos/imagen/offline), trámites y pagos por WebView |
| 06 | [06-biometria-faceliveness-qr.md](06-biometria-faceliveness-qr.md) | FaceLiveness en WebView (sin SDK nativo), QR por ZXing |
| 07 | [07-webview-puente-js.md](07-webview-puente-js.md) | `ICustomWebView`, puente `invokeCSharpAction`→C#, blob→archivo |
| 08 | [08-cripto-almacenamiento.md](08-cripto-almacenamiento.md) | Sin cripto de app, dependencia de HTTPS, `allowBackup`, licencia offline |
| 09 | [09-sdks-terceros.md](09-sdks-terceros.md) | ZXing, Camera.MAUI, Mopups, GoogleGson; sin pagos/biometría/analytics nativos |
| 10 | [10-firma-recursos.md](10-firma-recursos.md) | Firma auténtica, Source Stamp, estructura XAPK, AssemblyStore |
| 11 | [11-hallazgos-seguridad.md](11-hallazgos-seguridad.md) | Hallazgos priorizados (H1–H6) + lo fuera de alcance |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 de XAPK, APK base, split arm64, `libassembly-store.so`, `ICV_APP.dll` |
| `.gitignore` | Bloquea apk/so/dll/dex/decompiled/evidence |
| `evidence/` (no versionado) | `.so` del split, los 424 DLL extraídos, recursos apktool, `endpoints.txt` |

> Lo propietario del ICVNL (APK, `.so`, ensamblados `.dll`, recursos) **no se versiona**
> (`.gitignore`).

## 6 hallazgos principales

1. **`usesCleartextTraffic=true` + WebView con puente JS y URL del servidor** (H2, medio) —
   superficie de MITM→abuso del puente; la API nativa es HTTPS, el riesgo vive en el WebView.
2. **`allowBackup=true`** (H3) — respaldable la licencia offline (con imagen), usuario y token.
3. **Sin certificate pinning** (H4) — validación solo contra CAs del sistema; sin bypass.
4. **Pagos y liveness facial en el portal web**, no en el APK — pasarela y proveedor no
   identificables desde el binario (documentado como límite).
5. **Sin criptografía de aplicación** — 0 refs a `System.Security.Cryptography`; confidencialidad
   100% dependiente de HTTPS.
6. **`INSTALL_PACKAGES` declarado** (H5) — permiso privilegiado inefectivo en apps de tienda;
   señala intención de instalar APKs fuera de Play.

## Alcance y límites

Análisis **original** de una app pública del gobierno (NL) que se posee, con fines de
investigación de seguridad. **No redistribuye** el APK ni los ensamblados. Endpoints
documentados son rutas presentes en el binario, no secretos ni datos de usuarios. Observaciones
**estáticas** sobre metadata/IL/llamadas/literales (no C# textual: sin ILSpy en el entorno); la
app no se ejecutó ni se llamó a ningún endpoint. **Firma** auténtica del ICVNL (doc 10).
