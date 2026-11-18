# Arquitectura — nativa R8, módulos

## Pila (confirmada)

- **Nativa Android (Kotlin/Java).** 1 `classes.dex`, 2007 clases, única librería nativa
  `libblur.so` (efecto de desenfoque de UI). **Sin** Flutter (`flutter_assets`), **sin** Unity
  (`il2cpp`), **sin** .NET (`assembly-store`), **sin** bundle web (`assets/www`), **sin**
  React Native. App Android convencional.
- **Ofuscada con R8**: nombres de métodos/campos minificados (`M1`, `N1`, `f6544q0`, helpers
  `o4.b`/`o4.c`). Los Fragments/Activities propios conservan nombres legibles por paquete.

## Módulos propios (`mx.org.inegi.denuemv.*`)

| Paquete / clase | Función |
|---|---|
| `ficha.FichaFragment` | ficha del establecimiento en un **BottomSheet** (16 campos de UI, 20 inner classes) |
| `ficha.StreetViewActivity` | vista de calle (Google Street View) del establecimiento |
| `ficha.GaleriaActivity` | galería de fotos |
| `ar.ArActivity`, `ar.ficha.ArFichaFragment` | **realidad aumentada** (cámara) para ubicar negocios |
| `widget.MainWidget`, `widget.WidgetService` | widget de pantalla de inicio |
| `favorito.*`, `historial.*` | favoritos e historial de consultas (local) |
| `analytics.google.AnalyticsApplication` | inicialización de **Google Analytics** (doc 03) |

## Flujo (observado)

El usuario busca/navega el mapa → selecciona un establecimiento → `FichaFragment` carga su
ficha (datos públicos del DENUE) desde los endpoints HTTPS del INEGI (doc 03), con opciones de
Street View, galería y AR. Favoritos e historial son locales.

## Límite por ofuscación

El cargador de datos de la ficha está en helpers ofuscados (`o4.b`, `o4.c`), por lo que el
trazado función por función del parseo exacto no es legible sin des-ofuscar. La superficie de
seguridad relevante (permisos, endpoints, TLS, Maps key, telemetría) **sí** es observable y se
documenta en los capítulos siguientes. No se infiere el detalle del parseo; se marca como límite.
