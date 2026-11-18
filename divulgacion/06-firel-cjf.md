# Informe de divulgación responsable — FiREL (CJF / FielNet-IQSEC)

**Para (primario):** **FielNet / IQSEC** (proveedor del producto; el package real es
`com.fielnet.notificaciones_colegio_notarios_mvvm`).
**Para (copia / afectado):** Consejo de la Judicatura Federal (CJF) — Dirección General de
Tecnologías de la Información del PJF.
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **ALTA — A CONFIRMAR** (postura TLS contradictoria; requiere confirmación del proveedor).
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

La app oficial de **Firma Electrónica del Poder Judicial de la Federación (FiREL)** es en realidad
un **white-label** de una app de "Colegio de Notarios" del proveedor **FielNet/IQSEC**, cuyo
backend está **fuera de `gob.mx`** (`iqsec-masivos.com.mx`). Realiza **enrolamiento biométrico
extremo** (lectura de INE, rostro con liveness, **huellas sin contacto**, firma autógrafa y video
de verificación) que se envía a **terceros**, uno de ellos **extranjero** (ShuftiPro, KYC;
biometría de Identy.io). En el binario **coexisten** una configuración de certificate pinning
(`okhttp3.CertificatePinner`) **y** un patrón de **desactivación de validación TLS**
(`getUnsafeOkHttpClient` con `X509TrustManager`/`sslSocketFactory`/`hostnameVerifier` permisivos).
No fue posible confirmar estáticamente cuál ruta usa el cliente inseguro — se reporta como **bandera
alta a confirmar**, no como MITM demostrado.

## App afectada

- **Nombre:** FiREL (Firma Electrónica PJF)
- **Package:** `gob.mx.android.firel` (código interno: `com.fielnet.notificaciones_colegio_notarios_mvvm`).
- **Pila:** nativa Kotlin/MVVM (Volley + SOAP). Backend: `iqsec-masivos.com.mx`.
- **Firma del acervo:** re-firmada por el redistribuidor (recomendable validar contra el canal oficial).

## Detalle técnico

- **CWE-295 (a confirmar):** coexisten en el bytecode `okhttp3.CertificatePinner` (pinning) y una
  utilidad `getUnsafeOkHttpClient` con `checkServerTrusted`/`hostnameVerifier` permisivos. No se
  pudo atar por xref el cliente inseguro a una ruta de producción (ofuscación R8 + posible librería)
  → **debe confirmarlo el proveedor**: ¿qué cliente usa cada endpoint?
- **CWE-359 (exposición de PII sensible):** enrolamiento biométrico (INE, rostro+liveness, huellas,
  firma, video, ubicación) enviado a múltiples terceros, incluido uno **extranjero** (ShuftiPro).
- Artefactos de higiene: `tempuri.org` (SOAP sin configurar) y dependencia de pruebas (JUnit)
  empaquetada en el release.

## Impacto

- Si alguna ruta sensible usa el cliente inseguro, un MITM podría interceptar/alterar datos
  biométricos y de identidad de usuarios del PJF (abogados, notarios, ciudadanos).
- Transferencia de biometría a terceros (uno extranjero): implicaciones de protección de datos
  (LFPDPPP / avisos de privacidad) a revisar por el CJF.

## Remediación recomendada

1. **Confirmar y, si aplica, eliminar** cualquier `getUnsafeOkHttpClient`/trust-all del release.
2. Asegurar que **todas** las rutas (biometría/KYC/firma) usen el cliente con pinning.
3. Revisar la cadena de terceros (ShuftiPro, Identy.io) y el aviso de privacidad del PJF.
4. Retirar `tempuri.org` y dependencias de prueba del build de producción.

## Divulgación

Ruta primaria: proveedor (FielNet/IQSEC), con copia al CJF. Plazo sugerido 90 días. Se ofrece
coordinar la confirmación del punto TLS (es el dato que decide si la severidad sube a crítica).
