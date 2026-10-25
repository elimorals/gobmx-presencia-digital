# Ingeniería inversa de BilletesMx 5.5 (`org.banxico.dge.movil`) — índice

Análisis **estático + dinámico** del APK de la app oficial del Banco de México
**BilletesMx**: cadena REA (JADX 0.7.1 + apktool 3.0.3), **Il2CppDumper** (`dump.cs`
tipado), parser propio de metadata IL2CPP, extracción de assets con UnityPy, y **ejecución
real en dispositivo Android por cable** (ver [14](14-analisis-dinamico.md)). Es una app
Unity **6000.2.7f2** (IL2CPP) con el SDK de realidad aumentada EasyAR que explica, en RA,
las características de seguridad de los billetes de la familia G.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila (Unity IL2CPP + EasyAR), mapa de módulos, 5 hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes, superficie de ataque (mínima) |
| 02 | [02-unity-il2cpp.md](02-unity-il2cpp.md) | Metadata IL2CPP v31, build Unity, reconstrucción realizada |
| 03 | [03-codigo-csharp.md](03-codigo-csharp.md) | Clases C# recuperadas: managers, modelo `Bill`, flujo AR, ecosistemas |
| 04 | [04-easyar-ar.md](04-easyar-ar.md) | EasyAR Sense: image targets locales, cloud **no** configurado, licencia, cámara |
| 05 | [05-red-endpoints.md](05-red-endpoints.md) | TLS/defaults, Google TTS, enlaces `http://` banxico.org.mx, store |
| 06 | [06-datos-bills-json.md](06-datos-bills-json.md) | Catálogo: 12 billetes (F/F1/G), campos y `elements` de seguridad |
| 07 | [07-assets-unity.md](07-assets-unity.md) | `data.unity3d`, 64 GIFs, 13 imágenes de referencia, audio, libs |
| 08 | [08-telemetria-ads.md](08-telemetria-ads.md) | Unity Analytics, residuo Unity Ads/`AD_ID`, sin SDKs de rastreo |
| 09 | [09-nativo-so.md](09-nativo-so.md) | `libil2cpp/libunity/libEasyAR/libmain`: cadena de carga, API IL2CPP |
| 10 | [10-firma-recursos.md](10-firma-recursos.md) | Firma v1 (cert Banxico, **SHA1withRSA débil**), stamp Play, recursos |
| 11 | [11-accesibilidad-tts.md](11-accesibilidad-tts.md) | UAP + Google Cloud TTS para discapacidad visual; manejo de la clave |
| 12 | [12-privacidad-hallazgos.md](12-privacidad-hallazgos.md) | Perfil de privacidad y hallazgos consolidados con severidad |
| 13 | [13-proyectos-similares.md](13-proyectos-similares.md) | Contexto oficial (no es autenticador), estado del arte, aporte |
| 14 | [14-analisis-dinamico.md](14-analisis-dinamico.md) | Ejecución real por cable: instalación, arranque Unity, **telemetría confirmada en runtime** |

## Artefactos (`evidence/`, `so-extract/`, `assets-extract/`)

| Ruta | Contenido |
|---|---|
| `evidence/AndroidManifest.xml` | Manifiesto decodificado (apktool) |
| `evidence/xapk-manifest.json` | Manifiesto del XAPK (splits, permisos, tamaños) |
| `evidence/hashes.sha256` | SHA-256 de base.apk y config.arm64_v8a.apk |
| `evidence/signer-cert.txt` | Certificado firmante (keytool) |
| `decompiled/il2cppdumper-out/dump.cs` | **Dump C# tipado** (Il2CppDumper): 4,667 clases con tipos + RVA |
| `decompiled/il2cppdumper-out/{script.json,il2cpp.h,stringliteral.json,DummyDll}` | Script Ghidra, cabeceras C, literales, DLLs reconstruidas |
| `decompiled/types-structured.md` / `.json` | Dump estructural (parser propio, validado) |
| `decompiled/app-classes.md` | Clases de app (global + `easyar` + `UAP_*`), 1,422 entradas |
| `evidence/dynamic/` | Logs de arranque, `analytics-config.json`/`values`, screenshot (ejecución real) |
| `evidence/metadata-identifiers.txt` | 36,453 identificadores IL2CPP (tipos/métodos/campos) |
| `evidence/metadata-string-literals.txt` | 5,807 literales C# (URLs, mensajes, config) |
| `evidence/easyar-key.txt` | Licencia EasyAR (redactada, **gitignored**) |
| `evidence/assets/bills.json` | Catálogo de billetes embebido |
| `evidence/assets/bin/Data/` | `global-metadata.dat`, `boot.config`, `ScriptingAssemblies.json`, … |
| `so-extract/lib/arm64-v8a/` | `libil2cpp.so`, `libunity.so`, `libEasyAR.so`, `libmain.so` |
| `assets-extract/assets/bin/Data/` | `data.unity3d`, `resources.resource`, `sharedassets0.resource` |

