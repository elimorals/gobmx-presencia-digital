# 01 · Manifiesto — eSIT Móvil Hidalgo

Package `com.esitmovil` · v1.23 (23) · minSdk 24 / targetSdk 34.

## Permisos (16)

| Permiso | Nota |
|---------|------|
| INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE | red |
| CAMERA | escaneo de documentos/QR (VisionCamera) |
| RECORD_AUDIO | **amplio** para app tributaria — probable dependencia de VisionCamera, sin uso evidente |
| READ/WRITE_EXTERNAL_STORAGE | recibos/PDF (pdfium) |
| BLUETOOTH, BLUETOOTH_ADMIN, BLUETOOTH_ADVERTISE, BLUETOOTH_CONNECT, BLUETOOTH_SCAN | emparejamiento con terminal/lector (POS/impresora) |
| **DUMP** | 🚩 **permiso de firma/privilegiado** — no se otorga a apps normales, queda inerte; su presencia indica un manifiesto copiado/descuidado (CWE-250) |
| DOWNLOAD_WITHOUT_NOTIFICATION | descargas silenciosas |
| WAKE_LOCK, BIND_JOB_SERVICE | servicios/jobs |

No se hallaron deeplinks con esquema propio ni componentes exportados de riesgo evidentes en el
volcado del manifiesto (la lógica vive en el bundle Hermes).

## Red / cleartext

- **Sin** `android:usesCleartextTraffic`.
- **Sin** `networkSecurityConfig`.
- targetSdk 34 (≥28) → **cleartext bloqueado por defecto**. Correcto.
