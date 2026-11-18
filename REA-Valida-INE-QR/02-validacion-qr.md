# 02 — Validación del QR: SDK PersonalCode y el núcleo nativo

## Flujo general (reconstruido de clases y manifiesto)

1. El usuario abre la app → `SplashActivity` → `HomeActivity`.
2. Se lanza `com.personalcode.ScannerActivity` (el escáner del SDK), que usa **CameraX**
   (`DecoratedCameraPreview`, `CameraOverlay`) para capturar cuadros de la cámara.
3. Los cuadros se pasan como `List<byte[]>` al método de procesamiento del SDK.
4. El SDK devuelve un `String` con el resultado; la app parsea los campos (CIC, CURP, OCR,
   sección, vigencia — doc 04) y los muestra en `ResultsActivity`, o va a `ErrorActivity`
   si el QR es inválido (hay un audio `res/raw/invalidqrs.mp3`).

## El motor es un SDK propietario: `com.personalcode.*`

Clases del paquete (de `search_android_classes`):

- `com.personalcode.ScannerActivity`, `CameraOverlay`, `DecoratedCameraPreview` — captura/UI.
- `com.personalcode.qrdetection.*`, `com.personalcode.scanner.*`, `com.personalcode.verification.*`
  — detección, escaneo y **verificación** (databinding generado + lógica).
- `com.personalcode.Azf`, `com.personalcode.lI` — fachada del SDK con nombres ofuscados.

### Fachada `com.personalcode.Azf` (métodos observados)

| Método | Firma | Rol |
|---|---|---|
| `pc` | `(Activity, List<byte[]>, boolean): String` | **Procesa el QR** — núcleo |
| `l1` | `(String): ce` | Parsea el resultado a un objeto de datos |
| `gImg` | `(String): Bitmap` | Obtiene imagen del código |
| `gTxt` | `(String): String` | Obtiene texto del código |
| `hasRemaining` | `(Activity): boolean` | ¿Quedan escaneos en la cuota? (doc 03) |
| `getRemaining` | `(Activity): int` | Nº de escaneos restantes (doc 03) |
| `getLastError` | `(): String` | Último error |
| `lI` | `(): boolean` | Valida que la lista de cuadros no tenga nulos |

### Hallazgo 2 — `pc()` es nativo: muro para el análisis estático del DEX

`com.personalcode.Azf.pc(...)` se decompila con el marcador **`native_method`** y **cuerpo
vacío**: es un método `native`. Es decir, el decode del QR, su posible descifrado y la
verificación criptográfica **no están en el DEX**, sino en las librerías nativas del SDK:

```
lib/arm64-v8a/libO0p.so      ← nombre ofuscado
lib/arm64-v8a/libl1I.so      ← nombre ofuscado
lib/arm64-v8a/libimage_processing_util_jni.so   ← CameraX (Google)
lib/arm64-v8a/libpl_droidsonroids_gif.so        ← GIF drawable (UI)
```

Los nombres `libO0p`/`libl1I` aparecen referenciados en el smali (carga nativa). El único
método `lI()` legible solo comprueba que la lista de `byte[]` no contenga elementos nulos
antes de pasarla a `pc()`.

> **Consecuencia:** cómo se valida el QR de la credencial (si verifica una firma digital del
> INE embebida, si descifra un payload, qué algoritmo usa) **no es observable** desde el DEX.
> Determinarlo requeriría desensamblar `libO0p.so`/`libl1I.so`, lo cual queda **fuera de
> alcance** del análisis estático del DEX. Se documenta como muro; **no se infiere** el
> mecanismo criptográfico sin evidencia.

## Sin dominios ni endpoints

Búsqueda de strings en `classes.dex` (`strings | grep` de `http(s)://`, `.gob.mx`, `.ine.mx`,
`api.`): **no se encontró ningún dominio ni URL de servicio**. Combinado con la ausencia del
permiso `INTERNET` (doc 01), confirma que **no hay comunicación de red** y la validación es
enteramente local. (El único string con URL en el DEX es un enlace a un issue tracker de
Google dentro de CameraX, no un endpoint de la app.)

## OCR complementario

El manifiesto declara `com.google.mlkit.vision.DEPENDENCIES=ocr` y exige **cámara frontal**.
Además del QR, la app usa **OCR de MLKit** para leer el texto impreso de la credencial
(reconocimiento de caracteres), probablemente para contrastar lo impreso con lo codificado
en el QR. El modelo OCR de MLKit puede descargarse vía Google Play Services (fuera del
permiso INTERNET de la app, lo gestiona el framework).

---

## Decompilación del lado DEX (función por función)

