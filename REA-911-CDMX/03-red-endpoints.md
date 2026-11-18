# 03 — Red y endpoints

Backend **WCF .NET** (servicios `.svc` + algunas APIs REST `/api/`) alojado en
`apps.c5.cdmx.gob.mx`. Los endpoints están declarados como constantes en
`com.desarrollo.c5.app_066_android_2.utils.DataRegister` (34 campos: `SERVER_URL2`,
`SERVER_EMERGENCIAS_URL`, `CAD_URL`, `ALERT_EMERGENCY`, `SIGUEME`, `REGISTRO_FICHA_SALUD`,
`VALIDAR_NUMERO`, …) y confirmados por strings del DEX. Lista completa en
[`decompiled/endpoints.txt`](decompiled/endpoints.txt).

## Hallazgo central: TODO en HTTP en claro

**Todas** las rutas del backend de emergencias usan `http://` (no `https://`). Esto, con
`usesCleartextTraffic="true"` y Volley por defecto (doc 02), significa que no hay
seguridad de transporte en ninguna operación sensible.

### Registro / identidad (OTP) — `WsRegistryExternal/RegistroExterno.svc`
```
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/setRegister
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/GetBackCode      ← código (OTP)
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/SetBackCode
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/AlertEmergencyRed ← alerta a la red
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/findDeviceRed
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/regPendienteAceptar
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/DeleteContactoRed
http://apps.c5.cdmx.gob.mx/.../RegistroExterno.svc/cambioEstatusContacto
```

### Incidente / emergencia
```
http://apps.c5.cdmx.gob.mx/.../MiC911e/ExternalIncidentService/CreateIncidente   ← crear 911
http://apps.c5.cdmx.gob.mx/.../WsExternalIncident/Service1.svc/Followme          ← ubicación en vivo
http://apps.c5.cdmx.gob.mx/.../WsExternalIncident/Service1.svc/GetlocationthismovilApp
http://apps.c5.cdmx.gob.mx/.../WsExternalMisEmergencias/IncidentesWebService.svc/Emergencias
http://apps.c5.cdmx.gob.mx/.../WsExternalAttachImage/Service1.svc/attachImage    ← subir foto
http://apps.c5.cdmx.gob.mx/.../WsExternal089/Numero_Extorsion.svc/validarNumero  ← 089 extorsión
```

### Ficha de salud (datos médicos)
```
http://apps.c5.cdmx.gob.mx/.../ExternalHealthCard.svc/registryHealthCard         ← tipo de sangre, etc.
http://apps.c5.cdmx.gob.mx/.../ExternalHealthCard.svc/registryInsurance
http://apps.c5.cdmx.gob.mx/.../ExternalHealthCard.svc/registryEmergencyContact
http://apps.c5.cdmx.gob.mx/.../ExternalHealthCard.svc/deleteHealthCard|deleteInsurance|deleteEmergencyContact
```

### Sincronización / chat / topics
```
http://apps.c5.cdmx.gob.mx/.../Synchronizes.svc/SincronizaFichaSalud|SincronizaPersona|SincronizaRed|SincronizaMensaje
http://apps.c5.cdmx.gob.mx/.../WSExternalLocatel/ChatService.svc/GetToken|GetTokenStatus
http://apps.c5.cdmx.gob.mx/.../WsExternalAPITopic/api/Topic/Subscribe
https://locatel.chat.cdmx.gob.mx/cliente_whf.php                                  ← chat Locatel (único HTTPS)
```

## IPs hardcodeadas en producción

Dos endpoints se referencian por **dirección IP directa**, no por dominio:

```
http://10.19.113.18/WcfServices/WsAppExternoWcf/Service1.svc/json/EavisaRed?   ← IP PRIVADA (RFC1918)
http://187.174.224.151/WebServices/                                            ← IP pública directa
```

- `10.19.113.18` es una **IP privada interna** del C5 que quedó embebida en el binario de
  producción → **fuga de topología de red interna** (útil para un atacante que ya esté en
  la red del C5, y señal de que el build de producción arrastra configuración interna).
- `187.174.224.151` es una IP pública referenciada sin dominio (sin posibilidad de pinning
  por hostname ni de rotación limpia).

## Consecuencia

Un atacante en la misma red (Wi-Fi pública, red móvil comprometida, MITM de operador) puede,
sin romper cifrado alguno:

- **Leer** la ubicación en vivo, la ficha de salud (tipo de sangre), el OTP de registro y
  el contenido del incidente de auxilio.
- **Alterar o suprimir** una solicitud de auxilio (`CreateIncidente`, `AlertEmergencyRed`)
  o inyectar incidentes falsos, al no haber integridad ni autenticación de transporte.

Es el riesgo más grave del lote por tratarse de un servicio de **emergencias**. Ver doc 08.

> **No cubierto (estático):** los cuerpos de request/response reales y el esquema de
> autenticación de aplicación (token/headers) no se ejercieron; no se llamó a ningún
> endpoint. La afirmación "todo HTTP" se basa en los literales del binario, no en tráfico.
