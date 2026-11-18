# Arquitectura

## Pila técnica (confirmada por clases decompiladas)

Kotlin/Java nativo, sin librerías nativas (`lib/*.so` = 0). Patrón MVVM/MVP mixto con DI por
Hilt. Evidencia en el inventario de clases (`search_android_classes`):

- **DI (Hilt/Dagger):** `App_HiltComponents`, `DaggerApp_HiltComponents_SingletonC`,
  `di.AppModule`, `di.NetworkModule`, `di.RepositoryModule`, `di.ViewModelModule`,
  `di.QrGeneratorModule`, `di.CoroutineScopeModule`.
- **Red:** `di.NetworkModule` (Retrofit + OkHttp vía Hilt) **y** `webservice.ServiceGenerator`
  (singleton legado). `webservice.Service`, `ServiceInfraccion`, `ServiceInfraccionGenerator`.
- **Persistencia:** `db.MovilidadDataBase` (Room) con DAOs `LicenseDao`, `ConfigDao`,
  `DataIsoDao`, `QrProviderDao`; entidades `LicenseEntity`, `ElectronicSignData`,
  `DataIsoEntity`, `ConfigEntity`. Cifrado **SQLCipher** (doc 05).
- **UI:** Jetpack Compose (`...refactor.infracciones.InfraccionesScreenComposeKt`) +
  databinding (`ActivityHomeBinding`, `FragmentLicenseBinding`, …) + navegación AndroidX.
- **Asíncronía:** Coroutines (DI de `IOCoroutineContext`) y RxJava (`rxactivityresult.*`,
  interactores de infracciones).

## Dos pilas de red conviviendo (clave para doc 04)

| Ruta | Clase | Validación TLS | Qué transporta |
|---|---|---|---|
| **Legada (activa)** | `webservice.ServiceGenerator` | **Desactivada** (trust-all) | Licencia, pago, OTP, catálogos, config, Face API |
| Hilt/Retrofit | `di.NetworkModule.provideOkHttpClient` | Estándar (CA del sistema) | Consumidores del `Service` inyectado (p. ej. noticias/infracciones) |

El grueso de los interactores de negocio (`LicenseInteractorImpl`, `PaymentInteractorImpl`,
`PhoneVerificationInteractorImpl`, `CatalogInteractorImpl`, `ConfigInteractorImpl`,
`LetterScreenInteractorImpl`, `AppointmentInteractorImpl`) llama a `ServiceGenerator.createService`
— la ruta sin validación TLS. Ver doc 04.

## White-label multi-estado (Cosmocolor)

El package es `mx.com.cosmocolor.movapp` y el backend vive en `licenciamanejo.com`: es una
plataforma del proveedor **Cosmocolor** replicada por estado. Evidencia en el binario de EDOMEX:

- `enums.Proyecto` + `Proyecto$Companion.getPatternForProject(int)` → la app parametriza su
  comportamiento (p. ej. el patrón de QR) por "proyecto"/estado (doc 06).
- Endpoints y recursos de **Chiapas** embebidos: `https://chiapas-verifica-infraccion-ty3qpufnqq-uc.a.run.app/`
  y `https://www.sspc.chiapas.gob.mx/Secciones/vialidad`, además de un
  `webservice.interceptors.ChiapasVerificaInterceptor` (doc 03).
- Submódulo propio `mx.com.cosmocolor.cameralibrary` (captura/Face API) con **su propio**
  `ServiceGenerator` igualmente trust-all.

Implicación: el binario de un estado arrastra artefactos de otros despliegues del mismo
proveedor — amplía la superficie documentada y filtra infraestructura ajena a EDOMEX.
