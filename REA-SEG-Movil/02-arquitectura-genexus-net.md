# Arquitectura — GeneXus Smart Devices sobre .NET

## Señales de pila (con evidencia)

| Señal | Implicación |
|---|---|
| Firma `CN=Artech Genexus, O=Artech` | app generada con **GeneXus** (plataforma low-code de Artech) |
| Package `com.artech.sdsigueme.*` | `artech` = runtime de GeneXus Smart Devices |
| Launcher `crc6434…MainActivity` | **Java Callable Wrapper** de Xamarin/.NET → runtime .NET |
| `assets/appsettings.json` | convención de configuración **.NET** |
| Azure AD B2C vía MSAL (doc 03) | cliente de identidad de Microsoft (.NET) |
| Strings DASH-IF / aomedia / Apple HLS en el DEX | **ExoPlayer** (reproducción de video — cursos) |

Conclusión: **GeneXus para Smart Devices generando una app .NET (Xamarin/MAUI)**. GeneXus
define las pantallas y la lógica; el build produce el runtime .NET + los wrappers Java.

## Dónde vive la lógica (y el límite)

- En una app GeneXus SD/.NET, la lógica de negocio (procedimientos, SDT, llamadas a los
  servicios REST de los `Hosts`) la **genera GeneXus** y corre en el runtime .NET, no como
  clases Java legibles en el DEX. El DEX contiene el host (Activities wrapper, bridges).
- **Las `.dll`/assemblies .NET no se localizaron** en el APK base ni en los splits disponibles
  (`config.armeabi_v7a`, densidad, idiomas). Probablemente residen en un split de arquitectura
  no presente en el bundle descargado (el `assembly-store`/arch split). Se documenta como
  **límite**, no se afirma su contenido.
- Lo que **sí** es plenamente observable y rico es la **configuración** (`appsettings.json`):
  identidad y backends completos (doc 03–04). Para una app así, esa config es el mapa de
  arquitectura más valioso recuperable sin ejecutar.

## ExoPlayer

El DEX trae los marcadores de ExoPlayer/DASH (`dashif.org`, `aomedia.org/emsg`, HLS de Apple) →
la app reproduce **video** (coherente con "cursos"/"escuela de padres"). No es una app híbrida
WebView ni Flutter/Unity.
