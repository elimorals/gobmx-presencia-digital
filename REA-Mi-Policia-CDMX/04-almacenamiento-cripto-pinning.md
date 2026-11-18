# 04 — Almacenamiento, criptografía y pinning TLS

## Certificado embebido

`assets/flutter_assets/assets/cert/ssc-cdmx-gob-mx.pem` (1 316 bytes). Contenido (leído con
`openssl x509`):

```
subject = C=US, O=DigiCert Inc, OU=www.digicert.com, CN=DigiCert Global Root G2
issuer  = (self-signed, misma CA)
notBefore = Aug  1 2013   notAfter = Jan 15 2038
```

Es la **raíz pública DigiCert Global Root G2**, no un certificado hoja de la SSC. El nombre del archivo
(`ssc-cdmx-gob-mx.pem`) indica que está pensado para las conexiones a `*.ssc.cdmx.gob.mx`, cuyos
certificados encadenan a esa raíz.

## Primitivas de pinning presentes (indicio, no hecho)

En `libapp.so` aparecen los símbolos de `dart:io`:

```
SecurityContext_Allocate
SecurityContext_SetTrustedCertificatesBytes
SecurityContext_TrustBuiltinRoots
SecureSocket_RegisterBadCertificateCallback
_onBadCertificateWrapper / _registerBadCertificateCallback
```

**Matiz honesto:** estos símbolos forman parte del runtime de `dart:io` y **aparecen en prácticamente
cualquier app Flutter**, haya o no pinning. Lo que sí es específico de esta app es el `.pem` de la raíz
DigiCert embebido como asset nombrado para la SSC. La combinación (cert embebido + `SetTrusted­Certificates­Bytes`)
es un **indicio fuerte de pinning a la raíz DigiCert G2** — más débil que pinning a hoja (acepta
cualquier cert válido emitido bajo esa raíz), pero restringe el conjunto de CAs aceptadas. El código
Dart que efectivamente crea el `SecurityContext` con ese cert y decide rechazar/aceptar vive en el
snapshot AOT y **no se aisló**; por tanto se reporta como indicio, no como hecho confirmado (doc 08).

## Almacenamiento local

El DEX no contiene lógica de almacenamiento (doc 02). En Dart, los plugins típicos para esto
(shared_preferences / sqflite / secure storage) **no** aparecen en la lista de `package:` recuperada
(doc 05): se vieron `cached_network_image`/`flutter_cache_manager` (caché de imágenes) y `file`, pero
no un store cifrado ni base de datos evidente. No se afirma cómo persiste la sesión/tokens del usuario
porque esa lógica es Dart no recuperable — se marca como límite (doc 08), sin inventar.

## Criptografía

No se observaron símbolos de cripto de aplicación propios más allá de los de TLS de `dart:io`. El
token `Bearer` y el token de ruta viajan en claro dentro del snapshot (doc 03), sin derivación ni
cifrado local observable.
