# Resumen — ProcurApp 2.3.5 (`mx.gob.pgjguanajuato.procurapp`)

## Identidad
- **App:** ProcurApp — **Fiscalía General del Estado de Guanajuato** (FGE, antes PGJ). Denuncias,
  pánico, emergencias, antecedentes, perfil genético, persona desaparecida, vehículo robado.
- **Package:** `mx.gob.pgjguanajuato.procurapp` · **versión** 2.3.5 (versionCode 70).
- **#40** por instalaciones (≈100 K). compileSdk 35.
- **Firma AUTÉNTICA:** `O=Procuraduría General de Justicia del Estado de Guanajuato`.

## Pila
**Nativa Kotlin/Java**, 2 dex, ~697 clases propias, **sin ofuscar**. Arquitectura MVVM por
módulo (`model`/`network`/`persistence`(Room)/`view`/`viewmodel`). Red con **Retrofit** + Gson;
persistencia con **Room**.

## Hallazgo crítico (confirmado en código + xref smali)

`utils.UnsafeOkHttpClient.getUnsafeOkHttpClient()` construye un OkHttpClient con:
- `X509TrustManager` cuyo `checkServerTrusted` está **vacío** (acepta cualquier certificado),
- `hostnameVerifier` que **siempre devuelve `true`**,
- `SSLContext.getInstance("SSL")` (protocolo débil).

**Lo usan** (xref en smali, no inferencia): `RetrofitClientInstancePROD` (**producción**),
`RetrofitClientInstanceGenetica` (**perfil genético**), `RetrofitClientInstanceWS_CITAS` (citas).
`RetrofitClientInstance` (default) **no** lo usa → esa ruta sí valida.

**Backends:** `https://portal.fgeguanajuato.gob.mx/`, `https://webservice.fgeguanajuato.gob.mx/`.
El HTTPS es **inútil** en las rutas con el cliente inseguro: un MITM presenta cualquier cert y la
app lo acepta.

## Superficie (módulos)
`denuncia`, `denuncia_casahabitacion`, `denuncia_genero`, `extorcion`, `emergencias`, `panico`,
`perfil_genetico`, `persona_desaparecida`, `antecedentes`, `investigacion`, `revision_vehicular`,
`vehiculo_robado`, `citas`, `registro`/login, `perfil`, `quejas_sugerencias`, `notificaciones`,
`twitter`.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| TLS (PROD/Genética/Citas) | 🔴 **DESACTIVADO** (trust-all) → MITM |
| TLS (ruta default) | ✅ valida |
| Datos | 🔴 PII de víctimas/denunciantes, género, ADN, pánico |
| Backends | ⚠️ HTTPS, pero sin validar cert en las rutas críticas |
| Ofuscación | ⚠️ ninguna (transparente) |
| Firma | ✅ auténtica de la FGE |

**Lectura:** es, junto con Infonavit y Licencia EDOMEX, uno de los hallazgos más graves del
acervo — y el de **datos más sensibles** (una Fiscalía, con denuncias de género, pánico y perfil
genético, enviando por un canal TLS que no valida al servidor). Amerita divulgación responsable.
