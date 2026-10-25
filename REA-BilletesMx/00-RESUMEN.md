# BilletesMx 5.5 — Resumen ejecutivo

Análisis **estático + dinámico** del APK `org.banxico.dge.movil` (BilletesMx): cadena REA
(JADX 0.7.1 + apktool 3.0.3), **Il2CppDumper** (`dump.cs` tipado), parser propio de
metadata IL2CPP, assets con UnityPy y **ejecución real en dispositivo por cable** (ver
[14](14-analisis-dinamico.md)). Es la app oficial pública del Banco de México (Banxico)
para conocer los billetes mexicanos mediante realidad aumentada.

## Identidad

| Campo | Valor |
|---|---|
| Paquete | `org.banxico.dge.movil` |
| Nombre visible | **BilletesMx** (`appCategory="game"`) |
| Versión | 5.5 (versionCode 136) |
| minSdk / targetSdk | 23 / 36 (compileSdk 36, "16") |
| Formato de entrega | XAPK (base + split `config.arm64_v8a`), 181 MB |
| ABI | arm64-v8a únicamente (sólo el split arm64 en este XAPK) |
| Autor del paquete | Banxico — Dirección General de Emisión (`dge`) |
| SHA-256 base.apk | `23991c1a8dc2daa99f60fdef42d9f51e402c2b4bbdf66f4fefe8cc9458b88c46` |
| SHA-256 config arm64 | `1e7874c3e0193339ab08f681d33ae3d2fae8c8c7dfd97da88fdd2562f13b1658` |

## Pila tecnológica

