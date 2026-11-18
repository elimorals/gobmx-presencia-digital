# Arquitectura — Compose + Hilt + Retrofit + Room

## Pila (confirmada)
Nativa **Kotlin/Jetpack Compose**. `search_android_classes` sobre `mx.gob.jmasmovil` muestra el
patrón MVVM moderno completo:

- **DI (Hilt/Dagger):** `di.NetworkModule`, `di.AppModule`, `DaggerJMASMovilApplication_...`.
- **Red (Retrofit/OkHttp):** `data.network.{JmasApi, AuthApi, RemoteRepository, IRemoteRepository}`,
  `data.network.request.*` (LoginRequest, UserRegisterRequest, PaymentURLRequest, …),
  `data.network.response.*` (LoginResponse, PaymentResponse, TokenResponse, ServerResponse),
  `data.network.utils.{TokenAuthenticator, ResultCallAdapterFactory, ResultCall}`.
- **Persistencia (Room):** `data.models.{JMASMDatabase, ContractsDao, NotificationsDao, Contract,
  BillInfo, UserData, JMASNotification}` + `data.preferences.SharedPreferencesManager`.
- **UI (Compose):** `ui.{login, register, contracts, contractdetails, profile, notifications,
  splash, main}` con sus `ViewModel` por pantalla.

Sin Flutter (`flutter_assets`), Unity (`il2cpp`), .NET (`assemblies`), bundle web (`assets/www`)
ni React Native → app Android **nativa**, decompilable.

## Flujo
`RemoteRepository` expone (con `ResultCall` envolviendo éxito/error): `logIn`, `signUp`,
`resetPassword`, `updatePassword`, `deleteAccount`, `getUserContracts`, `registerNewContract`,
`getUserBillingHistory`, **`getPaymentURL`** (doc 04), `systemHealthCheck`. El estado se guarda en
Room (contratos/notificaciones) y en `SharedPreferencesManager` (token/sesión, doc 05).
