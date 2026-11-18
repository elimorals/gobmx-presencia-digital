# 06 · Hallazgos (consolidado)

| # | Hallazgo | CWE | Severidad | Estado | Evidencia |
|---|----------|-----|-----------|--------|-----------|
| H1 | Alerta de violencia de género + ubicación enviadas por **HTTP en claro** a `http://alertagenero.sspver.gob.mx/alerta/`; NSC `res/8G.xml` permite cleartext para ese host | CWE-319 | **CRÍTICA** | **CONFIRMADO** | string en DEX + NSC decodificado |
| H2 | Trust-all / desactivación de validación TLS | CWE-295 | — | **AUSENTE-SANO** | solo strings internas de OkHttp; sin `getUnsafeOkHttpClient`/`checkServerTrusted` vacío |
| H3 | PII extrema (ubicación tiempo real + contactos + cámara de víctima) sobre canal inseguro | CWE-359 | **ALTA** | CONFIRMADO | deriva de H1 + permisos |
| H4 | APK firmada por el vendor `O=Artech` (Uruguay), no por SSP Veracruz | — | INFO | CONFIRMADO | apksigner |
| H5 | Almacenamiento local (token/sesión GeneXus, `allowBackup`) | CWE-312 | — | **NO AUDITADO** | fuera de alcance de este pase; no se afirma sin evidencia |

## Severidad global: CRÍTICA

Por H1: una app cuya función es proteger a mujeres ante violencia de género transmite la alerta y la
ubicación sin cifrar. El riesgo no es solo de datos, es de **seguridad física** de la usuaria.

## Para divulgación

Dirigir a **SSP Veracruz** (dueño del backend `sspver.gob.mx`), con copia al proveedor
(Artech/GeneXus integrador local). Mismo patrón que el informe 04 (911-CDMX): CWE-319 en app de
emergencia/seguridad. Candidata a sumarse al paquete `divulgacion/`.

## Límites de este pase

- Backend solo observado estáticamente (string del endpoint); no se tocó el servidor vivo.
- Almacenamiento local no auditado a fondo (H5).
- El cuerpo exacto del request de la alerta (campos) vive en la lógica GeneXus; no se decompiló al
  detalle de campo-por-campo en este pase.
