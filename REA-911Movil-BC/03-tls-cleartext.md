# 03 · TLS / Cleartext (Network Security Config)

## NSC decodificado (CONFIRMADO)

El `network_security_config` (compilado en `res/8G.xml`, referenciado desde el manifest) contiene:

```xml
<network-security-config>
  <domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">tijuana.c4bcdev.gob.mx</domain>
    <domain includeSubdomains="true">066movilbc.seguridadbc.gob.mx</domain>
    <domain includeSubdomains="true">911bc.gob.mx</domain>
    <domain includeSubdomains="true">www.seguridadbc.gob.mx</domain>
  </domain-config>
</network-security-config>
```

## Lectura

- **H1 — Cleartext permitido (CWE-319), CONFIRMADO.** La app **permite explícitamente HTTP en
  claro** (`cleartextTrafficPermitted="true"`, `includeSubdomains="true"`) hacia sus 4 dominios de
  backend. En una app de **emergencias/pánico**, cualquier request que caiga en http sobre esos
  dominios viaja sin cifrar (ubicación, alerta de auxilio) y es interceptable/alterable por un MITM
  de red. El `app.config` sí referencia variantes `http://` de esos hosts.
- **H2 — Subdominio de desarrollo en producción (CWE-489), CONFIRMADO.** `tijuana.c4bcdev.gob.mx`
  (`c4bcdev` = dev) está empacado en el build de producción y whitelisteado para cleartext. Fuga de
  infraestructura de pruebas + superficie extra.
- **H3 — Trust-all TLS: AUSENTE-SANO.** No se halló `getUnsafeOkHttpClient`/`ServiceGenerator`/
  `checkServerTrusted` vacío (es RN, no Retrofit nativo). El NSC **no** declara `trust-anchors` con
  CAs de usuario → no confía en CAs instaladas por el usuario. En ese eje, correcto.

## Matiz honesto

El NSC **permite** cleartext pero no **obliga** a usarlo. Confirmar si el tráfico vivo realmente
viaja en http requeriría análisis dinámico (Frida/proxy) — bloqueado por el muro Hermes en
estático. Lo CONFIRMADO: la configuración lo autoriza, y hay referencias `http://` a esos hosts.
