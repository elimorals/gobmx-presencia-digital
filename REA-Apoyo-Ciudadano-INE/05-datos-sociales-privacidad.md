# 05 — Datos sociales, geolocalización y bitácora

Además de la credencial del ciudadano, la app recolecta **datos del usuario (auxiliar)** de varias
fuentes. Es la cara de privacidad del lado del operador.

## Capa social de Facebook

Modelos propios decompilados en `mx.ine.apoyociudadano.model.SocialLayer.*`:

- `FbData`, `FbProfile`, `FbPicture` — perfil de Facebook.
- `FbEmployer`, `FbWork`, `FbWorks` — **empleador y trabajo**.
- `FbLocation` — **ubicación declarada** en Facebook.

Más `mx.ine.apoyociudadano.activity.SocialSessionActivity` (+ `SocialSessionActivityKt`). En el DEX
aparece `http://graph.facebook.com/` (endpoint del SDK de Facebook). Es decir, la app está preparada
para **vincular la sesión social del auxiliar y leer su perfil, empleador y ubicación de Facebook**.

> Qué se envía y cuándo depende del flujo de registro social (no se trazó el gatillo exacto a cuerpo
> de método). Lo verificable estáticamente: **existe la estructura y el SDK para recolectar perfil,
> empleo y ubicación de Facebook**.

## Twitter

`config.properties` incluye `CONSUMER_K` y `CONSUMER_SK` (consumer key/secret de Twitter), cifradas
junto a los endpoints (doc 04). Confirma integración de login/compartir con Twitter; las llaves de la
app son recuperables al ser la llave AES local (doc 04).

## Geolocalización

Permisos `ACCESS_FINE_LOCATION` + `ACCESS_COARSE_LOCATION` (doc 01). En una app de captación, la
ubicación precisa se asocia típicamente a cada apoyo recolectado (dónde se captó la firma). Liga
**ubicación GPS** con la **credencial del ciudadano** y el dispositivo del auxiliar.

## Estado del teléfono

`READ_PHONE_STATE` permite leer identificadores/estado del dispositivo. Combinado con
`ACCESS_ADSERVICES_AD_ID`, da capacidad de identificar el dispositivo del auxiliar.

## Bitácora a Firebase Firestore

Clases `mx.ine.apoyociudadano.utils.firestore.*`: `FireStoreBitacoraBeta` (+ `BConfig`, `Buffer`,
`RegistroEventoDTO`, varias lambdas `enviar$...`). La app **registra eventos ("bitácora") en Cloud
Firestore**. El `RegistroEventoDTO` y el buffer indican telemetría de eventos de uso; el detalle de
qué campos se registran por evento no se enumeró campo por campo, pero el destino (Firestore de
Google) y el patrón (bitácora con buffer) están confirmados.

> Implicación: hay telemetría de la operación de captación fluyendo a infraestructura de Google
> (Firestore), además del backend del INE. Para una app electoral conviene tenerlo documentado.

## Aviso de privacidad in-app

Existe `PreDataProtectionManifestActivity` (manifiesto de protección de datos) → la app presenta un
aviso de privacidad antes de capturar. Es un control positivo, aunque no sustituye la minimización de
los permisos/telemetría anteriores.
