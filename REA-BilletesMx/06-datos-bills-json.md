# 06 — Catálogo de billetes (`bills.json`)

El corazón de datos de la app es `assets/bills.json` (170 KB), un catálogo embebido que
`BilleteManager` carga y ordena (`GetActiveBills`, `CreateDictionaryBill`, `DoSortBills`,
`CompareBillDenomination`, `CompareBillFamily`). Copia en
[`evidence/assets/bills.json`](evidence/assets/bills.json).

- `update_date`: `1660040000000` → **2022-08-09** (fecha del catálogo embebido).
- **12 billetes**.

## Billetes catalogados

| id | Denom. | Familia | Material | Emisión | RA |
|---|---|---|---|---|---|
| 1 | $20 | F | Polímero | 2007-08-20 | — |
| 2 | $50 | F1 | Polímero | 2013-05-06 | — |
| 3 | $100 | F | Papel de algodón | 2010-08-09 | — |
| 4 | $200 | F | Papel de algodón | 2008-09-08 | — |
| 5 | $500 | F | Papel de algodón | 2010-08-30 | — |
| 6 | $1000 | F | Papel de algodón | 2008-04-07 | — |
| 11 | $500 | G | Papel de algodón | 2018-08-27 | ✓ |
| 12 | $200 | G | Papel de algodón | 2019-09-02 | ✓ |
| 13 | $1000 | G | Papel de algodón | 2020-11-19 | ✓ |
| 14 | $100 | G | Papel de algodón | 2020-11-12 | ✓ |
| 15 | $50 | G | Polímero | 2021-10-28 | ✓ |
| 16 | $20 | G | Polímero | 2021-09-24 | ✓ |

Reparto: **familia F** (5) + **F1** (1, el $50 polímero de 2013) + **familia G** (6, la
familia vigente). La realidad aumentada (`hasRA=true`) está en los **6 billetes de la
familia G**; los de la familia F son fichas informativas sin RA.

## Esquema de un billete

```jsonc
{
  "idBill": 15,
  "nameBill": "Billete de 50 pesos de la familia G",
  "denomination": 50,
  "family": "G",                // "F" | "F1" | "G"
  "material": "Polímero",       // "Polímero" | "Papel de algodón"
  "releaseDate": "2021-10-28",
  "urlImgFront": "...jpg", "urlImgBack": "...jpg", "urlImgUv": "buv15.jpg",
  "width": 625, "height": 325,  // dimensiones de la imagen de referencia (px)
  "infoFront": "…", "infoBack": "…", "infoUV": "…",  // descripciones por cara
  "hasRA": true,                // tiene experiencia de realidad aumentada
  "isActive": true,            // visible en el catálogo
  "isLocal": true,             // recursos embebidos en el APK (no descarga)
  "updateDate": 1634776702001,
  "elements": [ … ]            // características de seguridad interactivas
}
```

### `elements[]` — características de seguridad

Cada billete lista sus elementos de seguridad como **hotspots** sobre la imagen. Son los
que la app resalta (y, en la familia G, anima en RA). El $50 G trae **13 elementos**.

```jsonc
{
  "idElement": 258,
  "coordX": 72, "coordY": 178,      // posición del hotspot sobre la imagen del billete
  "width": 50, "height": 50,         // tamaño del hotspot
  "elementType": { "idElementType": 8, "nameElementType": "Ventana Transparente" },
  "descriptionElement": "Zona transparente rodeada del símbolo \"Ollin\" que cambia de color…",
  "urlMultimedia": "e1_b15.jpg",     // imagen o GIF que ilustra el elemento
  "isFront": true,                   // cara frontal / reverso
  "isDesign": false,
  "isRightLock": true,               // anclaje del hotspot
  "isGif": false,                    // si el multimedia es animado (ver AnimatedElements/)
  "isActive": true,
  "idBill": 15
}
```

Tipos de elemento observados incluyen, entre otros, **"Ventana Transparente"**
(`idElementType` 8). Los elementos con `isGif=true` enlazan a los GIFs de
`assets/AnimatedElements/` (64 archivos; ver [07](07-assets-unity.md)).

## Qué revela el catálogo

- La RA es **local y offline** para los 6 billetes G (`isLocal=true`, `hasRA=true`): sus
  imágenes de referencia, hotspots y multimedia viajan dentro del APK. Esto concuerda
  con que EasyAR Cloud **no** esté configurado (ver [04](04-easyar-ar.md)).
- El catálogo se puede **actualizar por red** (hay lógica de descarga — `bytesDownloaded`,
  `GetActiveBills`), pero la copia embebida es de 2022-08 y cubre la familia vigente.
- Los textos `infoFront/infoBack/infoUV` y `descriptionElement` son **contenido
  educativo oficial** de Banxico sobre las medidas de seguridad de cada billete; son el
  insumo que el modo accesibilidad lee en voz alta (ver [11](11-accesibilidad-tts.md)).

> Nota: `bills.json` es contenido de Banxico; se conserva como evidencia del modelo de
> datos, no para redistribución.
