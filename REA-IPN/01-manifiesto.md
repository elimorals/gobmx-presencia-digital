# Manifiesto — permisos y Network Security Config

Fuente: `AndroidManifest.xml` decodificado. versionName 2.2.2, compileSdk 35.

## Permisos

| Permiso | Para qué (inferido) |
|---|---|
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | ubicación (mapas de planteles / servicios) |
| `INTERNET`, `ACCESS_NETWORK_STATE` | red |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE`, `READ_GSERVICES` | notificaciones push (FCM) |
| `READ_EXTERNAL_STORAGE`, `WAKE_LOCK`, `VIBRATE` | estándar |

Conjunto **acotado** para una app institucional. No pide cámara, micrófono, contactos ni teléfono.

## Network Security Config (el punto débil, doc 05)

`evidence/network_security_config.xml`:

```xml
<network-security-config>
    <domain-config>                         <!-- con PINNING -->
        <domain includeSubdomains="true">www.ipn.mx</domain>
        <domain includeSubdomains="true">www.broadcast.radio.ipn.mx</domain>
        <trust-anchors>
            <certificates src="@raw/certipnder" />
            <certificates src="@raw/ipnmx" />
            <certificates src="@raw/radioipnbase64" />
            <certificates src="system" />
        </trust-anchors>
    </domain-config>
    <base-config cleartextTrafficPermitted="true" />   <!-- ⚠️ cleartext global -->
</network-security-config>
```

**Dos problemas:**

1. **`base-config cleartextTrafficPermitted="true"`** — por defecto, **HTTP en claro permitido**
   para todos los dominios no listados, incluido el backend sensible `auth.dsi.ipn.mx` (auth, NSS,
   pagos). En la práctica la app usa HTTPS, pero la política **permitiría** un downgrade a HTTP.
2. **Pinning con prioridad invertida** — se pinnean los certificados de `www.ipn.mx` y el dominio de
   **radio** (contenido de marketing/streaming), pero **no** `auth.dsi.ipn.mx`, que es donde viajan
   la credencial, el NSS y los pagos. Se protege lo menos sensible y se deja lo más sensible sin
   pinning.

Lo correcto sería: quitar el cleartext global y pinnear `auth.dsi.ipn.mx` (o al menos moverlo a un
`domain-config` sin cleartext y con sus CAs).
