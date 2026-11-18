# 11 — Hallazgos de seguridad (priorizados)

Análisis **estático** de `com.icv.app` 2.4. Cada hallazgo con evidencia; donde la evidencia no
alcanza, se dice. La postura general es **razonable**: firma auténtica, sin bypass de TLS, sin
cripto rota, telemetría nativa mínima. Los puntos abiertos se concentran en el plano WebView.

## H1 — Lógica y endpoints recuperados del AssemblyStore .NET (informativo)
La app es .NET MAUI; sus 425 ensamblados viven cifrados-en-store (`libassembly-store.so`, LZ4).
Se extrajeron y se recuperó el backend y las 16 acciones de la API (docs 02-03). No es una
vulnerabilidad: es el alcance del análisis. **Impacto:** nulo. **Evidencia:** doc 02-03.

## H2 — `usesCleartextTraffic=true` + WebView con puente JS y URL del servidor (medio)
`usesCleartextTraffic="true"` sin `networkSecurityConfig` (doc 01), combinado con un WebView que
(a) carga URLs que **devuelve el servidor** (`UrlPortal`/`UrlTramite`), (b) expone un puente
`invokeCSharpAction`→C#, y (c) tiene helpers blob→archivo y acceso a cámara/liveness (doc 06-07).
Si alguna carga ocurriera sobre HTTP o con contenido mixto, un MITM podría inyectar JS que
invoque el puente. **No hay evidencia de que suceda** (las URLs no son observables y la API
nativa es HTTPS). **Impacto:** potencial MITM→abuso del puente. **Fix:** quitar
`usesCleartextTraffic` o añadir NSC que fuerce HTTPS; validar origen en el WebView.
**Evidencia:** docs 01, 03, 07.

## H3 — `allowBackup=true` (bajo-medio)
Permite respaldar datos de la app, incluida la **licencia offline** (con imagen), el usuario
recordado y el token de atención (doc 08). En un dispositivo con depuración/USB o respaldo en
nube, esos datos salen del sandbox. **Fix:** `allowBackup=false` o `fullBackupContent`/
`dataExtractionRules` que excluya datos sensibles. **Evidencia:** docs 01, 08.

## H4 — Sin certificate pinning (bajo)
No hay pinning ni validación personalizada (doc 03). La validación recae en la cadena de CAs del
sistema. Para una app con PII vehicular y de identidad, el pinning reduciría el riesgo de MITM
vía CA comprometida/inyectada. **No hay** la desactivación de validación que sí tiene Infonavit;
es una mejora recomendable, no un defecto crítico. **Evidencia:** doc 03.

## H5 — Permiso `INSTALL_PACKAGES` declarado (bajo / informativo)
La app declara `android.permission.INSTALL_PACKAGES` (doc 01), un permiso `signature|privileged`
que **el sistema no concede a apps de tienda**: queda inefectivo. Señala intención de instalar
APKs (¿auto-actualización fuera de Play?). **Fix:** eliminarlo si no se usa; usar el flujo de
actualización in-app de Play. **Evidencia:** doc 01.

## H6 — Permisos de almacenamiento amplios (bajo)
`WRITE_EXTERNAL_STORAGE` + `READ_EXTERNAL_STORAGE` (doc 01) para guardar PDF/imagen de licencia
vía los helpers del WebView (doc 07-08). En targetSdk 36 el acceso amplio está acotado por
scoped storage, pero sigue siendo más de lo necesario si solo se guardan archivos propios.
**Fix:** usar almacenamiento específico de la app / SAF. **Evidencia:** docs 01, 07.

## Lo que quedó fuera de alcance (sin inventar)
- **Pagos:** se realizan en el portal web, no en el APK (doc 05). No se identificó pasarela.
- **Proveedor de liveness facial:** es web, no SDK nativo; no identificable desde el binario
  (doc 06).
- **URLs de `UrlPortal`/`UrlTramite`:** las resuelve el servidor; no observables estáticamente.
- **C# textual:** REA entrega metadata/IL/llamadas/literales, no fuente descompilada (sin ILSpy
  en el entorno). Las afirmaciones se basan en ese nivel, no en lectura línea a línea.
- **Comportamiento en runtime:** la app no se ejecutó.
