# Hallazgos de seguridad (priorizados)

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se pagó ni se llamó a ningún
endpoint. Contexto: app de servicios de agua con **login + pago** (el pago delegado a Bancomer).

## H1 (Medio) — NSC: cleartext global + confianza en CAs de usuario
El `network_security_config` permite `cleartextTrafficPermitted="true"` en el **`base-config`**
(todos los dominios) y confía en `<certificates src="user" />` globalmente (doc 04). → **MITM
trivial** instalando una CA, sobre todo el tráfico, incluido el **InAppBrowser de pago** (doc 03).
Más una IP hardcodeada con cleartext (`132.147.100.89`). **Fix:** cleartext `false`, quitar CAs de
usuario y la IP, pinnear `cespt.gob.mx`.

## H2 (Bajo) — Google Maps API key embebida
`AIzaSy…` en el bundle JS (doc 05). Debe restringirse por package+SHA-1/referer. **Fix:** aplicar
restricciones de la key en Google Cloud Console.

## H3 (Bajo) — `allowBackup="true"`
Estado de la app respaldable (doc 01). Mitigado en parte por `BackupWebStorage="none"`. **Fix:**
`allowBackup="false"`.

## H4 (Bajo / higiene) — `appId` default `io.ionic.starter`
No personalizaron el identificador de Capacitor (doc 02). No es vulnerabilidad; delata plantilla
sin ajustar.

## Positivos

- **P1 — Pago delegado a Bancomer vía InAppBrowser.** La tarjeta se captura en la página del
  banco, no en el JS; sin datos de tarjeta ni claves de pasarela embebidos (doc 03).
- **P2 — Identidad delegada a Llave BC (OAuth).** Sin credenciales de identidad propias en el
  cliente (doc 02).
- **P3 — Endpoints propios HTTPS** (`cespt.gob.mx/API`) (doc 04).
- **P4 — Permisos acotados** (doc 01).

## Límites (sin inventar)
- La lógica JS troceada no se recorrió pantalla por pantalla; se confirmó la arquitectura, los
  endpoints, el flujo de pago (InAppBrowser→Bancomer) y la config de red, no cada callback.
- El comportamiento del InAppBrowser (restricción de origen/navegación) no es verificable sin
  ejecutar.
- La restricción de la Maps key y la autorización real de la API viven en el servidor/consola, no
  en el APK.
- Firma re-firmada por APKPure → conclusiones de arquitectura, no de integridad.