Ampliación tras decompilar con REA los paquetes propios y el motor. Clases crudas guardadas
en `decompiled/` (smali, no versionado): `Azf.smali`, `Native.smali`, `ce.smali`, `vz.smali`,
`ResultsActivity.smali`, `LegalsActivity.smali`.

### Carga de la librería nativa (confirmado en código)

`com.personalcode.Azf.<clinit>` carga la librería nativa directamente con `System.loadLibrary`
(no ReLinker, aunque ReLinker está presente para otras):

```smali
.method static constructor <clinit>()V
    const-string v0, "l1I"
    invoke-static {v0}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V
    return-void
.end method
```

→ carga `lib/arm64-v8a/libl1I.so`. `com.scandit.recognition.Native` carga la otra
(`libO0p.so`) en su propio `<clinit>` (ambos nombres referenciados en smali;
`loadLibrary` se invoca desde `Azf`, `Native`, CameraX y la librería de GIF).

### El motor nativo es Scandit (confirmado por el binding JNI)

`com.scandit.recognition.Native` es un **binding JNI estilo SWIG** con **121 métodos
nativos** del SDK **Scandit Barcode Scanner**. Métodos y constantes que lo identifican:

- `sc_recognition_context_new_full(String×9): long` — crea el contexto de reconocimiento.
- `sc_recognition_context_process_frame(long, long, byte[]): long` — **procesa un cuadro de
  cámara** (el `byte[]` que `Azf.pc` recibe como `List<byte[]>`).
- `sc_barcode_fill_data(long, byte[])`, `sc_barcode_get_data_length(long): int` — extraen los
  datos del código leído.
- Simbologías: `SC_SYMBOLOGY_QR`, `SC_SYMBOLOGY_PDF417`, `SC_SYMBOLOGY_DATA_MATRIX`,
  `SC_SYMBOLOGY_AZTEC`, … → confirma que lee **QR** (y otros 2D).
- Estados: incluye `SC_RECOGNITION_CONTEXT_STATUS_LICENSE_KEY_EXPIRED`,
  `…_LICENSE_VALIDATION_FAILED_400/403`, `…_NO_NETWORK_CONNECTION`.

> El puente es: `Azf.pc(Activity, List<byte[]>, boolean)` (nativo) → dentro de `libl1I.so`/
> `libO0p.so` llama a la cadena `sc_recognition_context_*` de Scandit → devuelve el String que
> luego `Azf.l1(String): ce` parsea. La **decodificación y verificación del contenido del QR**
> ocurre dentro del `.so` de Scandit: sigue siendo un **muro** (no se desensambló el nativo);
> lo que SÍ queda confirmado por código es **qué** motor es y **cómo** se le entregan/reciben
> los datos, no el algoritmo interno.

### El modelo de datos de la credencial: clase `ce`

`com.personalcode.Azf.l1(String): ce` convierte el String del escaneo en un objeto `ce` (20
campos `String` con nombres ofuscados). Su `toString` (en `decompiled/ce.smali`, líneas
~1065–1281) revela el mapeo semántico:

```smali
const-string v1, ", cic="
const-string v1, ", vigencia="
const-string v1, ", curp="
const-string v1, ", ocr="
const-string v1, ", seccion="
```

→ el objeto lleva **CIC, CURP, OCR, sección y vigencia** (doc 04). `vz` (binding de
`ResultsActivity`) vuelca estos valores en los TextViews `tvCIC`, `tvCURP`, `tvOCR`.

### Verificación de salida de red en código (no solo por permiso)

Se confirmó **en el código decompilado**, no solo por la ausencia del permiso `INTERNET`:

| Vector de red | Resultado (grep sobre smali) |
|---|---|
| `java.net.Socket` / `URL` / `HttpURLConnection` / `openConnection` | **0 coincidencias** |
| OkHttp (`okhttp3.*`) / `javax.net.ssl` | **0 coincidencias** |
| URLs `http(s)://` propias (fuera de namespaces XML y un issue-tracker de CameraX) | **0** |
| `Intent ACTION_VIEW` / apertura de navegador | **0** |
| `WebView` | 3 clases; la única con carga es `LegalsActivity` → `loadUrl("file:///android_asset/legal.html")` (**asset local**, ver `decompiled/LegalsActivity.smali`) |

> **Conclusión (confirmada por código):** no existe ningún cliente de red ni salida latente en
> el DEX. El WebView solo renderiza un HTML empaquetado. Aunque el SDK Scandit expone estados
> de validación de licencia *online*, la app **no puede ejercerlos**: sin permiso `INTERNET`
> no hay sockets, y no hay código que los abra. La validación de licencia es, por fuerza,
> **offline** (el blob firmado de `license.txt`, doc 03).
