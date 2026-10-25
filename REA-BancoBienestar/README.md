# REA-BancoBienestar · Banco del Bienestar Móvil (`gob.bancodelbienestar.bcobienapp`)

Ingeniería inversa **estática** de la app oficial **Banco del Bienestar Móvil**, v7.17.0.52467.
La app nunca se ejecuta. Parte del proyecto `gob-mx-reverse-engineering`.

## TL;DR

App **nativa Android construida sobre la plataforma low-code Veritran**: su namespace propio tiene
**solo 2 clases**; la lógica bancaria se interpreta vía **Duktape** desde payloads **`.szip`
empacados**, no es decompilable como Kotlin. Fuertemente **endurecida (RASP)**: detección de root
(RootBeer), **Xposed**, emulador, debugger, **Frida**, `libtoolChecker.so`, packer nativo con `.so`
de nombre aleatorio y `DETECT_SCREEN_CAPTURE`. **Sin endpoints en texto plano** (config Veritran en
runtime) y **network_security_config vacío**. Cripto: SQLCipher + `vtcrypto` + Conscrypt (llave no
observable en estático). Superficie de datos amplia: `READ_CONTACTS`, `READ_PHONE_STATE`, ubicación,
biometría, Advertising-ID.

> **Procedencia:** el archivo del SSD con este nombre era **Aptoide mal nombrado**. El APK real se
> obtuvo instalando la app desde **Google Play** en un Motorola y extrayéndola con `adb pull`.

## Índice

| Doc | Contenido |
|---|---|
| [00-RESUMEN](00-RESUMEN.md) | Identidad, Veritran, pila, hallazgos, contraste |
| [01-manifiesto](01-manifiesto.md) | 34 permisos, componentes, deeplinks (`bancodelbienestar://`, App Links, NFC) |
| [02-arquitectura-veritran](02-arquitectura-veritran.md) | Plataforma Veritran, Duktape, `.szip`, por qué la lógica no se decompila |
| [03-red-endpoints](03-red-endpoints.md) | Sin endpoint en claro, NSC vacío, Conscrypt, Retrofit/OkHttp |
| [04-proteccion-rasp](04-proteccion-rasp.md) | **Root/Xposed/Frida/emulador/debugger, packer nativo** |
| [05-cripto-almacenamiento](05-cripto-almacenamiento.md) | SQLCipher/Zetetic, vtcrypto, SpongyCastle, Conscrypt |
| [06-sdks-dependencias](06-sdks-dependencias.md) | Firebase, ML Kit, SMS Retriever, Advertising-ID, WorkManager |
| [07-permisos-datos](07-permisos-datos.md) | Superficie de datos y privacidad |
| [08-firma-recursos](08-firma-recursos.md) | Firma, splits, hashes, limitaciones |

## Artefacto

| | |
|---|---|
| Package | `gob.bancodelbienestar.bcobienapp` |
| Versión | 7.17.0.52467 (versionCode 5246790) |
| APK base | 33 MB · SHA-256 `4b928b2a31da00d57fc8684e11980fc87e24f449a9c26b5015e576a880b1985f` |
| Bundle | base + splits `arm64_v8a` / `en` / `xxhdpi` |
| minSdk / targetSdk | 29 / 36 |
| Firebase | `app-banco-del-bienestar` |

## Metodología

apktool 3.0.3 + JADX headless (REA). Package verificado con ambas herramientas **antes** de
documentar (tras el incidente del APK Aptoide mal nombrado). Análisis **estático**: la lógica
Veritran empacada y el gateway requieren dinámica/desempacado, bloqueados por el RASP (doc 08).

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
