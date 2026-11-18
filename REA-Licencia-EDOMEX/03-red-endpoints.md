# Red y endpoints

Dominios y rutas extraídos del código decompilado (smali de `mx/com/cosmocolor`). Todo el
backend vive en el **dominio privado del proveedor `licenciamanejo.com`** (Cosmocolor), **no**
en `*.edomex.gob.mx`.

## Endpoints base

| URL | Rol | TLS |
|---|---|---|
| `https://api2.licenciamanejo.com/edomex/appmov/` | **API principal** de la app móvil (licencia, pago, OTP, catálogos) | https, pero por la ruta **trust-all** (doc 04) |
| `https://faceapi.licenciamanejo.com/` | **Verificación facial** (enrolamiento) | https, ruta trust-all (`cameralibrary`) |
| `https://prod.cosmokeys.edomex.licenciamanejo.com` | Servicio de **llaves** ("cosmokeys") — probable aprovisionamiento de material criptográfico | https |
| `http://impresion.licenciamanejo.com/objetosImpresionApi/` | **API de impresión** de objetos de la licencia | ⚠️ **HTTP EN CLARO** |
| `https://api2.licenciamanejo.com/edomex/web/noticias/edomex` | Noticias (WebView/news) | https |
| `https://docs.google.com/gview` | Visor de documentos (PDF embebido) | https (tercero) |

## Restos de Chiapas (white-label)

| URL | Nota |
|---|---|
| `https://chiapas-verifica-infraccion-ty3qpufnqq-uc.a.run.app/` | Cloud Run de verificación de infracciones de **Chiapas** |
| `https://www.sspc.chiapas.gob.mx/Secciones/vialidad` | Portal de vialidad de Chiapas |

Van acompañados de `webservice.interceptors.ChiapasVerificaInterceptor`. Son código reutilizado
de otro despliegue del mismo proveedor que quedó en el binario de EDOMEX (doc 02).

## Cleartext

- `android:usesCleartextTraffic="true"` a nivel aplicación, **sin** `networkSecurityConfig` que
  lo restrinja por dominio (doc 01).
- El endpoint de impresión usa `http://` explícito. Cualquier objeto de impresión de la licencia
  (que por definición contiene datos del titular) viaja sin cifrar y es interceptable/modificable
  en red.

## Interceptores

`webservice.interceptors`: `HeaderInterceptor` (encabezados/token), `ConnectivityInterceptor`
(control de conectividad, lanza `NoConnectivityException`), `ChiapasVerificaInterceptor`. El
`NetworkModule` de Hilt añade sus interceptores sobre un cliente con validación TLS estándar; la
ruta `ServiceGenerator` los añade sobre el cliente trust-all (doc 04).

> Nota de alcance: no se ejecutó la app ni se llamó a ningún endpoint. Las rutas se leyeron del
> binario; los cuerpos de request/response reales no se observaron.
