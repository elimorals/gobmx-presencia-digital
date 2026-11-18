# 03 · Hallazgos

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | **Postura de red correcta** — API 100% HTTPS a `alertac.c4ver.gob.mx`, sin cleartext (targetSdk 36, sin NSC), sin trust-all propio | — | ✅ | AUSENTE-SANO (verificado) |
| H2 | **Apache HttpClient deprecado** empacado (269 refs `org.apache.http.impl`); incluye `AllowAllHostnameVerifier` de la lib (inerte, sin uso hallado) | CWE-1104 (componente obsoleto) | ⚪ BAJA | CONFIRMADO (presencia) / A-CONFIRMAR (uso) |
| H3 | Firma de **desarrollador individual** (`CN=Adair de Jesus Castillo Meza`), no entidad de gobierno | — | ⚪ INFO | CONFIRMADO |

## Conclusión

**Contraejemplo limpio.** App de alerta/emergencia ciudadana con red bien construida: HTTPS exclusivo,
sin cleartext y sin desactivación de TLS propia. **No** repite el defecto de las otras apps de Veracruz
(SSP Mujer Alerta, SEV Empleado), ni el de 911-CDMX.

**Pendiente (requeriría dinámico):** confirmar que el cliente Apache HttpClient no se configura con
`ALLOW_ALL_HOSTNAME_VERIFIER` en runtime (la librería lo trae, pero no se halló wiring en el código de
la app). Única salvedad de la clasificación AUSENTE-SANO.

## No candidata a divulgación

Sin hallazgo de severidad media o superior.
