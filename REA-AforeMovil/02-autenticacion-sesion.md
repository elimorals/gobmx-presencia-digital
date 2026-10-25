# 02 — Autenticación, NIP, FIDO y sesión

## Modelo de acceso

AforeMóvil autentica contra el backend del SAR en `api.esar.io` con un esquema de **token SAR**
(“sartoken”), y usa **FIDO/UAF (Daon)** para la biometría local y la **autorización de
operaciones** (GestoPago). Evidencia de endpoints (`res/values/strings.xml`):

| String | Valor | Rol |
|---|---|---|
| `url_api_login` | `https://api.esar.io/sartoken/` | Login / emisión de token |
| `url_api` | `https://api.esar.io/aweb/sartoken/` | API autenticada (aweb) |
| `server_url` | `https://api.esar.io:443/aforemovil/` | Base de servicios de la app |

## Factores y credenciales (modelo de datos)

En `com.procesar.aforemovil.commons.data.*` aparecen los validadores y estados que delatan los
factores de acceso:

| Clase | Qué valida / representa |
|---|---|
| `error.PasswordError`, `status.PasswordUiState` | Contraseña de la cuenta |
| `error.NipError`, `status.InfoNip` | **NIP** (PIN numérico del SAR) |
| `error.RfcError`, `error.ClabeError` | RFC y CLABE interbancaria (operaciones/retiros) |
| `status.UserStatus`, `model.RegisterStatusData` | Estado de registro / usuario |
| `userstatusdata.DataGroup/DataIdentifier/DataMode/DataStatus` | Datos de estatus del usuario |
| `status.captureidentification.*` | Flujo de captura de identidad en alta (doc 03) |

Los módulos `nip_activation` y `niprecovery` (actividades dedicadas) cubren alta y recuperación
de NIP; `preexistence` resuelve si el usuario ya tiene cuenta antes de registrar.

## FIDO / UAF — Daon IdentityX

El manifiesto declara el cliente FIDO completo de Daon (todas `exported="false"`):

```
com.daon.fido.client.sdk.init.InitialiseSdkActivity
com.daon.fido.client.sdk.reg.UafClientRegisterActivity / UafAsmRegisterActivity
com.daon.fido.client.sdk.auth.UafClientAuthenticateActivity / UafAsmAuthenticateActivity
com.daon.fido.client.sdk.dereg.UafClientDeregisterActivity / UafAsmDeregisterActivity
com.daon.fido.client.sdk.ui.ChooseAuthenticatorActivity / ChooseAccountActivity / DisplayTransactionActivity
```

- Arquitectura UAF estándar: **Client** + **ASM** (Authenticator-Specific Module) + authenticators.
- Ruta REST del servidor IdentityX observada en el DEX:
  `IdentityXServices/rest/v1/secured/users` (cliente `com.daon.sdk.fido.service.rest.HTTP`).
- Claves protegidas con `com.daon.sdk.crypto.SecureKeyStore` y
  `com.daon.sdk.keystore.hardware.only` → keystore respaldado por hardware (ver doc 04).
- `com.daon.fido.client.sdk.state.Key(s)` gestiona el estado de las credenciales UAF.

`USE_BIOMETRIC` + `USE_FINGERPRINT` habilitan el authenticator biométrico local; la verificación
facial/voz (docs 03) actúa como authenticator adicional del mismo framework.

## GestoPago — autorización biométrica de operaciones

`GestoPago` es el mecanismo con el que la app **autoriza transacciones** (traspasos, retiros,
ahorro) mediante biometría FIDO, no solo para iniciar sesión.

- Modelo Realm `...commons.data.realm.model.GestoPago` (campos `content: PsContent`,
  `contentDescription`, getters `id/name/status/description/content`), construido desde el modelo
  del SDK `mx.com.procesar.ps.core.mvp.model.PSGestoPago`.
- Strings del DEX: `createGestoPagoAuthorizationList`, `realmGet$psGestoPagoAuthorization` →
  el cliente mantiene una **lista de autorizaciones** de GestoPago, persistida en Realm.
- `PsContent` indica contenido descrito por servidor (textos/estado de la autorización).

Lectura: cada operación sensible genera una autorización que el usuario firma biométricamente
(UAF `DisplayTransactionActivity` muestra el detalle a firmar) — patrón *transaction confirmation*
de FIDO, correcto para una app financiera.

## Token de acceso y caducidad (evidencia estática)

El token del SAR es un **Bearer con refresh**, estilo OAuth2:

