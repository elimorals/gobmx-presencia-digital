# Mapa de proveedores, SDKs y defectos compartidos — 45 apps gob.mx

**Fecha:** 2026-11-18
**Método:** cruce estático de los 45 artefactos ya documentados en `REA-*/` (manifiestos,
decompilado, docs). Cero descargas nuevas, cero tráfico contra sistemas vivos.
**Objetivo:** convertir 45 análisis sueltos en hallazgos *sistémicos* — "una falla, N
dependencias afectadas" — que un análisis app-por-app no ve.

> **Nota de rigor.** Cada clúster marca nivel de evidencia: **CONFIRMADO** (atado a código/manifest
> verificado), **A CONFIRMAR** (señal presente pero no amarrada a una ruta de producción), o
> **AUSENTE/SANO** (se revisó explícitamente y no está). No se reporta una causa que la evidencia no
> identifique.

---

## Resumen de clústeres

| # | Clúster | Clase | Evidencia | Apps |
|---|---------|-------|-----------|------|
| A | Antipatrón TLS trust-all (`getUnsafeOkHttpClient`/`ServiceGenerator`) | CWE-295 | CONFIRMADO ×3 orígenes + 1 a confirmar | 4 |
| B | Producto Cosmocolor multi-estado | CWE-295 sistémico | CONFIRMADO 2 apps + integración Chiapas | 2 |
| C | Backends compartidos entre apps distintas | — | CONFIRMADO | 4 apps / 2 backends |
| D | Cleartext HTTP habilitado | CWE-319 | CONFIRMADO (manifest) | 22 |
| E | Biometría/INE a terceros KYC extranjeros | CWE-359 | CONFIRMADO | 4 |
| F | IPs internas/privadas en binario de producción | CWE-200 | CONFIRMADO | 4 |
| G | Endpoints de prueba/mock en producción | CWE-489 | CONFIRMADO | 6+ |
| H | Clúster geográfico (mismo gobierno estatal) | — | CONFIRMADO | Guanajuato ×3 |
| I | Proveniencia: re-firmadas por la tienda | (no vuln) | CONFIRMADO | 18 |

---

## Clúster A — Antipatrón TLS trust-all (CWE-295)

El hallazgo más importante del cruce. Un *helper* que **desactiva la validación del certificado
TLS** (`X509TrustManager` con `checkServerTrusted` vacío + `HostnameVerifier` que devuelve `true`)
aparece de forma **independiente** en equipos de desarrollo sin relación entre sí. No es una sola
librería compartida: **cada app tiene su propia clase**, con el mismo cuerpo copiado. Es el patrón
de un snippet de StackOverflow endémico al desarrollo de apps de gobierno en México.

| App | Clase portadora | Ruta | Evidencia |
|-----|-----------------|------|-----------|
| **Mi Cuenta Infonavit** | `com.infonavit.data.RetrofitService.getUnsafeOkHttpClient()` | cliente de **todos** los servicios | **CONFIRMADO** — `checkServerTrusted` vacío |
| **Licencia Digital EDOMEX** | `mx.com.cosmocolor.movapp.webservice.ServiceGenerator.createService` | toda la API | **CONFIRMADO** — trust-all |
| **Licencia Digital Jalisco** | `mx.com.cosmocolor.movapp.jal…ServiceGenerator` | toda la API | **CONFIRMADO** — trust-all + hostnameVerifier=true |
| **ProcurApp (FGE Gto)** | `utils.UnsafeOkHttpClient.getUnsafeOkHttpClient()` | PROD / Genética(ADN) / Citas | **CONFIRMADO** — trust-all |
| **FiREL (CJF/FielNet)** | `getUnsafeOkHttpClient` + `CertificatePinner` coexisten | no atada a ruta prod | **A CONFIRMAR** |

**Orígenes independientes confirmados:** 3 (Infonavit · producto Cosmocolor · ProcurApp). Cosmocolor
cuenta como **un** producto replicado en ≥2 estados (ver clúster B). FiREL queda a confirmar porque
el método existe pero coexiste con pinning y no se pudo atar por xref a una ruta activa (ofuscación
R8).

**Revisadas y SANAS (no trust-all) — control del cruce:** 911-CDMX, Aguascalientes-Digital,
Directorio-INEGI, ICVNL, Identidad-Digital-BC, Tarjeta-Rosa, VaYVen, Valida-INE-QR. Todas se
revisaron explícitamente contra este antipatrón y **no** lo tienen (varias sin pinning, lo que es
otra cosa). Esto vale: distingue "no lo busqué" de "lo busqué y no está".

