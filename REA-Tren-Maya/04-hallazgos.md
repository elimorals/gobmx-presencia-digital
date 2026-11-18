# 04 · Hallazgos

Cruzado con `analisis/MAPA-PROVEEDORES-SDK.md`. Cada hallazgo marcado
CONFIRMADO / A-CONFIRMAR / AUSENTE-SANO. No se infiere causa sin evidencia.

## H1 · TLS y cleartext correctos — ✅ AUSENTE-SANO

No hay trust-all (sin clase `TrustManager` propia, sin `getUnsafeOkHttpClient`), no hay
`usesCleartextTraffic`, targetSdk 36 bloquea cleartext, backends HTTPS. **No** repite 911-CDMX ni
Infonavit. Contraejemplo limpio. (Detalle en [03](03-tls-pagos.md).)

## H2 · Telemetría y publicidad de terceros — BAJA-MEDIA · CONFIRMADO (CWE-359)

App de gobierno que empaca **publicidad y atribución**: permisos `AD_ID` +
`ACCESS_ADSERVICES_ATTRIBUTION`/`_AD_ID`, `BIND_GET_INSTALL_REFERRER_SERVICE`, SDK de
googlesyndication/googleadservices, Facebook SDK, Google Analytics (`app-measurement.com`) y un
webhook de métricas a **MongoDB Realm/Atlas** (`realmsdkmetrics-zmhtm`). Implica recolección de
telemetría/identificadores de usuarios de un servicio público y envío a terceros. Revisar contra el
aviso de privacidad (`trenmaya.gob.mx/AvisoPrivacidad.php`) y LFPDPPP. No es una vulnerabilidad de
seguridad clásica, sí una consideración de privacidad.

## H3 · Versión del backend expuesta — BAJA · CONFIRMADO (CWE-200)

La URL de la API embebe la versión del servicio web: `.../trenmaya-web-0.31.0/`. Facilita a un
atacante mapear la versión del backend y buscar vulnerabilidades conocidas de esa release. Fuga de
información menor; recomendable versionar por cabecera/API gateway, no en la ruta pública.

## H4 · Secretos hardcodeados — ✅ AUSENTE (CWE-798)

No se hallaron API keys, llaves de pasarela ni credenciales de comercio en claro en el DEX. Solo
nombres de propiedad `api_key`/`api-key` sin valores. Contrasta favorablemente con Tesorería CDMX
(credenciales Banorte) y Guía Pemex (`.env`).

## H5 · Proveniencia: re-firmada APKPure — INFO · CONFIRMADO

Firma genérica `CN=Android, O=Google Inc.` (APKPure), no la de Indra/operador. El análisis de código
es válido; la autenticidad del binario debe cruzarse contra Google Play oficial antes de divulgar.

## Veredicto

**Riesgo bajo.** La app está bien construida en lo esencial de seguridad de transporte (TLS,
cleartext, secretos de pago). El único punto con sustancia es la **telemetría/publicidad de
terceros** (H2), que es de privacidad, no de seguridad. No es candidata a `divulgacion/`.

## Pendiente (requiere dinámico)

- Pinning TLS efectivo en runtime (estático solo descarta el antipatrón, no confirma pinning).
- Flujo completo de pago Clip/Stripe (que el PAN no toque el backend propio).
- Almacenamiento local (Realm DB cifrada o no) — no auditado a fondo en este pase.
