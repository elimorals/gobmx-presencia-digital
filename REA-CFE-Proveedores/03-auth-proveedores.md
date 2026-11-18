# Autenticación y datos del proveedor

Observado por metadata/literales de `DemoCFE.dll` (no C# textual; no se ejecutó).

## Flujo de acceso
Vistas propias: `DemoCFE.Views.Login`, `DemoCFE.Views.MainPageLogin`,
`DemoCFE.Views.RecoverPassword`. La recuperación maneja campos **`Token`** y **`Password`**
(propiedades `get_Token`/`get_Password` + `<Password>k__BackingField`), consistente con un flujo
de "recuperar contraseña" por token enviado por el backend.

- La autenticación es **propia del portal CFE** (login usuario/contraseña contra
  `enlacesec.cfe.mx`), no un IdP externo (no se observó Azure AD/MSAL ni OAuth de terceros).
- No se hallaron **secretos** embebidos (`client_secret`, API key, Bearer fijo) en los literales
  del DLL.

## Datos que maneja (entidades `CFE.MSC.Entities`)
Gestión de **proveedores y concursos** (B2G): `IdProveedor`, `IdProveedorTitular`,
`NombreProveedor`, `ProveedorTitular`, `IdSugerenciaProveedor`, `ProveedoresConsorcioGrouped`
(consorcios). Endpoint observado: `getdatosinicioproveedor/`.

Es información **empresarial/fiscal del proveedor** (razón social, RFC implícito por el dominio
de proveedores, participación en concursos/consorcios), **no** PII de ciudadanos ni datos de
tarjeta. No se observó pasarela de pago ni captura de tarjeta (no es un flujo de pago al público;
es acceso a licitaciones).

> **Límite:** el protocolo exacto de login (hashing de contraseña, formato del token, headers de
> sesión) vive en los 4 métodos de `DemoCFE.Data.RestService`; se identificaron, no se
> desensamblaron a C#.
