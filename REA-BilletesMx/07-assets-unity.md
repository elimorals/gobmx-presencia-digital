# 07 — Assets Unity y multimedia

El grueso de los 162 MB del APK base son **assets Unity y multimedia**, no código.

## Imágenes de referencia (targets AR)

13 imágenes de billete en `assets/` — las que EasyAR usa como **image targets** y la app
muestra como fichas. Nomenclatura `<denom>G_Anverso/Reverso.jpg` para la familia G:

```
20G_Anverso.jpg   20G_Reverso.jpg
50G_Anverso.jpg   50G_Reverso.jpg
100G_Anverso.jpg  100G_Reverso.jpg
200G_Anverso.jpg  200G_Anverso1.png  200G_Reverso.jpg
500G_Anverso.jpg  500G_Reverso.jpg
1000G_Anverso.jpg 1000G_Reverso.jpg
```

Son los 6 billetes de la familia G (anverso + reverso), más una variante
`200G_Anverso1.png`. Enlazan con `urlImgFront/urlImgBack/urlImgUv` de `bills.json`.

## Elementos animados

`assets/AnimatedElements/` — **64 GIFs** (`2.gif`, `10.gif`, …, hasta `515.gif`; algunos
de varios MB). Son las animaciones de las **características de seguridad** y del ecosistema
que se superponen en RA. Enlazan con los `elements[].urlMultimedia` cuyo `isGif=true` en
`bills.json` (ver [06](06-datos-bills-json.md)).

## Assetbundles Unity (`assets/bin/Data/`)

| Archivo | Tamaño | Contenido |
|---|---|---|
| `data.unity3d` | 61 MB | Assetbundle principal: escenas, prefabs, materiales, texturas, **audio** y configuraciones serializadas (incluida la config/clave de EasyAR). Comprimido. |
| `resources.resource` | 5.2 MB | Recursos empaquetados (texturas/audio). |
| `sharedassets0.resource` | 45 KB | Assets compartidos. |

No hay archivos de audio sueltos (`.mp3/.wav/.ogg`) en el APK: el audio (p. ej.
`audioAjolote`) está **dentro** de `data.unity3d`/`resources.resource` como `AudioClip`
de Unity. Extraerlos requeriría una herramienta de assetbundles (AssetStudio/UnityPy);
no se hizo en este pase estático.

## Librerías de terceros embebidas (por metadata)

- **TextMeshPro** (`Unity.TextMeshPro.dll`) — tipografía.
- **Timeline** (`Unity.Timeline.dll`) — secuencias de animación.
- **AI Navigation** (`Unity.AI.Navigation.dll`) — navegación (presente; uso no confirmado).
- **iTween** — animación por tweening.
- **EasyAR Sense** — RA (ver [04](04-easyar-ar.md)).
- **UAP** — accesibilidad (ver [11](11-accesibilidad-tts.md)).

## Lectura

El peso del APK se justifica por el contenido multimedia de alta resolución (imágenes de
billete + 64 GIFs + assetbundle de 61 MB), no por código. Todo es **local**: coherente con
una experiencia AR que funciona **offline** una vez instalada.
