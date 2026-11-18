# 03 — Licencia del SDK, cuota de escaneos y validez

El SDK PersonalCode es **comercial y licenciado**. La licencia viaja embebida en el APK como
`assets/license.txt` (copiada a `evidence/license.txt`, no versionada).

## Estructura de `license.txt`

Es una sola línea con campos separados por `|`:

```
Android | mx.ine.rfe.lectorqr | False | False | vcredencial:100d | 20260604 | | <SHA1-A>,<SHA1-B> | | 463-1-T-<blob base64 ~largo> | F | <firma ECDSA base64>
```

| Campo | Valor observado | Lectura |
|---|---|---|
| Plataforma | `Android` | — |
| Package autorizado | `mx.ine.rfe.lectorqr` | La licencia está atada a **este** package |
| Flags | `False` / `False` | Banderas del SDK (no determinadas) |
| Producto/feature | `vcredencial:100d` | Módulo "validación de credencial"; `100d` parece un parámetro de cuota/caducidad |
| Fecha | `20260604` | **2026-06-04** — fecha asociada a la licencia (emisión o caducidad) |
| Huellas de certificado | `67:86:89:BB:…`, `01:90:20:19:…` (SHA-1) | La licencia está **atada a certificados de firma** concretos |
| ID de licencia | `463-1-T-…` + blob base64 | Payload firmado de la licencia |
| Firma | `F` + `MGQCMD…` (ASN.1/ECDSA) | **Firma digital** del emisor del SDK sobre la licencia |

### Hallazgo 3 — Licencia firmada, atada al package y a los certificados

La licencia no es un simple flag: está **firmada criptográficamente** (bloque final ECDSA,
formato DER `MGQCMD…`) y **vinculada** tanto al package `mx.ine.rfe.lectorqr` como a dos
huellas SHA-1 de certificado. Esto permite al SDK validar **offline** (sin red) que:
1. corre dentro del package autorizado,
2. firmado por el certificado esperado,
3. con una licencia no manipulada.

Es el mecanismo que, junto con el núcleo nativo (doc 02), hace que la validación funcione sin
conexión pero siga siendo controlada por el proveedor del SDK.

## Cuota de escaneos

La fachada `Azf` expone `hasRemaining(Activity): boolean` y `getRemaining(Activity): int`:
el SDK lleva una **cuenta de escaneos restantes**. El token `vcredencial:100d` de la licencia
es coherente con un límite de uso (p. ej. número de validaciones o ventana temporal), aunque
el valor exacto se interpreta en el código nativo.

### La fecha `20260604` (dato observado, no veredicto)

La licencia incluye la fecha `20260604` (2026-06-04). A la fecha de este análisis
(2026-11-17) esa fecha **ya pasó**. No obstante, **no se afirma** que la app esté caducada o
inoperante: si `20260604` es fecha de emisión o de caducidad, y si el SDK bloquea el uso tras
esa fecha, depende de la lógica de aplicación de la licencia, que vive en `libO0p.so`/
`libl1I.so` y **no se decompiló**. Se deja como dato verificable y como pregunta abierta; el
build analizado proviene de APKPure y podría no ser la última versión publicada por el INE en
Play.

> Evidencia: `assets/license.txt` (ver `evidence/license.txt`); métodos `hasRemaining`/
> `getRemaining` en `com.personalcode.Azf`.
