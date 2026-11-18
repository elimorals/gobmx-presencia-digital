# Informe de divulgación responsable — Veracruz Mujer Alerta

**Para:** Secretaría de Seguridad Pública de Veracruz (SSP Veracruz) — área de tecnología.
(Proveedor del desarrollo: **FielNet/Artech-GeneXus**, firmante `O=Artech`.)
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **CRÍTICA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días. Por tratarse de una app de
**protección a víctimas de violencia de género**, se ofrece priorizar la coordinación.

## Resumen ejecutivo

La app oficial **Veracruz Mujer Alerta** envía la **alerta de auxilio y la ubicación en tiempo real
de una mujer en riesgo por HTTP en claro (sin TLS)**. Un atacante de red (MITM) puede **leer,
alterar o suprimir** una alerta de violencia de género — un riesgo directo a la seguridad física de
la víctima.

## App afectada

- **Nombre:** Veracruz Mujer Alerta
- **Package:** `com.artech.nuevoalerta.alertamujer`
- **Pila:** GeneXus (`com.artech.*`), backend REST. Firma `O=Artech` (vendor, no gobierno de Veracruz).
- SHA-256: ver `hashes.sha256`.

## Detalle técnico

- **CWE-319: Cleartext Transmission of Sensitive Information.**
- La alerta y la ubicación viajan a **`http://alertagenero.sspver.gob.mx/alerta/`** (sin cifrar).
  El `network_security_config` (`res/8G.xml`) **permite explícitamente cleartext** para ese host.
- **CWE-359 (exposición de PII sensible):** ubicación en tiempo real + contactos + cámara de la
  víctima sobre el canal inseguro.
- **Matiz de causa (honesto):** el runtime GeneXus **no** desactiva la validación TLS (no es
  trust-all). El defecto es de **despliegue de la SSP** (endpoint sin HTTPS + NSC que lo permite),
  no del framework.

## Impacto

- Un MITM en la ruta de red (WiFi público, red móvil comprometida) puede leer la ubicación de la
  víctima, o **alterar/suprimir la alerta** para que no llegue a la autoridad — riesgo a la vida.

## Alcance y método (ética)

Análisis **estático** de un APK público de tienda. **No** se disparó ninguna alerta real (sería una
falsa alarma a la SSP), no se interceptó tráfico real y no se usó dato de ninguna persona.

## Remediación recomendada

1. Migrar `alertagenero.sspver.gob.mx` a **HTTPS** y retirar el cleartext del `network_security_config`.
2. Añadir **certificate pinning** a los dominios de la SSP.
3. Revisar que toda la cadena (alerta, ubicación, multimedia) use el canal cifrado.

## Divulgación

Ruta: SSP Veracruz, con copia al proveedor (FielNet/Artech). Por el perfil de la app se recomienda
corrección prioritaria. Acuse solicitado; plazo sugerido 90 días.
