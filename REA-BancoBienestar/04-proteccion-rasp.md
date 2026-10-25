# 04 · Protección en runtime (RASP) y packer

El contraste de seguridad más marcado del pipeline: esta app está **endurecida**. Evidencia en
`evidence/rasp-strings.txt` (conteo de strings en smali).

## Anti-tampering / RASP

| Mecanismo | Evidencia (refs en smali) |
|---|---|
| **Detección de root** | `RootBeer`/`rootbeer` (64), `isRooted` (12), `/su` (7) |
| **Detección de Xposed** | `xposed` (182) |
| **Detección de emulador** | `Emulator`/`EMULATOR` (81) |
| **Detección de debugger** | `Debugger`/`DEBUGGER` (78) |
| **Detección de Frida** | `Frida` (1) |
| **Anti-captura de pantalla** | permiso `DETECT_SCREEN_CAPTURE` (doc 01) |
| **Librería de seguridad Veritran** | `net/veritran/android_vtsecuritylibrary` |
| **Checker nativo** | `libtoolChecker.so` |

Es un stack RASP típico de banca: la app intenta detectar entornos de análisis/instrumentación
(root, Xposed, Frida, emulador, debugger) y probablemente degrada o bloquea su operación en ellos.

## Packer / ofuscación nativa

- `.so` de **nombre aleatorio**: `libnjhpdqdxvc.so`, `libxmsqibvoii.so` (en el split arm64).
- Payloads **empacados** `.szip` en assets: `njhpdqdxvc.szip`, `xmsqibvoii.szip`.
- Referencias cruzadas intensas desde smali (`njhpdqdxvc` 200, `xmsqibvoii` 91) + motor **Duktape**
  (`libduktape.so`, 112 refs) → los módulos nativos cargan/descifran los `.szip` y ejecutan la
  lógica Veritran (doc 02).

Interpretación: combinación de **plataforma Veritran protegida** + packer nativo. No se identificó un
nombre comercial de packer de terceros con certeza; los artefactos (nombres aleatorios, `.szip`,
`toolChecker`) son consistentes con el propio endurecimiento de Veritran. **No se afirma** un vendor
concreto de packer sin más evidencia.

## Implicación para análisis dinámico

Cualquier intento de dinámica (Frida/MITM) sobre esta app **chocará con el RASP**: habría que
neutralizar la detección de root/Frida/debugger primero (unpinning + anti-RASP). Es un objetivo de
dificultad alta, a diferencia de Licencia Federal Digital (sin protección).

## Límite honesto

- Los conteos son de **strings** en el DEX: prueban que el código de detección **existe**; no se
  verificó en runtime **cuáles** se ejecutan ni su reacción exacta. No se desempacaron los `.szip`.
