# Firebase y backends

## Backends propios (SEJ) — HTTPS
| Host | Rol (inferido) |
|---|---|
| `https://api2plataformaeducativa.jalisco.gob.mx` | API principal de la plataforma |
| `https://plataformadigital.sej.jalisco.gob.mx/sass` | plataforma digital SEJ |
| `https://mi.sej.jalisco.gob.mx` | registro de alumno, "mimuro", ayuda |
| `https://g.plataformaeducativa.jalisco.gob.mx` | estáticos (logos/imágenes) |

Todos **HTTPS** sobre dominios oficiales `jalisco.gob.mx`/`sej`. Sin `http://` propio, sin
cleartext (doc 01). Transporte **sano**.

## Firebase
- **Auth:** el bundle trae los códigos de error de Firebase Auth (`invalid-credential`,
  `weak-password`, `invalid-custom-token`, `PASSWORD_RESET`…) → identidad delegada a **Firebase
  Authentication**. No se observó manejo de credenciales propias en el cliente (positivo).
- **Realtime Database:** `databaseURL` apuntando a `*.firebaseio.com` → uso de **Firebase RTDB**.
- **Varios proyectos** (config completa en `evidence/`):
  - `apprende-prod` (producción) — `apprende-prod.firebaseapp.com`, RTDB `apprende-prod-default-rtdb`.
  - **`apprende-prueba`** (PRUEBA) — `apprende-prueba.firebaseapp.com`, RTDB `apprende-prueba-default-rtdb`.
  - `geniat-recreajalisco`, `ingeniat-comunication` (servicios asociados).
  - 3 `messagingSenderId` distintos → 3+ proyectos Firebase.

### Observaciones
1. **Proyecto de PRUEBA en producción (Medio).** `apprende-prueba` embebido en el build público:
   fuga de infraestructura y superficie extra. Debería quedar solo producción (doc 06).
2. **Reglas de RTDB no verificables (pregunta abierta).** La seguridad de los datos en RTDB
   (calificaciones, asistencia, salud) depende de las **reglas de seguridad del lado servidor**,
   que **no están en el cliente**. Si fueran laxas (lectura/escritura abierta), habría exposición
   de PII educativa. **No se puede afirmar estáticamente**; se marca como superficie a revisar por
   la SEJ. No se inventa el estado de las reglas.
