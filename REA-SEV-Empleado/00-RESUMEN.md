# 00 · Resumen ejecutivo — SEV Empleado (Veracruz)

## Identidad

| Campo | Valor |
|---|---|
| App | Empleados SEV (nómina/trámites de empleados) |
| Package | `mx.gob.sev.empleados` |
| versionName / versionCode | 1.2 / 102008 |
| minSdk / targetSdk | **14 / 22** (Android 5.1 — muy antiguo) |
| Firma | **Auténtica** · `CN=Direccion de Tecnologias de la Informacion, O=Secretaria de Educacion de Veracruz, L=Xalapa, ST=Veracruz, C=MX` |
| SHA-256 cert | `38:27:32:76:…:6d:6b:fc:b1` |
| sha256 APK | `2666a6f5eaf99821137489b6c7b0c2c3f5bcdaa86c8fc26c9a8b664b564fcc47` |
| Clases / recursos | 63 / 588 |
| Dueño | Secretaría de Educación de Veracruz (SEV) — Oficialía Mayor |

Package **verificado con `inspect_android_package`** antes de documentar: coincide.

## Pila tecnológica

- **Cordova + Ionic 1 (AngularJS 1.x)** — shell nativo delgado (63 clases, 1 `classes.dex`) que
  carga una app web local (`assets/www/index.html`). Build de **2017** (bower_components con
  timestamps 03-10-2017).
- Plugins Cordova: `whitelist`, `InAppBrowser`, `FileTransfer`, `FileOpener2`, `Device`,
  `AppVersion`, `SplashScreen`, `com.ionic.keyboard`.
- **Sin** capa nativa de red propia: toda la red la hace AngularJS `$http` desde el bundle JS
  (legible, no minificado — los `scripts/*.js` propios están en claro).
- Sin ofuscación, sin packer, sin librerías nativas propias.

## Mapa de módulos (features)

`registro` (Perfil Móvil), `login/autenticar`, `password` (reasignar), `perfil`,
`comprobantes` (nómina quincenal: percepciones/deducciones), `seguimiento_tramites`,
`constancia_trabajo`, `configuracion`, `acercade`. Detalle en
[02-red-endpoints.md](02-red-endpoints.md).

## Hallazgos (tabla)

| # | Hallazgo | Clase | Severidad | Estado |
|---|----------|-------|-----------|--------|
| H1 | **Datos de empleado (RFC + nómina + trámites) por HTTP en claro** | CWE-319 | **ALTA** | **CONFIRMADO** |
| H2 | targetSdk 22 → cleartext permitido por defecto, sin NSC | CWE-319 | (habilitante de H1) | CONFIRMADO |
| H3 | Whitelist Cordova `<access origin="*">` (abierta a todo) | CWE-942 | MEDIA | CONFIRMADO |
| H4 | Trust-all TLS / WebView SSL bypass | CWE-295 | — | **AUSENTE-SANO** |
| H5 | Secretos/API keys embebidos | CWE-798 | — | **AUSENTE-SANO** |
| H6 | `ENV.apiEndpoint` = placeholder `http://dev.yoursite.com:10000/` (muerto) | — | INFO | CONFIRMADO |

## Lo esencial

La identidad va bien (login, contraseña, OTP y credencial sobre **HTTPS** a
`perfil-r1.sev.gob.mx` / `mailer-s1.sev.gob.mx`). El problema es que **los datos sensibles del
empleado** —RFC, comprobantes de pago (nómina), estatus de trámites, constancias— viajan por
**HTTP en claro** a `sirhportal.sev.gob.mx` e `innova.sev.gob.mx:8094`. Un atacante de red lee el
RFC y la nómina del trabajador. No hay trust-all ni secretos embebidos: el defecto es de
**despliegue** (endpoints sin TLS), no del framework.
