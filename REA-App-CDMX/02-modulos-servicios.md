# 02 — Módulos y servicios de la super-app

App CDMX no es una app de un dominio: es un **contenedor de ~24 servicios** ciudadanos,
cada uno en su sub-paquete `mx.gob.cdmx.adip.<modulo>`. El núcleo (`.apps`) arranca el
Splash y enruta a cada módulo; `.utils` y `.llave` son transversales. El reparto por nº de
componentes en el manifiesto da la escala relativa de cada servicio.

## Catálogo de módulos

| Módulo (`.`) | Servicio | Notas de evidencia |
|---|---|---|
| `apps` | Núcleo, Splash, contenedor, Mundial 2026 | `SplashActivity` (LAUNCHER), `app.cdmx.gob.mx/Mundial/*` |
| `pagos` | Pasarela de pagos de trámites | 13 componentes; integra Mercado Pago (doc 06) |
| `mitaxi` | Mi Taxi — app del pasajero | deeplink `appcdmx://mitaxi`, `UserHomeActivity` exportada |
| `mitaxioperador` | Mi Taxi — app del chofer | `Taxi/Driver`, `Taxi/Trips`, `Taxi/Route` (doc 03) |
| `seguridad` | Seguridad ciudadana | widget `policia_cuadrante`, enlaza C5 |
| `movilidadintegrada` | Transporte público integrado | rutas, Metrobús (`MetrobusArrivalTime`), incidencias, horarios |
| `clinicacondesa` | Clínica Condesa (VIH/ITS, salud sexual) | endpoint `ClinicaCondesa/` |
| `documentosdigitales` | Documentos oficiales digitales | widget `documentos_digitales`, `DigitalDocuments/` |
| `tarjetavirtualmi` | Tarjeta virtual "Mi" | widget `tarjeta_mi`; tarjeta de transporte (doc 05) |
| `recargami` | Recarga de la tarjeta Mi | pago vía Mercado Pago (doc 06) |
| `puntoswifi` | Puntos de WiFi gratuito | widget `wifi_hotspot` (C5 / Internet para Todos) |
| `culturaentretenimiento` | Cultura y entretenimiento | Activity exportada, `CulturaEntretenimiento/` |
| `botonpanico` | Botón de pánico | widget `boton_panico`, usa ubicación en 2º plano |
| `locatel` / `locatelreport` | LOCATEL (chat y reportes) | widget `chat_locatel`, `Locatel/Chat`, `Locatel/Reports` |
| `denunciadigital` | Denuncia digital | `DigitalComplaint/`, aviso en `denunciadigital.cdmx.gob.mx` |
| `calidadaire` | Calidad del aire | `AirQuality/`, `Weather/` |
| `anticorrupcion` | Canal anticorrupción | `AnticorruptionComplaint/` |
| `reporteciudadano` | Reporte ciudadano (311) | `CitizenReport/ComplaintHistory` |
| `garagedigital` | Garage digital | vehículos del usuario (`Users/Vehicles`), verificación |
| `saludutopias` | Salud en UTOPÍAS | deeplink `appcdmx://utopias` |
| `saludbienestar` | Salud y bienestar | — |
| `llave` | **Llave CDMX** (identidad única) | SSO transversal (doc 04) |
| `utils` | Utilidades compartidas | `LlavePreferencesManager`, modelos comunes |

## Observaciones de arquitectura

- **Un binario, muchos equipos.** La diversidad de sub-paquetes (y de estilos de modelo:
  `…llave.model`, `…locatel.model.report`, `…locatelreport.model.report` — hay `LlaveData`
  duplicado en tres módulos) sugiere varios equipos integrando contra un núcleo común. La
  duplicación de DTOs es deuda típica de super-apps.
- **Servicios de alto impacto social** conviven con telemetría comercial (Firebase
  Analytics, AdServices). Botón de pánico, denuncia anónima y canal anticorrupción están
  en el **mismo proceso** que Analytics/Crashlytics (doc 08) — a considerar para un modelo
  de amenazas de la privacidad del denunciante.
- **Widgets como accesos rápidos** a los servicios más urgentes (pánico, LOCATEL, policía
  de cuadrante, tarjeta de transporte, WiFi, documentos, taxi).
- **Mundial 2026.** El núcleo incluye una sección de resultados en vivo
  (`app.cdmx.gob.mx/Mundial/{Livescoretournament, Matchcenter, Standings, Topscore,
  Squadtournament}`), funcionalidad de temporada dentro de la app de servicios.
