# 04 — Autenticación, token y gestión de usuario

Evidencia: métodos de `ICV_APP.ISeguridad`, modelos de `ICV_APP.Entitys`, literales de UI.

## Login

- Método `ISeguridad.Login` → POST `…/SeguridadContribuyenteController/Login` con modelo
  `LoginRequest`. Credenciales: **usuario + contraseña** (literales de UI: "Captura tu Usuario",
  "Captura tu Contraseña", "Recordar Usuario").
- Hay medidor de fuerza de contraseña (literales "Nivel de seguridad: ALTA/MEDIA/BAJA",
  "Seguridad de la contraseña") → validación en cliente.
- "Recordar Usuario" sugiere persistencia del usuario (no necesariamente la contraseña) en
  preferencias (doc 08).

## Token de atención (`GeneraToken`)

- `ISeguridad.GeneraToken` (async `d__17`) → POST `…/GeneraToken`, modelo `TokenRequest` →
  respuesta `ResTokenEntity` con campos `Token`, `TokenActivo`, `TokenCorto`.
- La UI (`PopUpToken`, `Detalle.GeneraToken_Tapped`) **muestra el token y permite copiarlo**
  (`btnCopy_Clicked`) con un tooltip temporal. Es un **token de atención/turno** que el
  contribuyente presenta en ventanilla, no un secreto de sesión. No se observa que se cifre en
  cliente (doc 08).

## Registro y activación

- `AltaUsuario` (modelo con **CURP**, correo, teléfono, contraseña) → alta.
- `ValidarDuplicado` / `ValidaActivo` / `ReqUsuario` → validan CURP/usuario antes del alta
  (literal "El CURP ya se encuentra registrado").
- `ActivarUsuario` + `ReEnviaCodigo` → activación por **código enviado al correo**
  (modelo `ActivaCryptoRequest`; literal "código que te llegó al correo").

> `ActivaCryptoRequest` lleva "Crypto" en el nombre, pero **no se observó uso de
> `System.Security.Cryptography` en `ICV_APP.dll`** (doc 08). No se afirma qué cifra: puede ser
> solo el nombre del DTO del código de activación. Queda como pregunta abierta, no como hallazgo.

## Recuperación

- `RecuperarContraseña` / `ReqRecuperarContraseña` → flujo de nueva contraseña con confirmación
  por correo.
- `RecuperaUsuarioFoto` + `RecuperarUsuario` → recuperar el **usuario** (se envía al correo:
  "Tu usuario fue enviado al correo").

## Datos personales gestionados (PII)

De los modelos y la UI: **CURP**, correo electrónico, teléfono, usuario/contraseña, **placa**,
datos de **licencia de conducir** (incl. imagen y biométrico, doc 05-06) y **vehículos**
(doc 05). Todo viaja a la API HTTPS :1046; no hay cifrado adicional en cliente (doc 08).
