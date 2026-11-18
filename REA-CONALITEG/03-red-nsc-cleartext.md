# Red — Network Security Config: cleartext + CAs de usuario

El archivo `res/xml/network_security_config.xml` (decodificado, en
`evidence/network_security_config.xml`) es el hallazgo central. Un único `<domain-config>`:

```xml
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">api.conaliteg.gob.mx</domain>
    <domain includeSubdomains="true">middleware.conaliteg.gob.mx</domain>
    <domain includeSubdomains="true">libros.conaliteg.gob.mx</domain>
    <domain includeSubdomains="true">secundaria.conaliteg.gob.mx</domain>
    <domain includeSubdomains="true">milesdehistorias.conaliteg.gob.mx</domain>
    <domain includeSubdomains="true">pruebasistemas.conaliteg.gob.mx</domain>
    <domain includeSubdomains="true">200.94.149.205</domain>
    <domain includeSubdomains="true">200.94.149.206</domain>
    <domain includeSubdomains="true">200.94.149.207</domain>
    <domain includeSubdomains="true">35.190.15.47</domain>
    <domain includeSubdomains="true">192.168.101.6</domain>
    <domain includeSubdomains="true">10.0.2.2</domain>
    <domain includeSubdomains="true">127.0.0.1</domain>
    <domain includeSubdomains="true">localhost</domain>
    <!-- + dominios de terceros: FCE, UPN, UNAM, Grupo SM, Trillas, … -->
    <trust-anchors>
        <certificates src="system" />
        <certificates src="user" />
    </trust-anchors>
</domain-config>
```

## H1 (Medio) — Cleartext HTTP a backends de producción

`cleartextTrafficPermitted="true"` aplica a **todos** los dominios listados, incluidos los de
**producción** de la CONALITEG (`api`, `middleware`, `libros`, `secundaria`, `milesdehistorias`).
Es decir, **el tráfico a la API y al middleware puede ir por HTTP sin TLS**. Un atacante en la
red puede leer o alterar el contenido/catálogo y la telemetría de navegación.

## H2 (Medio) — Confianza en CAs del usuario (`src="user"`)

Los `trust-anchors` incluyen `<certificates src="user" />`. Esto hace que la app **confíe en
certificados firmados por CAs instaladas por el usuario**, cuando el valor por defecto en
Android 7+ es **no** confiar en ellas. Consecuencia: el **MITM es trivial** — basta con instalar
una CA (un escenario común en redes corporativas/cautivas o con un dispositivo comprometido) y
la app aceptará el certificado. Suele dejarse para depuración con proxies (Charles/Burp) y **no
debería quedar en producción**.

## H3 (Bajo) — Residuos de desarrollo en el NSC de producción

El NSC de producción lista:
- `192.168.101.6` — **IP privada** de una LAN de desarrollo,
- `10.0.2.2` — loopback del **emulador** de Android,
- `127.0.0.1` / `localhost`,
- `pruebasistemas.conaliteg.gob.mx` — un **subdominio de pruebas**.

Son artefactos de un entorno de desarrollo que quedaron embebidos en el build publicado —
fuga menor de información interna (nombres de host/IP) y señal de que el NSC se armó para pruebas
y no se limpió para producción.

## Backends y content providers (del NSC + DEX)

- **CONALITEG:** `api`, `middleware`, `libros`, `secundaria`, `milesdehistorias`
  (`*.conaliteg.gob.mx`) + IPs `200.94.149.205/206/207` y `35.190.15.47` (GCP).
- **Terceros (contenido):** `docs.fondodeculturaeconomica.com`, `biblioteca.ajusco.upn.mx`,
  `editorial.upnvirtual.edu.mx`, `www.librosoa.unam.mx`, `www.revistas.unam.mx`,
  `guiasdigitales.grupo-sm.com.mx`, `appstrillas.mx`, `www.cervantesvirtual.com`,
  `academia.org.mx`, `www.fonotecanacional.gob.mx`, `bit.ly`, `ajax.googleapis.com`.

## Nota (sin sobre-afirmar)

En el DEX aparecen referencias a `HostnameVerifier` y `checkServerTrusted`, pero son los
símbolos estándar de las librerías de red (OkHttp/JSSE); **no se confirmó** un `TrustManager`
trust-all personalizado como el de Infonavit/Licencia EDOMEX. La debilidad confirmada es la del
NSC (cleartext + CAs de usuario), no una desactivación de validación en código. Se marca así,
sin inventar.
