# IMSS Digital — Ingeniería inversa (REA)

> Análisis **estático** del APK base de `st.android.imsspublico`. La app nunca se ejecuta.
> Herramienta: REA (JADX 0.7.1 + apktool) + descompresión LZ4 de los assemblies .NET.
> Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| App | IMSS Digital (Instituto Mexicano del Seguro Social, oficial) |
| Paquete | `st.android.imsspublico` |
| Versión | **8.7.4** (versionCode 259) |
| minSdk / targetSdk | 28 (Android 9) / 36 |
| Instalaciones (Play, inventario gob.mx) | **32,254,664** — #3 del ranking |
| APK base | SHA-256 `4e039450a300e1eadad8ffd5b7b63e622730377987cebbbc3b0ac401ac43d28d` |
| Clases DEX (shim) | 19,312 · Recursos | 2,381 |
| **Runtime** | **Xamarin / .NET** — código C# en `assemblies/assemblies.blob` (17 MB, 162 assemblies LZ4) |
| Firma app | DN `L=Mexico`, SHA-256 `9066f751…` (propio); Source Stamp genérico de Google |
| Idiomas | 19 splits (`config.es/en/ar/de/fr/…`) |

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| Runtime | **Xamarin.Forms (.NET/Mono)** | 363 refs `Lmono/`, `assemblies.blob`, nombres de clase `crc64…` |
| Código | C# AOT en `assemblies.blob` (XALZ/LZ4) | `IMSSDigital.*.dll`, 162 bloques XALZ |
| UI | Xamarin.Forms + SlideOverKit + SVG | `SlideOverKit.dll`, `Svg.Platform.dll` |
| Red | HttpClient .NET (endpoints en C#) | 83 endpoints extraídos (doc 03) |
| Identidad/KYC | **Mati/MetaMap + FaceTec + Acuant** | `com.facetec.sdk`, `*.acuant.net`, `facetec-preprod.firmaautografa.com`, reglas INE/pasaporte |
| Biometría local | Plugin.Fingerprint | `Plugins.Fingerprint.dll`, `USE_BIOMETRIC` |
| Push/analytics | **Firebase** (FCM, Analytics) + **Huawei AGConnect** | `Plugin.FirebaseAnalytics.dll`, `agconnect-services.json` |
| Local DB | SQLite | `SQLitePCL.raw` |
| Mapas | Google/Apple/Bing maps | deep links de navegación |

## Módulos funcionales (por los endpoints, doc 03/05)

| Módulo | Backend |
|---|---|
| Autenticación | `siap.imss.gob.mx/.../api/auth/login`, `bmt.imss.gob.mx` (OAuth/OTP) |
| Derechohabiencia / vigencia / constancias | `canalmovil.imss.gob.mx/mscmov-derechohabiencia-rest` |
| **Expediente clínico** (notas, laboratorio, somatometría) | `cds.imss.gob.mx/mscds-*` |
| Pensionados (RJP) + prueba de supervivencia | `siap.imss.gob.mx/.../api/rjp/*` |
| Maternidad / incapacidades | `tspi.imss.gob.mx`, `cds-qa.apps.ocp.imss.gob.mx` |
| COVID | `testcoronavirus`, `covid-calculadora`, `infofamiliarcovid` |
| **Detección de violencia familiar** + centros de víctimas | `vfam.imss.gob.mx`, `msvfam-*` |
| VPH (papiloma) resultados | `msvph-consulta-…` |
| Reportes / notificaciones | `rpci.imss.gob.mx/msreci-*` |
| ChécaT (cuestionario de riesgo) | `chkt(back).imss.gob.mx` |
| Revista / CMS / carrusel | `canalmovil…/mscmov-cmsrevista`, `mscmsamid-cms` |
| Alta de clínica (UMF) + CURP | `gob.mx/curp`, `…/AltaCambioUMF` |

## Hallazgos (detalle en doc 08)

1. **Cleartext**: `usesCleartextTraffic="true"` + login y servicios en `http://`.
2. **Datos de salud por API** (expediente clínico completo).
3. **KYC triple** (Mati, FaceTec, Acuant) + datos biométricos a terceros (región EU).
4. **QA/preprod embebidos** en el binario de producción.
5. **Permisos de sistema** no concedibles declarados en el manifiesto.
6. **IP hardcodeada** (`201.149.45.42`).

## Contraste con el resto del lote

| | CFE | SAT | IMSS |
|---|---|---|---|
| Runtime | Nativo (Kotlin) | Flutter | **Xamarin/.NET** |
| Endpoints recuperables | No (DexGuard) | Parcial | **Sí (83, descomprimidos)** |
| Cleartext | No | No | **Sí** |
| KYC/biometría | ThreatMetrix | SAT ID | **Mati+FaceTec+Acuant** |
