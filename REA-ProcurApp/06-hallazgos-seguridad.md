# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se enviaron denuncias ni se
llamó a ningún endpoint. El hallazgo crítico está probado por **código decompilado + xref smali**.

## H1 (CRÍTICO) — Validación TLS desactivada en producción y en genética
`utils.UnsafeOkHttpClient.getUnsafeOkHttpClient()` arma un OkHttpClient con `checkServerTrusted`
**vacío** y `hostnameVerifier` que devuelve **`true`** (código citado, doc 03). El xref en smali
confirma que lo usan **`RetrofitClientInstancePROD`** (producción), **`RetrofitClientInstanceGenetica`**
(ADN) y **`RetrofitClientInstanceWS_CITAS`** (citas). → **MITM total** sobre denuncias (incl.
género), pánico, perfil genético. Mismo antipatrón que Infonavit y Licencia EDOMEX, con datos
aún más sensibles. **Fix:** eliminar `UnsafeOkHttpClient`, validar con CAs del sistema en todos
los clientes, añadir pinning a `*.fgeguanajuato.gob.mx`, subir de `SSL` a TLS 1.2/1.3.

## H2 (Alto) — Exposición de PII de víctimas por el canal inseguro
Por H1, los datos más sensibles de la app viajan sin validación de certificado (doc 04):
identidad y relato de denunciantes, **violencia de género**, **ubicación de pánico**, **perfil
genético/ADN**, personas desaparecidas, extorsión. Impacto sobre la **seguridad física** de
personas en riesgo, no solo privacidad.

## H3 (Medio) — Evidencia fotográfica con geolocalización
`CAMERA` + `ACCESS_MEDIA_LOCATION` (doc 01): las fotos de evidencia pueden llevar coordenadas
EXIF del lugar/denunciante, que suben por el canal inseguro (H1). **Fix:** despojar EXIF antes de
subir; de todos modos corregir H1.

## H4 (Bajo, pregunta abierta) — Posible IDOR en cartas de antecedentes
`antecedentes` descarga cartas por `{folio}` (`CartasFolioRepository`, doc 04). Si los folios son
predecibles y el endpoint no liga autorización al folio, superficie de IDOR. **No verificable
estáticamente**; a revisar del lado del servidor. No se afirma que exista.

## Positivos / neutrales

- **Firma auténtica** de la FGE (doc 05) → binario íntegro; el defecto es del código oficial.
- **Código sin ofuscar** → auditable (también por un atacante, pero facilita la corrección).
- **La ruta `RetrofitClientInstance` default SÍ valida** TLS → el equipo sabe hacerlo bien; el
  fix es homogeneizar.

## Límites (sin inventar)

- No se enumeraron las rutas exactas de cada API bajo `portal`/`webservice.fgeguanajuato.gob.mx`
  (no se tocó el servidor).
- El contenido real de los request (campos de la denuncia, formato del perfil genético) no se
  decompiló método a método en cada módulo; el hallazgo central (TLS) no lo requiere.
- `SSLContext.getInstance("SSL")` indica protocolo débil además del trust-all; el handshake real
  no se observó (estático).
- Posible IDOR de antecedentes: hipótesis, no confirmada.
