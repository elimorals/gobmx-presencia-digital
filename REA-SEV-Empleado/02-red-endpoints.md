# 02 · Red y endpoints

Toda la red la hace AngularJS `$http` desde `assets/www/scripts/*.js` (legible). Dos planos
claramente separados por protocolo:

## Plano de IDENTIDAD — HTTPS (correcto)

`https://perfil-r1.sev.gob.mx/...`

| Endpoint | Función |
|---|---|
| `/Usuario/Autenticar` | login |
| `/Usuario/Registrar` · `/Usuario/Activar` | alta de Perfil Móvil |
| `/Usuario/Actualizar/Password` | cambio de contraseña (`{User, Pass}`) |
| `/Usuario/Codigo/Recuperar` · `/Usuario/Existe` | recuperación |
| `/Credencial/Obtener` · `/Credencial/Verificar/RECHUM` | credencial |
| `https://mailer-s1.sev.gob.mx/CodigoVerificacion` | OTP de verificación |

Las credenciales y el OTP viajan cifrados. Bien.

## Plano de DATOS — HTTP EN CLARO (defecto H1)

| Endpoint (http://) | Dato que transporta | Param |
|---|---|---|
| `sirhportal.sev.gob.mx/api/ServiciosAdministrativos.svc/SeguimientoTramite` | estatus de trámite | `{rfc, Folio}` |
| `sirhportal.sev.gob.mx/api/serviciosAdministrativos.svc/DatosEmpleado` | datos del empleado | rfc |
| `innova.sev.gob.mx:8094/api/json.aspx` | **comprobante de pago / nómina** (percepciones/deducciones) | rfc |
| `innova.sev.gob.mx:8094/api/jsonQnaAct.aspx` | quincenas disponibles | — |
| `sirhportal.sev.gob.mx/ImpReportes2.aspx` · `WfImpresionDeConstancias.aspx` | constancias/reportes (PDF) | — |
| `innova.sev.gob.mx:8094/Cheque_Estatal_Movil.aspx` · `Cheque_Federal_Movil.aspx` | recibo de cheque | — |

**El identificador es el RFC del empleado** (`$localStorage.Perfil.Credenciales[0].RFC`), que se
envía en el cuerpo de las peticiones HTTP. El RFC es a la vez identificador y PII fiscal; la
respuesta de `json.aspx` es la nómina (salario). Todo esto viaja sin cifrar.

## Config muerta

`scripts/configuration.js` declara `.constant('ENV', {name:'development', apiEndpoint:'http://dev.yoursite.com:10000/'})`
— es el **placeholder por defecto del generador de Ionic**, nunca reemplazado. No se usa (los
endpoints reales están hardcodeados en `services.js`). Higiene (H6), sin impacto directo.
