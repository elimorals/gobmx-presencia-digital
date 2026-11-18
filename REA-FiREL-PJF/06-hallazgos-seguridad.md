# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se capturó biometría ni se
llamó a ningún endpoint. Contexto: app de **firma electrónica judicial** que maneja la PII más
sensible del acervo (INE + biometría + firma + video).

## H1 (Alto, A CONFIRMAR) — Postura TLS contradictoria sobre datos biométricos
El DEX contiene a la vez `okhttp3.CertificatePinner` (pinning) y **`getUnsafeOkHttpClient`** +
`X509TrustManager`/`sslSocketFactory`/`setHostnameVerifier` (patrón de desactivación de TLS,
doc 04). Si el cliente inseguro está en la ruta de las llamadas de INE/rostro/huella/firma, un
MITM podría leer o alterar biometría de ciudadanos — misma clase que Infonavit/Licencia EDOMEX.
**No se confirmó** la relación (método posiblemente de librería + R8). **Severidad potencial
alta; estado: a verificar** (dinámico o decompilación completa del `ApiServiceOperator`). **Fix:**
garantizar que todas las rutas usan el cliente pinneado; eliminar `getUnsafeOkHttpClient`.

## H2 (Medio) — PII biométrica extrema hacia múltiples terceros
Captura y transmite INE (OCR+foto), rostro (match+liveness), huellas sin contacto, firma y video
(doc 03) a **IQSEC + Identy.io + ShuftiPro** (doc 04), uno de ellos extranjero. Amplía la
superficie de exposición de datos biométricos irrevocables. **Fix:** minimizar terceros;
transparencia sobre dónde se procesa la biometría; retención/borrado.

## H3 (Medio) — White-label con fuga de origen y backend fuera de `gob.mx`
El código real es `com.fielnet.notificaciones_colegio_notarios_mvvm` (app de Colegio de Notarios
re-etiquetada) y el backend es de **IQSEC** (`iqsec-masivos.com.mx`), no `cjf.gob.mx` (doc 02,
04). Cadena de suministro: la firma del Poder Judicial depende de un producto y una
infraestructura de un proveedor privado. Observación, no vulnerabilidad en sí.

## H4 (Bajo) — Higiene del build
- **`tempuri.org`** (namespace SOAP por defecto, sin configurar) en producción (doc 04).
- **JUnit empaquetado** en el release (doc 05).

## Positivos

- **Biometría con liveness/anti-spoof** (Identy touchless + `TouchlessSpoofDialog`, doc 03).
- **Pinning presente** (`CertificatePinner`) — la intención de asegurar TLS existe; el problema es
  la coexistencia con el cliente inseguro (H1).
- **Sin cleartext declarado** en el manifiesto; sin endpoints `http://` propios (doc 01, 04).
- **Pantallas de aviso de privacidad y acuerdo** (`ActivityNoticePrivacy`, `PrivacyAndAgreement`).

## Límites (sin inventar)

- **H1 no está confirmado:** no se ató `getUnsafeOkHttpClient` a una clase propia ni a una ruta de
  red (R8 + posible librería). Es lo pendiente clave.
- **Almacenamiento del certificado emitido:** se genera en servidor (`RequestGenerateCertificate`);
  el guardado local fino no se verificó.
- **Claves de ShuftiPro/Identy:** no se hallaron en claro; probablemente por token/servidor — no
  se afirma su ausencia absoluta.
- **Firma re-firmada por APKPure** → conclusiones de arquitectura, no de integridad.
