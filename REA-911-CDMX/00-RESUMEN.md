# 00 — Resumen ejecutivo

## Identidad

| Campo | Valor |
|---|---|
| Nombre | 911 CDMX |
| Package | `com.desarrollo.c5.app_066_android_2` |
| Versión | 1.4.7 (versionCode 40) |
| minSdk / targetSdk | 29 / 33 |
| Dependencia | **C5 / CAEPCCM** (Centro de Comando y Contacto Ciudadano, Gobierno CDMX) |
| Instalaciones | ≈1 M+ (**#13** del inventario) |
| Firma | **Auténtica**: `CN=CAEPCCM, O=CAEPCCM, L=CDMX` (verifica; sin Source Stamp) |
| SHA-256 APK | `c2ab3ebe52d4575e9e5a4f31da4792c895ad5a5bd02c48916d4e0cb3a9ca7fe7` |

## Pila (confirmada por evidencia)

- **Nativa Android Kotlin/Java.** 2 dex (`classes.dex` 8.9 MB + `classes2.dex` 5.0 MB),
  6 313 clases. Única `.so` propia: `libpl_droidsonroids_gif` (GIF, de terceros).
- **Sin ofuscar**: los paquetes se leen tal cual (`com.desarrollo.c5.app_066_android_2.activities/services/receivers/utils/…`). No hay renombrado R8/ProGuard del código propio.
- Red con **Volley** (`com.android.volley`), sin Retrofit/OkHttp.
- NO es Flutter / Unity / .NET MAUI / Capacitor / React Native (verificado por libs y dex).

## Qué hace la app

Super-app de emergencias del C5:

- **Botón de auxilio (911)** → crea un incidente con ubicación (`BotonAuxiloFragment`, `AlertActivity`).
- **Sígueme** → comparte ubicación en tiempo real con una **red de emergencia** de contactos guardianes (`SiguemeService`, `LocationService`).
- **Ficha de salud** → tipo de sangre, seguros, contactos de emergencia (`FichaDeSaludFragment`).
- **Denuncia de extorsión (089)** → valida un número (`ExtorsionLlamadaFragment`, `PhonecallReceiver`).
- **Chat** con Locatel, anuncios públicos, "Mis emergencias".
- Registro por teléfono con **OTP** (`RegistroFragment`, acción `CODIGO_VERIFICACION`).

## Módulos (paquetes propios)

`activities` (SplashScreen, Registro, Navigation, Configuracion, Alert) ·
`fragments` (BotonAuxilo, FichaDeSalud, RedEmergencia, ExtorsionLlamada, Chat, Anuncios, Registro, ProteccionDatosPersonales) ·
`services` (Notification=FCM, **Sigueme**, **Location**) ·
`receivers` (Phonecall, Sigueme, Solicitud, Dissmis) ·
`utils` (VolleySingleton, DataRegister=catálogo de endpoints, App066PreferenceManager=almacenamiento, packager=modelos de PII, validator, database=SQLite).

## Postura de seguridad — veredicto

**Débil en transporte y almacenamiento; íntegra en procedencia.** El código es auténtico
y auditable, pero una app que gestiona auxilio, ubicación en vivo y datos de salud
transmite **todo en HTTP en claro** y guarda la PII **sin cifrar**. El riesgo no es
teórico: interceptar/alterar una solicitud de auxilio en una red hostil es trivial.

**6 hallazgos** (ver doc 08): 1 crítico (cleartext del backend de emergencias), 3 medios
(IPs hardcodeadas, secreto de Twitter en claro, PII sin cifrar), 2 menores (servicios
exportados, ads/analytics). Contrapesos positivos: firma auténtica, código sin ofuscar.
