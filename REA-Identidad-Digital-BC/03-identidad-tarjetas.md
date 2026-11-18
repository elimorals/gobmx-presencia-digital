# Identidad, tarjetas y compartir

Qué PII maneja la app y cómo, según los modelos y las APIs (nombres propios, no inventados).

## Datos de identidad (modelos propios)

- **`Ciudadano` / `Identidad` / `UsuarioCiudadano`** — identidad del ciudadano.
- **`Credenciales` / `AuthorizationCredentials` / `TokenSession`** — credenciales y sesión.
- **CURP** — verificación de identidad vía `ObtenerCurpContract`/`VerificarIdentidadCurpPresenter`
  y el endpoint `https://www.gob.mx/curp` (doc 04).
- **`Licencia` / `LicenciaImagen` / `TipoLicencia`** — licencia de conducir digital.
- **`TarjetaCirculacion` / `TarjetaImagen` / `VigenciaTargetaCirculacion`** — tarjeta de
  circulación vehicular digital.
- **`Documento` / `DocumentoPersonal` / `DocumentosDigitales`** — documentos personales.
- **`Dispositivo`** — datos del dispositivo (ligado a `READ_PHONE_STATE`).

## Registro y autenticación (flujo)

1. **Registro** (`RegistroV2Page1-3`, `RegistroUsuarioPresent`) con verificación por **CURP**.
2. **OTP SMS** vía Firebase Phone Auth (`SolicitarCodigoSms` → `ConfirmarCodigoSms`,
   `SmsFirebaseActivity`, `PhoneVerificationApi`).
3. **reCAPTCHA Enterprise** (`model.reCaptcha.*`) como capa anti-bot/antifraude.
4. **Login** (`LoginPresenter`/`LoginAPI`) con token de sesión (`TokenGenerator`,
   `AuthorizationCredentials`).

Es un flujo de alta **razonable** para una app de identidad: identidad verificada (CURP) +
posesión del teléfono (OTP) + anti-bot (reCAPTCHA).

## Compartir tarjeta de circulación con terceros

Módulo dedicado: `CompartirTarjetaActivity`, `CompartirTarjetaPresenter`,
`TarjetaCirculacionCompartida`, `RegistroTcCompartida`, `TokenCompartirTarjeta`,
`ListaPersonasTcCompartidaPresenter`, `TarjetaCompartidaAdapter`, y `ScannQrActivity`.

El ciudadano puede **compartir su tarjeta de circulación digital con otras personas** mediante un
**token** (`TokenCompartirTarjeta`) y/o un **QR** (`ScannQrActivity`), y gestionar/revocar la
lista de personas con quienes la compartió (`NotificacionRevocar`, `EliminarTarjetaContract`).

- **Superficie:** si el `TokenCompartirTarjeta` es predecible/enumerable o no caduca, un tercero
  podría acceder a tarjetas ajenas. **No verificable estáticamente** (la generación/validación del
  token vive en el backend). Se reporta como **superficie a revisar**, no como hallazgo confirmado
  (doc 06).

## Generación de PDF

`TcPdfProvider` genera un PDF de la tarjeta de circulación (de ahí los permisos de almacenamiento,
doc 01) para guardar/compartir.
