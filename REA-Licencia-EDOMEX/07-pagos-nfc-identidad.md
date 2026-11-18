# Pagos, NFC e identidad

## Pago con tarjeta dentro de la app

La app captura datos de tarjeta **in-app** (no delega a una pasarela web/SDK PCI visible). Vistas
dedicadas en `views.card`:

- `CardNumberEditText`, `CardNumberTransformation`, `CvvEditText`, `CardholderNameEditText`,
  `CardExpirationDateEditText`, `CardType`.

Modelos: `models.Payment`, `PaymentData`, `PaymentRequest`, `PaymentResponse`,
`ValidatePaymentResponse`. Flujo: `ui.licenses.interactor.PaymentInteractorImpl`, que **usa
`ServiceGenerator.createService`** — la ruta **trust-all** (doc 04).

**Consecuencia:** número de tarjeta, CVV y titular se envían por un canal cuya validación TLS
está desactivada → **datos de tarjeta interceptables/alterables por MITM**. Riesgo PCI serio
(doc 09, hallazgo P2). El **procesador de pagos** concreto no se identificó en el binario (no hay
SDK de pasarela evidente; la petición va a `api2.licenciamanejo.com`) → se marca como límite, no
se inventa el proveedor.

## NFC: lectura de la licencia física

- `ui.enrollment.nfcreader.NfcLicenseReader`, `ui.enrollment.EnrollmentNfcActivity`,
  `databinding.ActivityNfcBinding`, permiso `NFC`.
- Lee el chip de la licencia/credencial física durante el enrolamiento para precargar datos.

## Identidad: Regula + Face API

- **Regula** (`utils.RegulaUtils`): SDK de lectura/validación de documento de identidad
  (OCR/MRZ de la licencia).
- **Face API** (`cameralibrary.facedetection.interactors.FaceApiInteractorImpl`,
  `ui.enrollment.FaceApiFragment`): verificación facial contra `https://faceapi.licenciamanejo.com/`
  — por la ruta trust-all del `cameralibrary` (doc 04).
- `models.IdentificationData`, `ValidateEnrollmentRequest/Response` → datos de identidad del
  enrolamiento.

## OTP por SMS

- `ui.enrollment.SendSmsFragment`, `SmsVerificationFragment`, `SmsVerificationSuccessFragment`,
  `databinding.FragmentSmsVerificationBinding`, `views.OtpEditText`.
- `ui.licenses.interactor.PhoneVerificationInteractorImpl` + `models.PhoneVerificationResponse`
  → verificación de teléfono por código, también sobre la ruta trust-all.

## PII en juego

CURP/RFC, nombre, dirección (`models.Address`, `PersonaLicencia`, `User`), foto, firma,
restricciones físicas (`PhysicalRestriction`), datos de la licencia (`Elicense`,
`DataLicenseResponse`), infracciones (`InfraccionDataResponse`) y **datos de tarjeta**. La DB
local los protege con SQLCipher (doc 05); el transporte no (doc 04).
