# 13 — Contexto, estado del arte y aporte

## Qué es (contexto oficial)

BilletesMx es la app oficial del Banco de México, **lanzada en septiembre de 2019** junto
con el nuevo billete de **$200 de la familia G**. Su propósito es **educativo**: usar
realidad aumentada para **resaltar y explicar las características de seguridad** de los
billetes de la familia G (denominación multicolor, hilo dinámico, marca de agua, relieves
sensibles al tacto, fluorescencia bajo luz UV), además de su contenido histórico/natural.

**No es un autenticador.** Banxico ha aclarado expresamente que la app **no** está
diseñada para verificar la autenticidad de piezas en circulación; la propia app muestra un
aviso de que la realidad aumentada no garantiza la autenticidad del billete. Parte de la
prensa de lanzamiento la presentó como "detector de billetes falsos", lo cual Banxico
corrigió: es una herramienta de divulgación. Este análisis técnico **confirma** ese
encuadre: no hay lógica de verificación forense, sino reconocimiento de imagen (EasyAR)
para disparar contenido educativo (ver [04](04-easyar-ar.md), [06](06-datos-bills-json.md)).
El propio código lo dice: entre los literales C# recuperados está la cadena **"Esta
aplicación no autentica billetes."** (`stringliteral.json`).

> La ficha de la App Store de la época reportaba la versión iOS 5.5.1 (~257 MB); el
> artefacto aquí analizado es el **APK Android v5.5 (build 136)**.

## Estado del arte (categoría)

BilletesMx pertenece a la familia de **apps de banco central sobre billetes**, que suele
cubrir tres funciones, a veces combinadas:

1. **Divulgación / educación** con RA sobre características de seguridad (el caso de
   BilletesMx).
2. **Verificación de autenticidad** asistida (apps que guían la revisión manual; ninguna
   app de consumo autentica con certeza).
3. **Accesibilidad**: identificar denominación por voz para personas con discapacidad
   visual.

Varios bancos centrales y entidades han publicado apps en estas líneas (p. ej.
identificadores de denominación por accesibilidad y "lupas"/checkers de seguridad). No se
enumeran versiones ni tecnologías concretas de terceros aquí porque **no se verificaron en
este análisis**; nombrarlas sin comprobarlas sería especulación.

Lo notable de BilletesMx frente a la categoría: combina **(1) divulgación con RA** y
**(3) accesibilidad por voz** (UAP + Google TTS; ver [11](11-accesibilidad-tts.md)) en una
sola app, y lo hace **offline** (RA local con EasyAR, sin enviar la imagen de la cámara a
la nube).

## Aporte de este repositorio

- **Metodología de RE para una app Unity/IL2CPP de gobierno**: identificación del stack
  (IL2CPP v31 + EasyAR) partiendo de un APK con **una sola clase Java propia**, y
  recuperación del mapa de clases C# desde `global-metadata.dat` con **extractores
  propios** (sin depender de compilar herramientas externas; ver [02](02-unity-il2cpp.md)).
- **Inventario verificable**: permisos, superficie de ataque, endpoints, firma y assets,
  cada afirmación anclada a evidencia en [`evidence/`](evidence/).
- **Separación honesta** entre lo observado estáticamente y lo que requiere un pase
  dinámico o decompilación nativa (marcado "por verificar" en [12](12-privacidad-hallazgos.md)).

## Fuentes (contexto)

- [We Are Mitú — BilletesMX AR app](https://wearemitu.com/wearemitu/culture/billetesmx-money-app/)
- [El Diario NY — Banxico lanza app (2019)](https://eldiariony.com/2019/09/04/banco-de-mexico-lanza-app-para-dectectar-billetes-falsos/)
- [IDC Online — "no está diseñada para autenticar piezas en circulación"](https://idconline.mx/corporativo/2019/09/13/la-nueva-app-de-banxico-identifica-billetes-falsos)
- [The Guadalajara Reporter — "helpful, not failsafe"](https://theguadalajarareporter.net/index.php/featured/54144-fake-bill-detection-app-helpful-not-failsafe)
