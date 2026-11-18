# 02 · Red, endpoints y TLS

## Backend (único, HTTPS)

Toda la API cuelga de `https://alertac.c4ver.gob.mx/resources/mobile/` (backend **PHP**). Endpoints
en `model.io.JsonServices` (14 URLs, todas construidas desde `URL_BASE` HTTPS):

| Campo | Endpoint |
|-------|----------|
| URL_LOGIN / LOGOUT / REGISTER | `login.php` / `logout.php` / `register.php` |
| URL_START_ALERT / END_ALERT | `startalert.php` / `closealert.php` |
| URL_SEND_ALERT_INFO | `sendinfo.php` |
| URL_UPDATE_LOCATION | (actualización de ubicación durante la alerta) |
| URL_ALERTS / ALERT_TYPES | `alerts.php` / `typealert.php` |
| URL_ACCESS_CODE | `access_code.php` |
| URL_PROFILE / EDIT_PROFILE | `profile.php` / (editar) |

`JsonServices` solo expone getters `java.net.URL`; **no configura SSL** (no instala
`SSLSocketFactory`/`TrustManager`/`HostnameVerifier` propios).

## TLS — AUSENTE-SANO (verificado, no "no buscado")

- **Sin cleartext:** cero endpoints `http://` de backend; sin `usesCleartextTraffic`; sin NSC;
  targetSdk 36 (cleartext bloqueado por defecto).
- **Sin trust-all propio:** no existe ninguna clase `mx.gob.veracruz.*` que implemente
  `X509TrustManager`/`HostnameVerifier` ni que llame a un bypass. Búsqueda dirigida = vacía.
- **Matiz honesto (Apache lib):** el DEX contiene `org.apache.http.conn.ssl.AllowAllHostnameVerifier`
  y los símbolos `checkServerTrusted`/`getAcceptedIssuers` — pero son **de la librería Apache
  HttpClient empacada**, no de la app. No se halló que el código propio instancie `ALLOW_ALL` ni lo
  cablee a un cliente. Clasificado AUSENTE-SANO con esa salvedad (confirmación 100% del runtime
  requeriría dinámico).

## Cruce con el mapa de proveedores

- **Rompe el "patrón Veracruz":** SSP Veracruz (Mujer Alerta) y SEV Empleado tienen cleartext
  confirmado; este **C4 Veracruz es sano**. → el cleartext de Veracruz es **por despliegue de
  dependencia**, no una política estatal uniforme.
- **Hipótesis pánico→cleartext:** otro **contraejemplo** (app de alerta, HTTPS correcto).
