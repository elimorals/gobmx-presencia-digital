# 03 · TLS y cleartext

## H2 (MEDIA) — `usesCleartextTraffic="true"` (CONFIRMADO)

El manifest declara `android:usesCleartextTraffic="true"` (verificado con `aapt2 dump xmltree`). En
una app de **telecom + pagos** que maneja MSISDN, ICCID, OTP por SMS y pagos vía PayNet, permitir
HTTP en claro es un riesgo: cualquier endpoint o recurso que caiga a `http://` viaja sin cifrar.

No se observó un `network_security_config` en el manifest que restrinja el cleartext a hosts
concretos (a diferencia de otras apps del corpus). Con `targetSdk 35`, el default sería bloquear
cleartext; el atributo en `true` lo **reactiva globalmente**.

Los hosts propios confirmados son `https://` (`auth-prod`, `mapp-prod`), así que el cleartext podría
ser para recursos/terceros; **cuáles** van por HTTP no se pudo enumerar (muro Hermes).

## H4 (A-CONFIRMAR) — `trustAll` en el bundle

La tabla de strings del bundle Hermes contiene la cadena **`trustAll`** (junto a `rejectUnauthorized`
y nombres de propiedad TLS). Es coherente con un patrón de **desactivación de validación TLS** como
el visto en otras apps del corpus (`getUnsafeOkHttpClient`/`ServiceGenerator`). **PERO:** en React
Native/Hermes no se pudo atar `trustAll` a una ruta de red activa ni determinar si está en código
propio o en una dependencia inerte. Se reporta como **bandera a confirmar**, NO como MITM demostrado.

Confirmarlo requiere decompilar el bytecode Hermes (`hermes-dec`) o análisis dinámico (Frida sobre
el cliente de red de RN).

## Postura

- CONFIRMADO: cleartext global habilitado.
- A-CONFIRMAR: desactivación de validación TLS (`trustAll`).
- No se halló certificate pinning en los strings, pero su ausencia tampoco se confirma por el muro.
