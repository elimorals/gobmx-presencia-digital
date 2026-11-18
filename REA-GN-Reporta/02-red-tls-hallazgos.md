# 02 · Red, TLS y hallazgos

## Backend y transporte

- Capa de red: **Volley** (88 refs en el DEX).
- Endpoint único observado: **`https://certmx.gn.gob.mx/gn_reporta/pfmovilservice.php`** (HTTPS) y
  la base `https://certmx.gn.gob.mx/gn_reporta/`. Dominio `gn.gob.mx` = Guardia Nacional.
- **No se halló ningún endpoint `http://`** de backend en el DEX. El `usesCleartextTraffic="true"`
  está **permitido pero no usado**.

## H1 — Cleartext permitido pero no usado (CWE-319, config) · MEDIA-BAJA · CONFIRMADO

`usesCleartextTraffic="true"` global, sin `network_security_config`. No hay endpoint http en uso,
pero el flag deja la puerta abierta. Para una app de **denuncia a la Guardia Nacional** la postura
correcta es cleartext deshabilitado. Es higiene, no una fuga activa.

## H2 — SelfSignedSSLSocketFactory de Couchbase Lite · A-CONFIRMAR

El DEX contiene `com.couchbase.lite.support.CouchbaseLiteHttpClientFactory$SelfSignedSSLSocketFactory`
con `checkServerTrusted` y `SelfSignedSSLCertificates`. **Es código de la librería Couchbase Lite**
(base de datos local con capacidad de replicación/sync), **no** una utilidad trust-all propia de la
app atada al backend GN (que usa Volley sobre HTTPS estándar).

- **Lo que NO se afirma:** no se observó un endpoint de **sync** de Couchbase ni se ató el factory
  self-signed a una ruta de producción. **No es el caso Infonavit/Cosmocolor** (no hay
  `getUnsafeOkHttpClient` ni un `X509TrustManager` vacío propio). Si existiera una replicación
  Couchbase contra un Sync Gateway con cert self-signed, ese canal sí sería débil — pero es
  **A-CONFIRMAR** y requeriría dinámico; no se inventa la causa.

## H3 — Superficie de captura de evidencia · INFO · CONFIRMADO

Cámara + audio + video + almacenamiento: la app graba evidencia del denunciante. Sensible por
naturaleza (contenido de una denuncia); el canal de subida es HTTPS, lo que mitiga el tránsito.

## H4 — Trust-all propio · AUSENTE-SANO

No se halló `getUnsafeOkHttpClient`, ni un `TrustManager`/`HostnameVerifier` permisivo propio de la
app, ni endpoints http de backend. El único `checkServerTrusted` es de la librería Couchbase (H2).

## Cruce con el mapa de proveedores

- **No** cae en el clúster A (trust-all) ni confirma el D (cleartext activo). Contraejemplo
  parcial: una app de denuncia federal con el backend por HTTPS, con una bandera de higiene
  (cleartext permitido) y una dependencia (Couchbase self-signed) a vigilar.
- **Proveniencia:** re-firmada APKPure → la firma no prueba autenticidad; verificar contra el canal
  oficial de la SSPC/GN antes de cualquier divulgación.
