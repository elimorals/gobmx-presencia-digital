# 02 · Red y endpoints

## Backends confirmados (strings del bundle Hermes)

| Host | Rol |
|------|-----|
| `auth-prod.internetparaelbienestar.mx` | Autenticación (**producción**) |
| `mapp-prod.internetparaelbienestar.mx` | API móvil de la app (**producción**) |
| `www.paynet.com.mx` | Pasarela de pago (PayNet / recargas en efectivo) |

Dominio raíz del servicio: **`internetparaelbienestar.mx`**.

## H3 (MEDIA) — `.preprod` en el package, prod en los endpoints

El package es `com.qvantel.bienestar.preprod` pero **ambos hosts propios son `*-prod`**
(`auth-prod`, `mapp-prod`). Interpretación: se tomó el **build de preproducción** (flavor/variant
`preprod`, que conserva el sufijo en el applicationId) y se **publicó en Google Play** apuntando a
producción. Consecuencias:

- Confusión de entorno: una futura actualización del "flavor prod real" tendría **otro package**
  (`com.qvantel.bienestar` o `.prod`), partiendo la base instalada.
- Riesgo de que el build arrastre flags/paneles/logs de preproducción activos (no confirmado por el
  muro Hermes).

No se halló en claro un host `*-preprod`/`*-qa` activo; la señal es el **nombre del package**, que es
un hecho del manifest. Que además hablara con preprod queda **A-CONFIRMAR** (muro Hermes).

## Pila de red y muro de análisis

- **React Native + Hermes:** la lógica de red vive en `index.android.bundle` (bytecode Hermes). La
  tabla de strings de Hermes concatena cadenas, así que la extracción de rutas `/api/...`, headers y
  secretos **no es fiable** sin decompilar Hermes (p. ej. `hermes-dec`). Se documentan sólo los
  hosts que aparecen como cadenas completas.
- **AWS SDK para JS** embebido: toda la superficie de API de AWS aparece en strings (S3, Cognito,
  SNS, SES, STS…). No implica que todas se usen; sí indica integración con AWS. Qué servicios y con
  qué credenciales = A-CONFIRMAR.
- **Flowable** (motor BPM) embebido: `flowable.startProcess`, `flowable.setVariables`,
  `flowable.setProcessInstanceId` — la app orquesta flujos (altas, portabilidad) contra un backend
  Flowable.
- **Sentry / Crashlytics:** telemetría de errores a terceros.

## Pendiente para confirmar (requiere dinámico o decompilación Hermes)

Rutas exactas de `mapp-prod`, headers de auth, si hay `trustAll` activo en el cliente de red, y si
hay credenciales AWS/API en el bundle. Marcado A-CONFIRMAR en doc 04.
