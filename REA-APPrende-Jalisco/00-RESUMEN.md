# Resumen — APPrende Jalisco 2.0.5 (`mx.gob.jalisco.edu.apprende`)

## Identidad
- **App:** APPrende — Secretaría de Educación Jalisco (SEJ). Plataforma educativa: calificaciones,
  asistencia, preinscripción/matriculación, trámites, **formulario de salud**, RH docente,
  contenido de cursos. Usuarios: alumnos, docentes, padres.
- **Package:** `mx.gob.jalisco.edu.apprende` · **versión** 2.0.5 (versionCode 128). compileSdk 35.
- **#36** del inventario por instalaciones (≈100 K). **Firma:** re-firmada por APKPure.

## Pila
**Ionic/Angular + Cordova** (híbrida). Bundle Angular en `assets/www/` (chunks `*-es2015.js`/
`*-es5.js` **con `.map`**), Cordova como contenedor. La lógica y la config viven en el JS — no
hay DEX propio con la lógica.

## Backends (HTTPS, `*.jalisco.gob.mx`)
- `https://api2plataformaeducativa.jalisco.gob.mx` (API principal)
- `https://plataformadigital.sej.jalisco.gob.mx/sass`
- `https://mi.sej.jalisco.gob.mx` (registro alumno, "mimuro", ayuda)
- `https://g.plataformaeducativa.jalisco.gob.mx` (estáticos)

## Identidad / Firebase
- **Firebase Auth** (los códigos `invalid-credential`/`weak-password`/… confirman el SDK de Auth).
- **Firebase Realtime Database** (`databaseURL` `*.firebaseio.com`).
- **Varios proyectos:** `apprende-prod`, **`apprende-prueba`** (prueba, en prod), `geniat-recreajalisco`,
  `ingeniat-comunication`. 6 llaves web `AIzaSy…` + `ApiKey` hex custom.

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Transporte | ✅ HTTPS a dominios oficiales; sin cleartext |
| Identidad | ✅ Firebase Auth (sin credenciales propias en el cliente) |
| Firebase | ⚠️ Proyecto de prueba en prod + RTDB (reglas no verificables) |
| Secretos | ⚠️ `ApiKey` hex hardcodeado + 6 llaves web expuestas |
| Código | ⚠️ Source maps en producción |
| Firma | ⚠️ Re-firmada por APKPure |

**Lectura:** transporte e identidad **bien resueltos** (HTTPS + Firebase Auth). El riesgo se
concentra en **Firebase** (proyecto de prueba embebido + reglas RTDB no auditables) y en los
**secretos del bundle**. Dado el dato que maneja (salud, calificaciones), merece revisar las
reglas de Firebase del lado servidor.
