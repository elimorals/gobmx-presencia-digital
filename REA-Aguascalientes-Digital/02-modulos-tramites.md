# Módulos, clientes de red y PII

## Arquitectura (confirmada por el grafo de clases)

Kotlin moderno con **Hilt** (inyección), **Room** (`AgsMovilDatabase`), **Moshi** (adapters
generados: `RespuestaPaseCajaJsonAdapter`, `MultaGeneradaJsonAdapter`, …) y **Retrofit**. Patrón
MVVM por módulo (`*Fragment` + `*ViewModel` + `*Repository` + `*Service`).

## 15 clientes Retrofit (`di.NetworkModule`, 46 métodos `@Provides`)

`provide…` para: **Refrendo**, **EpagosApi**, **Wallet**, **Curp**, ControlVehicular,
Emergencia, **Extorsion** (con `provideExtorsionAuthInterceptor`), ExpedienteDigital, GeaWs,
MultaVerificacion, Clima, Eventos, Notificaciones, AgsDigital, AsistenteVoice + WebSocket.

Cada uno arma su `OkHttpClient` + `Retrofit`. Comparten `provideHttpLoggingInterceptor` (nivel
BODY, doc 03), `provideNetworkConnectionInterceptor` y un `provideSponsoredInitInterceptor`.
**Ninguno** instala un cliente trust-all (doc 03).

## Módulos y PII que manejan

| Módulo | Datos |
|---|---|
| `login`/`registro`/`ajustes` | credenciales, correo, teléfono (CambioContrasena, VerificaNumero) |
| `refrendo` + `controlvehicular` | tarjeta de circulación, adeudos vehiculares, **pago** |
| `Curp` (service) | **CURP** del ciudadano |
| `licencia` | licencia de conducir (detalle, vinculación) |
| `tarjeta_soluciones` | expediente digital: datos generales, domicilio, documentos, login |
| `multa_verificacion` | datos de vehículo, multas, pago de multa |
| `emergencia` | **ubicación en tiempo real** (`LocationService`) |
| `extorsion` | **metadatos de llamadas** entrantes (`MyCallScreeningService`) |
| `documentos`/`QR` | documentos + códigos QR |

PII fuerte: CURP, licencia, tarjeta de circulación, datos de pago, ubicación, llamadas. El manejo
viaja por los backends del doc 03 (algunos con cleartext permitido) y puede pasar por el logging
BODY. El almacenamiento local usa Room (`AgsMovilDatabase`) + `SharedPreferencesHelper`; el cifrado
local fino (Keystore) no se decompiló a fondo — marcado como límite.
