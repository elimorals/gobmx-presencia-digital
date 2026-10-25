# Ofuscación y protecciones

CFE Contigo tiene un nivel de endurecimiento **poco común en apps de gob.mx** y es el rasgo
que más la diferencia de jw-library. Tres capas:

## 1. Cifrado de strings (estilo DexGuard)

Cada clase inspeccionada (`AuthResponse`, `OtpRequestDto`, `CardDto`, …) presenta el **mismo
patrón sintético**:

- Campos: `$$a: byte[]`, `$$b: int`, `$10: int`, `$11: int`, `RemoteActionCompatParcelizer: int`.
- Método estático `$$c(int, byte, short): java.lang.String` — **descifrador de strings**.
- Método `a(…, char[], java.lang.Object[])` / `b(char[], char, …)` — rutina de
  transformación invocada desde `<clinit>`.

Los literales (nombres de campo JSON, URLs, claves) se guardan como **bytes en `$$a`** y se
reconstruyen en tiempo de ejecución llamando a `$$c()`. Por eso `strings`/grep sobre el DEX no
encuentra las constantes sensibles. Este patrón (`$$a`/`$$b`/`$$c` + decrypt en `<clinit>`) es
característico de **DexGuard** (el endurecedor comercial de Guardsquare) o un ofuscador
equivalente — va más allá del R8/ProGuard estándar, que solo renombra pero **no cifra**
strings.

El renombrado también afecta a clases enteras: ~34 clases bajo `o.*` con nombres generados a
partir de `p`/`0070` (`o.pp0070p0070ppp0070`, `o.ppppp0070pp`, …). La capa de red (OkHttp)
vive aquí.

## 2. Librería nativa empaquetada — `libb1bd.so`

| Propiedad | Valor | Interpretación |
|---|---|---|
| Tamaño | 1,681,549 bytes (arm64-v8a) | — |
| Formato | ELF aarch64, **`no section header`**, `bad note description size` | Cabeceras destruidas → packer |
| Entropía (ratio gzip) | **≈1.000** | Contenido cifrado/comprimido (no compresible) |
| Strings legibles (≥12) | 11, todas basura | Sin símbolos, sin `JNI_OnLoad`, sin URLs |

El nombre no descriptivo (`libb1bd.so`) y la ausencia total de símbolos/strings indican una
**librería protegida por un packer nativo**. Su rol probable: bóveda de secretos
(claves/URLs), lógica anti-tamper, o el motor del SDK anti-fraude. No es analizable con
`strings`; requeriría desempaquetado dinámico (dump de memoria tras la carga), fuera de
alcance.

## 3. Anti-fraude / fingerprinting — LexisNexis ThreatMetrix

- Clases `com.lexisnexisrisk.threatmetrix.rl.*` registradas vía
  `assets/services/com.lexisnexisrisk.threatmetrix.rl.TMXModuleInitializerInterface` y
  `TMXProfilingConnectionsInterface`.
- Host de recolección: `h-sdk.online-metrix.net`.

ThreatMetrix construye un **perfil del dispositivo** (device fingerprint, señales de riesgo) y
lo envía a su backend para puntuar transacciones como fraudulentas. Es estándar en banca y
pagos; aquí respalda los flujos de pago/contratación. Implica recolección de telemetría del
dispositivo del usuario por un tercero (consideración de privacidad).

## Implicaciones

1. El análisis **estático puro tiene techo** en esta app: endpoints, claves y lógica de
   negocio sensible están protegidos por cifrado de strings + packer nativo.
2. El perfil de protección (DexGuard + packer + ThreatMetrix) es **de grado financiero**,
   consistente con que la app mueve pagos reales — no es una simple app de consulta.
3. Para ir más allá haría falta **análisis dinámico** (Frida/instrumentación, mitmproxy) sobre
   un dispositivo propio, documentado aparte. Este repositorio se detiene en lo estático.