BilletesMx **no es una app Android convencional**: es un juego/experiencia **Unity**
compilada con **IL2CPP** (C# → C++ → ARM64 nativo) con el SDK de realidad aumentada
**EasyAR Sense** encima.

| Capa | Tecnología | Evidencia |
|---|---|---|
| Motor | Unity **6000.2.7f2** (IL2CPP) | header UnityFS de `data.unity3d`; `libunity.so` (20 MB), `libil2cpp.so` (30 MB) |
| Metadata gestionada | `global-metadata.dat` **versión 31** (magic `0xFAB11BAF`) | header verificado; 36,453 identificadores |
| Lógica propia | `Assembly-CSharp.dll` / `Assembly-CSharp-firstpass.dll` | `ScriptingAssemblies.json` |
| Realidad aumentada | **EasyAR Sense** | `libEasyAR.so` (8 MB), `cn.easyar.Engine`, 289 ids `easyar`, logs `[EasyAR]` |
| Animación | **iTween** | literal `http://itween.pixelplacement.com`; `ButterflyManager`, `Garza/Manglar/Ajolote` |
| Accesibilidad | **UAP** (UI Accessibility Plugin) + **Google Cloud TTS** | `UAP_AccessibilityManager`, `texttospeech.googleapis.com` |
| Voz Java mínima | sólo `com.unity3d.player.UnityPlayerActivity` + `R` | 727 clases DEX, 1 clase propia (`R`) |
| Servicios Unity | Unity Ads / Services (empaquetado) | activities `com.unity3d.services.ads.*`, permiso `AD_ID` |

El código propio de Banxico vive **íntegramente en C# compilado a nativo** dentro de
`libil2cpp.so`; la capa Java es sólo el arranque estándar de Unity. Por eso JADX sólo
ve 727 clases (runtime de Unity + Google Play) y una sola clase del paquete (`R`).

## Qué hace la app

Apuntas la cámara a un billete mexicano y la app lo **reconoce por imagen** (targets
locales de EasyAR, sin nube) y superpone en 3D/2D el **ecosistema del billete de la
familia G**, con audio y animación:

- **$20** — manglar / humedales de Sian Ka'an (`ManglarBehaviour`)
- **$50** — ajolote de Xochimilco (`audioAjolote`, `actualPosParticulasAjolotes`)
- animaciones adicionales de fauna (`GarzaBehaviour`, `ButterflyManager`)

Lanzada en **septiembre de 2019** con el billete de **$200 de la familia G**, su fin es
**educativo**: resaltar y explicar las características de seguridad (denominación
multicolor, hilo dinámico, marca de agua, relieves al tacto, fluorescencia UV). **No es un
autenticador**: Banxico aclara que no verifica la autenticidad de piezas en circulación, y
este análisis técnico lo confirma (no hay lógica forense, sólo reconocimiento de imagen
para disparar contenido; ver [13](13-proyectos-similares.md)).

Además: catálogo de billetes (`BilleteManager`, `bills.json` — familias F y G con sus
características de seguridad por cara), modo de accesibilidad que **lee en voz alta** la
información de cada billete para personas con discapacidad visual, y enlaces a los
sitios informativos de Banxico.

## Mapa de módulos (clases propias recuperadas de metadata)

```
SharedManager (singleton)
├── BilleteManager ........... catálogo; GetActiveBills, CreateDictionaryBill, DoSortBills, GetBill
│     └── Bill / BilleteInfo / AccessibleBill .... modelo de billete (denominación, familia, caras)
├── MenuManager / ContextualMenuManager / SceneManager .... navegación y escenas
├── EasyARBehaviour .......... sesión AR; ImageTargetController, SurfaceTargetController
│     ├── ImageTarget / CameraDevice / ARSession (namespace easyar)
│     ├── ARTargetFound / ARTargetLost / LateARTargetFound .... ciclo de reconocimiento
│     └── ARGifAnimation / ARMaterial / ARShader .............. overlays
├── Animaciones de ecosistema: ButterflyManager, GarzaBehaviour, ManglarBehaviour, Ajolote
├── AudioManager_AR / AudioBehaviour .......... audio contextual AR
├── AccesibilityBehaviour / UAP_AccessibilityManager + Google TTS .... accesibilidad por voz
└── Botones AR: ButtonARBehavior, ButtonARBMBehavior; WorkAreaBehaviour
```

## 5 hallazgos principales

1. **Arquitectura Unity IL2CPP + EasyAR (RA local, sin nube).** El reconocimiento de
   billetes se hace contra **targets de imagen embebidos** (las referencias
   `assets/*_Anverso/Reverso.jpg`), no contra el servicio Cloud de EasyAR: las claves
   de nube son los **placeholders de muestra** del SDK (`--YOUR CLOUD KEY HERE--`,
   `--YOUR CLOUD SECRET HERE--`). Funciona **offline**. Ver [02](02-unity-il2cpp.md) y
   [04](04-easyar-ar.md).

2. **Superficie de ataque mínima.** Un único componente exportado:
   `com.unity3d.player.UnityPlayerActivity` (sólo por ser LAUNCHER). **Sin** deep
   links, **sin** content providers, **sin** servicios ni receivers propios, **sin**
   `FileProvider`, **sin** `networkSecurityConfig` (usa los defaults de plataforma, que
   en targetSdk 36 bloquean cleartext por defecto). Ver [01](01-manifiesto.md).

3. **Accesibilidad para débiles visuales, sin clave Google embebida.** Integra UAP y
   `AccessibleBill`; el campo `UAP_AccessibilityManager.m_GoogleTTSAPIKey` existe pero va
   **vacío** (0 claves `AIza` en los 13,722 objetos del bundle), con `AndroidTTS` nativo
   como alternativa. Hallazgo **positivo**: no hay clave de Google expuesta en el APK. Ver
   [11](11-accesibilidad-tts.md).

4. **Perfil de permisos conservador.** `CAMERA` (necesaria para la RA), `INTERNET`,
   `ACCESS_NETWORK_STATE`, `VIBRATE`, y `READ/WRITE_EXTERNAL_STORAGE` heredados (sin
   `maxSdkVersion`, amplios en SDK 23). **No** pide ubicación, micrófono, contactos ni
   almacenamiento de fotos moderno. Ver [01](01-manifiesto.md) y [12](12-privacidad-hallazgos.md).

5. **Telemetría de Unity activa + residuos.** El bundle trae **endpoints de Unity Gaming
   Services** configurados: analytics (`uca.cloud.unity3d.com`), diagnóstico (`cdp…`),
   rendimiento (`perf-events…`) y **crash** (`engine-data…/stack-traces`). Además, SDK de
   **Unity Ads** + permiso `AD_ID` sin publicidad observable (residuo), y enlaces
   informativos `http://www.banxico.org.mx/…` en **texto claro**. La licencia **EasyAR**
   (token de 1,016 chars, ligada al paquete por diseño) va embebida en `data.unity3d`. Ver
   [08](08-telemetria-ads.md), [05](05-red-endpoints.md) y [04](04-easyar-ar.md).

## Alcance, propósito y límites legales

Este directorio contiene **análisis original** (observaciones, inventarios y notas de
ingeniería inversa **estática**) sobre un APK **público y gratuito** distribuido por
Banxico en Google Play. No se redistribuye el APK ni sus assets con derechos; los
artefactos extraídos se conservan sólo como evidencia de las afirmaciones y pueden
excluirse del control de versiones. No se ejecutó la aplicación, no se atacó
infraestructura de Banxico y no se extrajo ni se publicó ninguna credencial. El fin es
educativo y de transparencia sobre software de gobierno. Las afirmaciones se limitan a
lo observado estáticamente; donde la evidencia no alcanza, se dice explícitamente.
