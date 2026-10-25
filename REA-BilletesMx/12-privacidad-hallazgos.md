# 12 — Privacidad y hallazgos consolidados

Síntesis de seguridad y privacidad del análisis **estático**. Severidades en clave
pentest/privacidad; donde hace falta ejecución para confirmar, se marca como **por
verificar (dinámico)**.

## Perfil de privacidad

| Dimensión | Observación |
|---|---|
| Permisos peligrosos | Sólo `CAMERA` (función central) y `READ/WRITE_EXTERNAL_STORAGE` (heredados). |
| Datos sensibles | **No** pide ubicación, micrófono, contactos, teléfono ni fotos modernas. |
| Datos que salen del dispositivo | **Telemetría de Unity** (analytics/diagnóstico/rendimiento/crash — endpoints activos, ver [08](08-telemetria-ads.md)); catálogo `bills.json` por red. |
| Funcionamiento | RA **offline**; sin cuentas, sin login, sin PII observable. |
| Publicidad | Sin anuncios activos; residuo de Unity Ads/`AD_ID` (que sí permite `AD_ID` junto a analytics). |
| Clave Google TTS | Campo `m_GoogleTTSAPIKey` **vacío**; **ninguna clave embebida** (barrido de 13,722 objetos). |

Perfil **conservador** en permisos, con un matiz: la **telemetría de Unity está activa**.

## Hallazgos

| # | Hallazgo | Severidad | Doc |
|---|---|---|---|
| H1 | **Telemetría de Unity configurada activa** (`analytics-config.json`: analytics + performance habilitados, `player_opted_out=false`, `limit_user_tracking=false`; endpoints UCA/CDP/perf/crash; `AD_ID`). **Matiz dinámico:** en ~5.5 min con Frida **no se observó transmisión** (ningún socket); capacidad presente, envío no confirmado. Riesgo latente para una app de gobierno. | **Media→Baja (latente)** | [08](08-telemetria-ads.md), [14](14-analisis-dinamico.md) |
| H2 | **Certificado de firma SHA1withRSA** (autofirmado por Banco de México, 2012→2040). Algoritmo de autofirma débil; clave RSA-2048 adecuada. | Baja | [10](10-firma-recursos.md) |
| H3 | **Enlaces en texto claro** `http://www.banxico.org.mx/…` (páginas informativas, no API). | Baja | [05](05-red-endpoints.md) |
| H4 | **Residuo Unity Ads / permiso `AD_ID`** innecesario para una app sin publicidad. | Baja (higiene) | [08](08-telemetria-ads.md) |
| H5 | **`READ/WRITE_EXTERNAL_STORAGE` sin `maxSdkVersion`** → amplios en SDK ≤28 (residuo de plantilla Unity). | Baja | [01](01-manifiesto.md) |
| H6 | **Licencia EasyAR embebida** (token Base64 de 1,016 chars en `data.unity3d`). Ligada al package name por diseño; no es secreto de servidor. | Informativo | [04](04-easyar-ar.md) |
| H7 | **Placeholders de EasyAR Cloud** (`--YOUR CLOUD KEY HERE--`): Cloud Recognition **no** se usa (RA local). | Informativo | [04](04-easyar-ar.md) |
| H8 | **Sin clave de Google embebida**: el campo `m_GoogleTTSAPIKey` va vacío; 0 claves `AIza` en 13,722 objetos. (Hallazgo **positivo**.) | Positivo | [11](11-accesibilidad-tts.md) |

## Fortalezas

- **Superficie de ataque Android mínima**: un solo componente exportado (el launcher),
  sin deep links, providers, services ni receivers propios (ver [01](01-manifiesto.md)).
- **RA local/offline**: sin dependencia de nube para la función principal; sin enviar
  imágenes de cámara a terceros (EasyAR local).
- **Sin SDKs de rastreo de terceros** (Firebase, atribuidores, crash-reporting externos).
- **TLS correcto** para el servicio que sí usa (Google TTS por HTTPS).

## Recomendaciones (para Banxico, si aplica)

1. **Desactivar Unity Analytics/UGS** si no se usa, o **declararlo** en el aviso de
   privacidad; quitar `AD_ID` y las activities `AdUnit*` para no ligar un ID de publicidad
   a la telemetría. (H1, H4)
2. Reemitir el certificado con **SHA256withRSA**. (H2)
3. Cambiar los enlaces informativos a **HTTPS**. (H3)
4. Acotar los permisos de almacenamiento con `maxSdkVersion` o eliminarlos si Unity no los
   necesita en runtime. (H5)
5. Mantener el modelo actual de accesibilidad **sin clave Google embebida** (usar TTS
   nativo de Android); si se activa Google TTS, restringir la clave por paquete+firma. (H8)

## Límites de este análisis

Se cubrió **estático completo** (incl. `dump.cs` tipado vía Il2CppDumper) **y dinámico de
arranque** en dispositivo real (ver [14](14-analisis-dinamico.md)). Lo que **falta**, por
ser un dispositivo **sin root** con app `targetSdk 36`:

- **Cuerpos** decompilados método a método (requiere Ghidra + `script.json`; ver [09](09-nativo-so.md)).
- **Captura de red TLS** (host real del catálogo, petición de Google TTS): necesita root /
  Frida / APK reempaquetado con CA de usuario, o emulador rooteado con mitmproxy.
- **Lectura del sandbox** `/data/data` (sin `run-as`; app no debuggable).
