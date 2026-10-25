# 01 — Manifiesto, permisos y superficie de ataque

Fuente: `AndroidManifest.xml` decodificado con apktool 3.0.3 (REA `decode_android_resources`).
Copia íntegra en [`evidence/AndroidManifest.xml`](evidence/AndroidManifest.xml).

## Permisos

| Permiso | Protección | Por qué está / nota |
|---|---|---|
| `android.permission.CAMERA` | dangerous | **Imprescindible**: la RA de EasyAR usa la cámara para reconocer el billete. |
| `android.permission.INTERNET` | normal | Descarga de catálogo (`bills.json`), Google TTS, enlaces Banxico. |
| `android.permission.ACCESS_NETWORK_STATE` | normal | Comprobar conectividad antes de descargar. |
| `android.permission.VIBRATE` | normal | Retroalimentación háptica al reconocer un billete. |
| `android.permission.READ_EXTERNAL_STORAGE` | dangerous | Heredado; **sin** `maxSdkVersion`, por lo que es amplio en dispositivos SDK 23–32. |
| `android.permission.WRITE_EXTERNAL_STORAGE` | dangerous | Igual; en SDK ≤28 escribe fuera del sandbox. Residuo típico de Unity. |
| `com.google.android.gms.permission.AD_ID` | normal | Lo declara el SDK de **Unity Ads/Services** empaquetado (ver [08](08-telemetria-ads.md)). |

**No** declara: ubicación (fina/gruesa), micrófono (`RECORD_AUDIO`), contactos,
`READ_MEDIA_IMAGES`, teléfono, SMS, Bluetooth ni `QUERY_ALL_PACKAGES`. Perfil
razonable para una app de RA; los dos `*_EXTERNAL_STORAGE` sin `maxSdkVersion` son lo
único expansivo y provienen de la plantilla de Unity, no de lógica propia.

## Características (`uses-feature`)

- `glEsVersion=0x30000` → **OpenGL ES 3.0 requerido** (render Unity).
- `android.hardware.vulkan.version` **no requerido** (fallback a GLES).
- Toda la familia `touchscreen*` marcada `required="false"` (plantilla Unity).
- **No** declara `android.hardware.camera` como feature → el filtro de Play no excluye
  dispositivos sin cámara pese a necesitarla para la función principal (lo gestiona en
  runtime).

## Componentes

### Exportados

| Componente | Tipo | Exported | Observación |
|---|---|---|---|
| `com.unity3d.player.UnityPlayerActivity` | activity | **true** | Único punto de entrada. `LAUNCHER`+`MAIN`, `singleTask`, `landscape`, `hardwareAccelerated=false`. Exportada **sólo** por ser el lanzador (obligatorio). Sin filtros de deep link ni esquemas URI. |

### No exportados (internos de Unity Ads)

`com.unity3d.services.ads.adunit.AdUnitActivity`,
`AdUnitTransparentActivity`, `AdUnitTransparentSoftwareActivity`,
`AdUnitSoftwareActivity` — todas **sin** `exported` (false por defecto) y **sin**
`intent-filter`. Pertenecen al SDK de Unity Ads; sólo son invocables desde dentro del
proceso.

### Ausentes

**Sin** `<provider>` (ni `FileProvider`), **sin** `<service>` propio, **sin**
`<receiver>`, **sin** `android:networkSecurityConfig`, **sin** `android:debuggable`,
**sin** `allowBackup` explícito (toma el default de la plataforma).

## `meta-data` relevantes

| Clave | Valor | Lectura |
|---|---|---|
| `unityplayer.SkipPermissionsDialog` | `true` | Unity no muestra su propio diálogo de permisos; los pide la app en runtime. |
| `unity.strip-engine-code` | `true` | **Code stripping activo** → IL2CPP elimina código no usado (menos tipos en metadata). |
| `unity.build-id` | `7a9c4d9c-5ea9-4b84-af33-d5f995070945` | Identificador de build Unity. |
| `com.android.stamp.type` | `STAMP_TYPE_DISTRIBUTION_APK` | Firmado/distribuido vía Google Play (Play App Signing). |
| `com.android.vending.splits` | `@xml/splits0` | App por splits (ABI); este XAPK trae base + arm64-v8a. |
| `appCategory` | `game` | Unity marca la app como juego. |

## Superficie de ataque — conclusión

La superficie **expuesta a otras apps** es prácticamente nula:

- Un solo componente exportado y es el lanzador estándar de Unity, sin `intent-filter`
  adicionales, sin datos de entrada vía Intent, sin esquemas propios.
- Ningún `provider`/`service`/`receiver` exportado → nada que invocar desde fuera.
- Sin deep links → no hay parsing de URIs externas que auditar.

El riesgo, si lo hubiera, está en la **capa nativa/C#** (reconocimiento AR, descarga de
catálogo, llamada a Google TTS) y en los **permisos de almacenamiento heredados**, no en
la superficie de componentes Android. Se analiza en [03](03-codigo-csharp.md),
[04](04-easyar-ar.md), [05](05-red-endpoints.md) y [11](11-accesibilidad-tts.md).
