# 03 · `.env` y secretos

## El archivo

`assets/flutter_assets/.env` (771 bytes) se empaca en el APK — **legible con `unzip`**, como en Guía
Pemex. Claves presentes (valores **redactados**; no se reproducen en esta documentación):

| Clave | Qué es | Forma del valor |
|-------|--------|-----------------|
| `PRUEBAS` | flag prod/prueba | booleano |
| `APP_NAME` | nombre | texto |
| `ID_APP` | id de app | base64 (`…==`) |
| `API_EVENTOS` | endpoint eventos (prod) | blob cifrado/base64 |
| `API_EVENTOS_PRUEBAS` | endpoint eventos (prueba) | blob cifrado/base64 |
| `API_USUARIOS` | endpoint usuarios (prod) | blob cifrado/base64 |
| `API_PRUEBAS` | endpoint usuarios (prueba) | blob cifrado/base64 |
| `APP_KEY` | **secreto** | **217 caracteres** |

## Lectura honesta

- **A favor (vs Guía Pemex):** los endpoints **no** están en texto plano — van cifrados/base64. La
  app los descifra en runtime.
- **El problema:** el `.env` empaca **`APP_KEY` (217 chars) junto a los blobs cifrados**. Si
  `APP_KEY` es la clave que descifra los `API_*` (hipótesis razonable: clave y ciphertext viajando
  en el mismo archivo), entonces el cifrado **no aporta protección** — un atacante tiene ambos. Es
  el patrón clásico de "cifrar con la llave al lado" (**CWE-312/798**).
- **No confirmado:** que `APP_KEY` descifre específicamente los `API_*` **no se pudo verificar** por
  análisis estático — la rutina de descifrado está en el Dart AOT (`libapp.so`, muro). Se marca
  **A-CONFIRMAR**, no como fuga demostrada. Requiere dinámico.

## Severidad

**ALTA / A-CONFIRMAR.** Hay un secreto de 217 chars en el cliente junto a config cifrada. El techo
del daño depende de qué protege `APP_KEY` (clave de descifrado vs token de API), que el muro Dart
impide confirmar. Los valores concretos **no** se incluyen aquí.
