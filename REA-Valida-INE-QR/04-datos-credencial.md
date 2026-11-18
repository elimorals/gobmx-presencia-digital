# 04 — Datos de la credencial extraídos del QR

El QR de la credencial para votar del INE codifica datos de identificación. La app los
decodifica (en el núcleo nativo, doc 02) y los parsea a un objeto de datos para mostrarlos.

## Campos detectados

Strings del DEX (`classes.dex`) muestran el modelo de datos y las etiquetas de UI:

```
, cic=     , curp=    , ocr=     , seccion=   , vigencia=        ← toString del modelo
cic_  curp_  ocr_  seccion                                        ← claves internas
tvCIC  tvCURP  tvOCR                                              ← TextViews de resultados
```

| Campo | Significado | Fuente |
|---|---|---|
| **CIC** | Código de Identificación de Credencial (identificador único del plástico) | QR |
| **CURP** | Clave Única de Registro de Población | QR |
| **OCR** | Número OCR impreso al reverso de la credencial | QR |
| **sección** | Sección electoral del ciudadano | QR |
| **vigencia** | Vigencia de la credencial | QR |

El método `com.personalcode.Azf.l1(String): ce` parsea el resultado del escaneo al objeto de
datos `ce`, que es el que lleva estos campos a `ResultsActivity` (`tvCIC`, `tvCURP`, `tvOCR`).

> Nota: son los campos que la app **parsea y muestra en pantalla**; se observan por sus claves
> y etiquetas en el DEX. El contenido real proviene del QR escaneado en tiempo de ejecución,
> que **no se capturó** (análisis estático, sin escanear credenciales reales).

## Catálogo de municipios

`assets/catalogo_municipios.txt` (81 KB) es una tabla del INE con el formato:

```
ENTIDAD|NOMBRE ENTIDAD|MUNICIPIO|NOMBRE MUNICIPIO
1|AGUASCALIENTES|1|AGUASCALIENTES
1|AGUASCALIENTES|2|ASIENTOS
...
```

Permite resolver **offline** los códigos numéricos de entidad/municipio (codificados en la
credencial/sección) a sus nombres, sin consultar un servicio. Es coherente con la arquitectura
sin red (doc 01/02). Muestra en `evidence/catalogo_municipios_muestra.txt`.

## Privacidad

Dado que **no hay permiso `INTERNET`** (doc 01) y no se detectó telemetría ni endpoints
(doc 02), los datos de la credencial escaneada **se procesan y muestran localmente** y no se
transmiten por red. El `allowBackup=false` (doc 01) evita además que queden en respaldos.
