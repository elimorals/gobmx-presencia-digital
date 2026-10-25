# 02 · Arquitectura — plataforma Veritran (low-code)

## El dato central

La app propia tiene **dos clases** en su namespace (`gob.bancodelbienestar.bcobienapp.R` y
`...VTCommonActivity`). Todo lo demás (16,355 clases) es el runtime de **Veritran**, una plataforma
comercial low-code para apps de banca. Evidencia de paquetes smali:

```
net/veritran/vtuserapplication      com/veritran/vtcrypto
net/veritran/contract               com/veritran/xpressplug/VTFlowActivity
net/veritran/monitor                com/veritran/ui/interfaces/service/VTBackgroundActionService
net/veritran/android_vtsecuritylibrary   (RASP, doc 04)
net/veritran/{commons,smart,component,utils}
```

## Qué implica para la ingeniería inversa

En una app Veritran, el APK es un **intérprete genérico**; la app concreta (pantallas, navegación,
reglas, llamadas a servicios) se define en un formato propio de Veritran que:

- viaja **empacado** como `.szip` en `assets/` (`njhpdqdxvc.szip`, `xmsqibvoii.szip`), y/o
- se **descarga/actualiza** desde el gateway Veritran en runtime, y
- se **ejecuta con un motor Duktape** (JavaScript embebido): `libduktape.so`, 112 referencias a
  `Duktape` en smali; los nombres `njhpdqdxvc`/`xmsqibvoii` (200/91 refs) son los módulos nativos
  que cargan/descifran esos payloads.

**Consecuencia:** la lógica de negocio **no es decompilable como Kotlin/Java**. JADX muestra el
intérprete Veritran, no las pantallas del banco. Recuperar los flujos reales exige:
1. Desempacar los `.szip` (formato Veritran, probablemente cifrado por los `.so` random), o
2. Análisis **dinámico** (hook del motor Duktape / del cargador Veritran) — complicado por el RASP (doc 04).

Esto se afirma por la estructura observada; **no** se desempacó el `.szip` en este análisis estático.

## Componentes del runtime (observados)

| Clase | Rol (inferido del nombre) |
|---|---|
| `VTCommonActivity` / `VTSpecialStartActivity` | Actividades contenedor / arranque |
| `com.veritran.xpressplug.VTFlowActivity` | Ejecutor de "flows" (pantallas) Veritran |
| `VTBackgroundActionService` | Acciones en segundo plano |
| `net.veritran.monitor` | Telemetría/monitoreo del runtime |
| `net.veritran.contract` | Contratos de servicio (definición de llamadas backend) |
| `com.veritran.vtcrypto` | Primitivas cripto del runtime (doc 05) |
| `net.veritran.android_vtsecuritylibrary` | RASP / seguridad (doc 04) |

## Nota de proveedor

Veritran es una plataforma de un tercero (fintech). La postura de seguridad de la app depende en
gran medida de la configuración de Veritran y de su backend, no sólo del banco. El contraste con
Licencia Federal Digital (doc del REA vecino) es total: aquí hay una plataforma bancaria endurecida.
