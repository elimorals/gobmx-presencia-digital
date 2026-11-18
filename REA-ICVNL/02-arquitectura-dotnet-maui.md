# 02 — Arquitectura: .NET MAUI y cómo se extrajeron los ensamblados

## La app es .NET MAUI, no Kotlin/Java

El triage inicial por tamaño/estructura la marcó "nativa Kotlin/Java". **Corrección con
evidencia:** el `AndroidManifest.xml` declara `mono.MonoRuntimeProvider`,
`microsoft.maui.essentials.fileProvider` y clases con nombre `crc64…` (hash JNI que genera el
puente managed→Java de Xamarin/.NET). El split `config.arm64_v8a` contiene
`libmonodroid.so`, `libmonosgen-2.0.so`, `libxamarin-app.so`, `libSystem.*.so` y
`libSystem.Security.Cryptography.Native.Android.so`. Es **.NET MAUI** (sucesor de
Xamarin.Forms) — la misma familia que IMSS Digital (Xamarin). La lógica de negocio vive en
**ensamblados .NET (C#)**, no en el DEX; el DEX solo contiene stubs `crc64…`.

## Dónde viven los ensamblados y cómo se extrajeron

En .NET Android moderno los ensamblados no están como `assemblies/*.dll` sueltos: van en un
**AssemblyStore** embebido en `lib/arm64-v8a/libassembly-store.so` (38 MB, un ELF cuyo payload
es el blob `XABA`). Cada ensamblado está comprimido con **LZ4** (cabecera `XALZ`).

Procedimiento (estático, reproducible):
1. `unzip` del split `config.arm64_v8a.apk` → `lib/arm64-v8a/libassembly-store.so`.
2. Cabecera `XABA` en offset 16384: **425 ensamblados**, todos `XALZ`/LZ4.
3. Script propio (`evidence/` → `xamextract.py`): carva cada `XALZ`, lee `uncompressed_size`,
   decomprime con `lz4.block`, y **nombra cada DLL leyendo la tabla Module de su metadata PE**.
4. Resultado: **424 DLL** extraídos a `evidence/assemblies/` (no versionado).

> Nota de método: 425 entradas en el índice, 424 PE válidos recuperados (1 entrada no
> decomprimió a un PE `MZ`; no se fuerza ni se inventa su contenido).

## El ensamblado de la app: `ICV_APP.dll`

De los 424, el ensamblado propio es **`ICV_APP.dll`** (703 KB). Los demás son framework
(`System.*`, `Microsoft.Maui.*`, `Mono.*`, `Xamarin.AndroidX.*`) o librerías de terceros
(doc 09). Todo el análisis de negocio sale de `ICV_APP.dll` con las tools managed de REA
(`inspect_managed_artifact`, `inspect_managed_members`).

## Mapa de la app (namespaces `ICV_APP.*`)

- **`ICV_APP.Pages`** — pantallas: `Login`, `Registro`/`Registro2`/`RegistroURL`, `Recupera`,
  `Detalle`, `Licencia`, `Vehiculos`, `Tramites`, `Redes`, `Configuracion`.
- **PopUps**: `PopUpToken`, `PopUpQR`, **`PopUpFaceLiveness`**, `PopUpLicencia`/`PopUpLicOff`,
  `PopUpAgregaLic`, `PopUpContrase`, `PopUpCorreo`, `PopUpTelefono`, `PopUpCalifica`,
  `PopUpCargando`.
- **`ICV_APP.ISeguridad`** — clase de servicio HTTP (todas las llamadas a la API, doc 03-05).
- **`ICV_APP.Entitys`** — modelos (doc 04-05): `LoginRequest`, `TokenRequest`,
  `ResTokenEntity`, `UsuarioEntity`, `VehiculoEntity`, `ResLicenciaEntity`,
  `BiometricoLicRequest`, `ValidaCertLicRequest`, `CertVehRequest`, `QrInfoEntity`,
  `FacelivenessEntity`, `TramiteRequest`, `ActivaCryptoRequest`, `DelegacionEntity`, `LogEntity`.
- **`ICV_APP.Render` / `ICV_APP.Droid.Handlers`** — `ICustomWebView` (WebView personalizada con
  puente JS, doc 07).

## Arquitectura funcional (dos planos)

1. **Plano nativo (.NET MAUI):** login, alta/activación/recuperación de usuario, generación de
   token, consulta de vehículos y licencia, contra la API `SeguridadContribuyenteController`
   (doc 03-05).
2. **Plano WebView:** trámites, pagos y **liveness facial** se cargan en `ICustomWebView`
   mediante `UrlPortal`/`UrlTramite` (URLs provistas por el servidor) con un puente
   JavaScript↔C# (doc 06-07).
