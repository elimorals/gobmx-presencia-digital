# Ingeniería inversa de CFE Proveedores 1.0.7 — índice

Análisis **estático** del APK `mx.com.cfe.proveedores` con REA (extracción del AssemblyStore
Xamarin + tools managed). La app **nunca se ejecutó**. Es la app del **portal de proveedores de
la Comisión Federal de Electricidad (CFE)** — concursos/licitaciones, consorcios y datos del
proveedor. **#49 por instalaciones** del inventario gob.mx (≈100 K).

> **Xamarin.Forms clásico (.NET/Mono).** Los ensamblados viajan en `assemblies/assemblies.blob`
> (formato **XABA** store v1, DLLs comprimidas **XALZ/LZ4**); se extrajeron y se analizó el
> ensamblado propio **`DemoCFE.dll`** con las tools managed de REA. El nombre interno del
> proyecto es **"DemoCFE"** (un codebase llamado *Demo* en producción). Postura TLS **correcta**
> (sin trust-all, sin bypass — al revés que ProcurApp/Infonavit), pero el NSC deja **cleartext
> global + 2 IPs internas de la CFE** embebidas.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Xamarin/.NET, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, `usesCleartextTraffic`, NSC, backup |
| 02 | [02-arquitectura-net.md](02-arquitectura-net.md) | Xamarin.Forms, XABA/XALZ, `DemoCFE.dll` |
| 03 | [03-auth-proveedores.md](03-auth-proveedores.md) | Login/recuperación, datos del proveedor |
| 04 | [04-red-endpoints.md](04-red-endpoints.md) | `enlacesec.cfe.mx`, portal, IPs internas |
| 05 | [05-cripto-tls.md](05-cripto-tls.md) | Validación TLS estándar, sin pinning/bypass |
| 06 | [06-firma-recursos.md](06-firma-recursos.md) | Re-firmada por APKPure |
| 07 | [07-hallazgos-seguridad.md](07-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml`, `hashes.sha256` (versionados); `evidence/` (firma, NSC,
`assemblies.manifest`, lista de DLL) y `decompiled/` (endpoints de `DemoCFE.dll`) **no
versionados**. Los DLL completos no se redistribuyen.

## 5 hallazgos principales

1. **(Bajo-Medio) Cleartext global + IPs internas de la CFE en el NSC de producción.** El
   manifiesto declara `android:usesCleartextTraffic="true"`, y el `network_security_config`
   permite cleartext explícito a **`10.4.70.22`** y **`10.220.1.71`** (IPs privadas RFC1918 de la
   red interna de la CFE). Fuga de topología interna + HTTP en claro no prevenido; la API pública
   (`enlacesec.cfe.mx`) es HTTPS por su propia URL, pero nada lo fuerza (doc 01, 04).
2. **(Bajo) Codebase "Demo" en producción.** El ensamblado propio es **`DemoCFE`**/`DemoCFE.Android`
   (doc 02) — la app de proveedores de la CFE se compila desde un proyecto llamado "Demo". Señal
   de madurez/higiene de release, no una vulnerabilidad en sí.
3. **(Bajo) `allowBackup="true"`.** Permite respaldar los datos de la app por ADB/backup (doc 01).
4. **Postura TLS correcta (positivo).** `DemoCFE.dll` **no** contiene
   `ServerCertificateCustomValidationCallback`/`DangerousAccept*`/trust-all en ninguna parte →
   validación de certificado **estándar** del sistema. Sin pinning, pero sin el antipatrón de
   Infonavit/ProcurApp (doc 05).
5. **Backend HTTPS oficial (positivo).** La API es `https://enlacesec.cfe.mx/api/CFEService/` y el
   portal `https://msc.cfe.mx/.../Proveedores/` (doc 04); no se hallaron secretos (`client_secret`,
   API keys) embebidos. Maneja datos de proveedor (IdProveedor, RFC/razón social, consorcios,
   concursos), no pagos con tarjeta ni PII de ciudadanos.

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación. **No
redistribuye** el APK ni los DLL. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. "Función por función" en .NET aquí = **metadata/IL/literales**, no C# textual
(no hay ILSpy en el entorno). **Firma** re-firmada por APKPure (`CN=Android, O=Google Inc.`) → no
verificable como el artefacto auténtico de la CFE.