## Identidad

| | |
|---|---|
| Paquete / nombre | `org.banxico.dge.movil` / **BilletesMx** |
| Versión | 5.5 (build 136), minSdk 23 / targetSdk 36, ABI arm64-v8a |
| Motor | **Unity 6000.2.7f2** (IL2CPP, metadata v31) |
| Stack | Unity IL2CPP + EasyAR Sense + iTween + UAP (accesibilidad) + Unity Gaming Services |
| Firma | autofirmada **Banco de México** (RSA-2048, SHA1withRSA, 2012→2040) |
| SHA-256 base | `23991c1a8dc2daa99f60fdef42d9f51e402c2b4bbdf66f4fefe8cc9458b88c46` |

## 5 hallazgos principales

1. **Unity IL2CPP + EasyAR, RA local/offline.** Reconocimiento de billetes contra targets
   de imagen embebidos; EasyAR **Cloud no configurado** (claves placeholder de muestra).
   No envía la imagen de la cámara a la nube. ([02](02-unity-il2cpp.md), [04](04-easyar-ar.md))
2. **Superficie de ataque mínima.** Un único componente exportado (el launcher de Unity),
   sin deep links, providers, services ni receivers propios, sin `networkSecurityConfig`.
   ([01](01-manifiesto.md))
3. **Accesibilidad por voz (UAP), sin clave Google embebida.** Lee la info del billete
   para personas con discapacidad visual; el campo `m_GoogleTTSAPIKey` va **vacío** (0
   claves `AIza` en 13,722 objetos), con `AndroidTTS` nativo como alternativa — hallazgo
   positivo. ([11](11-accesibilidad-tts.md))
4. **Certificado de firma con SHA1withRSA (débil)**, autofirmado por Banco de México,
   válido hasta 2040; clave RSA-2048 adecuada. ([10](10-firma-recursos.md))
5. **Telemetría de Unity activa** (analytics/diagnóstico/rendimiento/crash con endpoints
   `*.cloud.unity3d.com`), más residuos de Unity Ads/`AD_ID` y enlaces `http://` claro a
   banxico.org.mx. Licencia EasyAR embebida (por diseño). ([08](08-telemetria-ads.md), [05](05-red-endpoints.md))

## Alcance, propósito y límites legales

Contiene **análisis original** (observaciones, inventarios y notas de RE **estática**)
sobre un APK **público y gratuito** de Banxico en Google Play. No se ejecutó la app, no se
atacó infraestructura, no se extrajo ni publicó ninguna credencial. Fin educativo y de
transparencia sobre software de gobierno. Las afirmaciones se limitan a lo observado; donde
la evidencia no alcanza (cuerpos de método, tráfico real, clave de Google), se dice
explícitamente y se marca como paso siguiente. Los artefactos extraídos se conservan como
evidencia y pueden excluirse del control de versiones (`so-extract/`, `assets-extract/` son
pesados).

---

↩ Parte de **[gob-mx-presencia-digital](../README.md)** — inventario y análisis de la presencia digital del Estado mexicano. Índice de todas las apps analizadas en [§ Ingeniería inversa de apps](../README.md#ingeniería-inversa-de-apps-rea).
