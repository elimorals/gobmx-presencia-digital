# 02 · Red, TLS y hallazgos

## Endpoints (todos HTTPS)

| Endpoint | Rol |
|---|---|
| `https://c5.hidalgo.gob.mx/Extorsion/api/telefonos/` | descarga la **lista de números de extorsión** |
| `https://c5.hidalgo.gob.mx/Extorsion/api/alertAnalytics/` | analítica/reporte de alertas |
| `https://nomasextorsion-4d133.firebaseio.com` | Firebase Realtime Database |
| `https://s-seguridad.hidalgo.gob.mx/pag/aviso` | aviso de privacidad |

Backends oficiales del **C5 Hidalgo** (`c5.hidalgo.gob.mx`, `s-seguridad.hidalgo.gob.mx`), todos por
HTTPS.

## TLS — AUSENTE-SANO (verificado con evidencia)

- **Sin trust-all:** un barrido del DEX **no** halló `getUnsafeOkHttpClient`, `TrustAllCerts`,
  `checkServerTrusted` vacío ni `HostnameVerifier` permisivo. No es el defecto Infonavit/Cosmocolor.
- **Sin cleartext:** `usesCleartextTraffic=false`, sin `network_security_config`, targetSdk 36. No
  repite el patrón de 911-CDMX.
- **Sin secretos sensibles:** no se hallaron `AKIA`/`sk_live`/claves privadas/`password=` en claro;
  sin IPs internas RFC1918. La única llave es la **API key de Firebase** (`AIzaSy…`), presente por
  diseño y restringible por package/firma.

## A-CONFIRMAR (honesto)

- **Firebase RTDB `nomasextorsion-4d133.firebaseio.com`:** las **reglas de acceso** del RTDB no son
  verificables por análisis estático (son config del lado servidor) y **no se probaron** (sería
  acceso activo, fuera de alcance). Si las reglas estuvieran abiertas, la lista/reportes podrían
  quedar expuestos — pero **no se afirma**; queda como punto a revisar por el C5.

## Cruce con el mapa de proveedores

- **Contraejemplo limpio:** app de seguridad estatal con red correcta. Refuerza que el defecto
  cleartext/trust-all **no es universal** en apps de seguridad — depende del despliegue de cada
  dependencia (contrasta con 911-CDMX, 911Móvil BC y Veracruz Mujer Alerta, que sí lo tienen).
- Firma auténtica del C5i Hidalgo (no re-firmada), como Tuzobús (Hidalgo) — el estado firma sus apps
  con certificados propios.
