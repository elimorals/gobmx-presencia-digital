# 04 · Hallazgos y cruce con el mapa

## Resumen de severidad

| # | Hallazgo | Clase | Severidad | Estado |
|---|----------|-------|-----------|--------|
| H1 | RFC + nómina + trámites de empleado por HTTP en claro | CWE-319 | **ALTA** | CONFIRMADO |
| H2 | targetSdk 22 habilita cleartext por defecto (sin NSC) | CWE-319 | habilitante | CONFIRMADO |
| H3 | Whitelist Cordova `origin="*"` | CWE-942 | MEDIA | CONFIRMADO |
| H4 | Trust-all / WebView SSL bypass | CWE-295 | — | AUSENTE-SANO |
| H5 | Secretos embebidos | CWE-798 | — | AUSENTE-SANO |
| H6 | `ENV` placeholder muerto `dev.yoursite.com` | — | INFO | CONFIRMADO |

## Cruce con `analisis/MAPA-PROVEEDORES-SDK.md`

- **Clúster D (cleartext):** suma a la lista. Patrón de **datos sensibles por HTTP** compartido con
  911-CDMX, Veracruz Mujer Alerta, 911Móvil BC. Aquí es **nómina/RFC de empleados** en vez de
  ubicación de emergencia.
- **Clúster A (trust-all): NO aplica** — sin `getUnsafeOkHttpClient`/`ServiceGenerator`/trust-all.
  Igual que el caso Veracruz Mujer Alerta (GeneXus), el defecto es de **despliegue del cliente**
  (endpoints sin TLS), no del framework.
- **Segundo gobierno Veracruz** en el corpus con cleartext confirmado (junto a Veracruz Mujer
  Alerta de SSP Veracruz). Aquí el responsable es la **SEV** (Secretaría de Educación de Veracruz),
  firma auténtica propia.

## Candidata a divulgación

Sí — dirigida a **SEV, Dirección de Tecnologías de la Información** (emisor de la firma). Severidad
ALTA: RFC y nómina de servidores públicos por HTTP. Remediación: migrar `sirhportal.sev.gob.mx` e
`innova.sev.gob.mx:8094` a HTTPS, subir targetSdk, cerrar la whitelist a `*.sev.gob.mx`. Sin
secretos que rotar.

## Límites

Análisis estático de artefacto público. El bundle AngularJS es legible, así que los endpoints y el
flujo de red son ciertos; lo que no se verifica estáticamente es el comportamiento del backend
(p. ej. si `sirhportal` redirige a HTTPS en runtime — no observado en el cliente, que arma URLs
`http://` explícitas). Almacenamiento local y `allowBackup` quedaron a nivel superficie.