**Impacto:** en las 4 confirmadas el canal TLS no autentica al servidor → MITM transparente sobre
autenticación, pagos, **ADN/denuncias** (ProcurApp) y datos del derechohabiente (Infonavit).

**Divulgación:** cubierto en `divulgacion/01` (ProcurApp), `02` (Cosmocolor), `03` (Infonavit),
`06` (FiREL a confirmar).

---

## Clúster B — Producto Cosmocolor multi-estado (defecto de producto)

`mx.com.cosmocolor.movapp.*` es un **white-label** de licencia de conducir digital. El defecto TLS
vive en el **código base del proveedor**, no en la config de un estado.

- **Estados confirmados con app en binario:** EDOMEX (`mx.com.cosmocolor.movapp`), Jalisco (`.jal`).
- **Integración Chiapas (NO confirmada como app):** ambos builds embeben
  `chiapas-verifica-infraccion-…a.run.app`, `www.sspc.chiapas.gob.mx` y
  `webservice.interceptors.ChiapasVerificaInterceptor` → Cosmocolor tiene una **integración de
  verificación de infracciones con Chiapas**, con código reutilizado entre builds. **No** se
  confirmó una app de licencia de Chiapas publicada (no se halló en Google Play desde el sandbox;
  el developer de Play es la cuenta del gobierno estatal, no "Cosmocolor", así que no se puede
  enumerar por proveedor). Es una pista de cliente, no un tercer estado probado.
- **Backend compartido:** `api1/api2.licenciamanejo.com`, `faceapi.licenciamanejo.com`,
  `impresion.licenciamanejo.com` (HTTP en claro), `prod.cosmokeys.edomex.licenciamanejo.com`.
- **Dato nuevo del cruce:** `api.regulaforensics.com` / `faceapi.regulaforensics.com` en Jalisco →
  verificación facial/documental delegada a **Regula Forensics (Letonia)** — biometría a tercero
  extranjero (ver clúster E).

**Acción:** la corrección debe salir del proveedor y forzarse a **todos** los estados que lo usan.

---

## Clúster C — Backends compartidos entre apps distintas

Apps publicadas por dependencias distintas que pegan al **mismo backend** — amplían el radio de
cualquier falla del backend.

| Backend | Apps que lo comparten | Nota |
|---------|-----------------------|------|
| `api-cloud.pemex.com` | **Guía Pemex** + **Pemex-ASISTE** | mismo backend Pemex, 2 apps |
| `www.egbs5.com.mx` | **Tesorería CDMX** + **Japay (Yucatán)** | vendor compartido cross-estado (pagos) |
| `*.licenciamanejo.com` | EDOMEX + Jalisco (+Chiapas) | producto Cosmocolor (clúster B) |

El de `egbs5.com.mx` es el más interesante: une una app de **pagos de la CDMX** con una de **agua de
Yucatán** bajo un mismo proveedor — una falla ahí cruza dos gobiernos.

---

## Clúster D — Cleartext HTTP habilitado (CWE-319)

**22 de 45 apps** declaran `android:usesCleartextTraffic="true"` (valor leído del manifest, no de
prosa):

911-CDMX, APPrende-Jalisco, Bienvenido-México, CESPT, CFE-Proveedores, CONALITEG, CoDi-Banxico,
Directorio-INEGI, FiREL-PJF, ICVNL, IMSS-Digital, JMAS, Licencia-EDOMEX, Licencia-Jalisco,
Lotería-México, Mi-Aguah, Qrobus, Seguridad-Inmediata, Tesorería-CDMX, Urbani, VaYVen,
Aguascalientes-Digital (vía NSC).

**Higiene correcta (cleartext=false explícito):** AforeMóvil, App-CDMX, CFE-Contigo, Guía-Pemex.
19 apps no declaran el atributo (el default depende de `targetSdk`).

El caso grave es **911-CDMX**: cleartext **true** + 30+ endpoints `http://` de emergencias
(ubicación, ficha de salud, pánico). Cubierto en `divulgacion/04`.

---

## Clúster E — Biometría / INE a terceros KYC extranjeros (CWE-359)

Apps que capturan identidad (INE, rostro, huellas, firma) y la envían a **proveedores KYC de
terceros, varios extranjeros**:

| App | Terceros | Origen |
|-----|----------|--------|
| **FiREL (CJF)** | ShuftiPro, Identy.io | extranjero |
| **IMSS-Digital** | Acuant (`eu.acas.acuant.net`), FaceTec (`facetec-preprod.firmaautografa.com`) | extranjero / preprod |
| **ICVNL** | (KYC/biometría on-device + terceros) | revisar aviso |
| **Licencia-Jalisco** | Regula Forensics (`regulaforensics.com`) | Letonia |

