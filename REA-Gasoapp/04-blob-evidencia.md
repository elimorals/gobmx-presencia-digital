# Subida de "evidencia" a Blob Storage

## La configuración

En la config embebida del DLL propio:

```
"GetBlobStorage": "UriContainerEvidencia?deviceId={0}&permisoId={1}"
```

más la dependencia **`Microsoft.WindowsAzure.Storage.dll`**. Junto con el permiso `CAMERA` y los
`Service` de calificación (tienda/restaurante/baños/agua), el flujo es: el ciudadano levanta una
**queja con foto de evidencia** de la estación, y la app la **sube a un contenedor de Azure Blob
Storage** cuya URI se arma con `deviceId` y `permisoId`.

## Pregunta abierta (no confirmada) — autorización del contenedor

- La plantilla `UriContainerEvidencia?deviceId={0}&permisoId={1}` sugiere que el acceso al blob se
  parametriza por **identificador de dispositivo** y **de permiso/trámite**, no por una sesión de
  usuario autenticada. **Si** el contenedor es público o el SAS no está acotado por objeto, y los
  `permisoId` son predecibles, podría exponerse **evidencia de terceros**.
- **No verificable estáticamente:** el token SAS / la URI completa del contenedor se obtendrían en
  runtime desde el backend (`GetBlobStorage` parece un endpoint que devuelve la URI firmada). No se
  halló un SAS ni un `AccountKey` embebido (positivo). Se reporta como **superficie a revisar del
  lado del servidor**, no como hallazgo confirmado.

## Lo positivo
- **Sin credenciales de Storage en el cliente** (ni `AccountKey` ni SAS hardcodeado). El patrón
  correcto es que el backend emita un SAS acotado; aquí no se puede confirmar que lo haga, pero
  tampoco hay la mala práctica de embeber la llave de la cuenta.
- La foto de evidencia es dato que el propio usuario aporta sobre una estación pública (no PII
  sensible de terceros de forma evidente).
