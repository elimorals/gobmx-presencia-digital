# 02 · Red y endpoints

Todos los backends propios son **HTTPS**. Extraídos de strings del DEX (`classes*.dex`).

## Endpoints propios

| Host | Uso |
|---|---|
| `https://mobility.ventaboletostrenmaya.com.mx/trenmaya-web-0.31.0/` | **API de venta de boletos** (núcleo). Dominio del proveedor; la ruta embebe la versión web `0.31.0`. |
| `https://www.trenmaya.gob.mx/` | portal institucional: `index.php`, `nosotros.php`, `DestinosTrenMaya.php`, `AvisoPrivacidad.php`, `images/transparencia/` (contratos, términos) |
| `https://grupoolmecamayamexica.com.mx/` | operador (Grupo Olmeca-Maya-Mexica) |
| `https://www.mexicana.gob.mx/` | aerolínea Mexicana (enlace cruzado del mismo operador) |
| `https://aifa.aero/` | AIFA (enlace cruzado) |

## Terceros (telemetría/publicidad/pago)

| Host / SDK | Función |
|---|---|
| `https://data.mongodb-api.com/app/realmsdkmetrics-zmhtm/endpoint/metric_webhook/metric` | **MongoDB Realm/Atlas** — webhook de métricas |
| `https://app-measurement.com/...` | Google Analytics / Firebase |
| `https://pagead2.googlesyndication.com`, `www.googleadservices.com/.../deeplink` | **publicidad Google** |
| `https://www.facebook.com/TrenMayaOficial` + Facebook SDK | social / atribución |
| **Clip + Stripe SDK** | **pasarela de pago** (ver [03](03-tls-pagos.md)) |

> Los `http://` hallados en el DEX son constantes de namespaces XML (`xml.org/sax`,
> `javax.xml.XMLConstants`), `http://localhost/` y docs de slf4j — **no** son endpoints de
> backend. No hay tráfico de backend en claro.
