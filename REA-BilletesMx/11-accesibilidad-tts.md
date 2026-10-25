# 11 — Accesibilidad y Text-to-Speech

Una función distintiva de BilletesMx es su **modo de accesibilidad para personas con
discapacidad visual**: lee en voz alta la información de cada billete. Banxico tiene un
mandato de accesibilidad para la identificación de billetes, y esta app lo implementa.

## Componentes

| Pieza | Evidencia | Rol |
|---|---|---|
| **UAP** (UI Accessibility Plugin) | `UAP_AccessibilityManager` en metadata | Plugin Unity de accesibilidad (lector de pantalla, navegación por foco). |
| `AccesibilityBehaviour` | metadata | Comportamiento de accesibilidad en escena. |
| `AccessibleBill` | metadata | Variante accesible del modelo de billete (texto a leer). |
| **Google Cloud TTS** | literal `https://texttospeech.googleapis.com/v1/text:synthesize?key=` | Síntesis de voz del texto del billete. |
| Localización | literal `[Accessibility] No localization available for key '` | Gestión de textos accesibles por clave. |

## Flujo (inferido de símbolos + endpoint)

1. El usuario activa el modo accesible (UAP toma el control de foco/navegación).
2. Al seleccionar/reconocer un billete, `AccessibleBill` compone el texto a leer a partir
   de `bills.json` (`infoFront`, `infoBack`, `infoUV`, `descriptionElement` de cada
   `element`; ver [06](06-datos-bills-json.md)).
3. La app envía ese texto a **Google Cloud Text-to-Speech**
   (`v1/text:synthesize?key=<API_KEY>`) y reproduce el audio devuelto.

## Manejo de la clave de API — sin clave embebida

El campo que guardaría la clave **existe y está localizado**: `UAP_AccessibilityManager.
m_GoogleTTSAPIKey` (campo serializado de Unity; recuperado del dump estructural). Sin
embargo, al extraer el assetbundle con UnityPy y **barrer los 13,722 objetos** de
`data.unity3d`, **no aparece ninguna clave de Google (`AIza…`)** en ningún
`MonoBehaviour`, ni en el DEX ni en `global-metadata.dat`. Es decir: **el campo
`m_GoogleTTSAPIKey` viaja vacío** en el build analizado.

Consecuencias (observadas estáticamente):

- No hay una **clave de Google Cloud expuesta** en el APK — bien, descarta el riesgo de
  abuso de cuota por clave filtrada.
- La ruta de Google TTS (`v1/text:synthesize?key=`) **requiere una clave para funcionar**;
  si el campo está vacío, el TTS por Google **no operaría** y la app recaería en el **TTS
  nativo de Android** (`AndroidTTS.GetTTSStatus()`, `UAP_CustomTTS.InitializeCustomTTS()`,
  que sí están en el código) o la clave se inyecta en runtime por un canal no visible
  estáticamente.

**No se afirma el valor de una clave: no la hay en los artefactos.** Distinguir "campo
vacío → usa Android TTS" de "clave inyectada en runtime" requeriría **análisis dinámico**
(capturar si sale una petición a `texttospeech.googleapis.com`).

## Observación de diseño

Depender de un **servicio en la nube de Google** para una función de accesibilidad de una
app de gobierno tiene dos implicaciones: (1) **disponibilidad** — sin conexión, el TTS por
Google no funciona (aunque Android tiene TTS local nativo que podría usarse como
alternativa), y (2) **dependencia externa** — el texto del billete (no datos personales)
se envía a Google para sintetizar. Es una decisión razonable por calidad de voz, pero
anotarla es parte del análisis de privacidad ([12](12-privacidad-hallazgos.md)).