| Evidencia (clase / string del DEX) | Significado |
|---|---|
| `mx.com.procesar.ps.core.mvp.model.response.TokenResponse` | Respuesta de emisión de token |
| `mx.com.procesar.ps.core.mvp.model.request.RefreshTokenRequest` | Petición de **refresh** |
| strings `Authorization`, `Bearer `, `Proxy-Authorization` | Esquema de cabecera **Bearer** |
| `REFRESH_TOKEN_KEY`, `EXPIRES_IN_SECONDS_KEY` | Refresh token + `expires_in` (segundos) persistidos |
| `AUTH_TOKEN_EXPIRATION_BUFFER_IN_SECS`, `tokenExpirationTimestamp` | Margen de expiración + marca de caducidad local |
| `PARSING_EXPIRATION_TIME_ERROR_MESSAGE` | Parseo del tiempo de expiración |

→ La app guarda `expires_in` y calcula un `tokenExpirationTimestamp` local; refresca con
`RefreshTokenRequest` antes de que expire (restando un *buffer* en segundos). El acceso se envía en
cabecera `Authorization: Bearer <token>`.

**JWT (Tink) en el flujo de identidad.** Además del Bearer de sesión, hay **Google Tink JWT**
(`com.google.crypto.tink.jwt.JwtInvalidException`) con un **claim facial** (`getClaimFace`/
`setClaimFace`): el token del módulo de identidad/biometría es un JWT verificado con Tink que porta
un claim del rostro. Son dos nociones de token distintas (sesión SAR vs. identidad facial).

**Almacenamiento.** Los tokens/estado de sesión viven en **DataStore**
(`commons.domain.repository.shared.datastore.AppPreferencesRepositoryImpl`: `setLocalSessionActive`,
`setFirebaseToken`). No se observó que el refresh token se guarde en AndroidKeyStore; confirmarlo es
trabajo dinámico (si va en DataStore en claro, es extraíble en un dispositivo rooteado).

## Gestión de sesión

| Componente | Rol |
|---|---|
| `mx.com.procesar.ps.core.session.work.SessionWorker` | **WorkManager**: vigilancia/caducidad de sesión en segundo plano |
| `commons.data.services.ServicesResult.SessionExpired` | Resultado de servicio de primera clase “sesión expirada” |
| `ui.features.session.ui.SessionTimeoutActivity` (tema transparente) | Aviso/bloqueo por inactividad |
| `ui.features.session.ui.SessionExpiredActivity` | Sesión expirada → re-login |
| `BaseApp` implementa `DefaultLifecycleObserver` (`onStart/onStop/onPause/onResume`) | Foreground/background para temporizar la sesión |

La app observa el ciclo de vida del proceso (`BaseApp`/`ProcessLifecycleInitializer`) y un
`SessionWorker` para disparar timeout; cualquier servicio puede devolver `SessionExpired` y forzar
`SessionExpiredActivity`. Apropiado para datos financieros.

## Captura facial exportada: sin guard de sesión local

`FacialActivity.onCreate` e `IdxFacialCaptureActivity.onCreate` (ambas **exportadas**, doc 01)
**no comprueban sesión/token local** antes de montar la UI (decompilados en `decompiled/`):

- `IdxFacialCaptureActivity.onCreate` solo hace `super.onCreate` + `clearFlags(FULLSCREEN)` sobre la
  `CaptureActivity` de Daon.
- `FacialActivity.onCreate` cablea con Dagger toda la pila de identidad (casos de uso
  `GetLifeTests`, `CreateIdCheck`, `SearchFace`, `UploadPhoto`, `CreateEvaluation`, `UploadDocument`…)
  e inicializa `Fido.getInstance(...)`, luego pinta Compose — sin chequeo de sesión previo.

La contención real recae en el **backend IdentityX**: las operaciones inyectan credenciales de
usuario (`idxfacial.services.di.NetworkModule_ProvideUserCredentialsFactory`) y requieren un
contexto de evaluación (`CreateEvaluation`/`CreateIdCheck`). Lanzar estas activities desde otra app
abriría la cámara/captura, pero subir/evaluar exige credenciales del backend. Verificar el impacto
exacto (p. ej. si `CreateEvaluation` acepta un contexto controlable por el invocador) es el punto
que más merece análisis dinámico.

## Pendiente de verificación dinámica

- Confirmar si el refresh token se almacena cifrado (Keystore) o en DataStore en claro.
- Parámetros del registro UAF (policy, attestation) contra el servidor IdentityX/TrustX.
- Comportamiento real de las activities faciales exportadas ante un `Intent` externo sin sesión.
