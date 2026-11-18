# 00 · Resumen ejecutivo — Veracruz Mujer Alerta

## Identidad

| Campo | Valor |
|---|---|
| App | Veracruz Mujer Alerta (alerta de violencia de género) |
| Package | `com.artech.nuevoalerta.alertamujer` |
| versionName / versionCode | 1.83 / 183 |
| minSdk / targetSdk | 24 / 35 |
| Firma | `CN=Artech Genexus, OU=Artech Consultores, O=Artech, L=Montevideo, C=uy` · SHA256withRSA |
| SHA-256 cert | `6d33be3196ae50c7bc8d391b3375a9982f0fb580e92e20fcb66355b41b3a3fac` |
| sha256 APK | `6f82f6138700504b4272063dc9ed65c24ad9d3ebae12cc3df860fed572134477` |
| Clases / recursos | 15 257 / 1 291 |
| Dueño funcional | SSP Veracruz (Secretaría de Seguridad Pública) — backend `sspver.gob.mx` |

Package **verificado con `inspect_android_package` antes de documentar** (coincide).

## Proveniencia de la firma (matiz importante)

La app NO está firmada por el gobierno de Veracruz, sino por **`O=Artech` (Montevideo, Uruguay)** —
el fabricante de **GeneXus**. Es la firma del *tooling* del proveedor (posiblemente el certificado
por defecto de la KB de GeneXus), no de la dependencia. La autenticidad del vínculo app↔SSP Veracruz
se apoya en el backend (`alertagenero.sspver.gob.mx`), no en la firma.

## Pila tecnológica

- **GeneXus** (namespace `com.artech.*`), igual que **SEG-Móvil** (`REA-SEG-Movil`). App generada por
  la KB de GeneXus; capa de red del runtime GeneXus (Retrofit/OkHttp bajo `com.artech`/`com.genexus`)
  consumiendo un **backend REST GeneXus** en `/alerta/`.
- Push vía FCM/C2DM. Sin Flutter, sin .NET, sin React Native.

## Función

App de **pánico/alerta de violencia de género**: botón de auxilio que envía la **alerta + ubicación**
a la SSP Veracruz. Permisos: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `READ_CONTACTS`,
`WRITE_CONTACTS`, `CAMERA`, `FOREGROUND_SERVICE`, `RECEIVE_BOOT_COMPLETED`, `INTERNET`.

## Hallazgos (tabla)

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | **Backend de la alerta en cleartext HTTP** (`http://alertagenero.sspver.gob.mx/alerta/`); NSC permite cleartext para ese host exacto | CWE-319 | **CRÍTICA** | **CONFIRMADO** |
| H2 | Trust-all TLS (patrón Infonavit/Cosmocolor) | CWE-295 | — | **AUSENTE-SANO** (no se halló; las strings X509 son internas de OkHttp) |
| H3 | PII muy sensible (ubicación en tiempo real + contactos + cámara) sobre canal inseguro | CWE-359 | **ALTA** | CONFIRMADO (deriva de H1) |
| H4 | Firma del vendor (Artech/Uruguay), no de la dependencia | — | INFO | CONFIRMADO |

## Conclusión del cruce de proveedor (GeneXus)

**El defecto NO es del producto GeneXus.** SEG-Móvil (GeneXus) usa HTTPS + Azure B2C; esta app usa
**cleartext a nivel de despliegue** (backend HTTP + NSC que lo permite). Es decir: GeneXus no impone
trust-all ni cleartext; el fallo aquí es **de configuración del despliegue** de SSP Veracruz, de la
misma **clase** que 911-CDMX (CWE-319), con otro proveedor. No se confirma un defecto GeneXus
compartido con SEG-Móvil.
