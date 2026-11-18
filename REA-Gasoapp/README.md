# Ingeniería inversa de Gasoapp 2.2.1 — índice

Análisis **estático** del APK `cre.comercializacion.mobile` con REA. La app **nunca se ejecutó**.
Es la app oficial de la **Comisión Reguladora de Energía (CRE)** — consulta de estaciones de
servicio (gasolineras) y **calificación/queja ciudadana** sobre precios y servicios (tienda,
restaurante, baños, agua). **#31 por instalaciones** del inventario gob.mx (≈500 K).

> **Xamarin.Forms clásico (.NET/Mono), DLLs comprimidas XALZ.** Se descomprimieron los
> ensamblados LZ4 (`XALZ`) de `assemblies/` y se analizó el propio
> `Cre.Comercializacion.Mobile.XForms.dll` (metadata/strings .NET). Build **viejo** (2020,
> targetSdk 29, Xamarin clásico EOL). Lo recuperado limpio: **3 endpoints de API** (incluido un
> **QA** y un **dominio Azure crudo** en el binario de producción) y la ruta de **subida de
> "evidencia" a Blob Storage** por `deviceId`+`permisoId`. **Sin bypass TLS y sin secretos
> hardcodeados** (postura correcta). Firma **auténtica de la CRE**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Xamarin, endpoints, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación, cámara) |
| 02 | [02-arquitectura-net.md](02-arquitectura-net.md) | Xamarin.Forms, XALZ, MVVM, servicios |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | 3 BaseUrls (prod/QA/Azure), TLS |
| 04 | [04-blob-evidencia.md](04-blob-evidencia.md) | Subida de evidencia a Blob Storage |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma auténtica CRE |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos y preguntas abiertas |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK (versionado) |
| `decompiled/config-endpoints.txt` | Config .NET: endpoints + blob + servicios (no versionado) |
| `evidence/*.bin` | DLLs propios descomprimidos de XALZ (no versionados) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 5 hallazgos principales

1. **(Bajo-Medio) 3 BaseUrls en el binario de producción, incl. QA y un dominio Azure crudo.**
   `api-gasoapp.cre.gob.mx` (producción, oficial), **`api-gasoapp-qa.cre.gob.mx`** (entorno
   **QA** dejado embebido) y **`api-estacionservicio-327800.gasoapp.p.azurewebsites.net`** (un
   servicio en el **dominio por defecto de Azure App Service**, fuera de `cre.gob.mx`, con nombre
   interno `327800`). Fuga de infraestructura/entornos (doc 03).
2. **(Pregunta abierta) Subida de "evidencia" a Blob Storage por `deviceId`+`permisoId`.**
   Config `"GetBlobStorage": "UriContainerEvidencia?deviceId={0}&permisoId={1}"` + dependencia
   `Microsoft.WindowsAzure.Storage` → el ciudadano sube fotos de evidencia a un contenedor
   parametrizado por identificadores. Si el contenedor/SAS no exige autorización adecuada,
   superficie de acceso a evidencia ajena; **no verificable estáticamente** (el SAS se obtendría
   en runtime) (doc 04).
3. **TLS estándar, sin bypass (positivo).** No hay `ServerCertificateCustomValidationCallback`
   ni `DangerousAcceptAnyServerCertificateValidator` ni trust-all en los DLLs → la validación de
   certificado es la del sistema. Contrasta con Infonavit/Licencia EDOMEX (doc 03).
4. **Sin secretos hardcodeados (positivo).** No se hallaron `AccountKey`/SAS de Storage ni API
   keys/`ClientSecret` en los ensamblados descomprimidos (doc 03–04).
5. **Build obsoleto.** Xamarin.Forms clásico (EOL), DLLs de 2020, targetSdk 29 — runtime y
   dependencias sin mantener; deuda técnica/seguridad por falta de actualización (doc 02).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK ni los DLLs. Observaciones **estáticas**: no se ejecutó la app ni se
llamó a ningún endpoint; no se subió evidencia. En .NET, "función por función" significa nivel de
**metadata/strings/estructura** (no C# textual: no hay ILSpy en el entorno). **Firma auténtica
de la CRE** (`O=Comisión Reguladora de Energía`) → binario íntegro del emisor.
