# 07 · Permisos y superficie de datos

La app la usan beneficiarios de programas sociales (pensiones, becas); la superficie de datos
merece atención. Todo desde el manifiesto (estático); el **uso** real de cada permiso no se verificó
en runtime (lógica en Veritran).

## Datos personales / dispositivo

| Permiso | Dato | Comentario |
|---|---|---|
| `READ_CONTACTS` | Agenda completa | El más sensible. Plausible para transferencias/DiMo por teléfono, pero da acceso a toda la libreta. **Uso no confirmado**; conviene validar con dinámica. |
| `READ_PHONE_STATE`, `READ_BASIC_PHONE_STATE` | Estado/identidad de SIM y teléfono | Fingerprinting de dispositivo / anti-fraude |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación precisa | Uso no determinado (anti-fraude o cajeros/sucursales) |
| `CAMERA` | Cámara | Escaneo (ML Kit) / captura de documentos |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | Biometría | Autenticación local |
| `AD_ID`, `ACCESS_ADSERVICES_*` | Advertising-ID | Atribución/analítica (no hay publicidad, doc 06) |

## Telemetría / terceros

- **Firebase Analytics + Crashlytics**, **GMS Measurement** (`app-measurement.com`), **Advertising-ID**.
  Flujo de telemetría hacia infraestructura de Google, habitual pero relevante en una app de Estado:
  metadatos de uso de beneficiarios salen a un tercero.
- Monitoreo propio del runtime: `net.veritran.monitor`.

## Observaciones de privacidad (sin alarmismo, con evidencia)

1. `READ_CONTACTS` + `READ_PHONE_STATE` + ubicación + Advertising-ID es un perfil amplio para una
   app cuyo núcleo es consulta de saldo/transferencias de programas sociales. Que exista el permiso
   **no** prueba abuso; sí amplía la superficie y el deber de justificarlo en el aviso de privacidad.
2. La telemetría a Google (Analytics/Crashlytics/measurement) implica compartición de datos de uso
   con un tercero; verificar que el aviso de privacidad de Banco del Bienestar lo declare.

## Límite honesto

El mapeo permiso→uso real no se puede cerrar en estático porque la lógica vive en Veritran/`.szip`.
Lo anterior es superficie declarada, no comportamiento observado.
