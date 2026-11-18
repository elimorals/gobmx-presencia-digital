# Resumen — BanjeCel 7.10.3 (`mx.com.banjercito…VTUserApplicationBJCTMB`)

## Identidad

- **App:** BanjeCel — banca móvil de **Banjército** (Banco Nacional del Ejército, Fuerza Aérea
  y Armada), institución de banca social para las fuerzas armadas.
- **Package:** `mx.com.banjercito.model.common.VTUserApplicationBJCTMB` (el `VT` =
  **Veritran**) · **versión** 7.10.3.50500 (versionCode 5050090).
- **#11** del inventario por instalaciones (≈1 M). APK 141 MB (2 dex + modelos biométricos).
- **Firma:** **auténtica de Banjército** (`CN=Banjercito, O=Banco Nacional del Ejercito…`).

## Arquitectura (en una línea)

App **low-code Veritran**: la lógica bancaria es un bundle interpretado por **Duktape**
(`libduktape.so`, motor JS) sobre la plataforma Veritran (`libsmma.so`). El DEX es el host; no
contiene la lógica ni las rutas de API → **muro estático**, igual que Banco del Bienestar.

## Pila nativa (`evidence/native-libs.txt`)

| `.so` | Rol |
|---|---|
| `libduktape.so` | motor JS Duktape (interpreta la lógica Veritran) |
| `libsmma.so` | core de la plataforma Veritran / biometría (SMMA) |
| `libtool-checker.so` | **RASP** — detección de root/Frida/emulador/debug |
| `libsqlcipher.so` | base de datos local cifrada (SQLCipher) |
| `libconscrypt_jni.so` | TLS (Conscrypt) |
| `libRSSupport.so`, `librsjni*.so` | RenderScript |
| `libyoga.so` | layout (Yoga) |

## Qué se puede documentar sin ejecutar

- **Biometría on-device (doc 03):** 20 modelos en `assets/smma/model/` — facial con
  **liveness/anti-spoofing** (`liveness_ensemble`, `spoof_detect_nn`, `impostor`,
  `motion_liveness_nn`, `glasses_detector`) y **verificación de voz** (`voice-tssv`).
- **Protección (doc 04):** RASP (`tool-checker`), SQLCipher, Conscrypt.
- **Permisos (doc 01):** cámara, micrófono, huella, NFC, ubicación fina, contactos, teléfono —
  amplios pero consistentes con onboarding biométrico y banca.
- **Firma (doc 05):** auténtica del emisor → binario íntegro.

## Qué NO se puede (el muro)

- Rutas de API / endpoints (no están en el DEX; viven en el bundle Veritran interpretado).
- Lógica de transacciones, auth, cifrado de llaves — todo interpretado por Duktape.
- Requiere análisis dinámico instrumentando el intérprete y evadiendo el RASP (fuera de
  alcance; exigiría root). Documentado honestamente como límite, sin inventar.

## Lectura

Como Banco del Bienestar, es una app **Veritran no decompilable**. La diferencia útil: aquí el
binario es **auténtico** y el SDK biométrico (facial + voz, con anti-spoofing) **sí** es
observable y vale la pena registrar — es de las apps gob.mx con autenticación más sofisticada.
