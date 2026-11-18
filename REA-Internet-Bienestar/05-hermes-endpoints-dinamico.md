# 05 · Decompilación Hermes + intento dinámico

**Método:** extracción del string table del bundle Hermes (`assets/index.android.bundle`, HBC v94,
7.3 MB) + intento de MITM dinámico en device Motorola XT2435-3 (Android 16, sin root). Rompe
parcialmente el muro Hermes que el `strings` crudo no ató.

## Topología de backend revelada (NUEVO vs. análisis estático inicial)

| Host | Rol | Nota |
|------|-----|------|
| `auth-prod.internetparaelbienestar.mx/auth/realms/consumer` | **Keycloak OIDC** (identidad) | realm `consumer` |
| `mapp-prod.internetparaelbienestar.mx` | API de producción | dominio gov-facing |
| `mapp-altan-prod.qvantel.solutions` | API prod del **vendor Qvantel** | fuera de `gob.mx` |
| **`mapp-altan-dev.qvantel.systems`** | **endpoint DEV embebido en el build de producción** | **CWE-489 confirmado** |
| `digitalexpress.mobileapp.qvantel.com/callback` | callback Qvantel | vendor |

**Pago / PII:** tokenización de tarjeta vía **OpenPay** (`getOpenPayCardToken`,
`getOpenPayDeviceSessionId`), **OXXO Pay** y **Paynet**; escáner de tarjeta; `dateOfBirth`,
`saveCreditCard`. Analytics: Firebase (analytics + crashlytics). El bundle empaca el **AWS SDK**
completo (metadatos de API visibles) — uso probable de servicios AWS (Cognito/S3) a confirmar.

## Lo que el dinámico NO pudo (honesto)

- **MITM sin root:** el proxy intercepta (Google Play Integrity rechazó el cert de mitmproxy, como
  debe), pero la app quedó en **pantalla blanca** y **no emitió su backend por el proxy** en el
  arranque. **`trustAll` NO confirmado** — es solo un nombre de propiedad en el bytecode, no se ató
  a un cliente de red activo. Pista débil en contra (no aceptó el cert de MITM para una llamada
  exitosa), pero no es prueba.
- **Secretos:** no se hallaron valores de API key/token en claro en el string table (solo nombres
  de propiedad `ApiKeySource`/`authorizationToken`/`Bearer`, que son del SDK). Sin confirmación de
  secreto embebido.
- Confirmar el uso real de `trustAll` o leer secretos de runtime requeriría **root** (frida-server)
  o compilar un engine de instrumentación — fuera de alcance en este device sin root.

## Cruce con el mapa

- **Clúster G (dev/test en prod):** suma `mapp-altan-dev.qvantel.systems`.
- **Proveniencia:** infra crítica (identidad Keycloak, pagos) en dominios del **vendor Qvantel/Altan
  fuera de `gob.mx`** — consideración de gobernanza de datos, no de seguridad per se.
- Package `.preprod` (ya documentado) + endpoint `-dev` embebido = **confusión de entorno** confirmada.
