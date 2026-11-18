# 07 — Firma, recursos y procedencia

## Firma del APK: auténtica del C5

```
Signer #1 certificate DN:  CN=CAEPCCM, OU=Desarrollo, O=CAEPCCM, L=CDMX, ST=CDMX
Signer #1 SHA-256:         89ae3f338406e39907fa06fff0c52cccd2e3cc43cd7caedb2b0d2f0ade113fdf
Source Stamp:              (ninguno)
```

- El certificado es **propio del emisor**: `O=CAEPCCM` (Coordinación General del C5), no el
  genérico `CN=Android, O=Google Inc.`. La verificación de firma **pasa**.
- **No hay Source Stamp.** A diferencia de las apps re-empaquetadas por APKPure (p. ej. CFE,
  cuyo Source Stamp es el genérico de Google), aquí la ausencia de stamp y el certificado
  del C5 indican que **este binario es el artefacto original**, redistribuido sin
  re-firmar. Es, como en Banjército/INE del mismo lote, una procedencia **fuerte**: la
  firma identifica al emisor real.

Esto permite hacer afirmaciones de integridad: los hallazgos de seguridad de este informe
son sobre el **binario auténtico** publicado por el C5, no sobre una copia alterada.

## Estructura del APK

- Split por ABI (`@arm64-v8a`); base + splits de densidad/idioma (descargado como parte del
  bundle). 2 DEX (código), recursos (`resources.arsc`, `res/`), una `.so` de terceros (GIF).
- `extractNativeLibs="false"` → la `.so` se carga comprimida desde el APK.
- Versión 1.4.7 (versionCode 40), targetSdk 33.

## Recursos de interés

- `res/values/strings.xml`: claves de Twitter y Google Maps en claro (doc 06).
- Iconos/marca `app_logo_911`; tema `AppTheme`.
- Cadena que referencia el feed "**DE TWITTER DE SISMOLÓGICO MX Y SASMEX**" (origen del uso
  del Twitter SDK — alertas sísmicas).

## Hashes

Ver [`hashes.sha256`](hashes.sha256). SHA-256 del APK:
`c2ab3ebe52d4575e9e5a4f31da4792c895ad5a5bd02c48916d4e0cb3a9ca7fe7`.

## Ofuscación

**Ninguna** sobre el código propio: los nombres de paquete, clase y método se leen
íntegros (`activities.AlertActivity`, `services.LocationService`, `utils.VolleySingleton`,
`App066PreferenceManager.setBloodType`, …). No hay renombrado R8/ProGuard ni ofuscadores
comerciales (DexGuard). Esto facilita la auditoría pero también la ingeniería inversa por
un tercero malicioso.
