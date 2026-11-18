# Red — endpoints HTTP y ambientes en producción

Endpoints embebidos en el DEX (`evidence/endpoints.txt`), seleccionados por `ConfiguracionAmbiente`
(doc 02).

## Mapa de endpoints

| Ambiente | URL | Protocolo |
|---|---|---|
| **Producción SAT** | `m.sat.gob.mx/m/sp/_layouts/listfeed.aspx` | ⚠️ **HTTP** |
| **Producción SAT** | `www.sat.gob.mx/m/sp/_layouts/listfeed.aspx` | ⚠️ **HTTP** |
| **Producción SIAT** | `movil.siat.sat.gob.mx/` | HTTPS |
| **Producción SIAT** | `siat.sat.gob.mx/wcfcpitv` (servicio **WCF/SOAP**) | HTTPS |
| **UAT (pruebas)** | `piuat.sat.gob.mx/m/sp/_layouts/listfeed.aspx` | ⚠️ **HTTP** |
| **UAT (pruebas)** | `moviluat.siat.sat.gob.mx/` | HTTPS |
| **Desarrollo (Azure)** | `develmobile.cloudapp.net/` | ⚠️ **HTTP** |
| **QA (hosting gratuito)** | `satmovilqd.gear.host` | ⚠️ **HTTP** |
| Externo | `www.banjercito.com.mx/registro` (permiso vehicular) | HTTPS |

## H1 — Cleartext HTTP a producción del SAT

Los feeds de contenido (`listfeed.aspx`, SharePoint) de `m.sat.gob.mx`/`www.sat.gob.mx` están en
**`http://`**, y el manifiesto habilita `usesCleartextTraffic="true"` (doc 01). El contenido que
ve el paisano (información, avisos) **puede viajar sin TLS** → un MITM en la red (p. ej. un Wi-Fi
fronterizo) puede **leer o alterar** lo mostrado. El SIAT (consultas de permisos) sí es HTTPS.

## H2 — Ambientes de desarrollo/pruebas embebidos en el binario de producción

`ConfiguracionAmbiente` dejó en el APK publicado el **mapa completo de ambientes**:
- **UAT:** `piuat.sat.gob.mx`, `moviluat.siat.sat.gob.mx`,
- **Desarrollo:** `develmobile.cloudapp.net` (un **Azure Cloud Service** de "devel mobile"),
- **QA:** `satmovilqd.gear.host`.

Consecuencias: **fuga de infraestructura interna** (nombres de host de pruebas/desarrollo del
SAT), superficie innecesaria, y señal de que el build no se saneó para producción. Un atacante
obtiene gratis el inventario de entornos no productivos del Programa Paisano.

## H3 — Endpoint del SAT en un hosting gratuito

`http://satmovilqd.gear.host` (`qd` ≈ QA/dev) corre en **gear.host**, un proveedor de **hosting
gratuito** de terceros, por **HTTP**. Que un recurso relacionado con una app del SAT esté en un
host gratuito es una mala práctica de infraestructura (control, disponibilidad y confianza fuera
de manos del gobierno). No se verificó si sigue activo (no se tocó el endpoint).

## Pila de transporte

Las peticiones las hace `Connection/ConnectionManager` sobre **`org.apache.http.legacy`** (cliente
HTTP obsoleto, doc 01). No se observó `networkSecurityConfig` ni certificate pinning → con
`usesCleartextTraffic=true`, la validación recae en las CAs del sistema solo para las rutas HTTPS;
las HTTP no validan nada por definición.
