# Red y endpoints

Recuperados del DEX (y del `<clinit>` de `ServiceGenerator`, que fija el `BASE_URL`).

## Backends propios (`licenciamanejo.com` — dominio del proveedor Cosmocolor)

| Endpoint | Rol | Nota |
|---|---|---|
| `https://api1.licenciamanejo.com/multi/appmov/` | **API principal** (BASE_URL) | TLS **anulado** por `createService` (doc 02) |
| `http://impresion.licenciamanejo.com/objetos` | objetos / impresión de la licencia | **CLEARTEXT HTTP** |
| `https://faceapi.licenciamanejo.com/` | verificación facial | pasa por el cliente con TLS anulado |

## Verificación de identidad — Regula Forensics
- `https://api.regulaforensics.com`, `https://faceapi.regulaforensics.com`,
  `https://software.regulaforensics.com/mobile_sdk_db/` → SDK **Regula** (lectura/validación de
  documento de identidad + comparación facial). Lee el documento de la licencia y el rostro.

## Residuos cross-estado (fuga de infraestructura)
En el binario de **Jalisco** aparecen endpoints de **otros estados** — confirma el white-label y
filtra infraestructura ajena:
- `https://api2.licenciamanejo.com/edomex/web/noticias/edomex` → **EDOMEX**
- `https://prod.cosmokeys.edomex.licenciamanejo.com` → "cosmokeys" **EDOMEX** (gestión de llaves)
- `https://chiapas-verifica-infraccion-ty3qpufnqq-uc.a.run.app/` → **Chiapas** (Cloud Run)

(EDOMEX ya mostraba residuos de Chiapas; aquí Jalisco muestra residuos de EDOMEX y Chiapas — el
build mezcla config de varios despliegues.)

## Transporte (veredicto)
- **TLS desactivado** en el cliente principal (doc 02) → el `https://` de `api1`/`faceapi` **no
  protege**.
- **Cleartext** explícito en `impresion.licenciamanejo.com`.
- Interceptor de **logging a nivel BODY** activo en el `OkHttpClient` (doc 02) → el cuerpo de las
  peticiones/respuestas se registra en logcat; con datos de licencia/PII, es exposición adicional
  en el dispositivo.
