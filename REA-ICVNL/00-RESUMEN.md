# 00 — Resumen ejecutivo: ICVNL 2.4

Análisis **estático** del APK `com.icv.app` 2.4 (vc 45) con REA (JADX 0.7.1 para el envoltorio
Android + extracción y análisis de los **ensamblados .NET** con las tools managed de REA) +
apktool + `apksigner`. La app **nunca se ejecutó**. App oficial del **Instituto de Control
Vehicular de Nuevo León (ICVNL)**, #25 por instalaciones del inventario gob.mx (500 K+).
Fecha: 2026-11-17.

## Identidad y pila

- **.NET MAUI/Xamarin** (no Kotlin/Java — corrección del triage con evidencia: `MonoRuntimeProvider`,
  `microsoft.maui.*`, clases `crc64…`, `.so` de Mono). Código de negocio en C# dentro de
  **`ICV_APP.dll`**, extraído del AssemblyStore `libassembly-store.so` (425 ensamblados LZ4).
- **Firma auténtica del ICVNL** (`O=ICVNL`, v1+v2+v3, Source Stamp de Play) → binario íntegro del
  emisor, más fuerte que CFE/IMSS.

## Arquitectura en dos planos

1. **Nativo (.NET MAUI):** login usuario/contraseña, token de atención, alta/activación/
   recuperación de usuario (CURP, correo), consulta de **vehículos** y **licencia** —todo contra
   una API HTTPS única: `https://agencias.icvnl.gob.mx:1046/appmovilapiseg/SeguridadContribuyenteController/`
   (16 acciones, JSON, `System.Text.Json`).
2. **WebView (`ICustomWebView` + puente JS):** **trámites, pagos y liveness facial** se cargan del
   portal web vía `UrlPortal`/`UrlTramite` (URLs que devuelve el servidor), con puente
   `invokeCSharpAction`→C#. QR por **ZXing local**.

## Postura de seguridad

Razonable. **Sin bypass de validación TLS**, **sin cripto rota** (de hecho sin cripto de app:
depende del HTTPS), **sin SDK de pagos/biometría/analytics nativos**. Los puntos abiertos están
en el plano WebView y en banderas del manifiesto.

## 6 hallazgos principales

1. **`usesCleartextTraffic=true` + WebView con puente JS y URL provista por el servidor** (H2,
   medio): la combinación habilita, en teoría, inyección de JS vía MITM que abuse del puente
   (cámara, blob→archivo, liveness). La API nativa es HTTPS; el riesgo vive en lo que cargue el
   WebView, que no es observable estáticamente.
2. **`allowBackup=true`** (H3): respalda datos de la app, incluida la **licencia offline con
   imagen** y el usuario/token (contrasta con AforeMóvil).
3. **Sin certificate pinning** (H4): validación solo contra CAs del sistema. No hay desactivación
   de validación (a diferencia de Infonavit), pero sí falta pinning para PII vehicular/identidad.
4. **Pagos y liveness facial ocurren en el portal web**, no en el APK: no se identificó pasarela
   ni proveedor de liveness (documentado como límite, no inventado).
5. **Sin criptografía de aplicación**: 0 referencias a `System.Security.Cryptography` en
   `ICV_APP.dll`; la confidencialidad depende por completo de HTTPS.
6. **Permiso `INSTALL_PACKAGES` declarado** (H5): `signature|privileged` no concedible a apps de
   tienda → inefectivo; señala intención de instalar APKs fuera de Play.

## Alcance y límites

Análisis original de una app pública que se posee, con fines de investigación de seguridad.
**No** redistribuye el APK ni los ensamblados (`.gitignore`). Observaciones estáticas sobre
metadata/IL/llamadas/literales (no C# textual: sin ILSpy en el entorno); la app no se ejecutó y
no se llamó a ningún endpoint. El portal web cargado en el WebView queda fuera del APK.
