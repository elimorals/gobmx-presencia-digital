# Red — Network Security Config y endpoints

## El NSC (hallazgo central)

`res/xml/network_security_config.xml` (en `evidence/`):

```xml
<network-security-config>
    <debug-overrides>
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </debug-overrides>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </base-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">132.147.100.89</domain>
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </domain-config>
</network-security-config>
```

### H1 (Medio) — cleartext global + confianza en CAs de usuario

- **`<base-config cleartextTrafficPermitted="true">`** — a diferencia de CONALITEG (que restringía
  el cleartext a una lista de dominios), aquí el `base-config` aplica a **TODOS** los dominios: la
  app **permite HTTP en claro con cualquier host**.
- **`<certificates src="user" />`** en el `base-config` → la app **confía en CAs instaladas por el
  usuario** para todo el tráfico. En Android 7+ el valor por defecto es **no** confiar en ellas.
  Consecuencia: **MITM trivial** — basta instalar una CA (red corporativa/cautiva o dispositivo
  comprometido) y la app aceptará el certificado, incluido el tráfico del **InAppBrowser de pago**
  (doc 03).
- **IP hardcodeada `132.147.100.89`** con cleartext explícito → un backend referenciado por IP, en
  claro; fuga de infraestructura + superficie de MITM adicional.
- El `debug-overrides` con `user` solo aplica en builds depurables (menos relevante).

**Fix:** `cleartextTrafficPermitted="false"`, quitar `<certificates src="user" />`, eliminar la IP,
y pinnear los dominios propios (`cespt.gob.mx`).

## Endpoints (de `decompiled/endpoints.txt`)

Todos HTTPS, bajo `www.cespt.gob.mx` salvo la identidad de Llave BC:
- API: `https://www.cespt.gob.mx/API/g/v...`
- Login identidad: `https://www.cespt.gob.mx/identidaddigital/loginidentidad.aspx`
- Pago: `https://www.cespt.gob.mx/pago/pcm.aspx` (doc 03)
- Citas: `https://www.cespt.gob.mx/servicioscespt/citasweb_generacita.aspx`
- Identidad (OAuth): `https://www.llave.bajacalifornia.gob.mx/oauth.xhtml`
- Mapas: `https://maps.googleapis.com/maps/api/geocode/json` (con API key embebida, doc 05)

> Los endpoints propios usan HTTPS; el problema **no** es que la app llame HTTP, sino que su NSC
> lo **permitiría** y confía en CAs de usuario, debilitando el transporte de todo (incl. pago).
