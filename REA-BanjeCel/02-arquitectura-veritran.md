# Arquitectura Veritran — por qué no es decompilable

## La señal

- Package `…common.VTUserApplicationBJCTMB` → el prefijo **`VT`** es la convención de
  **Veritran**, plataforma low-code para banca móvil (misma que Banco del Bienestar).
- `lib/*/libduktape.so` presente en las 4 ABIs (arm64, armeabi-v7a, x86, x86_64) →
  **Duktape**, un motor JavaScript embebido.
- `libsmma.so` → runtime/SDK de Veritran (SMMA).

## Qué implica

En una app Veritran, la **lógica de negocio no se compila a DEX**: se define como un bundle
(pantallas, flujos, reglas, llamadas a API) que el runtime **interpreta en ejecución** con el
motor Duktape. El DEX contiene el *host* (el intérprete, los bridges nativos, la UI base), pero
no las transacciones, el login, las rutas de API ni el cifrado de llaves.

Evidencia estática consistente con esto:
- **Sin endpoints en claro en el DEX.** Un barrido de `strings` sobre `classes*.dex` no arrojó
  dominios/rutas de API propias (solo el namespace XMP de Adobe, irrelevante). En una app
  nativa normal, las URLs de la API aparecen en el DEX; aquí no, porque viven en el bundle
  interpretado.
- Los assets de nivel superior son solo `assets/dexopt` y `assets/smma` (modelos biométricos,
  doc 03). El bundle de lógica Veritran no está como texto plano decompilable: está empacado
  y/o cifrado y lo resuelve el runtime.

## El muro

Para recuperar la lógica, los endpoints y el manejo de credenciales haría falta:
1. instrumentar el **intérprete Duktape** en ejecución (hook a la carga/evaluación del bundle),
2. **evadir el RASP** (`libtool-checker.so`, doc 04), que detecta Frida/root/emulador,
3. en la práctica, **root** o un entorno controlado.

Todo eso es **análisis dinámico fuera del alcance** de este repositorio (estático, sin root).
Se documenta el muro y su vía de superación, **sin inventar** el contenido del bundle. Es el
mismo veredicto que Banco del Bienestar: *lógica bancaria interpretada, no decompilable*.
