# Firma, recursos y procedencia

## Firma del APK

```
Signer #1 certificate DN: L=Mexico
Signer #1 SHA-256: 9066f751073a0cdcb6210f0fe93f9b71ce54e8430adad601065a2c0b1ef3b25d
Source Stamp Signer DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, …
Source Stamp SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

- El **certificado de app** tiene DN mínimo `L=Mexico` (sin CN/O) — inusual, pero **es un
  certificado propio**, no el genérico de Google. Plausiblemente el certificado real con el que
  se firmó la app (o el del proveedor que la desarrolló).
- El **Source Stamp** sí es el genérico `CN=Android, O=Google Inc.`. El source stamp lo añade
  el pipeline de Play/redistribución; su presencia genérica indica que el paquete **pasó por un
  re-proceso** (coherente con la descarga vía APKPure).
- **Conclusión**: el binario no es verificable como idéntico al de Play sin comparar contra el
  canal oficial, pero el certificado de app sugiere origen legítimo. Para los hallazgos de
  arquitectura (doc 03–08) esto no cambia nada; para una afirmación forense sobre bytes exactos,
  obtener el APK desde Google Play con cuenta.

## Estructura del bundle XAPK

Bundle multi-split con **20 APK** (base + 19 configs):

| APK | Rol |
|---|---|
| `st.android.imsspublico.apk` | base (shim DEX + `assemblies.blob` + recursos) — **el analizado** |
| `config.armeabi_v7a.apk` | librerías nativas (runtime Mono) |
| `config.{es,en,ar,de,fr,hi,in,it,ja,ko,my,pt,ru,th,tr,vi,zh}.apk` | **17 idiomas** |
| `config.mdpi.apk` | recursos de densidad |

El soporte de 19 idiomas (incluye árabe, hindi, japonés, coreano, birmano, tailandés…) es
inusual para una app de trámite nacional — probablemente viene del template base, no de una
localización real a esos idiomas.

Hashes del XAPK y el APK base en `hashes.sha256`.

## Recursos y assets

- **2,381 recursos**.
- `assets/` relevante:
  - `assemblies/assemblies.blob` (17 MB) + `assemblies.manifest` — código .NET (doc 02).
  - `agconnect-services.json` — config Huawei AGConnect.
  - `api_process_fallback_rules_ine.json`, `api_process_fallback_rules_passport.json` — reglas KYC.
  - `attributions.txt` — licencias de terceros.
  - Tipografías (Montserrat, NotoSans, Roboto + decorativas) y fuentes de iconos.
  - Varias fuentes de "image editor"/avatar → módulo de edición de imagen de documento/perfil.

## Inventario de código

- DEX: **19,312 clases** (mayormente bindings Xamarin `crc64…` + SDKs); la lógica real es C#.
- `assemblies.blob`: **162 assemblies .NET** (LZ4/XALZ). App principal `IMSSDigital.dll`.
- Librería nativa: runtime Mono en los splits por ABI (no en el base).

## Versionado

Lo propietario de IMSS (APK, `res-decoded/`, `assemblies.blob`, `.so`, assets) está en
`.gitignore` y **no se sube**. Vive en el SSD bajo `/Volumes/PortableSSD/apps-gobmx-mx/`. El
repositorio solo versiona los documentos de análisis y `decompiled/endpoints.txt` (rutas de API,
no secretos).
