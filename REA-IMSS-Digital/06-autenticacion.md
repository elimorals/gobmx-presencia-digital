# Autenticación y sesión

## Mecanismos presentes

| Mecanismo | Evidencia |
|---|---|
| Login usuario/contraseña | `…/api/auth/login` (siap, **HTTP**), `mschkt-login/v1/login` |
| OAuth2 token | `bmt.imss.gob.mx/.../authorization-server/oauth/token` |
| OTP por **SMS** | `bmt.imss.gob.mx/msotp-registro/v1/registro/[valida/]sms/` |
| Enrolamiento biométrico | `bmt.imss.gob.mx/msbmt-enroll/v1/…/media/selfie` |
| Biometría local | `Plugins.Fingerprint.dll`, `USE_BIOMETRIC`/`USE_FINGERPRINT` |

## Flujo inferido

```
Alta / primer acceso
  ├─ KYC (Mati + FaceTec liveness + Acuant documentos)  [doc 04]
  ├─ Enrolamiento biométrico IMSS (msbmt-enroll, selfie)
  └─ OTP por SMS (msotp-registro)  → vincula teléfono

Login recurrente
  ├─ usuario/contraseña  → http://siap.imss.gob.mx/.../api/auth/login   ⚠ sin TLS
  │     o  → bmt.imss.gob.mx/.../oauth/token (OAuth2)
  ├─ desbloqueo con huella/rostro local (Plugin.Fingerprint)
  └─ token → llamadas a microservicios de salud (HTTPS)  [doc 05]
```

## Punto débil

El `token` que autoriza el expediente clínico se obtiene, en al menos una ruta, por **HTTP sin
cifrar** (`http://siap.imss.gob.mx/.../api/auth/login`). Aunque los microservicios de datos van
por HTTPS, un atacante en la misma red (Wi-Fi pública) podría capturar credenciales o el token
emitido por el endpoint HTTP y reutilizarlo contra los servicios HTTPS. El doble canal
(`bmt…/oauth/token` por HTTPS vs. `siap…/auth/login` por HTTP) sugiere migración incompleta a
OAuth/TLS. Ver doc 08.

## OTP y biometría como 2FA

El enrolamiento combina **OTP SMS** + **selfie/liveness**, lo que da un 2FA razonable en el
alta. La debilidad no está en los factores sino en el **transporte** del login recurrente.

> Límite: no se decompiló el IL C# para confirmar el tipo exacto de token (JWT vs opaco) ni la
> política de expiración; se infiere de las rutas (`oauth/token`, `validation`). Continuable con
> ILSpy sobre `IMSSDigital.dll` (doc 02).
