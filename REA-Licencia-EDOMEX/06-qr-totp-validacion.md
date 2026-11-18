# QR de la licencia: TOTP, firma electrónica y modo verificador

La app genera el QR de la licencia digital y también puede **validar** QRs escaneados
(verificador). Dos piezas: un código basado en tiempo (TOTP) y una validación de formato por
estado.

## Generación del QR (emisor)

Clases: `ui.licenses.detail.qr.QRGeneratorImpl`, `utils.QRGenerator`,
`domain.GetQrBitmapUseCase`, `domain.GetESignatureUseCase`, `ui.licenses.detail.qr.QrViewModel`,
`data.QrRepositoryImpl`.

El QR incorpora un **TOTP** (código de un solo uso basado en tiempo). Evidencia en `utils.totp`:

- `Totp`, `Hmac`, `Hash`, `Time`, `Digits` — implementación RFC 6238.
- `Hash.smali`: `HmacSHA1`, `HmacSHA256`, `HmacSHA512`.
- `Hmac`/`Totp` operan sobre un `"secret"` (el *seed*).

Combinado con `GetESignatureUseCase` + `ElectronicSignData` (doc 05), el QR transporta un código
**time-based + firma** de modo que un verificador pueda comprobar vigencia/autenticidad.

**Límite honesto:** el **origen del *seed* TOTP y de la firma electrónica** no se resolvió por
completo. Si el *seed* se aprovisiona por licencia desde el servidor (`prod.cosmokeys...`,
doc 03) el esquema es razonable; si fuese fijo/derivable, el QR sería falsificable. **No se
afirma** cuál es el caso — requiere trazar el aprovisionamiento desde `cosmokeys`.

## Validación del QR (verificador)

`decompiled/ValidateQrUrlUseCase.smali`:

```smali
const-string v0, "ValidateQrUrlUseCase"
Log.d(...)                                  # loggea el contenido del QR escaneado
Proyecto$Companion->getPatternForProject(I)Lkotlin/text/Regex;   # patrón por estado
Regex->matches(CharSequence)Z              # valida que la URL del QR cumpla el formato
```

La validación local es una **comprobación de formato de URL por "proyecto"/estado** (regex), no
una verificación criptográfica en el dispositivo. La verificación fuerte (si existe) ocurriría en
el backend al abrir la URL — no observable estáticamente.

**Observación menor:** `Log.d("ValidateQrUrlUseCase", <contenido del QR>)` escribe el contenido
del QR escaneado al logcat. En release el impacto es bajo (logcat no es legible por otras apps en
Android moderno), pero es telemetría de datos de licencia en el log. Se anota sin exagerar.

## Enums de dominio

`enums.Proyecto` (estado/despliegue), `enums.QrType`, `enums.Validity`, `enums.ProcedureType`,
`enums.Gender` → confirman la parametrización multi-estado (doc 02) y los tipos de QR/trámite.
