# Criptografía y TLS

## Validación TLS: estándar, sin bypass (positivo)
Un barrido completo de `DemoCFE.dll` (y de los literales/metadata) **no** encontró ninguno de los
antipatrones de desactivación de TLS de .NET:
- sin `ServerCertificateCustomValidationCallback` / `DangerousAcceptAnyServerCertificateValidator`,
- sin `RemoteCertificateValidationCallback` que devuelva `true`,
- sin manipulación de `ServicePointManager.ServerCertificateValidationCallback`.

`DemoCFE.Data.RestService` usa `HttpClientHandler` con su configuración por defecto → **validación
de certificado estándar contra las CAs del sistema**. Es la postura **correcta**, lo opuesto a
ProcurApp / Licencia EDOMEX / Infonavit (que anulan la validación).

## Pinning
**No hay certificate pinning** (ni `CertificatePinner` equivalente, ni certificados/fingerprints
embebidos). Para una app B2G de licitaciones es una omisión menor; la confianza recae en las CAs
del sistema, que es el estándar aceptable.

## Almacenamiento
No se observó cifrado local propio (SQLCipher/keystore) ni secretos embebidos. Con
`allowBackup=true` (doc 01), cualquier dato persistido localmente (sesión/token) sería
respaldable; el detalle de qué se persiste vive en los cuerpos de método no desensamblados.

## Nota de alcance
La ausencia de marcadores trust-all se afirma sobre los literales + metadata del ensamblado
propio. Un callback construido dinámicamente o en un ensamblado de terceros no sería visible por
strings; no se observó indicio de ello, pero se marca el método (estático, sin ejecución) como
límite.
