# Red y endpoints

Recuperados de los literales `ldstr` (#US, UTF-16) de `DemoCFE.dll`.

## Endpoints propios
| Uso | URL |
|---|---|
| **API REST** | `https://enlacesec.cfe.mx/api/CFEService/` (+ `getdatosinicioproveedor/`) |
| **Portal web** | `https://msc.cfe.mx/Aplicaciones/NCFE/Concursos/Proveedores/` |

Ambos **HTTPS** y en dominios oficiales de la CFE (`cfe.mx`). "MSC" = módulo de servicios CFE;
"NCFE/Concursos/Proveedores" = concursos de proveedores. La capa cliente es
`DemoCFE.Data.RestService` sobre `System.Net.Http.HttpClientHandler`.

## IPs internas en el NSC (hallazgo)
El `network_security_config` (doc 01) habilita **cleartext** a dos **IPs privadas de la CFE**:
```
10.4.70.22   (RFC1918, red interna)
10.220.1.71  (RFC1918, red interna)
```
No son alcanzables desde internet, pero su presencia en el build de producción **filtra
topología interna** (rangos/segmentos de la red de la CFE) y deja HTTP en claro habilitado hacia
ellas — residuo de un entorno de desarrollo/interno.

## Terceros
No se observaron SDKs de analytics/ads/pagos en los literales del ensamblado propio (solo
Newtonsoft.Json para serialización y Rg.Plugins.Popup para UI).

## Transporte
`usesCleartextTraffic=true` a nivel app (doc 01), pero los endpoints propios usan `https://` por
su URL → en la práctica cifrados. Sin endpoints `http://` propios observados fuera de las 2 IPs
internas.
