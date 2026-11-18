# Ingeniería inversa de APP QRO 9.0.8 — índice

Análisis **estático** del APK `appqro.queretaro.gob.mx` con REA (JADX + inspección de archivo).
La app **nunca se ejecutó**. Es la super-app oficial del **Poder Ejecutivo del Estado de
Querétaro** (servicios y trámites estatales). **#34 por instalaciones** del inventario gob.mx
(≈100 K).

> **Flutter — lógica en el snapshot Dart (muro).** `assets/flutter_assets/` + `libapp.so`
> (Dart AOT) + `libflutter.so`. La lógica de negocio y el backend propio viven en el snapshot
> Dart, **no en el DEX** → no decompilable estáticamente. Lo observable es la **amplísima
> superficie de permisos** (ubicación en segundo plano, cámara, micrófono, contactos, biometría,
> alarmas exactas) y los SDKs (Firebase Remote Config/FCM, ExoPlayer, ML translate). Firma
> re-firmada por APKPure.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, permisos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | **Permisos amplios** de super-app, componentes |
| 02 | [02-flutter-muro.md](02-flutter-muro.md) | Flutter/Dart AOT, por qué no es decompilable |
| 03 | [03-red-sdks.md](03-red-sdks.md) | Firebase, ExoPlayer, ML translate; backend en el muro |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Re-firmada por APKPure, procedencia |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos y límites |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado), `evidence/firma.txt`
(no versionado).

## 4 hallazgos principales

1. **Flutter = lógica en el snapshot Dart (muro).** `libapp.so` (Dart AOT); el DEX es embedding.
   Backend propio (`*.queretaro.gob.mx`), auth y trámites **no observables estáticamente** — no
   aparecen en el DEX (solo endpoints de SDKs). Vía: reflutter/blutter o Frida (doc 02).
2. **(Medio) Superficie de permisos muy amplia.** Declara `ACCESS_FINE/COARSE_LOCATION` +
   **`FOREGROUND_SERVICE_LOCATION`** (ubicación en segundo plano), `CAMERA`, `RECORD_AUDIO`,
   `READ_CONTACTS`, `USE_BIOMETRIC`/`USE_FINGERPRINT`, `SCHEDULE_EXACT_ALARM`/`USE_EXACT_ALARM`,
   `READ_MEDIA_AUDIO`. Una super-app lo justifica en parte, pero es un conjunto de alto impacto
   para privacidad y el **uso exacto vive en el snapshot Dart** (no verificable) (doc 01).
3. **Firebase Remote Config + FCM.** La app usa **Firebase Remote Config** (configuración remota
   del comportamiento) + Cloud Messaging + Installations. El backend de contenido/trámites no
   está en claro en el DEX (doc 03).
4. **Transporte sin cleartext declarado.** El manifiesto **no** declara `usesCleartextTraffic`
   (targetSdk 36 → HTTP bloqueado por defecto). Firma **re-firmada por APKPure** → arquitectura
   sí, integridad no (doc 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. La lógica Dart no se desensambló (muro), documentado como límite sin inventar.
Firma re-firmada por APKPure → no verificable como auténtica del Gobierno de Querétaro.
