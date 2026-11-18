# 03 · TLS, cleartext y almacenamiento

## H1 · Datos de empleado por HTTP en claro — CWE-319 · ALTA · CONFIRMADO

- La app consulta **comprobantes de pago (nómina), datos del empleado, estatus de trámites y
  constancias** por `http://` (ver doc 02), enviando el **RFC** del trabajador en el cuerpo.
- **Impacto:** un atacante en la misma red (Wi-Fi institucional, hotspot, ISP, MITM) puede leer el
  RFC y el detalle de la nómina de cualquier empleado que use la app, y alterar las respuestas
  (p. ej. falsear un estatus de trámite o una constancia). Es PII fiscal + dato salarial de
  servidores públicos.
- **Severidad ALTA** (no crítica): expone PII/finanzas, no vida; y la autenticación en sí va por
  HTTPS, así que no se interceptan credenciales de login por este canal.

## H2 · Cleartext permitido por defecto (targetSdk 22) — CONFIRMADO

- `targetSdk 22` ⇒ Android permite tráfico HTTP sin `usesCleartextTraffic` ni NSC. No hay
  `network_security_config` en el APK. Es el habilitante de H1. Subir targetSdk a ≥28 **rompería**
  las llamadas HTTP (fallarían), forzando la migración a HTTPS — por eso sigue en 22.

## H3 · Whitelist Cordova abierta — CWE-942 · MEDIA · CONFIRMADO

- `config.xml`: `<access origin="*">`. La app Cordova puede hacer peticiones a **cualquier** host.
  Amplía la superficie (p. ej. si una vista carga contenido remoto). Debería restringirse a los
  dominios `*.sev.gob.mx`.

## H4 · Trust-all TLS / WebView SSL bypass — CWE-295 · AUSENTE-SANO

- Barrido del `classes.dex`: **no** hay `onReceivedSslError`/`SslErrorHandler.proceed`,
  **no** hay `X509TrustManager`/`checkServerTrusted` vacío, **no** hay `getUnsafeOkHttpClient`.
- El WebView usa el `WhitelistPlugin` estándar de Cordova sin sobreescribir validación TLS.
- **No** es el caso Infonavit/Cosmocolor: donde usa HTTPS (identidad), la validación es la del
  sistema. El problema es que no usa HTTPS para los datos, no que lo desactive.

## H5 · Secretos embebidos — CWE-798 · AUSENTE-SANO

- No se hallaron API keys, tokens ni credenciales hardcodeadas en los `scripts/*.js` propios. La
  autenticación es por usuario/contraseña que teclea el empleado (`$scope.data.email/password` →
  `/Usuario/Autenticar`), sin llave de app embebida.

## Almacenamiento local

- Usa `$localStorage` (Ionic) para el `Perfil` y las `Credenciales[0].RFC`. Es `localStorage` del
  WebView (sin cifrar), pero vive en el sandbox de la app; `allowBackup` conviene revisarlo (no
  evaluado a fondo — segundo pase si interesa). Los PDFs de comprobantes se guardan en
  almacenamiento externo (permiso `WRITE_EXTERNAL_STORAGE`), legibles por otras apps en Android
  antiguo.