Implicación de protección de datos (LFPDPPP / avisos de privacidad): identidad de ciudadanos
mexicanos procesada fuera del país. A revisar por cada dependencia.

---

## Clúster F — IPs internas/privadas en binario de producción (CWE-200)

Direcciones RFC1918 / hostnames internos horneados en el release (fuga de topología de red):

| App | Artefacto |
|-----|-----------|
| **911-CDMX** | `http://10.19.113.18/...EavisaRed` |
| **Infonavit** | `10.81.1.56`, `10.82.1.41`, `*.jaxws.infonavit.org.mx`, hosts `…internoqa…` |
| **IMSS-Digital** | `201.149.45.42`, hosts `…qaocp…` |
| **SAT-Móvil** | `13.85.190.35` |

---

## Clúster G — Endpoints de prueba/mock en producción (CWE-489)

Builds de producción que embeben entornos de prueba:

- **VaYVen** → `*.mock.pstmn.io` (**mock de Postman** en prod) + `api-dev/-qa/-prep.…mrptechnology.mx`.
- **SAT-Móvil** → `www.dev.cloudb.sat.gob.mx`, `…uat…`.
- **IMSS-Digital** → `…-qa.apps.ocp…`, `facetec-preprod…`.
- **Japay** → `dev-dev-japayaplicacion.yucatan.gob.mx`.
- **Identidad-Digital-BC** → `sandbox.ebajacalifornia.gob.mx`.
- **Tesorería-CDMX** → `prepro.adquiracloud.mx`.

---

## Clúster H — Clúster geográfico (mismo gobierno estatal)

**Guanajuato** concentra 3 apps analizadas: **ProcurApp** (FGE), **SEG-Móvil** (Seguridad),
**Tarjeta-Rosa**. ProcurApp tiene el trust-all crítico; Tarjeta-Rosa se revisó y está sana;
SEG-Móvil es GeneXus/Azure B2C. No comparten proveedor, pero sí comprador — útil para coordinar la
divulgación por un solo canal estatal.

---

## Clúster I — Proveniencia (no es vulnerabilidad)

**18 apps** fueron re-firmadas por la tienda de descarga (`CN=Android, O=Google Inc.` genérico de
APKPure) — artefacto de **adquisición**, no de la app. Para esas, el análisis de *código* es válido
(el bytecode no cambia al re-firmar) pero **la firma no sirve para probar autenticidad**; hay que
cruzar contra el canal oficial antes de divulgar.

**Auténticas confirmadas (firma del emisor real):** 911-CDMX (CAEPCCM), AforeMóvil (procesar),
Aguascalientes (GEA), Apoyo-Ciudadano-INE, Bienvenido-México (SAT), Directorio-INEGI, Gasoapp (CRE),
Guía-Pemex, Infonavit, ProcurApp (FGE), Tesorería-CDMX (cweb), Valida-INE-QR (INE).

---

## Frameworks / proveedores de desarrollo (inventario)

Proveedores y frameworks identificados por namespace/strings (señal, no exhaustivo):

- **Cosmocolor** — licencias (EDOMEX, Jalisco, Chiapas).
- **GeneXus** (`com.artech.*`) — SEG-Móvil.
- **TCS / Tata** (`com.tcs.*`) — Bienvenido-México (SAT).
- **Veritran** (`VTUserApplication`, Duktape) — BanjeCel, BancoBienestar, CoDi-Banxico,
  Aguascalientes, Lotería, Seguridad-Inmediata (muro de análisis estático).
- **moobky** — Mi-Policía-CDMX.
- **bigbangdev** — UNE-Sonora.
- **MRP Technology** (`mrptechnology.mx`) — VaYVen.
- **Urbani** (`urbani.io`) — Urbani/MiRuta MTY.
- **IQSEC / FielNet / Identy.io** — FiREL.
- **Pilas AOT (muro):** Flutter y .NET MAUI/Xamarin en ~15 apps — requieren análisis dinámico
  (Frida/reflutter) para ir más allá de strings.

---

## Adenda — Lote 4 (13 apps ≥100k nuevas, total corpus = 58)

RE de las 13 apps ≥100k que faltaban del top por instalaciones. Hallazgos cruzados con los clústeres:

### Nuevos en clúster D (cleartext / HTTP en claro, CWE-319)

