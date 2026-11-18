# 04 · Hallazgos consolidados

| # | Hallazgo | CWE | Severidad | Estado |
|---|----------|-----|-----------|--------|
| H1 | Permisos privilegiados de operador (SMS, call log, phone state privilegiado, APN, carrier services) + ubicación background, en app de 158k | CWE-250 / CWE-926 | **ALTA** | CONFIRMADO |
| H2 | `usesCleartextTraffic="true"` global en app de telecom/pagos | CWE-319 | **MEDIA** | CONFIRMADO |
| H3 | Build `.preprod` publicado en producción (package preprod → endpoints prod) | CWE-489 (higiene) | **MEDIA** | CONFIRMADO |
| H4 | `trustAll` presente en el bundle Hermes | CWE-295 | — | **A-CONFIRMAR** |
| H5 | Nombres de propiedad de secreto (`ApiKey`/`Bearer`) + AWS SDK embebido; valores no confirmables | CWE-798 | — | **A-CONFIRMAR** |

## Lo confirmado

1. **Sobre-permisología privilegiada (H1):** el riesgo mayor. `READ_SMS` + `READ_CALL_LOG` +
   `READ_PRIVILEGED_PHONE_STATE` + `MODIFY_PHONE_STATE` + `WRITE_APN_SETTINGS` +
   `BIND_CARRIER_SERVICES` exceden por mucho lo que un autoservicio de cliente necesita, y amplían la
   superficie de ataque a todo el SMS/registro de llamadas del usuario.
2. **Cleartext habilitado (H2)** en una app que mueve MSISDN/ICCID/OTP/pagos.
3. **Entorno preproducción en Play (H3):** el package `.preprod` de una app de 158k.

## Lo que el muro Hermes impidió confirmar (honesto)

- Si `trustAll` desactiva realmente la validación TLS en una ruta activa (H4).
- Credenciales AWS/API embebidas y su valor (H5).
- Rutas exactas de `mapp-prod` y headers de autenticación.
- Qué recursos concretos viajan por cleartext.

**Siguiente paso técnico** (fuera del estático): decompilar el bytecode Hermes (`hermes-dec`) o Frida
sobre el cliente de red de React Native para resolver H4/H5.

## Cruce con el mapa de proveedores

- **Pila nueva en el corpus:** primera app **React Native/Hermes** con muro de bytecode — se suma a
  los muros Flutter/Veritran/.NET del mapa (`analisis/MAPA-PROVEEDORES-SDK.md`).
- **Vendor Qvantel** (BSS telecom finlandés): no visto antes en el corpus.
- El patrón `trustAll` (A-confirmar) y el cleactext encajan con el clúster D del mapa.
