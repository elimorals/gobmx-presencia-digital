# CFE Contigo — Ingeniería inversa (REA)

> Análisis **estático** del APK base de `mx.com.cfe.cfecontigo`. La app nunca se ejecuta.
> Herramienta: REA (JADX headless 0.7.1 + apktool). Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| App | CFE Contigo (Comisión Federal de Electricidad, oficial) |
| Paquete | `mx.com.cfe.cfecontigo` |
| Versión | **7.0.0** (versionCode 235) |
| minSdk / targetSdk | 29 (Android 10) / 36 |
| Instalaciones (Play, inventario gob.mx) | **37,450,757** — #1 del ranking |
| Bundle | XAPK (split APKs estilo Google Play): base + `config.arm64_v8a` + `config.en` + `config.hdpi` |
| APK base | 24,030,457 bytes · SHA-256 `d859fb91e74279e2a18fe9015d5f61a3a99a822c9a2af7eabf45479089596bf6` |
| DEX | classes.dex + classes2.dex |
| Clases (top-level, JADX) | **10,339** · Recursos | 1,011 |
| Firma APK | **no verificada** por el motor (origen APKPure) |
| Librería nativa | `lib/arm64-v8a/libb1bd.so` (1,681,549 bytes, **empaquetada**) |

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| UI | Android Views clásicas (MVVM por pantalla, `ui.*`) | 31 Activities `mx.com.cfe.cfecontigo.ui.*` |
| Red | **OkHttp** (ofuscado dentro de `o.*`) | `Lokhttp3/internal`, headers `Authorization`/`Content-Type`/`application/json` |
| Serialización | DTOs Kotlin `data class` (patrón `component1..N`, `copy`) | ~90 clases `data.*` |
| Auth | **OAuth2** (access/refresh), **OTP email**, captcha propio, **AppAuth/OIDC**, Firebase Auth | `AuthResponse`, `OtpRequestDto`, `net.openid.appauth`, `GenericIdpActivity` |
| Cripto local | **AndroidKeyStore** + AES-GCM/CBC + PBKDF2 | constantes en DEX (doc 03) |
| Push / backend móvil | **Firebase** (FCM, Auth, Analytics, Crashlytics, In-App Msg, Remote Config) | `strings.xml`, servicios en manifest |
| Push / servicios HMS | **Huawei Mobile Services** (HMS Push, AGConnect, GRS) | `assets/grs_*`, `*.bks`, servicios HMS |
| Anti-fraude | **LexisNexis ThreatMetrix** | `assets/services/com.lexisnexisrisk.threatmetrix.*`, host `h-sdk.online-metrix.net` |
| QR / códigos | **ZXing** (`com.journeyapps.barcodescanner`) | `CaptureActivity` |
| Protección | **DexGuard** string encryption + packer nativo | patrón `$$a`/`$$c` por clase, `.so` entropía ≈1.0 |

## Módulos propios (`mx.com.cfe.cfecontigo.*`)

| Paquete | Rol |
|---|---|
| `.ui.login` | Splash, Login, Mantenimiento, recuperación de contraseña |
| `.ui.dashboard` | Dashboard, mapa de ubicaciones (CAC), fragment de ubicaciones |
| `.ui.contratacion` | Contratación de servicio (alta nueva), firma digital (`SignatureView`) |
| `.ui.services` / `.ui.cards` | Servicios/contratos ligados, tarjetas de pago |
| `.ui.reports` | Reportes de fallas / "no luz" (`ReportsAddActivity`) |
| `.ui.simulator` | Simulador de consumo (`ConsumptionGaugeView`) |
| `.ui.paperless` | Facturación electrónica / sin papel |
| `.ui.reciept` | Recibos / comprobantes |
| `.ui.anticorrupcion` | **Canal de denuncia de corrupción** integrado |
| `.ui.messages` | Mensajes / notificaciones |
| `.ui.profile` / `.ui.password` | Perfil, cambio de contraseña |
| `.data` | ~90 DTOs (auth, pagos, trámites, citas, facturas, quejas) |

## Mapa de hallazgos por documento

- `01-manifiesto.md` — 16 permisos, deeplink `mx.com.cfe.cfecontigo://`, FileProvider `/`.
- `02-autenticacion.md` — OAuth2 con refresh + `mustChangePassword`, OTP por email, captcha.
- `03-cripto-almacenamiento.md` — AndroidKeyStore, AES-GCM/CBC, PBKDF2, llave pública `crmkey_pub.pem`.
- `04-red-endpoints.md` — OkHttp + Basic/Bearer; endpoints no recuperables (DexGuard).
- `05-ofuscacion-proteccion.md` — DexGuard, `.so` packed, ThreatMetrix.
- `06-pagos-tarjetas.md` — tokenización PCI-friendly, cargos recurrentes, pago con puntos.
- `07-sdks-terceros.md` — doble ecosistema Google + Huawei, anti-fraude.
- `08-pantallas-flujos.md` — trámites cubiertos y superficie de UI.
- `09-firma-recursos.md` — firma no verificada, assets, estructura del bundle.

## Contraste con jw-library (mismo pipeline REA)

| Dimensión | JW Library | CFE Contigo |
|---|---|---|
| Endpoints en claro | Sí (prod/dev/QA visibles) | **No** (DexGuard string encryption) |
| Ofuscación de código | R8 parcial, símbolos conservados | **DexGuard** (strings cifrados, nombres `o.*`) |
| Librería nativa | libsodium/Javet legibles | `libb1bd.so` **empaquetada** (ilegible) |
| Dominio funcional | Lectura de contenido | **Financiero/trámites** (pagos, tarjetas) |
| Anti-fraude | — | **ThreatMetrix** |
| Permisos peligrosos | Ninguno | Cámara, ubicación (fina y gruesa), teléfono |
