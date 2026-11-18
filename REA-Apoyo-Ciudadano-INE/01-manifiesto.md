# 01 — Manifiesto, permisos y componentes

Fuente: `AndroidManifest.xml` decodificado (apktool). minSdk 28, targetSdk 36, versión 5.93 (236).

## Permisos declarados

| Permiso | Para qué (inferido del uso) | Riesgo |
|---|---|---|
| `CAMERA` | Fotografiar la credencial y foto en vivo | Esperado (captura de identidad) |
| `ACCESS_FINE_LOCATION` | Geolocaliza la captación del apoyo | **Sensible**: liga ubicación precisa a cada firma |
| `ACCESS_COARSE_LOCATION` | Ubicación aproximada | Sensible |
| `READ_PHONE_STATE` | Identidad/estado del dispositivo | **Sensible**: permite leer identificadores del teléfono |
| `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` | Guardar fotos/paquete antes de subir | Medio |
| `INTERNET` / `ACCESS_NETWORK_STATE` | Subir el paquete, consultar avance | Esperado |
| `VIBRATE`, `WAKE_LOCK` | UX / mantener proceso | Bajo |
| `ACCESS_ADSERVICES_AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION` | Privacy Sandbox / atribución de ads | **Inusual** en una app electoral |

**Combinación relevante.** Una app de captación de apoyo que declara **cámara + ubicación precisa +
estado del teléfono** liga, por diseño, la identidad del ciudadano respaldado (credencial) con la
ubicación y el dispositivo del auxiliar. Es PII de terceros con contexto geográfico (ver doc 05).

## Flags de la aplicación

| Flag | Valor | Lectura |
|---|---|---|
| `android:allowBackup` | `false` | ✅ Correcto: evita respaldo adb de datos de la app |
| `android:usesCleartextTraffic` | *(ausente)* | Por defecto `false` en API 28+ → **no permite HTTP en claro por política** |
| `networkSecurityConfig` | *(ausente)* | Sin NSC explícito; aplica la política por defecto (solo HTTPS) |
| `android:debuggable` | *(ausente)* | No debuggable |

> A diferencia de IMSS Digital (cleartext habilitado) o Va-y-Ven (NSC con HTTP a pagos), aquí la
> política de red por defecto **no permite tráfico en claro**. Es una postura correcta.

## Componentes

| Tipo | Cantidad |
|---|---|
| `activity` | 56 |
| `service` | 10 |
| `receiver` | 6 |
| `provider` | 7 |

Actividades propias clave (`mx.ine.apoyociudadano.activity.*`): `SplashActivity`, `MainActivity`,
`WelcomeActivity`, `RegistroMiApoyoActivity`, `SeleccionAspiranteActivity`, `CredentialTypeActivity`,
`CredentialCaptureActivity`, `CredentialCiudadanoActivity`, `CredentialInfoFormActivity`,
`OcrProcessingActivity`, `LivePhotoActivity`, `SignActivity`, `PasswordCreateActivity`,
`VerificacionActivity`, `SummaryActivity`, `StatisticsActivity`, `SocialSessionActivity`,
`PreDataProtectionManifestActivity` (aviso de privacidad). La captura usa además
`mx.ine.apoyociudadano.CapturaQRsCifradosKt` (pantalla que instancia `com.personalcode.PersonalCodeOffline`).

`uses-feature`: `android.hardware.camera` (+ autofocus, **front**, front.autofocus) → confirma el uso
de cámara frontal para la foto en vivo del ciudadano.

> No se enumeran aquí los componentes exportados por filtro de intención; la revisión de superficie
> exportada no identificó un componente propio claramente peligroso expuesto, pero no se auditó cada
> uno de los 79 componentes función por función (límite declarado, no veredicto de ausencia).
