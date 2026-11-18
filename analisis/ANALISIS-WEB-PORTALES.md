# Análisis estático de portales web gob.mx — secretos y endpoints

**Fecha:** 2026-11-18
**Alcance ético:** descarga y análisis **estático** del **bundle JS público** de cada portal (un
artefacto público, igual que un APK de tienda). Un `GET` de los assets que el navegador ya carga.
**No** hubo crawl, fuzzing, descubrimiento de rutas ni tráfico contra lógica viva.
**Método:** `curl` del `index.html` → extracción de `<script src>` → descarga de los bundles
*first-party* a un directorio aislado → grep de secretos/endpoints. Las libs CDN (jQuery, Bootstrap)
se descartaron.

## Conclusión principal

**La superficie web rinde mucho menos que los APKs.** En la muestra analizada **no se halló ningún
secreto en claro** (API keys, tokens, credenciales) en los bundles públicos. Es el resultado
contrario al de las apps, y la razón es estructural:

- Los portales institucionales estatales son **server-rendered** (ASP.NET/IIS, WordPress, JSF/
  PrimeFaces): no hay bundle de cliente donde filtrar secretos.
- Las pocas SPA modernas (React/Next/Vite) mantienen los secretos **del lado servidor** y, cuando
  hay lógica sensible, el bundle está **ofuscado** (string-array), como la app de Tarjeta Rosa.
- Las apps móviles filtran porque empacan `.env`/llaves en el cliente (`flutter_assets/.env`,
  bundles JS Capacitor). La web, en esta muestra, **no repite ese error**.

> Esto es un hallazgo, no una ausencia de trabajo: confirma que el vector de fuga de secretos en
> gob.mx está en las **apps**, no en los **portales**.

## Muestra (portales probados)

| Portal | HTTP | Pila | Resultado |
|--------|------|------|-----------|
| app-tarjeta-rosa.guanajuato.gob.mx | 200 | **Vite SPA + AWS Amplify/Cognito**, ofuscado | endpoint `api-ciudadano.guanajuato.gob.mx`; sin secretos en claro |
| transparencia.durango.gob.mx | 200 | Next.js | solo hosts propios de transparencia; sin secretos |
| llave.cdmx.gob.mx | 200 | JSF / PrimeFaces 11 | server-rendered; scripts de validación/2FA; sin secretos |
| www.ventanillabc.bajacalifornia.gob.mx | 200 | HTML + libs | widget Zendesk (key pública por diseño); contador de visitas de tutorial |
| www.app.cdmx.gob.mx | 200 | landing + jQuery/Bootstrap | sin bundle propio jugoso |
| www.aforemovil.com | 200 | sitio estático | sin secretos |
| plataformadigital.sej.jalisco.gob.mx | 200 | HTML + jQuery | sin SPA real |
| idigital.bajacalifornia.gob.mx | 200 | landing mínima | sin bundle |
| tramites.ebajacalifornia.gob.mx | **403** | — | **WAF** bloquea GET automatizado |
| epagos.aguascalientes.gob.mx | **401** | — | requiere auth |
| micuenta.infonavit.org.mx | **403** | — | **WAF** |
| tesoreriacdmx.finanzas.cdmx.gob.mx | **403** | — | **WAF** |

## Hallazgos (modestos, pero reales)

1. **Divulgación de arquitectura — Tarjeta Rosa (web).** El SPA de
   `app-tarjeta-rosa.guanajuato.gob.mx` está **ofuscado** y empaca el **AWS SDK** (`vendor-aws`):
   usa **AWS Amplify/Cognito** directo desde el navegador y pega a
   `api-ciudadano.guanajuato.gob.mx/comunicaciones`. Los `IdentityPoolId`/`UserPoolId`/región
   visibles en el chunk son **nombres de propiedad del SDK**, no los valores configurados (esos no
   aparecen en claro por la ofuscación). **No se confirma** fuga de pool IDs. Cruza con la app
   `gob.guanajuato.TarjetaRosa` (mismo programa estatal).
2. **Clave de widget de terceros en HTML — ventanilla BC.** `static.zdassets.com/...key=2f1c12f6-…`
   (Zendesk). Es **pública por diseño** (widget de chat); se anota por completitud, no es un secreto.
3. **Código de tutorial en producción — ventanilla BC.** `contador.js` es un contador de visitas
   copiado de un blog (parzibyte) que postea a `contador/registrar_visita.php`. Higiene menor.
4. **Buena postura (WAF/geofencing).** Los portales **sensibles** (pagos, Infonavit, tesorería, BC
   trámites) devuelven 401/403 a un GET automatizado — bloquean esta vía. Es correcto, y es a la vez
   un **límite del método**.

## Fingerprint de pila por portal (enriquece el inventario)

- **JSF / PrimeFaces 11:** llave.cdmx.gob.mx (identidad CDMX).
- **ASP.NET / IIS:** mayoría de portales estatales institucionales.
- **Next.js:** transparencia.durango.gob.mx.
- **Vite + AWS Amplify (ofuscado):** app-tarjeta-rosa.guanajuato.gob.mx.

## Límites honestos de este análisis

- **Muestra:** ~12 portales de 394 del inventario. El catálogo está dominado por portales
  **institucionales server-rendered** (sin bundle de cliente), así que ampliar la muestra
  probablemente confirma el mismo resultado, no lo cambia.
- **Geofencing/WAF:** varios portales sensibles bloquean GET desde el sandbox (US). Lo que esté
  detrás de esos WAF no se pudo evaluar por esta vía.
- **Ofuscación:** las SPA ofuscadas (Tarjeta Rosa) son un muro para el estático, igual que
  Flutter/Veritran en apps. Confirmar config requeriría análisis dinámico en navegador.

## Qué NO se hizo (y por qué)

No se hizo crawl activo, descubrimiento de rutas ocultas, ni prueba de endpoints vivos: eso es
escaneo de infraestructura de terceros, fuera del alcance ético de este proyecto (análisis de
artefactos públicos), con o sin autorización reclamada.
