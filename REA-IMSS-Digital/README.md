# Ingeniería inversa de IMSS Digital 8.7.4 — índice

Análisis **estático** del APK `st.android.imsspublico` (base del bundle XAPK) con REA
(JADX headless 0.7.1 + apktool) + descompresión de los assemblies .NET. La app **nunca se
ejecutó**. Es la app oficial pública del Instituto Mexicano del Seguro Social, **#3 por
instalaciones** del inventario gob.mx (≈32.25 millones en Google Play).

> A diferencia de CFE Contigo (ofuscada con DexGuard), IMSS Digital está hecha con
> **Xamarin/.NET** y su código C# se pudo **descomprimir**, exponiendo **83 endpoints reales**
> y todo el stack de verificación de identidad. Es el análisis más revelador del lote.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, stack .NET/Xamarin, módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | **Permisos de sistema extremos**, componentes, `usesCleartextTraffic=true` |
| 02 | [02-arquitectura-xamarin.md](02-arquitectura-xamarin.md) | .NET/Xamarin.Forms, `assemblies.blob` LZ4, cómo se extrajo |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | **83 endpoints**, microservicios, HTTP vs HTTPS, QA/preprod, IP |
| 04 | [04-identidad-biometria-kyc.md](04-identidad-biometria-kyc.md) | Mati/MetaMap, FaceTec, Acuant, INE/pasaporte/CURP |
| 05 | [05-salud-datos-sensibles.md](05-salud-datos-sensibles.md) | Expediente clínico vía API: notas, laboratorio, vigencia, maternidad |
| 06 | [06-autenticacion.md](06-autenticacion.md) | Login, OTP SMS, biometría (BMT), token OAuth |
| 07 | [07-sdks-terceros.md](07-sdks-terceros.md) | Firebase, Huawei AGConnect, plugins Xamarin, SDKs KYC |
| 08 | [08-hallazgos-seguridad.md](08-hallazgos-seguridad.md) | **Hallazgos priorizados** (cleartext, QA en prod, IP, permisos) |
| 09 | [09-firma-recursos.md](09-firma-recursos.md) | Firma, estructura XAPK (20 idiomas), assets |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `decompiled/endpoints.txt` | **83 endpoints** extraídos de los assemblies .NET descomprimidos |
| `AndroidManifest.xml` | Manifiesto decodificado |
| `xapk-manifest.json` | `manifest.json` del bundle XAPK |
| `hashes.sha256` | SHA-256 del XAPK y del APK base |

> Lo propietario de IMSS (APK, `res-decoded/`, `assemblies.blob`, `.so`) **no se versiona**
> (`.gitignore`); vive en el SSD en `/Volumes/PortableSSD/apps-gobmx-mx/`.

## 6 hallazgos principales

1. **Tráfico en claro habilitado.** `android:usesCleartextTraffic="true"` y varios endpoints
   son **`http://`**, incluido el **login** (`http://siap.imss.gob.mx/ServiciosWebIMSSDigital/api/auth/login`)
   y servicios de pensionados y de salud. Credenciales y datos sensibles pueden viajar sin TLS
   (doc 03, 08).
2. **Datos de salud por API.** El expediente clínico es accesible vía microservicios
   `cds.imss.gob.mx`: notas médicas, resultados de laboratorio, somatometría, vigencia de
   derechos, incapacidades por maternidad (doc 05).
3. **Verificación de identidad con 3 proveedores + biometría facial.** Mati/MetaMap (dominante),
   **FaceTec** (liveness, vía `facetec-preprod.firmaautografa.com`) y **Acuant** (lectura de
   INE/pasaporte, región EU). Lee INE, pasaporte, CURP y rostro (doc 04).
4. **Entornos de prueba en el binario de producción.** `cds-qa.apps.ocp.imss.gob.mx`,
   `*-cmov-qa.apps.qaocp.imss.gob.mx` y `facetec-**preprod**` quedaron embebidos (doc 03, 08).
5. **Permisos de sistema imposibles para una app normal.** Declara `MOUNT_FORMAT_FILESYSTEMS`,
   `INJECT_EVENTS`, `MODIFY_PHONE_STATE`, `MOUNT_UNMOUNT_FILESYSTEMS`, `ACCESS_MOCK_LOCATION`
   — permisos `signature|privileged` que el sistema no concede a apps de tienda (doc 01, 08).
6. **IP pública hardcodeada.** `https://201.149.45.42/Chat_Yo_Decido/` — un chat referenciado
   por dirección IP directa en vez de dominio (doc 03).

## Alcance y límites

Análisis **original** de una app pública del gobierno mexicano que se posee, con fines de
investigación de seguridad. **No redistribuye** el APK ni los assemblies de IMSS
(`.gitignore`). Los endpoints documentados son rutas de API presentes en el binario, no
secretos ni datos de usuarios. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. **Firma del APK**: el certificado de app es `L=Mexico` (propio), pero el
Source Stamp es el genérico de Google (señal de re-proceso por el redistribuidor APKPure);
no verificable como artefacto idéntico al de Play sin comparar contra el canal oficial.

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
