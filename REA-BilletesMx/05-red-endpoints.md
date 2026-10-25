# 05 — Red, TLS y endpoints

El tráfico de red de BilletesMx es escaso: no es una app cliente/servidor, sino una
experiencia de RA mayormente **offline**. Los endpoints que aparecen como literales en la
metadata C# son:

## Endpoints y enlaces (literales verificados)

| URL / plantilla | Esquema | Uso | Nota |
|---|---|---|---|
| `https://texttospeech.googleapis.com/v1/text:synthesize?key=` | HTTPS | **Google Cloud Text-to-Speech** — síntesis de voz para accesibilidad. | Clave API se concatena (`?key=`). Ver [11](11-accesibilidad-tts.md). |
| `https://www.banxico.org.mx` | HTTPS | Sitio oficial. | — |
| `http://www.banxico.org.mx/billetes-y-monedas/en-proceso-retiro-circulacion.html` | **HTTP** | Página informativa (billetes en retiro). | **Texto claro**. |
| `http://www.banxico.org.mx/portales-de-usuarios/usuarios-billetes-monedas-ban.html` | **HTTP** | Página informativa (usuarios). | **Texto claro**. |
| `https://play.google.com/store/apps/details?id=` | HTTPS | Enlace a la ficha de Play (calificar / otras apps). | — |
| `itms-apps:itunes.apple.com/app/` | itms-apps | Enlace a App Store (variante iOS del proyecto). | — |
| `http://itween.pixelplacement.com` | HTTP | **Metadato de la librería iTween** (no se solicita en red). | Marcador de la lib de animación. |
| `https://localhost/` | HTTPS | Base local (EasyAR/Unity web requests internas). | No sale a Internet. |

## Endpoints hallados en el assetbundle (`data.unity3d`)

Al extraer `data.unity3d` (UnityPy, 13,722 objetos) aparecen más URLs:

| URL | Tipo |
|---|---|
| `https://api.uca.cloud.unity3d.com/v1/events` | Telemetría Unity (analytics) — ver [08](08-telemetria-ads.md) |
| `https://cdp.cloud.unity3d.com/v1/events` | Telemetría Unity (diagnóstico) |
| `https://perf-events.cloud.unity3d.com` | Telemetría Unity (rendimiento) |
| `https://engine-data.services.api.unity.com/v1/stack-traces` | Telemetría Unity (crash) |
| `https://config.uca.cloud.unity3d.com`, `https://engine-data.unity3d.com`, `https://dashboard.unity3d.com` | Config/panel Unity |
| `https://www.facebook.com/DivulgacionBanxico` | Red social oficial |
| `https://www.twitter.com/Banxico` | Red social oficial |
| `https://www.youtube.com/user/canalbanxico` | Red social oficial |
| `https://www.banxico.org.mx` | Sitio oficial |

## Descarga del catálogo

El dump tipado (`NetworkController`, ver [03](03-codigo-csharp.md)) muestra el servicio de
catálogo:

- Campos `readonly string`: **`hostUrl`**, **`wsAppName`**, **`wsAppNameImg`**,
  **`wsAppNameImgElem`** (base + rutas del web-service).
- Catálogo remoto: **`/billetesMX.json`** (vs. local `bills.json`).
- Métodos: `GetActiveBills(url)`, `IsVersionCompatible()` / `GetIsVersionCompatibleIE(platform, versionNumber)`,
  `GetLocalDate()`, `AreNewData(date)` / `AreNewDataIE`, `GetNewData(date)` / `GetNewDataIE`,
  `LoadLocalJSONAndSaveIt()`, `CheckAndReplaceData(newJSON)` → comprueba versión y fecha,
  descarga `billetesMX.json` si hay datos nuevos y reemplaza el catálogo local.

El valor de `hostUrl`/`wsAppName*` **no** aparece como literal independiente ni en la
metadata ni en los 13,722 objetos del bundle (se concatena o se serializa). **Lo único
seguro: no hay CDN de terceros** — los únicos hosts en todo el binario son
`banxico.org.mx`, `localhost`, `googleapis.com` (TTS), `play.google.com` e `itween`
(marcador). El host del catálogo es, por tanto, infraestructura de **banxico.org.mx** o se
compone en runtime; confirmarlo requiere captura de red ([14](14-analisis-dinamico.md)).

## TLS / seguridad de transporte

- **No** hay `android:networkSecurityConfig` en el manifiesto → la app usa la **política
  por defecto** de la plataforma. En `targetSdk=36`, el default **bloquea cleartext
  (HTTP)** salvo excepción explícita, que aquí **no existe**.
- Consecuencia: los dos enlaces `http://www.banxico.org.mx/…` **fallarían** si se
  intentaran abrir dentro de la app vía red directa en Android 9+; en la práctica son
  enlaces que se abren en el **navegador externo** (ahí sí cargan). No son llamadas de
  API, son páginas web informativas. El impacto de seguridad es bajo pero es una
  **inconsistencia de higiene** (enlaces oficiales deberían ser `https://`; el sitio de
  Banxico ya sirve HTTPS).
- Google TTS va por **HTTPS**. Correcto.

## Conclusión

Superficie de red mínima y mayormente correcta (HTTPS para Google TTS y el sitio
principal). Dos puntos a pulir: los enlaces `http://` en claro a `banxico.org.mx` y la
dependencia de un servicio de **Google** (TTS) para una función de accesibilidad de una
app de gobierno — ambos tratados en [11](11-accesibilidad-tts.md) y
[12](12-privacidad-hallazgos.md).
