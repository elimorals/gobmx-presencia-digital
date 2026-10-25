# 04 · Autenticación y credenciales hardcodeadas — H2 (Alta)

Evidencia: campos estáticos de `com.infonavit.data.RetrofitService` y su `<clinit>`
(`evidence/smali/RetrofitService.smali`). Los valores se **redactan parcialmente** aquí; están
íntegros en el smali de evidencia (propietario, gitignored).

## Credenciales de servicio embebidas en el APK

`RetrofitService` declara constantes de usuario/clave para tres planos de backend:

| Constante | Valor (redactado) | Forma | Rol inferido |
|---|---|---|---|
| `USER_PAYMENT` | `UMCIpdMAP` | **texto plano** | usuario del servicio de pago |
| `PASSWORD_PAYMENT` | `PMCI…pdMAP` | **texto plano** | clave del servicio de pago |
| `USER_AUTH` | `ZV54…yJs=` | base64 (32 B) | usuario Basic Auth servicios |
| `PASSWORD_AUTH` | `2dx2…07s=` | base64 (32 B) | clave Basic Auth servicios |
| `USER_AUTH_UPLOAD` | `e7BC…NOQ=` | base64 (32 B) | usuario de carga de documentos |
| `PASSWORD_AUTH_UPLOAD` | `ZYgV…deg=` | base64 (32 B) | clave de carga |
| `USER_MQ` | `l/Ni…g3Q=` | base64 (32 B) | usuario cola MQ |
| `PASSWORD_MQ` | `ueJO…dV8=` | base64 (32 B) | clave cola MQ |
| `X_API_KEY_REGISTRO`, `X_API_FLAG_REGISTRO` | — | string | cabeceras del servicio de registro |

- **Las de pago están en texto plano**: cualquiera que abra el APK las lee directamente.
- **Las `*_AUTH` y `*_MQ`** son cadenas base64 de 44 caracteres (32 bytes). Se descifran en runtime
  con `CryptoHelper` (`AES/ECB/NoPadding`, doc 05) usando una llave **también hardcodeada en el
  mismo binario** → la "protección" es reversible por quien tenga el APK (security theater).

## Material de llave adyacente

En el `<clinit>` de `RetrofitService` (y de `EnviromentRetrofit`) aparecen, antes de inicializar
`helper:CryptoHelper`, cuatro strings cortos:

```
"9B&cV0sT"   "Zq!8Rt5$kLm2"   "mQ#4vR8z"   "Xy6$P1wU"
```

Por su longitud (8/12/8/8) y posición son candidatos a **llave/semilla de AES** para `CryptoHelper`
(p. ej. concatenación a 16 bytes). No se ejecutó el descifrado para confirmar la combinación ni
para recuperar las credenciales en claro — se documenta el mecanismo, no el secreto.

## Referers y User-Agent

`REFERER_CONSULTA_DOCUMENTO = http://infonavit.org.mx/GENERICO/PORTAL/sndConsultaDocumentos`,
`REFERER_CASO = http://infonavit.org.mx/GENERICO/sndBusquedaOrdenGenerico`,
`REFERER_REGISTRO` deriva de `https://micuenta.infonavit.org.mx` + `/registro`. `USER_AGENT` se
inicializa vacío y lo fija en runtime el `UserAgentInterceptor` (doc 03).

## Login de derechohabiente

El login de usuario (NSS/CURP + contraseña, con biometría opcional) vive en
`com.infonavit.micuentainfonavit.ui.login` (`SplashActivity` → `LoginV3Activity`) y
`ui.biometric`. No se trazó el flujo token completo en estático; los paquetes `token_infonavit` y
`nip_infonatel` indican generación de token/NIP en la app. El login del **usuario** es distinto de
las credenciales de **servicio** de arriba: estas últimas autentican la app contra el gateway, no
al derechohabiente.

## Impacto

Las credenciales de servicio (sobre todo las de pago, en claro) permiten a un atacante que extrae
el APK autenticarse contra los backends de INFONAVIT con la misma identidad que la app. Combinado
con H1 (MITM), se puede además observar cómo se usan en vivo.

## Recomendación

Sacar toda credencial de servicio del cliente: la autenticación app↔gateway debe hacerse con
tokens efímeros emitidos por el servidor (OAuth2 client-credentials con secreto en backend, o
attestation), nunca con usuario/clave fijos en el binario. Rotar las credenciales expuestas.

## Límite

No se descifraron los blobs base64 ni se validó la combinación de llave; no se probó el uso real
de las credenciales contra el backend.
