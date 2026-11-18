# Informe de divulgación responsable — 911 Móvil BC

**Para:** Centro de Control, Comando, Comunicación y Cómputo (C4) de Baja California / Secretaría de
Seguridad Ciudadana de BC — área de tecnología (firmante del APK, `O=C4, L=Mexicali, ST=Baja California`).
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **ALTA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días. Por ser app de emergencias se
ofrece priorizar la coordinación.

## Resumen ejecutivo

La app oficial **911 Móvil BC** permite **tráfico HTTP en claro (sin TLS)** hacia cuatro dominios de
backend de emergencias. Un atacante de red puede leer o alterar la comunicación de una llamada de
auxilio. Es la misma clase de defecto que la app 911 CDMX, aunque más acotada (vía
`network_security_config`, no cleartext global).

## App afectada

- **Nombre:** 911 Móvil BC
- **Package:** `com.c4bc.alerta066m`
- **Pila:** React Native (Hermes). Firma **auténtica del C4 de Baja California** (no re-firmada).
  SHA-256: ver `hashes.sha256`.

## Detalle técnico

- **CWE-319: Cleartext Transmission of Sensitive Information.**
  - El `network_security_config` (`res/8G.xml`) declara `cleartextTrafficPermitted="true"` para 4
    dominios backend: `066movilbc.seguridadbc.gob.mx`, `911bc.gob.mx`, `www.seguridadbc.gob.mx` y
    `tijuana.c4bcdev.gob.mx`.
- **CWE-489 (dev en producción):** el subdominio de **desarrollo** `tijuana.c4bcdev.gob.mx` va
  empacado y whitelisteado para cleartext en el build público.
- **(Positivo) AUSENTE-SANO:** sin trust-all (no es el caso Infonavit); sin trust de CAs de usuario.
- **A-CONFIRMAR (muro Hermes):** los cuerpos exactos de las peticiones y la ausencia total de
  secretos no son verificables estáticamente (bytecode Hermes); no se afirma más de lo observado.

## Impacto

- MITM sobre una app de **emergencias**: lectura o alteración del tráfico de una alerta/llamada de
  auxilio por los dominios permitidos en claro.

## Alcance y método (ética)

Análisis **estático** del APK público; **no** se registró ningún incidente ni se contactó al 911.

## Remediación recomendada

1. Migrar los 4 dominios a **HTTPS** y retirar el `cleartextTrafficPermitted`.
2. Retirar el subdominio de desarrollo (`c4bcdev`) del build de producción.
3. Añadir certificate pinning a los dominios del C4 BC.

## Divulgación

Acuse solicitado; plazo sugerido 90 días. Por tratarse de emergencias, se ofrece priorizar.
