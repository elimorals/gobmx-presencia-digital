# Arquitectura Flutter — el muro (y lo que sí se recuperó)

## Confirmación

- `assets/flutter_assets/` presente; `libapp.so` (Dart AOT) + `libflutter.so` (**engine 3.5.3
  stable**) en el split de arquitectura. Triage preliminar correcto: **es Flutter**.
- Sin `.env` empaquetado (solo `AssetManifest.json`) → la config/backend va dentro del snapshot Dart.

## El muro

La lógica de negocio (login OIDC, manejo de tokens, consulta de credencial/NSS, flujo de pago BAZ,
UI) se compila en el **snapshot Dart AOT** dentro de `libapp.so`. JADX solo ve el embedding Flutter;
no hay bytecode Java/Kotlin con esa lógica. Recuperarla requiere **reflutter/blutter** o **Frida**
(dinámico, fuera de alcance estático).

## Lo que SÍ se recuperó (sin romper el muro)

A diferencia de muchas apps Flutter, el `libapp.so` de IPN **deja los endpoints en texto plano**
(recuperables con `strings`). Esto permitió reconstruir **todo el mapa de backend** sin ejecutar la
app (doc 03, `decompiled/endpoints.txt`): dominio `auth.dsi.ipn.mx`, realm Keycloak, broker Azure AD,
rutas Karaf de credencial/NSS/pagos, y el dominio del proveedor `mycampusdigital.com`.

> Qué NO revela el `libapp.so`: cómo se construyen los cuerpos de las peticiones, qué campos de PII
> se envían en cada una, cómo se guardan los tokens localmente, ni el detalle del flujo de pago.
> Eso vive en la lógica Dart compilada → se marca como límite, sin inventar.
