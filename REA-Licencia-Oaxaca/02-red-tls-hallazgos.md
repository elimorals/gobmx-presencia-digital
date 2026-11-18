# 02 · Red, TLS y hallazgos — Licencia Digital GobOax

## Red / endpoints

- Backend único observado: **`https://oaxaca.validatulicencia.mx/`** (+ `/aviso_privacidad`).
- Enlace informativo: `https://www.oaxaca.gob.mx/comunicacion/...licencias-de-conducir-digitales...`.
- Todo **HTTPS**. Sin endpoints `http://` ni dominios de prueba (`-qa`/`-dev`) en `libapp.so`.
- Vendor: **validatulicencia.mx** (producto de licencia digital de terceros; distinto de Cosmocolor).

## TLS / cleartext

- Manifest: **sin** `android:usesCleartextTraffic` y **sin** `networkSecurityConfig`.
- `targetSdk 33` → en Android ≥9 el cleartext está **bloqueado por defecto**. Postura correcta.

## Hallazgos

| # | Hallazgo | CWE | Estado |
|---|----------|-----|--------|
| H1 | TLS/cleartext correcto (sin cleartext, sin NSC, HTTPS) | — | **AUSENTE-SANO · CONFIRMADO** |
| H2 | Sin trust-all en el DEX (0 `getUnsafeOkHttpClient`/`checkServerTrusted`/`ServiceGenerator`) | CWE-295 | **AUSENTE-SANO · CONFIRMADO** |
| H3 | Validación TLS del cliente Dart (en `libapp.so` AOT) | CWE-295 | **A-CONFIRMAR** (muro Dart; requiere dinámico) |
| H4 | Sin secretos/`.env` embebidos; permisos mínimos | CWE-312 | **AUSENTE-SANO · CONFIRMADO** |

## Método / ética

Análisis **estático** del XAPK público (JADX/rea + aapt2 + strings sobre `libapp.so`). El APK está
**re-firmado por APKPure**, por lo que la firma **no** prueba autenticidad — cruzar con el canal
oficial del Gobierno de Oaxaca antes de cualquier aseveración de identidad. No se ejecutó la app ni
se tocó infraestructura.
