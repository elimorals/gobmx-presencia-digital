# Resumen — Aguascalientes Digital 26.5.5 (`mx.gob.ags.refrendo`)

## Identidad
- **App:** Aguascalientes Digital — super-app del **Gobierno del Estado de Aguascalientes (GEA)**.
- **Package:** `mx.gob.ags.refrendo` · **versión** 26.5.5 (versionCode 307). compileSdk 36.
- **#46** del inventario por instalaciones (≈100 K).
- **Firma:** AUTÉNTICA — `CN=Ulises Mendoza, OU=OFMA, O=GOBAGS, L=Aguascalientes` (firma de
  gobierno, original preservada → binario íntegro del emisor).

## Arquitectura
Nativa **Kotlin moderna**: Hilt (DI), Room (`AgsMovilDatabase`), Moshi (JSON adapters),
Retrofit/OkHttp, MVVM por módulo. 19 071 clases. `.so` propias mínimas (`libd64d72.so`
—nombre ofuscado—, datastore). No Flutter/.NET/Unity/Veritran.

## Módulos (clases propias)
refrendo (adeudos, proceso de pago, tarjeta de circulación), **pago**, **EpagosApi** (gateway),
**Wallet** (tokenización de tarjeta), **CURP**, controlvehicular, licencia, tarjeta_soluciones
(expediente digital), multa_verificacion, **emergencia** (+`LocationService`), **extorsion**
(`MyCallScreeningService`, `CallReceiver`), asistente (websocket/voz), noticias, eventos,
notificaciones, documentos/QR, mapa, ryta (streaming), Firebase.

## Red: 15 clientes Retrofit (NetworkModule)
AgsDigital, AsistenteVoice, AsistenteWebSocket, Clima, ControlVehicular, **Curp**, Emergencia,
**Epagos**, Eventos, ExpedienteDigital, Extorsion, GeaWs, MultaVerificacion, Notificaciones,
**Refrendo**, **Wallet**. Backends: `egobierno2`/`eservicios2`/`expedientedigital.aguascalientes.gob.mx`,
`epagos.aguascalientes.gob.mx` (pagos), `interfaces.c5ags.gob.mx` (C5), `mdt.mx` (mapas),
`ryta.com.mx`/streamlock (video).

## Postura (balance)
| Aspecto | Veredicto |
|---|---|
| Validación TLS | ✅ **No desactivada** (sin trust-all) — correcto |
| Cleartext | ⚠️ **Permitido a producción incl. `epagos` (pagos) y C5** (NSC, doc 03) |
| Logging | ⚠️ `HttpLoggingInterceptor` nivel **BODY** en los 15 clientes (doc 03) |
| Pagos | ✅ Wallet tokenizado + ePagos/CitiBanamex (tarjeta no en claro) (doc 04) |
| Higiene de build | ⚠️ Dev domains + IP cruda en el NSC de producción |
| Firma | ✅ Auténtica del GEA |

**Lectura:** app **bien construida** (Kotlin moderno, sin trust-all, pagos tokenizados), pero con
dos malas prácticas de red claras y confirmadas por código/config: **cleartext permitido a los
backends de producción (incluidos pagos)** y **logging de cuerpos a logcat**. Como la firma es
auténtica, los defectos son del código oficial del GEA.
