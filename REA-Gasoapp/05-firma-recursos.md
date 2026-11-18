# Firma y procedencia

## Firma AUTÉNTICA de la CRE (original preservada)

```
Signer #1 certificate DN: CN="Horacio Colina Fajardo, O=Comisión Reguladora de Energía, L=CDMX, S=CDMX, C=MX"
```

- El certificado identifica al **emisor real** (un desarrollador bajo la organización **Comisión
  Reguladora de Energía**), **no** el genérico `CN=Android, O=Google Inc.` de APKPure → la firma
  **original se conservó**; APKPure redistribuyó el APK sin re-firmarlo. Es, por tanto, el
  **artefacto auténtico** publicado por la CRE (base de análisis fuerte).

Ver `evidence/firma.txt` para esquemas (v1/v2/v3) y Source Stamp.

## Hashes
Ver [`hashes.sha256`](hashes.sha256): SHA-256 del APK analizado.

## Recursos / estructura
- APK ~21 MB. Runtime Mono/Xamarin (`libmono*`) + `assemblies/*.dll` (XALZ) + recursos XForms.
- `assemblies/` incluye la app propia (2 DLLs) + Newtonsoft.Json, Microsoft.WindowsAzure.Storage,
  Plugin.* y el stack Xamarin.Android.Support (todo de 2020).
- Artefactos del análisis: DLLs propios descomprimidos en `evidence/*.bin` (no versionados) y la
  config en `decompiled/config-endpoints.txt`.