| App | Dependencia | Detalle | Severidad |
|-----|-------------|---------|-----------|
| **Veracruz Mujer Alerta** | SSP Veracruz (prod. GeneXus) | alerta de **violencia de género + ubicación** por `http://alertagenero.sspver.gob.mx` | **CRÍTICA** |
| **SEV Empleado** | SEV Veracruz | **nómina + RFC** por `http://sirhportal.sev.gob.mx` (login sí HTTPS) | **ALTA** |
| **911Móvil BC** | C4 Baja California | NSC cleartext en 4 dominios backend de emergencias | **ALTA** |
| **CAASIM** | Agua Hidalgo | NSC reabre cleartext para API de **pagos de agua** | **ALTA** |
| **Internet Bienestar** | Qvantel/Altan | `usesCleartextTraffic=true` global (telecom/pagos) | MEDIA |

**Patrón Veracruz:** 2 dependencias del mismo estado (SSP + SEV) con cleartext confirmado.
**Hipótesis "pánico→cleartext":** mixta — Veracruz Mujer Alerta y 911Móvil BC ✓; Escudo Urbano y
Jalisco Alerta ✗. No universal.

### Clúster C (backends/pasarelas compartidas) — nuevo cruce

- **Adquira México** (pasarela de pago): **Tesorería CDMX** (`adquiracloud.mx`) ↔ **CAASIM Hidalgo**
  (`adquiramexico.com.mx`) — mismo procesador, dos gobiernos.

### Clúster G (dev/test en producción, CWE-489) — nuevos

Tuzobús (`apis-tests.hidalgo.gob.mx`), 911Móvil BC (`tijuana.c4bcdev.gob.mx`), Internet Bienestar
(`mapp-altan-dev.qvantel.systems`), Preinscripciones Coahuila (`jsonplaceholder`/`pokeapi` de plantilla).

### Proveedores / pilas nuevas

- **Nueva clase de muro — React Native/Hermes:** Internet Bienestar, Jalisco Alerta, 911Móvil BC,
  eSIT Hidalgo, Tuzobús. (Antes solo Flutter/Veritran/.NET eran muros.) El bytecode Hermes se
  rompe parcialmente con decompilación del string table (ver `REA-Internet-Bienestar/05`).
- **GeneXus cruce resuelto:** Veracruz Mujer Alerta (`CN=Artech Genexus`) **no** comparte el defecto
  de SEG-Móvil — GeneXus no mete trust-all; el cleartext de Mujer Alerta es despliegue de SSP.
- Vendors: Qvantel/Altan, Indra (Tren Maya), LUCA (Preinscripciones), Erlia (Jalisco Alerta).
- Pasarelas: Clip/Stripe (Tren Maya), OpenPay+OXXO+Paynet (Internet Bienestar), Adquira (CAASIM).

### Contraejemplos sanos del lote 4 (revisados, sin defecto)

REFAyGN/SEDENA (Flutter, red impecable), Descarga Cultura UNAM (la más limpia), Tren Maya (Clip/Stripe,
sin trust-all), eSIT Hidalgo, Jalisco Alerta. Preinscripciones Coahuila: PII de menores en Azure del
vendor (gobernanza, no seguridad).

### Candidatas nuevas a `divulgacion/`

Veracruz Mujer Alerta (CRÍTICA), SEV Empleado (ALTA), 911Móvil BC (ALTA), CAASIM (ALTA).

### Nota del análisis dinámico (device Motorola, sin root)

El MITM sin root topó muros: **Escudo Urbano** usa un engine Flutter **Shorebird no soportado** por
reflutter; **Internet Bienestar** (RN) quedó en blanco sin emitir su backend por el proxy. El
cleartext crítico de la mayoría es **post-login** → no confirmable sin credenciales reales (límite
ético). Lo que sí rindió: **decompilación estática del bytecode Hermes** (endpoints/topología). El
camino limpio para los `A-CONFIRMAR` restantes es **rootear el device** (frida-server) o Hermes/Dart
estático; no se afirma ninguna confirmación dinámica no obtenida.

---

## Qué sigue (si se decide continuar)

1. **Informe de divulgación sistémico** del clúster A (antipatrón TLS endémico) dirigido a un CERT
   nacional — es más eficaz que 4 informes sueltos para un patrón que se repite.
2. **Confirmar FiREL** (único "a confirmar" del clúster A) — requiere dinámico.
3. **Chiapas Cosmocolor** — confirmar si hay app de licencia publicada (hoy solo hay integración
   de verificación de infracciones por residuo; no se halló app en Play desde el sandbox US).
4. **Muro Flutter/.NET** (~15 apps) — Frida, único camino para lo que el estático no alcanza.
