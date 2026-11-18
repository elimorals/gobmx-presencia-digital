# Informe de divulgación responsable — SEV Empleado (Veracruz)

**Para:** Secretaría de Educación de Veracruz (SEV) — Dirección de Tecnologías de la Información
(firmante del APK, `O=Secretaria de Educacion de Veracruz`).
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **ALTA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

La app oficial **SEV Empleado** transmite los **datos del empleado —RFC, comprobantes de pago
(nómina), estatus de trámites y constancias— por HTTP en claro (sin TLS)**. Un atacante de red
puede leer el salario y el RFC de los servidores públicos de la SEV. La autenticación (login) sí va
por HTTPS; el problema es la capa de **datos** tras el login.

## App afectada

- **Nombre:** SEV Empleado
- **Package:** `mx.gob.sev.empleados`
- **Pila:** Cordova/Ionic 1 (bundle JS legible). Firma **auténtica** de la SEV. SHA-256: ver `hashes.sha256`.

## Detalle técnico

- **CWE-319: Cleartext Transmission of Sensitive Information.**
  - Datos del empleado por **`http://sirhportal.sev.gob.mx`** e **`http://innova.sev.gob.mx:8094`**.
    El RFC viaja en el cuerpo de la petición (`{"rfc": ...}`).
  - En contraste, la identidad (login/contraseña/OTP) **sí** va por HTTPS a `perfil-r1.sev.gob.mx`.
    El split es: **auth cifrada, datos del empleado en claro**.
- **Habilitante:** `targetSdk 22` → cleartext permitido por defecto, sin `network_security_config`.
  (Por eso sigue en SDK 22: subir a ≥28 rompería las llamadas HTTP.)
- **CWE-942:** whitelist de Cordova `<access origin="*">` abierta a cualquier host.
- **(Positivo) AUSENTE-SANO:** sin trust-all / bypass de SSL en el WebView; sin secretos embebidos.
  El defecto es de **despliegue** (endpoints sin TLS), no del framework.

## Impacto

- MITM lee el **RFC y el comprobante de nómina** (salario) de un empleado de la SEV; puede alterar
  la respuesta (estatus de trámites, constancias).

## Alcance y método (ética)

Análisis **estático** del APK público; en el pase dinámico **no** se usaron credenciales reales (el
flujo de datos sensibles es post-login y quedó sin ejecutar por ética). El hallazgo es propiedad del
código y del `targetSdk`/endpoints `http://` embebidos.

## Remediación recomendada

1. Migrar `sirhportal.sev.gob.mx` e `innova.sev.gob.mx` a **HTTPS**; subir `targetSdk` a ≥34.
2. Cerrar la whitelist Cordova `*` a los dominios propios.
3. Añadir certificate pinning a los dominios de la SEV.

## Divulgación

Acuse solicitado; plazo sugerido 90 días. (La SEV es el **2º despliegue de gobierno de Veracruz**
con cleartext confirmado en esta muestra, junto a la app de la SSP — conviene una revisión de la
política estatal de TLS.)
