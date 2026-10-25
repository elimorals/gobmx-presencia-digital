# 03 — Protección: packer Baidu (LianTian / Baidu Protect)

Urbani está endurecida con el packer chino **Baidu LianTian** (联天 / "Baidu App Shield",
comercialmente *Baidu Protect*). A diferencia de PairIP (App CDMX, anti-tamper sin cifrar strings)
o DexGuard (CFE, cifra strings del DEX), aquí lo central es que **cifra el snapshot Dart AOT**
(`libapp.so`), que es donde vive toda la lógica de una app Flutter.

## Evidencia — cifrado del Dart AOT

Medición directa sobre `libapp.so` (29,016,941 bytes), guardada en
[`evidence/libapp-entropia.txt`](evidence/libapp-entropia.txt):

```
entropía global = 7.9954 bits/byte   (máximo teórico 8.0; uniforme en los 8 bloques: 7.92–8.00)
magic snapshot Dart (f5 f5 dc dc) = 0 ocurrencias
secciones de snapshot detectadas  = 0 (una AOT completa tiene 2: VM + isolate)
```

Entropía ~8.0 uniforme = **datos cifrados o comprimidos sin cabeceras en claro**. Las 345,139
"strings" que extrae `strings` son ruido de alta entropía (p. ej. `tta~m`, `3w$h`), no texto. Una
`libapp.so` Flutter normal estaría llena de URIs `package:`/`dart:` y de las URLs de la app. **No
hay ninguna.** Conclusión sostenida por la evidencia: **el AOT de Dart está cifrado** y se descifra
en runtime. **No sé** con qué algoritmo ni dónde está la llave — eso exigiría el runtime.

## Evidencia — shell nativo y de DEX

Librerías del packer en `config.armeabi_v7a` (doc 08 lista todas):

```
lib/armeabi-v7a/libliantian.so                 50,956
lib/armeabi-v7a/libliantiansec.so             380,708
lib/armeabi-v7a/libbaiduprotect_sdk-f57be64359f2f36931a46c7827e036d2c2796441.so   125,764
lib/armeabi-v7a/libbaiduprotect_sec_jni_v3_lite_face.so   50,748
assets/liantiancontent.properties             (config del packer)
```

~187 clases `com.baidu.liantian.*` en el DEX (vía JADX). Las relevantes:

| Clase | Rol inferido por nombre |
|---|---|
| `com.baidu.liantian.LiantianProvider` / `LiantianReceiver` | puntos de arranque (manifest) |
| `com.baidu.liantian.x0.jni.{Engine, EngineProxy, Native, LoadEngineHelper}` | carga del motor nativo (descifra/ejecuta) |
| `com.baidu.liantian.x0.jni.NativeCrashIntercept` | intercepción de crashes nativos |
| `com.baidu.liantian.x6.jni.EmulatorAcquisition` | **detección de emulador** (anti-análisis) |
| `com.baidu.liantian.utility.WbEncryptUtil` | utilidad de cifrado (white-box) |
| `com.baidu.liantian.core.{ApkInfo, CallArgs}` | verificación de integridad del APK |

La `Application` declarada (`o.access301`) y `zygotePreloadName` (`o.AppCompatTextHelperApi24Impl`)
son el envoltorio del packer: el proceso arranca en el shell, que carga el motor nativo y entrega
el control a la app real.

## Qué bloquea y qué no

- **Sí bloquea:** el análisis estático de la **lógica Dart** (endpoints del backend Urbani, cripto
  de la tarjeta de transporte, flujos de recarga/pago, validación de deeplink). Todo eso está en
  `libapp.so` cifrado.
- **No bloquea:** el **DEX Java** sigue siendo parseable por JADX (33,861 clases con nombres de
  plugins y SDKs intactos). El packer protege su propio shell y cifra el AOT, pero no reescribe los
  nombres de los plugins Flutter ni de los SDKs de terceros. Por eso los docs 05–08 sí mapean la
  superficie de SDKs.

> **Implicación:** se recupera *qué* capacidades tiene la app (mapas, NFC, pago, KYC, Firebase) por
> sus SDKs, pero **no** *cómo* las orquesta ni contra qué servidores. Lo segundo requiere runtime,
> que además tendría que sortear el `EmulatorAcquisition` y el anti-tamper de LianTian.

## Nota sobre `libed66.so`

`lib/armeabi-v7a/libed66.so` (1,438,000 bytes) tiene nombre no estándar y no se identificó su
origen ni función con la evidencia disponible. **No lo atribuyo** a ningún SDK sin más datos; queda
como desconocido para un pase futuro.
