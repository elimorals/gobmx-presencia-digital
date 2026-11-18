# Ingeniería inversa de APPrende Jalisco 2.0.5 — índice

Análisis **estático** del APK `mx.gob.jalisco.edu.apprende` (etiqueta "APPrende") con REA
(JADX headless + inspección del bundle web). La app **nunca se ejecutó**. Es la app de la
**Secretaría de Educación Jalisco (SEJ)** — plataforma educativa: calificaciones, asistencia,
preinscripción, trámites, formulario de salud, RH docente. **#36 por instalaciones** del
inventario gob.mx (≈100 K).

> **Híbrida Ionic/Angular + Cordova.** La lógica es un bundle Angular (`assets/www/`, chunks
> `*-es2015/es5.js` **con source maps**) sobre Cordova. Identidad por **Firebase** (Auth + RTDB,
> **varios proyectos** incluido uno de **prueba** embebido en producción); backends **HTTPS** en
> `*.jalisco.gob.mx`. En el bundle quedan **6 llaves web de Google/Firebase** y un **`ApiKey`
> hex hardcodeado**. Transporte sano (HTTPS, sin cleartext); la app maneja **PII educativa
> fuerte** (calificaciones, asistencia, salud, RH), así que la exposición vive en las reglas de
> Firebase (no verificables estáticamente) y en los secretos embebidos.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Ionic/Angular, Firebase, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (cuentas, audio), componentes |
| 02 | [02-arquitectura-hibrida.md](02-arquitectura-hibrida.md) | Ionic/Angular + Cordova, source maps, módulos |
| 03 | [03-firebase-backends.md](03-firebase-backends.md) | Firebase (Auth/RTDB, proyectos), backends SEJ |
| 04 | [04-secretos-keys.md](04-secretos-keys.md) | 6 llaves Google + `ApiKey` hex, source maps |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure, procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos y preguntas abiertas |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado),
`evidence/firebase-keys-endpoints.txt` (configs Firebase + ApiKey + endpoints, **no versionado**),
`evidence/firma.txt` (no versionado).

## 6 hallazgos principales

1. **(Medio) Proyecto Firebase de PRUEBA embebido en producción.** El bundle trae config de
   `apprende-prod` **y** `apprende-prueba` (+ `geniat-recreajalisco`, `ingeniat-comunication`),
   con sus `databaseURL` de **Firebase Realtime Database**. Un proyecto de prueba en el build de
   producción es fuga de infraestructura y superficie extra (doc 03).
2. **(Medio, pregunta abierta) Firebase RTDB.** Se usan `databaseURL` de Realtime Database
   (`*.firebaseio.com`). La seguridad de los datos (calificaciones, asistencia, salud) depende de
   las **reglas de seguridad del lado servidor**, que **no son verificables estáticamente**. Si
   fueran laxas, habría exposición de PII educativa. Se marca como superficie a revisar, no como
   hecho (doc 03, 06).
3. **(Bajo-Medio) `ApiKey` hex hardcodeado en el bundle** (`ce93108d…`, valor en `evidence/`).
   Secreto embebido de propósito no confirmado; recuperable por cualquiera que abra el APK
   (doc 04).
4. **(Bajo) 6 llaves web de Google/Firebase en claro** (`AIzaSy…`). Son **públicas por diseño**
   en apps Firebase, pero deben **restringirse por package/SHA-1 y API** en la consola de GCP;
   expuestas identifican los proyectos (doc 04).
5. **(Bajo) Source maps en producción.** Se envían los `.map` → el TypeScript Angular es
   reconstruible (exposición de código/estructura; no es vulnerabilidad en sí) (doc 02).
6. **Transporte sano (positivo).** Backends `https://api2plataformaeducativa.jalisco.gob.mx`,
   `https://plataformadigital.sej.jalisco.gob.mx`, `https://mi.sej.jalisco.gob.mx` — **HTTPS**,
   dominios oficiales; sin `usesCleartextTraffic`; identidad delegada a Firebase Auth (doc 03).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK; los valores de llaves/ApiKey viven solo en `evidence/` (`.gitignore`).
Observaciones **estáticas**: no se ejecutó la app ni se llamó a Firebase/endpoints. Las **reglas
de seguridad de Firebase** y el propósito del `ApiKey` custom **no son verificables
estáticamente** — marcados como preguntas abiertas. **Firma** re-firmada por APKPure → no
verificable como auténtica de la SEJ.
