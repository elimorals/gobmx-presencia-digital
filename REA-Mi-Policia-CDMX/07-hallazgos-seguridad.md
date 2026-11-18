# 07 — Hallazgos de seguridad (priorizados)

Observaciones **estáticas**; el binario nunca se ejecutó. Cada hallazgo lleva su evidencia. Lo que
depende del snapshot Dart no recuperable se marca como límite, no se infiere (doc 08).

## H1 — Credenciales de API hardcodeadas en el cliente · **Medio-Alto**

`libapp.so` contiene un **token `Bearer` estático** (`Bearer l1duV4g…`) y un **token de API en la ruta**
del endpoint de cuadrantes (`/cuadrante/s2lbgWk…/`). Son fijos para todas las instalaciones y se
extraen con `strings` sobre el `.so` (que va en claro dentro del APK). Quien los obtenga puede llamar a
los endpoints de la SSC autenticándose como la app.
- **Evidencia:** `evidence/secretos-hardcoded.txt` (valores completos, no versionado); doc 03.
- **Límite:** no se probaron los endpoints; no se afirma el alcance de autorización de cada token.
- **Mitigación:** emitir tokens por sesión/usuario desde el backend; no embeber credenciales de servicio
  en el cliente.

## H2 — App de seguridad firmada por un desarrollador individual · **Medio**

La firma es `CN=Saul Espinosa Ruiz` (persona física), no un certificado institucional de la SSC. La
llave que gobierna actualizaciones e identidad de una app oficial de seguridad con ~1 M de
instalaciones es personal → riesgo de cadena de suministro.
- **Evidencia:** `apksigner` (doc 06).
- **Mitigación:** migrar a una cuenta/clave institucional (Play App Signing con custodia de la SSC).

## H3 — Permiso `DUMP` + ubicación en segundo plano · **Bajo-Medio**

El manifiesto declara `android.permission.DUMP` (permiso `signature|privileged` que una app de tienda
no puede obtener): declaración inusual/residual. Sumado a `ACCESS_BACKGROUND_LOCATION`
(`background_location`), la app tiene una superficie de ubicación amplia.
- **Evidencia:** `AndroidManifest.xml` (doc 01).
- **Límite:** el código que usaría `DUMP` está en Dart; no se observó su uso (no se infiere propósito).

## H4 — Superficie de API de la SSC expuesta · **Informativo**

El snapshot revela la topología del backend: `api-p.ssc.cdmx.gob.mx` (reglamento de tránsito:
`busca_placa`, `busca_empresa`, `elemento_cuadrante`, `acreditado_post`), `cuadrantes.*`, `cms.*`,
`ucs.*:8443`. Útil para entender el sistema; no es una vulnerabilidad por sí mismo (doc 03).

## H5 — Referencia `http://` latente · **Bajo**

`http://data.ssp.cdmx.gob.mx/.../AVISOS.xlsx` es el único recurso en claro. Sin `networkSecurityConfig`
y sin `usesCleartextTraffic`, quedaría bloqueado por defecto en Android moderno (doc 01). Se reporta
por completitud; no constituye cleartext activo salvo targetSdk &lt;28 (no confirmado).

## H6 — Runtime Flutter dated · **Bajo**

Motor **Flutter 3.3.4** (2022). No es una vulnerabilidad concreta, pero es deuda: un engine viejo
acumula correcciones de seguridad no aplicadas (doc 02).

## Postura positiva

- **Todas las APIs de la SSC por HTTPS.**
- **Indicio de pinning a la raíz DigiCert G2** (cert embebido + primitivas Dart) — defensa en
  profundidad frente a MITM (doc 04).
- **Sin cleartext habilitado** (sin NSC permisivo).
- **Permisos acotados** salvo `DUMP`: sin SMS, contactos ni almacenamiento externo amplio.
- **Firma original preservada** (no re-firmada en su firma principal).
