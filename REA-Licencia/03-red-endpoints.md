# 03 · Red y endpoints

Análisis estático del JS en `assets/public/`. No se ejecutó la app ni se interceptó tráfico.

## Backends

| Entorno | Host base | Evidencia |
|---|---|---|
| **Producción** | `https://wslfd.sct.gob.mx/api` | literal en el bundle JS |
| **QA / lab** | `https://wslfdqa.apps.lab.sct.gob.mx/api` | literal en el bundle JS |

`WSLFD` = Web Service Licencia Federal Digital. `sct.gob.mx` = Secretaría de Comunicaciones y
Transportes (hoy SICT).

> **Hallazgo:** el host de **QA** (`wslfdqa.apps.lab.sct.gob.mx`) viaja en el **mismo bundle de
> producción**. No es un endpoint activo por defecto (la app usa el de prod), pero expone
> infraestructura interna de pruebas y es un resto que no debería embarcarse.

## Rutas concretas de la API

Las rutas (`/api/...`) se **construyen dinámicamente** en la capa de servicios (concatenación
`base + recurso`), no aparecen como literales completos en el bundle minificado. Por tanto este
documento fija los **hosts base** con evidencia; el detalle de cada endpoint (`/login`, etc.)
**no se afirma** porque no hay un literal que lo sustente. Se resolvería con análisis dinámico
(proxy MITM) o beautify + trace del `LicenseService`/`http` wrapper.

## Capa HTTP

- Cliente: **`cordova-plugin-advanced-http`** (HTTP nativo sobre OkHttp, no `fetch`/XHR del WebView).
  Submódulos presentes: `http`, `cookie-handler`, `tough-cookie`, `local-storage-store`, `url-util`,
  `ponyfills`, `lodash` (ver `cordova_plugins.js`).

## Confianza TLS / certificate pinning

- advanced-http expone `setServerTrustMode('default'|'legacy'|'nocheck'|'pinned')`.
- **El código de la app no invoca `setServerTrustMode`** (no hay llamada en `main`/`common`/`pages`;
  los valores `pinned`/`nocheck` sólo aparecen como enum dentro del propio plugin).
- Conclusión: la app queda en el modo **`default`** → valida el certificado contra el almacén de
  confianza del sistema. **No hay pinning propio**, pero **tampoco** está desactivada la
  validación. No se detectó certificado embebido (`.cer`/`.der`/`.pem`) para pinning.

## Push

- **Firebase Cloud Messaging** (`CapacitorFirebaseMessagingService`, `FirebaseInstanceIdReceiver`,
  `FirebaseInitProvider`). No se recuperó el `project_id` del `strings.xml` decodificado
  (`PROJECT_ID_RESOURCE_NAME` es genérico); no se afirma el proyecto Firebase.

## Qué queda abierto (honesto)

- Rutas exactas de la API y formato de payload/token: **no determinados estáticamente** (requieren
  dinámica). No se inventan.
