# 03 — Red y endpoints

Evidencia: literales UTF-16 del `#US` de `ICV_APP.dll` + `inspect_managed_members` sobre
`ICV_APP.ISeguridad` (métodos, literales, call edges). Análisis **estático**: no se llamó a
ningún endpoint.

## Base de la API nativa

Un solo literal de base, en el `.ctor` y métodos de `ICV_APP.ISeguridad`:

```
https://agencias.icvnl.gob.mx:1046/appmovilapiseg/SeguridadContribuyenteController/
```

- **HTTPS** sobre el puerto **1046** (no 443). Host `agencias.icvnl.gob.mx`.
- Ruta `/appmovilapiseg/` (API de seguridad para app móvil) + controlador
  `SeguridadContribuyenteController`.
- El otro literal de la clase es `"1.3"` (versión de API/app enviada en la petición).

## Acciones (métodos de `ICV_APP.ISeguridad`)

Cada método concatena la base + su nombre de acción y hace POST (helper `POST`). Métodos
observados en la metadata:

| Método | Función (doc) |
|---|---|
| `Login` | Autenticación usuario/contraseña (04) |
| `GeneraToken` | Genera el token de atención (04) |
| `AltaUsuario`, `ActivarUsuario`, `ReEnviaCodigo` | Registro y activación (04) |
| `ValidarDuplicado`, `ValidaActivo`, `ReqUsuario` | Validaciones de alta (04) |
| `RecuperarContraseña`, `ReqRecuperarContraseña`, `RecuperaUsuarioFoto` | Recuperación (04) |
| `ActualizarDatos` | Cambio de correo/teléfono/contraseña (04) |
| `ObtenerVeh` | Consulta de vehículos del contribuyente (05) |
| `ObtenerLicBio`, `ImgLicencia`, `AgregaLicencia` | Licencia de conducir: datos/imagen/alta (05) |
| `UrlPortal`, `UrlTramite` | **Devuelven la URL del portal/trámite para el WebView** (06-07) |

> `UrlPortal`/`UrlTramite` son `async` (`<UrlPortal>d__13`, `<UrlTramite>d__14`): la app pide
> al servidor la URL a cargar en el WebView en vez de embeberla. No se pudo observar el valor
> (se resuelve en runtime/servidor) — se documenta como **superficie remota**, sin inventarlo.

## Transporte y serialización

- Cliente: `System.Net.Http.HttpClient` (patrón estándar MAUI; el `.ctor` de `ISeguridad`
  construye `System.Text.Json.JsonSerializerOptions` con `PropertyNameCaseInsensitive=true`).
  Cuerpos **JSON**.
- **No se observó tráfico en claro (`http://`) en la API nativa**: el único esquema en literales
  es `https://`. El riesgo de `usesCleartextTraffic=true` (doc 01) queda acotado a lo que el
  WebView cargue vía `UrlPortal`/`UrlTramite`, que no es observable estáticamente (hallazgo H2).

## TLS: validación y pinning

- **Sin pinning y sin bypass.** No hay en `ICV_APP.dll` ningún
  `ServerCertificateCustomValidationCallback`, `DangerousAcceptAnyServerCertificateValidator`,
  `HttpClientHandler` con validación desactivada, ni `TrustManager`/`HostnameVerifier`
  personalizado. La validación de certificado recae en la plataforma (cadena del sistema).
- Consecuencia: **no hay certificate pinning** (un CA comprometido o inyectado en el store del
  dispositivo permitiría MITM), pero **tampoco hay la desactivación de validación** que sí tiene
  Infonavit (`getUnsafeOkHttpClient`). Postura estándar. Ver doc 10 / H4.
