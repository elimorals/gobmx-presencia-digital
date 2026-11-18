# 08 — Criptografía y almacenamiento

## Criptografía de aplicación: ausente

- **No se observó uso de `System.Security.Cryptography` en `ICV_APP.dll`**: búsqueda negativa de
  `Aes`, `Rijndael`, `RSA`, `HMAC*`, `Rfc2898`/`Pbkdf2`, `CreateEncryptor`, `CipherMode`,
  `PaddingMode`. Solo hay utilidades **Base64** (`FromBase64String`/`ToBase64String`,
  `getBase64FromBlobData`, `convertBase64StringToPdfAndStoreIt`) — Base64 es codificación, **no
  cifrado**.
- El split incluye `libSystem.Security.Cryptography.Native.Android.so`, pero es la librería
  nativa que .NET enlaza por defecto; no implica que la app cifre datos propios.
- Consecuencia: la confidencialidad de credenciales y PII **depende por completo del transporte
  HTTPS** (doc 03). No hay capa de cifrado en cliente que proteja datos en reposo ni payloads.
- `ActivaCryptoRequest` (doc 04) lleva "Crypto" en el nombre pero **no se halló la operación
  criptográfica asociada** en el binario. No se afirma qué hace; se deja como pregunta abierta.

## Almacenamiento local

- **`allowBackup="true"`** (doc 01): preferencias y datos de la app son respaldables (adb backup
  / nube), incluido lo que persista de sesión (usuario recordado, token de atención, licencia
  offline). Contrasta con la higiene de AforeMóvil (`allowBackup=false`). Ver H3.
- **Licencia offline** (`PopUpLicOff`, `ResLicenciaEntity`) + guardado de PDF/imagen vía los
  helpers Base64→archivo del WebView (doc 07) y permisos `WRITE/READ_EXTERNAL_STORAGE`: hay datos
  de licencia (con imagen) escritos a almacenamiento. No se observó que se cifren en reposo.
- `Preferences`/almacenamiento de MAUI Essentials para el "Recordar Usuario" y configuración.

> Límite: la metadata PE expone miembros, llamadas y literales, no el C# textual (no hay ILSpy
> en el entorno). "Sin cripto" se afirma por ausencia de referencias/llamadas a
> `System.Security.Cryptography` y de literales de algoritmos en `ICV_APP.dll`, no por lectura
> línea a línea del cuerpo descompilado.
