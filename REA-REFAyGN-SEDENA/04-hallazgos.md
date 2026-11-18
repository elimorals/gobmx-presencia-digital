# 04 · Hallazgos

## H1 — Proveniencia: re-firmada por APKPure · INFO · CONFIRMADO

El APK de la tienda está re-firmado con el cert genérico `CN=Android, O=Google Inc.`. El bytecode/
AOT no cambia al re-firmar (el análisis de código es válido), pero la **firma no sirve para probar
autenticidad**. Antes de cualquier divulgación, cruzar el hash contra el APK de Google Play oficial
de SEDENA.

## H2 — Red bien construida · AUSENTE-SANO · CONFIRMADO (el hallazgo principal)

Para una app de **máxima sensibilidad** (registro federal de armas de fuego, SEDENA), la capa de red
es correcta:

- **HTTPS** exclusivo a dominio oficial `reefam.sedena.gob.mx`; sin `http://`, sin IP interna, sin
  subdominio de prueba.
- **Sin cleartext** (bloqueado por default en targetSdk 34, sin NSC permisivo).
- **Permisos mínimos** (solo INTERNET + NETWORK_STATE); sin ubicación/cámara/SMS/almacenamiento.
- **Sin secretos** en claro (strings y assets limpios); **sin Firebase**.
- Componentes: solo el launcher exportado; receiver exportado protegido por permiso propio.

**No cae en ningún clúster del [mapa de proveedores](../analisis/MAPA-PROVEEDORES-SDK.md).** Es un
contraejemplo: no toda app gob.mx sensible hereda los defectos.

## H3 — Validación TLS efectiva no verificable · CWE-295 · A-CONFIRMAR

El cliente Dart podría (o no) registrar un `badCertificateCallback` permisivo; el cuerpo vive en el
AOT y no se puede leer estáticamente. El símbolo `SecureSocket_RegisterBadCertificateCallback` en
`libapp.so` es del runtime de Flutter y **no** prueba trust-all. Para cerrarlo se necesita dinámico
(reflutter / Frida con MITM y hook a la validación de certificado).

## Qué quedó tras el muro Dart AOT

- Lógica real del registro/consulta de armas.
- Validación TLS efectiva del cliente (H3).
- Ausencia total de secretos (podrían estar en el snapshot ofuscado).
- Qué datos del ciudadano/arma se envían y cómo se almacenan localmente.

Todo lo anterior requeriría análisis dinámico. En estático, **la app no presenta hallazgo de
severidad media o superior**.
