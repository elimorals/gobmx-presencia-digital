# Secretos y llaves en el bundle

Valores reales en `evidence/firebase-keys-endpoints.txt` (no versionado); aquí redactados.

## 6 llaves web de Google/Firebase (`AIzaSy…`)
El bundle incluye ~6 llaves `AIzaSy…` (las `apiKey` de los proyectos Firebase + posibles llaves de
Maps/otros servicios).

- **Matiz importante:** la `apiKey` de un proyecto Firebase/Google **es pública por diseño** —
  identifica el proyecto, no autoriza por sí sola. **No es un secreto** en el sentido clásico.
- **Pero** deben **restringirse** en la consola de GCP (por package name + SHA-1 de firma, y por
  API habilitada). Si no están restringidas, podrían usarse desde otro origen para consumir cuota
  o servicios. El estado de restricción **no es verificable estáticamente**.

## `ApiKey` hex hardcodeado (`ce93108d…`)
Además de las llaves Firebase, hay un **`ApiKey` hexadecimal de 32+ chars** embebido en el bundle
(valor en `evidence/`). A diferencia de las llaves Firebase, **no es una llave pública por diseño**:
es un secreto de aplicación recuperable por cualquiera que abra el APK. Su propósito (qué API/host
autoriza) **no se confirmó** estáticamente — se reporta como secreto embebido, sin inventar su uso.

## Source maps
Los `.map` (doc 02) exponen el código Angular original, incluido dónde se arman estas llaves y
peticiones. Facilita extraer los secretos.

## Recomendaciones (defensivas)
- Mover el `ApiKey` custom fuera del cliente (o convertirlo en token efímero por sesión).
- Restringir las llaves Firebase/Google por package+SHA-1 y por API en GCP.
- No publicar source maps en producción.
- Quitar el proyecto `apprende-prueba` del build de producción (doc 03).
