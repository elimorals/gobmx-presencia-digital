# Arquitectura — Ionic/Angular + Cordova

## Confirmación de pila
- `assets/www/` con `cordova.js` + chunks Angular `*-es2015.js` / `*-es5.js` (dual build) y
  **source maps `.map`** para casi todos. Es una app **Ionic/Angular** empaquetada con **Cordova**.
- No hay DEX propio con la lógica (el DEX es el contenedor Cordova); la lógica de negocio vive en
  el bundle Angular (TypeScript transpilado).

## Módulos (chunks lazy) — amplitud de la plataforma
El nombre de los chunks revela el alcance (PII educativa fuerte):
- `pages-calificaciones-captura-*` — **captura de calificaciones**
- `pages-asistencia-*` — **asistencia** por grupo/modalidad
- `preinscripcion-*`, `pages-matriculacion-*` — **preinscripción / matriculación**
- `pages-salud-formulario-salud-*` — **formulario de salud**
- `pages-tramites-solicitud-personal-rh-*` — **RH docente** (solicitudes de personal)
- `course-content-*`, `pages-child-activity-detail-*` — contenido de cursos / actividades
- `administracion-criterios-acreditacion-*` — administración/acreditación

## Source maps en producción
Se envían los `.map` junto al JS minificado → el **TypeScript Angular original es reconstruible**
(nombres de clases, estructura de servicios, rutas de API). No es una vulnerabilidad por sí misma,
pero **facilita el análisis** y expone la estructura interna; lo recomendable es no publicar
source maps en el build de producción (doc 06).

## Dónde vive la seguridad
Al ser híbrida, el transporte y la identidad se resuelven en el bundle JS (HTTPS + Firebase,
doc 03) y en la config embebida (doc 04). No hay lógica nativa propia relevante que decompilar.
